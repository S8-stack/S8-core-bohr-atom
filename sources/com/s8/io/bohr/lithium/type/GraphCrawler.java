package com.s8.io.bohr.lithium.type;

import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.object.LiObject;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public interface GraphCrawler {

	
	/**
	 * 
	 * @param object
	 * @throws LiIOException
	 */
	public void accept(LiObject object);
	
	
}
