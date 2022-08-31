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
public class UInt16NeMethodRunner extends NeMethodRunner {

	public final static long SIGNATURE = BOHR_Types.UINT16;
	
	public @Override long getSignature() { return SIGNATURE; }

	
	public UInt16NeMethodRunner(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}

	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		int arg =  inflow.getUInt16();
		((NeObject.UInt16Lambda) lambda).operate(arg);
	}
}