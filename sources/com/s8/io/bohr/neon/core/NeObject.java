package com.s8.io.bohr.neon.core;

public class NeObject {
	
	
	public final NeVertex vertex;
	
	
	public NeObject(NeBranch branch, String typeName) {
		super();
		
		// create vertex
		vertex = new NeVertex(branch, typeName, this);
	}

}
