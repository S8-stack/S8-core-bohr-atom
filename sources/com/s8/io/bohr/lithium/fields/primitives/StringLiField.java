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
public class StringLiField extends PrimitiveLiField {


	public final static StringLiField.Prototype PROTOTYPE = new Prototype(String.class){

		@Override
		public PrimitiveLiField.Builder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new StringLiField.Builder(properties, handler);
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
			return new StringLiField(ordinal, properties, handler);
		}		
	}


	/**
	 * 
	 * @param outboundTypeName
	 * @param handler
	 * @throws LiBuildException 
	 */
	public StringLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException{
		super(ordinal, properties, handler);
	}
	

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}


	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {
		String value = handler.getString(object);
		if(value!=null) {
			weight.reportInstance();
			weight.reportBytes(value.length());
		}
	}

	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		String value = handler.getString(origin);
		handler.setString(clone, value);
	}


	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws IOException {
		String baseValue = handler.getString(base);
		String updateValue = handler.getString(update);
		if(baseValue==null && updateValue==null) {
			return false;
		}
		else if((baseValue!=null && updateValue==null) || (baseValue==null && updateValue!=null)) {
			return true;
		}
		else {
			return !baseValue.equals(updateValue);	
		}
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (String)");
	}



	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		String val = handler.getString(object);
		writer.write(val!=null ? val : "<null>");
	}


	/* <IO-inflow-section> */


	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {

		case BOHR_Types.STRING_UTF8 : return new UTF8_Inflow();

		default : throw new LiIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}



	private class UTF8_Inflow extends LiFieldParser {

		@Override
		public StringLiField getField() {
			return StringLiField.this;
		}

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.setString(object, inflow.getStringUTF8());
		}
	}

	/* </IO-inflow-section> */


	/* <IO-outflow-section> */

	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG:
		case "StringUTF8" : return new UTF8_Outflow(code);
		default : throw new LiIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private class UTF8_Outflow extends LiFieldComposer {

		public UTF8_Outflow(int code) {
			super(code);
		}

		@Override
		public StringLiField getField() {
			return StringLiField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.STRING_UTF8);
		}

		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			outflow.putStringUTF8(handler.getString(object));
		}
	}

	/* <IO-outflow-section> */	


}
