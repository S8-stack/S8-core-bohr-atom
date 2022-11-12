package com.s8.io.bohr.neodymium.fields.arrays;


import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldComposer;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.fields.NdFieldParser;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;
import com.s8.io.bohr.neodymium.fields.primitives.PrimitiveNdField;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class StringArrayNdField extends PrimitiveArrayNdField {

	/**
	 * 
	 */
	public final static Prototype PROTOTYPE = new Prototype(String[].class){
		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new Builder(properties, handler);
		}
	};
	

	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	private static class Builder extends PrimitiveNdField.Builder {

		public Builder(NdFieldProperties properties, NdHandler handler) {
			super(properties, handler);
		}

		@Override
		public NdFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public NdField build(int ordinal) throws NdBuildException {
			return new StringArrayNdField(ordinal, properties, handler);
		}		
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}
	
	
	public StringArrayNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}
	


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		String[] array = (String[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance(); // the array object itself	
			int length = array.length;
			for(int i = 0; i<length; i++) {
				weight.reportInstance(); // the array object itself	
				weight.reportBytes(array[i].length()); // the array object itself	
			}
		}
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		String[] array = (String[]) handler.get(origin);
		handler.set(clone, clone(array));
	}

	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		String[] baseValue = (String[]) handler.get(base);
		String[] updateValue = (String[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new StringArrayNdFieldDelta(this, (String[]) handler.get(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (String[])");
	}


	/**
	 * 
	 * @param base
	 * @return
	 */
	private String[] clone(String[] base) {
		if(base!=null) {
			int n = base.length;
			String[] copy = new String[n];
			for(int i=0; i<n; i++) {
				copy[i] = base[i]; // String are immutables !
			}
			return copy;
		}
		else {
			return null;
		}
	}



	private boolean areEqual(String[] array0, String[] array1) {

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
	protected void printValue(NdObject object, Writer writer) throws IOException {
		String[] array = (String[]) handler.get(object);
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
				writer.write(array[i]);
			}
			writer.write(']');
		}
		else {
			writer.write("null");
		}
	}
	
	
	
	/* <IO-inflow-section> */


	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		
		
		int code = inflow.getUInt8();
		if(code != BOHR_Types.ARRAY) {
			throw new IOException("Only array accepted");
		}
		
		switch(code = inflow.getUInt8()) {

		case BOHR_Types.STRING_UTF8 : return new UTF8_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}
	


	private class UTF8_Inflow extends NdFieldParser {

		@Override
		public StringArrayNdField getField() {
			return StringArrayNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new StringArrayNdFieldDelta(StringArrayNdField.this, deserialize(inflow));
		}

		public String[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				String[] values = new String[length];
				for(int i = 0; i<length; i++) { values[i] = inflow.getStringUTF8(); }
				return values;
			}
			else { return null; }
		}
	}

	
	
	/* </IO-inflow-section> */
	

	/* <IO-outflow-section> */
	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case DEFAULT_FLOW_TAG: case "string_UTF8[]" : return new UTF8_Outflow(code);
		
		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private class UTF8_Outflow extends NdFieldComposer {

		public UTF8_Outflow(int code) {
			super(code);
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.STRING_UTF8);
		}
		
		@Override
		public StringArrayNdField getField() {
			return StringArrayNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, (String[]) handler.get(object));
		}
		
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((StringArrayNdFieldDelta) delta).value);
		}
		
		
		public void serialize(ByteOutflow outflow, String[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putStringUTF8(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}

	}

	/* <IO-outflow-section> */
	
}
