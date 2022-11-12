package com.s8.io.bohr.lithium.fields.collections;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Queue;

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
import com.s8.io.bohr.lithium.properties.LiFieldProperties1T;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bohr.lithium.type.GraphCrawler;
import com.s8.io.bohr.lithium.type.PublishScope;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class S8ObjectArrayLiField extends CollectionLiField {



	public final static LiFieldPrototype PROTOTYPE = new LiFieldPrototype(){


		@Override
		public LiFieldProperties captureField(Field field) throws LiBuildException {
			Class<?> baseType = field.getType();
			if(baseType.isArray()) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(LiS8Object.class.isAssignableFrom(componentType)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.FIELD, componentType);
						properties.setFieldAnnotation(annotation);
						return properties;
					}
					else {
						throw new LiBuildException("S8Annotated field of type List must have its "
								+"parameterized type inheriting from S8Object", field);
					}
				}
				else { return null; }
			}
			else { return null; }
		}


		@Override
		public LiFieldProperties captureSetter(Method method) throws LiBuildException {
			Class<?> baseType = method.getParameterTypes()[0];
			if(baseType.isArray()) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(LiS8Object.class.isAssignableFrom(componentType)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.FIELD, componentType);
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
			else { return null; }
		}

		@Override
		public LiFieldProperties captureGetter(Method method) throws LiBuildException {

			Class<?> baseType = method.getReturnType();
			if(baseType.isArray()) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					Class<?> componentType = baseType.getComponentType();
					if(LiS8Object.class.isAssignableFrom(componentType)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.FIELD, componentType);
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
			else { return null; }
		}


		@Override
		public LiFieldBuilder createFieldBuilder(LiFieldProperties properties, LiHandler handler) {
			return new Builder<>(properties, handler);
		}
	};



	private static class Builder<T> extends LiFieldBuilder {

		public Builder(LiFieldProperties properties, LiHandler handler) {
			super(properties, handler);
		}

		@Override
		public LiFieldPrototype getPrototype() {
			return PROTOTYPE;
		}

		@Override
		public LiField build(int ordinal) throws LiBuildException {
			return new S8ObjectArrayLiField(ordinal, properties, handler);
		}
	}



	public S8ObjectArrayLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException {
		super(ordinal, properties, handler);
	}


	@Override
	public void sweep(LiS8Object object, GraphCrawler crawler) throws LiIOException {
		LiS8Object[] array = (LiS8Object[]) handler.get(object);
		if(array!=null) {
			for(LiS8Object item : array) {
				if(item!=null) {
					crawler.accept(item);
				}
			}
		}
	}


	@Override
	public void collectReferencedBlocks(LiS8Object object, Queue<String> references) {
		// No ext references
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (S8Object[])");
	}

	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {
		LiS8Object[] array = (LiS8Object[]) handler.get(object);
		if(array!=null) {
			weight.reportInstance();
			weight.reportReferences(array.length);	
		}
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {
		LiS8Object[] value = (LiS8Object[]) handler.get(origin);
		if(value!=null) {
			int n = value.length;

			LiS8Object[] clonedArray = new LiS8Object[n];
			String[] indices = new String[n];
			for(int i=0; i<n; i++) {
				indices[i] = value[i].S8_index;
			}

			handler.set(clone, clonedArray);

			scope.appendBinding(new BuildScope.Binding() {

				@Override
				public void resolve(BuildScope scope) throws LiIOException {
					for(int i=0; i<n; i++) {
						// no need to upcast to S8Object
						LiS8Object indexedObject = scope.retrieveObject(indices[i]);
						if(indexedObject==null) {
							throw new LiIOException("Fialed to retriev vertex");
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
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws IOException {
		LiS8Object[] baseValue = (LiS8Object[]) handler.get(base);
		LiS8Object[] updateValue = (LiS8Object[]) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}



	private static boolean areEqual(LiS8Object[] array0, LiS8Object[] array1) {

		// check nulls
		if(array0 == null) { return array1==null; }
		if(array1 == null) { return array0==null; }

		// check lengths
		int n0 = array0.length;
		int n1 = array1.length;
		if(n0!=n1) { return false; }

		// check values
		LiS8Object obj0, obj1;
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
		LiS8Object[] array = (LiS8Object[]) iterable;
		if(array!=null) {
			int n = array.length;
			for(int i=0; i<n; i++) {
				consumer.consume(array[i]);
			}
		}
	}

	@Override
	public boolean isValueResolved(LiS8Object object) {
		return false; // never resolved
	}




	/**
	 * 
	 * @author pierreconvert
	 *
	 * @param <T>
	 */
	private static class Binding implements BuildScope.Binding {

		private LiS8Object[] array;

		private String[] identifiers;


		public Binding(LiS8Object[] array, String[] indices) {
			super();
			this.array = array;
			this.identifiers = indices;
		}

		@Override
		public void resolve(BuildScope scope) throws LiIOException {
			int length = identifiers.length;
			for(int i=0; i<length; i++) {
				String graphId = identifiers[i];
				array[i] = scope.retrieveObject(graphId);
			}
		}
	}



	/* <IO-inflow-section> */
	@Override
	public LiFieldParser createParser(ByteInflow inflow) throws IOException {
		if(inflow.matches(SEQUENCE)) {
			return new Inflow();
		}
		else {
			throw new IOException("Only one possible encoding! ");
		}
	}


	private class Inflow extends LiFieldParser {


		@Override
		public void parseValue(LiS8Object object, ByteInflow inflow, BuildScope scope) throws IOException {
			String[] indices = deserializeIndices(inflow);
			if(indices != null) {
				LiS8Object[] array = new LiS8Object[indices.length];
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
		public S8ObjectArrayLiField getField() {
			return S8ObjectArrayLiField.this;
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
				throw new LiIOException("Illegal code for List object length");
			}
		}
	}
	/* </IO-inflow-section> */



	/* <IO-outflow-section> */
	@Override
	public LiFieldComposer createComposer(int code) throws LiIOException {
		switch(flow) {
		case DEFAULT_FLOW_TAG: case "obj[]" : return new Composer(code);
		default : throw new LiIOException("Impossible to match IO type for flow: "+flow);
		}
	}


	private class Composer extends LiFieldComposer {

		public Composer(int code) {
			super(code);
		}

		@Override
		public LiField getField() {
			return S8ObjectArrayLiField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putByteArray(SEQUENCE);
		}

		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {

			// array
			LiS8Object[] array = (LiS8Object[]) handler.get(object);
			if(array!=null) {
				int length = array.length;
				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					LiS8Object itemObject = array[i];
					String index;
					if(itemObject != null) {
						index = itemObject.S8_index;
						if(index == null) {
							index = scope.append(itemObject);
							itemObject.S8_index = index;
						}
						outflow.putStringUTF8(index);
					}
					else {
						outflow.putStringUTF8(null);
					}
				}
			}
			else {
				// advertise NULL
				outflow.putUInt7x(-1);
			}
		}




	}
	/* </IO-outflow-section> */
}
