package com.s8.io.bohr.lithium.fields.primitives;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
import com.s8.io.bohr.lithium.fields.LiFieldParser;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bohr.lithium.type.PublishScope;
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
public class FloatLiField extends PrimitiveLiField {


	public final static PrimitiveLiField.Prototype PROTOTYPE = new Prototype(float.class){

		@Override
		public PrimitiveLiField.Builder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new FloatLiField.Builder(properties, handler);
		}
	};


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
			return new FloatLiField(ordinal, properties, handler);
		}		
	}

	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws LiBuildException 
	 */
	public FloatLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException{
		super(ordinal, properties, handler);
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) {
		weight.reportBytes(4);
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		float value = handler.getFloat(origin);
		handler.setFloat(clone, value);
	}


	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws IOException {
		float baseValue = handler.getFloat(base);
		float updateValue = handler.getFloat(update);
		return baseValue != updateValue;
	}

	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (float)");
	}


	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		writer.write(Float.toString(handler.getFloat(object)));
	}



	/* <IO-inflow-section> */


	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.FLOAT32 : return new Float32_Parser();
		case BOHR_Types.FLOAT64 : return new Float64_Parser();

		default : throw new LiIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Parser extends LiFieldParser {

		@Override
		public FloatLiField getField() {
			return FloatLiField.this;
		}

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setFloat(object, deserialize(inflow));
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
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "float32" : return new Float32_Outflow(code);
		case "float64" : return new Float64_Outflow(code);
		default : throw new LiIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends LiFieldComposer {

		public Composer(int code) { super(code); }

		@Override
		public FloatLiField getField() {
			return FloatLiField.this;
		}


		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			serialize(outflow, handler.getFloat(object));
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
