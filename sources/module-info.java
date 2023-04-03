

/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
module com.s8.io.bohr.atom {


	
	exports com.s8.io.bohr.atom;
	exports com.s8.io.bohr.atom.annotations;



	
	/* <beryllium> */
	exports com.s8.io.bohr.beryllium.codebase;
	exports com.s8.io.bohr.beryllium.fields;
	exports com.s8.io.bohr.beryllium.object;
	exports com.s8.io.bohr.beryllium.syntax;
	exports com.s8.io.bohr.beryllium.tables;
	exports com.s8.io.bohr.beryllium.types;
	/* </beryllium> */
	

	requires transitive com.s8.io.bytes;
}