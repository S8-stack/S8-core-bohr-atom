package com.s8.io.bohr.lithium.branches;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.lithium.codebase.LiCodebase;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.LiType;
import com.s8.io.bohr.lithium.type.LiTypeComposer;
import com.s8.io.bohr.lithium.type.PublishScope;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class LiOutbound implements PublishScope {
	
	
	private final LiCodebase codebase;
	
	private long typeCode = 0;
	
	/**
	 * 
	 */
	private final Map<String, LiTypeComposer> composers;
	

	
	private boolean hasUnpublishedChanges = false;
	
	private final LiBranch branch;

	public final Deque<LiVertex> unpublishedVertices;
	
	
	
	public LiOutbound(LiBranch branch) {
		super();
		this.branch = branch;
		this.codebase = branch.codebase;
		
		this.composers = new HashMap<>();
		
		// branch

				// unpublished vertices
				this.unpublishedVertices = new LinkedList<LiVertex>();
	}
	
	
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws LiIOException
	 */
	public LiTypeComposer getComposer(Class<?> type) throws LiIOException {
		String runtimeName = type.getName();
		return getComposer(runtimeName);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 * @throws LiIOException
	 */
	public LiTypeComposer getComposer(String runtimeTypeName) throws LiIOException {
		LiTypeComposer composer = composers.computeIfAbsent(runtimeTypeName, name -> {
			LiType nType = codebase.getTypeByRuntimeName(name);
			try {
				return new LiTypeComposer(nType, ++typeCode);
			} 
			catch (LiBuildException e) {
				e.printStackTrace();
				return null;
			}
		});
		if(composer != null) { return composer; }
		else {
			throw new LiIOException("failed to build composer");
		}
	}
	

	
	public void reportActivity(LiVertex vertex) {
		unpublishedVertices.add(vertex);
		hasUnpublishedChanges = true;
	}
	
	public void pushVertex(LiVertex vertex) {
		hasUnpublishedChanges = true;
		unpublishedVertices.add(vertex);
	}
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException 
	 * @throws S8Exception 
	 * @throws S8BuildException 
	 */
	public void compose(ByteOutflow outflow) throws S8BuildException, S8Exception, IOException {
		
		outflow.putUInt8(BOHR_Keywords.OPEN_SEQUENCE);
		
		outflow.putUInt8(BOHR_Keywords.OPEN_JUMP);
		
		if(hasUnpublishedChanges) {
			LiVertex vertex;
			while((vertex = unpublishedVertices.poll()) != null) {
				vertex.publish(outflow);
			}
			hasUnpublishedChanges = false;
		}
		
		outflow.putUInt8(BOHR_Keywords.CLOSE_JUMP);
		
		outflow.putUInt8(BOHR_Keywords.CLOSE_SEQUENCE);
	}


	@Override
	public String append(LiS8Object object) {
		
		/* append to branch (-> under the hood, add to unpublished) */
		
		/* retrieve object index */
		String index = object.S8_index;
		
		/* if index is null, assigned a newly generated one */
		if(index == null) {
			index = branch.createNewIndex();
			object.S8_index = index;
		}
		
		
		/* retrieve object vertex */
		LiVertex vertex = (LiVertex) object.S8_vertex;
		
		/* if no vertex assigned, create one on the fly */
		if(vertex == null) {
			
			/* create vertex */
			vertex = new LiVertex(branch, object);
			
			/* assign newly created vertex */
			object.S8_vertex = vertex;
			
			/* newly created vertex, so report activity */
			unpublishedVertices.add(vertex);
			hasUnpublishedChanges = true;
			
			/* register vertex */
			branch.vertices.put(index, vertex);
		}
		
		/* append always result in object having an index */
		return object.S8_index;
	}




	public LiBranch getBranch() {
		return branch;
	}

}
