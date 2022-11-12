package com.s8.io.bohr.lithium.branches;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.S8Graph;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.LiType;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DebugModule {

	private final LiBranch branch;
	
	public DebugModule(LiBranch branch) {
		super();
		this.branch = branch;
	}
	


	/**
	 * 
	 * @throws S8ShellStructureException 
	 * @throws IOException 
	 */
	public void deepCompare(LiBranch update, Writer writer) throws IOException, S8ShellStructureException {
		
		writer.write("\n<delta-tracking:>\n");
		//shell.remap();
		//update.remap();
		
		update.vertices.forEach((index, vertex) -> {
			try {
				LiType type = vertex.getType();
				
				LiVertex baseVertex = branch.vertices.get(index);
				if(baseVertex==null || !baseVertex.getType().getSerialName().equals(type.getSerialName())) {
					writer.append("Object replacement");
				}
				else {
					LiS8Object baseObject = baseVertex.getObject();
					type.deepCompare(baseObject, vertex.getObject(), writer);	
				}
			} 
			catch (LiIOException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (S8ShellStructureException e) {
				e.printStackTrace();
			}
		});
		
		
		
		for(int slot=0; slot<S8Graph.EXPOSURE_RANGE; slot++) {
			LiVertex vertex = update.exposure[slot], baseVertex = branch.exposure[slot];
			if(vertex != null) {
				if(!baseVertex.getIndex().equals(vertex.getIndex())) {
					try {
						writer.append("Exposed entry "+slot+" is remapped to");
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		writer.write("</delta-tracking>\n");
		writer.append("DEEP COMPARE TERMINATED!\n");
	}
	
	/**
	 * 
	 * @throws IOException 
	 */
	public void print(Writer writer) throws IOException {
		
		writer.write("<shell:>\n");
		
		branch.vertices.forEach((index, vertex) -> {
			try {
				LiType type = vertex.getType();
				LiS8Object object = vertex.getObject();
				type.print(object, writer);
			} 
			catch (LiIOException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (S8ShellStructureException e) {
				e.printStackTrace();
			}
		});
		writer.write("\n</shell:>");
	}
}
