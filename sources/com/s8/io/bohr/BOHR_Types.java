package com.s8.io.bohr;


/**
 * 
 * @author Pierre Convert
 * Copyright (c) 2022, Pierre Convert. All rights reserved.
 * 
 * 
 */
public class BOHR_Types {

	
	/* <core> */
	
	public final static int VOID = 0x02;
	
	/* </core> */
	
	
	/* <structure> */
	public final static int TUPLE = 0x12;
	
	public final static int ARRAY = 0x14;
	
	public final static int HASH_MAP = 0x16;
	
	public final static int TREE_MAP = 0x17;

	/* </structure> */
	
	
	/* <byte> */
	public final static int BYTE = 0x22;
	
	
	/**
	 * Open new vista
	 */
	public final static int SERIAL = 0x24;	
	/*</byte> */
	
	
	
	/* <boolean> */
	public final static int BOOL8 = 0x32;	
	public final static int BOOL8_BIT_ARRAY = 0x33;	
	public final static int BOOL64 = 0x38;
	/* </boolean> */
	
	
	/* <unsigned-integer> */
	public final static int UINT8 = 0x41;
	public final static int UINT16 = 0x42;
	public final static int UINT32 = 0x44;	
	public final static int UINT64 = 0x48;
	/* </unsigned-integer> */
	
	
	/* <signed-integer> */
	public final static int INT8 = 0x51;	
	public final static int INT16 = 0x52;	
	public final static int INT32 = 0x54;
	public final static int INT64 = 0x58;
	/* </signed-integer> */
	
	
	/* <float> */
	public final static int FLOAT32 = 0x62;	
	public final static int FLOAT64 = 0x64;
	/* </float> */
	
	/* <string> */
	public final static int STRING_UTF8 = 0x72;	
	/* </string> */
	
	
	
	/* <object> */
	
	/**
	 * 
	 */
	public final static int S8OBJECT = 0x82;
	
	public final static int S8REF = 0x84;
	
	public final static int S8TABLE = 0x86;
	/* </object> */

	
	
}
