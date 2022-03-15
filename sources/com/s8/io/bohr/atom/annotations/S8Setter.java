package com.s8.io.bohr.atom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * S8Method annotation is permitted on:
 * </p>
 * <ul>
 * <li>Synchronous methods (in this case target=method)</li>
 * <li>Asynchronous methods (in this case target=final static field)</li>
 * </ul>
 * <p>
 * Note that rights is defined within method body with custom rules
 * </p>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface S8Setter {

	/**
	 * code for quick access to the method
	 * 
	 * @return
	 */
	public String name();
	

	/**
	 * 
	 * @return
	 */
	public long props() default 0L;
	
	
}
