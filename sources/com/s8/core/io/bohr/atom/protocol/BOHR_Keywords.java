package com.s8.core.io.bohr.atom.protocol;

import java.nio.charset.StandardCharsets;


/**
 * 
 * @author Pierre Convert
 * Copyright (c) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class BOHR_Keywords {





	/**
	 * 
	 */
	public final static byte[] FRAME_HEADER = "<BOHR_:>".getBytes(StandardCharsets.US_ASCII);

	public final static byte[] FRAME_FOOTER = "</BOHR_>".getBytes(StandardCharsets.US_ASCII);





	public final static int OPEN_SEQUENCE = 0x02;


	public final static int CLOSE_SEQUENCE = 0x0f;


	/**
	 * 
	 */
	public final static int OPEN_JUMP = 0x22;


	public final static int DEFINE_JUMP_TIMESTAMP = 0x24;
	public final static int DEFINE_JUMP_AUTHOR = 0x25;
	public final static int DEFINE_JUMP_COMMENT = 0x26;

	//public final static int DEFINE_JUMP_VERSION = 0x26;

	/**
	 * 
	 */
	public final static int CLOSE_JUMP = 0x2f;



	/* <types> */


	/**
	 * 
	 */
	public final static int DECLARE_TYPE = 0x32;

	/* </types> */


	/* <nodes> */


	/**
	 * (Must be closed)
	 */
	public final static int CREATE_NODE = 0x42;

	/**
	 * (must be closed)
	 */
	public final static int UPDATE_NODE = 0x43;


	/**
	 * no-closing required
	 */
	public final static int EXPOSE_NODE = 0x44;


	/**
	 * no-closing required
	 */
	public final static int REMOVE_NODE = 0x45;

	/**
	 * 
	 */
	public final static int CLOSE_NODE = 0x4f;
	/* </nodes> */


	/* <fields> */



	/**
	 * declare field and set value
	 */
	public final static int DECLARE_FIELD = 0x52;

	/* </fields> */

	/* <value> */

	/**
	 * 
	 */
	public final static int SET_VALUE = 0x64;

	/* </value> */



	public final static int DECLARE_METHOD = 0x72;



	public final static int RUN_METHOD = 0x74;



	public final static int DECLARE_PROVIDER = 0x82;



	public final static int RUN_PROVIDER = 0x84;



}
