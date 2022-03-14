package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.List;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.neon.fields.NeField;
import com.s8.io.bohr.neon.fields.NeValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt16ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.UInt32ArrayNeField;
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
import com.s8.io.bohr.neon.methods.NeFunc;
import com.s8.io.bohr.neon.methods.NeMethod;
import com.s8.io.bohr.neon.methods.arrays.Bool8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Float32ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Float64ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.Int64ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.StringUTF8ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt16ArrayNeMethod;
import com.s8.io.bohr.neon.methods.arrays.UInt32ArrayNeMethod;
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
public abstract class NeObject {

	public final NeBranch branch;


	private boolean hasUnpublishedChanges;

	private boolean isCreateUnpublished;

	private boolean isExposeUnpublished;

	private boolean isUpdateUnpublished;


	private int slot;


	NeValue[] values;

	private NeFunc[] funcs;

	public final NeObjectPrototype prototype;


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
		this.prototype = branch.getObjectPrototype(typeName);

		values = new NeValue[4];


		hasUnpublishedChanges = true;
		isCreateUnpublished = true;
	}

	/**
	 * 
	 * @return index
	 */
	public String _index() {

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

		/* keep track of unchanged status */
		hasUnpublishedChanges = true;

		/* keep track of update required status */
		isUpdateUnpublished = true;

		/* general change notified */
		onChange();
	}


	private void onChange() {
		if(!hasUnpublishedChanges) {

			/* push toUnpublished */
			branch.outbound.notifyChanged(this);
			
			/* has unpublished changes */
			hasUnpublishedChanges = true;
		}
	}



	public void publish(ByteOutflow outflow) throws IOException {

		if(hasUnpublishedChanges) {
			String index = _index();

			/* publish prototype */
			prototype.declare(outflow);

			if(isCreateUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

				/* publish type code */
				outflow.putUInt7x(prototype.code);

				/* publish index */
				outflow.putStringUTF8(index);

				prototype.publishFields(values, outflow);			

				isCreateUnpublished = false;
			}	
			else if(isUpdateUnpublished) {

				// declare type
				outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

				/* publish index */
				outflow.putStringUTF8(index);

				/* fields */
				prototype.publishFields(values, outflow);

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
	
	

	private NeValue getEntry(NeField field) {
		int ordinal= field.ordinal;
		NeValue value;
		if(field.ordinal < values.length) {
			if((value = values[ordinal]) != null) {
				return value;
			}
			else {
				return (values[ordinal] = field.createValue());
			}
		}
		else {
			int n = values.length;
			NeValue[] extendedValues = new NeValue[2*n];
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



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setBool8(String name, boolean value) {
		Bool8NeField field = prototype.getBool8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBool8(String name) {
		Bool8NeField field = prototype.getBool8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onBool8(String name, Bool8NeMethod.Lambda lambda) {
		Bool8NeMethod method = prototype.getBool8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setBool8Array(String name, boolean[] value) {
		Bool8ArrayNeField field = prototype.getBool8ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean[] getBool8Array(String name) {
		Bool8ArrayNeField field = prototype.getBool8ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onBool8Array(String name, Bool8ArrayNeMethod.Lambda lambda) {
		Bool8ArrayNeMethod method = prototype.getBool8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt8(String name, int value) {
		UInt8NeField field = prototype.getUInt8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt8(String name) {
		UInt8NeField field = prototype.getUInt8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	public void onUInt8(String name, UInt8NeMethod.Lambda lambda) {
		UInt8NeMethod method = prototype.getUInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt16(String name, int value) {
		UInt16NeField field = prototype.getUInt16Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt16(String name) {
		UInt16NeField field = prototype.getUInt16Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onUInt16(String name, UInt16NeMethod.Lambda lambda) {
		UInt16NeMethod method = prototype.getUInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt16Array(String name, int[] value) {
		UInt16ArrayNeField field = prototype.getUInt16ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int[] getUInt16Array(String name) {
		UInt16ArrayNeField field = prototype.getUInt16ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onUInt16Array(String name, UInt16ArrayNeMethod.Lambda lambda) {
		UInt16ArrayNeMethod method = prototype.getUInt16ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt32(String name, long value) {
		UInt32NeField field = prototype.getUInt32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt32(String name) {
		UInt32NeField field = prototype.getUInt32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void onUInt32(String name, UInt32NeMethod.Lambda lambda) {
		UInt32NeMethod method = prototype.getUInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt32Array(String name, long[] value) {
		UInt32ArrayNeField field = prototype.getUInt32ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long[] getUInt32Array(String name) {
		UInt32ArrayNeField field = prototype.getUInt32ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void onUInt32Array(String name, UInt32NeMethod.Lambda lambda) {
		UInt32ArrayNeMethod method = prototype.getUInt32ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt64(String name, long value) {
		UInt64NeField field = prototype.getUInt64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt64(String name) {
		UInt64NeField field = prototype.getUInt64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void onUInt64(String name, UInt64NeMethod.Lambda lambda) {
		UInt64NeMethod method = prototype.getUInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt8(String name, int value) {
		Int8NeField field = prototype.getInt8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt8(String name) {
		Int8NeField field = prototype.getInt8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	public void onInt8(String name, Int8NeMethod.Lambda lambda) {
		Int8NeMethod method = prototype.getInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt16(String name, int value) {
		Int16NeField field = prototype.getInt16Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/*
	 * 
	 */
	public int getInt16(String name) {
		Int16NeField field = prototype.getInt16Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onInt16(String name, Int16NeMethod.Lambda lambda) {
		Int16NeMethod method = prototype.getInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt32(String name, int value) {
		Int32NeField field = prototype.getInt32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt32(String name) {
		Int32NeField field = prototype.getInt32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void onInt32(String name, Int32NeMethod.Lambda lambda) {
		Int32NeMethod method = prototype.getInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64(String name, long value) {
		Int64NeField field = prototype.getInt64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getInt64(String name) {
		Int64NeField field = prototype.getInt64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onInt64(String name, Int64NeMethod.Lambda lambda) {
		Int64NeMethod method = prototype.getInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64Array(String name, long[] value) {
		Int64ArrayNeField field = prototype.getInt64ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public long[] getInt64Array(String name) {
		Int64ArrayNeField field = prototype.getInt64ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onInt64Array(String name, Int64ArrayNeMethod.Lambda lambda) {
		Int64ArrayNeMethod method = prototype.getInt64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32(String name, float value) {
		Float32NeField field = prototype.getFloat32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public float getFloat32(String name) {
		Float32NeField field = prototype.getFloat32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onFloat32(String name, Float32NeMethod.Lambda lambda) {
		Float32NeMethod method = prototype.getFloat32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32Array(String name, float[] value) {
		Float32ArrayNeField field = prototype.getFloat32ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}



	/**
	 * 
	 * @param name
	 * @return
	 */
	public float[] getFloat32Array(String name) {
		Float32ArrayNeField field = prototype.getFloat32ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void onFloat32Array(String name, Float32ArrayNeMethod.Lambda lambda) {
		Float32ArrayNeMethod method = prototype.getFloat32ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64(String name, double value) {
		Float64NeField field = prototype.getFloat64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public double getFloat64(String name) {
		Float64NeField field = prototype.getFloat64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void onFloat64(String name, Float64NeMethod.Lambda lambda) {
		Float64NeMethod method = prototype.getFloat64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64Array(String name, double[] value) {
		Float64ArrayNeField field = prototype.getFloat64ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	/**
	 * 
	 * @param name
	 * @return
	 */
	public double[] getFloat64Array(String name) {
		Float64ArrayNeField field = prototype.getFloat64ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void onFloat64Array(String name, Float64ArrayNeMethod.Lambda lambda) {
		Float64ArrayNeMethod method = prototype.getFloat64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setStringUTF8(String name, String value) {
		StringUTF8NeField field = prototype.getStringUTF8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	public String getStringUTF8(String name) {
		StringUTF8NeField field = prototype.getStringUTF8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	public void onStringUTF8(String name, StringUTF8NeMethod.Lambda lambda) {
		StringUTF8NeMethod method = prototype.getStringUTF8NeMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}


	public void setStringUTF8Array(String name, String[] value) {
		StringUTF8ArrayNeField field = prototype.getStringUTF8ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	public String[] getStringUTF8Array(String name) {
		StringUTF8ArrayNeField field = prototype.getStringUTF8ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	public void onStringUTF8Array(String name, StringUTF8ArrayNeMethod.Lambda lambda) {
		StringUTF8ArrayNeMethod method = prototype.getStringUTF8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}




	public <T extends NeObject> void setObj(String name, T value) {
		NeObj<T> field = prototype.getObjField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	public <T extends NeObject> T getObj(String name) {
		NeObj<T> field = prototype.getObjField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	public <T extends NeObject>  void onObj(String name, ObjNeMethod.Lambda<T> lambda) {
		ObjNeMethod<T> method = prototype.getObjMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}



	public <T extends NeObject> void setObjList(String name, List<T> value) {
		NeList<T> field = prototype.getObjArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
		onUpdate();
	}


	public <T extends NeObject> List<T> getObjList(String name) {
		NeList<T> field = prototype.getObjArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public <T extends NeObject> void onObjList(String name, ListNeMethod.Lambda<T> lambda) {
		ListNeMethod<T> method = prototype.getObjListMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}




}
