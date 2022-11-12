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
public class InterfaceNdField extends NdField {



	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype() {


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> fieldType = field.getType();
			S8Field annotation = field.getAnnotation(S8Field.class);
			if(annotation != null) {
				NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, fieldType);
				properties.setFieldAnnotation(annotation);
				return properties;	
			}
			else { return null; }
		}


		@Override
		public NdFieldProperties captureSetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			S8Setter annotation = method.getAnnotation(S8Setter.class);
			if(annotation != null) {
				NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
				properties.setSetterAnnotation(annotation);
				return properties;
			}
			else { return null; }
		}

		@Override
		public NdFieldProperties captureGetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getReturnType();

			S8Getter annotation = method.getAnnotation(S8Getter.class);
			if(annotation != null) {

				NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.METHODS, baseType);
				properties.setGetterAnnotation(annotation);
				return properties;

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
		public NdField build(int ordinal) {
			return new InterfaceNdField(ordinal, properties, handler);
		}
	}


	public InterfaceNdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super(ordinal, properties, handler);
	}





	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		try {
			NdObject fieldObject = (NdObject) handler.get(object);
			if(fieldObject!=null) {
				crawler.accept(fieldObject);
			}
		} 
		catch (NdIOException cause) {
			cause.printStackTrace();
		}
	}


	@Override
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		// No ext references
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Object)");
	}

	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {
		weight.reportReference();
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {
		NdObject value = (NdObject) handler.get(origin);
		if(value!=null) {
			String index = value.S8_index;

			scope.appendBinding(new BuildScope.Binding() {

				@Override
				public void resolve(BuildScope scope) throws NdIOException {

					// no need to upcast to S8Object
					NdObject indexedObject = scope.retrieveObject(index);
					if(indexedObject==null) {
						throw new NdIOException("Fialed to retriev vertex");
					}
					handler.set(clone, indexedObject);
				}
			});
		}
		else {
			handler.set(clone, null);
		}
	}


	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		NdObject baseValue = (NdObject) handler.get(base);
		NdObject updateValue = (NdObject) handler.get(update);
		if(baseValue == null && updateValue == null) {
			return false;
		}
		else if ((baseValue != null && updateValue == null) || (baseValue == null && updateValue != null)) {
			return true;
		}
		else {
			return !baseValue.S8_index.equals(updateValue.S8_index);
		}
	}



	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		NdObject value = (NdObject) handler.get(object);
		if(value != null) {
			return new InterfaceNdFieldDelta(this, value.S8_index);
		}
		else {
			return new InterfaceNdFieldDelta(this, null);
		}
	}




	@Override
	protected void printValue(NdObject object, Writer writer) throws IOException {
		NdObject value = (NdObject) handler.get(object);
		if(value!=null) {
			writer.write("(");
			writer.write(value.getClass().getCanonicalName());
			writer.write("): ");
			writer.write(value.S8_index.toString());	
		}
		else {
			writer.write("null");
		}
	}

	@Override
	public String printType() {
		return "S8Object";
	}

	public void setValue(Object object, NdObject struct) throws NdIOException {
		handler.set(object, struct);
	}





	@Override
	public boolean isValueResolved(NdObject object) {
		return true; // always resolved at resolve step in shell
	}



	/**
	 * 
	 * @author pierreconvert
	 *
	 * @param <T>
	 */
	private class ObjectBinding implements BuildScope.Binding {

		private Object object;

		private String id;

		public ObjectBinding(Object object, String id) {
			super();
			this.object = object;
			this.id = id;
		}

		@Override
		public void resolve(BuildScope scope) throws NdIOException {
			handler.set(object, scope.retrieveObject(id));
		}
	}



	/* <IO-inflow-section> */

	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch(code){
		case BOHR_Types.S8OBJECT : return new Inflow();
		default: throw new NdIOException("Unsupported code: "+Integer.toHexString(code));
		}
	}


	private class Inflow extends NdFieldParser {

		@Override
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
			String id = inflow.getStringUTF8();
			if(id != null) {
				/* append bindings */
				scope.appendBinding(new ObjectBinding(object, id));
			}
			else {
				// set list
				handler.set(object, null);	
			}
		}


		@Override
		public InterfaceNdField getField() {
			return InterfaceNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new InterfaceNdFieldDelta(InterfaceNdField.this, inflow.getStringUTF8());
		}
	}


	/* </IO-inflow-section> */



	/* <IO-outflow-section> */

	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {

		case DEFAULT_FLOW_TAG: case "obj[]" : return new Outflow(code);

		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Outflow extends NdFieldComposer {

		public Outflow(int code) {
			super(code);
		}

		@Override
		public NdField getField() {
			return InterfaceNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putUInt8(BOHR_Types.S8OBJECT);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			NdObject value = (NdObject) handler.get(object);
			outflow.putStringUTF8(value != null ? value.S8_index : null);
		}
		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			outflow.putStringUTF8(((InterfaceNdFieldDelta) delta).index);
		}
	}
	/* </IO-outflow-section> */
}
