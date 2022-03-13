package com.s8.io.bohr.lithium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface LiHandler {


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
	public void attachField(Field field) throws LiBuildException;
	
	
	/**
	 * 
	 * @param method
	 * @throws LithTypeBuildException
	 */
	public void attachGetMethod(Method method) throws LiBuildException;

	
	/**
	 * 
	 * @param method
	 * @throws LithTypeBuildException
	 */
	public void attachSetMethod(Method method) throws LiBuildException;


	/**
	 * default getter
	 * 
	 * @param object
	 * @return
	 * @throws LthSerialException
	 */
	public Object get(Object object) throws LiIOException;


	/**
	 * default setter
	 * 
	 * @param object
	 * @param value
	 * @throws LthSerialException
	 */
	public void set(Object object, Object value) throws LiIOException;


	public byte getByte(Object object) throws LiIOException;
	

	public void setByte(Object object, byte value) throws LiIOException;
	

	public boolean getBoolean(Object object) throws LiIOException;


	public void setBoolean(Object object, boolean value) throws LiIOException;


	public double getDouble(Object object) throws LiIOException;
	
	
	public void setDouble(Object object, double value) throws LiIOException;
	
	
	public void setFloat(Object object, float value) throws LiIOException;


	public float getFloat(Object object) throws LiIOException;


	public void setInteger(Object object, int value) throws LiIOException;


	public int getInteger(Object object) throws LiIOException;


	public void setLong(Object object, long value) throws LiIOException;


	public long getLong(Object object) throws LiIOException;


	public void setShort(Object object, short value) throws LiIOException;


	public short getShort(Object object) throws LiIOException;


	public void setString(Object object, String value) throws LiIOException;


	public String getString(Object object) throws LiIOException;






}
