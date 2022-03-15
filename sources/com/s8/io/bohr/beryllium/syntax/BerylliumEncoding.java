package com.s8.io.bohr.beryllium.syntax;

public class BerylliumEncoding {


	public final static int UINT8_ARRAY_MAX_SIZE = 255;

	public final static int UINT16_ARRAY_MAX_SIZE = 65535;

	/**
	 * true limit is 4294967295, but cannot be encoded in JAVA directly, so choose: 2^31-1
	 */
	public final static int UINT32_ARRAY_MAX_SIZE = 2147483647; 	




	/* <bytes : 0x0X > */

	public final static int BYTE = 0x02;


	public final static int NULL_BYTES = 0x03;

	public final static int EMPTY_BYTES = 0x04;

	public final static int BYTES = 0x06;

	/* </bytes : 0x0X > */

	


	/* </JAVA_booleans : 0x01XX > */



	/**
	 * for pattern
	 */
	public final static int BOOL8 = 0x14;

	/**
	 * sepcial value for true
	 */
	public final static int TRUE_BOOL8 = 0x15;


	/**
	 * special value for false
	 */
	public final static int FALSE_BOOL8 = 0x17;

	/* </JAVA_booleans : 0x01XX > */


	/* </JAVA_ints : 0x02XX > */


	/**
	 * (length encoding for null collections)
	 */
	public final static int VOID_INT = 0x20;

	/**
	 * Big endian 8 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	public final static int ZERO_INT = 0x21;

	/**
	 * Big endian 8 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	public final static int INT_MINUS_ONE = 0x22;

	/**
	 * Big endian 8 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	public final static int UINT8 = 0x23;


	/** 
	 * Big endian 8 bits signed integer
	 * (Java and network is big-endian)
	 */
	public final static int INT8 = 0x024;


	/** 
	 * Big endian 16 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	public final static int UINT16 = 0x25;


	/** 
	 * Big endian 16 bits signed integer
	 * (Java and network is big-endian)
	 */
	public final static int INT16 = 0x26;


	/** 
	 * Big endian 32 bits unsigned integer
	 * (Java and network is big-endian)
	 */
	public final static int UINT31 = 0x27;


	/** 
	 * Big endian 32 bits signed integer
	 * (Java and network is big-endian)
	 */
	public final static int INT32 = 0x28;


	/** 
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 */
	public final static int INT64 = 0x29;

	/** 
	 * (Theorical type, since not supported in JAVA, must be casted)
	 * Big endian 64 bits signed integer
	 * (Java and network is big-endian)
	 */
	public final static int UINT64 = 0x2a;


	/**
	 * Special Uint
	 * (Java and network is big-endian)
	 */
	public final static int UINT = 0x2b;

	/* </JAVA_ints : 0x02XX > */

	/* </JAVA_floats : 0x03XX > */



	/**
	 * zero castable to float, double
	 */
	public final static int ZERO_FLOAT = 0x30;


	/**
	 * The IEEE Standard for Floating-Point Arithmetic (IEEE 754) is a technical
	 * standard for floating-point arithmetic established in 1985 by the Institute
	 * of Electrical and Electronics Engineers (IEEE). The standard addressed many
	 * problems found in the diverse floating-point implementations that made them
	 * difficult to use reliably and portably. Many hardware floating-point units
	 * use the IEEE 754 standard.
	 */
	public final static int FLOAT32 = 0x32;


	/**
	 * Floating point is used to represent fractional values, or when a wider range
	 * is needed than is provided by fixed point (of the same bit width), even if at
	 * the cost of precision. Double precision may be chosen when the range or
	 * precision of single precision would be insufficient.
	 * 
	 * In the IEEE 754-2008 standard, the 64-bit base-2 format is officially
	 * referred to as binary64; it was called double in IEEE 754-1985. IEEE 754
	 * specifies additional floating-point formats, including 32-bit base-2 single
	 * precision and, more recently, base-10 representations.
	 */
	public final static int FLOAT64 = 0x34;

	/* </JAVA_floats : 0x03XX > */



	/* <String : 0x04XX > */


	/**
	 * 
	 */
	public final static int NULL_STRING = 0x42;

	/**
	 * 
	 */
	public final static int EMPTY_STRING = 0x43;

	/**
	 * <p>String with the following characteristics</p>
	 * <ul>
	 * <li>Length is encoded on an UInt8 (this implies that length cannot be more than 256</li>
	 * <li>Chars are encoded with UTF8 format</li>
	 *</ul>
	 */
	public final static int L8_STRING_UTF8 = 0x44;

	/**
	 * <p>
	 * String with the following characteristics
	 * </p>
	 * <ul>
	 * <li>Length is encoded on an UInt32 (this implies that length cannot be more
	 * than 2^31, yes 32 is slightly bullshit since JAVA int naturale encoding is on
	 * 31 bits and 1 bit for sign)</li>
	 * <li>Chars are encoded with UTF8 format</li>
	 * </ul>
	 */
	public final static int L32_STRING_UTF8 = 0x45;

	
	/**
	 * Full ASCII String with length encoded on 8 bits (maximum compacity, use for universal identifiers)
	 */
	public final static int L8_STRING_ASCII = 0x46;


	/* <String : 0x04XX /> */


	/* <objects : 0x06XX > */


	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	public final static int NULL_OBJECT = 0x60;

	/**
	 * 8-bits boolean value
	 * true = 0x08, false = 0x26;
	 */
	public final static int NULL_S8ADDRESS = 0x61;

	/**
	 * 
	 */
	public final static int S8_REFERENCE = 0x62;

	/**
	 * 
	 */
	public final static int UNDEFINED_S8_STRUCT_ID = 0x63;

	/**
	 * 
	 */
	public final static int S8_STRUCT_ID = 0x64;


	/**
	 * 
	 */
	public final static int ENUM_INDEX = 0x66;

	/**
	 * 
	 */
	public final static int TABLE = 0x68;



	/* </objects : 0x06XX > */
	
	
	/* <S8Object : 0x07XX > */

	public final static int S8_SERIAL = 0x72;

	public final static int S8_OBJECT = 0x74;

	public final static int S8_NODE = 0x76;



	/* </S8Object : 0x07XX > */


	/* <extensible types : 0x08XX > */

	public final static int MIME_IMAGE = 0x82;

	public final static int MIME_IMAGE_PNG = 0x22;
	public final static int MIME_IMAGE_JPG = 0x23;


	public final static int MIME_AUDIO =  0x83;

	public final static int MIME_VIDEO = 0x84;

	public final static int MIME_APPLICATION = 0x85;


	/* </extensible types : 0x08XX > */


	/* <arrays : 0x0bXX > */


	/**
	 * empty array
	 */
	public final static int NULL_ARRAY = 0xb0;

	/**
	 * empty array
	 */
	public final static int EMPTY_ARRAY = 0xb1;

	/**
	 * array structure with length encoded as UINT8
	 * 
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt8</b></p>
	 * <p>Next byte indicate the type of data</p>
	 */
	public final static int ARRAY_UINT8 = 0xb2;

	/**
	 * true = 0x08, false = 0x26;
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	public final static int ARRAY_UINT16 = 0xb4;

	/**
	 * true = 0x08, false = 0x26;
	 * <p>/!\ IMPORTANT NOTICE: MAX ARRAY SIZE and LENGTH encoding is <b>UInt16</b></p>
	 */
	public final static int ARRAY_UINT31 = 0xb6;

	/**
	 */
	public final static int ARRAY_UINT64 = 0xb8;


	/*
	 * <collections>
	 */

	public final static int LIST = 0xc2;

	/*
	 * </collections>
	 */
}
