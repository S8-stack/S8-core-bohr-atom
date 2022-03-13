package com.s8.io.bohr.lithium.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class LiFieldPrototype {

	
	
	
	/**
	 * 
	 * @param field
	 * @return
	 * @throws LiBuildException 
	 */
	public abstract LiFieldProperties captureField(Field field) throws LiBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public abstract LiFieldProperties captureGetter(Method method) throws LiBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @return
	 * @throws LiBuildException 
	 */
	public abstract LiFieldProperties captureSetter(Method method) throws LiBuildException;
	
	
	/**
	 * 
	 * @param properties
	 * @param handler
	 * @return
	 */
	public abstract LiFieldBuilder createFieldBuilder(LiFieldProperties properties, LiHandler handler);


	/**
	 * 
	
	public boolean captureField(NdTypeBuilder typeBuilder, Field field) throws NdBuildException {
		NdFieldProperties props = readPropertiesFromField(field);
		if(props != null) {
			NdFieldBuilder fieldBuilder = typeBuilder.getFieldBuilder(props.getName());
			if(fieldBuilder != null) { // update
				fieldBuilder.getField().properties.merge(props);
			}
			else { // create
				FieldNdHandler handler = new FieldNdHandler();
				handler.attachField(field);
				fieldBuilder = createFieldBuilder(props, handler);
				typeBuilder.setFieldBuilder(props.getName(), fieldBuilder);
			}
			return true;
		}
		else {
			return false;		
		}
	}

	
	
	public boolean captureSetter(NdTypeBuilder typeBuilder, Method method) throws NdBuildException {
		NdFieldProperties props = readPropertiesFromSetter(method);
		if(props != null) {
			NdFieldBuilder fieldBuilder = typeBuilder.getFieldBuilder(props.getName());
			if(fieldBuilder != null) { // update
				fieldBuilder.getField().properties.merge(props);
			}
			else { // create
				MethodNdHandler handler = new MethodNdHandler();
				handler.attachSetMethod(method);
				fieldBuilder = createFieldBuilder(props, handler);
				typeBuilder.setFieldBuilder(props.getName(), fieldBuilder);
			}
			return true;
		}
		else {
			return false;		
		}
	}


	public boolean captureGetter(NdTypeBuilder typeBuilder, Method method) throws NdBuildException {
		NdFieldProperties props = readPropertiesFromGetter(method);
		if(props != null) {
			NdFieldBuilder fieldBuilder = typeBuilder.getFieldBuilder(props.getName());
			if(fieldBuilder != null) { // update
				fieldBuilder.getField().properties.merge(props);
			}
			else { // create
				MethodNdHandler handler = new MethodNdHandler();
				handler.attachGetMethod(method);
				fieldBuilder = createFieldBuilder(props, handler);
				typeBuilder.setFieldBuilder(props.getName(), fieldBuilder);
			}
			return true;
		}
		else {
			return false;		
		}
	}

	 */
	
}
