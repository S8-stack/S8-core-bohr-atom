package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.BOHR_Keywords;
import com.s8.io.bohr.neon.fields.NeFieldHandler;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int16ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int32ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.objects.ListNeFieldHandler;
import com.s8.io.bohr.neon.fields.objects.ObjNeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Bool8NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Float32NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Float64NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Int16NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Int32NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Int64NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.Int8NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.StringUTF8NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.UInt16NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.UInt32NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.UInt64NeFieldHandler;
import com.s8.io.bohr.neon.fields.primitives.UInt8NeFieldHandler;
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
public class NeObjectTypeHandler {


	/**
	 * The name associated to this object type
	 */
	public final String name;

	public final long outflowCode;

	private NeFieldHandler[] outboundFields;

	
	private boolean isUnpublished;

	private Map<String, NeFieldHandler> fieldsByName;



	public NeRunnable[] inboundRunnables;


	private Map<String, NeMethod> methodsByName;


	public NeObjectTypeHandler(String name, long code) {
		super();
		this.name = name;
		this.outflowCode = code;
		this.outboundFields = new NeFieldHandler[2];
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
	public Bool8NeFieldHandler getBool8Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8NeFieldHandler) field;
		}
		else {
			Bool8NeFieldHandler newField = new Bool8NeFieldHandler(this, name);
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


	public Bool8ArrayNeFieldHandler getBool8ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8ArrayNeFieldHandler) field;
		}
		else {
			Bool8ArrayNeFieldHandler newField = new Bool8ArrayNeFieldHandler(this, name);
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
	public UInt8NeFieldHandler getUInt8Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt8NeFieldHandler) field;
		}
		else {
			UInt8NeFieldHandler newField = new UInt8NeFieldHandler(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	


	public UInt8ArrayNeFieldHandler getUInt8ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt8ArrayNeFieldHandler) field;
		}
		else {
			UInt8ArrayNeFieldHandler newField = new UInt8ArrayNeFieldHandler(this, name);
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
	public UInt16NeFieldHandler getUInt16Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt16NeFieldHandler) field;
		}
		else {
			UInt16NeFieldHandler newField = new UInt16NeFieldHandler(this, name);
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
	public UInt16ArrayNeFieldHandler getUInt16ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt16ArrayNeFieldHandler) field;
		}
		else {
			UInt16ArrayNeFieldHandler newField = new UInt16ArrayNeFieldHandler(this, name);
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
	public UInt32NeFieldHandler getUInt32Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt32NeFieldHandler) field;
		}
		else {
			UInt32NeFieldHandler newField = new UInt32NeFieldHandler(this, name);
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
	public UInt32ArrayNeFieldHandler getUInt32ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt32ArrayNeFieldHandler) field;
		}
		else {
			UInt32ArrayNeFieldHandler newField = new UInt32ArrayNeFieldHandler(this, name);
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
	public UInt64NeFieldHandler getUInt64Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64NeFieldHandler) field;
		}
		else {
			UInt64NeFieldHandler newField = new UInt64NeFieldHandler(this, name);
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
	public UInt64ArrayNeFieldHandler getUInt64ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64ArrayNeFieldHandler) field;
		}
		else {
			UInt64ArrayNeFieldHandler newField = new UInt64ArrayNeFieldHandler(this, name);
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
	public Int8NeFieldHandler getInt8Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8NeFieldHandler) field;
		}
		else {
			Int8NeFieldHandler newField = new Int8NeFieldHandler(this, name);
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
	public Int8ArrayNeFieldHandler getInt8ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8ArrayNeFieldHandler) field;
		}
		else {
			Int8ArrayNeFieldHandler newField = new Int8ArrayNeFieldHandler(this, name);
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
	public Int16NeFieldHandler getInt16Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int16NeFieldHandler) field;
		}
		else {
			Int16NeFieldHandler newField = new Int16NeFieldHandler(this, name);
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
	public Int16ArrayNeFieldHandler getInt16ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int16ArrayNeFieldHandler) field;
		}
		else {
			Int16ArrayNeFieldHandler newField = new Int16ArrayNeFieldHandler(this, name);
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
	public Int32NeFieldHandler getInt32Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int32NeFieldHandler) field;
		}
		else {
			Int32NeFieldHandler newField = new Int32NeFieldHandler(this, name);
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
	public Int32ArrayNeFieldHandler getInt32ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int32ArrayNeFieldHandler) field;
		}
		else {
			Int32ArrayNeFieldHandler newField = new Int32ArrayNeFieldHandler(this, name);
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
	public Int64NeFieldHandler getInt64Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int64NeFieldHandler) field;
		}
		else {
			Int64NeFieldHandler newField = new Int64NeFieldHandler(this, name);
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
	public Int64ArrayNeFieldHandler getInt64ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int64ArrayNeFieldHandler) field;
		}
		else {
			Int64ArrayNeFieldHandler newField = new Int64ArrayNeFieldHandler(this, name);
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
	public Float32NeFieldHandler getFloat32Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float32NeFieldHandler) field;
		}
		else {
			Float32NeFieldHandler newField = new Float32NeFieldHandler(this, name);
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
	public Float32ArrayNeFieldHandler getFloat32ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float32ArrayNeFieldHandler) field;
		}
		else {
			Float32ArrayNeFieldHandler newField = new Float32ArrayNeFieldHandler(this, name);
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
	public Float64NeFieldHandler getFloat64Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64NeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float64NeFieldHandler) field;
		}
		else {
			Float64NeFieldHandler newField = new Float64NeFieldHandler(this, name);
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
	public Float64ArrayNeFieldHandler getFloat64ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Float64ArrayNeFieldHandler) field;
		}
		else {
			Float64ArrayNeFieldHandler newField = new Float64ArrayNeFieldHandler(this, name);
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
	public StringUTF8NeFieldHandler getStringUTF8Field(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature");
			}

			return (StringUTF8NeFieldHandler) field;
		}
		else {
			StringUTF8NeFieldHandler newField = new StringUTF8NeFieldHandler(this, name);
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
	public StringUTF8ArrayNeFieldHandler getStringUTF8ArrayField(String name) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8ArrayNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (StringUTF8ArrayNeFieldHandler) field;
		}
		else {
			StringUTF8ArrayNeFieldHandler newField = new StringUTF8ArrayNeFieldHandler(this, name);
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
	public <T extends NeObject> ObjNeFieldHandler<T> getObjField(String string) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != ObjNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (ObjNeFieldHandler<T>) field;
		}
		else {
			ObjNeFieldHandler<T> newField = new ObjNeFieldHandler<T>(this, name);
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
	public <T extends NeObject> ListNeFieldHandler<T> getObjArrayField(String string) {
		NeFieldHandler field = fieldsByName.get(name);
		if(field != null) {
			if(field.getSignature() != ListNeFieldHandler.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (ListNeFieldHandler<T>) field;
		}
		else {
			ListNeFieldHandler<T> newField = new ListNeFieldHandler<T>(this, name);
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
	private void appendField(NeFieldHandler field) {
		int position = outboundFields.length;
		field.ordinal = position;
		field.code = position;

		NeFieldHandler[] extended = new NeFieldHandler[position+1];
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
	public void publishFields(NeFieldValue[] values, ByteOutflow outflow) throws IOException {
		
		
		int n = outboundFields.length;
		
		for(int code =0; code < n; code++) {
			NeFieldHandler field = outboundFields[code];
			if(field != null) {
				
				/* declare field (if not already done) */
				field.declare(outflow);
			
				/* publish entry */
				values[code].publishEntry(code, outflow);
			}
		}
	}
}
