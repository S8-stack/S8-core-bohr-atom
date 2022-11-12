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
public class DoubleBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(double.class) {

		@Override
		public DoubleBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new DoubleBeField(name, props, field);
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
	public DoubleBeField(String name, long props, Field field){
		super(name, props, field);
	}


	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			switch(inflow.getUInt8()) {
			case BerylliumEncoding.FLOAT64: 
				// read advertised encoding
				field.setDouble(object, inflow.getFloat64());
				break;
				
			case BerylliumEncoding.ZERO_FLOAT:
				field.setDouble(object, 0);
				break;
				
			default : throw new BeSerialException("failed to read (I/O) with: "+name);
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
			double val = field.getDouble(object);
			if(val!=0) {
				outflow.putUInt8(BerylliumEncoding.FLOAT64);
				outflow.putFloat64(val);
			}
			else {
				outflow.putUInt8(BerylliumEncoding.ZERO_FLOAT);
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
			field.setDouble(clone, field.getDouble(origin));	
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to deep clone (field access) for field: "+name, cause);
		}
	}


	@Override
	public boolean hasDiff(Object base, Object update) throws BeSerialException {
		try {
			return field.getDouble(base) != field.getDouble(update);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		}
	}



	@Override
	protected void printValue(Object object, Writer writer) throws BeSerialException {
		try {
			writer.write(Double.toString(field.getDouble(object)));
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
			throw new BeSerialException("failed to compute diffs (field access) for field: "+name, cause);
		} 
		catch (IOException cause) {
			throw new BeSerialException("failed to print values (field access) for field: "+name, cause);
		}
	}

}
