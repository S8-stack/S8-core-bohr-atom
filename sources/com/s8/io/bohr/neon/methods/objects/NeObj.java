package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;

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
public class NeObj<T extends NeObject> extends NeFieldHandler {
	
	public final static long SIGNATURE =  BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }

	/**
	 * 
	 * @param view
	 */
	public NeObj(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.S8OBJECT);
	}

	
	

	/**
	 * 
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(NeFieldValue wrapper) {
		return ((Value<T>) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void set(NeFieldValue wrapper, T value) {
		((Value<T>) wrapper).value = value;
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
		
		private T value;
	
		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			outflow.putStringUTF8(value != null ? value.vertex.getIndex() : null);
		}

		
		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			
			// get index
			String index = inflow.getStringUTF8();
			
			if(index != null) {
				scope.appendBinding(new BuildScope.Binding() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void resolve(BuildScope scope) throws IOException {
						Value.this.value = (T) scope.retrieveObject(index);
					}
				});
			}
			else {
				value = null;
			}
		}
	}	
}
