package com.s8.io.bohr.atom;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author pierreconvert
 *
 */
public class S8BuildException extends IOException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2543520696403103890L;
	
	
	private Class<?> type;
	
	private Field field;
	
	private Method method;
	
	
	public S8BuildException(String message) {
		super(message);
	}
	
	
	/**
	 * 
	 * @param message
	 * @param type
	 */
	public S8BuildException(String message, Class<?> type) {
		super(message+"for type: "+type);
		this.type = type;
	}
	

	public S8BuildException(String message, Field field) {
		super(message+"for type: "+field);
		this.field = field;
	}
	
	
	public S8BuildException(String message, Method method) {
		super(message+"for type: "+method);
		this.method = method;
	}
	
	
	
	public Class<?> getCauseType(){
		return type;
	}
	
	public Field getCauseField(){
		return field;
	}
	
	public Method getCauseMethod(){
		return method;
	}
	
}
