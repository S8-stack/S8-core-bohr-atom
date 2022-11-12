package com.s8.io.bohr.lithium.branches;

import static com.s8.io.bohr.atom.BOHR_Keywords.CLOSE_JUMP;
import static com.s8.io.bohr.atom.BOHR_Keywords.CREATE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.DECLARE_TYPE;
import static com.s8.io.bohr.atom.BOHR_Keywords.EXPOSE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.REMOVE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.UPDATE_NODE;

import java.io.IOException;

import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiFieldParser;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.BuildScope;
import com.s8.io.bohr.lithium.type.LiType;
import com.s8.io.bohr.lithium.type.LiTypeParser;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class LiBranchParser {
	

	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	private static class TypeListener implements LiTypeParser.DeltaListener {

		public LiS8Object object;

		public BuildScope scope;

		public TypeListener(LiS8Object object, BuildScope scope) {
			super();
			this.object = object;
			this.scope = scope;
		}

		@Override
		public void onFieldValueChange(LiFieldParser parser, ByteInflow inflow) throws IOException {

			/* read value */
			parser.parseValue(object, inflow, scope);
		}
	}
	
	
	
	private LiBranch branch;
	

	public LiBranchParser(LiBranch branch) {
		super();
		this.branch = branch;
		this.inbound = branch.inbound;
	}

	
	private BuildScope scope;

	
	
	


	/**
	 * code base
	 */
	private final LiInbound inbound;

	

	/**
	 * 
	 * @param graph
	 * @param inflow
	 * @throws IOException
	 */
	public void parse(ByteInflow inflow) throws IOException {


		int code;

		onOpenDelta();

		while((code = inflow.getUInt8()) != CLOSE_JUMP) {
			switch(code) {
			
			case DECLARE_TYPE: onDeclareType(inflow);
				break;
			
			case CREATE_NODE: onCreateNode(inflow);
				break;
				
			case UPDATE_NODE: onUpdateNode(inflow);
				break;

			case EXPOSE_NODE: onExposeNode(inflow);
				break;
		
			case REMOVE_NODE: onRemoveNode(inflow);
				break;

			default : throw new IOException("Unsupported BOHR keyword code: "+Integer.toHexString(code));
			}
		}

		onCloseDelta();
	}
	
		
	public void onOpenDelta() throws LiIOException {
		this.scope = branch.createBuildScope();
	}
	
	
	public void onDeclareType(ByteInflow inflow) throws IOException {
		inbound.declareType(inflow);
	}
	
	
	public void onCreateNode(ByteInflow inflow) throws IOException {

		/* type code */
		long typeCode = inflow.getUInt7x();
		
		/* typeParser */
		LiTypeParser typeParser = inbound.getTypeParserByCode(typeCode);
		
		/* index */
		String index = inflow.getStringUTF8();
		
		/* retrieve type */
		LiType type = typeParser.getType();

		/* create object */
		LiS8Object object = type.createNewInstance();

		/* clear spin */
		object.S8_spin = false;

		/* assign object id */
		object.S8_index = index;

		/* create vertex on object (v->o & o->v links created) */
		LiVertex vertex = branch.append(object);

		/* assign parser */
		vertex.typeParser = typeParser;

		/* store vertex in graph */
		//branch.vertices.add(vertex);

		/* parse fields values */
		typeParser.parse(inflow, new TypeListener(object, scope));
	}

	
	
	public void onUpdateNode(ByteInflow inflow) throws IOException {
		
		String index = inflow.getStringUTF8();
		
		LiVertex vertex = branch.vertices.get(index);
		if(vertex == null) {
			throw new LiIOException("Failed to find vertex for index = "+index);
		}

		/* retrieve parser from vertex */
		LiTypeParser typeParser = vertex.typeParser;

		/* parse fields values */
		typeParser.parse(inflow, new TypeListener(vertex.object, scope));
	}

	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void onExposeNode(ByteInflow inflow) throws IOException {
		
		String index = inflow.getStringUTF8();
		int slot = inflow.getUInt8();

		LiVertex vertex = branch.vertices.get(index);
		if(vertex == null) {
			throw new LiIOException("Failed to find vertex for index = "+index);
		}
		
		/* expose vertex in slot */
		branch.exposure[slot] = vertex;
	}

	
	

	
	public void onRemoveNode(ByteInflow inflow) throws IOException {

		
		String index = inflow.getStringUTF8();
		
		/* remove vertex */
		branch.vertices.remove(index);
	}


	
	public void onCloseDelta() throws LiIOException {
		// resolve scope
		scope.resolve();	
	}


	
	public void onDefineComment(String comment) throws LiIOException {
		branch.comment = comment;
	}


	
	public void onDefineTimestamp(long timestamp) throws LiIOException {
		branch.timestamp = timestamp;
	}


	
	public void onDefineVersion(long version) throws LiIOException {
		branch.version = version;
	}
}
