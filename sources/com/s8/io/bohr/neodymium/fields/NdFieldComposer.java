package com.s8.io.bohr.neodymium.fields;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bytes.alpha.ByteOutflow;

public abstract class NdFieldComposer {


	private boolean isFieldUndeclared;


	public abstract NdField getField();


	public final int code;



	public NdFieldComposer(int code) {
		super();
		this.code = code;
		this.isFieldUndeclared = true;
	}

	/*
	 * public void write(S8Object object, ByteOutflow outflow) throws
	 * S8ObjectIOException {
	 * 
	 * }
	 */
	public abstract void composeValue(S8Object object, ByteOutflow outflow) throws IOException;



	/**
	 * 
	 * @param delta
	 * @param outflow
	 * @throws IOException
	 */
	public void publish(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
		publishFieldHeader(outflow);
		publishValue(delta, outflow);
	}






	/**
	 * 
	 * @param object
	 * @param code
	 * @param outflow
	 * @throws IOException
	 */
	public void publishFieldHeader(ByteOutflow outflow) throws IOException {
		if(isFieldUndeclared) {

			// advertise transmission
			outflow.putUInt8(BOHR_Keywords.DECLARE_FIELD);

			// send field name
			outflow.putStringUTF8(getField().name);

			// send field flow properties
			publishFlowEncoding(outflow);

			// send code
			outflow.putUInt8(code);

			isFieldUndeclared = false;
		}


		// advertise transmission
		outflow.putUInt8(BOHR_Keywords.SET_VALUE);

		// send code
		outflow.putUInt8(code);

	}


	/**
	 * 
	 * @param outflow
	 * @throws IOException 
	 */
	public abstract void publishFlowEncoding(ByteOutflow outflow) throws IOException;

	public abstract void publishValue(NdFieldDelta delta, ByteOutflow outflow) throws IOException;

	/**
	 * 
	 * @param object
	 * @param outflow
	 * @throws IOException
	 */
	public void compose(S8Object object, ByteOutflow outflow) throws IOException {
		publishFieldHeader(outflow);
		composeValue(object, outflow);
	}

	public void serializeDelta(NdFieldDelta delta, ByteOutflow outflow) throws IOException {
		publishFieldHeader(outflow);
		publishValue(delta, outflow);
	}
}
