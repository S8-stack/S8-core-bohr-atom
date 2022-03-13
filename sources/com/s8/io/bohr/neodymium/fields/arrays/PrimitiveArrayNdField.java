package com.s8.io.bohr.neodymium.fields.arrays;


import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.primitives.PrimitiveNdField;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveArrayNdField extends PrimitiveNdField {

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public PrimitiveArrayNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
	}

	
	/**
	 * 
	 * @param object
	 * @param array
	 * @throws LthSerialException
	 */
	public void setValue(Object object, Object array) throws NdIOException {
		handler.set(object, array);
	}


}
