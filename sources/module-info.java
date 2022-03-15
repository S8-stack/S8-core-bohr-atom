

/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
module com.s8.io.bohr {


	exports com.s8.io.bohr;
	
	exports com.s8.io.bohr.atom;
	exports com.s8.io.bohr.atom.annotations;


	/* <neon> */
	exports com.s8.io.bohr.neon.core;
	
	exports com.s8.io.bohr.neon.fields;
	exports com.s8.io.bohr.neon.fields.primitives;
	exports com.s8.io.bohr.neon.fields.arrays;
	exports com.s8.io.bohr.neon.fields.objects;
	
	exports com.s8.io.bohr.neon.methods;
	exports com.s8.io.bohr.neon.methods.primitives;
	exports com.s8.io.bohr.neon.methods.arrays;
	exports com.s8.io.bohr.neon.methods.objects;

	/* </neon> */
	
	/* <lithium> */
	exports com.s8.io.bohr.lithium.codebase;
	exports com.s8.io.bohr.lithium.fields;
	exports com.s8.io.bohr.lithium.handlers;
	exports com.s8.io.bohr.lithium.properties;
	exports com.s8.io.bohr.lithium.branches;
	exports com.s8.io.bohr.lithium.type;

	/* </lithium> */


	/* <neodymium> */

	exports com.s8.io.bohr.neodymium.codebase;
	exports com.s8.io.bohr.neodymium.type;
	exports com.s8.io.bohr.neodymium.branches;
	exports com.s8.io.bohr.neodymium.objects;
	exports com.s8.io.bohr.neodymium.handlers;
	exports com.s8.io.bohr.neodymium.properties;
	exports com.s8.io.bohr.neodymium.exceptions;

	exports com.s8.io.bohr.neodymium.fields;
	exports com.s8.io.bohr.neodymium.fields.primitives;
	exports com.s8.io.bohr.neodymium.fields.arrays;
	exports com.s8.io.bohr.neodymium.fields.objects;
	exports com.s8.io.bohr.neodymium.fields.collections;

	/* </neodymium> */

	/**
	 * 
	 */
	exports com.s8.io.bohr.tests.lithium;

	requires transitive com.s8.io.bytes;
}