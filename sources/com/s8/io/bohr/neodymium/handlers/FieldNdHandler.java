package com.s8.io.bohr.neodymium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldNdHandler implements NdHandler {


	public static NdHandler init(Field field) throws NdBuildException {
		FieldNdHandler handler = new FieldNdHandler();
		handler.attachField(field);
		return handler;
	}

	private Field field;

	/**
	 * 
	 * @param level
	 * @param field
	 * @param value
	 */
	public FieldNdHandler() {
		super();
	}
	
	public FieldNdHandler(Field field) {
		super();
		this.field = field;
	}


	@Override
	public Kind getKind() {
		return Kind.FIELD_BASED;
	}



	/**
	 * 
	 * @return
	 */
	public Field getField() {
		return field;
	}


	@Override
	public void attachField(Field field) throws NdBuildException {
		if(this.field!=null) {
			throw new NdBuildException("Conflict in field definition between "+this.field+" and "+field, field);
		}
		
		/*
		 * With setAccessible() you change the behavior of the AccessibleObject, i.e.
		 * the Field instance, but not the actual field of the class.
		 */
		field.setAccessible(true);
		
		this.field = field;
	}

	@Override
	public void attachGetMethod(Method method) throws NdBuildException {
		throw new NdBuildException("This field is of FIELD_BASED type and cannot be added a getter method", method);
	}

	@Override
	public void attachSetMethod(Method method) throws NdBuildException {
		throw new NdBuildException("This field is of FIELD_BASED type and cannot be added a setter method", method);	
	}




	/**
	 * 
	 * @param field
	 * @return true is the field passed as argument could be replacing this field (same name, same type)
	 */	
	public boolean isOverriding(Field field) {
		if(field.getType()==this.field.getType() && field.getName()==this.field.getName()) {
			return true;
		}
		else {
			return false;
		}
	}
	


	@Override
	public String describe(boolean isGetting) {
		S8Field fieldAnnotation = field.getAnnotation(S8Field.class);
		Class<?> declaringType =field.getDeclaringClass();
		S8ObjectType typeAnnotation = declaringType.getAnnotation(S8ObjectType.class);
		return "field: "+fieldAnnotation.name()+" of type "+typeAnnotation.name()+" ("+field+")";
	}




	@Override
	public Object get(Object object) throws NdIOException {
		try {
			return field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new NdIOException(e.getMessage(), field, e);
		}
	}


	@Override
	public void set(Object object, Object value) throws NdIOException {
		try {
			field.set(object, value);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new NdIOException(e.getMessage(), field, e);
		}
	}


	@Override
	public void setByte(Object object, byte value) throws NdIOException {
		try {
			field.setByte(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}

	@Override
	public byte getByte(Object object) throws NdIOException {
		try {
			return field.getByte(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read boolean field", field, cause);
		}
	}
	
	@Override
	public void setBoolean(Object object, boolean value) throws NdIOException {
		try {
			field.setBoolean(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}

	@Override
	public boolean getBoolean(Object object) throws NdIOException {
		try {
			return field.getBoolean(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read boolean field", field, cause);
		}
	}


	@Override
	public void setDouble(Object object, double value) throws NdIOException {
		try {
			field.setDouble(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public double getDouble(Object object) throws NdIOException {
		try {
			return field.getDouble(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}
	
	
	@Override
	public void setFloat(Object object, float value) throws NdIOException {
		try {
			field.setFloat(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public float getFloat(Object object) throws NdIOException {
		try {
			return field.getFloat(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}
	
	
	@Override
	public void setShort(Object object, short value) throws NdIOException {
		try {
			field.setShort(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public short getShort(Object object) throws NdIOException {
		try {
			return field.getShort(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}
	
	@Override
	public void setInteger(Object object, int value) throws NdIOException {
		try {
			field.setInt(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public int getInteger(Object object) throws NdIOException {
		try {
			return field.getInt(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}
	
	@Override
	public void setLong(Object object, long value) throws NdIOException {
		try {
			field.setLong(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public long getLong(Object object) throws NdIOException {
		try {
			return field.getLong(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}
	

	@Override
	public void setString(Object object, String value) throws NdIOException {
		try {
			field.set(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public String getString(Object object) throws NdIOException {
		try {
			return (String) field.get(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new NdIOException("failed to read long field", field, cause);
		}
	}

}
