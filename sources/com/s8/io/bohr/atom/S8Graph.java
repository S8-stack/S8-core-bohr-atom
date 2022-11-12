package com.s8.io.bohr.atom;

import com.s8.io.bohr.lithium.object.LiObject2;

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
	 * @throws S8Exception 
	 */
	public void expose(int port, LiObject2 object);

	
	/**
	 * Remove a specific object from exposure list
	 * @param id
	 */
	public void unexpose(int port);

	
	/**
	 * Retrieve an exposed object
	 * @param id
	 * @return
	 * @throws S8Exception 
	 */
	public <T extends LiObject2> T access(int port) throws S8Exception;

	
	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws S8Exception 
	 */
	public void attach(LiObject2 object) throws S8Exception;


}
