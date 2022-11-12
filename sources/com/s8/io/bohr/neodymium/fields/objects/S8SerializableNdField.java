package com.s8.io.bohr.neodymium.fields.objects;

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
public class S8SerializableNdField extends NdField {



	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> fieldType = field.getType();
			if(BohrSerializable.class.isAssignableFrom(fieldType)){
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					NdFieldProperties properties = new NdFieldProperties0T(this, NdFieldProperties.FIELD, fieldType);
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
				if(BohrSerializable.class.isAssignableFrom(baseType)) {
					NdFieldProperties properties = new NdFieldProperties0T(this, NdFieldProperties.METHODS, baseType);
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
				if(BohrSerializable.class.isAssignableFrom(baseType)){
					NdFieldProperties properties = new NdFieldProperties0T(this, NdFieldProperties.METHODS, baseType);
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
			return new S8SerializableNdField(ordinal, properties, handler);
		}
	}



	private BohrSerializable.S8SerialPrototype<?> deserializer;



	/**
	 * 
	 * @param properties
	 * @param handler
	 * @throws NdBuildException 
	 */
	public S8SerializableNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
		Class<?> baseType = properties.getBaseType();
		try {
			deserializer = BohrSerializable.getDeserializer(baseType);
		} 
		catch (S8Exception e) {
			e.printStackTrace();
			throw new NdBuildException("Failed to build the S8Serizalizable GphField due to  "+e.getMessage() , baseType);
		}
	}





	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		// no sweep
	}


	@Override
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		// No ext references
	}





	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		BohrSerializable value = (BohrSerializable) handler.get(object);
		if(value!=null) {
			weight.reportInstance();
			weight.reportBytes(value.computeFootprint());	
		}
	}

	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		BohrSerializable value = (BohrSerializable) handler.get(origin);
		handler.set(clone, value.deepClone());
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (ByteSerializableFieldHandler)");
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		BohrSerializable baseValue = (BohrSerializable) handler.get(base);
		BohrSerializable updateValue = (BohrSerializable) handler.get(update);
		return (baseValue!=null && !baseValue.equals(updateValue)) || (baseValue==null && updateValue!=null);
	}


	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		return new S8SerializableNdFieldDelta(S8SerializableNdField.this, (BohrSerializable) handler.get(object));
	}


	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
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
	public boolean isValueResolved(NdObject object) {
		return true; // always resolved at resolve step in shell
	}




	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		if(code != BOHR_Types.SERIAL) {
			throw new IOException("only array accepted");
		}

		String signature = deserializer.signature;
		if(signature != deserializer.signature) {
			throw new NdIOException("Unsupported SERIAL: "+printType());
		}

		// in fine, create parser
		return new Parser();
	}


	private class Parser extends NdFieldParser {

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			handler.set(object, deserialize(inflow));
		}


		@Override
		public S8SerializableNdField getField() {
			return S8SerializableNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8SerializableNdFieldDelta(S8SerializableNdField.this, deserialize(inflow));
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
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case DEFAULT_FLOW_TAG: case "serial" : return new Outflow(code);

		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends NdFieldComposer {

		public Outflow(int code) { super(code); }

		@Override
		public NdField getField() {
			return S8SerializableNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.SERIAL);
			outflow.putStringUTF8(deserializer.signature);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			BohrSerializable value = (BohrSerializable) handler.get(object);
			if(value != null) {
				outflow.putUInt8(BOHR_Properties.IS_NON_NULL_PROPERTIES_BIT);
				value.serialize(outflow);
			}
			else {
				outflow.putUInt8(0x00);
			}
		}

		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			BohrSerializable value = ((S8SerializableNdFieldDelta) delta).value;
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
