package com.s8.io.bohr.neon.methods;

import java.io.IOException;

import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NeMethod {


	public NeObjectPrototype prototype;
	/**
	 * 
	 */
	public int code;
	
	public int ordinal;
	
	
	public final String name;
	
	
	/**
	 * 
	 * @param name
	 */
	public NeMethod(NeObjectPrototype prototype, String name) {
		super();
		this.prototype = prototype;
		this.name = name;
	}
	
	
	public abstract long getSignature();
	
	
	public NeFunc createFunc() {
		return new NeFunc();
	}
	
	public abstract void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException;
	
}
