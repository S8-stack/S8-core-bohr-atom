package com.s8.io.bohr.neodymium.fields.objects;

import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.BuildScope.Binding;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 * @author pc
 *
 */
public class S8ObjectNdFieldDelta extends NdFieldDelta {



	public final S8ObjectNdField field;

	/**
	 * 
	 */
	public final S8Index index;


	/**
	 * 
	 * @param fieldCode
	 * @param field
	 * @param index
	 */
	public S8ObjectNdFieldDelta(S8ObjectNdField field, S8Index index) {
		super();
		this.field = field;
		this.index = index;
	}


	@Override
	public void consume(S8Object object, BuildScope scope) throws NdIOException {

		if(index != null) {
			scope.appendBinding(new Binding() {

				@Override
				public void resolve(BuildScope scope) throws NdIOException {
					S8Object struct = scope.retrieveObject(index);
					field.handler.set(object, struct);
				}
			});
		}
		else {
			// nothing to do
			field.handler.set(object, null);
		}
	}


	@Override
	public S8ObjectNdField getField() { 
		return field;
	}

	@Override
	public void computeFootprint(MemoryFootprint weight) {
		weight.reportReference();
	}

}
