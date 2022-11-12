package com.s8.io.bohr.neodymium.fields.collections;

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
 * <p> Internal object ONLY</p>
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class S8ObjectListNdField<T extends NdObject> extends CollectionNdField {




	public final static NdFieldPrototype PROTOTYPE = new NdFieldPrototype(){


		@Override
		public NdFieldProperties captureField(Field field) throws NdBuildException {
			Class<?> baseType = field.getType();
			if(List.class.isAssignableFrom(baseType)) {
				S8Field annotation = field.getAnnotation(S8Field.class);
				if(annotation != null) {
					Type parameterType = field.getGenericType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(NdObject.class.isAssignableFrom(typeArgument)) {
						NdFieldProperties properties = new NdFieldProperties1T(this, NdFieldProperties.FIELD, typeArgument);
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
			if(List.class.isAssignableFrom(baseType)) {
				S8Setter annotation = method.getAnnotation(S8Setter.class);
				if(annotation != null) {
					Type parameterType = method.getGenericParameterTypes()[0];
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(NdObject.class.isAssignableFrom(typeArgument)) {
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
			else { return null; }
		}

		@Override
		public NdFieldProperties captureGetter(Method method) throws NdBuildException {
			Class<?> baseType = method.getReturnType();
			if(List.class.isAssignableFrom(baseType)) {
				S8Getter annotation = method.getAnnotation(S8Getter.class);
				if(annotation != null) {
					Type parameterType = method.getGenericReturnType();
					ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
					Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if(NdObject.class.isAssignableFrom(typeArgument)) {
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
			return new S8ObjectListNdField<>(ordinal, properties, handler);
		}

	}



	public final static int NULL_OBJECT_INDEX = -8;


	private Class<?> baseType;


	public S8ObjectListNdField(int ordinal, NdFieldProperties properties, NdHandler handler) throws NdBuildException {
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
		public void resolve(BuildScope scope) throws NdIOException {
			int length = identifiers.length;
			for(int i=0; i<length; i++) {
				String graphId = identifiers[i];

				if(graphId != null) {
					// might be null
					NdObject struct = scope.retrieveObject(graphId);
					if(struct!=null) {
						list.add((T) struct);		
					}
					else {
						throw new NdIOException("Failed to retrive object for index="+graphId);
					}	
				}
				else {
					list.add(null);		
				}

			}
		}
	}




	@Override
	public void sweep(NdObject object, GraphCrawler crawler) {
		try {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) handler.get(object);


			if(list!=null) {
				for(NdObject item : list) {
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
		catch (NdIOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void computeFootprint(NdObject object, MemoryFootprint weight) throws NdIOException {

		@SuppressWarnings("unchecked")
		List<NdObject> list = (List<NdObject>) handler.get(object);
		if(list!=null) {
			weight.reportInstances(1+list.size()); // the array object itself	
			weight.reportReferences(list.size());
		}
	}


	@Override
	public void deepClone(NdObject origin, NdObject clone, BuildScope scope) throws NdIOException {

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
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		/* not referencing external values */
	}


	@Override
	public void DEBUG_print(String indent) {
		System.out.println(indent+name+": (List<? extends S8Object>)");
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDiff(NdObject base, NdObject update) throws NdIOException {
		List<T> baseValue = (List<T>) handler.get(base);
		List<T> updateValue = (List<T>) handler.get(update);
		return !areEqual(baseValue, updateValue);
	}



	@Override
	public NdFieldDelta produceDiff(NdObject object) throws NdIOException {
		@SuppressWarnings("unchecked")
		List<T> array = (List<T>) handler.get(object);
		String[] indices = null;
		if(array!=null) {
			int n = array.size();
			indices = new String[n];
			NdObject item;
			for(int i=0; i<n; i++) {
				item = array.get(i);
				indices[i] = (item != null ? item.S8_index : null);	
			}
		}
		return new S8ObjectListNdFieldDelta<>(this, indices);
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
		NdObject obj0, obj1;
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
	protected void printValue(NdObject object, Writer writer) throws IOException {
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

				NdObject value = list.get(i);
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
	public boolean isValueResolved(NdObject object) {
		return false; // never resolved
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
		public void parseValue(NdObject object, ByteInflow inflow, BuildScope scope) throws IOException {
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
		public S8ObjectListNdField<T> getField() {
			return S8ObjectListNdField.this;
		}

		@Override
		public NdFieldDelta deserializeDelta(ByteInflow inflow) throws IOException {
			return new S8ObjectListNdFieldDelta<>(S8ObjectListNdField.this,  deserializeIndices(inflow));
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
			// TODO Auto-generated constructor stub
		}

		@Override
		public NdField getField() {
			return S8ObjectListNdField.this;
		}

		@Override
		public void publishFlowEncoding(ByteOutflow outflow) throws IOException {
			outflow.putByteArray(SEQUENCE);
		}

		@Override
		public void composeValue(NdObject object, ByteOutflow outflow) throws IOException {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) handler.get(object);

			if(list!=null) {

				int length = list.size();

				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					T itemObject = list.get(i);
					outflow.putStringUTF8(itemObject!=null ? itemObject.S8_index : null);
				}
			}
			else {
				// advertise NULL
				outflow.putUInt7x(-1);
			}
		}

		
		@Override
		public void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
			serialize(outflow, ((S8ObjectListNdFieldDelta<?>) delta).indices);
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
