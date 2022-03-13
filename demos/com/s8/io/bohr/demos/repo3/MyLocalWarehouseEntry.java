package com.s8.io.bohr.demos.repo3;

import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Ref;
import com.s8.io.bohr.atom.S8Row;
import com.s8.io.bohr.atom.S8RowType;

@S8RowType(name = "warehouse-entry")
public final class MyLocalWarehouseEntry extends S8Row {

	public MyLocalWarehouseEntry(String index) {
		super(index);
	}

	public @S8Field(name = "product-name") String name;
	
	public @S8Field(name = "quantity") int quantity;
	
	public @S8Field(name = "product") S8Ref<MyProduct> product;
	
}
