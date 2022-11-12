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
public class IntegerBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(int.class) {

		@Override
		public IntegerBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new IntegerBeField(name, props, field);
		}
	};




	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			
			int value = 0;

			switch(inflow.getUInt8()) {
			
			
			case BerylliumEncoding.UINT8: value = inflow.getUInt8(); break;
		
			case BerylliumEncoding.INT16: value = inflow.getInt16(); break;
				
			case BerylliumEncoding.UINT16: value = inflow.getUInt16(); break;
			
			case BerylliumEncoding.INT32: value = inflow.getInt32(); break;
				
			case BerylliumEncoding.UINT31: value = inflow.getUInt31(); break;

			case BerylliumEncoding.ZERO_INT: value = 0; break;

			default: throw new BeSerialException("failed to write (I/O) with: "+name);

			}
			
			field.setInt(object, value);
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
			int value = field.getInt(object);
			
			if(value==0) {
				outflow.putUInt8(BerylliumEncoding.ZERO_INT);
			}
			else if(value>0) {
				if(value<0xff) {
					outflow.putUInt8(BerylliumEncoding.UINT8);
					outflow.putUInt8(value);
				}
				else if(value<0xffff) {
					outflow.putUInt8(BerylliumEncoding.UINT16);
					outflow.putUInt16(value);
				}
				else if(value<0xffffffff) {
					outflow.putUInt8(BerylliumEncoding.UINT31);
					outflow.putUInt32(value);
				}
			}
			else {
				if(-value<0x7fff) {
					outflow.putUInt8(BerylliumEncoding.INT16);
					outflow.putInt16((short) value);
				}
				else {
					outflow.putUInt8(BerylliumEncoding.INT32);
					outflow.putInt32(value);
				}
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
	public Prototype getPrototype() {
		return PROTOTYPE;
	}

	/**
	 * 
	 * @param name
	 * @param handler
	 */
	public IntegerBeField(String name, long props, Field field){
		super(name, props, field);
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
			return field.getInt(base) != field.getInt(update);
		}
		catch (IllegalArgumentException | IllegalAccessException cause) {
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
