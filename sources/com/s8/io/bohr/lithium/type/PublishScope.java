package com.s8.io.bohr.lithium.type;

import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.atom.S8Object;


/**
 * 
 * @author pierreconvert
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public interface PublishScope {
	
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public S8Index append(S8Object object);

}
