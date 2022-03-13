package com.s8.io.bohr.atom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface S8ObjectType {

	public String name();
	
	public Class<?>[] sub() default {};
	
}
