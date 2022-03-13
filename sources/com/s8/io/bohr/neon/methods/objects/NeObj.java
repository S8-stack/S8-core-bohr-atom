package com.s8.io.bohr.neon.methods.objects;

import java.io.IOException;

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
public class NeObj<T extends NeObject> extends NeField {
	
	public final static long SIGNATURE =  BOHR_Types.S8OBJECT;

	public @Override long getSignature() { return SIGNATURE; }

	/**
	 * 
	 * @param view
	 */
	public NeObj(NeObjectPrototype prototype, String name) {
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
	public T get(NeValue wrapper) {
		return ((Value<T>) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void set(NeValue wrapper, T value) {
		((Value<T>) wrapper).value = value;
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
		
		private T value;
	
		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			S8Index.write(value != null ? value.getIndex() : null, outflow);
		}

		
		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			
			// get index
			S8Index index = S8Index.read(inflow);
			
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
