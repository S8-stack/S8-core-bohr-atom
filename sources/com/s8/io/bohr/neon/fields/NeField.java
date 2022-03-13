package com.s8.io.bohr.neon.fields;

import java.io.IOException;

import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NeField {
	
	
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
	public NeField(NeObjectPrototype prototype, String name) {
		super();
		this.prototype = prototype;
		this.name = name;
	}
	
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException 
	 */
	public abstract void publishEncoding(ByteOutflow outflow) throws IOException;
	
	
	
	public abstract long getSignature();
	
	
	
	

	/**
	 * 
	 * @param vertex
	 * @return
	 */
	public abstract NeValue createValue();
	
	


	/**
	 * 
	 * @param front
	 */
	//public abstract void sweep(Queue<QtzS8View<?>> front);


	
	
}
