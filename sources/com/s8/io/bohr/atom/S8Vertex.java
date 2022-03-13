package com.s8.io.bohr.atom;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract interface S8Vertex {
	
	
	/**
	 * 
	 * @return
	 */
	public abstract S8Object getObject();
	
	
	/**
	 * 
	 * @return
	 */
	public abstract S8Branch getBranch();


	public abstract void advertise(long event);
	
	
	
	public abstract void expose(int slot);

	/**
	 * Remove a specific object from exposure list
	 * @param id
	 */
	public abstract void unexpose();



}
