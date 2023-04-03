package com.s8.io.bohr.beryllium.fields.objects;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.fields.MappedBeField;
import com.s8.io.bohr.beryllium.object.BeRef;
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
public class S8RefBeField extends MappedBeField {


	public final static Prototype PROTOTYPE = new MappedBeField.Prototype(BeRef.class) {

		@Override
		public S8RefBeField createField(String name, long props, Field field) throws BeTypeBuildException {
			return new S8RefBeField(name, props, field);
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
	public S8RefBeField(String name, long props, Field field){
		super(name, props, field);
	}



	@Override
	public void readValue(Object object, ByteInflow inflow) throws BeSerialException {
		try {
			switch(inflow.getUInt8()) {
			
			case BerylliumEncoding.S8_REFERENCE:
				String address = inflow.getStringUTF8();
				String branch = inflow.getStringUTF8();
				long release = inflow.getUInt7x();
				int port = inflow.getUInt8();
				field.set(object, new BeRef<>(address, branch, release, port));
				break;
				
			case BerylliumEncoding.NULL_OBJECT:
				field.set(object, null);
				break;
			
			default: throw new BeSerialException("failed to write (I/O) with: "+name);
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
			BeRef<?> ref = (BeRef<?>) field.get(object);
			if(ref!=null) {
				outflow.putUInt8(BerylliumEncoding.S8_REFERENCE);
				outflow.putStringUTF8(ref.address);
				outflow.putStringUTF8(ref.branch);
				outflow.putUInt7x(ref.version);
				outflow.putUInt8(ref.port);
			}
			else {
				outflow.putUInt8(BerylliumEncoding.NULL_OBJECT);
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
