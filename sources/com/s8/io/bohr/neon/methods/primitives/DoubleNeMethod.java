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
public class DoubleNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(double arg);
		
	}
	

	public final static long SIGNATURE = BOHR_Types.FLOAT64;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public DoubleNeMethod(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}




	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		switch(inflow.getUInt8()) {
		case BOHR_Types.FLOAT32 : new Float32NeRunnable();
		case BOHR_Types.FLOAT64 : new Float64NeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	
	
	private static class Float32NeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			float arg = inflow.getFloat32();
			((Lambda) (func.lambda)).operate(arg);
		}
	}
	

	private static class Float64NeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			double arg = inflow.getFloat64();
			((Lambda) (func.lambda)).operate(arg);
		}
	}
	
}
