package com.s8.io.bohr.neon.fields.primitives;

import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bohr.neon.fields.NeField;
import com.s8.io.bohr.neon.fields.NeValue;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class PrimitiveNeField extends NeField {

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public PrimitiveNeField(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}
	
	
	
	public static abstract class Value extends NeValue {
		public Value() {
			super();
		}
	}
	
	
}
