package com.s8.io.bohr.neon.fields;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NeFieldHandler {
	
	
	public NeObjectTypeHandler prototype;
	
	
	/**
	 * 
	 */
	public int code;
	
	
	/**
	 * 
	 */
	public int ordinal;
	
	
	public final String name;
	
	private boolean isUnpublished = true;
	
	/**
	 * 
	 * @param name
	 */
	public NeFieldHandler(NeObjectTypeHandler prototype, String name) {
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
	public abstract NeFieldValue createValue();
	
	


	/**
	 * 
	 * @param front
	 * @throws IOException 
	 */
	//public abstract void sweep(Queue<QtzS8View<?>> front);

	
	
	public void declare(ByteOutflow outflow) throws IOException {
		
		if(isUnpublished) {
			
			/* open declare tag */
			outflow.putUInt8(BOHR_Keywords.DECLARE_FIELD);
			
			/* declare name */
			outflow.putStringUTF8(name);
			
			/* publish encoding */
			publishEncoding(outflow);
			
			/* code */
			outflow.putUInt8(code);
			
			isUnpublished = false;
		}
	}

	
	
}
