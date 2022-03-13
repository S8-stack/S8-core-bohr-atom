package com.s8.io.bohr.lithium.properties;

import com.s8.io.bohr.lithium.fields.EmbeddedTypeNature;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LiFieldProperties1T extends LiFieldProperties {

	private final Class<?> baseType;
	

	public LiFieldProperties1T(LiFieldPrototype prototype, int mode, Class<?> baseType) {
		super(prototype, mode);
		this.baseType = baseType;
	}

	@Override
	public EmbeddedTypeNature getEmbeddedTypeNature() {
		return EmbeddedTypeNature.S8_OBJECT;
	}

	@Override
	public Class<?> getEmbeddedType() {
		return baseType;
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
