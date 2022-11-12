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
public class LongNdField extends PrimitiveNdField {


	public final static PrimitiveNdField.Prototype PROTOTYPE = new Prototype(long.class){

		@Override
		public PrimitiveNdField.Builder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new LongNdField.Builder(properties, handler);
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
			return new LongNdField(ordinal, properties, handler);
		}		
	}

	
	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws NdBuildException 
	 */
	public LongNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException{
		super(ordinal, properties, handler);
	}


	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}

	
	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		long value = handler.getLong(origin);
		handler.setLong(clone, value);
	}

	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		long baseValue = handler.getLong(base);
		long updateValue = handler.getLong(update);
		return baseValue != updateValue;
	}

	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new LongNdFieldDelta(this, handler.getLong(object));
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

		case BOHR_Types.UINT8 : return new UInt8_NdFieldInflow();
		case BOHR_Types.UINT16 : return new UInt16_NdFieldInflow();
		case BOHR_Types.UINT32 : return new UInt32_NdFieldInflow();
		case BOHR_Types.UINT64 : return new UInt64_NdFieldInflow();

		case BOHR_Types.INT8 : return new Int8_NdFieldInflow();
		case BOHR_Types.INT16 : return new Int16_NdFieldInflow();
		case BOHR_Types.INT32 : return new Int32_NdFieldInflow();
		case BOHR_Types.INT64 : return new Int64_NdFieldInflow();

		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class LongNdFieldInflow extends NdFieldParser {

		@Override
		public LongNdField getField() {
			return LongNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setLong(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new LongNdFieldDelta(LongNdField.this, deserialize(inflow));
		}

		public abstract long deserialize(ByteInflow inflow) throws IOException;

	}

	private class UInt8_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getUInt8();
		}
	}

	private class UInt16_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getUInt16();
		}
	}
	private class UInt32_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getUInt8();
		}
	}

	private class UInt64_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getUInt16();
		}
	}

	private class Int8_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getInt8();
		}
	}

	private class Int16_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getInt16();
		}
	}
	private class Int32_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getInt8();
		}
	}

	private class Int64_NdFieldInflow extends LongNdFieldInflow {
		public @Override long deserialize(ByteInflow inflow) throws IOException {
			return (long) inflow.getInt16();
		}
	}


	/* </IO-inflow-section> */


	/* <IO-outflow-section> */
	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "uint8" : return new UInt8_NdFieldOutflow(code);
		case "uint16" : return new UInt16_NdFieldOutflow(code);
		case "uint32" : return new UInt32_NdFieldOutflow(code);
		case "uint64" : return new UInt64_NdFieldOutflow(code);

		case "int8" : return new Int8_NdFieldOutflow(code);
		case "int16" : return new Int16_NdFieldOutflow(code);
		case "int32" : return new Int32_NdFieldOutflow(code);
		case DEFAULT_FLOW_TAG: case "int64" : return new Int64_NdFieldOutflow(code);

		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {
		public Composer(int code) { super(code); }

		@Override
		public LongNdField getField() {
			return LongNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, handler.getLong(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((LongNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, long value) throws IOException;
	}


	private class UInt8_NdFieldOutflow extends Composer {
		public UInt8_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT8);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putUInt8((int) value);
		}
	}

	private class UInt16_NdFieldOutflow extends Composer {
		public UInt16_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT16);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putUInt16((int) value);
		}
	}

	private class UInt32_NdFieldOutflow extends Composer {
		public UInt32_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT32);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putUInt32((int) value);
		}
	}

	private class UInt64_NdFieldOutflow extends Composer {
		public UInt64_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT64);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putUInt64((int) value);
		}
	}

	private class Int8_NdFieldOutflow extends Composer {
		public Int8_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT8);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putInt8((byte) value);
		}
	}

	private class Int16_NdFieldOutflow extends Composer {
		public Int16_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT16);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putInt16((short) value);
		}
	}

	private class Int32_NdFieldOutflow extends Composer {
		public Int32_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT32);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putInt32((int) value);
		}
	}

	private class Int64_NdFieldOutflow extends Composer {
		public Int64_NdFieldOutflow(int code) { super(code); }
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.INT64);
		}
		public @Override void serialize(ByteOutflow outflow, long value) throws IOException {
			outflow.putInt64((int) value);
		}
	}

	/* <IO-outflow-section> */

}
