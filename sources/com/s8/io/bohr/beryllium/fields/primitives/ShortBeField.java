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
public class ShortBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(short.class) {

		@Override
		public ShortBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new ShortBeField(name, props, field);
		}
	};

	@Override
	public Prototype getPrototype() {
		return PROTOTYPE;
	}




	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			switch(inflow.getUInt8()) {

			case BerylliumEncoding.INT16:
				field.setShort(object, inflow.getInt16());
				break;

			case BerylliumEncoding.ZERO_INT:
				field.setShort(object, (short) 0);
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
			short value = field.getShort(object);
			if(value!=0) {
				outflow.putUInt8(BerylliumEncoding.INT16);	
				outflow.putInt16(value);
			}
			else {
				outflow.putUInt8(BerylliumEncoding.ZERO_INT);
			}
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to write (I/O): "+name, cause);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to write (field access) with: "+name, cause);
		}
	}



	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public ShortBeField(String name, long props, Field field){
		super(name, props, field);
	}



	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) {
		weight.reportBytes(2);
	}


	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {
		try {
			field.setShort(clone, field.getShort(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			return field.getShort(base) != field.getShort(update);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write(Short.toString(field.getShort(object)));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}

}
