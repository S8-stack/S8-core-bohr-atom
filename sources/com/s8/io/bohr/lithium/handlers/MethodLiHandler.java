package com.s8.io.bohr.lithium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;

/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class MethodLiHandler implements LiHandler {


	@Override
	public Kind getKind() {
		return Kind.METHOD_BASED;
	}

	


	public static boolean isMatching(Method method) {
		S8Getter methodAnnotation = method.getAnnotation(S8Getter.class);
		return methodAnnotation!=null;
	}


	protected Method setMethod;

	protected Method getMethod;
	
	
	public static MethodLiHandler initWithGetter(Method method) throws LiBuildException {
		MethodLiHandler handler = new MethodLiHandler();
		handler.attachGetMethod(method);
		return handler;
	}
	
	
	public static MethodLiHandler initWithSetter(Method method) throws LiBuildException {
		MethodLiHandler handler = new MethodLiHandler();
		handler.attachSetMethod(method);
		return handler;
	}
	

	
	/*
	public String getDescriptor(boolean isGetting) {
		if(isGetting && getMethod!=null) {
			return "getter: "+name+"@"+getMethod.getDeclaringClass();	
		}
		else if(!isGetting && setMethod!=null){
			return "setter: "+name+"@"+setMethod.getDeclaringClass();
		}
		else {
			return "method: "+name;
		}
	}
	*/

	@Override
	public void attachGetMethod(Method method) throws LiBuildException {
		if(getMethod!=null) {
			throw new LiBuildException("Cannot override method: "+method.getName());
		}
		
		Parameter[] parameters = method.getParameters();
		if(parameters.length>0) {
			throw new LiBuildException("Getter MUST NOT have parameters: "+method.getName());
		}
		this.getMethod = method;
	}


	
	@Override
	public void attachSetMethod(Method method) throws LiBuildException {
		if(setMethod!=null) {
			throw new LiBuildException("Cannot override method: "+method.getName());
		}
		Parameter[] parameters = method.getParameters();
		if(parameters.length!=1) {
			throw new LiBuildException("Setter MUST have only ONE parameter: "+method.getName());
		}
		
		this.setMethod = method;
	}



	@Override
	public void attachField(Field field) throws LiBuildException {
		throw new LiBuildException("failed to write double[] field");
	}
	

	@Override
	public String describe(boolean isGetting) {
		if(isGetting && getMethod!=null) {
			S8Getter methodAnnotation = getMethod.getAnnotation(S8Getter.class);
			Class<?> declaringType = getMethod.getDeclaringClass();
			S8ObjectType typeAnnotation = declaringType.getAnnotation(S8ObjectType.class);
			return "method: "+methodAnnotation.name()+" of type "+typeAnnotation.name()+" ("+getMethod+")";		
		}
		else if(!isGetting && setMethod!=null) {
			S8Setter methodAnnotation = setMethod.getAnnotation(S8Setter.class);
			Class<?> declaringType = setMethod.getDeclaringClass();
			S8ObjectType typeAnnotation = declaringType.getAnnotation(S8ObjectType.class);
			return "method: "+methodAnnotation.name()+" of type "+typeAnnotation.name()+" ("+setMethod+")";		
		}
		else {
			return "/!\\ undefined handler";
		}
	}
	
	
	
	
	@Override
	public void set(Object object, Object value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public Object get(Object object) throws LiIOException {
		try {
			return getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}


	
	@Override
	public void setBoolean(Object object, boolean value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}
	
	
	@Override
	public byte getByte(Object object) throws LiIOException {
		try {
			return (byte) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	

	@Override
	public void setByte(Object object, byte value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public boolean getBoolean(Object object) throws LiIOException {
		try {
			return (boolean) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	

	@Override
	public void setDouble(Object object, double value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public double getDouble(Object object) throws LiIOException {
		try {
			return (double) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}

	

	@Override
	public void setFloat(Object object, float value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public float getFloat(Object object) throws LiIOException {
		try {
			return (float) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setShort(Object object, short value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public short getShort(Object object) throws LiIOException {
		try {
			return (short) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	@Override
	public void setInteger(Object object, int value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public int getInteger(Object object) throws LiIOException {
		try {
			return (int) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setLong(Object object, long value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public long getLong(Object object) throws LiIOException {
		try {
			return (long) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setString(Object object, String value) throws LiIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public String getString(Object object) throws LiIOException {
		try {
			return (String) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new LiIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
}
