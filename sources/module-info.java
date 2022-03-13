module com.s8.io.bohr {
	
	
	exports com.s8.io.bohr.atom;
	
	
	/* <lithium> */
	exports com.s8.io.bohr.lithium.codebase;
	exports com.s8.io.bohr.lithium.fields;
	exports com.s8.io.bohr.lithium.handlers;
	exports com.s8.io.bohr.lithium.properties;
	exports com.s8.io.bohr.lithium.branches;
	exports com.s8.io.bohr.lithium.type;
	
	/* </lithium> */


	/**
	 * 
	 */
	exports com.s8.io.bohr.tests.lithium;
	
	requires transitive com.s8.io.bytes;
}