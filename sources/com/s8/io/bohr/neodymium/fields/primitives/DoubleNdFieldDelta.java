package com.s8.io.bohr.neodymium.fields.primitives;

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
public class DoubleNdFieldDelta extends NdFieldDelta {



	public final DoubleNdField field;

	private double value;

	public DoubleNdFieldDelta(DoubleNdField field, double value) {
		super();
		this.field = field;
		this.value = value;
	}
	

	@Override
	public void consume(NdObject object, BuildScope scope) throws NdIOException {
		field.handler.setDouble(object, value);
	}


	@Override
	public void computeFootprint(MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public NdField getField() { 
		return field;
	}


}
