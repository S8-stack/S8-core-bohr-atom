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
public class ShortArrayNdField extends PrimitiveArrayNdField {




	/**
	 * 
	 */
	public final static Prototype PROTOTYPE = new Prototype(short[].class){
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
			return new ShortArrayNdField(ordinal, properties, handler);
		}		
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}



	public ShortArrayNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}
	

	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		short[] array = (short[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance(); // the array object itself	
			weight.reportBytes(array.length*2);
		}
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		short[] array = (short[]) handler.get(origin);
		handler.set(clone, clone(array));
	}

	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		short[] baseValue = (short[]) handler.get(base);
		short[] updateValue = (short[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new ShortArrayNdFieldDelta(this, (short[]) handler.get(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (long[])");
	}


	/**
	 * 
	 * @param base
	 * @return
	 */
	private short[] clone(short[] base) {
		if(base!=null) {
			int n = base.length;
			short[] copy = new short[n];
			for(int i=0; i<n; i++) {
				copy[i] = base[i];
			}
			return copy;
		}
		else {
			return null;
		}
	}



	private boolean areEqual(short[] array0, short[] array1) {

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
		short[] array = (short[]) handler.get(object);
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
				writer.write(Short.toString(array[i]));
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

		case BOHR_Types.UINT8 : return new UInt8_Inflow();
		case BOHR_Types.UINT16 : return new UInt16_Inflow();

		case BOHR_Types.INT8 : return new Int8_Inflow();
		case BOHR_Types.INT16 : return new Int16_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Inflow extends NdFieldParser {

		@Override
		public ShortArrayNdField getField() {
			return ShortArrayNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new ShortArrayNdFieldDelta(ShortArrayNdField.this, deserialize(inflow));
		}

		public abstract short[] deserialize(ByteInflow inflow) throws IOException;

	}

	private class UInt8_Inflow extends Inflow {
		public @Override short[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] values = new short[length];
				for(int i = 0; i<length; i++) { values[i] = (short) inflow.getUInt8(); }
				return values;
			}
			else { return null; }
		}
	}

	private class UInt16_Inflow extends Inflow {
		public @Override short[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] values = new short[length];
				for(int i = 0; i<length; i++) { values[i] = (short) inflow.getUInt16(); }
				return values;
			}
			else { return null; }
		}
	}



	private class Int8_Inflow extends Inflow {
		public @Override short[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] values = new short[length];
				for(int i = 0; i<length; i++) { values[i] = (short) inflow.getInt8(); }
				return values;
			}
			else { return null; }
		}
	}

	private class Int16_Inflow extends Inflow {
		public @Override short[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				short[] values = new short[length];
				for(int i = 0; i<length; i++) { values[i] = inflow.getInt16(); }
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

		case "uint8[]" : return new UInt8_Outflow(code);
		case "uint16[]" : return new UInt16_Outflow(code);

		case "int8[]" : return new Int8_Outflow(code);
		case "int16[]" : return new Int16_Outflow(code);

		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) {
			super(code);
		}


		@Override
		public ShortArrayNdField getField() {
			return ShortArrayNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, (short[]) handler.get(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((ShortArrayNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, short[] value) throws IOException;
	}


	private class UInt8_Outflow extends Composer {
		public UInt8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.UINT8);
		}
		public @Override void serialize(ByteOutflow outflow, short[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putUInt8(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}

	private class UInt16_Outflow extends Composer {
		public UInt16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.UINT16);
		}
		public @Override void serialize(ByteOutflow outflow, short[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putUInt16(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}



	private class Int8_Outflow extends Composer {
		public Int8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.INT8);
		}
		public @Override void serialize(ByteOutflow outflow, short[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putInt8((byte) value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}

	private class Int16_Outflow extends Composer {
		public Int16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.INT16);
		}
		public @Override void serialize(ByteOutflow outflow, short[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putInt16(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}

	/* <IO-outflow-section> */
}
