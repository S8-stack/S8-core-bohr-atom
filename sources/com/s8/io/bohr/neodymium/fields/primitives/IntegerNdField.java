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
public class IntegerNdField extends PrimitiveNdField {

	public final static PrimitiveNdField.Prototype PROTOTYPE = new Prototype(int.class){

		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new IntegerNdField.Builder(properties, handler);
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
			return new IntegerNdField(ordinal, properties, handler);
		}		
	}

	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws NdBuildException 
	 */
	public IntegerNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException{
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
		int value = handler.getInteger(origin);
		handler.setInteger(clone, value);
	}

	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		int baseValue = handler.getInteger(base);
		int updateValue = handler.getInteger(update);
		return baseValue != updateValue;
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new IntegerNdFieldDelta(this, handler.getInteger(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (long)");
	}



	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		writer.write(Long.toString(handler.getLong(object)));
	}


	/* <IO-inflow-section> */


	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.UINT8 : return new UInt8_Inflow();
		case BOHR_Types.UINT16 : return new UInt16_Inflow();
		case BOHR_Types.UINT32 : return new UInt32_Inflow();

		case BOHR_Types.INT8 : return new Int8_Inflow();
		case BOHR_Types.INT16 : return new Int16_Inflow();
		case BOHR_Types.INT32 : return new Int32_Inflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Inflow extends NdFieldParser {

		@Override
		public IntegerNdField getField() {
			return IntegerNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setInteger(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new IntegerNdFieldDelta(IntegerNdField.this, deserialize(inflow));
		}

		public abstract int deserialize(ByteInflow inflow) throws IOException;

	}

	private class UInt8_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return (int) inflow.getUInt8();
		}
	}

	private class UInt16_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return inflow.getUInt16();
		}
	}

	private class UInt32_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return inflow.getUInt8();
		}
	}

	private class Int8_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return inflow.getInt8();
		}
	}

	private class Int16_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return inflow.getInt16();
		}
	}

	private class Int32_Inflow extends Inflow {
		public @Override int deserialize(ByteInflow inflow) throws IOException {
			return inflow.getInt32();
		}
	}

	/* </IO-inflow-section> */


	/* <IO-outflow-section> */

	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "uint8" : return new UInt8_Outflow(code);
		case "uint16" : return new UInt16_Outflow(code);
		case "uint32" : return new UInt32_Outflow(code);

		case "int8" : return new Int8_Outflow(code);
		case "int16" : return new Int16_Outflow(code);
		case DEFAULT_FLOW_TAG: case "int32" : return new Int32_Outflow(code);

		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) {
			super(code);
		}


		@Override
		public IntegerNdField getField() {
			return IntegerNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, handler.getInteger(object));
		}

		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((IntegerNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, int value) throws IOException;
	}


	private class UInt8_Outflow extends Composer {
		public UInt8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT8);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putUInt8(value);
		}
	}

	private class UInt16_Outflow extends Composer {
		public UInt16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT16);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putUInt16(value);
		}
	}

	private class UInt32_Outflow extends Composer {
		public UInt32_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT32);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putUInt32(value);
		}
	}


	private class Int8_Outflow extends Composer {
		public Int8_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT8);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putInt8((byte) value);
		}
	}

	private class Int16_Outflow extends Composer {
		public Int16_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT16);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putInt16((short) value);
		}
	}

	private class Int32_Outflow extends Composer {
		public Int32_Outflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT32);
		}
		public @Override void serialize(ByteOutflow outflow, int value) throws IOException {
			outflow.putInt32(value);
		}
	}	
	/* <IO-outflow-section> */

}
