package com.s8.io.bohr.lithium.fields.objects;

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
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.EmbeddedTypeNature;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldBuilder;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
import com.s8.io.bohr.lithium.fields.LiFieldParser;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bohr.lithium.type.GraphCrawler;
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
public class S8TableLiField extends LiField {

	private static class Props extends LiFieldProperties {

		private Class<?> embeddedType;

		public Props(LiFieldPrototype prototype, int mode, Class<?> embeddedType) {
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


	public final static LiFieldPrototype PROTOTYPE = new LiFieldPrototype() {


		@Override
		public LiFieldProperties captureField(Field field) throws LiBuildException {
			Class<?> fieldType = field.getType();
			if(S8Table.class.equals(fieldType)) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {

					Type parameterType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					LiFieldProperties properties = new Props(this, LiFieldProperties.FIELD, typeArgument);
					properties.setFieldAnnotation(annotation);
					return properties;	
				}
				else { return null; }
			}
			else { return null; }
		}


		@Override
		public LiFieldProperties captureSetter(Method method) throws LiBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			S8Setter annotation = method.getAnnotation(S8Setter.class);
			if(annotation != null) {
				if(S8Table.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericParameterTypes()[0];
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					LiFieldProperties properties = new Props(this, LiFieldProperties.METHODS, typeArgument);
					properties.setSetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new LiBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);
				}
			}
			else { return null; }
		}

		@Override
		public LiFieldProperties captureGetter(Method method) throws LiBuildException {
			Class<?> baseType = method.getReturnType();

			S8Getter annotation = method.getAnnotation(S8Getter.class);
			if(annotation != null) {

				if(S8Table.class.isAssignableFrom(baseType)) {

					Type parameterType = method.getGenericReturnType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					LiFieldProperties properties = new Props(this, LiFieldProperties.METHODS, typeArgument);
					properties.setGetterAnnotation(annotation);
					return properties;
				}
				else {
					throw new LiBuildException("S8Annotated field of type List must have its "
							+"parameterized type inheriting from S8Object", method);
				}
			}
			else { return null; }
		}


		@Override
		public LiFieldBuilder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new Builder(properties, handler);
		}
	};


	private static class Builder extends LiFieldBuilder {

		public Builder(LiFieldProperties properties, LiHandler handler) {
			super(properties, handler);
		}

		@Override
		public LiFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public LiField build(int ordinal) throws LiBuildException {
			return new S8TableLiField(ordinal, properties, handler);
		}
	}




	public S8TableLiField(int ordinal, LiFieldProperties properties, LiHandler handler) {
		super(ordinal, properties, handler);
	}



	@Override
	public void sweep(LiS8Object object, GraphCrawler crawler) throws LiIOException {	
	}

	@Override
	public void collectReferencedBlocks(LiS8Object object, Queue<String> references) {
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
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {
		S8Table<?> value = (S8Table<?>) handler.get(object);
		weight.reportBytes(1 + value.address.length() + 8);
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		S8Table<?> value = (S8Table<?>) handler.get(origin);
		handler.set(clone, value);
	}


	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws LiIOException {
		S8Table<?> baseValue = (S8Table<?>) handler.get(base);
		S8Table<?> updateValue = (S8Table<?>) handler.get(update);
		return !S8Table.areEqual(baseValue, updateValue);
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Ref<?>)");
	}
	

	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
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

	public void setValue(Object object, S8Table<?> table) throws LiIOException {
		handler.set(object, table);
	}



	/* <IO-inflow-section> */

	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		int code;
		switch((code = inflow.getUInt8())){
		case BOHR_Types.S8TABLE : return new Inflow();
		default: throw new LiIOException("Unsupported code: "+Integer.toHexString(code));
		}
	}


	private class Inflow extends LiFieldParser {

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}

		public @Override LiField getField() { return S8TableLiField.this; }


		private S8Table<?> deserialize(ByteInflow inflow) throws IOException {
			return S8Table.read(inflow);
		}
	}

	/* </IO-inflow-section> */



	/* <IO-outflow-section> */

	@Override
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {

		case "table" : return new Outflow(code);

		default : throw new LiIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends LiFieldComposer {

		public Outflow(int code) {
			super(code);
		}

		@Override
		public LiField getField() { return S8TableLiField.this; }

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.S8TABLE);
		}

		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			S8Table<?> value = (S8Table<?>) handler.get(object);
			S8Table.write(value, outflow);
		}
	}
	/* </IO-outflow-section> */




	@Override
	public boolean isValueResolved(LiS8Object object) throws LiIOException {
		// TODO Auto-generated method stub
		return false;
	}




}
