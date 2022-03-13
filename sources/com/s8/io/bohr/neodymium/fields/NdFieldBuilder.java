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
public abstract class NdFieldBuilder {
	
	
	
	/**
	 * 
	 * @return
	 */
	public NdFieldProperties properties;
	
	
	public NdHandler handler;
	

	/**
	 * 
	 * @param field
	 */
	public NdFieldBuilder(NdFieldProperties properties, NdHandler handler) {
		super();
		this.properties = properties;
		this.handler = handler;
	}

	
	/**
	 * 
	 * @param field
	 * @throws NdBuildException
	 */
	public void attachField(Field field) throws NdBuildException {
		NdFieldProperties props = this.getPrototype().captureField(field);
		if (props != null) {
			this.properties.merge(props);
			this.handler.attachField(field);
		}
		else {
			throw new NdBuildException("Field type is not matching field builder", field);
		}
	}
	

	/**
	 * 
	 * @param method
	 * @param isValidationEnabled
	 * @throws LithTypeBuildException
	 */
	public void attachGetMethod(Method method) throws NdBuildException {
		NdFieldProperties props = getPrototype().captureGetter(method);
		if (props != null) {
			properties.merge(props);
			handler.attachGetMethod(method);
		}
		else {
			throw new NdBuildException("Getter type is not matching", method);
		}
	}

	/**
	 * 
	 * @param method
	 * @param isValidationEnabled
	 * @throws LithTypeBuildException
	 */
	public void attachSetMethod(Method method) throws NdBuildException {
		NdFieldProperties props = getPrototype().captureSetter(method);
		if (props != null) {
			properties.merge(props);
			handler.attachSetMethod(method);
		}
		else {
			throw new NdBuildException("Getter type is not matching", method);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public abstract NdFieldPrototype getPrototype();
	
	

	/**
	 * 
	 * @return
	 * @throws NdBuildException 
	 */
	public abstract NdField build(int ordinal) throws NdBuildException;


}
