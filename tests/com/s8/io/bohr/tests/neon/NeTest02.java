package com.s8.io.bohr.tests.neon;

import com.s8.io.bohr.demos.neon.repo2.AircraftRotor;
import com.s8.io.bohr.neon.core.NeBranch;

public class NeTest02 {

	public static void main(String[] args) {

		NeBranch branch = new NeBranch("com.hello.boz", "master");
		AircraftRotor rotor = new AircraftRotor(branch);
		System.out.println("done!"+rotor);
	}

}
