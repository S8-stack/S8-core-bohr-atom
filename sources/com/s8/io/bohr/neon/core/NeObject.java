package com.s8.io.bohr.neon.core;

import java.util.List;

import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.neon.fields.NeField;
import com.s8.io.bohr.neon.fields.NeValue;
import com.s8.io.bohr.neon.fields.arrays.Bool8ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float32ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Float64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.Int64ArrayNeField;
import com.s8.io.bohr.neon.fields.arrays.StringUTF8ArrayNeField;
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


/**
 * 
 * @author pierreconvert
 *
 */
public abstract class NeObject {

	public final NeBranch branch;
	
	
	private String typeName;

	private NeValue[] values;
	
	private NeFunc[] funcs;
	
	private NeObjectPrototype prototype;
	
	
	/**
	 * index
	 */
	private S8Index index;
	
	

	/**
	 * 
	 * @param branch
	 */
	public NeObject(NeBranch branch, String typeName) {
		super();


		// branch
		this.branch = branch;
		this.typeName = typeName;

		values = new NeValue[4];
	}

	/**
	 * 
	 * @return index
	 */
	public S8Index getIndex() {
		return index!=null ? index : (index = branch.appendObject(this));
	}

	
	
	/**
	 * 
	 * @return
	 */
	private NeObjectPrototype _prototype() {
		return prototype != null ? prototype : (prototype = branch.getObjectPrototype(typeName));
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
		Bool8NeField field = _prototype().getBool8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBool8(String name) {
		Bool8NeField field = _prototype().getBool8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	public void forBool8(String name, Bool8NeMethod.Lambda lambda) {
		Bool8NeMethod method = _prototype().getBool8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setBool8Array(String name, boolean[] value) {
		Bool8ArrayNeField field = _prototype().getBool8ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public boolean[] getBool8Array(String name) {
		Bool8ArrayNeField field = _prototype().getBool8ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	public void forBool8Array(String name, Bool8ArrayNeMethod.Lambda lambda) {
		Bool8ArrayNeMethod method = _prototype().getBool8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt8(String name, int value) {
		UInt8NeField field = _prototype().getUInt8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt8(String name) {
		UInt8NeField field = _prototype().getUInt8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	


	public void forUInt8(String name, UInt8NeMethod.Lambda lambda) {
		UInt8NeMethod method = _prototype().getUInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt16(String name, int value) {
		UInt16NeField field = _prototype().getUInt16Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getUInt16(String name) {
		UInt16NeField field = _prototype().getUInt16Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forUInt16(String name, UInt16NeMethod.Lambda lambda) {
		UInt16NeMethod method = _prototype().getUInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt32(String name, long value) {
		UInt32NeField field = _prototype().getUInt32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt32(String name) {
		UInt32NeField field = _prototype().getUInt32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	
	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forUInt32(String name, UInt32NeMethod.Lambda lambda) {
		UInt32NeMethod method = _prototype().getUInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setUInt64(String name, long value) {
		UInt64NeField field = _prototype().getUInt64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getUInt64(String name) {
		UInt64NeField field = _prototype().getUInt64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forUInt64(String name, UInt64NeMethod.Lambda lambda) {
		UInt64NeMethod method = _prototype().getUInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt8(String name, int value) {
		Int8NeField field = _prototype().getInt8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt8(String name) {
		Int8NeField field = _prototype().getInt8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	

	public void forInt8(String name, Int8NeMethod.Lambda lambda) {
		Int8NeMethod method = _prototype().getInt8Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt16(String name, int value) {
		Int16NeField field = _prototype().getInt16Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/*
	 * 
	 */
	public int getInt16(String name) {
		Int16NeField field = _prototype().getInt16Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	public void forInt16(String name, Int16NeMethod.Lambda lambda) {
		Int16NeMethod method = _prototype().getInt16Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt32(String name, int value) {
		Int32NeField field = _prototype().getInt32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public int getInt32(String name) {
		Int32NeField field = _prototype().getInt32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	/**
	 * 
	 * @param name
	 * @param lambda
	 */
	public void forInt32(String name, Int32NeMethod.Lambda lambda) {
		Int32NeMethod method = _prototype().getInt32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64(String name, long value) {
		Int64NeField field = _prototype().getInt64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public long getInt64(String name) {
		Int64NeField field = _prototype().getInt64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	public void forInt64(String name, Int64NeMethod.Lambda lambda) {
		Int64NeMethod method = _prototype().getInt64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setInt64Array(String name, long[] value) {
		Int64ArrayNeField field = _prototype().getInt64ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public long[] getInt64Array(String name) {
		Int64ArrayNeField field = _prototype().getInt64ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	public void forInt64Array(String name, Int64ArrayNeMethod.Lambda lambda) {
		Int64ArrayNeMethod method = _prototype().getInt64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32(String name, float value) {
		Float32NeField field = _prototype().getFloat32Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public float getFloat32(String name) {
		Float32NeField field = _prototype().getFloat32Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	public void forFloat32(String name, Float32NeMethod.Lambda lambda) {
		Float32NeMethod method = _prototype().getFloat32Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat32Array(String name, float[] value) {
		Float32ArrayNeField field = _prototype().getFloat32ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public float[] getFloat32Array(String name) {
		Float32ArrayNeField field = _prototype().getFloat32ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}


	public void forFloat32Array(String name, Float32ArrayNeMethod.Lambda lambda) {
		Float32ArrayNeMethod method = _prototype().getFloat32ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64(String name, double value) {
		Float64NeField field = _prototype().getFloat64Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public double getFloat64(String name) {
		Float64NeField field = _prototype().getFloat64Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forFloat64(String name, Float64NeMethod.Lambda lambda) {
		Float64NeMethod method = _prototype().getFloat64Method(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setFloat64Array(String name, double[] value) {
		Float64ArrayNeField field = _prototype().getFloat64ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	/**
	 * 
	 * @param name
	 * @return
	 */
	public double[] getFloat64Array(String name) {
		Float64ArrayNeField field = _prototype().getFloat64ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}



	/**
	 * 
	 * @param name
	 * @param func
	 */
	public void forFloat64Array(String name, Float64ArrayNeMethod.Lambda lambda) {
		Float64ArrayNeMethod method = _prototype().getFloat64ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}

	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setStringUTF8(String name, String value) {
		StringUTF8NeField field = _prototype().getStringUTF8Field(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	public String getStringUTF8(String name) {
		StringUTF8NeField field = _prototype().getStringUTF8Field(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	
	public void forStringUTF8(String name, StringUTF8NeMethod.Lambda lambda) {
		StringUTF8NeMethod method = _prototype().getStringUTF8NeMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	

	public void setStringUTF8Array(String name, String[] value) {
		StringUTF8ArrayNeField field = _prototype().getStringUTF8ArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	public String[] getStringUTF8Array(String name) {
		StringUTF8ArrayNeField field = _prototype().getStringUTF8ArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	
	public void forStringUTF8Array(String name, StringUTF8ArrayNeMethod.Lambda lambda) {
		StringUTF8ArrayNeMethod method = _prototype().getStringUTF8ArrayMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	
	

	public <T extends NeObject> void setObj(String name, T value) {
		NeObj<T> field = _prototype().getObjField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	public <T extends NeObject> T getObj(String name) {
		NeObj<T> field = _prototype().getObjField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	

	
	public <T extends NeObject>  void forObj(String name, ObjNeMethod.Lambda<T> lambda) {
		ObjNeMethod<T> method = _prototype().getObjMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	
	

	public <T extends NeObject> void setObjAray(String name, List<T> value) {
		NeList<T> field = _prototype().getObjArrayField(name);
		NeValue entry = getEntry(field);
		field.set(entry, value);
	}
	

	public <T extends NeObject> List<T> getObjArray(String name) {
		NeList<T> field = _prototype().getObjArrayField(name);
		NeValue entry = getEntry(field);
		return field.get(entry);
	}
	
	
	public <T extends NeObject> void forObjList(String name, ListNeMethod.Lambda<T> lambda) {
		ListNeMethod<T> method = _prototype().getObjListMethod(name);
		NeFunc func = getFunc(method);
		func.lambda = lambda;
	}
	

}
