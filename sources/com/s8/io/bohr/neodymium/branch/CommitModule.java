package com.s8.io.bohr.neodymium.branch;

import java.io.IOException;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.object.NdVertex;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class CommitModule {


	/**
	 * 
	 */
	private final NdBranch branch;


	/**
	 * 
	 * @param shell
	 */
	public CommitModule(NdBranch branch) {
		super();
		this.branch = branch;
	}


	/**
	 * Update
	 * 
	 * @throws IOException 
	 * @throws S8ShellStructureException 
	 * 
	 */
	public void commitChange(NdBranch update) throws IOException, S8ShellStructureException {

		/* ALWAYS REMAP PRIOR TO ANY delta ops
		 * 
		 * Note: you can think of remap like a garbage collecting operation  
		 */
		//shell.remap();
		//update.remap();


		/* generate common code base */
		//Map<String, LiType> types = new HashMap<String, LiType>();
		//shell.declareTypes(types);
		//update.declareTypes(types);

		//LithCodebaseIO codebaseIO = new LithCodebaseIO();
		//codebaseIO.compile(types);


		/* compute delta */
		NdBranchDelta delta = new NdBranchDelta();


		update.vertices.forEach((index, vertex) -> {
			try {
				/* update case */
				NdVertex baseVertex;
				if((baseVertex = branch.vertices.get(index)) != null) {
					vertex.publishUpdate(delta.objectDeltas, baseVertex);
				}
				/* create case  */
				else {
					vertex.publishCreate(delta.objectDeltas);
				}
			} catch (NdIOException e) {
				e.printStackTrace();
			}
		});



		// set parameters
		//delta.lastAssignedIndex = update.highestIndex;

		// append delta to branch delta
		branch.deltas.add(delta);
	}

}
