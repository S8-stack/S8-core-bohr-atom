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
public class ShortArrayNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(short[] arg);
		
	}
	

	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.INT16;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public ShortArrayNeMethod(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}

	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		if(inflow.getUInt8() != BOHR_Types.ARRAY) {	throw new IOException("Must be an array"); }
		switch(inflow.getUInt8()) {
		case BOHR_Types.UINT8 : new UInt8ArrayNeRunnable();
		case BOHR_Types.UINT16 : new UInt16ArrayNeRunnable();
		case BOHR_Types.INT8 : new Int8ArrayNeRunnable();
		case BOHR_Types.INT16 : new Int16ArrayNeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	

	private static class UInt8ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] arg = new short[length];
				for(int i=0; i<length; i++) { arg[i] = (short) inflow.getUInt8(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}
		
	}
	


	private static class UInt16ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] arg = new short[length];
				for(int i=0; i<length; i++) { arg[i] = (short) inflow.getUInt16(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}
		
	}
	

	


	private static class Int8ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] arg = new short[length];
				for(int i=0; i<length; i++) { arg[i] = inflow.getInt8(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}
		
	}
	


	private static class Int16ArrayNeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] arg = new short[length];
				for(int i=0; i<length; i++) { arg[i] = inflow.getInt16(); }
				((Lambda) (func.lambda)).operate(arg);
			}
			else {
				((Lambda) (func.lambda)).operate(null);
			}
		}
		
	}
	
}
