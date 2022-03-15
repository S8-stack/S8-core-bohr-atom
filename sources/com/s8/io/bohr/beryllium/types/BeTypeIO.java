package com.s8.io.bohr.beryllium.types;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.s8.io.bohr.beryllium.fields.BeField;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * @author pierreconvert
 *
 * @param <R>
 */
public class BeTypeIO {


	public final static byte[] FORMAT_OPENING_TAG = "<record-format:>".getBytes(StandardCharsets.US_ASCII);
	public final static byte[] FORMAT_CLOSING_TAG = "</record-format>".getBytes(StandardCharsets.US_ASCII);



	private final BeType type;
	
	private final int nFields;
	
	private final BeField[] fields;



	/**
	 * 
	 * @param type
	 */
	public BeTypeIO(BeType type, BeField[] fields) {
		super();
		this.type = type;
		this.nFields = fields.length;
		this.fields = fields;
	}

	


	public static BeTypeIO parse(BeType type, ByteInflow inflow) throws IOException {
		if(!inflow.matches(FORMAT_OPENING_TAG)) {
			throw new IOException("Failed to match FORMAT_OPENING_TAG");
		}
		int nFields = inflow.getUInt8();
		BeField[] fields = new BeField[nFields];
		for(int i=0; i<nFields; i++) {
			int iColumn = inflow.getUInt8();
			if(iColumn!=i) {
				throw new IOException("Discrepancy in column indices");
			}
			String name = inflow.getStringUTF8();
			BeField field = type.getField(name);
			if(field==null) {
				throw new IOException("Cannot retrieve field for name = "+name);
			}
			fields[i] = field;
		}
		if(!inflow.matches(FORMAT_CLOSING_TAG)) {
			throw new IOException("Failed to match FORMAT_CLOSING_TAG");
		}
		
		return new BeTypeIO(type, fields);
	}


	public void compose(ByteOutflow outflow) throws IOException {
		// opening sequence
		outflow.putByteArray(FORMAT_OPENING_TAG);


		int nFields = fields.length;
		outflow.putUInt8(nFields);
		for(int i=0; i<nFields; i++) {
			
			// advertise column index
			outflow.putUInt8(i);
			
			// adevrtise field
			outflow.putStringUTF8(fields[i].name);
		}

		// closing sequence
		outflow.putByteArray(FORMAT_CLOSING_TAG);
	}


	/**
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException 
	 */
	public Object deserialize(ByteInflow inflow) throws IOException {
		Object object = type.createInstance();
		for(int i=0; i<nFields; i++) {
			fields[i].readValue(object, inflow);
		}
		return object;
	}
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException 
	 */
	public void serialize(Object object, ByteOutflow outflow) throws IOException {
		for(int i=0; i<nFields; i++) {
			fields[i].writeValue(object, outflow);
		}
	}




	/**
	 * 
	 * @return
	 */
	public BeType getType() {
		return type;
	}

}
