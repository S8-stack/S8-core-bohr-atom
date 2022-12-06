package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.BuildScope;
import com.s8.io.bohr.neon.core.NeObject;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.fields.NeFieldHandler;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NeList<T extends NeObject> extends NeFieldHandler {
	
	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }

	/**
	 * 
	 * @param view
	 */
	public NeList(NeObjectTypeHandler prototype, String name) {
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
	public List<T> get(NeFieldValue wrapper) {
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
	public void set(NeFieldValue wrapper, List<T> list) {
		Value<T> value = (Value<T>) wrapper; 
		value.list = list;
	}
	
	
	
	@Override
	public NeFieldValue createValue() {
		return new Value<>();
	}
	

	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Value<T extends NeObject> extends NeFieldValue {
		
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
					outflow.putStringUTF8(item != null ? item.vertex.getIndex() : null);
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
				List<String> indices = new ArrayList<String>(n);
				for(int i = 0; i < n; i++) { indices.add(inflow.getStringUTF8()); }
				
				scope.appendBinding(new BuildScope.Binding() {
					
					@Override
					public void resolve(BuildScope scope) throws IOException {
						list = new ArrayList<T>(n);
						String index;
						for(int i = 0; i < n; i++) {
							index = inflow.getStringUTF8();
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
