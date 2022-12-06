package com.s8.io.bohr.neon.fields.objects;

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
public class ListNeFieldHandler<T extends NeObject> extends NeFieldHandler {

	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }

	/**
	 * 
	 * @param view
	 */
	public ListNeFieldHandler(NeObjectTypeHandler prototype, String name) {
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
	
	public List<T> get(NeFieldValue wrapper) {
		@SuppressWarnings("unchecked")
		Value<T> value = (Value<T>) wrapper; 
		if(value.list == null) {
			List<T> list = new ArrayList<T>();
			value.list = list;
			return list;
		}
		else {
			List<T> list = value.list;
			List<T> copy = new ArrayList<T>(list.size());
			list.forEach(item -> copy.add(item));
			return copy;
		}
	}

	@SuppressWarnings("unchecked")
	public void set(NeFieldValue wrapper, List<T> list) {
		((Value<T>) wrapper).setValue(list);
	}




	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public void add(NeFieldValue wrapper, T obj) {
		((Value<T>) wrapper).addObject(obj);
	}
	

	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param obj
	 */
	@SuppressWarnings("unchecked")
	public void remove(NeFieldValue wrapper, String index) {
		((Value<T>) wrapper).removeObject(index);
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

		public void notifyChange() {
			hasDelta = true;
		}

		public void setValue(List<T> value) {
			this.list = value;
			this.hasDelta = true;
		}
		
		
		public void addObject(T obj) {
			if(list == null) { list = new ArrayList<T>(); }
			list.add(obj);
			hasDelta = true;
		}
		
		public void removeObject(String objectIndex) {
			if(list != null) {
				boolean isFound = false;
				int i = 0, n = list.size();
				while(!isFound && i < n) {
					if(list.get(i).vertex.getIndex().equals(objectIndex)) {
						isFound = true;
					}
					else {
						i++;
					}
				}
				list.remove(i);
				
				hasDelta = true;		
			}
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
							index = indices.get(i);
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
