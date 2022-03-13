package com.s8.io.bohr.demos.neon.repo2;

import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;


/**
 * 
 * @author pierreconvert
 *
 */
public class TestNeObject extends NeObject {


	public TestNeObject(NeBranch branch) {
		super(branch, "Test-object");
	}




	public void setRotor(AircraftRotor value) { setObj("rotor", value); }

	public AircraftRotor getRotor() { return getObj("rotor"); }


	public void setPitch(double value) { setFloat64("pitch", value); }

	public double getPitch() { return getFloat64("pitch"); }

	
	public void setYaw(double value) { setFloat64("yaw", value); }

	public double getYaw() { return getFloat64("yaw"); }



	/**
	 * 
	 * @param vertices
	 */
	public void setVertexCoordinates(double[] vertices) {
		setFloat64Array("vertex-coordinates#12", vertices);
	}


}
