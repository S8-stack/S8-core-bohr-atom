package com.s8.io.bohr.neodymium.fields.primitives;

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
public class FloatNdField extends PrimitiveNdField {


	public final static PrimitiveNdField.Prototype PROTOTYPE = new Prototype(float.class){

		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new FloatNdField.Builder(properties, handler);
		}
	};


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
			return new FloatNdField(ordinal, properties, handler);
		}		
	}

	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws NdBuildException 
	 */
	public FloatNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException{
		super(ordinal, properties, handler);
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) {
		weight.reportBytes(4);
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		float value = handler.getFloat(origin);
		handler.setFloat(clone, value);
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		float baseValue = handler.getFloat(base);
		float updateValue = handler.getFloat(update);
		return baseValue != updateValue;
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new FloatNdFieldDelta(this, handler.getFloat(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (float)");
	}


	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		writer.write(Float.toString(handler.getFloat(object)));
	}



	/* <IO-inflow-section> */


	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.FLOAT32 : return new Float32_Parser();
		case BOHR_Types.FLOAT64 : return new Float64_Parser();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Parser extends NdFieldParser {

		@Override
		public FloatNdField getField() {
			return FloatNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setFloat(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new FloatNdFieldDelta(FloatNdField.this, deserialize(inflow));
		}

		public abstract float deserialize(ByteInflow inflow) throws IOException;

	}

	private class Float32_Parser extends Parser {
		public @Override float deserialize(ByteInflow inflow) throws IOException {
			return inflow.getFloat32();
		}
	}

	private class Float64_Parser extends Parser {
		public @Override float deserialize(ByteInflow inflow) throws IOException {
			return (float) inflow.getFloat64();
		}
	}
	/* </IO-inflow-section> */


	/* <IO-outflow-section> */
	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "float32" : return new Float32_Outflow(code);
		case "float64" : return new Float64_Outflow(code);
		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) { super(code); }

		@Override
		public FloatNdField getField() {
			return FloatNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, handler.getFloat(object));
		}

		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((FloatNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, float value) throws IOException;
	}


	private class Float32_Outflow extends Composer {
		public Float32_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.FLOAT32);
		}
		public @Override void serialize(ByteOutflow outflow, float value) throws IOException {
			outflow.putFloat32(value);
		}
	}

	private class Float64_Outflow extends Composer {
		public Float64_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.FLOAT64);
		}
		public @Override void serialize(ByteOutflow outflow, float value) throws IOException {
			outflow.putFloat64((double) value);
		}
	}

	/* <IO-outflow-section> */

}
