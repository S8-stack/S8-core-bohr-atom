package com.s8.io.bohr.neodymium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface NdHandler {


	public enum Kind {
		FIELD_BASED, METHOD_BASED;
	}


	/**
	 * 
	 * @return
	 */
	public abstract Kind getKind();

	
	/**
	 * 
	 * @param isGetting
	 * @return
	 */
	public abstract String describe(boolean isGetting);
	
	
	
	/**
	 * 
	 * @param field
	 * @throws LithTypeBuildException
	 */
	public void attachField(Field field) throws NdBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @throws LithTypeBuildException
	 */
	public void attachGetMethod(Method method) throws NdBuildException;

	
	/**
	 * 
	 * @param method
	 * @throws LithTypeBuildException
	 */
	public void attachSetMethod(Method method) throws NdBuildException;


	/**
	 * default getter
	 * 
	 * @param object
	 * @return
	 * @throws LthSerialException
	 */
	public Object get(Object object) throws NdIOException;


	/**
	 * default setter
	 * 
	 * @param object
	 * @param value
	 * @throws LthSerialException
	 */
	public void set(Object object, Object value) throws NdIOException;


	public byte getByte(Object object) throws NdIOException;
	

	public void setByte(Object object, byte value) throws NdIOException;
	

	public boolean getBoolean(Object object) throws NdIOException;


	public void setBoolean(Object object, boolean value) throws NdIOException;


	public double getDouble(Object object) throws NdIOException;
	
	
	public void setDouble(Object object, double value) throws NdIOException;
	
	
	public void setFloat(Object object, float value) throws NdIOException;


	public float getFloat(Object object) throws NdIOException;


	public void setInteger(Object object, int value) throws NdIOException;


	public int getInteger(Object object) throws NdIOException;


	public void setLong(Object object, long value) throws NdIOException;


	public long getLong(Object object) throws NdIOException;


	public void setShort(Object object, short value) throws NdIOException;


	public short getShort(Object object) throws NdIOException;


	public void setString(Object object, String value) throws NdIOException;


	public String getString(Object object) throws NdIOException;






}
