package com.s8.io.bohr.neon.methods.arrays;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;
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
public class Bool8ArrayNeMethodRunner extends NeMethodRunner {


	public interface Lambda {

		public void operate(boolean[] arg);

	}


	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.BOOL8;

	public @Override long getSignature() { return SIGNATURE; }


	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public Bool8ArrayNeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super(prototype, name, ordinal);
	}


	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		boolean[] arg = null;
		int length = (int) inflow.getUInt7x();
		if(length >= 0) {
			arg = new boolean[length];
			for(int i=0; i<length; i++) { arg[i] = inflow.getBool8(); }
		}
		((NeObject.Bool8ArrayLambda) lambda).operate(arg);
	}

}
