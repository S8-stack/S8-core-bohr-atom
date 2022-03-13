package com.s8.io.bohr.neon.web;

import java.io.IOException;

import com.s8.io.bytes.alpha.ByteInflow;



/**
 * 
 * @author pierreconvert
 *
 */
public class NeRequest {


	private int forkCode;

	private long viewIndex = -1;

	private int methodCode = -1;

	private Object[] params;

	public NeRequest(ByteInflow inflow) throws IOException {
		super();

		// fields
		//parse(inflow);
	}


	/*
	private void parse(NeInflow inflow) throws IOException {

		// method code
		forkCode = inflow.getUInt8();

		// parameters
		params = new Object[BOHR_RequestProtocol.PARAMS_RANGE];


		int code;
		while((code = inflow.getUInt8()) != BOHR_RequestProtocol.TERMINATED) {

			switch(code) {

			case BOHR_RequestProtocol.VIEW_ID: viewIndex = inflow.getInt64(); break;

			case BOHR_RequestProtocol.FIELD_ID: methodCode = inflow.getUInt8(); break;

			case BOHR_RequestProtocol.PARAM0: params[0] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM1: params[1] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM2: params[2] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM3: params[3] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM4: params[4] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM5: params[5] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM6: params[6] = inflow.retrieve(); break;

			case BOHR_RequestProtocol.PARAM7: params[7] = inflow.retrieve(); break;

			default: throw new IOException("Failed to match code for this field: "+code);
			}
		}
	}
	*/


	/**
	 * 
	 * @return the sub-protocol index within the [0x00, 0xff] index range.
	 */
	public int getForkCode() {
		return forkCode;
	}


	/**
	 * 
	 * @return the view index [0x0, 0xffffffffffffffff]
	 */
	public long getViewIndex() {
		return viewIndex;
	}



	/**
	 * 
	 * @return the method code
	 */
	public int getMethodCode() {
		return methodCode;
	}



	/**
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return params;
	}

	/**
	 * 
	 * @return
	 */
	public Object getParameter(int index) {
		return params[index];
	}
	

}
