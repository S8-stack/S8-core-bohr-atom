package com.s8.io.bohr.neodymium.fields;

import java.io.IOException;
import java.io.Writer;
import java.util.Queue;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.handlers.NdHandler;
import com.s8.io.bohr.neodymium.properties.NdFieldProperties;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.GraphCrawler;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.MemoryFootprint;

/**
 * 
 * @author pierreconvert
 *
 */
public abstract class NdField {

	public final static String DEFAULT_FLOW_TAG = "(default)";

	
	public final int ordinal;
	
	/**
	 * 
	 */
	public final NdHandler handler;


	/**
	 * 
	 */
	public final String name;

	/**
	 * 
	 */
	public final String flow;

	/**
	 * 
	 */
	public final long mask;

	/**
	 * 
	 */
	public final long flags;
	
	/**
	 * 
	 * @param properties
	 * @param handler
	 */
	public NdField(int ordinal, NdFieldProperties properties, NdHandler handler) {
		super();
		
		this.ordinal = ordinal;
		
		/* <field-properties> */
		this.name = properties.getName();
		this.flow = properties.getFlow();
		this.mask = properties.getMask();
		this.flags = properties.getFlags();
		/* </field-properties> */
		
		/* <handler> */
		this.handler = handler;
		/* </handler> */
	}


	
	
	protected NdHandler getHandler() {
		return handler;
	}

	
	
	
	/**
	 * 
	 * @param flow (null = use flow define in field)
	 * @return
	 * @throws  
	 */
	public abstract NdFieldComposer createComposer(int code) throws NdIOException;
	
	
	public abstract NdFieldParser createParser(ByteInflow inflow) throws IOException;
	
	


	public abstract void computeFootprint(S8Object object, MemoryFootprint weight) throws NdIOException;

	/**
	 * Collect all INTERNAL (belonging to this block) object not already in the
	 * state passed as argument, and switch their state to the state passed as
	 * argument
	 * 
	 * @param object
	 * @param state
	 * @param referenced
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public abstract void sweep(S8Object object, GraphCrawler crawler) throws NdIOException;

	/**
	 * Collect all external blocks with flag not already set to true
	 * 
	 * @param object
	 */
	public abstract void collectReferencedBlocks(S8Object object, Queue<String> references);

	

	/**
	 * Print field for debugging purposes
	 * 
	 * @param indent
	 */
	public abstract void DEBUG_print(String indent);

	/**
	 * 
	 * @param clone
	 * @param bindings
	 * @throws LthSerialException
	 */
	public abstract void deepClone(S8Object origin, S8Object clone, BuildScope scope) throws NdIOException;

	/**
	 * 
	 * @param base
	 * @param update
	 * @param outflow
	 * @throws LthSerialException
	 * @throws IOException
	 */
	public abstract boolean hasDiff(S8Object base, S8Object update) throws NdIOException;


	/**
	 * <p>
	 * <b>IMPORTANT NOTICE<b>: It is often a good idea to track change AND remap at
	 * the same time, that's why we take advantage of the scope
	 * </p>
	 * 
	 * @param object
	 * @param scope
	 * @return
	 * @throws IOException
	 */
	public abstract NdFieldDelta produceDiff(S8Object object) throws NdIOException;

	

	public void print(S8Object object, Writer writer) throws IOException, S8ShellStructureException {
		writer.append("(");
		writer.append(printType());
		writer.append(") ");
		writer.append(name);
		writer.append(": ");
		printValue(object, writer);
	}
	

	/**
	 * print standard name of field type
	 */
	public abstract String printType();


	protected abstract void printValue(S8Object object, Writer writer) throws NdIOException, 
	IOException, S8ShellStructureException;

	

	/**
	 * 
	 * @param object
	 * @return true is the object has been resolved, false otherwise
	 * @throws LthSerialException 
	 */
	public abstract boolean isValueResolved(S8Object object) throws NdIOException;

	
}
