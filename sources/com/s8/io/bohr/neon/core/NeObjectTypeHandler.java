package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.BOHR_Keywords;
import com.s8.io.bohr.neon.fields.NeFieldComposer;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Int16ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Int32ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.Int8ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.UInt64ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.arrays.UInt8ArrayNeFieldComposer;
import com.s8.io.bohr.neon.fields.objects.ListNeFieldComposer;
import com.s8.io.bohr.neon.fields.objects.ObjNeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Bool8NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Float32NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Float64NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Int16NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Int32NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Int64NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.Int8NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.StringUTF8NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.UInt16NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.UInt32NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.UInt64NeFieldComposer;
import com.s8.io.bohr.neon.fields.primitives.UInt8NeFieldComposer;
import com.s8.io.bohr.neon.methods.NeMethodRunner;
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
import com.s8.io.bohr.neon.methods.objects.ListNeMethodRunner;
import com.s8.io.bohr.neon.methods.objects.ObjNeMethodRunner;
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
	public final String outboundTypeName;

	public final long code;

	private boolean isUnpublished;
	
	private NeFieldComposer[] fieldComposers;

	private Map<String, NeFieldComposer> fieldComposersByName;

	
	/**
	 * Code-based. Code is defined by inbound
	 */
	public NeMethodRunner[] methodRunners;

	private Map<String, NeMethodRunner> methodRunnersByName;

	private int nextMethodOrdinal = 0;

	public NeObjectTypeHandler(String name, long code) {
		super();
		this.outboundTypeName = name;
		this.code = code;
		this.fieldComposers = new NeFieldComposer[2];
		fieldComposersByName = new HashMap<>();


		this.methodRunners = new NeMethodRunner[2];
		this.methodRunnersByName = new HashMap<>();

		isUnpublished = true;
	}




	public final static String RUNTIME_MODFICATION_ERROR_MESSAGE = "Prototype can only be edited at compile time";


	
	
	public VoidNeMethodRunner getVoidMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != VoidNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (VoidNeMethodRunner) method;
		}
		else {
			VoidNeMethodRunner newMethod = new VoidNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Bool8NeFieldComposer getBool8Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8NeFieldComposer.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8NeFieldComposer) field;
		}
		else {
			Bool8NeFieldComposer newField = new Bool8NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}




	public Bool8NeMethodRunner getBooleanMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Bool8NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Bool8NeMethodRunner) method;
		}
		else {
			Bool8NeMethodRunner newMethod = new Bool8NeMethodRunner(this, name, nextMethodOrdinal++);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	public Bool8ArrayNeFieldComposer getBool8ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Bool8ArrayNeFieldComposer.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (Bool8ArrayNeFieldComposer) field;
		}
		else {
			Bool8ArrayNeFieldComposer newField = new Bool8ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	public Bool8ArrayNeMethodRunner getBool8ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Bool8ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Bool8ArrayNeMethodRunner) method;
		}
		else {
			Bool8ArrayNeMethodRunner newMethod = new Bool8ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt8NeFieldComposer getUInt8Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt8NeFieldComposer) field;
		}
		else {
			UInt8NeFieldComposer newField = new UInt8NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt8NeMethodRunner getUInt8Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt8NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt8NeMethodRunner) method;
		}
		else {
			UInt8NeMethodRunner newMethod = new UInt8NeMethodRunner(this, name, nextMethodOrdinal++);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	public UInt8ArrayNeFieldComposer getUInt8ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt8ArrayNeFieldComposer) field;
		}
		else {
			UInt8ArrayNeFieldComposer newField = new UInt8ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt8ArrayNeMethodRunner getUInt8ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt16ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt8ArrayNeMethodRunner) method;
		}
		else {
			UInt8ArrayNeMethodRunner newMethod = new UInt8ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt16NeFieldComposer getUInt16Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt16NeFieldComposer) field;
		}
		else {
			UInt16NeFieldComposer newField = new UInt16NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt16NeMethodRunner getUInt16Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt8NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt16NeMethodRunner) method;
		}
		else {
			UInt16NeMethodRunner newMethod = new UInt16NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt16ArrayNeFieldComposer getUInt16ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16ArrayNeFieldComposer.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt16ArrayNeFieldComposer) field;
		}
		else {
			UInt16ArrayNeFieldComposer newField = new UInt16ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt16ArrayNeMethodRunner getUInt16ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt16ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt16ArrayNeMethodRunner) method;
		}
		else {
			UInt16ArrayNeMethodRunner newMethod = new UInt16ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt32NeFieldComposer getUInt32Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32NeFieldComposer.SIGNATURE) { throw new RuntimeException("Cannot change field signature"); }
			return (UInt32NeFieldComposer) field;
		}
		else {
			UInt32NeFieldComposer newField = new UInt32NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt32NeMethodRunner getUInt32Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt32NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt32NeMethodRunner) method;
		}
		else {
			UInt32NeMethodRunner newMethod = new UInt32NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt32ArrayNeFieldComposer getUInt32ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt32ArrayNeFieldComposer) field;
		}
		else {
			UInt32ArrayNeFieldComposer newField = new UInt32ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt32ArrayNeMethodRunner getUInt32ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt32ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt32ArrayNeMethodRunner) method;
		}
		else {
			UInt32ArrayNeMethodRunner newMethod = new UInt32ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt64NeFieldComposer getUInt64Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt64NeFieldComposer) field;
		}
		else {
			UInt64NeFieldComposer newField = new UInt64NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt64NeMethodRunner getUInt64Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt64NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt64NeMethodRunner) method;
		}
		else {
			UInt64NeMethodRunner newMethod = new UInt64NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public UInt64ArrayNeFieldComposer getUInt64ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt64ArrayNeFieldComposer) field;
		}
		else {
			UInt64ArrayNeFieldComposer newField = new UInt64ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}



	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public UInt64ArrayNeMethodRunner getUInt64ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != UInt64ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt64ArrayNeMethodRunner) method;
		}
		else {
			UInt64ArrayNeMethodRunner newMethod = new UInt64ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int8NeFieldComposer getInt8Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int8NeFieldComposer) field;
		}
		else {
			Int8NeFieldComposer newField = new Int8NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int8NeMethodRunner getInt8Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int8NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int8NeMethodRunner) method;
		}
		else {
			Int8NeMethodRunner newMethod = new Int8NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int8ArrayNeFieldComposer getInt8ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int8ArrayNeFieldComposer) field;
		}
		else {
			Int8ArrayNeFieldComposer newField = new Int8ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int8ArrayNeMethodRunner getInt8ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int8ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int8ArrayNeMethodRunner) method;
		}
		else {
			Int8ArrayNeMethodRunner newMethod = new Int8ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int16NeFieldComposer getInt16Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int16NeFieldComposer) field;
		}
		else {
			Int16NeFieldComposer newField = new Int16NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int16NeMethodRunner getInt16Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int16NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int16NeMethodRunner) method;
		}
		else {
			Int16NeMethodRunner newMethod = new Int16NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int16ArrayNeFieldComposer getInt16ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int16ArrayNeFieldComposer) field;
		}
		else {
			Int16ArrayNeFieldComposer newField = new Int16ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int16ArrayNeMethodRunner getInt16ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int16ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int16ArrayNeMethodRunner) method;
		}
		else {
			Int16ArrayNeMethodRunner newMethod = new Int16ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int32NeFieldComposer getInt32Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int32NeFieldComposer) field;
		}
		else {
			Int32NeFieldComposer newField = new Int32NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int32NeMethodRunner getInt32Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int32NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int32NeMethodRunner) method;
		}
		else {
			Int32NeMethodRunner newMethod = new Int32NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int32ArrayNeFieldComposer getInt32ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int32ArrayNeFieldComposer) field;
		}
		else {
			Int32ArrayNeFieldComposer newField = new Int32ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}



	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int32ArrayNeMethodRunner getInt32ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int32ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int32ArrayNeMethodRunner) method;
		}
		else {
			Int32ArrayNeMethodRunner newMethod = new Int32ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int64NeFieldComposer getInt64Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int64NeFieldComposer) field;
		}
		else {
			Int64NeFieldComposer newField = new Int64NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}

	


	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int64NeMethodRunner getInt64Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int64NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int64NeMethodRunner) method;
		}
		else {
			Int64NeMethodRunner newMethod = new Int64NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Int64ArrayNeFieldComposer getInt64ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int64ArrayNeFieldComposer) field;
		}
		else {
			Int64ArrayNeFieldComposer newField = new Int64ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}



	/**
	 * 
	 * @param name
	 * @return
	 */
	public Int64ArrayNeMethodRunner getInt64ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Int64ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int64ArrayNeMethodRunner) method;
		}
		else {
			Int64ArrayNeMethodRunner newMethod = new Int64ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Float32NeFieldComposer getFloat32Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float32NeFieldComposer) field;
		}
		else {
			Float32NeFieldComposer newField = new Float32NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	public Float32NeMethodRunner getFloat32Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float32NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float32NeMethodRunner) method;
		}
		else {
			Float32NeMethodRunner newMethod = new Float32NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Float32ArrayNeFieldComposer getFloat32ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float32ArrayNeFieldComposer) field;
		}
		else {
			Float32ArrayNeFieldComposer newField = new Float32ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}



	public Float32ArrayNeMethodRunner getFloat32ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float32ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float32ArrayNeMethodRunner) method;
		}
		else {
			Float32ArrayNeMethodRunner newMethod = new Float32ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Float64NeFieldComposer getFloat64Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float64NeFieldComposer) field;
		}
		else {
			Float64NeFieldComposer newField = new Float64NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}


	public Float64NeMethodRunner getFloat64Method(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float64NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float64NeMethodRunner) method;
		}
		else {
			Float64NeMethodRunner newMethod = new Float64NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public Float64ArrayNeFieldComposer getFloat64ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float64ArrayNeFieldComposer) field;
		}
		else {
			Float64ArrayNeFieldComposer newField = new Float64ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}



	public Float64ArrayNeMethodRunner getFloat64ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != Float64ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (Float64ArrayNeMethodRunner) method;
		}
		else {
			Float64ArrayNeMethodRunner newMethod = new Float64ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
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
	public StringUTF8NeFieldComposer getStringUTF8Field(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8NeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature");
			}

			return (StringUTF8NeFieldComposer) field;
		}
		else {
			StringUTF8NeFieldComposer newField = new StringUTF8NeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}






	public StringUTF8NeMethodRunner getStringUTF8NeMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringUTF8NeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringUTF8NeMethodRunner) method;
		}
		else {
			StringUTF8NeMethodRunner newMethod = new StringUTF8NeMethodRunner(this, name, nextMethodOrdinal++);
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
	public StringUTF8ArrayNeFieldComposer getStringUTF8ArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8ArrayNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (StringUTF8ArrayNeFieldComposer) field;
		}
		else {
			StringUTF8ArrayNeFieldComposer newField = new StringUTF8ArrayNeFieldComposer(this, name);
			appendField(newField);
			return newField;
		}
	}





	public StringUTF8ArrayNeMethodRunner getStringUTF8ArrayMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != StringUTF8ArrayNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (StringUTF8ArrayNeMethodRunner) method;
		}
		else {
			StringUTF8ArrayNeMethodRunner newMethod = new StringUTF8ArrayNeMethodRunner(this, name, nextMethodOrdinal++);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	@SuppressWarnings("unchecked")
	public <T extends NeObject> ObjNeFieldComposer<T> getObjField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != ObjNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (ObjNeFieldComposer<T>) field;
		}
		else {
			ObjNeFieldComposer<T> newField = new ObjNeFieldComposer<T>(this, name);
			appendField(newField);
			return newField;
		}
	}




	@SuppressWarnings("unchecked")
	public <T extends NeVertex>  ObjNeMethodRunner<T> getObjMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != ObjNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (ObjNeMethodRunner<T>) method;
		}
		else {
			ObjNeMethodRunner<T> newMethod = new ObjNeMethodRunner<>(this, name, nextMethodOrdinal++);
			appendMethod(newMethod);
			return newMethod;
		}
	}



	@SuppressWarnings("unchecked")
	public <T extends NeObject> ListNeFieldComposer<T> getObjArrayField(String name) {
		NeFieldComposer field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != ListNeFieldComposer.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (ListNeFieldComposer<T>) field;
		}
		else {
			ListNeFieldComposer<T> newField = new ListNeFieldComposer<T>(this, name);
			appendField(newField);
			return newField;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends NeObject> ListNeMethodRunner<T> getObjListMethod(String name) {
		NeMethodRunner method = methodRunnersByName.get(name);
		if(method != null) {
			if(method.getSignature() != ListNeMethodRunner.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); 
			}
			return (ListNeMethodRunner<T>) method;
		}
		else {
			ListNeMethodRunner<T> newMethod = new ListNeMethodRunner<T>(this, name, nextMethodOrdinal++);
			appendMethod(newMethod);
			return newMethod;
		}
	}


	/**
	 * 
	 * @param field
	 */
	private void appendField(NeFieldComposer field) {
		int position = fieldComposers.length;
		field.ordinal = position;
		field.code = position;

		NeFieldComposer[] extended = new NeFieldComposer[position+1];
		for(int i=0; i<position; i++) {
			extended[i] = fieldComposers[i];
		}
		extended[position] = field;
		fieldComposers = extended;

		if(fieldComposersByName.containsKey(field.name)) {
			System.err.println("NE_COMPILE_ERROR: name conflict: "+field.name);
		}

		fieldComposersByName.put(field.name, field);
	}



	/**
	 * Assign ordinal
	 * @param field
	 */
	private void appendMethod(NeMethodRunner methodRunner) {
		
		String name = methodRunner.name;
		
		if(methodRunnersByName.containsKey(name)) {
			System.err.println("NE_COMPILE_ERROR: METHOD name conflict: "+name);
		}
		
		methodRunnersByName.put(name, methodRunner);
	}


	public void consume_DECLARE_METHOD(ByteInflow inflow) throws IOException {

		String methodName = inflow.getStringUTF8();


		NeMethodRunner methodRunner = methodRunnersByName.get(methodName);
		if(methodRunner == null) {
			System.err.println("CANNOT find method for name : "+methodName);
		}

		
		long format = NeMethodRunner.parseFormat(inflow);
		if(format != methodRunner.getSignature()) {
			System.err.println("Lismatch in signature for method: "+methodName);
		}
		

		int code = inflow.getUInt8();
		
		methodRunner.code = code;
		int n = methodRunners.length;

		/* extend if necessary */
		if(n <= code) {
			int m = methodRunners.length;
			while(m <= code) { m*=2; }
			NeMethodRunner[] extended = new NeMethodRunner[m]; 
			for(int i=0; i<n; i++) { extended[i] = methodRunners[i]; }
			methodRunners = extended;
		}

		// method runner is now assined a code
		methodRunners[code] = methodRunner;
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
			outflow.putStringUTF8(outboundTypeName);

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
	public void publishFields(NeFieldValue[] values, ByteOutflow outflow) throws IOException {


		int n = values.length;

		NeFieldValue value;
		for(int code =0; code < n; code++) {

			if((value = values[code]) != null) {

				NeFieldComposer field = fieldComposers[code];

				/* declare field (if not already done) */
				field.declare(outflow);

				/* publish entry */
				value.publishEntry(code, outflow);
			}
		}
	}
}
