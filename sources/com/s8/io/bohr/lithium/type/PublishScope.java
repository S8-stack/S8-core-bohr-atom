package com.s8.io.bohr.lithium.type;

import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.atom.S8Object;


/**
 * 
 * @author pierreconvert
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
