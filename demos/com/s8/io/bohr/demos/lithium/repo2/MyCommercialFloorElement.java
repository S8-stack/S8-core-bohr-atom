package com.s8.io.bohr.demos.lithium.repo2;

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
@S8ObjectType(name = "my-commercial-floor-element", sub= {
		MyCommercialBoxedWorkseat.class,
		MyCommercialMeetingRoom.class
})
public abstract class MyCommercialFloorElement extends LiObject2 {
	

	public final static long HAS_CHANGED = 0x02;

	public @S8Field(name = "tag", mask = HAS_CHANGED) String tag;
	
	public @S8Field(name = "x0", mask = HAS_CHANGED) double x0;

	public @S8Field(name = "x1", mask = HAS_CHANGED) double x1;

	public @S8Field(name = "y0", mask = HAS_CHANGED) double y0;

	public @S8Field(name = "y1", mask = HAS_CHANGED) double y1;

	public @S8Field(name = "door-location-face", mask = HAS_CHANGED) MyFloor.Face doorLocationFace;
	
	
	/**
	 * Must be between 0 and 1
	 */
	public @S8Field(name = "door-location-coordinate", mask = HAS_CHANGED) double doorLocationCoordinate;
	
	
	public MyCommercialFloorElement() {
		super();
	}
	

	public void baseInit() {
		x0 = Math.random()*100;
		x1 = x0 + Math.random()*100;
		y0 = Math.random()*100;
		y1 = y0 + Math.random()*100;
		
		doorLocationFace = MyFloor.Face.values()[(int) (Math.random()*4)];
		doorLocationCoordinate = Math.random();	
	}
	

	public abstract void init();
	
	
	public static MyCommercialFloorElement create() {
		MyCommercialFloorElement element = null;
		if(Math.random()<0.3){
			element = MyCommercialMeetingRoom.create();
		}
		else {
			element = MyCommercialBoxedWorkseat.create();
		}
		return element;
	}


	public abstract void variate();
	
	
}
