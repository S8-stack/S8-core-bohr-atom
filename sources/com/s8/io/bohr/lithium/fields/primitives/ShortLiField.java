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
public class ShortLiField extends PrimitiveLiField {


	public final static PrimitiveLiField.Prototype PROTOTYPE = new Prototype(short.class){

		@Override
		public PrimitiveLiField.Builder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new ShortLiField.Builder(properties, handler);
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
			return new ShortLiField(ordinal, properties, handler);
		}		
	}

	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws LiBuildException 
	 */
	public ShortLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException{
		super(ordinal, properties, handler);
	}

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (short)");
	}



	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		writer.write(Short.toString(handler.getShort(object)));
	}


	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) {
		weight.reportBytes(2);
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		short value = handler.getShort(origin);
		handler.setShort(clone, value);
	}


	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws IOException {
		short baseValue = handler.getShort(base);
		short updateValue = handler.getShort(update);
		return baseValue != updateValue;
	}


	/* <IO-inflow-section> */


	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.UINT8 : return new UInt8_Inflow();
		case BOHR_Types.UINT16 : return new UInt16_Inflow();

		case BOHR_Types.INT8 : return new Int8_Inflow();
		case BOHR_Types.INT16 : return new Int16_Inflow();

		default : throw new LiIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private abstract class Inflow extends LiFieldParser {

		@Override
		public ShortLiField getField() {
			return ShortLiField.this;
		}

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setShort(object, deserialize(inflow));
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
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {

		case "uint8" : return new UInt8_Outflow(code);
		case "uint16" : return new UInt16_Outflow(code);

		case "int8" : return new Int8_Outflow(code);
		
		case DEFAULT_FLOW_TAG: case "int16" : return new Int16_Outflow(code);

		default : throw new LiIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends LiFieldComposer {

		public Composer(int code) { super(code); }


		@Override
		public ShortLiField getField() {
			return ShortLiField.this;
		}


		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			serialize(outflow, handler.getShort(object));
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
