package com.s8.io.bohr.atom;

import java.io.IOException;

import com.s8.api.bytes.ByteInflow;
import com.s8.api.bytes.ByteOutflow;

/**
 * 
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 * @param <T>
 */
public final class S8Ref<T extends S8Object> {
	
	
	/**
	 * 
	 */
	public final String address;
	
	
	/**
	 * 
	 */
	public final String branch;
	
	
	/**
	 * 
	 */
	public final long version;


	/**
	 * 
	 */
	public final int port;
	
	
	
	
	public S8Ref(String address, String branch, long version, int port) {
		super();
		this.address = address;
		this.branch = branch;
		this.version = version;
		this.port = port;
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static boolean areEqual(S8Ref<?> left, S8Ref<?> right) {
		if(left==null && right==null) {
			return true;
		}
		else if ((left==null && right!=null) || (left!=null && right==null)) {
			return false;
		}
		else {
			return left.address.equals(right.address) 
					&& (left.branch == right.branch)
					&& (left.version == right.version)
					&& (left.port == right.port);
		}
	}
	
	

	
	public static <T extends S8Object> S8Ref<T> read(ByteInflow inflow) throws IOException {
		String address = inflow.getStringUTF8();
		if(address != null) {
			String branch = inflow.getStringUTF8();
			long version = inflow.getUInt7x();
			int port = inflow.getUInt8();
			return new S8Ref<T>(address, branch, version, port);
		}
		else {
			return null;
		}
	}
	
	

	
	public static void write(S8Ref<?> ref, ByteOutflow outflow) throws IOException {
		if(ref != null) {
			outflow.putStringUTF8(ref.address);
			
			// branch
			outflow.putStringUTF8(ref.branch);
			
			// version
			outflow.putUInt7x(ref.version);
			
			// port
			outflow.putUInt8(ref.port);
		}
		else {
			outflow.putStringUTF8(null);
		}
	}


	
	
}
