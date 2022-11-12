package com.s8.io.bohr.neodymium.branch;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.object.NdObject;
import com.s8.io.bohr.neodymium.object.NdVertex;
import com.s8.io.bohr.neodymium.type.NdType;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DebugModule {

	private final NdBranch shell;
	
	public DebugModule(NdBranch shell) {
		super();
		this.shell = shell;
	}
	


	/**
	 * 
	 * @throws S8ShellStructureException 
	 * @throws IOException 
	 */
	public void deepCompare(NdBranch update, Writer writer) throws IOException, S8ShellStructureException {
		
		writer.write("<delta-tracking:>\n");
		
		update.vertices.forEach((index, vertex) -> {
			try {
				NdType type = vertex.type;
				
				NdVertex baseVertex = shell.vertices.get(index);
				if(baseVertex==null || !baseVertex.type.getSerialName().equals(type.getSerialName())) {
					writer.append("Object replacement: "+index+"\n");
				}
				else {
					NdObject baseObject = shell.vertices.get(index).getObject();
					type.deepCompare(baseObject, vertex.getObject(), writer);
					
					if(baseVertex.port != vertex.port) {
						try {
							writer.append("Exposed entry "+vertex.port+" is remapped to");
						} 
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} 
			catch (NdIOException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			} 
			catch (S8ShellStructureException e) {
				e.printStackTrace();
			}
		});
		
		writer.write("</delta-tracking>\n");
		writer.append("DEEP COMPARE TERMINATED\n\n");
	}
	
	/**
	 * 
	 * @throws IOException 
	 */
	public void print(Writer writer) throws IOException {
		
		writer.write("<shell:>\n");
		
		shell.vertices.forEach((index, vertex) -> {
			try {
				NdType type = vertex.type;
				NdObject object = vertex.getObject();
				type.print(object, writer);
			} 
			catch (NdIOException e) {
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
