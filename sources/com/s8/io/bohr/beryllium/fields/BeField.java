package com.s8.io.bohr.beryllium.fields;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.beryllium.object.BeObject;
import com.s8.io.bohr.beryllium.object.BeSerialException;
import com.s8.io.bytes.alpha.Bool64;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract class BeField {

	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static abstract class Prototype {
		
		
		
		/**
		 * 
		 * @return
		 */
		public abstract String getTypeName();

	}
	
	


	/**
	 * The name of field. The only thing that is NEVER supposed to change
	 */
	public final String name;

	public final Field field;

	public final Bool64 props;
	
	


	public BeField(String name, long props, Field field) {
		super();
		this.name = name;
		this.props = new Bool64(props);
		this.field = field;
	}
	

	public abstract Prototype getPrototype();

	
	
	
	/**
	 * 
	 * @param map
	 * @param object
	 * @param inflow
	 * @param bindings
	 * @throws BkException
	 */
	public abstract void readValue(Object object, ByteInflow inflow) throws BeSerialException;

	/*
	 * public void write(S8Object object, ByteOutflow outflow) throws
	 * S8ObjectIOException {
	 * 
	 * }
	 */

	public abstract void writeValue(Object object, ByteOutflow outflow) throws BeSerialException;

	public abstract void computeFootprint(Object object, MemoryFootprint weight) throws BeSerialException;

	

	/**
	 * 
	 * @param clone
	 * @param bindings
	 * @throws GphSerialException
	 */
	public abstract void deepClone(Object origin, Object clone) throws BeSerialException;

	/**
	 * 
	 * @param base
	 * @param update
	 * @param outflow
	 * @throws GphSerialException
	 * @throws IOException
	 */
	public abstract boolean hasDiff(Object base, Object update) throws BeSerialException;

	
	
	
	public void print(BeObject object, Writer writer) throws IOException, S8ShellStructureException {
		writer.append("(");
		writer.append(getPrototype().getTypeName());
		writer.append(") ");
		writer.append(name);
		writer.append(": ");
		printValue(object, writer);
	}
	


	protected abstract void printValue(Object object, Writer writer) throws BeSerialException;
	

	
}
