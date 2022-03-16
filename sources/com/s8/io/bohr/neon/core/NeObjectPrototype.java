package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.BOHR_Keywords;
import com.s8.io.bohr.neon.fields.NeField;
import com.s8.io.bohr.neon.fields.NeValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int16ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int32ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt8ArrayNeField;
import com.s8.io.bohr.neon.fields.objects.NeList;
import com.s8.io.bohr.neon.fields.objects.NeObj;
import com.s8.io.bohr.neon.fields.primitives.Bool8NeField;
import com.s8.io.bohr.neon.fields.primitives.Float32NeField;
import com.s8.io.bohr.neon.fields.primitives.Float64NeField;
import com.s8.io.bohr.neon.fields.primitives.Int16NeField;
import com.s8.io.bohr.neon.fields.primitives.Int32NeField;
import com.s8.io.bohr.neon.fields.primitives.Int64NeField;
import com.s8.io.bohr.neon.fields.primitives.Int8NeField;
import com.s8.io.bohr.neon.fields.primitives.StringUTF8NeField;
import com.s8.io.bohr.neon.fields.primitives.UInt16NeField;
import com.s8.io.bohr.neon.fields.primitives.UInt32NeField;
import com.s8.io.bohr.neon.fields.primitives.UInt64NeField;
import com.s8.io.bohr.neon.fields.primitives.UInt8NeField;
import com.s8.io.bohr.neon.methods.NeMethod;
import com.s8.io.bohr.neon.methods.NeRunnable;
import com.s8.io.bohr.neon.methods.arrays.BooleanArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.DoubleArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.FloatArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.IntegerArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.LongArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.ShortArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.StringArrayNeMethod;
import com.s8.io.bohr.neon.methods.objects.ListNeMethod;
import com.s8.io.bohr.neon.methods.objects.ObjNeMethod;
import com.s8.io.bohr.neon.methods.primitives.BooleanNeMethod;
import com.s8.io.bohr.neon.methods.primitives.DoubleNeMethod;
import com.s8.io.bohr.neon.methods.primitives.FloatNeMethod;
import com.s8.io.bohr.neon.methods.primitives.IntegerNeMethod;
import com.s8.io.bohr.neon.methods.primitives.LongNeMethod;
import com.s8.io.bohr.neon.methods.primitives.ShortNeMethod;
import com.s8.io.bohr.neon.methods.primitives.StringNeMethod;
import com.s8.io.bohr.neon.methods.primitives.VoidNeMethod;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;



