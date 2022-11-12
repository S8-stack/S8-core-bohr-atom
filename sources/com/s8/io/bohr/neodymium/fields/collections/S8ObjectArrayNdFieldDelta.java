package com.s8.io.bohr.neodymium.fields.collections;

import java.lang.reflect.Array;

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
public class S8ObjectArrayNdFieldDelta extends NdFieldDelta {

	
	
	public final S8ObjectArrayNdField field;
	
	public final String[] indices;

	/**
	 * 
	 * @param field
	 * @param indices
	 */
	public S8ObjectArrayNdFieldDelta(S8ObjectArrayNdField field, String[] indices) {
		super();
		this.field = field;
		this.indices = indices;
	}
	

	@Override
	public NdField getField() { 
		return field;
	}

	
	@Override
	public void consume(NdObject object, BuildScope scope) throws NdIOException {

		if(indices!=null) {
			int n = indices.length;
			NdObject[] array = new NdObject[n];


			scope.appendBinding(new BuildScope.Binding() {
				@Override
				public void resolve(BuildScope scope) throws NdIOException {
					for(int i=0; i<n; i++) {
						int index = i;
						Array.set(array, index, scope.retrieveObject(indices[i]));
					}
				}
			});	
			field.handler.set(object, array);
		}
		else {
			field.handler.set(object, null);
		}
	}


	@Override
	public void computeFootprint(MemoryFootprint weight) {
		if(indices!=null) {
			weight.reportInstance();
			weight.reportReferences(indices.length);	
		}
	}


}
