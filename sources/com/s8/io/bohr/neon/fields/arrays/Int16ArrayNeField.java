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
public class Int16ArrayNeField extends PrimitiveNeField {

	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.INT16;

	public @Override long getSignature() { return SIGNATURE; }


	public Int16ArrayNeField(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.ARRAY);
		outflow.putUInt8(BOHR_Types.INT16);
	}

	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public short[] get(NeValue wrapper) {
		return ((Value) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	public void set(NeValue wrapper, short[] value) {
		((Value) wrapper).value = value;
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
		
		private short[] value;
	
		public Value() {
			super();
		}


		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					outflow.putInt16(value[i]);		
				}
			}
			else {
				outflow.putUInt7x(-1);
			}
		}

		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >=0 ) {
				value = new short[length];
				for(int i=0; i<length; i++) {
					value[i] = inflow.getInt16();
				}
			}
			else {
				value = null;
			}
		}
	}
}
