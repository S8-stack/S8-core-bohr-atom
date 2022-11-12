package com.s8.io.bohr.neodymium.fields.primitives;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldBuilder;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties0T;
import com.s8.io.bohr.neodymium.type.GraphCrawler;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveNdField extends NdField {
	
	

	public static abstract class Prototype extends NdFieldPrototype {

		private Class<?> fieldType;

		public Prototype(Class<?> fieldType) {
			super();
			this.fieldType = fieldType;
		}

		public Class<?> getType(){
			return fieldType;
		}

		public String getKey() {
			return fieldType.getCanonicalName();
		}
		
		
		@Override
		public NdFieldProperties captureField(Field field) {
			if(fieldType.equals(field.getType())) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					
					// types
					NdFieldProperties props = new NdFieldProperties0T(this, NdFieldProperties.FIELD, fieldType);
					props.setFieldAnnotation(annotation);
					return props;
				}
				else {
					return null;
				}
			}
			else {
				return null;  // type is not matching
			}
		}
		
		

		@Override
		public NdFieldProperties captureSetter(Method method) {
			Class<?> setterType = method.getParameters()[0].getType();
			if(fieldType.equals(setterType)) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					
					// types
					NdFieldProperties props = new NdFieldProperties0T(this, NdFieldProperties.METHODS, fieldType);
					props.setSetterAnnotation(annotation);
					return props;
				}
				else {
					return null;
				}
			}
			else {
				return null;  // type is not matching
			}
		}

		@Override
		public NdFieldProperties captureGetter(Method method) {
			Class<?> getterType = method.getReturnType();
			if(fieldType.equals(getterType)) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					
					// types
					NdFieldProperties props = new NdFieldProperties0T(this, NdFieldProperties.METHODS, fieldType);
					props.setGetterAnnotation(annotation);
					return props;
				}
				else {
					return null;
				}
			}
			else {
				return null;  // type is not matching
			}
		}
		
		

		@Override
		public abstract PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties props, NdHandler handler);
		
	}
	
	
	public static abstract class Builder extends NdFieldBuilder {
		
		public Builder(NdFieldProperties properties, NdHandler handler) {
			super(properties, handler);
		}
	}
	
	

	/**
	 * 
	 * @param code
	 * @param field
	 * @param value
	 */
	public PrimitiveNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
	}

	
	public abstract Prototype getPrototype();
	

	

	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		// nothing to collect
	}

	@Override
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		//no blocks to collect
	}
	
	@Override
	public String printType() {
		return getPrototype().getKey();
	}
	
	@Override
	public boolean isValueResolved(NdObject object) {
		return true; // always resolved
	}
	
}
