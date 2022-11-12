package com.s8.io.bohr.neodymium.branch;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.codebase.NdCodebase;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.object.NdVertex;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bohr.neodymium.type.GraphCrawler;
import com.s8.io.bohr.neodymium.type.NdType;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.base64.Base64Generator;




/**
 * <ul>
 * <li>PULL: Retrieve the branch deltas from inflow. No objects is constructed at this point </li>
 * <li>ROLL: move construction up to a specific version.</li>
 * <li>CLONE: Create a new Object by deep cloning current state (this clone is not connected to vertices)</li>
 * <li>COMMIT: (ONLY possible upon rolled to HEAD) create new delta from HEAD to the objects passed as argument</li>
 * <li>PUSH: release delta to outbound.</li>
 * Build head by playing all delta -> At this point </li>
 * <li>
 * 
 * @author pierreconvert
 *
 *
 *
 * <ul>
 * <li><b>PULL</b>: Read from I/O, .</li>
 * <li><b>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdBranch {
	
	

	/**
	 * 
	 */
	public final static int EXPOSURE_RANGE = 8;



	/**
	 * like hyuanday.cn.com/main-ref/orc/project0273
	 */
	public final String address;
	
	
	/**
	 * branch id (like 'master', 'main', 'dev', 'bug-fix-ticket08909808')
	 */
	public final String id;

	/**
	 * codebase
	 */
	public final NdCodebase codebase;


	/**
	 * The interior mapping
	 */
	public final Map<String, NdVertex> vertices;


	/**
	 * highest index
	 */
	long highestIndex;



	/**
	 * 
	 */
	public final Deque<NdBranchDelta> deltas;


	/**
	 * inbound
	 */
	public final NdInbound inbound;


	/**
	 * outbound
	 */
	public final NdOutbound outbound;


	private NdVertex[] exposure;


	private final CloneModule cloneModule;

	private final CommitModule commitModule;

	private final DebugModule debugModule;
	
	
	private final Base64Generator idxGen;


	/**
	 * current version
	 */
	long version;

	/**
	 * 
	 * @param branchId
	 * @param graph
	 * @param deltas
	 * @throws IOException 
	 */
	public NdBranch(NdCodebase codebase, String address, String id, long version) {
		super();
		this.address = address;
		this.id = id;
		this.codebase = codebase;
		this.version = version;


		vertices = new HashMap<String, NdVertex>();

		deltas  = new LinkedList<NdBranchDelta>();


		inbound = new NdInbound(this);
		outbound = new NdOutbound(this);


		/* <modules> */
		cloneModule = new CloneModule(this);
		commitModule = new CommitModule(this);
		debugModule = new DebugModule(this);
		/* </modules> */
		
		
		/**
		 * 
		 */
		exposure = new NdVertex[0];
		
		idxGen = new Base64Generator(id+':');
	}
	
	
	



	/**
	 * 
	 * @param objects
	 * @throws  
	 * @throws IOException 
	 */
	public void update(long version, NdObject[] objects) throws IOException {
		
		if(objects == null) {
			throw new IOException("Must defined objects");
		}
		
		if(objects.length > EXPOSURE_RANGE) {
			throw new IOException("Cannot exceed exposure range");
		}
		
		if(version <= this.version) {
			throw new IOException("Version must be incremented");
		}
		this.version = version;	
		
		
		
	
		
	
		Queue<NdVertex> queue = new LinkedList<NdVertex>();
		List<NdObject> rollback = new ArrayList<NdObject>();
		

		GraphCrawler crawler = new GraphCrawler() {
			
			@Override
			public void accept(NdObject object) {
				if(!object.S8_spin) {
					
					/* retrieve type */
					NdType type = codebase.getType(object);
					
					String index = object.S8_index;

					/* create vertex */
					NdVertex vertex = new NdVertex(NdBranch.this, type);

					/* assign object to vertex */
					vertex.object = object;
					object.S8_vertex = vertex;

					

					// else, object already known from update branch base
					if(index == null) {

						/* index */
						index = createNewIndex();

						/* index */
						object.S8_index = index;

						// newly created
						vertex.isCreateUnpublished = true;
					}

					/* append vertex */
					vertices.put(index, vertex);
					
					queue.add((NdVertex) object.S8_vertex);
					rollback.add(object);
					object.S8_spin = true;	
				}
			}
		};


		int range = objects.length;
		exposure = new NdVertex[range];
		for(int port = 0; port < range; port ++) {

			NdObject object = objects[port];

			if(object!=null) {

				// prepare initial set
				crawler.accept(object);
				
				NdVertex vertex = (NdVertex) object.S8_vertex;
			
				/* expose vertex */
				vertex.isExposeUnpublished = true;
				vertex.port = port;

				exposure[port] = vertex;
			}
		}
		
	
		NdVertex vertex;
		
		// 
		while((vertex = queue.poll()) != null) { vertex.sweep(crawler); }
		
		// rollback
		rollback.forEach(object -> { object.S8_spin = false; });
	}


	public NdObject retrieveObject(String index) {
		return vertices.get(index).object;
	}


	/**
	 * 
	 * @return
	 */
	public String createNewIndex() {
		return idxGen.generate(++highestIndex);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public NdVertex getVertex(String index) {
		return vertices.get(index);
	}




	@SuppressWarnings("unchecked")
	public <T extends NdObject> T access(int port) throws S8Exception {
		NdVertex vertex = exposure[port];
		return  vertex != null ? (T) vertex.object : null;
	}




	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void pull(ByteInflow inflow) throws IOException {

		// check opening
		if(!inflow.matches(BOHR_Keywords.FRAME_HEADER)) { throw new IOException("DO NOT MATCH HEADER"); }
		inbound.parse(inflow);
		if(!inflow.matches(BOHR_Keywords.FRAME_FOOTER)) { throw new IOException("DO NOT MATCH FOOTER"); }
	}


	/**
	 * @throws IOException
	 * 
	 */
	public void roll() throws IOException {
		NdBranchDelta delta;
		while((delta = deltas.poll()) != null){ delta.consume(this); }
	}



	/**
	 * 
	 * @return
	 * @throws S8ShellStructureException 
	 * @throws IOException 
	 */
	public NdBranch deepClone() throws IOException, S8ShellStructureException {
		return cloneModule.deepClone();
	}


	/**
	 * 
	 * @param branch
	 * @throws IOException
	 * @throws S8ShellStructureException
	 */
	public void commit(NdBranch branch) throws IOException, S8ShellStructureException {
		commitModule.commitChange(branch);
	}


	/**
	 * 
	 * @param outflow
	 * @throws IOException
	 */
	public void push(ByteOutflow outflow) throws IOException {

		// check opening
		outflow.putByteArray(BOHR_Keywords.FRAME_HEADER);
		outbound.compose(outflow);
		outflow.putByteArray(BOHR_Keywords.FRAME_FOOTER);
	}




	/**
	 * 
	 * @param update
	 * @param writer
	 * @throws IOException
	 * @throws S8ShellStructureException
	 */
	public void deepCompare(NdBranch update, OutputStreamWriter writer) throws IOException, S8ShellStructureException {
		debugModule.deepCompare(update, writer);
	}
	


	public BuildScope createBuildScope() {
		return new BuildScope() {
			@Override
			public NdObject retrieveObject(String index) {
				return vertices.get(index).object;
			}
		};
	}
	
	
	
	
	
	
	/**
	 * <b>(Internal use only)</b>
	 * @param port
	 * @param object
	 */
	public void expose(int port, NdVertex vertex) {
		int range = exposure.length;
		if(port >= range) {
			NdVertex[] expansion = new NdVertex[range > 0 ? 2 * range : 2];
			for(int i=0; i<range; i++) {
				expansion[i] = exposure[i];
			}
			exposure = expansion;
		}
		exposure[port] = vertex;
	}
	
	
	/**
	 * <b>(Internal use only)</b>
	 * @param vertices
	 */
	public void expose(NdVertex[] exposure) {
		this.exposure = exposure;
	}






	public int getExposureRange() {
		return exposure.length;
	}
}
