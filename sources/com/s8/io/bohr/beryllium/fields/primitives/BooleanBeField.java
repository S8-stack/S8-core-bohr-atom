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
public class BooleanBeField extends MappedBeField {

	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(boolean.class) {

		@Override
		public BooleanBeField createField(String name, long props, Field field)
				throws BeTypeBuildException {
			return new BooleanBeField(name, props, field);
		}
	};

	
	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public BooleanBeField(String name, long props, Field field){
		super(name, props, field);
	}


	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			// read advertised encoding
			switch(inflow.getUInt8()) {
			case BerylliumEncoding.TRUE_BOOL8:
				field.setBoolean(object, true);
				break;
				
			case BerylliumEncoding.FALSE_BOOL8:
				field.setBoolean(object, false);
				break;
			
			default: throw new BeSerialException("Illegal encoding for boolean value: "+name, null);
			}
		} 
		catch (IOException cause) {
			cause.printStackTrace();
			throw new BeSerialException("failed to write (I/O) with: "+name, cause);
		} 
		catch (IllegalArgumentException | IllegalAccessException cause) {
			cause.printStackTrace();
			throw new BeSerialException("failed to write (field access) with: "+name, cause);
		}
	}


	@Override
	public void writeValue(Object object, ByteOutflow outflow) throws BeSerialException {
		try {
			outflow.putUInt8(field.getBoolean(object) ? BerylliumEncoding.TRUE_BOOL8 : BerylliumEncoding.FALSE_BOOL8);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to write (I/O): "+name, cause);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to write (field access) with: "+name, cause);
		}
	}


	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {
		try {
			field.setBoolean(clone, field.getBoolean(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			return field.getBoolean(base) != field.getBoolean(update);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write(Boolean.toString(field.getBoolean(object)));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}

}
