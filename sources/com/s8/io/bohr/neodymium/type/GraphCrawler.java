package com.s8.io.bohr.neodymium.type;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;

/**
 * 
 * @author pierreconvert
 *
 */
public interface GraphCrawler {

	
	/**
	 * 
	 * @param object
	 * @throws NdIOException
	 */
	public void accept(S8Object object);
	
	
}
