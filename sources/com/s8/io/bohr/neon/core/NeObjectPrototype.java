package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.BOHR_Keywords;
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
import com.s8.io.bohr.neon.methods.arrays.Bool8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Float32ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Float64ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Int16ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Int32ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Int64ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Int8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.StringUTF8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt16ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt32ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt64ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.objects.ListNeMethod;
import com.s8.io.bohr.neon.methods.objects.ObjNeMethod;
import com.s8.io.bohr.neon.methods.primitives.Bool8NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Float32NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Float64NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Int16NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Int32NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Int64NeMethod;
import com.s8.io.bohr.neon.methods.primitives.Int8NeMethod;
import com.s8.io.bohr.neon.methods.primitives.StringUTF8NeMethod;
import com.s8.io.bohr.neon.methods.primitives.UInt16NeMethod;
import com.s8.io.bohr.neon.methods.primitives.UInt32NeMethod;
import com.s8.io.bohr.neon.methods.primitives.UInt64NeMethod;
import com.s8.io.bohr.neon.methods.primitives.UInt8NeMethod;
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

	public final long code;

	private NeField[] fields;

	
	private boolean isUnpublished;

	private Map<String, NeField> fieldsByName;



	private NeMethod[] methods;


	private Map<String, NeMethod> methodsByName;


	public NeObjectPrototype(String name, long code) {
		super();
		this.name = name;
		this.code = code;
		this.fields = new NeField[0];
		fieldsByName = new HashMap<>();


		this.methods = new NeMethod[0];
		this.methodsByName = new HashMap<>();
		
		isUnpublished = true;
	}




	public final static String RUNTIME_MODFICATION_ERROR_MESSAGE = "Prototype can only be edited at compile time";

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




	public Bool8NeMethod getBool8Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Bool8NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Bool8NeMethod) method;
		}
		else {
			Bool8NeMethod newMethod = new Bool8NeMethod(this, name);
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

	
	public Bool8ArrayNeMethod getBool8ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Bool8ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Bool8ArrayNeMethod) method;
		}
		else {
			Bool8ArrayNeMethod newMethod = new Bool8ArrayNeMethod(this, name);
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
	
	

	
	public UInt8NeMethod getUInt8Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt8NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt8NeMethod) method;
		}
		else {
			UInt8NeMethod newMethod = new UInt8NeMethod(this, name);
			appendMethod(newMethod);
			return newMethod;
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
	

	public UInt8ArrayNeMethod getUInt8ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt8ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt8ArrayNeMethod) method;
		}
		else {
			UInt8ArrayNeMethod newMethod = new UInt8ArrayNeMethod(this, name);
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
	

	
	public UInt16NeMethod getUInt16Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt16NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt16NeMethod) method;
		}
		else {
			UInt16NeMethod newMethod = new UInt16NeMethod(this, name);
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

	

	
	public UInt16ArrayNeMethod getUInt16ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt16ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt16ArrayNeMethod) method;
		}
		else {
			UInt16ArrayNeMethod newMethod = new UInt16ArrayNeMethod(this, name);
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
	

	
	public UInt32NeMethod getUInt32Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt32NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt32NeMethod) method;
		}
		else {
			UInt32NeMethod newMethod = new UInt32NeMethod(this, name);
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
	 */
	public UInt32ArrayNeMethod getUInt32ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt32NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (UInt32ArrayNeMethod) method;
		}
		else {
			UInt32ArrayNeMethod newMethod = new UInt32ArrayNeMethod(this, name);
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




	public UInt64NeMethod getUInt64Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt64NeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64NeMethod) method;
		}
		else {
			UInt64NeMethod newMethod = new UInt64NeMethod(this, name);
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

	

	public UInt64ArrayNeMethod getUInt64ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt64ArrayNeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt64ArrayNeMethod) method;
		}
		else {
			UInt64ArrayNeMethod newMethod = new UInt64ArrayNeMethod(this, name);
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
	

	public Int8NeMethod getInt8Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int8NeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8NeMethod) method;
		}
		else {
			Int8NeMethod newMethod = new Int8NeMethod(this, name);
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
	 */
	public Int8ArrayNeMethod getInt8ArrayNeMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int8ArrayNeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int8ArrayNeMethod) method;
		}
		else {
			Int8ArrayNeMethod newMethod = new Int8ArrayNeMethod(this, name);
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
	public Int16NeMethod getInt16Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int16NeMethod.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Int16NeMethod) method;
		}
		else {
			Int16NeMethod newMethod = new Int16NeMethod(this, name);
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
	public Int16ArrayNeMethod getInt16ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int16ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Int16ArrayNeMethod) method;
		}
		else {
			Int16ArrayNeMethod newMethod = new Int16ArrayNeMethod(this, name);
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
	public Int32NeMethod getInt32Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int32NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Int32NeMethod) method;
		}
		else {
			Int32NeMethod newMethod = new Int32NeMethod(this, name);
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
	public Int32ArrayNeMethod getInt32ArrayNeMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int32ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Int32ArrayNeMethod) method;
		}
		else {
			Int32ArrayNeMethod newMethod = new Int32ArrayNeMethod(this, name);
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
	
	
	public Int64NeMethod getInt64Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int64NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Int64NeMethod) method;
		}
		else {
			Int64NeMethod newMethod = new Int64NeMethod(this, name);
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
	public Int64ArrayNeMethod getInt64ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int64NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Int64ArrayNeMethod) method;
		}
		else {
			Int64ArrayNeMethod newMethod = new Int64ArrayNeMethod(this, name);
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

	
	public Float32NeMethod getFloat32Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float32NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float32NeMethod) method;
		}
		else {
			Float32NeMethod newMethod = new Float32NeMethod(this, name);
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



	public Float32ArrayNeMethod getFloat32ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float32ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float32ArrayNeMethod) method;
		}
		else {
			Float32ArrayNeMethod newMethod = new Float32ArrayNeMethod(this, name);
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


	public Float64NeMethod getFloat64Method(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt64NeMethod.SIGNATURE) { throw new RuntimeException("Cannot change method signature"); }
			return (Float64NeMethod) method;
		}
		else {
			Float64NeMethod newMethod = new Float64NeMethod(this, name);
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

	

	public Float64ArrayNeMethod getFloat64ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float64ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float64ArrayNeMethod) method;
		}
		else {
			Float64ArrayNeMethod newMethod = new Float64ArrayNeMethod(this, name);
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

	
	

	

	public StringUTF8NeMethod getStringUTF8NeMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringUTF8NeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringUTF8NeMethod) method;
		}
		else {
			StringUTF8NeMethod newMethod = new StringUTF8NeMethod(this, name);
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



	

	public StringUTF8ArrayNeMethod getStringUTF8ArrayMethod(String name) {
		NeMethod method = methodsByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringUTF8ArrayNeMethod.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringUTF8ArrayNeMethod) method;
		}
		else {
			StringUTF8ArrayNeMethod newMethod = new StringUTF8ArrayNeMethod(this, name);
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
		int position = fields.length;
		field.ordinal = position;
		field.code = position;

		NeField[] extended = new NeField[position+1];
		for(int i=0; i<position; i++) {
			extended[i] = fields[i];
		}
		extended[position] = field;
		fields = extended;

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
		int position = fields.length;
		method.ordinal = position;
		method.code = position;

		NeMethod[] extended = new NeMethod[position+1];
		for(int i=0; i<position; i++) {
			extended[i] = methods[i];
		}
		extended[position] = method;
		methods = extended;

		if(methodsByName.containsKey(method.name)) {
			System.err.println("NE_COMPILE_ERROR: METHOD name conflict: "+method.name);
		}

		methodsByName.put(method.name, method);
	}
	
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void declare(ByteOutflow outflow) throws IOException {
		
		if(isUnpublished) {
			
			// declare type
			outflow.putUInt8(BOHR_Keywords.DECLARE_TYPE);
			
			/* publish name */
			outflow.putStringUTF8(name);

			/* publish code */
			outflow.putUInt7x(code);
			
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
		
		
		int n = fields.length;
		
		for(int code =0; code < n; code++) {
			NeField field = fields[code];
			if(field != null) {
				
				/* declare field (if not already done) */
				field.declare(outflow);
			
				/* publish entry */
				values[code].publishEntry(code, outflow);
			}
		}
	}
}
