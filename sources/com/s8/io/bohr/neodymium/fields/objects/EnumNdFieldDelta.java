package com.s8.io.bohr.neodymium.fields.objects;

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
public class EnumNdFieldDelta extends NdFieldDelta {


	public final EnumNdField field;

	public final Object value;

	public EnumNdFieldDelta(EnumNdField field, Object value) {
		super();
		this.field = field;
		this.value = value;
	}

	@Override
	public void consume(NdObject object, BuildScope scope) throws NdIOException {
		field.handler.set(object, value);
	}


	public @Override NdField getField() { return field; }

	@Override
	public void computeFootprint(MemoryFootprint weight) {
		weight.reportInstance();
		weight.reportBytes(4); // int ordinal
	}

}
