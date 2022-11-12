package com.s8.io.bohr.neon.methods.arrays;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject.UInt64ArrayLambda;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.methods.NeMethodRunner;
import com.s8.io.bytes.alpha.ByteInflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class Int64ArrayNeMethodRunner extends NeMethodRunner {


	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.INT64;
	public @Override long getSignature() { return SIGNATURE; }


	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public Int64ArrayNeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super(prototype, name, ordinal);
	}


	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		long[] arg = null;
		int length = (int) inflow.getUInt7x();
		if(length >= 0) {
			arg = new long[length];
			for(int i=0; i<length; i++) { arg[i] = inflow.getInt64(); }
		}
		((UInt64ArrayLambda) lambda).operate(arg);
	}
}
