package com.s8.io.bohr.demos.repo3;

import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ObjectType;

/**
 * 
 * @author pierreconvert
 *
 */

@S8ObjectType(name = "item-stock")
public class MyItemStock extends S8Object {


	public @S8Field(name = "n-items") int nItemsCurrentlyInWarehouse;
		
	public MyItemStock() {
		super();
	}

}
