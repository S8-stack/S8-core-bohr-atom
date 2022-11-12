package com.s8.io.bohr.neodymium.type;


import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdType {


	/**
	 * 
	 */
	protected final Class<?> baseType;



	/**
	 * 
	 */
	Constructor<?> constructor;





	/**
	 * <p><b>/!\ WARNING </b></p>
	 * code for serialization / deserialization
	 */
	String name;




	/**
	 * fields
	 */
	public Map<String, NdField> fieldsByName;

	
	/**
	 * fields by ordinal number
	 */
	public NdField[] fields;




	private final DebugModule debugModule;



	/**
	 * The number of references to be stored in vertex
	 */
	int nVertexReferences;


	public NdType(Class<?> baseType) {
		super();
		this.baseType = baseType;

		nVertexReferences = 0;

		// modules
		debugModule = new DebugModule(this);
	}
	
	
	
	
	public int getNumberOfFields() {
		return fields.length;
	}
	



	public Class<?> getBaseType(){
		return baseType;
	}

	public String getSerialName() {
		return name;
	}

	public String getRuntimeName() {
		return baseType.getName();
	}



	/**
	 * 
	 * @param object
	 * @param footprint
	 */
	public void computeFootprint(NdObject object, MemoryFootprint footprint) {
		footprint.reportInstance();
		fieldsByName.forEach((name, handler) -> {  
			try {
				footprint.reportEntry();

				handler.computeFootprint(object, footprint);
			} catch (NdIOException e) {
				e.printStackTrace();
			} 
		});
	}



	/**
	 * 
	 * @return
	 * @throws LthSerialException
	 */
	public NdObject createNewInstance() throws NdIOException {
		try {
			return (NdObject) constructor.newInstance(new Object[]{});
		}
		catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new NdIOException("instance creation failed due to constructor call error", baseType, e);
		}
	}

	public final static byte[] CODEBASE_ENTRY = "<e/>".getBytes(StandardCharsets.US_ASCII);




	/**
	 * Collect all INTERNAL (belonging to this block) object whose state is matching the 
	 * state passed as argument, and switch their state to the state passed as
	 * argument.
	 * 
	 * @param object
	 * @param flagState
	 * @param queue
	 * @throws IOException 
	 * @throws S8ShellStructureException 
	 */
	public void sweep(NdObject object, GraphCrawler crawler) throws NdIOException {
		int nFields = fields.length;
		for(int i = 0; i<nFields; i++) { fields[i].sweep(object, crawler); }
	}




	/**
	 * Field accessor
	 * 
	 * @param name
	 * @return
	 */
	public NdField getField(String name) {
		return fieldsByName.get(name);
	}


	/**
	 * Collect all EXTERNAL blocks effectively (meaning ref is resolved) referenced by field of this objects
	 * @param object
	 * @param references
	 */
	public void collectReferencedBlocks(NdObject object, Queue<String> references) {
		for(NdField entryHandler : fieldsByName.values()) {
			entryHandler.collectReferencedBlocks(object, references);	
		}
	}


	/**
	 * 
	 * @param origin
	 * @param context
	 * @return
	 * @throws LthSerialException
	 */
	public NdObject deepClone(NdObject origin, BuildScope scope) throws NdIOException {
		NdObject clone = createNewInstance();
		for(NdField field : fields) {
			field.deepClone(origin, clone, scope);
		}
		return clone;
	}






	/**
	 * 
	 * @param indent
	 */
	public void DEBUG_print(String indent) {
		System.out.println(indent+"S8Object Type, name = "+getSerialName());
		System.out.println(indent+" fields:{");
		fieldsByName.forEach((name, field) -> { field.DEBUG_print(indent+"   "); });
		System.out.println(indent+" }\n");
	}

	/**
	 * 
	 * @param object
	 * @param inflow
	 * @param context
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void print(NdObject object, Writer writer) throws IOException, S8ShellStructureException {
		debugModule.print(object, writer);
	}


	/**
	 * 
	 * @param value
	 * @param inflow
	 * @param context
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void deepCompare(NdObject left, NdObject right, Writer writer) throws IOException, S8ShellStructureException {
		debugModule.deepCompare(left, right, writer);
	}
	
	
	public boolean equals(NdType right) {
		return getSerialName().equals(right.getSerialName());
	}
	
	public int getNumberOfVertexReferences() {
		return nVertexReferences;
	}
	
	

	/**
	 * 
	 * @param object
	 * @param inflow
	 * @param scope
	 * @throws IOException
	 */
	public void consumeDiff(NdObject object, List<NdFieldDelta> deltas, BuildScope scope) throws NdIOException {
		for(NdFieldDelta delta : deltas) {
			delta.consume(object, scope);
		}
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param index
	 * @param object
	 * @param scope
	 * @return
	 * @throws IOException
	 */
	public List<NdFieldDelta> produceCreateDeltas(NdObject object) throws IOException {
		int n = fields.length;
		List<NdFieldDelta> deltas = new ArrayList<NdFieldDelta>(n);
		for(int i=0; i<n; i++) {
			deltas.add(fields[i].produceDiff(object));
		}
		return deltas;
	}

}
