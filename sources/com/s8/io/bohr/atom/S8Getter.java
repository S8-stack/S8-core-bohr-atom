package com.s8.io.bohr.atom;

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
 * @author pc
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface S8Getter {

	/**
	 * code for quick access to the method
	 * 
	 * @return
	 */
	public String name();
	
	
	public String flow() default "(default)";
	
	
	/**
	 * 
	 * @return
	 */
	public long mask() default 0L;
	
	/**
	 * 
	 * @return
	 */
	public long flags() default 0L;
	
}
