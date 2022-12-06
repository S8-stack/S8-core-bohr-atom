package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.BOHR_Keywords;
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
	
	private NeFieldHandler[] fieldComposers;

	private Map<String, NeFieldHandler> fieldComposersByName;

	
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
		this.fieldComposers = new NeFieldHandler[2];
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
	public Bool8NeFieldHandler getBool8Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
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


	public Bool8ArrayNeFieldHandler getBool8ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
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
	public UInt8NeFieldHandler getUInt8Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (UInt8NeFieldHandler) field;
		}
		else {
			UInt8NeFieldHandler newField = new UInt8NeFieldHandler(this, name);
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



	public UInt8ArrayNeFieldHandler getUInt8ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt8ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public UInt16NeFieldHandler getUInt16Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt16NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public UInt16ArrayNeFieldHandler getUInt16ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
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
	public UInt32NeFieldHandler getUInt32Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
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
	public UInt32ArrayNeFieldHandler getUInt32ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt32ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public UInt64NeFieldHandler getUInt64Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public UInt64ArrayNeFieldHandler getUInt64ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != UInt64ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int8NeFieldHandler getInt8Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int8ArrayNeFieldHandler getInt8ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int8ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int16NeFieldHandler getInt16Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int16ArrayNeFieldHandler getInt16ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int16ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int32NeFieldHandler getInt32Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int32ArrayNeFieldHandler getInt32ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int32ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Int64NeFieldHandler getInt64Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Int64NeFieldHandler) field;
		}
		else {
			Int64NeFieldHandler newField = new Int64NeFieldHandler(this, name);
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
	public Int64ArrayNeFieldHandler getInt64ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Int64ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
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
	public Float32NeFieldHandler getFloat32Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float32NeFieldHandler) field;
		}
		else {
			Float32NeFieldHandler newField = new Float32NeFieldHandler(this, name);
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
	public Float32ArrayNeFieldHandler getFloat32ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float32ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float32ArrayNeFieldHandler) field;
		}
		else {
			Float32ArrayNeFieldHandler newField = new Float32ArrayNeFieldHandler(this, name);
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
	public Float64NeFieldHandler getFloat64Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64NeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float64NeFieldHandler) field;
		}
		else {
			Float64NeFieldHandler newField = new Float64NeFieldHandler(this, name);
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
	public Float64ArrayNeFieldHandler getFloat64ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != Float64ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (Float64ArrayNeFieldHandler) field;
		}
		else {
			Float64ArrayNeFieldHandler newField = new Float64ArrayNeFieldHandler(this, name);
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
	public StringUTF8NeFieldHandler getStringUTF8Field(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
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
	public StringUTF8ArrayNeFieldHandler getStringUTF8ArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != StringUTF8ArrayNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (StringUTF8ArrayNeFieldHandler) field;
		}
		else {
			StringUTF8ArrayNeFieldHandler newField = new StringUTF8ArrayNeFieldHandler(this, name);
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
	public <T extends NeObject> ObjNeFieldHandler<T> getObjField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != ObjNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (ObjNeFieldHandler<T>) field;
		}
		else {
			ObjNeFieldHandler<T> newField = new ObjNeFieldHandler<T>(this, name);
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
	public <T extends NeObject> ListNeFieldHandler<T> getObjArrayField(String name) {
		NeFieldHandler field = fieldComposersByName.get(name);
		if(field != null) {
			if(field.getSignature() != ListNeFieldHandler.SIGNATURE) { 
				throw new RuntimeException("Cannot change field signature"); }
			return (ListNeFieldHandler<T>) field;
		}
		else {
			ListNeFieldHandler<T> newField = new ListNeFieldHandler<T>(this, name);
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
	private void appendField(NeFieldHandler field) {
		int position = fieldComposers.length;
		field.ordinal = position;
		field.code = position;

		NeFieldHandler[] extended = new NeFieldHandler[position+1];
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

				NeFieldHandler field = fieldComposers[code];

				/* declare field (if not already done) */
				field.declare(outflow);

				/* publish entry */
				value.publishEntry(code, outflow);
			}
		}
	}
}
