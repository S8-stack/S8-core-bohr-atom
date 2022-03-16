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
public class StringNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(String arg);
		
	}
	

	public final static long SIGNATURE = BOHR_Types.STRING_UTF8;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public StringNeMethod(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}



	@Override
	public NeRunnable buildRunnable(ByteInflow inflow) throws IOException {
		switch(inflow.getUInt8()) {
		case BOHR_Types.STRING_UTF8 : new StringUTF8NeRunnable();
		default : throw new IOException("Unsupported type");
		}
	}
	
	
	private static class StringUTF8NeRunnable implements NeRunnable {

		@Override
		public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
			String arg = inflow.getStringUTF8();
			((Lambda) (func.lambda)).operate(arg);
		}
	}
}
