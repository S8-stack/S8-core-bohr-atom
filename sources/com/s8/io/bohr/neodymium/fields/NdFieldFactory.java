package com.s8.io.bohr.neodymium.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.fields.arrays.BooleanArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.DoubleArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.FloatArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.IntegerArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.LongArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.ShortArrayNdField;
import com.s8.io.bohr.neodymium.fields.arrays.StringArrayNdField;
import com.s8.io.bohr.neodymium.fields.collections.S8ObjectArrayNdField;
import com.s8.io.bohr.neodymium.fields.collections.S8ObjectListNdField;
import com.s8.io.bohr.neodymium.fields.objects.EnumNdField;
import com.s8.io.bohr.neodymium.fields.objects.InterfaceNdField;
import com.s8.io.bohr.neodymium.fields.objects.S8ObjectNdField;
import com.s8.io.bohr.neodymium.fields.objects.S8RefNdField;
import com.s8.io.bohr.neodymium.fields.objects.S8SerializableNdField;
import com.s8.io.bohr.neodymium.fields.primitives.BooleanNdField;
import com.s8.io.bohr.neodymium.fields.primitives.DoubleNdField;
import com.s8.io.bohr.neodymium.fields.primitives.FloatNdField;
import com.s8.io.bohr.neodymium.fields.primitives.IntegerNdField;
import com.s8.io.bohr.neodymium.fields.primitives.LongNdField;
import com.s8.io.bohr.neodymium.fields.primitives.PrimitiveNdField;
import com.s8.io.bohr.neodymium.fields.primitives.ShortNdField;
import com.s8.io.bohr.neodymium.fields.primitives.StringNdField;
import com.s8.io.bohr.neodymium.handlers.FieldNdHandler;
import com.s8.io.bohr.neodymium.handlers.MethodNdHandler;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdFieldFactory {


	/**
	 * mapped
	 */
	public final static PrimitiveNdField.Prototype[] DEFAULT_PRIMITIVES = new PrimitiveNdField.Prototype[] {

					BooleanNdField.PROTOTYPE,
					BooleanArrayNdField.PROTOTYPE,

					ShortNdField.PROTOTYPE,
					ShortArrayNdField.PROTOTYPE,

					IntegerNdField.PROTOTYPE,
					IntegerArrayNdField.PROTOTYPE,

					LongNdField.PROTOTYPE,
					LongArrayNdField.PROTOTYPE,

					FloatNdField.PROTOTYPE,
					FloatArrayNdField.PROTOTYPE,

					DoubleNdField.PROTOTYPE,
					DoubleArrayNdField.PROTOTYPE,

					StringNdField.PROTOTYPE,
					StringArrayNdField.PROTOTYPE
	};


	public final static NdFieldPrototype[] STANDARDS = new NdFieldPrototype[] {

			S8SerializableNdField.PROTOTYPE,
			
			/* must be tested before S8ObjectGphField */
			S8RefNdField.PROTOTYPE,
			//S8TableGphField.PROTOTYPE,

			/* must be tested before S8Struct */
			S8ObjectNdField.PROTOTYPE,

			S8ObjectArrayNdField.PROTOTYPE,
			S8ObjectListNdField.PROTOTYPE,
			EnumNdField.PROTOTYPE,

			// wildcard
			InterfaceNdField.PROTOTYPE
	};


	private Map<String, PrimitiveNdField.Prototype> primitivePrototypes;


	/**
	 * 
	 * @param buildables
	 */
	public NdFieldFactory() {
		super();

		// load default primitives
		primitivePrototypes = new HashMap<String, PrimitiveNdField.Prototype>();

		// add default
		for(PrimitiveNdField.Prototype primitive : DEFAULT_PRIMITIVES) {
			primitivePrototypes.put(primitive.getKey(), primitive);
		}
	}


	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public NdFieldBuilder captureField(Field field) throws NdBuildException {

		// build key
		Class<?> type = field.getType();
		String key = type.getCanonicalName();

		NdFieldProperties props;

		// try to match primitives
		PrimitiveNdField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureField(field))!=null) {
			return primitivePrototype.createFieldBuilder(props, FieldNdHandler.init(field));
		}

		// if not working try other builders
		
		for(NdFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureField(field))!=null) {
				return prototype.createFieldBuilder(props, FieldNdHandler.init(field));
			}
		}

		//DEBUG_analyze(field);

		// no prototypes has been able to capture the field
		throw new NdBuildException("Failed to capture the field ", field);
	}




	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public NdFieldBuilder captureGetter(Method method) throws NdBuildException {

		// build key
		Class<?> type = method.getReturnType();
		String key = type.getCanonicalName();

		
		NdFieldProperties props;
		
		// try to match primitives
		PrimitiveNdField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureGetter(method))!=null) {
			return primitivePrototype.createFieldBuilder(props, MethodNdHandler.initWithGetter(method));
		}

		// if not working try other builders
		for(NdFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureGetter(method))!=null) {
				return primitivePrototype.createFieldBuilder(props, MethodNdHandler.initWithGetter(method));
			}
		}
		// no prototypes has been able to capture the field
		throw new NdBuildException("Failed to capture the getter", method);
	}


	/**
	 * 
	 * @param field
	 * @return
	 * @throws LithTypeBuildException
	 */
	public NdFieldBuilder captureSetter(Method method) throws NdBuildException {

		// build key
		Class<?> type = method.getParameterTypes()[0];
		String key = type.getCanonicalName();

		NdFieldProperties props;

		// try to match primitives
		PrimitiveNdField.Prototype primitivePrototype = primitivePrototypes.get(key);
		if(primitivePrototype!=null && (props = primitivePrototype.captureSetter(method))!=null) {
			return primitivePrototype.createFieldBuilder(props, MethodNdHandler.initWithSetter(method));
		}

		// if not working try other builders
		for(NdFieldPrototype prototype : STANDARDS) {
			if((props = prototype.captureGetter(method))!=null) {
				return primitivePrototype.createFieldBuilder(props,  MethodNdHandler.initWithGetter(method));
			}
		}
		// no prototypes has been able to capture the field
		throw new NdBuildException("Failed to capture the setter", method);
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
