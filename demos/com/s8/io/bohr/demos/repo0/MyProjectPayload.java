package com.s8.io.bohr.demos.repo0;

import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ObjectType;

@S8ObjectType(name = "project-payload", 
sub= {
		PlanePayload.class,
		CarPayload.class
})
public abstract class MyProjectPayload extends S8Object {

	public @S8Field(name="viscosity") double visc;
	
	public @S8Field(name="message") String specializedName;

	public @S8Field(name = "field1") int counter;
	
	
	
	public MyProjectPayload() {
		super();
	}
	
	
	/* <view01> */
}
