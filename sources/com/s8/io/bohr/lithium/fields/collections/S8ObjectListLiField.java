package com.s8.io.bohr.lithium.fields.collections;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
 * <p> Internal object ONLY</p>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class S8ObjectListLiField<T extends LiS8Object> extends CollectionLiField {




	public final static LiFieldPrototype PROTOTYPE = new LiFieldPrototype(){


		@Override
		public LiFieldProperties captureField(Field field) throws LiBuildException {
			Class<?> baseType = field.getType();
			if(List.class.isAssignableFrom(baseType)) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					Type parameterType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(LiS8Object.class.isAssignableFrom(typeArgument)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.FIELD, typeArgument);
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
			if(List.class.isAssignableFrom(baseType)) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					Type parameterType = method.getGenericParameterTypes()[0];
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(LiS8Object.class.isAssignableFrom(typeArgument)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.METHODS, typeArgument);
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
			if(List.class.isAssignableFrom(baseType)) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					Type parameterType = method.getGenericReturnType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(LiS8Object.class.isAssignableFrom(typeArgument)) {
						LiFieldProperties properties = new LiFieldProperties1T(this, LiFieldProperties.METHODS, typeArgument);
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
			return new S8ObjectListLiField<>(ordinal, properties, handler);
		}

	}



	public final static int NULL_OBJECT_INDEX = -8;


	private Class<?> baseType;


	public S8ObjectListLiField(int ordinal, LiFieldProperties properties, LiHandler handler) throws LiBuildException {
		super(ordinal, properties, handler);
		baseType = properties.getEmbeddedType();
	}


	/**
	 * 
	 * @author pierreconvert
	 *
	 * @param <T>
	 */
	private static class ListBinding<T> implements BuildScope.Binding {

		private List<T> list;

		private String[] identifiers;


		public ListBinding(List<T> list, String[] indices) {
			super();
			this.list = list;
			this.identifiers = indices;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void resolve(BuildScope scope) throws LiIOException {
			int length = identifiers.length;
			for(int i=0; i<length; i++) {
				String graphId = identifiers[i];

				if(graphId != null) {
					// might be null
					LiS8Object struct = scope.retrieveObject(graphId);
					if(struct!=null) {
						list.add((T) struct);		
					}
					else {
						throw new LiIOException("Failed to retrive object for index="+graphId);
					}	
				}
				else {
					list.add(null);		
				}

			}
		}
	}




	@Override
	public void sweep(LiS8Object object, GraphCrawler crawler) {
		try {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) handler.get(object);


			if(list!=null) {
				for(LiS8Object item : list) {
					if(item!=null) { crawler.accept(item); }
				}
			}
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (ClassCastException e) {
			e.printStackTrace();
		} 
		catch (LiIOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void computeFootprint(LiS8Object object, MemoryFootprint weight) throws LiIOException {

		@SuppressWarnings("unchecked")
		List<LiS8Object> list = (List<LiS8Object>) handler.get(object);
		if(list!=null) {
			weight.reportInstances(1+list.size()); // the array object itself	
			weight.reportReferences(list.size());
		}
	}


	@Override
	public void deepClone(LiS8Object origin, LiS8Object clone, BuildScope scope) throws LiIOException {

		@SuppressWarnings("unchecked")
		List<T> value = (List<T>) handler.get(origin);
		if(value!=null) {
			int n = value.size();

			List<T> clonedList = new ArrayList<T>(n);
			String[] indices = new String[n];
			for(int i=0; i<n; i++) {
				indices[i] = value.get(i).S8_index;
			}

			handler.set(clone, clonedList);
			scope.appendBinding(new ListBinding<>(clonedList, indices));
		}
		else {
			handler.set(clone, null);
		}
	}


	@Override
	public void collectReferencedBlocks(LiS8Object object, Queue<String> references) {
		/* not referencing external values */
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (List<? extends S8Object>)");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDiff(LiS8Object base, LiS8Object update) throws LiIOException {
		List<T> baseValue = (List<T>) handler.get(base);
		List<T> updateValue = (List<T>) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}






	private boolean areEqual(List<T> array0, List<T> array1) {

		// check nulls
		if(array0 == null) { return array1==null; }
		if(array1 == null) { return array0==null; }

		// check lengths
		int n0 = array0.size();
		int n1 = array1.size();
		if(n0!=n1) { return false; }

		// check values
		LiS8Object obj0, obj1;
		for(int i=0; i<n0; i++) {
			obj0 = array0.get(i);
			obj1 = array1.get(i);
			if((obj0==null && obj1!=null) || (obj1==null && obj0!=null) // one is null while the other is non-null
					|| (obj0!=null  && obj1!=null // both non null with different indices
					&& !obj0.S8_index.equals(obj1.S8_index))) { 
				return false; 
			}
		}
		return true;
	}


	@Override
	protected void printValue(LiS8Object object, Writer writer) throws IOException {
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) handler.get(object);
		if(list!=null) {
			boolean isInitialized = false;
			writer.write('[');
			int n = list.size();
			for(int i=0; i<n; i++) {
				if(isInitialized) {
					writer.write(" ,");	
				}
				else {
					isInitialized = true;
				}

				LiS8Object value = list.get(i);
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
			writer.write(']');
		}
		else {
			writer.write("null");
		}
	}


	@Override
	public String printType() {
		return "List<"+baseType.getCanonicalName()+">";
	}




	@Override
	public void forEach(Object iterable, ItemConsumer consumer) throws IOException {
		if(iterable!=null) {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) iterable;
			for(T item : list) {
				consumer.consume(item);
			}	
		}
	}

	@Override
	public boolean isValueResolved(LiS8Object object) {
		return false; // never resolved
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
				List<T> list = new ArrayList<T>(indices.length);
				/* append bindings */
				scope.appendBinding(new ListBinding<>(list, indices));
				// set list
				handler.set(object, list);	
			}
			else {
				// set list
				handler.set(object, null);	
			}
		}


		@Override
		public S8ObjectListLiField<T> getField() {
			return S8ObjectListLiField.this;
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
			// TODO Auto-generated constructor stub
		}

		@Override
		public LiField getField() {
			return S8ObjectListLiField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putByteArray(SEQUENCE);
		}

		@Override
		public void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) handler.get(object);

			if(list!=null) {

				int length = list.size();

				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					T itemObject = list.get(i);
					
					if(itemObject!=null) {
						String index = itemObject.S8_index;
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
