package com.s8.io.bohr.demos.neon.repo2;

import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObject;


/**
 * 
 * @author pierreconvert
 *
 */
public class AircraftRotor extends NeObject {



	public AircraftRotor(NeBranch branch) {
		super(branch, "Aircraft-rotor");
		
		forFloat64("pitch", p -> {
			setFloat64("pitch^2", p*p);
			setFloat64("pitch*radius", p*getFloat64("radius"));
		});
	}



	/**
	 * 
	 * @param value
	 */
	public void setPitch(double value) { setFloat64("pitch", value); }
	
	


	/**
	 * 
	 * @return pitch
	 */
	public double getPitch() { return getFloat64("pitch"); }
	
	
	
}
