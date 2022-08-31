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
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class Float32NeMethodRunner extends NeMethodRunner {

	public final static long SIGNATURE = BOHR_Types.FLOAT32;
	public @Override long getSignature() { return SIGNATURE; }


	public Float32NeMethodRunner(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}

	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		float arg =  inflow.getFloat32();
		((NeObject.Float32Lambda) lambda).operate(arg);
	}
}
