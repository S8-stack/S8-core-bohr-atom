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
public class DoubleArrayNdField extends PrimitiveArrayNdField {

	/**
	 * 
	 */
	public final static Prototype PROTOTYPE = new Prototype(double[].class){
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
			return new DoubleArrayNdField(ordinal, properties, handler);
		}		
	}


	public @Override Prototype getPrototype() { return PROTOTYPE; }



	public DoubleArrayNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		double[] array = (double[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance(); // the array object itself	
			weight.reportBytes(array.length*4);
		}
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		double[] array = (double[]) handler.get(origin);
		handler.set(clone, clone(array));
	}

	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		double[] baseValue = (double[]) handler.get(base);
		double[] updateValue = (double[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new DoubleArrayNdFieldDelta(this, (double[]) handler.get(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (double[])");
	}


	/**
	 * 
	 * @param base
	 * @return
	 */
	private double[] clone(double[] base) {
		if(base!=null) {
			int n = base.length;
			double[] copy = new double[n];
			for(int i=0; i<n; i++) {
				copy[i] = base[i];
			}
			return copy;
		}
		else {
			return null;
		}
	}



	private boolean areEqual(double[] array0, double[] array1) {

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
		double[] array = (double[]) handler.get(object);
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
				writer.write(Double.toString(array[i]));
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

		case BOHR_Types.FLOAT32 : return new Float32_Inflow();
		case BOHR_Types.FLOAT64 : return new Float64_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Inflow extends NdFieldParser {

		@Override
		public DoubleArrayNdField getField() {
			return DoubleArrayNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new DoubleArrayNdFieldDelta(DoubleArrayNdField.this, deserialize(inflow));
		}

		public abstract double[] deserialize(ByteInflow inflow) throws IOException;

	}

	private class Float32_Inflow extends Inflow {
		public @Override double[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				double[] values = new double[length];
				for(int i = 0; i<length; i++) { values[i] = inflow.getFloat32(); }
				return values;
			}
			else { return null; }
		}
	}

	private class Float64_Inflow extends Inflow {
		public @Override double[] deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {
				double[] values = new double[length];
				for(int i = 0; i<length; i++) { values[i] = inflow.getFloat64(); }
				return values;
			}
			else { return null; }
		}
	}

	/* </IO-inflow-section> */


	/* <IO-outflow-section> */

	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "float32[]" : return new Float32_Outflow(code);
		case DEFAULT_FLOW_TAG: case "float64[]" : return new Float64_Outflow(code);

		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) { super(code); }

		@Override
		public DoubleArrayNdField getField() {
			return DoubleArrayNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, (double[]) handler.get(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((DoubleArrayNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, double[] value) throws IOException;
	}


	private class Float32_Outflow extends Composer {
		public Float32_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.FLOAT32);
		}
		public @Override void serialize(ByteOutflow outflow, double[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putFloat32((float) value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}

	private class Float64_Outflow extends Composer {
		public Float64_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.ARRAY);
			outflow.putUInt8(BOHR_Types.FLOAT64);
		}
		public @Override void serialize(ByteOutflow outflow, double[] value) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i = 0; i<length; i++) { outflow.putFloat64(value[i]); }
			}
			else { outflow.putUInt7x(-1); }
		}
	}

	/* <IO-outflow-section> */
}
