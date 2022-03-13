package com.s8.io.bohr.neodymium.branches;

import java.io.IOException;

import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.objects.NdVertex;
import com.s8.io.bohr.neodymium.type.BuildScope;


/**
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class CloneModule {
	
	private final NdBranch branch;
	
	public CloneModule(NdBranch branch) {
		super();
		this.branch = branch;
	}
	
	


	/**
	 * 
	 * @throws S8ShellStructureException 
	 * @throws IOException 
	 */
	public NdBranch deepClone() throws IOException, S8ShellStructureException {
		
		
		// copy map
		//shell.remap();
		
		NdBranch branchClone = new NdBranch(
				branch.codebase, 
				branch.address, 
				branch.getIdentifier(), 
				branch.version);
		
	
		
		
		BuildScope scope = branchClone.createBuildScope();
		
		
		/* vertices */
		branch.vertices.forEach((index, vertex) -> {
			try {
				S8Object objectClone = vertex.type.deepClone(vertex.object, scope);
				objectClone.S8_index = index;
				NdVertex vertexClone = new NdVertex(branchClone, vertex.type);
				vertexClone.object = objectClone;
				vertexClone.port = vertex.port;
				
				branchClone.vertices.put(index, vertexClone);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		});

		// resolve
		scope.resolve();
		
		// copy exposure
		int range = branch.getExposureRange();
		NdVertex[] exposureClone = new NdVertex[range];
		S8Object exposed;
		for(int slot = 0; slot < range; slot++) {
			if((exposed = branch.access(slot)) != null) {
				exposureClone[slot] = branchClone.vertices.get(exposed.S8_index);
			}
		}
		branchClone.expose(exposureClone);
		
		// copy last assigned index
		return branchClone;
	}

}
