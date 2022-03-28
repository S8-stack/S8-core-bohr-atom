package com.s8.io.bohr.demos.neon.repo2;

import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class TestNeObject extends NeObject {


	public TestNeObject(NeBranch branch) {
		super(branch, "Test-object");
	}




	public void setRotor(AircraftRotor value) { vertex.setObj("rotor", value); }

	public AircraftRotor getRotor() { return vertex.getObj("rotor"); }


	public void setPitch(double value) { vertex.setFloat64("pitch", value); }

	public double getPitch() { return vertex.getFloat64("pitch"); }

	
	public void setYaw(double value) { vertex.setFloat64("yaw", value); }

	public double getYaw() { return vertex.getFloat64("yaw"); }



	/**
	 * 
	 * @param vertices
	 */
	public void setVertexCoordinates(double[] vertices) {
		vertex.setFloat64Array("vertex-coordinates#12", vertices);
	}


}
