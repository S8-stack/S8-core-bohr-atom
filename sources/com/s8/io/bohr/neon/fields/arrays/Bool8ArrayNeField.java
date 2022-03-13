package com.s8.io.bohr.neon.fields.arrays;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.BuildScope;
import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bohr.neon.fields.NeValue;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * @author pierreconvert
 *
 */
public class Bool8ArrayNeField extends PrimitiveNeField {

	
	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.BOOL8;

	public @Override long getSignature() { return SIGNATURE; }



	/**
	 * 
	 * @param name
	 */
	public Bool8ArrayNeField(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}
	


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.ARRAY);
		outflow.putUInt8(BOHR_Types.BOOL8);
	}
	

	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean[] get(NeValue wrapper) {
		return ((Value) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	public void set(NeValue wrapper, boolean[] value) {
		((Value) wrapper).value = value;
	}
	


	/***
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException
	 */
	public static boolean[] parse(ByteInflow inflow) throws IOException {
		int n = (int) inflow.getUInt7x();
		if(n >= 0) {
			boolean[] value = new boolean[n];
			for(int i = 0; i<n; i++) { value[i] = inflow.getBool8(); }
			return value;
		}
		else {
			return null;
		}
	}
	

	@Override
	public NeValue createValue() {
		return new Value();
	}


	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Value extends PrimitiveNeField.Value {

		private boolean[] value;

		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			if(value != null) {
				int n = value.length;
				outflow.putUInt7x(n);
				for(int i = 0; i<n; i++) { outflow.putBool8(value[i]); }
			}
			else {
				outflow.putUInt7x(-1);
			}
		}
		
		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			int n = (int) inflow.getUInt7x();
			if(n >= 0) {
				value = new boolean[n];
				for(int i = 0; i<n; i++) { value[i] = inflow.getBool8(); }
			}
			else {
				value = null;
			}
		}
	}



}
