package com.s8.io.bohr.beryllium.object;

import java.io.IOException;

public class BeSerialException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4054524632560836423L;
	
	private Exception cause;
	
	public BeSerialException(String message) {
		super(message);
	}
	
	public BeSerialException(String message, Exception cause) {
		super(message);
		this.cause = cause;
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
		if(cause!=null) {
			cause.printStackTrace();
		}
	}
}
