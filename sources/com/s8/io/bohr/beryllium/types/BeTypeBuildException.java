package com.s8.io.bohr.beryllium.types;

import com.s8.io.bohr.atom.S8BuildException;

public class BeTypeBuildException extends S8BuildException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4054524632560836423L;

	
	public BeTypeBuildException(String message) {
		super(message);
	}
	
	public BeTypeBuildException(String message, Class<?> type) {
		super(message, type);
	}


	
}
