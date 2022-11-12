package com.s8.io.bohr.neodymium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldBuilder;
import com.s8.io.bohr.neodymium.fields.NdFieldComposer;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.fields.NdFieldParser;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties0T;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties1T;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.GraphCrawler;
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
public class EnumNdField extends NdField {



	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> baseType = field.getType();
			if(baseType.isEnum()) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					NdFieldProperties properties = new NdFieldProperties0T(this, NdFieldProperties.FIELD, baseType);
					properties.setFieldAnnotation(annotation);
					return properties;	
				}
				else { return null; }
			}
			else { return null; }
		}


		@Override
		public NdFieldProperties captureSetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			S8Setter annotation = method.getAnnotation(S8Setter.class);
			if(annotation != null) {
				if(baseType.isEnum()) {
					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
					properties.setSetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new NdBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);
				}
			}
			else { return null; }
		}

		@Override
		public NdFieldProperties captureGetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getReturnType();

			S8Getter annotation = method.getAnnotation(S8Getter.class);
			if(annotation != null) {
				if(baseType.isEnum()) {
					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
					properties.setGetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new NdBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);

				}
			}
			else { return null; }
		}


		@Override
		public NdFieldBuilder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new Builder(properties, handler);
		}
	};



	private static class Builder extends NdFieldBuilder {

		public Builder(NdFieldProperties properties, NdHandler handler) {
			super(properties, handler);
		}

		@Override
		public NdFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public NdField build(int ordinal) throws NdBuildException {
			return new EnumNdField(ordinal, properties, handler);
		}
	}



	private Class<?> enumType;

	private Object[] values;


	/**
	 * 
	 * @param ordinal
	 * @param properties
	 * @param handler
	 * @throws NdIOException
	 */
	public EnumNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
		this.enumType = properties.getBaseType();
		this.values = enumType.getEnumConstants();
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) {
		weight.reportInstance();
		weight.reportBytes(4); // int ordinal
	}


	@Override
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		//no blocks to collect
	}


	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		// nothing to collect
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": ("+enumType.getName()+")");
	}



	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		Object value = handler.get(origin);
		handler.set(clone, value);
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		Object baseValue = handler.get(base);
		Object updateValue = handler.get(update);
		return (baseValue!=null && !baseValue.equals(updateValue)) 
				|| (baseValue==null && updateValue!=null);
	}


	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new EnumNdFieldDelta(this, handler.get(object));
	}

	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		Object value = handler.get(object);
		if(value!=null) {
			Enum<?> enumValue = (Enum<?>) value;
			writer.write(enumValue.name());	
		}
		else {
			writer.write("null");
		}
	}


	@Override
	public String printType() {
		return enumType.getCanonicalName();
	}


	@Override
	public boolean isValueResolved(NdObject object) {
		return true; // always resolved
	}



	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code) {
		case BOHR_Types.UINT8 : return new UInt8_Inflow();
		case BOHR_Types.UINT16 : return new UInt16_Inflow();
		case BOHR_Types.UINT32 : return new UInt32_Inflow();
		default : throw new NdIOException("Failed to find field-inflow for code: "+Integer.toHexString(code));
		}
	}


	private abstract class Inflow extends NdFieldParser {

		@Override
		public EnumNdField getField() {
			return EnumNdField.this;
		}

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new EnumNdFieldDelta(EnumNdField.this, deserialize(inflow));
		}

		public abstract Object deserialize(ByteInflow inflow) throws IOException;

	}

	private class UInt8_Inflow extends Inflow {
		public @Override Object deserialize(ByteInflow inflow) throws IOException {
			return values[(int) inflow.getUInt8()];
		}
	}

	private class UInt16_Inflow extends Inflow {
		public @Override Object deserialize(ByteInflow inflow) throws IOException {
			return values[inflow.getUInt16()];
		}
	}

	private class UInt32_Inflow extends Inflow {
		public @Override Object deserialize(ByteInflow inflow) throws IOException {
			return values[inflow.getUInt32()];
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
		case DEFAULT_FLOW_TAG: 
			if(values.length<=0xff) {
				return new UInt8_Outflow(code);
			}
			else if(values.length<=0xffff) {
				return new UInt16_Outflow(code);
			}
			else {
				return new UInt32_Outflow(code);
			}
		default : throw new NdIOException("Failed to find field-outflow for encoding: "+flow);
		}
	}


	private abstract class Composer extends NdFieldComposer {

		public Composer(int code) {
			super(code);
		}


		@Override
		public EnumNdField getField() {
			return EnumNdField.this;
		}


		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			serialize(outflow, handler.get(object));
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((EnumNdFieldDelta) delta).value);
		}

		public abstract void serialize(ByteOutflow outflow, Object value) throws IOException;
	}


	private class UInt8_Outflow extends Composer {
		public UInt8_Outflow(int code) {
			super(code);
		}
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT8);
		}
		public @Override void serialize(ByteOutflow outflow, Object value) throws IOException {
			int code = ((Enum<?>) value).ordinal();
			outflow.putUInt8(code);
		}
	}

	private class UInt16_Outflow extends Composer {
		public UInt16_Outflow(int code) {
			super(code);
		}
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT16);
		}
		public @Override void serialize(ByteOutflow outflow, Object value) throws IOException {
			int code = ((Enum<?>) value).ordinal();
			outflow.putUInt16(code);
		}
	}

	private class UInt32_Outflow extends Composer {
		public UInt32_Outflow(int code) {
			super(code);
		}
		public @Override void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.UINT32);
		}
		public @Override void serialize(ByteOutflow outflow, Object value) throws IOException {
			int code = ((Enum<?>) value).ordinal();
			outflow.putUInt32(code);
		}
	}

	/* <IO-outflow-section> */
}
