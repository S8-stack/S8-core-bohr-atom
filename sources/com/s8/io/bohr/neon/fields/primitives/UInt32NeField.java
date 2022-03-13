package com.s8.io.bohr.neon.fields.primitives;

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
public class UInt32NeField extends PrimitiveNeField {

	
	public final static long SIGNATURE = BOHR_Types.UINT32;

	public @Override long getSignature() { return SIGNATURE; }



	public UInt32NeField(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.UINT32);
	}

	/**
	 * 
	 * @param values
	 * @return
	 */
	public long get(NeValue wrapper) {
		return ((Value) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	public void set(NeValue wrapper, long value) {
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
		
		private long value;
	
		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			outflow.putUInt32(value);
		}

		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			value = inflow.getUInt32();
		}
	}
}
