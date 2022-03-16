package com.s8.io.bohr.demos.neodymium.repo2;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
@S8ObjectType(name = "MyCommercialBoxedWorkseat")
public class MyCommercialBoxedWorkseat extends MyCommercialFloorElement {


	public @S8Field(name = "x-deck-dimension") double xDeckDimension;

	public @S8Field(name = "y-deck-dimension") double yDeckDimension;


	public MyCommercialBoxedWorkseat() {
		super();
	}



	@Override
	public void init() {
		baseInit();
		xDeckDimension = Math.random();
		yDeckDimension = Math.random();
	}


	public static MyCommercialBoxedWorkseat create() {
		MyCommercialBoxedWorkseat room = new MyCommercialBoxedWorkseat();
		room.init();
		return room;
	}

	@Override
	public void variate() {
		double u = Math.random();
		if(u<0.3) {
			init();
		}
		else {
			xDeckDimension = Math.random();
			yDeckDimension = Math.random();
		}
	}
}
