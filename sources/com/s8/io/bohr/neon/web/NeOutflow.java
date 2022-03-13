package com.s8.io.bohr.neon.web;

import java.io.IOException;

import com.s8.io.bohr.neon.BOHR_Encoding;
import com.s8.io.bytes.alpha.ByteOutflow;

public interface NeOutflow extends ByteOutflow {



	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishArrayLength(int value) throws IOException {
		if(value > 0) {
			if(value <= 0xff) {
				putUInt8(BOHR_Encoding.UINT8);
				putUInt8(value);
			}
			else if(value <= 0xffff) {
				putUInt8(BOHR_Encoding.UINT16);
				putUInt16(value);
			}
			else {
				putUInt8(BOHR_Encoding.UINT31);
				putUInt31(value);
			}
		}
		else if(value == 0){
			putUInt8(BOHR_Encoding.ZERO_INT);
		}
		else { // if(value < 0)
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}


	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishBool8(boolean value) throws IOException {
		putUInt8(value ? BOHR_Encoding.TRUE_BOOL8 : BOHR_Encoding.FALSE_BOOL8);
	}
	
	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishBooleanArray(boolean[] value) throws IOException {
		putUInt8(BOHR_Encoding.BOOLEAN_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishBool8(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}



	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishShort(short value) throws IOException {
		if(value != 0) {
			if(value > 0) {
				if(value <= 0xff) {
					putUInt8(BOHR_Encoding.UINT8);
					putUInt8(value);
				}
				else {
					putUInt8(BOHR_Encoding.UINT16);
					putUInt16(value);
				}
			}
			else { // negative
				putUInt8(BOHR_Encoding.INT16);
				putInt16(value);
			}
		}
		else {
			putUInt8(BOHR_Encoding.ZERO_INT);
		}
	}
	
	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishShortArray(short[] value) throws IOException {
		putUInt8(BOHR_Encoding.SHORT_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishShort(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}


	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishInteger(int value) throws IOException {
		if(value != 0) {
			if(value > 0) {
				if(value <= 0xff) {
					putUInt8(BOHR_Encoding.UINT8);
					putUInt8(value);
				}
				else if(value <= 0xffff) {
					putUInt8(BOHR_Encoding.UINT16);
					putUInt16(value);
				}
				else {
					putUInt8(BOHR_Encoding.UINT31);
					putUInt31(value);
				}
			}
			else { // negative
				if(value > -0xffff) {
					putUInt8(BOHR_Encoding.INT16);
					putInt16((short) value);
				}
				else {
					putUInt8(BOHR_Encoding.INT32);
					putInt32(value);
				}
			}
		}
		else {
			putUInt8(BOHR_Encoding.ZERO_INT);
		}
	}
	
	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishIntegerArray(int[] value) throws IOException {
		putUInt8(BOHR_Encoding.INTEGER_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishInteger(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}
	
	

	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishUInt8Array(int[] value) throws IOException {
		putUInt8(BOHR_Encoding.UINT8_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { putUInt8(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}
	
	

	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishUInt16Array(int[] value) throws IOException {
		putUInt8(BOHR_Encoding.UINT8_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { putUInt16(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}
	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishUInt32Array(int[] value) throws IOException {
		putUInt8(BOHR_Encoding.UINT8_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { putUInt32(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}

	
	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishLong(long value) throws IOException {
		if(value != 0) {
			if(value > 0) {
				if(value <= 0xff) {
					putUInt8(BOHR_Encoding.UINT8);
					putUInt8((int) value);
				}
				else if(value <= 0xffff) {
					putUInt8(BOHR_Encoding.UINT16);
					putUInt16((int) value);
				}
				else if(value <= 0xffffffff) {
					putUInt8(BOHR_Encoding.UINT31);
					putUInt31((int) value);
				}
				else {
					putUInt8(BOHR_Encoding.UINT63);
					putUInt64(value);
				}
			}
			else { // negative
				if(value > -0xffff) {
					putUInt8(BOHR_Encoding.INT16);
					putInt16((short) value);
				}
				else if(value > -0xffffffff) {
					putUInt8(BOHR_Encoding.INT32);
					putInt32((int) value);
				}
				else {
					putUInt8(BOHR_Encoding.INT64);
					putInt64((int) value);
				}
			}
		}
		else {
			putUInt8(BOHR_Encoding.ZERO_INT);
		}
	}
	
	public default void publishLongArray(long[] value) throws IOException {
		putUInt8(BOHR_Encoding.LONG_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishLong(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}




	public default void publishFloat(float value) throws IOException {
		if(value != 0.00f) {
			putUInt8(BOHR_Encoding.FLOAT32);
			putFloat32(value);
		}
		else {
			putUInt8(BOHR_Encoding.ZERO_FLOAT);
		}
	}
	
	public default void publishFloatArray(float[] value) throws IOException {
		putUInt8(BOHR_Encoding.FLOAT_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishFloat(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}
	
	public default void publishFloat32Array(float[] value) throws IOException {
		putUInt8(BOHR_Encoding.FLOAT32_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { putFloat32(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}



	public default void publishDouble(double value) throws IOException {
		if(value != 0.00) {
			putUInt8(BOHR_Encoding.FLOAT64);
			putFloat64(value);
		}
		else {
			putUInt8(BOHR_Encoding.ZERO_FLOAT);
		}
	}


	public default void publishDoubleArray(double[] value) throws IOException {
		putUInt8(BOHR_Encoding.DOUBLE_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishDouble(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}
	
	
	public default void publishFloat64Array(double[] value) throws IOException {
		putUInt8(BOHR_Encoding.FLOAT64_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { putFloat64(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}



	/**
	 * 
	 * @param value
	 * @throws IOException
	 */
	public default void publishString(String value) throws IOException {
		if(value != null) {
			if(value.length()>0) {
				putUInt8(BOHR_Encoding.L32_STRING_UTF8);
				putL32StringUTF8(value);
			}
			else {
				putUInt8(BOHR_Encoding.EMPTY_STRING);
			}
		}
		else {
			putUInt8(BOHR_Encoding.NULL_STRING);
		}
	}


	public default void publishStringArray(String[] value) throws IOException {
		putUInt8(BOHR_Encoding.STRING_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishString(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}



	/**
	 * 
	 * @param index
	 * @throws IOException
	 */
	public default void publishId(long index) throws IOException {
		if(index>=0) {
			if(index <= 0xffL) {
				putUInt8(BOHR_Encoding.ID_UINT8);
				putUInt8((int) index);
			}
			else if(index <= 0xffffL) {
				putUInt8(BOHR_Encoding.ID_UINT16);
				putUInt16((int) index);
			}
			else if(index <= 0xffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT24);
				putUInt24((int) index);
			}
			else if(index <= 0xffffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT32);
				putUInt32(index);
			}
			else if(index <= 0xffffffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT40);
				putUInt40(index);
			}
			else if(index <= 0xffffffffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT48);
				putUInt48(index);
			}
			else if(index <= 0xffffffffffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT56);
				putUInt56(index);
			}
			else if(index <= 0xffffffffffffffffL) {
				putUInt8(BOHR_Encoding.ID_UINT63);
				putUInt64(index);
			}
		}
		else {
			putUInt8(BOHR_Encoding.NULL_ID);
		}
	}
	

	public default void publishIdArray(long[] value) throws IOException {
		putUInt8(BOHR_Encoding.INDEX_ARRAY);
		if(value!=null) {
			int length = value.length;
			publishArrayLength(length);
			for(int i=0; i<length; i++) { publishId(value[i]); }
		}
		else {
			putUInt8(BOHR_Encoding.MINUS_ONE_INT);
		}
	}





	/**
	 * 
	 * @param isEnabled
	 * @throws IOException
	 */
	public default void publishControl(boolean isEnabled) throws IOException {
		if(isEnabled) {
			putUInt8(BOHR_Encoding.ENABLED_METHOD);	
		}
		else {
			putUInt8(BOHR_Encoding.DISABLED_METHOD);
		}	
	}


}
