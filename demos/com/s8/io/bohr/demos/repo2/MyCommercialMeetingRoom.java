package com.s8.io.bohr.demos.repo2;

import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8ObjectType;



/**
 * 
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
@S8ObjectType(name = "my-com-meeting-room#0002")
public class MyCommercialMeetingRoom extends MyCommercialFloorElement {

	public @S8Field(name = "x-table-dimension", mask = HAS_CHANGED) double xTableDimension;
	
	public @S8Field(name = "y-table-dimension", mask = HAS_CHANGED) double yTableDimension;
	
	public MyCommercialMeetingRoom() {
		super();
	}
	
	
	@Override
	public void init() {
		baseInit();
		xTableDimension = Math.random();
		yTableDimension = Math.random();
	}

	
	public static MyCommercialMeetingRoom create() {
		MyCommercialMeetingRoom room = new MyCommercialMeetingRoom();
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
			xTableDimension = Math.random();
			yTableDimension = Math.random();
		}
		advertise(HAS_CHANGED);
	}
}
