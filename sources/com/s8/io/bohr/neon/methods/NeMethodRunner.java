package com.s8.io.bohr.neon.methods;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeFunc;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.methods.arrays.Bool8ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Float32ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Float64ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Int16ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Int32ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Int64ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.Int8ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.StringUTF8ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.UInt16ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.UInt32ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.UInt64ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.UInt8ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Bool8NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Float32NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Float64NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Int16NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Int32NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Int64NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.Int8NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.StringUTF8NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.UInt16NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.UInt32NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.UInt64NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.UInt8NeMethodRunner;
import com.s8.io.bohr.neon.methods.primitives.VoidNeMethodRunner;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class NeMethodRunner {


	public NeObjectTypeHandler prototype;
	/**
	 * 
	 */
	public int code;

	public final int ordinal;


	public final String name;


	/**
	 * 
	 * @param name
	 */
	public NeMethodRunner(NeObjectTypeHandler prototype, String name, int ordinal) {
		super();
		this.prototype = prototype;
		this.name = name;
		this.ordinal = ordinal;
	}


	public abstract long getSignature();


	public NeFunc createFunc() {
		return new NeFunc();
	}


	/**
	 * 
	 * @param branch
	 * @param inflow
	 * @param lambda
	 * @throws IOException
	 */
	public abstract void run(NeBranch branch, ByteInflow inflow, Object lambda) throws IOException;






	/**
	 * 
	 * @param {ByteInflow} inflow 
	 * @throws IOException 
	 * @returns {NeFieldParser}
	 */
	public static long parseFormat(ByteInflow inflow) throws IOException {

		int code = inflow.getUInt8();

		switch (code) {
		
		/* <specials> */
		case BOHR_Types.VOID: return VoidNeMethodRunner.SIGNATURE;
		/* </specials> */


		/* <structure> */
		case BOHR_Types.ARRAY: return parseArrayFormat(inflow);
		/* </structure> */

		/* <bytes> */
		case BOHR_Types.SERIAL: throw new IOException("Unsupported serial");
		/* </bytes> */


		/* <booleans> */
		case BOHR_Types.BOOL8: return Bool8NeMethodRunner.SIGNATURE;
		/* </booleans> */

		/* <unsigned-integers> */
		case BOHR_Types.UINT8: return UInt8NeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT16: return UInt16NeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT32: return UInt32NeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT64: return UInt64NeMethodRunner.SIGNATURE;
		/* </unsigned-integers> */

		/* <signed-integers> */
		case BOHR_Types.INT8: return Int8NeMethodRunner.SIGNATURE;
		case BOHR_Types.INT16: return Int16NeMethodRunner.SIGNATURE;
		case BOHR_Types.INT32: return Int32NeMethodRunner.SIGNATURE;
		case BOHR_Types.INT64: return Int64NeMethodRunner.SIGNATURE;
		/* </signed-integers> */

		/* <float> */
		case BOHR_Types.FLOAT32: return Float32NeMethodRunner.SIGNATURE;
		case BOHR_Types.FLOAT64: return Float64NeMethodRunner.SIGNATURE;
		/* </float> */

		/* <string> */
		case BOHR_Types.STRING_UTF8: return StringUTF8NeMethodRunner.SIGNATURE;
		/* </string> */

		/* <object> */
		// case BOHR_Types.S8OBJECT: return S8ObjectNeMethodRunner.SIGNATURE;
		/* </object> */

		default: throw new IOException("Unsupported BOHR type code: " + code);
		}
	}


	/**
	 * 
	 * @param inflow
	 * @return
	 * @throws IOException
	 */
	private static long parseArrayFormat(ByteInflow inflow) throws IOException {
		int code = inflow.getUInt8();
		switch (code) {

		case BOHR_Types.BOOL8: return Bool8ArrayNeMethodRunner.SIGNATURE;

		case BOHR_Types.UINT8: return UInt8ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT16: return UInt16ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT32: return UInt32ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.UINT64: return UInt64ArrayNeMethodRunner.SIGNATURE;

		case BOHR_Types.INT8: return Int8ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.INT16: return Int16ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.INT32: return Int32ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.INT64: return Int64ArrayNeMethodRunner.SIGNATURE;

		case BOHR_Types.FLOAT32: return Float32ArrayNeMethodRunner.SIGNATURE;
		case BOHR_Types.FLOAT64: return Float64ArrayNeMethodRunner.SIGNATURE;

		case BOHR_Types.STRING_UTF8: return StringUTF8ArrayNeMethodRunner.SIGNATURE;
		//case BOHR_Types.S8OBJECT: return S8ObjectArrayNeMethodRunner.SIGNATURE;

		default: throw new IOException("Unsupported BOHR ARRAY type code: " + code);
		}
	}

}
