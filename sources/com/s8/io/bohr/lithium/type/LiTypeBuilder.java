package com.s8.io.bohr.lithium.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.lithium.codebase.LiCodebaseBuilder;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldBuilder;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;

/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LiTypeBuilder {



	private final Class<?> baseType;

	private final boolean isVerbose;




	private boolean isBuildable = false;


	private Map<String, LiFieldBuilder> fieldBuildersByName;



	private Constructor<?> constructor;



	private String name;



	private int nVertexReferences;




	private S8ObjectType typeAnnotation;







	public LiTypeBuilder(Class<?> type, boolean isVerbose) {
		super();


		this.baseType = type;
		this.isVerbose = isVerbose;
	}







	/**
	 * 
	 * @param name
	 * @return
	 */
	public LiFieldBuilder getFieldBuilder(String name) {
		return fieldBuildersByName.get(name);
	}

	/**
	 * 
	 * @param name
	 * @param fieldBuilder
	 */
	public void setFieldBuilder(String name, LiFieldBuilder fieldBuilder) {
		fieldBuildersByName.put(name, fieldBuilder);
	}




	/**
	 * 
	 * <h1>CALLED SECOND</h1>
	 * @param level
	 * @param factory
	 * @throws BohrTypeBuildingException 
	 * @throws BkException
	 * @throws Exception 
	 */
	public boolean process(LiCodebaseBuilder codebaseBuilder) throws LiBuildException {

		// retrieve typeAnnotation once and for all
		typeAnnotation = baseType.getAnnotation(S8ObjectType.class);

		// check status
		check();

		resolveName();

		initializeAttributes();

		/* <constructor> */
		retrieveConstructor();
		/* </constructor> */

		// recursive extraction
		crawl(baseType, codebaseBuilder);
		/* </fields> */

		return isBuildable;
	}





	private void check() {

		int mods = baseType.getModifiers();

		/**
		 * Determine if abstract
		 */


		// is static ?
		if(!baseType.isMemberClass() || (baseType.isMemberClass() && Modifier.isStatic(mods))) {
			if(LiS8Object.class.isAssignableFrom(baseType)) {
				isBuildable = true;
			}
			else if(isVerbose) {
				System.out.println("Type "+baseType+" is not a valid subType for S8Object");
			}
		}
		else if(isVerbose) {
			System.out.println("Type "+baseType+" rejected as not static");
		}
	}



	/**
	 * <h1>CALLED FIRST</h1>
	 * <p>Reslve name by checking direct case and nested class case.</p>
	 * @throws LithTypeBuildException
	 */
	private void resolveName() throws LiBuildException {
		if(isBuildable) {
			/* <code> */

			if(typeAnnotation==null) {
				throw new LiBuildException("Missing type annotation", baseType);
			}

			Class<?> enclosingType = baseType.getEnclosingClass();
			if(enclosingType!=null) {
				S8ObjectType enclosingTypeAnnotation = enclosingType.getAnnotation(S8ObjectType.class);
				if(enclosingTypeAnnotation==null) {
					throw new LiBuildException("Missing enclosing type annotation", baseType);
				}	
				name = typeAnnotation.name()+'@'+enclosingTypeAnnotation.name();
			}
			else {
				name = typeAnnotation.name();
			}
		}

	}



	/**
	 * <p>
	 * Initialize all attributes:
	 * </p>
	 * <ul>
	 * <li>fields</li>
	 * </ul>
	 */
	public void initializeAttributes() {
		/* <fields> */
		if(isBuildable) {
			fieldBuildersByName = new HashMap<String, LiFieldBuilder>();	
		}
	}


	/**
	 * lookUp this level 
	 * 
	 * @param type
	 * @param factory
	 * @throws LithTypeBuildException
	 * @throws BohrTypeBuildingException 
	 * @throws LthSerialException 
	 */
	public void crawl(Class<?> type, LiCodebaseBuilder codebaseBuilder) throws LiBuildException {

		/* <sub-types> */

		/* --> Always to be done, even if not buildable */
		Class<?>[] subTypes = typeAnnotation.sub();
		if(subTypes!=null) {
			for(Class<?> subType : subTypes) {
				codebaseBuilder.pushObjectType(subType);
			}
		}
		/* </sub-types> */


		/* <nested-types> */

		/* --> Always to be done, even if not buildable */
		Class<?>[] nestedTypes = type.getNestMembers();
		int nNestedTypes = nestedTypes.length;
		for(int i=1; i<nNestedTypes; i++) {
			codebaseBuilder.pushObjectType(nestedTypes[i]);
		}
		/* </nested-types> */



		// search superclass
		/* --> Always to be done, even if not buildable */
		Class<?> superType = type.getSuperclass();
		if(superType!=null && superType.isAnnotationPresent(S8ObjectType.class)) {

			// append superclass to type to be built and added to the Mapping
			codebaseBuilder.pushObjectType(superType);

			// perform extract on supertype
			crawl(superType, codebaseBuilder);
		}

		// search trough all declared field at this level
		if(isBuildable) {
			/* <fields> */
			Field[] declaredFields = type.getDeclaredFields();
			if(declaredFields!=null) {
				for(Field field : declaredFields) { onField(codebaseBuilder, field); }	
			}
			/* </fields> */



			/* <methods> */
			Method[] declaredMethods = type.getDeclaredMethods();
			if(declaredMethods!=null) {
				for(Method method : declaredMethods) { onMethod(codebaseBuilder, method); }
			}
			/* </methods> */	
		}

		/* </subTypes> */
	}


	/**
	 * 
	 * @return
	 */
	public Class<?> getRawType() {
		return baseType;
	}



	public void retrieveConstructor() throws LiBuildException {
		if(isBuildable) {
			try {
				/*
				 * retrieve constructor with no parameters 
				 */
				constructor = baseType.getConstructor(new Class<?>[]{});

			} 
			catch (NoSuchMethodException | SecurityException e) {
				throw new LiBuildException("missing public constructor with parameters", baseType);
			}
			catch (ClassCastException e) {
				throw new LiBuildException("Must inherit DkObject", baseType);
			}	
		}
	}



	public void filter(Field field) {
		/*
		// check ownership
		S8Owner ownerAnnotation = field.getAnnotation(S8Owner.class);
		if(ownerAnnotation!=null) {
			// TODO objectType.
		}
		 */
	}



	/**
	 * <p>
	 * append field
	 * </p>
	 * <p>
	 * <b>Note that testing if field must be collected with <code>isCollected</code>
	 * must be performed prior to using this method</b>
	 * </p>
	 * 
	 * @param field
	 * @param factory
	 * @throws LthSerialException 
	 * @throws BkException
	 */
	private void onField(LiCodebaseBuilder contextBuilder, Field field) throws LiBuildException {

		S8Field fieldAnnotation = field.getAnnotation(S8Field.class);
		if(fieldAnnotation!=null) {
			String name = fieldAnnotation.name();


			// non-mixing check

			LiFieldBuilder fieldBuilder = fieldBuildersByName.get(name);


			// aggregate with an already existing field
			if(fieldBuilder!=null) {

				fieldBuilder.attachField(field);
			}
			// create new field
			else {
				fieldBuilder = contextBuilder.getGphFieldFactory().captureField(field);
				LiFieldProperties props = fieldBuilder.properties;
				switch(props.getEmbeddedTypeNature()) {

				case S8_OBJECT : 
					contextBuilder.pushObjectType(props.getEmbeddedType());
					break;

				case S8_ROW:
					contextBuilder.pushRowType(props.getEmbeddedType());
					break;

				default: break; // do nothing
				}

				// apply filter before appending fields
				filter(field);

				// add fields
				fieldBuildersByName.put(name, fieldBuilder);	
			}
		}
		// else: do nothing (field is skipped)
	}



	public void onMethod(LiCodebaseBuilder codebaseBuilder, Method method) throws LiBuildException {

		boolean hasBeenCaptured = false;

		S8Getter getterAnnotation = method.getAnnotation(S8Getter.class);
		if(getterAnnotation !=null) {

			String name = getterAnnotation.name();
			LiFieldBuilder fieldBuilder = fieldBuildersByName.get(name);
			if(fieldBuilder!=null) { // update
				fieldBuilder.attachGetMethod(method);
			}
			else { // create
				fieldBuilder = codebaseBuilder.getGphFieldFactory().captureGetter(method);

				// add fields
				fieldBuildersByName.put(name, fieldBuilder);	

				// further crawl...
				LiFieldProperties properties = fieldBuilder.properties;

				switch(properties.getEmbeddedTypeNature()) {
				case S8_OBJECT : 
					codebaseBuilder.pushObjectType(properties.getEmbeddedType());
					break;

				case S8_ROW:
					codebaseBuilder.pushRowType(properties.getEmbeddedType());
					break;

				default: break; // do nothing
				}
			}
			hasBeenCaptured = true;
		}

		S8Setter setterAnnotation = method.getAnnotation(S8Setter.class);
		if(!hasBeenCaptured && setterAnnotation!=null) {

			String name = setterAnnotation.name();
			LiFieldBuilder fieldBuilder = fieldBuildersByName.get(name);
			if(fieldBuilder!=null) {
				fieldBuilder.attachSetMethod(method);
			}
			else {

				fieldBuilder = codebaseBuilder.getGphFieldFactory().captureSetter(method);
				LiFieldProperties properties = fieldBuilder.properties;
				// add fields
				fieldBuildersByName.put(name, fieldBuilder);

				switch(properties.getEmbeddedTypeNature()) {
				case S8_OBJECT : 
					codebaseBuilder.pushObjectType(properties.getEmbeddedType());
					break;

				case S8_ROW:
					codebaseBuilder.pushRowType(properties.getEmbeddedType());
					break;

				default: break; // do nothing
				}

			}
		}
		else if(hasBeenCaptured && setterAnnotation != null) {
			throw new LiBuildException("Cannot be getter and setter at the same time");
		}
	}



	/**
	 * Post build
	 * @throws LiBuildException 
	 */
	public LiType build() throws LiBuildException {

		if(!isBuildable) {
			throw new LiBuildException("This type is not buildable");
		}

		// generate type
		LiType type = new LiType(baseType);


		type.name = name;

		type.constructor = constructor;

		type.nVertexReferences = nVertexReferences;

		// finalize build
		fieldBuildersByName.forEach((name, builder) -> {
			//builder.build2(this);
		});


		// retrieve nb fields
		int nFields = fieldBuildersByName.size();


		// build map
		Map<String, LiField> fieldsByName = new HashMap<String, LiField>(nFields);
		AtomicInteger ordinator = new AtomicInteger(0);
		fieldBuildersByName.forEach((name, builder) -> {
			try {
				fieldsByName.put(name, builder.build(ordinator.getAndIncrement()));
			} catch (LiBuildException e) {
				if(isVerbose) {
					e.printStackTrace();	
				}
			}
		});
		type.fieldsByName = fieldsByName;


		// build array
		LiField[] fields = new LiField[nFields];
		LiField field;
		for(Entry<String, LiField> entry : fieldsByName.entrySet()) {
			field = entry.getValue();
			fields[field.ordinal] = field;
		}
		type.fields = fields;

		return type;
	}



	public int onVertexReferenced() {
		return this.nVertexReferences++;
	}



	public void subDiscover() {

	}
}
