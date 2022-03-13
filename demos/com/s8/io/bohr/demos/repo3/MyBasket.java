package com.s8.io.bohr.demos.repo3;

import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.S8Field;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ObjectType;


/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 */
@S8ObjectType(name = "Basket")
public class MyBasket extends S8Object {

	
	public @S8Field(name = "picks") List<MyPick> picks;
	

	public MyBasket() {
		super();
		this.picks = new ArrayList<MyPick>();
	}
	
	
	
}
