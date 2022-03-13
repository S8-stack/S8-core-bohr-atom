package com.s8.io.bohr.lithium.type;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.lithium.exceptions.LiIOException;

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
	public void accept(S8Object object);
	
	
}
