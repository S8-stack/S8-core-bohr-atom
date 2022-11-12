package com.s8.io.bohr.neodymium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import com.s8.io.bohr.neodymium.object.NdRef;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
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
public class S8RefNdField extends NdField {


	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> fieldType = field.getType();
			if(NdRef.class.equals(fieldType)) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {

					Type parameterType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, typeArgument);
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
				if(NdRef.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericParameterTypes()[0];
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, typeArgument);
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

				if(NdRef.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericReturnType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, typeArgument);
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
			return new S8RefNdField(ordinal, properties, handler);
		}
	}




	/**
	 * 
	 * @param properties
	 * @param handler
	 * @throws NdBuildException 
	 */
	public S8RefNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}




	@Override
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		/*
		try {
			BkRef<?> ref = (BkRef<?>) field.get(object);

			// collect block ONLY is reference is not resolve at this point
			if(ref.isResolved()) {
				NgObject_i0 value = ref.get();
				if(value!=null) {
					Block block = value.getHandle().getBlock();
					if(!block.flag) {
						block.flag = true;
						references.add(block);
					}
				}	
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		 */
	}

	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		// nothing to collect here
	}



	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		NdRef<?> value = (NdRef<?>) handler.get(object);
		weight.reportBytes(1 + value.address.length() + 8);
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		handler.set(clone, (NdRef<?>) handler.get(origin));
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		NdRef<?> baseValue = (NdRef<?>) handler.get(base);
		NdRef<?> updateValue = (NdRef<?>) handler.get(update);
		return !NdRef.areEqual(baseValue, updateValue);
	}


	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new S8RefNdFieldDelta(this, (NdRef<?>) handler.get(object));
	}



	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Ref<?>)");
	}




	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		NdRef<?> value = (NdRef<?>) handler.get(object);
		if(value!=null) {
			writer.write(value.toString());
		}
		else {
			writer.write("null");
		}
	}

	@Override
	public String printType() {
		return "S8Ref<?>";
	}



	




	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code;
		switch((code = inflow.getUInt8())){
		case BOHR_Types.S8REF : return new Inflow();
		default: throw new NdIOException("Unsupported code: "+Integer.toHexString(code));
		}
	}


	private class Inflow extends NdFieldParser {

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}


		@Override
		public S8RefNdField getField() {
			return S8RefNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8RefNdFieldDelta(S8RefNdField.this, deserialize(inflow));
		}


		private NdRef<?> deserialize(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length > 0) {
				//byte[] bytes = inflow.getByteArray(length);
				return null;
			}
			else {
				return null;
			}
		}
	}

	/* </IO-inflow-section> */



	/* <IO-outflow-section> */

	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "obj[]" : return new Outflow(code);

		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends NdFieldComposer {

		public Outflow(int code) {
			super(code);
		}

		@Override
		public NdField getField() {
			return S8RefNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.S8REF);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			NdRef<?> value = (NdRef<?>) handler.get(object);
			NdRef.write(value, outflow);
		}

		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			NdRef<?> value = ((S8RefNdFieldDelta) delta).ref;
			NdRef.write(value, outflow);
		}
	}
	/* </IO-outflow-section> */




	@Override
	public boolean isValueResolved(NdObject object) throws NdIOException {
		// TODO Auto-generated method stub
		return false;
	}


}


