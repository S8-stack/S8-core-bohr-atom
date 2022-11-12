package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.core.NeVertex;
import com.s8.io.bohr.neon.methods.NeMethodRunner;
import com.s8.io.bytes.alpha.ByteInflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class ObjNeMethodRunner<T extends NeVertex> extends NeMethodRunner {


	public interface Lambda<T extends NeVertex> {

		public void operate(T arg);

	}


	public final static long SIGNATURE = BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }


	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public ObjNeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super(prototype, name, ordinal);
	}



	@SuppressWarnings("unchecked")
	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		String index = inflow.getStringUTF8();
		NeVertex arg = index != null ? branch.getVertex(index) : null;
		((Lambda<T>) lambda).operate((T) arg);
	}
}
