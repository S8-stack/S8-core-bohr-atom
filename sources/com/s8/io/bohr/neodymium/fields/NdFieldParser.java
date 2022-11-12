package com.s8.io.bohr.neodymium.fields;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Properties;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NdFieldParser {
	
	
	public abstract NdField getField();
	
	
	/**
	 * 
	 * @param map
	 * @param object
	 * @param inflow
	 * @param bindings
	 * @throws BkException
	 */
	public abstract void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException;
	

	/**
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException 
	 */
	public abstract NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException;
	

	
	public static boolean isNonNull(int props) {
		return (props & BOHR_Properties.IS_NON_NULL_PROPERTIES_BIT) == BOHR_Properties.IS_NON_NULL_PROPERTIES_BIT;
	}
}
