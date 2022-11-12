package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;
import com.s8.io.bohr.neon.core.NeObject.ListLambda;
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
public class ListNeMethodRunner<T extends NeObject> extends NeMethodRunner {





	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }


	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public ListNeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super(prototype, name, ordinal);
	}



	@SuppressWarnings("unchecked")
	@Override
	public void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException {
		List<T> list = null;
		int length = (int) inflow.getUInt7x();
		if(length >= 0) {
			list =  new ArrayList<T>();
			for(int i=0; i<length; i++) {
				String index = inflow.getStringUTF8();
				list.add(index != null ? (T) branch.getObject(index) : null);
			}	
		}
		((ListLambda<T>) lambda).operate(list);	
	}
}
