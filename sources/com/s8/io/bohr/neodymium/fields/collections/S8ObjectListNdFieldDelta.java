package com.s8.io.bohr.neodymium.fields.collections;

import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.BuildScope.Binding;
import com.s8.io.bytes.alpha.MemoryFootprint;




/**
 * <p> Internal object ONLY</p>
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class S8ObjectListNdFieldDelta<T extends NdObject> extends NdFieldDelta {

	
	public final S8ObjectListNdField<T> field;
	
	public final String[] indices;

	/**
	 * 
	 * @param field
	 * @param indices
	 */
	public S8ObjectListNdFieldDelta(S8ObjectListNdField<T> field, String[] indices) {
		super();
		this.field = field;
		this.indices = indices;
	}


	public @Override NdField getField() { return field; }


	@Override
	public void consume(NdObject object, BuildScope scope) throws NdIOException {

		if(indices!=null) {
			int n = indices.length;
			List<T> array = new ArrayList<>(n);


			scope.appendBinding(new Binding() {
				@SuppressWarnings("unchecked")
				@Override
				public void resolve(BuildScope scope) throws NdIOException {
					for(int i=0; i<n; i++) {
						String itemGphIndex = indices[i];
						if(itemGphIndex != null) {
							array.add((T) scope.retrieveObject(itemGphIndex));
						}
						else {
							array.add(null);
						}
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
