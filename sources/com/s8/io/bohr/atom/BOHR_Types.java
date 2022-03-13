package com.s8.io.bohr.atom;

public class BOHR_Types {

	
	/* <system> */
	public final static int TUPLE = 0x12;
	
	public final static int ARRAY = 0x14;
	
	public final static int HASH_MAP = 0x16;
	
	public final static int TREE_MAP = 0x17;

	/* </system> */
	
	
	/* <byte> */
	public final static int BYTE = 0x22;
	
	
	/**
	 * Open new vista
	 */
	public final static int SERIAL = 0x24;
	

	
	/* <SERIAL> */
	public final static int SOUND = 0x20;
	
	public final static int IMAGE = 0x22;
	
	/**
	 * <h1>Engineering (SERIAL - level 1)</h1>
	 * Engineering
	 */
	public final static int NG = 0x64;

	/* <SERIAL> */

	
	/**
	 * <h1>Math (SERIAL > NG - level 2)</h1>
	 * Math for engineering
	 */
	public final static int LINEAR_MATH = 0x24;
	
	/**
	 * <h1>Vector (SERIAL > NG > LINEAR_MATH - level 3)</h1>
	 * Math for engineering
	 */
	public final static int VECTOR2 = 0x22;
	public final static int MATRIX2 = 0x23;
	public final static int AFFINE2 = 0x24;
	
	public final static int VECTOR3 = 0x32;
	public final static int MATRIX3 = 0x33;
	public final static int AFFINE3 = 0x34;
	
	public final static int VECTOR4 = 0x42;
	public final static int MATRIX4 = 0x43;
	public final static int AFFINE4 = 0x44;
	
	public final static int VECTORN = 0x52;
	public final static int MATRIXN = 0x53;
	public final static int AFFINEN = 0x54;
	
	/**
	 * <h1>Math (SERIAL > NG - level 2)</h1>
	 * Math for engineering
	 */
	public final static int THERMODYNAMICS = 0x32;
	
	
	public final static int TH_FLUID = 0x22;
	public final static int TH_STATE = 0x24;
	
	
	public final static int TURBOMACHINES = 0x36;
	public final static int TX_FLOW = 0x12;

	
	
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
