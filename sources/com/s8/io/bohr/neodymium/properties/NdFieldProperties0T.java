package com.s8.io.bohr.neodymium.properties;

import com.s8.io.bohr.neodymium.fields.EmbeddedTypeNature;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdFieldProperties0T extends NdFieldProperties {

	private final Class<?> baseType;
	
	
	
	public NdFieldProperties0T(NdFieldPrototype prototype, int mode, Class<?> baseType) {
		super(prototype, mode);
		this.baseType = baseType;
	}

	@Override
	public EmbeddedTypeNature getEmbeddedTypeNature() {
		return EmbeddedTypeNature.NONE;
	}

	@Override
	public Class<?> getEmbeddedType() {
		return null;
	}

	@Override
	public Class<?> getBaseType() {
		return baseType;
	}

	@Override
	public Class<?> getParameterType1() {
		return null;
	}

	@Override
	public Class<?> getParameterType2() {
		return null;
	}

}
