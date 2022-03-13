package com.s8.io.bohr.atom;

import java.nio.charset.StandardCharsets;


/**
 * 
 * @author Pierre Convert
 * Copyright (c) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 */
public abstract class S8Branch {
	

	
	/**
	 * 
	 */
	public final String address;
	
	
	/**
	 * 
	 */
	public final byte[] id;

	
	/**
	 * 
	 * @param address
	 * @param id
	 */
	public S8Branch(String address, String id) {
		super();
		this.address = address;
		this.id = id.getBytes(StandardCharsets.UTF_8);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getIdentifier() {
		return new String(id, StandardCharsets.UTF_8);
	}
	
	

	/**
	 * 
	 */
	public final static int EXPOSURE_RANGE = 8;

	
	
	/**
	 * Access to any shell can only be made through the
	 * <code>S8AsyncFlow.doOn</code> or <code>S8AsyncFlow.callOn</code> methods,
	 * that is call upon a <code>S8Ref</code> that always define a release that
	 * define a version.
	 * 
	 * @return the base release this shell has been copied form
	 */
	public abstract long getBaseVersion();
	
	

	

	
	/**
	 * Retrieve an exposed object
	 * @param id
	 * @return
	 * @throws S8Exception 
	 */
	public abstract <T extends S8Object> T access(int port) throws S8Exception;

	
	

}
