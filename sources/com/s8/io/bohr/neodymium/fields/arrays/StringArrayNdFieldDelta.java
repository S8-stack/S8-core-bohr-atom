package com.s8.io.bohr.neodymium.fields.arrays;


import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class StringArrayNdFieldDelta extends NdFieldDelta {


	public final StringArrayNdField field;

	public final String[] value;

	public StringArrayNdFieldDelta(StringArrayNdField field, String[] array) {
		super();
		this.field = field;
		this.value = array;
	}

	@Override
	public NdField getField() { 
		return field; 
	}


	@Override
	public void consume(NdObject object, BuildScope scope) throws NdIOException {
		field.handler.set(object, value);
	}


	@Override
	public void computeFootprint(MemoryFootprint weight) {
		if(value!=null) {
			weight.reportInstances(1+value.length); // the array object itself	
			for(String str : value) {
				weight.reportBytes(str.length()); // proxy for UTF8 ~....
			}
		}
	}


}
