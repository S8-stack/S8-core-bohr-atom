package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.neon.core.BuildScope;
import com.s8.io.bohr.neon.core.NeObject;
import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bohr.neon.fields.NeField;
import com.s8.io.bohr.neon.fields.NeValue;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NeList<T extends NeObject> extends NeField {
	
	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }

	/**
	 * 
	 * @param view
	 */
	public NeList(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.ARRAY);
		outflow.putUInt8(BOHR_Types.S8OBJECT);
	}

	

	/**
	 * 
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> get(NeValue wrapper) {
		Value<T> value = (Value<T>) wrapper; 
		if(value.list == null) {
			List<T> list = new ArrayList<T>();
			value.list = list;
			return list;
		}
		else {
			return value.list;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void set(NeValue wrapper, List<T> list) {
		Value<T> value = (Value<T>) wrapper; 
		value.list = list;
	}
	
	
	
	@Override
	public NeValue createValue() {
		return new Value<>();
	}
	

	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Value<T extends NeObject> extends NeValue {
		
		private List<T> list;
	
		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			if(list != null) {
				int n = list.size();
				outflow.putUInt7x(n);
				T item;
				for(int i = 0; i < n; i++) {
					item = list.get(i);
					S8Index.write(item != null ? item.getIndex() : null, outflow);
				}
			}
			else {
				outflow.putUInt7x(-1);
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			int n = (int) inflow.getUInt7x();
			if(n >= 0) {
				List<S8Index> indices = new ArrayList<S8Index>(n);
				for(int i = 0; i < n; i++) { indices.add(S8Index.read(inflow)); }
				
				scope.appendBinding(new BuildScope.Binding() {
					
					@Override
					public void resolve(BuildScope scope) throws IOException {
						list = new ArrayList<T>(n);
						S8Index index;
						for(int i = 0; i < n; i++) {
							index = S8Index.read(inflow);
							list.add(index != null ? (T) scope.retrieveObject(indices.get(i)) : null);
						}	
					}
				});
			}
			else {
				list = null;
			}
			
		}
	}	
}
