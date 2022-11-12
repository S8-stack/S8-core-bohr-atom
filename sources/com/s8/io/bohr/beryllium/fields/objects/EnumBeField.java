package com.s8.io.bohr.beryllium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.fields.ScreenedBeField;
import com.s8.io.bohr.beryllium.object.BeSerialException;
import com.s8.io.bohr.beryllium.syntax.BerylliumEncoding;
import com.s8.io.bohr.beryllium.types.BeTypeBuildException;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 * @author pierreconvert
 *
 */
public abstract class EnumBeField extends ScreenedBeField {


	public final static Prototype PROTOTYPE = new ScreenedBeField.Prototype() {

		@Override
		public EnumBeField captureField(String name, long props, Field field) throws BeTypeBuildException {
			Class<?> type = field.getType();
			
			if(type.isEnum()){
				int nItems = type.getEnumConstants().length;
				if(nItems<BerylliumEncoding.UINT8_ARRAY_MAX_SIZE) {
					return new UINT8_Encoded(name, props, field);
				}
				else if(nItems<BerylliumEncoding.UINT16_ARRAY_MAX_SIZE) {
					return new UINT16_Encoded(name, props, field);
				}
				else {
					throw new BeTypeBuildException("Nb of items cannot exceed UINT16_ARRAY_MAX_SIZE, for: "+type.getName());
				}
			}
			else {
				return null;
			}
		}
		
	};




	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	private static class UINT16_Encoded extends EnumBeField {

		public UINT16_Encoded(String name, long props, Field field) {
			super(name, props, field);
		}


		@Override
		public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
			try {
				if(inflow.getUInt8()!=BerylliumEncoding.ENUM_INDEX) {
					throw new BeSerialException("failed to write (I/O) with: "+name);
				}
				// read advertised encoding
				field.set(object, enumValues[inflow.getUInt16()]);
			} 
			catch (IOException cause) {
				throw new BeSerialException("failed to write (I/O) with: "+name, cause);
			} 
			catch (IllegalArgumentException | IllegalAccessException cause) {
				throw new BeSerialException("failed to write (field access) with: "+name, cause);
			}
		}


		@Override
		public void writeValue(Object object, ByteOutflow outflow) throws BeSerialException {
			try {
				Object enumObject = field.get(object);
				int code = ((Enum<?>) enumObject).ordinal();
				outflow.putUInt8(BerylliumEncoding.ENUM_INDEX);
				outflow.putUInt16(code);
			} 
			catch (IOException cause) {
				throw new BeSerialException("failed to write (I/O): "+name, cause);
			}
			catch (IllegalArgumentException | IllegalAccessException cause) {
				throw new BeSerialException("failed to write (field access) with: "+name, cause);
			}
		}
	}


	private static class UINT8_Encoded extends EnumBeField {

		public UINT8_Encoded(String name, long props, Field field) {
			super(name, props, field);
		}


		@Override
		public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
			try {
				// read advertised encoding
				field.set(object, enumValues[inflow.getUInt8()]);
			} 
			catch (IOException cause) {
				throw new BeSerialException("failed to write (I/O) with: "+name, cause);
			} 
			catch (IllegalArgumentException | IllegalAccessException cause) {
				throw new BeSerialException("failed to write (field access) with: "+name, cause);
			}
		}


		@Override
		public void writeValue(Object object, ByteOutflow outflow) throws BeSerialException {
			try {
				Object enumObject = field.get(object);
				int code = ((Enum<?>) enumObject).ordinal();
				outflow.putUInt8(code);
			} 
			catch (IOException cause) {
				throw new BeSerialException("failed to write (I/O): "+name, cause);
			}
			catch (IllegalArgumentException | IllegalAccessException cause) {
				throw new BeSerialException("failed to write (field access) with: "+name, cause);
			}
		}
	}



	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}
	
	public final Object[] enumValues;

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public EnumBeField(String name, long props, Field field){
		super(name, props, field);
		
		Class<?> type = field.getType();
		enumValues = type.getEnumConstants();
	}




	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {
		try {
			field.setInt(clone, field.getInt(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			Object obj0 = field.get(base);
			Object obj1 = field.get(update);
			if(obj0==null && obj1==null) {
				return false;
			}
			else if ((obj0!=null && obj1==null) || (obj0==null && obj1!=null)) {
				return true;
			}
			else {
				return !obj0.equals(obj1);	
			}
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			cause.printStackTrace();
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write(Integer.toString(field.getInt(object)));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}

}
