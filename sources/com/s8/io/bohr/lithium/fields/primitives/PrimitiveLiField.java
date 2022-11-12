package com.s8.io.bohr.lithium.fields.primitives;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldBuilder;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.object.LiObject;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;
import com.s8.io.bohr.lithium.properties.LiFieldProperties0T;
import com.s8.io.bohr.lithium.type.GraphCrawler;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveLiField extends LiField {
	
	

	public static abstract class Prototype extends LiFieldPrototype {

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
		public LiFieldProperties captureField(Field field) {
			if(fieldType.equals(field.getType())) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					
					// types
					LiFieldProperties props = new LiFieldProperties0T(this, LiFieldProperties.FIELD, fieldType);
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
		public LiFieldProperties captureSetter(Method method) {
			Class<?> setterType = method.getParameters()[0].getType();
			if(fieldType.equals(setterType)) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					
					// types
					LiFieldProperties props = new LiFieldProperties0T(this, LiFieldProperties.METHODS, fieldType);
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
		public LiFieldProperties captureGetter(Method method) {
			Class<?> getterType = method.getReturnType();
			if(fieldType.equals(getterType)) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					
					// types
					LiFieldProperties props = new LiFieldProperties0T(this, LiFieldProperties.METHODS, fieldType);
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
		public abstract PrimitiveLiField.Builder createFieldBuilder(LiFieldProperties props, LiHandler handler);
		
	}
	
	
	public static abstract class Builder extends LiFieldBuilder {
		
		public Builder(LiFieldProperties properties, LiHandler handler) {
			super(properties, handler);
		}
	}
	
	

	/**
	 * 
	 * @param code
	 * @param field
	 * @param value
	 */
	public PrimitiveLiField(int ordinal, LiFieldProperties properties, LiHandler handler) {
		super(ordinal, properties, handler);
	}

	
	public abstract Prototype getPrototype();
	

	

	@Override
	public void sweep(LiObject object, GraphCrawler crawler) {
		// nothing to collect
	}

	@Override
	public void collectReferencedBlocks(LiObject object, Queue<String> references) {
		//no blocks to collect
	}
	
	@Override
	public String printType() {
		return getPrototype().getKey();
	}
	
	@Override
	public boolean isValueResolved(LiObject object) {
		return true; // always resolved
	}
	
}
