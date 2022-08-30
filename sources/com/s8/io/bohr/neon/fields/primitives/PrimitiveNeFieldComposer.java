package com.s8.io.bohr.neon.fields.primitives;

import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.fields.NeFieldComposer;
import com.s8.io.bohr.neon.fields.NeFieldValue;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveNeFieldComposer extends NeFieldComposer {

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public PrimitiveNeFieldComposer(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}
	
	
	
	public static abstract class Value extends NeFieldValue {
		public Value() {
			super();
		}
	}
	
	
}
