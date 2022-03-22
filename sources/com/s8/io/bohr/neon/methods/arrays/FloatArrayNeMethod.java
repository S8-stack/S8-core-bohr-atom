package com.s8.io.bohr.neon.methods.arrays;

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
public class FloatArrayNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(float[] arg);
		
	}
	

	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.FLOAT32;

	
	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public FloatArrayNeMethod(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}
	
	

	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		if(inflow.getUInt8() != BOHR_Types.ARRAY) {	throw new IOException("Must be an array"); }
		switch(inflow.getUInt8()) {
		case BOHR_Types.FLOAT32 : new Float32ArrayNeRunnable();
		case BOHR_Types.FLOAT64 : new Float64ArrayNeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	

	private static class Float32ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				float[] arg = new float[length];
				for(int i=0; i<length; i++) { arg[i] = inflow.getFloat32(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}
		
	}
	
	private static class Float64ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				float[] arg = new float[length];
				for(int i=0; i<length; i++) { arg[i] = (float) inflow.getFloat64(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}	
	}

	
}
