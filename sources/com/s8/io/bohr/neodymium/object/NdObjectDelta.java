package com.s8.io.bohr.neodymium.object;

import java.io.IOException;

import com.s8.io.bohr.neodymium.branch.NdBranch;
import com.s8.io.bohr.neodymium.branch.NdOutbound;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NdObjectDelta {

	public final String index;



	public NdObjectDelta(String index) {
		super();
		this.index = index;
	}


	/**
	 * 
	 * @param shell
	 * @return
	 * @throws NdIOException 
	 * @throws IOException 
	 */
	public abstract void consume(NdBranch branch, BuildScope scope) throws NdIOException;

	
	/**
	 * 
	 * @param outbound
	 * @param outflow
	 * @throws IOException
	 */
	public abstract void serialize(NdOutbound outbound, ByteOutflow outflow) throws IOException;

	
	/**
	 * 
	 * @param weight
	 */
	public abstract void computeFootprint(MemoryFootprint weight);
}
