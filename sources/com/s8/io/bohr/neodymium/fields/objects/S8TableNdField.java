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
import com.s8.io.bohr.beryllium.object.S8Table;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.EmbeddedTypeNature;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldBuilder;
import com.s8.io.bohr.neodymium.fields.NdFieldComposer;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.fields.NdFieldParser;
import com.s8.io.bohr.neodymium.fields.NdFieldPrototype;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
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
public class S8TableNdField extends NdField {

	private static class Props extends NdFieldProperties {

		private Class<?> embeddedType;

		public Props(NdFieldPrototype prototype, int mode, Class<?> embeddedType) {
			super(prototype, mode);
			this.embeddedType = embeddedType;
		}


		public @Override EmbeddedTypeNature getEmbeddedTypeNature() { return EmbeddedTypeNature.S8_ROW; }
		public @Override Class<?> getEmbeddedType() { return embeddedType; }

		@Override
		public Class<?> getBaseType() {
			return embeddedType;
		}

		@Override
		public Class<?> getParameterType1() {
			return null;
		}

		@Override
		public Class<?> getParameterType2() {
			return null;
		}

	}


	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> fieldType = field.getType();
			if(S8Table.class.equals(fieldType)) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {

					Type parameterType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new Props(this, NdFieldProperties.FIELD, typeArgument);
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
				if(S8Table.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericParameterTypes()[0];
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new Props(this, NdFieldProperties.METHODS, typeArgument);
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

				if(S8Table.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericReturnType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					NdFieldProperties properties = new Props(this, NdFieldProperties.METHODS, typeArgument);
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
			return new S8TableNdField(ordinal, properties, handler);
		}
	}




	public S8TableNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
	}



	@Override
	public void sweep(NdObject object, GraphCrawler crawler) throws NdIOException {	
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
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		S8Table<?> value = (S8Table<?>) handler.get(object);
		weight.reportBytes(1 + value.address.length() + 8);
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		S8Table<?> value = (S8Table<?>) handler.get(origin);
		handler.set(clone, value);
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		S8Table<?> baseValue = (S8Table<?>) handler.get(base);
		S8Table<?> updateValue = (S8Table<?>) handler.get(update);
		return !S8Table.areEqual(baseValue, updateValue);
	}


	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new S8TableNdFieldDelta(this, (S8Table<?>) handler.get(object));
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Ref<?>)");
	}
	

	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		S8Table<?> value = (S8Table<?>) handler.get(object);
		if(value!=null) {
			writer.write("(");
			writer.write(value.getClass().getCanonicalName());
			writer.write("): ");
			writer.write(value.address);	
		}
		else {
			writer.write("null");
		}
	}

	@Override
	public String printType() {
		return "S8Table<?>";
	}

	public void setValue(Object object, S8Table<?> table) throws NdIOException {
		handler.set(object, table);
	}


	

	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code;
		switch((code = inflow.getUInt8())){
		case BOHR_Types.S8TABLE : return new Inflow();
		default: throw new NdIOException("Unsupported code: "+Integer.toHexString(code));
		}
	}


	private class Inflow extends NdFieldParser {

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		public @Override NdField getField() { return S8TableNdField.this; }

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8TableNdFieldDelta(S8TableNdField.this, deserialize(inflow));
		}

		private S8Table<?> deserialize(ByteInflow inflow) throws IOException {
			return S8Table.read(inflow);
		}
	}

	/* </IO-inflow-section> */



	/* <IO-outflow-section> */

	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case "table" : return new Outflow(code);

		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends NdFieldComposer {

		public Outflow(int code) {
			super(code);
		}

		@Override
		public NdField getField() { return S8TableNdField.this; }

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.S8TABLE);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			S8Table<?> value = (S8Table<?>) handler.get(object);
			S8Table.write(value, outflow);
		}

		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			S8Table.write(((S8TableNdFieldDelta) delta).table, outflow);
		}
	}
	/* </IO-outflow-section> */




	@Override
	public boolean isValueResolved(NdObject object) throws NdIOException {
		// TODO Auto-generated method stub
		return false;
	}




}
