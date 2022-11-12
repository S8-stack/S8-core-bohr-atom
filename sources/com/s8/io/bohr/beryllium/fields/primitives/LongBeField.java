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
public class LongBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(long.class) {

		@Override
		public LongBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new LongBeField(name, props, field);
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
	public LongBeField(String name, long props, Field field){
		super(name, props, field);
	}


	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			// read advertised encoding

			switch(inflow.getUInt8()) {
			case BerylliumEncoding.INT64:
				field.setLong(object, inflow.getInt64());
				break;

			case BerylliumEncoding.ZERO_INT:
				field.setLong(object, 0L);
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
			long value = field.getLong(object);
			if(value!=0L) {
				outflow.putUInt8(BerylliumEncoding.INT64);	
				outflow.putInt64(value);	
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


	@Override
	public void computeFootprint(Object object, MemoryFootprint weight) {
		weight.reportBytes(8);
	}


	@Override
	public void deepClone(Object origin, Object clone) throws BeSerialException {
		try {
			field.setLong(clone, field.getLong(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			return field.getLong(base) != field.getLong(update);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write(Long.toString(field.getLong(object)));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}

}
