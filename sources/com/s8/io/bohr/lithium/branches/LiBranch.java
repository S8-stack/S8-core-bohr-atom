package com.s8.io.bohr.lithium.branches;

import static com.s8.io.bohr.atom.BOHR_Keywords.FRAME_FOOTER;
import static com.s8.io.bohr.atom.BOHR_Keywords.FRAME_HEADER;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.lithium.codebase.LiCodebase;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.base64.Base64Generator;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class LiBranch {
	
	

	/**
	 * 
	 */
	public final static int EXPOSURE_RANGE = 8;


	
	public final String address;
	
	public final String id;

	public final LiCodebase codebase;
	
	long highestIndex;


	/**
	 * The interior mapping
	 */
	public final Map<String, LiVertex> vertices;
	
	
	

	
	/**
	 * inbound
	 */
	public final LiInbound inbound;
	
	
	/**
	 * outbound
	 */
	public final LiOutbound outbound;
	
	
	
	public final LiVertex[] exposure;
	
	
	
	/**
	 * Stateful var
	 */
	long version;
	
	
	
	String comment;
	
	
	long timestamp;
	
	
	private final Base64Generator idxGen;
	
	private final DebugModule debugModule;
	

	/**
	 * 
	 * @param branchId
	 * @param graph
	 * @param deltas
	 */
	public LiBranch(String address, String id, LiCodebase codebase) {
		super();
		this.address = address;
		this.id = id;
		
		this.codebase = codebase;
		
		// exposure
		exposure = new LiVertex[EXPOSURE_RANGE];
		
		vertices = new HashMap<String, LiVertex>();
		
		inbound = new LiInbound(this);
		outbound = new LiOutbound(this);
		
		debugModule = new DebugModule(this);
		
		idxGen = new Base64Generator(id);
	}
	

	public LiS8Object retrieveObject(String index) {
		return vertices.get(index).object;
	}

	
	/**
	 * 
	 * @return
	 */
	public String createNewIndex() {
		return idxGen.generate(++highestIndex);
	}
	
	
	public BuildScope createBuildScope() {
		return new BuildScope() {
			@Override
			public LiS8Object retrieveObject(String index) {
				return vertices.get(index).object;
			}
		};
	}
	
	
	
	
	
	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void pull(ByteInflow inflow) throws IOException {
		// check opening
		if(!inflow.matches(FRAME_HEADER)) { throw new IOException("DO NOT MATCH HEADER"); }
		inbound.parse(inflow);
		if(!inflow.matches(FRAME_FOOTER)) { throw new IOException("DO NOT MATCH FOOTER"); }
	}
	
	
	/**
	 * 
	 * @param outflow
	 * @throws S8BuildException
	 * @throws S8Exception
	 * @throws IOException
	 */
	public void push(ByteOutflow outflow) throws S8BuildException, S8Exception, IOException {
		outflow.putByteArray(FRAME_HEADER);
		outbound.compose(outflow);
		outflow.putByteArray(FRAME_FOOTER);
	}


	/**
	 * 
	 * @param object
	 * @return
	 */
	public LiVertex append(LiS8Object object) {
		
		
		/* retrieve object index */
		String index = object.S8_index;
		
		/* if index is null, assigned a newly generated one */
		if(index == null) {
			index = createNewIndex();
			object.S8_index = index;
		}
		
		
		/* retrieve object vertex */
		LiVertex vertex = (LiVertex) object.S8_vertex;
		
		if(vertex == null) {
			
			/* create vertex */
			vertex = new LiVertex(this, object);
			
			/* assign newly created vertex */
			object.S8_vertex = vertex;
			
			/* newly created vertex, so report activity */
			outbound.reportActivity(vertex);
			
			/* register vertex */
			vertices.put(index, vertex);
		}
		
		return vertex;
	}


	

	/**
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void print(Writer writer) throws IOException {
		debugModule.print(writer);
	}

	
	/**
	 * 
	 * @param writer
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void deepCompare(LiBranch deviated, Writer writer) throws IOException, S8ShellStructureException {
		debugModule.deepCompare(deviated, writer);
		
	}


}
