package com.s8.io.bohr.lithium.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.fields.arrays.BooleanArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.DoubleArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.FloatArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.IntegerArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.LongArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.ShortArrayLiField;
import com.s8.io.bohr.lithium.fields.arrays.StringArrayLiField;
import com.s8.io.bohr.lithium.fields.collections.S8ObjectArrayLiField;
import com.s8.io.bohr.lithium.fields.collections.S8ObjectListLiField;
import com.s8.io.bohr.lithium.fields.objects.EnumLiField;
import com.s8.io.bohr.lithium.fields.objects.InterfaceLiField;
import com.s8.io.bohr.lithium.fields.objects.S8ObjectLiField;
import com.s8.io.bohr.lithium.fields.objects.S8RefLiField;
import com.s8.io.bohr.lithium.fields.objects.S8SerializableLiField;
import com.s8.io.bohr.lithium.fields.primitives.BooleanLiField;
import com.s8.io.bohr.lithium.fields.primitives.DoubleLiField;
import com.s8.io.bohr.lithium.fields.primitives.FloatLiField;
import com.s8.io.bohr.lithium.fields.primitives.IntegerLiField;
import com.s8.io.bohr.lithium.fields.primitives.LongLiField;
import com.s8.io.bohr.lithium.fields.primitives.PrimitiveLiField;
import com.s8.io.bohr.lithium.fields.primitives.ShortLiField;
import com.s8.io.bohr.lithium.fields.primitives.StringLiField;
import com.s8.io.bohr.lithium.handlers.FieldLiHandler;
import com.s8.io.bohr.lithium.handlers.MethodLiHandler;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LiFieldFactory {


	/**
	 * mapped
	 */
	public final static PrimitiveLiField.Prototype[] DEFAULT_PRIMITIVES = new PrimitiveLiField.Prototype[] {

					BooleanLiField.PROTOTYPE,
					BooleanArrayLiField.PROTOTYPE,

					ShortLiField.PROTOTYPE,
					ShortArrayLiField.PROTOTYPE,

					IntegerLiField.PROTOTYPE,
					IntegerArrayLiField.PROTOTYPE,

					LongLiField.PROTOTYPE,
					LongArrayLiField.PROTOTYPE,

					FloatLiField.PROTOTYPE,
					FloatArrayLiField.PROTOTYPE,

					DoubleLiField.PROTOTYPE,
					DoubleArrayLiField.PROTOTYPE,

					StringLiField.PROTOTYPE,
					StringArrayLiField.PROTOTYPE
	};


	public final static LiFieldPrototype[] STANDARDS = new LiFieldPrototype[] {

			S8SerializableLiField.PROTOTYPE,
			
			/* must be tested before S8ObjectGphField */
			S8RefLiField.PROTOTYPE,
			//S8TableGphField.PROTOTYPE,

			/* must be tested before S8Struct */
			S8ObjectLiField.PROTOTYPE,

			S8ObjectArrayLiField.PROTOTYPE,
			S8ObjectListLiField.PROTOTYPE,
			EnumLiField.PROTOTYPE,

			// wildcard
			InterfaceLiField.PROTOTYPE
	};


	private Map<String, PrimitiveLiField.Prototype> primitivePrototypes;


	/**
	 * 
	 * @param buildables
	 */
	public LiFieldFactory() {
		super();

		// load default primitives
		primitivePrototypes = new HashMap<String, PrimitiveLiField.Prototype>();

		// add default
		for(PrimitiveLiField.Prototype primitive : DEFAULT_PRIMITIVES) {
			primitivePrototypes.put(primitive.getKey(), primitive);
		}
	}


	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public LiFieldBuilder captureField(Field field) throws LiBuildException {

		// build key
		Class<?> type = field.getType();
		String key = type.getCanonicalName();

		LiFieldProperties props;

		// try to match primitives
		PrimitiveLiField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureField(field))!=null) {
			return primitivePrototype.createFieldBuilder(props, FieldLiHandler.init(field));
		}

		// if not working try other builders
		
		for(LiFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureField(field))!=null) {
				return prototype.createFieldBuilder(props, FieldLiHandler.init(field));
			}
		}

		//DEBUG_analyze(field);

		// no prototypes has been able to capture the field
		throw new LiBuildException("Failed to capture the field ", field);
	}




	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public LiFieldBuilder captureGetter(Method method) throws LiBuildException {

		// build key
		Class<?> type = method.getReturnType();
		String key = type.getCanonicalName();

		
		LiFieldProperties props;
		
		// try to match primitives
		PrimitiveLiField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureGetter(method))!=null) {
			return primitivePrototype.createFieldBuilder(props, MethodLiHandler.initWithGetter(method));
		}

		// if not working try other builders
		for(LiFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureGetter(method))!=null) {
				return primitivePrototype.createFieldBuilder(props, MethodLiHandler.initWithGetter(method));
			}
		}
		// no prototypes has been able to capture the field
		throw new LiBuildException("Failed to capture the getter", method);
	}


	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public LiFieldBuilder captureSetter(Method method) throws LiBuildException {

		// build key
		Class<?> type = method.getParameterTypes()[0];
		String key = type.getCanonicalName();

		LiFieldProperties props;

		// try to match primitives
		PrimitiveLiField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureSetter(method))!=null) {
			return primitivePrototype.createFieldBuilder(props, MethodLiHandler.initWithSetter(method));
		}

		// if not working try other builders
		for(LiFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureGetter(method))!=null) {
				return primitivePrototype.createFieldBuilder(props,  MethodLiHandler.initWithGetter(method));
			}
		}
		// no prototypes has been able to capture the field
		throw new LiBuildException("Failed to capture the setter", method);
	}
	
	
	
	


	/*
	public void DEBUG_analyze(Field field) throws GphTypeBuildException {

		Class<?> type = field.getType();

		System.out.println(type.getAnnotations());
		System.out.println(type.isAnnotationPresent(S8Serial.class));
		System.out.println(S8Serializable.class.isAssignableFrom(type));
		System.out.println(S8SerializableFieldHandler.PROTOTYPE.isCapturing(field));	
	}
	 */

}
