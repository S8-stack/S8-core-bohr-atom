package com.s8.io.bohr.neon.methods.primitives;

import java.io.IOException;

import com.s8.io.bohr.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.methods.NeMethodRunner;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 * @author pierreconvert
 *
 */
public class Int64NeMethodRunner extends NeMethodRunner {

	public final static long SIGNATURE = BOHR_Types.INT64;
	
	public @Override long getSignature() { return SIGNATURE; }

	
	public Int64NeMethodRunner(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}

	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		long arg =  inflow.getInt64();
		((NeObject.Int64Lambda) lambda).operate(arg);
	}
}
