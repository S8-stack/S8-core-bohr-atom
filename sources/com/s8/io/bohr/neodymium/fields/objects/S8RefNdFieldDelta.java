package com.s8.io.bohr.neodymium.fields.objects;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8Ref;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 * @author pc
 *
 */
public class S8RefNdFieldDelta extends NdFieldDelta {


	public final S8RefNdField field;

	public final S8Ref<?> ref;


	/**
	 * 
	 * @param fieldCode
	 * @param field
	 * @param address
	 * @param slot
	 */
	public S8RefNdFieldDelta(S8RefNdField field, S8Ref<?> ref) {
		super();
		this.field = field;
		this.ref = ref;
	}

	
	@Override
	public void consume(S8Object object, BuildScope scope) throws NdIOException {
		field.handler.set(object, ref);
	}

	
	@Override
	public NdField getField() { 
		return field;
	}


	@Override
	public void computeFootprint(MemoryFootprint weight) {
		weight.reportBytes(1 + ref.address.length() + 8);
	}

}


