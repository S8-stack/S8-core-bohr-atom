package com.s8.io.bohr.lithium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Properties;
import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.atom.BohrSerializable;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.fields.LiFieldBuilder;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
import com.s8.io.bohr.lithium.fields.LiFieldParser;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;
import com.s8.io.bohr.lithium.handlers.LiHandler;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.properties.LiFieldProperties;
import com.s8.io.bohr.lithium.properties.LiFieldProperties0T;
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
public class S8SerializableLiField extends LiField {
	


	public final static LiFieldPrototype PROTOTYPE = new LiFieldPrototype() {


		@Override
		public LiFieldProperties captureField(Field field) throws LiBuildException {
			Class<?> fieldType = field.getType();
			if(BohrSerializable.class.isAssignableFrom(fieldType)){
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					LiFieldProperties properties = new LiFieldProperties0T(this, LiFieldProperties.FIELD, fieldType);
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
				if(BohrSerializable.class.isAssignableFrom(baseType)) {
					LiFieldProperties properties = new LiFieldProperties0T(this, LiFieldProperties.METHODS, baseType);
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
				if(BohrSerializable.class.isAssignableFrom(baseType)){
					LiFieldProperties properties = new LiFieldProperties0T(this, LiFieldProperties.METHODS, baseType);
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
			return new S8SerializableLiField(ordinal, properties, handler);
		}
	}



	private BohrSerializable.S8SerialPrototype<?> deserializer;


	
	/**
	 * 
	 * @param properties
	 * @param handler
	 * @throws LiBuildException 
	 */
	public S8SerializableLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException {
		super(ordinal, properties, handler);
		Class<?> baseType = properties.getBaseType();
		try {
			deserializer = BohrSerializable.getDeserializer(baseType);
		} 
		catch (S8Exception e) {
			e.printStackTrace();
			throw new LiBuildException("Failed to build the S8Serizalizable GphField due to  "+e.getMessage() , baseType);
		}
	}





	@Override
	public void sweep(LiS8Object object, GraphCrawler crawler) {
		// no sweep
	}


	@Override
	public void collectReferencedBlocks(LiS8Object object, Queue<String> references) {
		// No ext references
	}





	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {
		BohrSerializable value = (BohrSerializable) handler.get(object);
		if(value!=null) {
			weight.reportInstance();
			weight.reportBytes(value.computeFootprint());	
		}
	}

	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		BohrSerializable value = (BohrSerializable) handler.get(origin);
		handler.set(clone, value.deepClone());
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (ByteSerializableFieldHandler)");
	}

	
	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws LiIOException {
		BohrSerializable baseValue = (BohrSerializable) handler.get(base);
		BohrSerializable updateValue = (BohrSerializable) handler.get(update);
		return (baseValue!=null && !baseValue.equals(updateValue)) || (baseValue==null && updateValue!=null);
	}


	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		Object value = handler.get(object);
		if(value!=null) {
			writer.write("(");
			writer.write(value.getClass().getCanonicalName());
			writer.write("): ");
			writer.write(value.toString());	
		}
		else {
			writer.write("null");
		}
	}

	@Override
	public String printType() {
		return "S8Object";
	}



	@Override
	public boolean isValueResolved(LiS8Object object) {
		return true; // always resolved at resolve step in shell
	}
	
	
	


	/* <IO-inflow-section> */

	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		if(code != BOHR_Types.SERIAL) {
			throw new IOException("only array accepted");
		}
		
		String signature = inflow.getStringUTF8();
		if(signature != deserializer.signature) {
			throw new IOException("Unsupported SERIAL: "+printType());
		}
		
		// in fine, create parser
		return new Parser();
	}
	

	private class Parser extends LiFieldParser {

		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}


		@Override
		public S8SerializableLiField getField() {
			return S8SerializableLiField.this;
		}
		
		private BohrSerializable deserialize(ByteInflow inflow) throws IOException {
			int props = inflow.getUInt8();
			if(isNonNull(props)) {
				return deserializer.deserialize(inflow);
			}
			else {
				return null;
			}
		}

	}
	
	/* </IO-inflow-section> */

	

	/* <IO-outflow-section> */
		
	@Override
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {

		case DEFAULT_FLOW_TAG: case "serial" : return new Outflow(code);

		default : throw new LiIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends LiFieldComposer {

		public Outflow(int code) { super(code); }

		@Override
		public LiField getField() {
			return S8SerializableLiField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.SERIAL);
			String signature = deserializer.signature;
			outflow.putStringUTF8(signature);
		}

		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			BohrSerializable value = (BohrSerializable) handler.get(object);
			if(value != null) {
				outflow.putUInt8(BOHR_Properties.IS_NON_NULL_PROPERTIES_BIT);
				value.serialize(outflow);
			}
			else {
				outflow.putUInt8(0x00);
			}
		}
	}
	/* </IO-outflow-section> */
	

}
