package com.s8.io.bohr.neodymium.fields.primitives;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.S8BuildException;
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
public class BooleanNdField extends PrimitiveNdField {

	public final static PrimitiveNdField.Prototype PROTOTYPE = new Prototype(boolean.class){

		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new BooleanNdField.Builder(properties, handler);
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
			return new BooleanNdField(ordinal, properties, handler);
		}
	}



	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws S8BuildException 
	 */
	public BooleanNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) {
		weight.reportBytes(1);
	}

	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		boolean value = handler.getBoolean(origin);
		handler.setBoolean(clone, value);
	}

	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent + name + ": (boolean)");
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		boolean baseValue = handler.getBoolean(base);
		boolean updateValue = handler.getBoolean(update);
		return baseValue != updateValue;
	}


	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new BooleanNdFieldDelta(this, handler.getBoolean(object));
	}


	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		writer.write(Boolean.toString(handler.getBoolean(object)));
	}

	

	



	/* <IO-inflow-section> */


	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.BOOL8 : return new Bool8_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private class Bool8_Inflow extends NdFieldParser {

		@Override
		public BooleanNdField getField() {
			return BooleanNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setBoolean(object, inflow.getBool8());
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new BooleanNdFieldDelta(BooleanNdField.this, inflow.getBool8());
		}
	}

	/* </IO-inflow-section> */


	/* <IO-outflow-section> */



	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "bool8" : return new Bool8_Outflow(code);
		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}	
	}
	
	
	private class Bool8_Outflow extends NdFieldComposer {

		public Bool8_Outflow(int code) { super(code); }

		@Override
		public BooleanNdField getField() {
			return BooleanNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.BOOL8);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			outflow.putBool8(handler.getBoolean(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			outflow.putBool8(((BooleanNdFieldDelta) delta).value);
		}
	}

	/* <IO-outflow-section> */	


}
