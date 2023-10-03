package com.s8.io.bohr.atom;

import java.io.IOException;

import com.s8.api.bytes.Bool64;
import com.s8.api.bytes.ByteInflow;
import com.s8.api.bytes.ByteOutflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 * @param <R>
 */
public final class S8Table<R> {
	

	public final static long HASH = Bool64.BIT02;
	


	public final String address;
	
	
	/**
	 * 
	 * @param address
	 */
	public S8Table(String address) {
		super();
		this.address = address;
	}


	public static boolean areEqual(S8Table<?> baseValue, S8Table<?> updateValue) {
		return baseValue.address.equals(updateValue.address);
	}
	
	


	
	public static <R> S8Table<R> read(ByteInflow inflow) throws IOException {
		String address = inflow.getStringUTF8();
		if(address != null) {
			return new S8Table<R>(address);
		}
		else {
			return null;
		}
	}
	
	


	
	public static void write(S8Table<?> table, ByteOutflow outflow) throws IOException {
		if(table != null) {
			outflow.putStringUTF8(table.address);
		}
		else {
			outflow.putStringUTF8(null);
		}
	}
	
	
}
