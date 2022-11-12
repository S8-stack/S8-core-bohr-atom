package com.s8.io.bohr.neodymium.branch;

import static com.s8.io.bohr.atom.BOHR_Keywords.CLOSE_JUMP;
import static com.s8.io.bohr.atom.BOHR_Keywords.CREATE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.DECLARE_TYPE;
import static com.s8.io.bohr.atom.BOHR_Keywords.DEFINE_JUMP_COMMENT;
import static com.s8.io.bohr.atom.BOHR_Keywords.DEFINE_JUMP_TIMESTAMP;
import static com.s8.io.bohr.atom.BOHR_Keywords.DEFINE_JUMP_VERSION;
import static com.s8.io.bohr.atom.BOHR_Keywords.EXPOSE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.REMOVE_NODE;
import static com.s8.io.bohr.atom.BOHR_Keywords.UPDATE_NODE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdFieldDelta;
import com.s8.io.bohr.neodymium.fields.NdFieldParser;
import com.s8.io.bohr.neodymium.object.CreateNdObjectDelta;
import com.s8.io.bohr.neodymium.object.ExposeNdObjectDelta;
import com.s8.io.bohr.neodymium.object.NdVertex;
import com.s8.io.bohr.neodymium.object.RemoveNdObjectDelta;
import com.s8.io.bohr.neodymium.object.UpdateNdObjectDelta;
import com.s8.io.bohr.neodymium.type.NdTypeParser;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdBranchParser {
	
	private final NdBranch branch;
	


	/**
	 * code base
	 */
	private final NdInbound inbound;


	private NdBranchDelta delta;
	
	private long version;


	
	/**
	 * 
	 * @param codebase
	 * @param graph
	 * @param isVerbose
	 */
	public NdBranchParser(NdBranch branch) {
		super();
		this.branch = branch;
		this.inbound = branch.inbound;
	}

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
			
			case DEFINE_JUMP_COMMENT:
				onDefineComment(inflow.getStringUTF8());
				break;
				
			case DEFINE_JUMP_TIMESTAMP:
				onDefineTimestamp(inflow.getUInt64());
				break;
				
			case DEFINE_JUMP_VERSION:
				onDefineVersion(inflow.getUInt64());
				break;

			case DECLARE_TYPE:
				onDeclareType(inflow);
				break;
				
			case CREATE_NODE:
				onCreateNode(inflow);
				break;
			

			case UPDATE_NODE:
				onUpdateNode(inflow);
				break;
			
			case EXPOSE_NODE:
				onExposeNode(inflow);
				break;

			case REMOVE_NODE: 
				onRemoveNode(inflow);
				break;

			default : throw new IOException("Unsupported BOHR keyword code: "+Integer.toHexString(code));
			}
		}

		onCloseDelta();
	}

	
	

	public class N6ObjectListener implements NdTypeParser.DeltaListener {

		private List<NdFieldDelta> deltas;
		
		public N6ObjectListener(List<NdFieldDelta> deltas) {
			super();
			this.deltas = deltas;
		}

		@Override
		public void onFieldValueChange(NdFieldParser parser, ByteInflow inflow) throws IOException {

			/* read value */
			deltas.add(parser.deserializeDelta(inflow));
		}

	}



	private void onOpenDelta() throws NdIOException {
		delta = new NdBranchDelta();
	}
	
	
	private void onDefineComment(String comment) throws NdIOException {
		delta.setComment(comment);
	}

	
	private void onDefineTimestamp(long timestamp) throws NdIOException {
		delta.setTimestamp(timestamp);
	}
	
	
	private void onDefineVersion(long version) throws NdIOException {
		delta.setVersion(version);
		this.version = version;
	}

	
	private void onDeclareType(ByteInflow inflow) throws IOException {
		inbound.declareType(inflow);
	}
	
	private void onCreateNode(ByteInflow inflow) throws IOException {

		if(version < 0) { throw new NdIOException("Version is not defined"); }
		
		long typeCode = inflow.getUInt7x();
		
		NdTypeParser typeParser = inbound.getTypeParserByCode(typeCode);
		
		String index = inflow.getStringUTF8();
		
		/* create vertex */
		NdVertex vertex = new NdVertex(branch, typeParser.getType());
		branch.vertices.put(index, vertex);

		/* assign type inflow */
		vertex.typeParser = typeParser;

		// store vertex in shell (as a consequence of having an index)
		branch.vertices.put(index, vertex);

		// object delta
		CreateNdObjectDelta objectDelta = new CreateNdObjectDelta(index, typeParser.getType(), new ArrayList<NdFieldDelta>());
		
		N6ObjectListener objectListener = new N6ObjectListener(objectDelta.deltas);

		// type parser
		typeParser.parse(inflow, objectListener);

		delta.objectDeltas.add(objectDelta);
	}

	


	private void onUpdateNode(ByteInflow inflow) throws IOException {

		/* index */
		String index = inflow.getStringUTF8();
		
		/* retrieve vertex */
		NdVertex vertex = branch.vertices.get(index);
		
		if(vertex == null) { throw new IOException("Out of context exception"); }

		/* retrieve parser */
		NdTypeParser typeParser = vertex.typeParser;

		/* create update object delta */
		UpdateNdObjectDelta objectDelta = new UpdateNdObjectDelta(index, typeParser.getType(), new ArrayList<NdFieldDelta>());

		/* listener */
		N6ObjectListener objectListener = new N6ObjectListener(objectDelta.deltas);
		
		// type parser
		typeParser.parse(inflow, objectListener);
		
		/* append */
		delta.appendObjectDelta(objectDelta);
	}


	

	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	private void onExposeNode(ByteInflow inflow) throws IOException {
		
		/* index */
		String index = inflow.getStringUTF8();

		/* retrieve slot */
		int slot = inflow.getUInt8();

		delta.appendObjectDelta(new ExposeNdObjectDelta(index, slot));
	}
	
	
	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	private void onRemoveNode(ByteInflow inflow) throws IOException {
		/* index */
		String index = inflow.getStringUTF8();

		delta.appendObjectDelta(new RemoveNdObjectDelta(index));
	}	

	private void onCloseDelta() throws NdIOException {
		branch.deltas.add(delta);
	}

	

}
