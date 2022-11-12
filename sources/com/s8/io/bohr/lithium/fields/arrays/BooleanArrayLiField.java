package com.s8.io.bohr.lithium.fields.arrays;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
import com.s8.io.bohr.lithium.fields.LiFieldParser;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;
import com.s8.io.bohr.lithium.fields.primitives.PrimitiveLiField;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bohr.lithium.type.PublishScope;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * later aggregate
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BooleanArrayLiField extends PrimitiveArrayLiField {

	/**
	 * 
	 */
	public final static Prototype PROTOTYPE = new Prototype(boolean[].class){
		@Override
		public PrimitiveLiField.Builder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new Builder(properties, handler);
		}
	};



	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	private static class Builder extends PrimitiveLiField.Builder {

		public Builder(LiFieldProperties properties, LiHandler handler) {
			super(properties, handler);
		}

		@Override
		public LiFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public LiField build(int ordinal) throws LiBuildException {
			return new BooleanArrayLiField(ordinal, properties, handler);
		}		
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	public BooleanArrayLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException {
		super(ordinal, properties, handler);
	}


	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {
		boolean[] array = (boolean[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance(); // the array object itself	
			weight.reportBytes(array.length*4);
		}
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		boolean[] array = (boolean[]) handler.get(origin);
		handler.set(clone, clone(array));
	}

	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws LiIOException {
		boolean[] baseValue = (boolean[]) handler.get(base);
		boolean[] updateValue = (boolean[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (boolean[])");
	}


	/**
	 * 
	 * @param base
	 * @return
	 */
	private boolean[] clone(boolean[] base) {
		if(base!=null) {
			int n = base.length;
			boolean[] copy = new boolean[n];
			for(int i=0; i<n; i++) {
				copy[i] = base[i];
			}
			return copy;
		}
		else {
			return null;
		}
	}



	private boolean areEqual(boolean[] array0, boolean[] array1) {

		// check nulls
		if(array0 == null) { return array1==null; }
		if(array1 == null) { return array0==null; }

		// check lengths
		int n0 = array0.length;
		int n1 = array1.length;
		if(n0!=n1) { return false; }

		// check values
		for(int i=0; i<n0; i++) {
			if(array0[i]!=array1[i]) { return false; }
		}
		return true;
	}





	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		boolean[] array = (boolean[]) handler.get(object);
		if(array!=null) {
			boolean isInitialized = false;
			writer.write('[');
			int n = array.length;
			for(int i=0; i<n; i++) {
				if(isInitialized) {
					writer.write(" ,");	
				}
				else {
					isInitialized = true;
				}
				writer.write(Boolean.toString(array[i]));
			}
			writer.write(']');
		}
		else {
			writer.write("null");
		}
	}


	
	/* <IO-inflow-section> */
	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {


		int code = inflow.getUInt8();
		if(code != BOHR_Types.ARRAY) {
			throw new IOException("Only array accepted");
		}

		switch(code = inflow.getUInt8()) {

		case BOHR_Types.BOOL8 : return new BOOL8_Inflow();

		default : throw new LiIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private class BOOL8_Inflow extends LiFieldParser {

		@Override
		public BooleanArrayLiField getField() {
			return BooleanArrayLiField.this;
		}

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		public boolean[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				boolean[] values = new boolean[length];
				for(int i = 0; i<length; i++) { values[i] = inflow.getBool8(); }
				return values;
			}
			else { return null; }
		}
	}



	/* </IO-inflow-section> */


	/* <IO-outflow-section> */

	@Override
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {

		case DEFAULT_FLOW_TAG: case "bool8[]" : return new BOOL8_Outflow(code);

		default : throw new LiIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private class BOOL8_Outflow extends LiFieldComposer {

		public BOOL8_Outflow(int code) { super(code); }

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.BOOL8);
		}

		@Override
		public BooleanArrayLiField getField() {
			return BooleanArrayLiField.this;
		}


		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			serialize(outflow, (boolean[]) handler.get(object));
		}

		public void serialize(ByteOutflow outflow, boolean[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putBool8(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}

	}

	/* <IO-outflow-section> */
}
