package com.s8.io.bohr.neodymium.fields.collections;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

import com.s8.io.bohr.atom.S8Object;
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
public class S8ObjectArrayNdField extends CollectionNdField {



	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype(){


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> baseType = field.getType();
			if(baseType.isArray()) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(S8Object.class.isAssignableFrom(componentType)) {
						NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, componentType);
						properties.setFieldAnnotation(annotation);
						return properties;
					}
					else {
						throw new NdBuildException("S8Annotated field of type List must have its "
								+"parameterized type inheriting from S8Object", field);
					}
				}
				else { return null; }
			}
			else { return null; }
		}


		@Override
		public NdFieldProperties captureSetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			if(baseType.isArray()) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(S8Object.class.isAssignableFrom(componentType)) {
						NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, componentType);
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
			else { return null; }
		}

		@Override
		public NdFieldProperties captureGetter(Method method) throws NdBuildException {

			Class<?> baseType = method.getReturnType();
			if(baseType.isArray()) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(S8Object.class.isAssignableFrom(componentType)) {
						NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, componentType);
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
			else { return null; }
		}


		@Override
		public NdFieldBuilder createFieldBuilder(NdFieldProperties properties, NdHandler handler) {
			return new Builder<>(properties, handler);
		}
	};



	private static class Builder<T> extends NdFieldBuilder {

		public Builder(NdFieldProperties properties, NdHandler handler) {
			super(properties, handler);
		}

		@Override
		public NdFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public NdField build(int ordinal) throws NdBuildException {
			return new S8ObjectArrayNdField(ordinal, properties, handler);
		}
	}



	public S8ObjectArrayNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
		super(ordinal, properties, handler);
	}


	@Override
	public void sweep(S8Object object, GraphCrawler crawler) throws NdIOException {
		S8Object[] array = (S8Object[]) handler.get(object);
		if(array!=null) {
			for(S8Object item : array) {
				if(item!=null) {
					crawler.accept(item);
				}
			}
		}
	}


	@Override
	public void collectReferencedBlocks(S8Object object, Queue<String> references) {
		// No ext references
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Object[])");
	}

	@Override
	public void computeFootprint(S8Object object, MemoryFootprint weight) throws NdIOException {
		S8Object[] array = (S8Object[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance();
			weight.reportReferences(array.length);	
		}
	}


	@Override
	public void deepClone(S8Object origin, S8Object clone, BuildScope scope) throws NdIOException {
		S8Object[] value = (S8Object[]) handler.get(origin);
		if(value!=null) {
			int n = value.length;

			S8Object[] clonedArray = new S8Object[n];
			String[] indices = new String[n];
			for(int i=0; i<n; i++) {
				indices[i] = value[i].S8_index;
			}

			handler.set(clone, clonedArray);

			scope.appendBinding(new BuildScope.Binding() {

				@Override
				public void resolve(BuildScope scope) throws NdIOException {
					for(int i=0; i<n; i++) {
						// no need to upcast to S8Object
						S8Object indexedObject = scope.retrieveObject(indices[i]);
						if(indexedObject==null) {
							throw new NdIOException("Fialed to retriev vertex");
						}
						clonedArray[i] = indexedObject;
					}
				}
			});	
		}
		else {
			handler.set(clone, null);
		}

	}


	@Override
	public boolean hasDiff(S8Object base, S8Object update) throws NdIOException {
		S8Object[] baseValue = (S8Object[]) handler.get(base);
		S8Object[] updateValue = (S8Object[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}

	@Override
	public NdFieldDelta produceDiff(S8Object object) throws NdIOException {
		S8Object[] array = (S8Object[]) handler.get(object);
		String[] indices = null;
		if(array!=null) {
			int n = array.length;
			indices = new String[n];
			S8Object item;
			for(int i=0; i<n; i++) {
				item = array[i];
				indices[i] = item != null ? item.S8_index : null;
			}
		}
		return new S8ObjectArrayNdFieldDelta(this, indices);
	}




	private static boolean areEqual(S8Object[] array0, S8Object[] array1) {

		// check nulls
		if(array0 == null) { return array1==null; }
		if(array1 == null) { return array0==null; }

		// check lengths
		int n0 = array0.length;
		int n1 = array1.length;
		if(n0!=n1) { return false; }

		// check values
		S8Object obj0, obj1;
		for(int i=0; i<n0; i++) {
			obj0 = array0[i];
			obj1 = array1[i];
			if((obj0==null && obj1!=null) 
					
					// one is null while the other is non-null
					|| (obj1==null && obj0!=null) 
					
					// both non null with different indices
					|| (obj0!=null && obj1!=null && !obj0.S8_index.equals(obj1.S8_index))) { 
				return false; 
			}
		}
		return true;
	}



	@Override
	public String printType() {
		return "Object[]";
	}


	@Override
	public void forEach(Object iterable, ItemConsumer consumer) throws IOException {
		S8Object[] array = (S8Object[]) iterable;
		if(array!=null) {
			int n = array.length;
			for(int i=0; i<n; i++) {
				consumer.consume(array[i]);
			}
		}
	}

	@Override
	public boolean isValueResolved(S8Object object) {
		return false; // never resolved
	}




	/**
	 * 
	 * @author pierreconvert
	 *
	 * @param <T>
	 */
	private static class Binding implements BuildScope.Binding {

		private S8Object[] array;

		private String[] identifiers;


		public Binding(S8Object[] array, String[] indices) {
			super();
			this.array = array;
			this.identifiers = indices;
		}

		@Override
		public void resolve(BuildScope scope) throws NdIOException {
			int length = identifiers.length;
			for(int i=0; i<length; i++) {
				String graphId = identifiers[i];
				array[i] = scope.retrieveObject(graphId);
			}
		}
	}


	

	/* <IO-inflow-section> */
	@Override
	public NdFieldParser createParser(ByteInflow inflow) throws IOException {
		if(inflow.matches(SEQUENCE)) {
			return new Inflow();
		}
		else {
			throw new IOException("Only one possible encoding! ");
		}
	}


	private class Inflow extends NdFieldParser {


		@Override
		public void parseValue(S8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			String[] indices = deserializeIndices(inflow);
			if(indices != null) {
				S8Object[] array = new S8Object[indices.length];
				/* append bindings */
				scope.appendBinding(new Binding(array, indices));
				// set list
				handler.set(object, array);	
			}
			else {
				// set list
				handler.set(object, null);	
			}
		}


		@Override
		public S8ObjectArrayNdField getField() {
			return S8ObjectArrayNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8ObjectArrayNdFieldDelta(S8ObjectArrayNdField.this, deserializeIndices(inflow));
		}

		/**
		 * 
		 * @param inflow
		 * @return
		 * @throws IOException
		 */
		public String[] deserializeIndices(ByteInflow inflow) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >= 0) {

				/* <data> */
				String[] indices = new String[length];
				for(int index=0; index<length; index++) { indices[index] = inflow.getStringUTF8(); }
				/* </data> */

				/* append bindings */
				return indices;
			}
			else if(length == -1) {

				// set list
				return null;
			}
			else {
				throw new NdIOException("Illegal code for List object length");
			}
		}
	}
	/* </IO-inflow-section> */



	/* <IO-outflow-section> */
	@Override
	public NdFieldComposer createComposer(int code) throws NdIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "obj[]" : return new Composer(code);
		default : throw new NdIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Composer extends NdFieldComposer {

		public Composer(int code) {
			super(code);
		}

		@Override
		public NdField getField() {
			return S8ObjectArrayNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putByteArray(SEQUENCE);
		}

		@Override
		public void composeValue(S8Object object, ByteOutflow outflow) throws IOException {

			// array
			S8Object[] array = (S8Object[]) handler.get(object);
			if(array!=null) {
				int length = array.length;
				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					S8Object itemObject = array[i];
					outflow.putStringUTF8(itemObject != null ? itemObject.S8_index : null);
				}
			}
			else {
				// advertise NULL
				outflow.putUInt7x(-1);
			}
		}


		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((S8ObjectArrayNdFieldDelta) delta).indices);
		}


		public void serialize(ByteOutflow outflow, String[] indices) throws IOException {
			if(indices!=null) {
				int length = indices.length;
				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) { outflow.putStringUTF8(indices[i]); }
			}
			else {
				// advertise NULL
				outflow.putUInt7x(-1);
			}
		}
	}
	/* </IO-outflow-section> */
}
