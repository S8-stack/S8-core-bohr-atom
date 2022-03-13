package com.s8.io.bohr.lithium.fields.arrays;


import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.primitives.PrimitiveLiField;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveArrayLiField extends PrimitiveLiField {

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public PrimitiveArrayLiField(int ordinal, LiFieldProperties properties, LiHandler handler) {
		super(ordinal, properties, handler);
	}

	
	/**
	 * 
	 * @param object
	 * @param array
	 * @throws LthSerialException
	 */
	public void setValue(Object object, Object array) throws LiIOException {
		handler.set(object, array);
	}


}
