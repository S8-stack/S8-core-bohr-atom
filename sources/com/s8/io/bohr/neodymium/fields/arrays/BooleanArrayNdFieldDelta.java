package com.s8.io.bohr.neodymium.fields.arrays;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * later aggregate
 * 
 * @author pc
 *
 */
public class BooleanArrayNdFieldDelta extends NdFieldDelta {


	public final BooleanArrayNdField field;

	public final boolean[] value;

	public BooleanArrayNdFieldDelta(BooleanArrayNdField field, boolean[] array) {
		super();
		this.field = field;
		this.value = array;
	}

	@Override
	public NdField getField() { 
		return field;
	}


	@Override
	public void consume(S8Object object, BuildScope scope) throws NdIOException {
		field.handler.set(object, value);
	}

	@Override
	public void computeFootprint(MemoryFootprint weight) {
		if(value!=null) {
			weight.reportInstance(); // the array object itself	
			weight.reportBytes(value.length/8);
		}
	}

}
