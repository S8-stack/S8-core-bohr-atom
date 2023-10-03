package com.s8.io.bohr.atom;

import com.s8.api.exceptions.S8IOException;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface S8Graph {
	

	/**
	 * 
	 */
	public final static int EXPOSURE_RANGE = 8;

	
	/**
	 * 
	 * @return
	 */
	public String getAddress();
	
	
	/**
	 * Access to any shell can only be made through the
	 * <code>S8AsyncFlow.doOn</code> or <code>S8AsyncFlow.callOn</code> methods,
	 * that is call upon a <code>S8Ref</code> that always define a release that
	 * define a version.
	 * 
	 * @return the base release this shell has been copied form
	 */
	public long getBaseVersion();
	
	
	/**
	 * Access to any shell can only be made through the
	 * <code>S8AsyncFlow.doOn</code> or <code>S8AsyncFlow.callOn</code> methods,
	 * that is call upon a <code>S8Ref</code> that always define a release that
	 * define a version.
	 * 
	 * @return the base release this shell has been copied from
	 */
	public String getBaseRelease();
	

	/**
	 * Assign an object within the exposure list
	 * @param id
	 * @param object
	 * @throws S8IOException 
	 */
	public void expose(int port, S8Object object);

	
	/**
	 * Remove a specific object from exposure list
	 * @param id
	 */
	public void unexpose(int port);

	
	/**
	 * Retrieve an exposed object
	 * @param id
	 * @return
	 * @throws S8IOException 
	 */
	public <T extends S8Object> T access(int port) throws S8IOException;

	
	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws S8IOException 
	 */
	public void attach(S8Object object) throws S8IOException;


}
