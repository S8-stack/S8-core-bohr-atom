package com.s8.io.bohr.beryllium.fields.primitives;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.fields.MappedBeField;
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
public abstract class StringBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(String.class) {

		@Override
		public StringBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new L32UTF8_Encoded(name, props, field);
		}
	};

	private static class L32UTF8_Encoded extends StringBeField {


		public L32UTF8_Encoded(String name, long props, Field field) {
			super(name, props,  field);
		}


		@Override
		public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
			try {
				switch(inflow.getUInt8()) {
				case BerylliumEncoding.L32_STRING_UTF8:
					// read advertised encoding
					field.set(object, inflow.getStringUTF8());
					break;

				case BerylliumEncoding.NULL_STRING:
					field.set(object, null);
					break;

				default : throw new BeSerialException("failed to write (I/O) with: "+name);
				}

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
				String val = (String) field.get(object);
				if(val!=null) {
					outflow.putUInt8(BerylliumEncoding.L32_STRING_UTF8);
					outflow.putStringUTF8(val);	
				}
				else {
					outflow.putUInt8(BerylliumEncoding.NULL_STRING);
				}
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

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public StringBeField(String name, long props, Field field){
		super(name, props, field);
	}



	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {
		try {
			field.set(clone, field.get(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			String baseValue = (String) field.get(base);
			String updateValue = (String) field.get(update);
			if(baseValue==null && updateValue==null) {
				return false;
			}
			else if((baseValue == null && updateValue!=null) || (baseValue != null && updateValue==null)) {
				return true;
			}
			else {
				return !baseValue.equals(updateValue);
			}
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write((String) field.get(object));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}


	/**
	 * Use this method to get hash
	 * @param object
	 * @return
	 * @throws BeSerialException
	 */
	public String getValue(Object object) throws BeSerialException {
		try {
			return (String) field.get(object);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}
}
