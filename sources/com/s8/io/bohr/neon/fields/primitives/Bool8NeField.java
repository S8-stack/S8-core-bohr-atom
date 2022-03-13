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
public class Bool8NeField extends PrimitiveNeField {

	
	public final static long SIGNATURE = BOHR_Types.BOOL8;

	public @Override long getSignature() { return SIGNATURE; }



	/**
	 * 
	 * @param name
	 */
	public Bool8NeField(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}
	


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.BOOL8);
	}
	

	/**
	 * 
	 * @param values
	 * @return
	 */
	public boolean get(NeValue wrapper) {
		return ((Value) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	public void set(NeValue wrapper, boolean value) {
		((Value) wrapper).value = value;
	}
	


	/***
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException
	 */
	public static Boolean parse(ByteInflow inflow) throws IOException {
		return inflow.getBool8();
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

		private boolean value;

		public Value() {
			super();
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			outflow.putBool8(value);
		}
		
		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			value = inflow.getBool8();
		}
	}


}
