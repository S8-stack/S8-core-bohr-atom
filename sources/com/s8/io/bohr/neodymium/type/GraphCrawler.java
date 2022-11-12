package com.s8.io.bohr.neodymium.type;

import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.object.NdObject;

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
	 * @throws NdIOException
	 */
	public void accept(NdObject object);
	
	
}
