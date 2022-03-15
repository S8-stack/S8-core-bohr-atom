package com.s8.io.bohr.beryllium.tables;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.io.bohr.beryllium.codebase.BeCodebase;
import com.s8.io.bohr.beryllium.types.BeType;
import com.s8.io.bohr.beryllium.types.BeTypeIO;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * @author pierreconvert
 *
 * @param <T>
 */
public class BerylTable {

	
	/**
	 * 
	 */
	private final BeCodebase codebase;
	
	
	/**
	 * 
	 */
	private final String address;
	
	
	/**
	 * 
	 */
	private BerylTablePrototype prototype;

	
	/**
	 * 
	 * @param root
	 * @param codebase
	 */
	public BerylTable(String address, BeCodebase codebase) {
		super();
		this.address = address;
		this.codebase = codebase;
	}

	
	
	public BerylTablePrototype getPrototype() {
		return prototype;
	}
	

	/**
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	
	/**
	 * 
	 */
	public final static byte[] OPENING_TAG = "<BeTable:>".getBytes(StandardCharsets.US_ASCII);

	
	/**
	 * 
	 */
	public final static byte[] CLOSING_TAG = "</BeTable>".getBytes(StandardCharsets.US_ASCII);

	
	/**
	 * 
	 * @param config
	 * @param rawType
	 * @throws IOException
	 */
	public void init(Class<?> rawType) throws IOException {
		BeType type = codebase.getType(rawType);
		BeTypeIO typeIO = type.generateIO();
		prototype = new BerylTablePrototype(typeIO);
	}
	

	
	public void serialize(ByteOutflow outflow) throws IOException {

		outflow.putByteArray(OPENING_TAG);

		BeTypeIO typeIO = prototype.getTypeIO();
		
		outflow.putStringUTF8(typeIO.getType().getSerialName());
		
		typeIO.compose(outflow);

		//serializeStore(outflow);

		outflow.putByteArray(CLOSING_TAG);
	}




	
	public void deserialize(ByteInflow inflow) throws IOException {

		if(!inflow.matches(OPENING_TAG)) {
			throw new IOException("Failed to match opening tag");
		}
		
		String serialName = inflow.getStringUTF8();
		
		BeType type = codebase.getTypeBySerialName(serialName);
		
		BeTypeIO typeIO = BeTypeIO.parse(type, inflow);
		
		prototype = new BerylTablePrototype(typeIO);
		
		//deserializeStore(inflow);

		if(!inflow.matches(CLOSING_TAG)) {
			throw new IOException("Failed to match opening tag");
		}
	}
	
	
}
