package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.BOHR_Keywords;
import com.s8.io.bohr.neon.fields.NeFieldHandler;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeFieldHandler;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeFieldHandler;
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
import com.s8.io.bohr.neon.methods.NeFunc;
import com.s8.io.bohr.neon.methods.NeMethod;
import com.s8.io.bohr.neon.methods.arrays.BooleanArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.DoubleArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.FloatArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.LongArrayNeMethod;
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
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public abstract class NeObject {

	public final NeBranch branch;


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
	public NeObject(NeBranch branch, String typeName) {
		super();


		// branch
		this.branch = branch;
		this.prototype = branch.retrieveObjectPrototype(typeName);

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
				outflow.putUInt7x(prototype.outboundCode);

				/* publish index */
				outflow.putStringUTF8(index);

				prototype.publishFields(values, outflow);
				
				outflow.putUInt8(BOHR_Keywords.CLOSE_NODE);

				isCreateUnpublished = false;
			}	
			else if(isUpdateUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

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
			int n = values.length, m = n;
			while(n <= field.ordinal) { m*=2; }
			NeFieldValue[] extendedValues = new NeFieldValue[m];
			for(int i = 0; i < n; i++) { extendedValues[i] = values[i]; }
			values = extendedValues;
			return (values[ordinal] = field.createValue());
		}
	}



	private NeFunc getFunc(NeMethod method) {
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

	

	public void onVoid(String name, VoidNeMethod.Lambda lambda) {
		VoidNeMethod method = prototype.getVoidMethod(name);
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


	public void onBoolean(String name, BooleanNeMethod.Lambda lambda) {
		BooleanNeMethod method = prototype.getBooleanMethod(name);
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


	public void onBooleanArray(String name, BooleanArrayNeMethod.Lambda lambda) {
		BooleanArrayNeMethod method = prototype.getBool8ArrayMethod(name);
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


	public void onShort(String name, ShortNeMethod.Lambda lambda) {
		ShortNeMethod method = prototype.getShortMethod(name);
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


	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void onInteger(String name, IntegerNeMethod.Lambda lambda) {
		IntegerNeMethod method = prototype.getIntegerMethod(name);
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


	public void onLong(String name, LongNeMethod.Lambda lambda) {
		LongNeMethod method = prototype.getLongMethod(name);
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


	public void onLongArray(String name, LongArrayNeMethod.Lambda lambda) {
		LongArrayNeMethod method = prototype.getLongArrayMethod(name);
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


	public void onFloat(String name, FloatNeMethod.Lambda lambda) {
		FloatNeMethod method = prototype.getFloatMethod(name);
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


	public void onFloatArray(String name, FloatArrayNeMethod.Lambda lambda) {
		FloatArrayNeMethod method = prototype.getFloatArrayMethod(name);
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
	public void onDouble(String name, DoubleNeMethod.Lambda lambda) {
		DoubleNeMethod method = prototype.getDoubleMethod(name);
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
	public void onDoubleArray(String name, DoubleArrayNeMethod.Lambda lambda) {
		DoubleArrayNeMethod method = prototype.getDoubleArrayMethod(name);
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



	public void onString(String name, StringNeMethod.Lambda lambda) {
		StringNeMethod method = prototype.getStringUTF8NeMethod(name);
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
	public void onStringArray(String name, StringArrayNeMethod.Lambda lambda) {
		StringArrayNeMethod method = prototype.getStringArrayMethod(name);
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



	public <T extends NeObject> void onObject(String name, ObjNeMethod.Lambda<T> lambda) {
		ObjNeMethod<T> method = prototype.getObjMethod(name);
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
		ListNeFieldHandler<T> field = prototype.getObjArrayField(name);
		NeFieldValue entry = getEntry(field);
		field.remove(entry, obj.getIndex());		
	}

	
	/**
	 * 
	 * @param <T>
	 * @param name
	 * @param lambda
	 */
	public <T extends NeObject> void onObjList(String name, ListNeMethod.Lambda<T> lambda) {
		ListNeMethod<T> method = prototype.getObjListMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}




}
