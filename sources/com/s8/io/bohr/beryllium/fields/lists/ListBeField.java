package com.s8.io.bohr.beryllium.fields.lists;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.beryllium.fields.ScreenedBeField;
import com.s8.io.bohr.beryllium.object.BeSerialException;
import com.s8.io.bohr.beryllium.syntax.BerylliumEncoding;
import com.s8.io.bohr.beryllium.types.BeTypeBuildException;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;



/**
 * <p> Internal object ONLY</p>
 * @author pc
 *
 */
public abstract class ListBeField<T> extends ScreenedBeField {


	public static abstract class Prototype<T> extends ScreenedBeField.Prototype {


		private Class<?> itemType;

		public Prototype(Class<?> itemType) {
			super();
			this.itemType = itemType;
		}

		public abstract ListBeField<T> createField(String name, long props, Field field); 

		@Override
		public ListBeField<T> captureField(String name, long props, Field field) throws BeTypeBuildException {
			Class<?> baseType = field.getType();
			if(List.class.isAssignableFrom(baseType)) {

				Type parameterType = field.getGenericType();
				ParameterizedType parameterizedType = (ParameterizedType) parameterType; 
				Class<?> typeArgument = (Class<?>) parameterizedType.getActualTypeArguments()[0];

				if(typeArgument.equals(itemType)) {
					return createField(name, props, field);
				}
				else {
					return null;
				}
			}
			else {
				return null;
			}
		}
	}


	public final static int NULL_OBJECT_INDEX = -8;


	public ListBeField(String name, long props, Field field) {
		super(name, props, field);
	}






	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			field.set(object, deserialize(inflow));
		} 
		catch (IOException | IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to read LIST field "+name, cause);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void writeValue(Object object, ByteOutflow outflow) throws BeSerialException {
		try {
			serialize((List<T>) field.get(object), outflow);
		} 
		catch (IOException | IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to write ObjectList field: "+name, cause);
		}
	}




	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) throws BeSerialException {

		try {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) field.get(object);
			if(list!=null) {
				weight.reportInstances(1+list.size()); // the array object itself	
				weight.reportReferences(list.size());
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}	
	}



	public abstract T deepClone(T base);

	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {

		try {
			@SuppressWarnings("unchecked")
			List<T> value = (List<T>) field.get(origin);
			if(value!=null) {
				int n = value.size();

				List<T> clonedList = new ArrayList<T>(n);
				for(int i=0; i<n; i++) {
					clonedList.add(deepClone(value.get(i)));
				}

				field.set(clone, clonedList);
			}
			else {
				field.set(clone, null);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BeSerialException(e.getMessage());
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			List<T> baseValue = (List<T>) field.get(base);
			List<T> updateValue = (List<T>) field.get(update);
			return !areEqual(baseValue, updateValue);
		} 
		catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new BeSerialException(e.getMessage());
		}

	}






	public abstract boolean areEqual(T obj0, T obj1);
	/**
	 * 
	 * @param array0
	 * @param array1
	 * @return
	 */
	private boolean areEqual(List<T> array0, List<T> array1) {

		// check nulls
		if(array0 == null) { return array1==null; }
		if(array1 == null) { return array0==null; }

		// check lengths
		int n0 = array0.size();
		int n1 = array1.size();
		if(n0!=n1) { return false; }

		// check values
		T obj0, obj1;
		for(int i=0; i<n0; i++) {
			obj0 = array0.get(i);
			obj1 = array1.get(i);
			if((obj0==null && obj1!=null) || (obj1==null && obj0!=null) // one is null while the other is non-null
					|| (obj0!=null && obj1!=null 
					&& !areEqual(obj0, obj1))) { // both non null with different indices
				return false; 
			}
		}
		return true;
	}


	public abstract String print(T value);

	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {

		try {
			@SuppressWarnings("unchecked")
			List<T> list = (List<T>) field.get(object);

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

					T value = list.get(i);
					if(value!=null) {
						writer.write("(");
						writer.write(value.getClass().getCanonicalName());
						writer.write("): ");
						writer.write(print(value));	
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
		catch (IllegalArgumentException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			throw new BeSerialException(e.getMessage());
		}

	}




	public abstract void serializeItem(T item, ByteOutflow outflow) throws IOException;



	public void serialize(List<T> list, ByteOutflow outflow) throws IOException {

		// advertise

		if(list!=null) {

			int length = list.size();

			// write list size

			if(length<BerylliumEncoding.UINT8_ARRAY_MAX_SIZE) {
				outflow.putUInt8(BerylliumEncoding.ARRAY_UINT8);
				outflow.putUInt8(length);	
			}
			else if(length<BerylliumEncoding.UINT16_ARRAY_MAX_SIZE) {
				outflow.putUInt8(BerylliumEncoding.ARRAY_UINT16);
				outflow.putUInt16(length);	
			}
			else {
				outflow.putUInt8(BerylliumEncoding.ARRAY_UINT31);
				outflow.putUInt31(length);
			}

			for(int i=0; i<length; i++) {
				serializeItem(list.get(i), outflow);
			}
		}
		else {
			// advertise 
			outflow.putUInt8(BerylliumEncoding.NULL_ARRAY);

			outflow.putUInt31(0);
		}
	}



	public abstract T deserializeItem(ByteInflow inflow) throws IOException;


	/**
	 * 
	 * @param inflow
	 * @param bindings
	 * @return
	 * @throws IOException
	 */
	public List<T> deserialize(ByteInflow inflow) throws IOException {

		/**
		 * Structure is mapped to array
		 */

		/* <length> */
		List<T> list = null;

		int structEncoding = inflow.getUInt8();
		int length = 0;

		switch(structEncoding) {

		case BerylliumEncoding.NULL_ARRAY:
			length = 0;
			list = null;
			break;

		case BerylliumEncoding.EMPTY_ARRAY:
			length = 0;
			list = new ArrayList<T>(0);
			break;

		case BerylliumEncoding.ARRAY_UINT8:
			length = inflow.getUInt8();
			list = new ArrayList<T>(length);
			break;

		case BerylliumEncoding.ARRAY_UINT16:
			length = inflow.getUInt16();
			list = new ArrayList<T>(length);
			break;

		case BerylliumEncoding.ARRAY_UINT31:
			length = inflow.getUInt31();
			list = new ArrayList<T>(length);

		default : throw new BeSerialException("failed to read S8LIst field "+name);
		}

		/* </length> */

		// length

		/* <data> */


		for(int index=0; index<length; index++) {
			list.add(deserializeItem(inflow));	
		}

		return list;
	}
}
