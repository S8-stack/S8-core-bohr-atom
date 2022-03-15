package com.s8.io.bohr.neodymium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;

/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class MethodNdHandler implements NdHandler {


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
	
	
	public static MethodNdHandler initWithGetter(Method method) throws NdBuildException {
		MethodNdHandler handler = new MethodNdHandler();
		handler.attachGetMethod(method);
		return handler;
	}
	
	
	public static MethodNdHandler initWithSetter(Method method) throws NdBuildException {
		MethodNdHandler handler = new MethodNdHandler();
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
	public void attachGetMethod(Method method) throws NdBuildException {
		if(getMethod!=null) {
			throw new NdBuildException("Cannot override method: "+method.getName());
		}
		
		Parameter[] parameters = method.getParameters();
		if(parameters.length>0) {
			throw new NdBuildException("Getter MUST NOT have parameters: "+method.getName());
		}
		this.getMethod = method;
	}


	
	@Override
	public void attachSetMethod(Method method) throws NdBuildException {
		if(setMethod!=null) {
			throw new NdBuildException("Cannot override method: "+method.getName());
		}
		Parameter[] parameters = method.getParameters();
		if(parameters.length!=1) {
			throw new NdBuildException("Setter MUST have only ONE parameter: "+method.getName());
		}
		
		this.setMethod = method;
	}



	@Override
	public void attachField(Field field) throws NdBuildException {
		throw new NdBuildException("failed to write double[] field");
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
	public void set(Object object, Object value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public Object get(Object object) throws NdIOException {
		try {
			return getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}


	
	@Override
	public void setBoolean(Object object, boolean value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}
	
	
	@Override
	public byte getByte(Object object) throws NdIOException {
		try {
			return (byte) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	

	@Override
	public void setByte(Object object, byte value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public boolean getBoolean(Object object) throws NdIOException {
		try {
			return (boolean) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	

	@Override
	public void setDouble(Object object, double value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public double getDouble(Object object) throws NdIOException {
		try {
			return (double) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}

	

	@Override
	public void setFloat(Object object, float value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public float getFloat(Object object) throws NdIOException {
		try {
			return (float) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setShort(Object object, short value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public short getShort(Object object) throws NdIOException {
		try {
			return (short) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	@Override
	public void setInteger(Object object, int value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public int getInteger(Object object) throws NdIOException {
		try {
			return (int) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setLong(Object object, long value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public long getLong(Object object) throws NdIOException {
		try {
			return (long) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
	
	@Override
	public void setString(Object object, String value) throws NdIOException {
		try {
			setMethod.invoke(object, new Object[]{ value });
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", setMethod, cause);
		}
	}


	@Override
	public String getString(Object object) throws NdIOException {
		try {
			return (String) getMethod.invoke(object, new Object[0]);
		} 
		catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException cause) {
			throw new NdIOException("failed to write double[] field", getMethod, cause);
		}
	}
	
}
