package com.s8.io.bohr.lithium.handlers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class FieldLiHandler implements LiHandler {


	public static LiHandler init(Field field) throws LiBuildException {
		FieldLiHandler handler = new FieldLiHandler();
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
	public FieldLiHandler() {
		super();
	}
	
	public FieldLiHandler(Field field) {
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
	public void attachField(Field field) throws LiBuildException {
		if(this.field!=null) {
			throw new LiBuildException("Conflict in field definition between "+this.field+" and "+field, field);
		}
		
		/*
		 * With setAccessible() you change the behavior of the AccessibleObject, i.e.
		 * the Field instance, but not the actual field of the class.
		 */
		field.setAccessible(true);
		
		this.field = field;
	}

	@Override
	public void attachGetMethod(Method method) throws LiBuildException {
		throw new LiBuildException("This field is of FIELD_BASED type and cannot be added a getter method", method);
	}

	@Override
	public void attachSetMethod(Method method) throws LiBuildException {
		throw new LiBuildException("This field is of FIELD_BASED type and cannot be added a setter method", method);	
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
	public Object get(Object object) throws LiIOException {
		try {
			return field.get(object);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new LiIOException(e.getMessage(), field, e);
		}
	}


	@Override
	public void set(Object object, Object value) throws LiIOException {
		try {
			field.set(object, value);
		}
		catch (IllegalArgumentException | IllegalAccessException e) {
			throw new LiIOException(e.getMessage(), field, e);
		}
	}


	@Override
	public void setByte(Object object, byte value) throws LiIOException {
		try {
			field.setByte(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}

	@Override
	public byte getByte(Object object) throws LiIOException {
		try {
			return field.getByte(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read boolean field", field, cause);
		}
	}
	
	@Override
	public void setBoolean(Object object, boolean value) throws LiIOException {
		try {
			field.setBoolean(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}

	@Override
	public boolean getBoolean(Object object) throws LiIOException {
		try {
			return field.getBoolean(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read boolean field", field, cause);
		}
	}


	@Override
	public void setDouble(Object object, double value) throws LiIOException {
		try {
			field.setDouble(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public double getDouble(Object object) throws LiIOException {
		try {
			return field.getDouble(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}
	
	
	@Override
	public void setFloat(Object object, float value) throws LiIOException {
		try {
			field.setFloat(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public float getFloat(Object object) throws LiIOException {
		try {
			return field.getFloat(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}
	
	
	@Override
	public void setShort(Object object, short value) throws LiIOException {
		try {
			field.setShort(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public short getShort(Object object) throws LiIOException {
		try {
			return field.getShort(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}
	
	@Override
	public void setInteger(Object object, int value) throws LiIOException {
		try {
			field.setInt(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public int getInteger(Object object) throws LiIOException {
		try {
			return field.getInt(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}
	
	@Override
	public void setLong(Object object, long value) throws LiIOException {
		try {
			field.setLong(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public long getLong(Object object) throws LiIOException {
		try {
			return field.getLong(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}
	

	@Override
	public void setString(Object object, String value) throws LiIOException {
		try {
			field.set(object, value);
		} 
		catch ( IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}


	@Override
	public String getString(Object object) throws LiIOException {
		try {
			return (String) field.get(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new LiIOException("failed to read long field", field, cause);
		}
	}

}
