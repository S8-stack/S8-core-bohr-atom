package com.s8.io.bohr.beryllium.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.s8.io.bohr.atom.annotations.S8RowType;
import com.s8.io.bohr.beryllium.codebase.BeCodebaseBuilder;
import com.s8.io.bohr.beryllium.fields.BeField;
import com.s8.io.bohr.beryllium.fields.BeFieldFactory;


/**
 * 
 * @author pierreconvert
 *
 */
public class BeTypeBuilder {
	
	
	
	private final Class<?> baseType;
	
	
	private final BeType type;


	private final boolean isVerbose;
	
	
	public BeTypeBuilder(Class<?> baseType, boolean isVerbose) {
		super();
		this.baseType = baseType;
		type = new BeType(baseType);
		this.isVerbose = isVerbose;
	}

	

	public void build(BeCodebaseBuilder codebaseBuilder) throws BeTypeBuildException {
		
		
		S8RowType typeAnnotation = baseType.getAnnotation(S8RowType.class);
		type.serialName = typeAnnotation.name();
		
		BeFieldFactory fieldFactory = codebaseBuilder.getFieldFactory();
		
		/*
		int modifiers = baseType.getModifiers();
		if(!Modifier.isFinal(modifiers)) {
			throw new BeTypeBuildException("Only final type accepted as S8Record types");
		}
		*/
		
		/* <constructor> */
		try {
			Constructor<?> constructor = baseType.getConstructor(new Class<?>[]{});
			if(constructor==null) {
				throw new BeTypeBuildException(
						"Failed to find a default 0-args constructor for type: "+baseType.getCanonicalName());
			}
			type.constructor = constructor;
		} 
		catch (NoSuchMethodException | SecurityException e) {
			throw new BeTypeBuildException(
					"Failed to find a default 0-args constructor for type: "+baseType.getCanonicalName());
		}
			

		/* </constructor> */
		
		
		/* <fields> */
		BeField fieldHandler;
		for(Field field : baseType.getFields()) {
			if((fieldHandler = fieldFactory.create(field)) != null) {
				type.map.put(fieldHandler.name, fieldHandler);
			}
			else if(isVerbose){
				System.out.println("[BeTypeBuilder] Skip un-annotated field: "+field.getName());
			}
		}
		/* </fields> */	
	}
	
	
	
	public Class<?> getBaseType(){
		return baseType;
	}
	
	public BeType getType() {
		return type;
	}
}
