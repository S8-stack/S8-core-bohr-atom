package com.s8.io.bohr.neon.methods.primitives;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
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
public class Int16NeMethodRunner extends NeMethodRunner {

	public final static long SIGNATURE = BOHR_Types.INT16;
	
	public @Override long getSignature() { return SIGNATURE; }

	
	public Int16NeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super(prototype, name, ordinal);
	}

	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		int arg =  inflow.getInt16();
		((NeObject.Int16Lambda) lambda).operate(arg);
	}
}
