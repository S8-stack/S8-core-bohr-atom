package com.s8.io.bohr.neon.methods.primitives;

import java.io.IOException;

import com.s8.io.bohr.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
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
public class BooleanNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(boolean arg);
		
	}
	

	public final static long SIGNATURE = BOHR_Types.BOOL8;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public BooleanNeMethod(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}


	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		switch(inflow.getUInt8()) {
		case BOHR_Types.BOOL8 : new Bool8NeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	
	
	private static class Bool8NeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			boolean arg = inflow.getBool8();
			((Lambda) (func.lambda)).operate(arg);
		}
	}
}
