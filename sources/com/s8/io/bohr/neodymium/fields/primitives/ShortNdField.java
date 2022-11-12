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
public class ShortNdField extends PrimitiveNdField {


	public final static PrimitiveNdField.Prototype PROTOTYPE = new Prototype(short.class){

		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new ShortNdField.Builder(properties, handler);
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
			return new ShortNdField(ordinal, properties, handler);
		}		
	}

	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws NdBuildException 
	 */
	public ShortNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException{
		super(ordinal, properties, handler);
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}

	@Override
	public Delta produceDiff(NdObject object) throws NdIOException {
		return new Delta(handler.getShort(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (short)");
	}



	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		writer.write(Short.toString(handler.getShort(object)));
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) {
		weight.reportBytes(2);
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		short value = handler.getShort(origin);
		handler.setShort(clone, value);
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		short baseValue = handler.getShort(base);
		short updateValue = handler.getShort(update);
		return baseValue != updateValue;
	}


	/* <delta> */


	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public class Delta extends NdFieldDelta {


		private short value;

		public Delta(short value) {
			super();
			this.value = value;
		}

		public @Override NdField getField() { return ShortNdField.this; }

		@Override
		public void consume(NdObject object, BuildScope scope) throws NdIOException {
			handler.setShort(object, value);
		}

		@Override
		public void computeFootprint(MemoryFootprint weight) {
			weight.reportBytes(2);
		}

	}

	/* </delta> */




	/* <IO-inflow-section> */


	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.UINT8 : return new UInt8_Inflow();
		case BOHR_Types.UINT16 : return new UInt16_Inflow();

		case BOHR_Types.INT8 : return new Int8_Inflow();
		case BOHR_Types.INT16 : return new Int16_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Inflow extends NdFieldParser {

		@Override
		public ShortNdField getField() {
			return ShortNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setShort(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new Delta(deserialize(inflow));
		}

		public abstract short deserialize(ByteInflow inflow) throws IOException;

	}

	private class UInt8_Inflow extends Inflow {
		public @Override short deserialize(ByteInflow inflow) throws IOException {
			return (short) inflow.getUInt8();
		}
	}

	private class UInt16_Inflow extends Inflow {
		public @Override short deserialize(ByteInflow inflow) throws IOException {
			return (short) inflow.getUInt16();
		}
	}


	private class Int8_Inflow extends Inflow {
		public @Override short deserialize(ByteInflow inflow) throws IOException {
			return (short) inflow.getInt8();
		}
	}

	private class Int16_Inflow extends Inflow {
		public @Override short deserialize(ByteInflow inflow) throws IOException {
			return inflow.getInt16();
		}
	}

	/* </IO-inflow-section> */


	/* <IO-outflow-section> */
	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "uint8" : return new UInt8_Outflow(code);
		case "uint16" : return new UInt16_Outflow(code);

		case "int8" : return new Int8_Outflow(code);
		
		case DEFAULT_FLOW_TAG: case "int16" : return new Int16_Outflow(code);

		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) { super(code); }


		@Override
		public ShortNdField getField() {
			return ShortNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, handler.getShort(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((Delta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, short value) throws IOException;
	}


	private class UInt8_Outflow extends Composer {
		public UInt8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT8);
		}
		public @Override void serialize(ByteOutflow outflow, short value) throws IOException {
			outflow.putUInt8((int) value);
		}
	}

	private class UInt16_Outflow extends Composer {
		public UInt16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT16);
		}
		public @Override void serialize(ByteOutflow outflow, short value) throws IOException {
			outflow.putUInt16((int) value);
		}
	}


	private class Int8_Outflow extends Composer {
		public Int8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT8);
		}
		public @Override void serialize(ByteOutflow outflow, short value) throws IOException {
			outflow.putInt8((byte) value);
		}
	}

	private class Int16_Outflow extends Composer {
		public Int16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT16);
		}
		public @Override void serialize(ByteOutflow outflow, short value) throws IOException {
			outflow.putInt16(value);
		}
	}

	/* <IO-outflow-section> */



}
