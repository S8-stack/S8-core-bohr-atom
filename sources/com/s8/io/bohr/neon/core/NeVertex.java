package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.neon.core.NeObject.Float32ArrayLambda;
import com.s8.io.bohr.neon.core.NeObject.Float32Lambda;
import com.s8.io.bohr.neon.core.NeObject.Float64ArrayLambda;
import com.s8.io.bohr.neon.core.NeObject.Float64Lambda;
import com.s8.io.bohr.neon.core.NeObject.Int64ArrayLambda;
import com.s8.io.bohr.neon.core.NeObject.ListLambda;
import com.s8.io.bohr.neon.core.NeObject.StringUTF8ArrayLambda;
import com.s8.io.bohr.neon.core.NeObject.StringUTF8Lambda;
import com.s8.io.bohr.neon.core.NeObject.VoidLambda;
import com.s8.io.bohr.neon.fields.NeFieldHandler;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeFieldHandler;
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
import com.s8.io.bohr.neon.methods.arrays.Int64ArrayNeMethodRunner;
import com.s8.io.bohr.neon.methods.arrays.StringUTF8ArrayNeMethodRunner;
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
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NeVertex {

	public final NeBranch branch;

	public final NeObject object;

	private boolean hasUnpublishedChanges = false;

	private boolean isCreateUnpublished = false;

	private boolean isExposeUnpublished = false;

	private boolean isUpdateUnpublished = false;


	private int slot;


	NeFieldValue[] values;

	NeFunc[] funcs;

	public final NeObjectTypeHandler prototype;


	/**
	 * index
	 */
	private String index;



	/**
	 * 
	 * @param branch
	 */
	public NeVertex(NeBranch branch, String typeName, NeObject object) {
		super();


		// branch
		this.branch = branch;
		this.prototype = branch.retrieveObjectPrototype(typeName);
		this.object = object;

		values = new NeFieldValue[4];
		funcs = new NeFunc[4];
	}

	/**
	 * 
	 * @return index
	 */
	public String getIndex() {

		if(index == null) {

			index = branch.appendObject(this);

			/* keep track of update required status */
			isCreateUnpublished = true;

			onChange();
		}

		return index;
	}



	public void expose(int slot) {

		isExposeUnpublished = true;
		
		this.slot = slot;

		/* general change notified */
		onChange();
	}




	private void onUpdate() {

		/* keep track of update required status */
		isUpdateUnpublished = true;

		/* general change notified */
		onChange();
	}


	private void onChange() {
		if(!hasUnpublishedChanges) {

			/* push toUnpublished */
			branch.outbound.notifyChanged(this);
			

			/* keep track of unchanged status */
			hasUnpublishedChanges = true;
		}
	}



	public void publish(ByteOutflow outflow) throws IOException {

		if(hasUnpublishedChanges) {
			String index = getIndex();

			/* publish prototype */
			prototype.publish_DECLARE_TYPE(outflow);

			if(isCreateUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

				/* publish type code */
				outflow.putUInt7x(prototype.code);

				/* publish index */
				outflow.putStringUTF8(index);

				prototype.publishFields(values, outflow);
				
				outflow.putUInt8(BOHR_Keywords.CLOSE_NODE);

				isCreateUnpublished = false;
			}	
			else if(isUpdateUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.UPDATE_NODE);

				/* publish index */
				outflow.putStringUTF8(index);

				/* fields */
				prototype.publishFields(values, outflow);
				
				outflow.putUInt8(BOHR_Keywords.CLOSE_NODE);

				isUpdateUnpublished = false;
			}

			if(isExposeUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.EXPOSE_NODE);

				/* publish index */
				outflow.putStringUTF8(index);

				/* fields */
				outflow.putUInt8(slot);

				isExposeUnpublished = false;
			}

			hasUnpublishedChanges = false;
		}
	}
	
	

	private NeFieldValue getEntry(NeFieldHandler field) {
		int ordinal= field.ordinal;
		NeFieldValue value;
		if(field.ordinal < values.length) {
			if((value = values[ordinal]) != null) {
				return value;
			}
			else {
				return (values[ordinal] = field.createValue());
			}
		}
		else {
			// increase values size
			int n = values.length, m = n >= 2 ? n : 2;
			while(m <= field.ordinal) { m*=2; }
			NeFieldValue[] extendedValues = new NeFieldValue[m];
			for(int i = 0; i < n; i++) { extendedValues[i] = values[i]; }
			values = extendedValues;
			
			return (values[ordinal] = field.createValue());
		}
	}



	/**
	 * 
	 * @param method
	 * @return
	 */
	private NeFunc getFunc(NeMethodRunner method) {
		int ordinal = method.ordinal;
		NeFunc func;
		if(ordinal < funcs.length) {
			if((func = funcs[ordinal]) != null) {
				return func;
			}
			else {
				return (funcs[ordinal] = method.createFunc());
			}
		}
		else {
			int n = values.length;
			NeFunc[] extendedFuncs = new NeFunc[2*n];
			for(int i = 0; i < n; i++) { extendedFuncs[i] = funcs[i]; }
			funcs = extendedFuncs;
			return (funcs[ordinal] = method.createFunc());
		}
	}

	

	public void forVoid(String name, VoidLambda lambda) {
		VoidNeMethodRunner method = prototype.getVoidMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setBool8(String name, boolean value) {
		Bool8NeFieldHandler field = prototype.getBool8Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBool8(String name) {
		Bool8NeFieldHandler field = prototype.getBool8Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forBool8(String name, NeObject.Bool8Lambda lambda) {
		Bool8NeMethodRunner method = prototype.getBooleanMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setBool8Array(String name, boolean[] value) {
		Bool8ArrayNeFieldHandler field = prototype.getBool8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean[] getBool8Array(String name) {
		Bool8ArrayNeFieldHandler field = prototype.getBool8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forBool8Array(String name, Bool8ArrayNeMethodRunner.Lambda lambda) {
		Bool8ArrayNeMethodRunner method = prototype.getBool8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt8(String name, int value) {
		UInt8NeFieldHandler field = prototype.getUInt8Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt8(String name) {
		UInt8NeFieldHandler field = prototype.getUInt8Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forUInt8(String name, NeObject.UInt8Lambda lambda) {
		UInt8NeMethodRunner method = prototype.getUInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt8Array(String name, int[] value) {
		UInt8ArrayNeFieldHandler field = prototype.getUInt8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int[] getUInt8Array(String name) {
		UInt8ArrayNeFieldHandler field = prototype.getUInt8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}
	

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt16(String name, int value) {
		UInt16NeFieldHandler field = prototype.getUInt16Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt16(String name) {
		UInt16NeFieldHandler field = prototype.getUInt16Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	public void forUInt16(String name, NeObject.UInt16Lambda lambda) {
		UInt16NeMethodRunner method = prototype.getUInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt16Array(String name, int[] value) {
		UInt16ArrayNeFieldHandler field = prototype.getUInt16ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int[] getUInt16Array(String name) {
		UInt16ArrayNeFieldHandler field = prototype.getUInt16ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt32(String name, long value) {
		UInt32NeFieldHandler field = prototype.getUInt32Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt32(String name) {
		UInt32NeFieldHandler field = prototype.getUInt32Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}

	public void forUInt32(String name, NeObject.UInt32Lambda lambda) {
		UInt32NeMethodRunner method = prototype.getUInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt32Array(String name, long[] value) {
		UInt32ArrayNeFieldHandler field = prototype.getUInt32ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long[] getUInt32Array(String name) {
		UInt32ArrayNeFieldHandler field = prototype.getUInt32ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt64(String name, long value) {
		UInt64NeFieldHandler field = prototype.getUInt64Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt64(String name) {
		UInt64NeFieldHandler field = prototype.getUInt64Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}

	public void forUInt64(String name, NeObject.UInt16Lambda lambda) {
		UInt64NeMethodRunner method = prototype.getUInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt8(String name, int value) {
		Int8NeFieldHandler field = prototype.getInt8Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt8(String name) {
		Int8NeFieldHandler field = prototype.getInt8Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}

	
	public void forInt8(String name, NeObject.Int8Lambda lambda) {
		Int8NeMethodRunner method = prototype.getInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt16(String name, int value) {
		Int16NeFieldHandler field = prototype.getInt16Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/*
	 * 
	 */
	public int getInt16(String name) {
		Int16NeFieldHandler field = prototype.getInt16Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}
	
	public void forInt16(String name, NeObject.Int16Lambda lambda) {
		Int16NeMethodRunner method = prototype.getInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}




	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt32(String name, int value) {
		Int32NeFieldHandler field = prototype.getInt32Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt32(String name) {
		Int32NeFieldHandler field = prototype.getInt32Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}

	public void forInt32(String name, NeObject.UInt32Lambda lambda) {
		Int32NeMethodRunner method = prototype.getInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64(String name, long value) {
		Int64NeFieldHandler field = prototype.getInt64Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getInt64(String name) {
		Int64NeFieldHandler field = prototype.getInt64Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forInt64(String name, NeObject.Int64Lambda lambda) {
		Int64NeMethodRunner method = prototype.getInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64Array(String name, long[] value) {
		Int64ArrayNeFieldHandler field = prototype.getInt64ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long[] getInt64Array(String name) {
		Int64ArrayNeFieldHandler field = prototype.getInt64ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void forInt64Array(String name, Int64ArrayLambda lambda) {
		Int64ArrayNeMethodRunner method = prototype.getInt64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32(String name, float value) {
		Float32NeFieldHandler field = prototype.getFloat32Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public float getFloat32(String name) {
		Float32NeFieldHandler field = prototype.getFloat32Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void forFloat32(String name, Float32Lambda lambda) {
		Float32NeMethodRunner method = prototype.getFloat32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32Array(String name, float[] value) {
		Float32ArrayNeFieldHandler field = prototype.getFloat32ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}



	/**
	 * 
	 * @param name
	 * @return
	 */
	public float[] getFloat32Array(String name) {
		Float32ArrayNeFieldHandler field = prototype.getFloat32ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forFloat32Array(String name, Float32ArrayLambda lambda) {
		Float32ArrayNeMethodRunner method = prototype.getFloat32ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64(String name, double value) {
		Float64NeFieldHandler field = prototype.getFloat64Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public double getFloat64(String name) {
		Float64NeFieldHandler field = prototype.getFloat64Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forFloat64(String name, Float64Lambda lambda) {
		Float64NeMethodRunner method = prototype.getFloat64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64Array(String name, double[] value) {
		Float64ArrayNeFieldHandler field = prototype.getFloat64ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public double[] getFloat64Array(String name) {
		Float64ArrayNeFieldHandler field = prototype.getFloat64ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forFloat64Array(String name, Float64ArrayLambda lambda) {
		Float64ArrayNeMethodRunner method = prototype.getFloat64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setStringUTF8(String name, String value) {
		StringUTF8NeFieldHandler field = prototype.getStringUTF8Field(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	public String getStringUTF8(String name) {
		StringUTF8NeFieldHandler field = prototype.getStringUTF8Field(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	public void forStringUTF8(String name, StringUTF8Lambda lambda) {
		StringUTF8NeMethodRunner method = prototype.getStringUTF8NeMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setStringUTF8Array(String name, String[] value) {
		StringUTF8ArrayNeFieldHandler field = prototype.getStringUTF8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public String[] getStringUTF8Array(String name) {
		StringUTF8ArrayNeFieldHandler field = prototype.getStringUTF8ArrayField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void forStringUTF8Array(String name, StringUTF8ArrayLambda lambda) {
		StringUTF8ArrayNeMethodRunner method = prototype.getStringUTF8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param value
	 */
	public <T extends NeObject> void setObj(String name, T value) {
		ObjNeFieldHandler<T> field = prototype.getObjField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param <T>
	 * @param name
	 * @return
	 */
	public <T extends NeObject> T getObj(String name) {
		ObjNeFieldHandler<T> field = prototype.getObjField(name);
		NeFieldValue entry = getEntry(field);
		return field.get(entry);
	}



	public <T extends NeVertex> void forObject(String name, ObjNeMethodRunner.Lambda<T> lambda) {
		ObjNeMethodRunner<T> method = prototype.getObjMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	public <T extends NeObject> void setObjList(String name, List<T> value) {
		ListNeFieldHandler<T> field = prototype.getObjArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @return a <b>COPY</b> of the underlying list
	 */
	public <T extends NeObject> List<T> getObjList(String name) {
		ListNeFieldHandler<T> field = prototype.getObjArrayField(name);
		NeFieldValue entry = getEntry(field);
		List<T> list = field.get(entry);
		List<T> copy = new ArrayList<T>(list.size());
		list.forEach(item -> copy.add(item));
		return copy;
	}

	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param obj
	 */
	public <T extends NeObject> void addObjToList(String name, T obj) {
		ListNeFieldHandler<T> field = prototype.getObjArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.add(entry, obj);
	}
	
	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param obj
	 */
	public <T extends NeObject> void removeObjFromList(String name, T obj) {
		if(obj != null) {
			ListNeFieldHandler<T> field = prototype.getObjArrayField(name);
			NeFieldValue entry = getEntry(field);
			field.remove(entry, obj.vertex.getIndex());
		}
	}

	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param lambda
	 */
	public <T extends NeObject> void forObjList(String name, ListLambda<T> lambda) {
		ListNeMethodRunner<T> method = prototype.getObjListMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}




}