/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NeObjectPrototype {


	/**
	 * The name associated to this object type
	 */
	public final String name;

	public final long outflowCode;

	private NeField[] outboundFields;

	
	private boolean isUnpublished;

	private Map<String, NeField> fieldsByName;



	public NeRunnable[] inboundRunnables;


	private Map<String, NeMethod> methodsByName;


	public NeObjectPrototype(String name, long code) {
		super();
		this.name = name;
		this.outflowCode = code;
		this.outboundFields = new NeField[2];
		fieldsByName = new HashMap<>();


		this.inboundRunnables = new NeRunnable[2];
		this.methodsByName = new HashMap<>();
		
		isUnpublished = true;
	}




	public final static String RUNTIME_MODFICATION_ERROR_MESSAGE = "Prototype can only be edited at compile time";

	
	

	public VoidNeMethod getVoidMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != VoidNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (VoidNeMethod) method;
		}
		else {
			VoidNeMethod newMethod = new VoidNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Bool8NeField getBool8Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8NeField) field;
		}
		else {
			Bool8NeField newField = new Bool8NeField(this, name);
			appendField(newField);
			return newField;
		}
	}




	public BooleanNeMethod getBooleanMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != BooleanNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (BooleanNeMethod) method;
		}
		else {
			BooleanNeMethod newMethod = new BooleanNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	public Bool8ArrayNeField getBool8ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8ArrayNeField) field;
		}
		else {
			Bool8ArrayNeField newField = new Bool8ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	public BooleanArrayNeMethod getBool8ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != BooleanArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (BooleanArrayNeMethod) method;
		}
		else {
			BooleanArrayNeMethod newMethod = new BooleanArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt8NeField getUInt8Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt8NeField) field;
		}
		else {
			UInt8NeField newField = new UInt8NeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	


	public UInt8ArrayNeField getUInt8ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt8ArrayNeField) field;
		}
		else {
			UInt8ArrayNeField newField = new UInt8ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt16NeField getUInt16Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt16NeField) field;
		}
		else {
			UInt16NeField newField = new UInt16NeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	

	

	


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt16ArrayNeField getUInt16ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt16ArrayNeField) field;
		}
		else {
			UInt16ArrayNeField newField = new UInt16ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt32NeField getUInt32Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt32NeField) field;
		}
		else {
			UInt32NeField newField = new UInt32NeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	

	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt32ArrayNeField getUInt32ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt32ArrayNeField) field;
		}
		else {
			UInt32ArrayNeField newField = new UInt32ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt64NeField getUInt64Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64NeField) field;
		}
		else {
			UInt64NeField newField = new UInt64NeField(this, name);
			appendField(newField);
			return newField;
		}
	}




	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public UInt64ArrayNeField getUInt64ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64ArrayNeField) field;
		}
		else {
			UInt64ArrayNeField newField = new UInt64ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	



	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int8NeField getInt8Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8NeField) field;
		}
		else {
			Int8NeField newField = new Int8NeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int8ArrayNeField getInt8ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8ArrayNeField) field;
		}
		else {
			Int8ArrayNeField newField = new Int8ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int16NeField getInt16Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int16NeField) field;
		}
		else {
			Int16NeField newField = new Int16NeField(this, name);
			appendField(newField);
			return newField;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public ShortNeMethod getShortMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != ShortNeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (ShortNeMethod) method;
		}
		else {
			ShortNeMethod newMethod = new ShortNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int16ArrayNeField getInt16ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int16ArrayNeField) field;
		}
		else {
			Int16ArrayNeField newField = new Int16ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public ShortArrayNeMethod getShortArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != ShortArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (ShortArrayNeMethod) method;
		}
		else {
			ShortArrayNeMethod newMethod = new ShortArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int32NeField getInt32Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int32NeField) field;
		}
		else {
			Int32NeField newField = new Int32NeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public IntegerNeMethod getIntegerMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != IntegerNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (IntegerNeMethod) method;
		}
		else {
			IntegerNeMethod newMethod = new IntegerNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int32ArrayNeField getInt32ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int32ArrayNeField) field;
		}
		else {
			Int32ArrayNeField newField = new Int32ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	

	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public IntegerArrayNeMethod getInt32ArrayNeMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != IntegerArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (IntegerArrayNeMethod) method;
		}
		else {
			IntegerArrayNeMethod newMethod = new IntegerArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int64NeField getInt64Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int64NeField) field;
		}
		else {
			Int64NeField newField = new Int64NeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	
	public LongNeMethod getLongMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != LongNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (LongNeMethod) method;
		}
		else {
			LongNeMethod newMethod = new LongNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Int64ArrayNeField getInt64ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int64ArrayNeField) field;
		}
		else {
			Int64ArrayNeField newField = new Int64ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public LongArrayNeMethod getLongArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != LongNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (LongArrayNeMethod) method;
		}
		else {
			LongArrayNeMethod newMethod = new LongArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Float32NeField getFloat32Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float32NeField) field;
		}
		else {
			Float32NeField newField = new Float32NeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	public FloatNeMethod getFloatMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != FloatNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (FloatNeMethod) method;
		}
		else {
			FloatNeMethod newMethod = new FloatNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Float32ArrayNeField getFloat32ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float32ArrayNeField) field;
		}
		else {
			Float32ArrayNeField newField = new Float32ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}



	public FloatArrayNeMethod getFloatArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != FloatArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (FloatArrayNeMethod) method;
		}
		else {
			FloatArrayNeMethod newMethod = new FloatArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}

	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Float64NeField getFloat64Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64NeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float64NeField) field;
		}
		else {
			Float64NeField newField = new Float64NeField(this, name);
			appendField(newField);
			return newField;
		}
	}


	public DoubleNeMethod getDoubleMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != DoubleNeMethod.SIGNATURE) { throw new RuntimeException("Cannot change method signature"); }
			return (DoubleNeMethod) method;
		}
		else {
			DoubleNeMethod newMethod = new DoubleNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public Float64ArrayNeField getFloat64ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float64ArrayNeField) field;
		}
		else {
			Float64ArrayNeField newField = new Float64ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	

	public DoubleArrayNeMethod getDoubleArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != DoubleArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (DoubleArrayNeMethod) method;
		}
		else {
			DoubleArrayNeMethod newMethod = new DoubleArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}

	

	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public StringUTF8NeField getStringUTF8Field(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8NeField.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature");
			}

			return (StringUTF8NeField) field;
		}
		else {
			StringUTF8NeField newField = new StringUTF8NeField(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	

	

	public StringNeMethod getStringUTF8NeMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringNeMethod) method;
		}
		else {
			StringNeMethod newMethod = new StringNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws RuntimeException 
	 */
	public StringUTF8ArrayNeField getStringUTF8ArrayField(String name) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8ArrayNeField.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (StringUTF8ArrayNeField) field;
		}
		else {
			StringUTF8ArrayNeField newField = new StringUTF8ArrayNeField(this, name);
			appendField(newField);
			return newField;
		}
	}



	

	public StringArrayNeMethod getStringArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringArrayNeMethod) method;
		}
		else {
			StringArrayNeMethod newMethod = new StringArrayNeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}
	


	@SuppressWarnings("unchecked")
	public <T extends NeObject> NeObj<T> getObjField(String string) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != NeObj.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (NeObj<T>) field;
		}
		else {
			NeObj<T> newField = new NeObj<T>(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	

	
	@SuppressWarnings("unchecked")
	public <T extends NeObject>  ObjNeMethod<T> getObjMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != ObjNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (ObjNeMethod<T>) method;
		}
		else {
			ObjNeMethod<T> newMethod = new ObjNeMethod<>(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}
	


	@SuppressWarnings("unchecked")
	public <T extends NeObject> NeList<T> getObjArrayField(String string) {
		NeField field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != NeList.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (NeList<T>) field;
		}
		else {
			NeList<T> newField = new NeList<T>(this, name);
			appendField(newField);
			return newField;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends NeObject> ListNeMethod<T> getObjListMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != ListNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (ListNeMethod<T>) method;
		}
		else {
			ListNeMethod<T> newMethod = new ListNeMethod<T>(this, name);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param field
	 */
	private void appendField(NeField field) {
		int position = outboundFields.length;
		field.ordinal = position;
		field.code = position;

		NeField[] extended = new NeField[position+1];
		for(int i=0; i<position; i++) {
			extended[i] = outboundFields[i];
		}
		extended[position] = field;
		outboundFields = extended;

		if(fieldsByName.containsKey(field.name)) {
			System.err.println("NE_COMPILE_ERROR: name conflict: "+field.name);
		}

		fieldsByName.put(field.name, field);
	}



	/**
	 * 
	 * @param field
	 */
	private void appendMethod(NeMethod method) {
	
		if(methodsByName.containsKey(method.name)) {
			System.err.println("NE_COMPILE_ERROR: METHOD name conflict: "+method.name);
		}

		methodsByName.put(method.name, method);
	}
	
	
	public void consumeDeclareRunnable(ByteInflow inflow) throws IOException {
		
		String methodName = inflow.getStringUTF8();
		
		
		NeMethod method = methodsByName.get(methodName);
		if(inboundRunnables == null) {
			System.err.println("CANNOT find method for name : "+methodName);
		}
		
		
		NeRunnable runnable = method.buildRunnable(inflow);
		

		int code = inflow.getUInt8();
		
		int n = inboundRunnables.length;
		
		/* extend if necessary */
		if(n <= code) {
			int m = inboundRunnables.length;
			while(m <= code) { m*=2; }
			NeRunnable[] extended = new NeRunnable[m];
			for(int i=0; i<n; i++) { extended[i] = inboundRunnables[i]; }
			inboundRunnables = extended;
		}

		inboundRunnables[code] = runnable;
	}
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void publish_DECLARE_TYPE(ByteOutflow outflow) throws IOException {
		
		if(isUnpublished) {
			
			// declare type
			outflow.putUInt8(BOHR_Keywords.DECLARE_TYPE);
			
			/* publish name */
			outflow.putStringUTF8(name);

			/* publish code */
			outflow.putUInt7x(outflowCode);
			
			isUnpublished = false;
		}
	}
	
	
	/**
	 * 
	 * @param object
	 * @param outflow
	 * @throws IOException
	 */
	public void publishFields(NeValue[] values, ByteOutflow outflow) throws IOException {
		
		
		int n = outboundFields.length;
		
		for(int code =0; code < n; code++) {
			NeField field = outboundFields[code];
			if(field != null) {
				
				/* declare field (if not already done) */
				field.declare(outflow);
			
				/* publish entry */
				values[code].publishEntry(code, outflow);
			}
		}
	}
}
