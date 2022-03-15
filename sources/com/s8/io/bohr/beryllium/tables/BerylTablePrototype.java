package com.s8.io.bohr.beryllium.tables;

import java.io.IOException;

import com.s8.io.bohr.beryllium.types.BeTypeIO;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * @author pierreconvert
 *
 * @param <T>
 */
public class BerylTablePrototype {
	
	
	private final BeTypeIO typeIO;
	
	public BerylTablePrototype(BeTypeIO typeIO){
		super();
		this.typeIO = typeIO;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public BeTypeIO getTypeIO() {
		return typeIO;
	}

	
	
	public Class<?> getBaseType() {
		return typeIO.getType().getBaseType();
	}

	
	
	public void serialize(Object value, ByteOutflow outflow) throws IOException {
		typeIO.serialize(value, outflow);
	}

	
	public Object deserialize(ByteInflow inflow) throws IOException {
		return typeIO.deserialize(inflow);
	}

}
