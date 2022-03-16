package com.s8.io.bohr.neon.methods.primitives;

import java.io.IOException;

import com.s8.io.bohr.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bohr.neon.methods.NeFunc;
import com.s8.io.bohr.neon.methods.NeMethod;
import com.s8.io.bohr.neon.methods.NeRunnable;
import com.s8.io.bytes.alpha.ByteInflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class VoidNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate();
		
	}
	

	public final static long SIGNATURE = BOHR_Types.VOID;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public VoidNeMethod(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	

	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		switch(inflow.getUInt8()) {
		case BOHR_Types.VOID : new VoidNeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	
	
	private static class VoidNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			((Lambda) (func.lambda)).operate();
		}
	}
}
