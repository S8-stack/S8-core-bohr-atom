package com.s8.io.bohr.beryllium.fields.lists;

import java.io.IOException;
import java.lang.reflect.Field;

import com.s8.io.bohr.beryllium.syntax.BerylliumEncoding;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;



/**
 * <p> Internal object ONLY</p>
 * @author pc
 *
 */
public class StringListBeField extends ListBeField<String> {

	public final static ListBeField.Prototype<String> PROTOTYPE = new Prototype<String>(String.class){

		@Override
		public StringListBeField createField(String name, long props, Field field) {
			return new StringListBeField(name, props, field);
		}		
	};


	public StringListBeField(String name, long props, Field field) {
		super(name, props, field);
	}
	
	@Override
	public void serializeItem(String item, ByteOutflow outflow) throws IOException {
		if(item!=null) {
			outflow.putUInt8(BerylliumEncoding.L32_STRING_UTF8);
			outflow.putStringUTF8(item);	
		}
		else {
			outflow.putUInt8(BerylliumEncoding.NULL_STRING);
		}
	}

	@Override
	public String deserializeItem(ByteInflow inflow) throws IOException {
		switch(inflow.getUInt8()) {
		case BerylliumEncoding.L32_STRING_UTF8: return inflow.getStringUTF8();
		case BerylliumEncoding.NULL_STRING: return null;
		default : throw new IOException("failed to write (I/O) with: "+name);
		}
	}


	@Override
	public Prototype<String> getPrototype() {
		return PROTOTYPE;
	}

	@Override
	public String deepClone(String base) {
		return base;
	}

	@Override
	public boolean areEqual(String obj0, String obj1) {
		return obj0.equals(obj1);
	}

	@Override
	public String print(String value) {
		return value;
	}

}
