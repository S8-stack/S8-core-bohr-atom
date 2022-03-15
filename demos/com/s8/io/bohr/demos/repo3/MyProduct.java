package com.s8.io.bohr.demos.repo3;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;


/**
 * This is PUBLIC object
 * 
 * @author pierreconvert
 *
 */
@S8ObjectType(name = "product")
public class MyProduct extends S8Object {


	@S8Field(name = "description")
	public String description;
	
	
	@S8Field(name = "stock", props = REF)
	public MyItemStock stock;
	

	/**
	 * 
	 * @param shell
	 */
	public MyProduct() {
		super();
	}
	
}
