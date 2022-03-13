package com.s8.io.bohr.neodymium.fields;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NdFieldPrototype {

	
	
	
	/**
	 * 
	 * @param field
	 * @return
	 * @throws NdBuildException 
	 */
	public abstract NdFieldProperties captureField(Field field) throws NdBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @return
	 */
	public abstract NdFieldProperties captureGetter(Method method) throws NdBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @return
	 * @throws NdBuildException 
	 */
	public abstract NdFieldProperties captureSetter(Method method) throws NdBuildException;
	
	
	/**
	 * 
	 * @param properties
	 * @param handler
	 * @return
	 */
	public abstract NdFieldBuilder createFieldBuilder(NdFieldProperties properties, NdHandler handler);


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
