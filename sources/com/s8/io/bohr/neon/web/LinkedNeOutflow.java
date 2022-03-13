package com.s8.io.bohr.neon.web;

import com.s8.io.bytes.linked.LinkedByteOutflow;


/**
 * 
 * @author pierreconvert
 *
 */
public class LinkedNeOutflow extends LinkedByteOutflow implements NeOutflow {

	/**
	 * 
	 * @param capacity
	 */
	public LinkedNeOutflow(int capacity) {
		super(capacity);
		
	}	
}
