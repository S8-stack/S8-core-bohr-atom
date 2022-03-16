package com.s8.io.bohr.demos.neodymium.repo3;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
@S8ObjectType(name = "item-stock")
public class MyItemStock extends S8Object {


	public @S8Field(name = "n-items") int nItemsCurrentlyInWarehouse;
		
	public MyItemStock() {
		super();
	}

}
