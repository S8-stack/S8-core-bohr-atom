package com.s8.io.bohr.neodymium.object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.branch.NdBranch;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.type.GraphCrawler;
import com.s8.io.bohr.neodymium.type.NdType;
import com.s8.io.bohr.neodymium.type.NdTypeComposer;
import com.s8.io.bohr.neodymium.type.NdTypeParser;
import com.s8.io.bytes.alpha.MemoryFootprint;


/**
 * <h1>Node for sweepable graph</h1>
 * <p>Node encompass in a unified interface two types of cases:</p>
 * <ul>
 * <li>On the fly type resolution (S8Struct)</li>
 * <li>Compiled type resolution, stored in S8Vertex extension (like LiVertex) (S8Object)</li>
 * </ul>
 * <p>This is the building block for using sweep on graph</p>
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdVertex {



	/**
	 * 
	 */
	public final NdBranch branch;

	/**
	 * 
	 */
	public final NdType type;


	public NdObject object;







	/**
	 * 
	 */
	public NdTypeParser typeParser;


	/**
	 * 
	 */
	public NdTypeComposer typeComposer;


	/**
	 * 
	 */
	public long event;

	public int port = -1;


	
	public boolean isCreateUnpublished = false;
	
	public boolean isExposeUnpublished = false;

	/**
	 * 
	 * @param branch
	 * @param type
	 */
	public NdVertex(NdBranch branch, NdType type) {
		super();
		this.branch = branch;
		this.type = type;
	}


	public String getIndex() {
		return object.S8_index;
	}


	/**
	 * 
	 * @param front
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void sweep(GraphCrawler crawler) throws IOException {
		type.sweep(object, crawler);
	}



	/**
	 * 
	 * @param references
	 * @throws IOException
	 */
	public void sweepReferences(Queue<String> references) {
		type.collectReferencedBlocks(object, references);	
	}


	public void getByteCount(MemoryFootprint footprint) {

		type.computeFootprint(object, footprint);

	}


	
	public void advertise(long event) {
		this.event |= event;
	}


	
	/**
	 * 
	 * @return
	 */
	public NdBranch getBranch() {
		return branch;
	}


	
	public NdObject getObject() {
		return object;
	}

	/**
	 * 
	 * @param objectDeltas
	 * @throws NdIOException
	 */
	public void publishCreate(List<NdObjectDelta> objectDeltas) throws NdIOException {
		
		String index = object.S8_index;
		
		List<NdFieldDelta> deltas = new ArrayList<NdFieldDelta>();

		NdField[] fields = type.fields;
		int n = fields.length;
		for(int i=0; i<n; i++) {
			deltas.add(fields[i].produceDiff(object));	
		}

		objectDeltas.add(new CreateNdObjectDelta(index, type, deltas));
		

		/* first time port exposure */
		if(port >= 0) { // slot update
			objectDeltas.add(new ExposeNdObjectDelta(index, port));	
		}
	}
	
	

	/**
	 * 
	 * @param base
	 * @param objectDeltas
	 * @throws NdIOException
	 */
	public void publishUpdate(List<NdObjectDelta> objectDeltas, NdVertex base) throws NdIOException {
		
		NdObject baseObject = base.object;
		List<NdFieldDelta> deltas = null;

		NdField[] fields = type.fields;
		int n = fields.length;
		NdField field;
		for(int i=0; i<n; i++) {
			if((field = fields[i]).hasDiff(baseObject, object)) {
				if(deltas == null) {
					deltas = new ArrayList<NdFieldDelta>();
				}
				deltas.add(field.produceDiff(object));
			}
		}
		
		/* publish only if has deltas */
		if(deltas != null) {
			objectDeltas.add(new UpdateNdObjectDelta(object.S8_index, type, deltas));
		}
		
		/* if port has been updated */
		if(base.port != port) {
			objectDeltas.add(new ExposeNdObjectDelta(object.S8_index, port));
		}
	}
}
