package com.s8.io.bohr.lithium.fields;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.PublishScope;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class LiFieldComposer {


	private boolean isFieldUndeclared;


	public abstract LiField getField();


	public final int code;



	public LiFieldComposer(int code) {
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
	public abstract void composeValue(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException;





	/**
	 * 
	 * @param object
	 * @param outflowCode
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


	/**
	 * 
	 * @param object
	 * @param outflow
	 * @throws IOException
	 */
	public void compose(LiS8Object object, ByteOutflow outflow, PublishScope scope) throws IOException {
		publishFieldHeader(outflow);
		composeValue(object, outflow, scope);
	}

}
