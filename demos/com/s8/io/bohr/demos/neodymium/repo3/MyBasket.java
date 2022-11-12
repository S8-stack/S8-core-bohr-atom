package com.s8.io.bohr.demos.neodymium.repo3;

import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.object.LiObject2;


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
public class MyBasket extends LiObject2 {

	
	public @S8Field(name = "picks") List<MyPick> picks;
	

	public MyBasket() {
		super();
		this.picks = new ArrayList<MyPick>();
	}
	
	
	
}
