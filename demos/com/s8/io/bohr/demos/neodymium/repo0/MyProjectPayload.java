package com.s8.io.bohr.demos.neodymium.repo0;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.object.LiS8Object;

@S8ObjectType(name = "project-payload", 
sub= {
		PlanePayload.class,
		CarPayload.class
})
public abstract class MyProjectPayload extends LiS8Object {

	public @S8Field(name="viscosity") double visc;
	
	public @S8Field(name="message") String specializedName;

	public @S8Field(name = "field1") int counter;
	
	
	
	public MyProjectPayload() {
		super();
	}
	
	
	/* <view01> */
}
