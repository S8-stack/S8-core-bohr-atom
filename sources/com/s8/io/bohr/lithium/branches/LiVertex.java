package com.s8.io.bohr.lithium.branches;

import java.io.IOException;
import java.util.Queue;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
import com.s8.io.bohr.lithium.object.LiS8Object;
import com.s8.io.bohr.lithium.type.GraphCrawler;
import com.s8.io.bohr.lithium.type.LiType;
import com.s8.io.bohr.lithium.type.LiTypeComposer;
import com.s8.io.bohr.lithium.type.LiTypeParser;
import com.s8.io.bytes.alpha.ByteOutflow;
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
public class LiVertex {

	
	
	public final LiBranch branch;
	

	/**
	 * The port used to expose this object
	 */
	public int port = -1;


	/**
	 * 
	 */
	public LiType type;
	
	
	/**
	 * 
	 */
	public LiTypeParser typeParser;
	
	
	/**
	 * 
	 */
	public LiTypeComposer typeComposer;
	
	
	
	private boolean isUnpublished = false;
	/**
	 * 
	 */
	private boolean isCreateUnpublished = false;
	
	private boolean isExposeUnpublished = false;
	
	
	
	public final LiS8Object object;
	
	/**
	 * 
	 */
	public long event = 0x7fffffffffffffffL;
	
	
	private int slot = -1;
	
	/**
	 * 
	 * @param type
	 * @param object
	 */
	public LiVertex(LiBranch branch, LiS8Object object) {
		super();
		this.branch = branch;
		this.object = object;
		
		// upon creation, advertise
		
		
		isUnpublished = true;
		
		isCreateUnpublished = true;
		isExposeUnpublished = false;
	}
	
	
	
	public LiS8Object getObject() {
		return object;
	}
	
	
	public String getIndex() {
		return object.S8_index;
	}
	
	
	public LiType getType() throws LiIOException {
		if(typeComposer == null) {
			typeComposer = branch.outbound.getComposer(object.getClass());
		}
		return typeComposer.type;
	}

	/**
	 * 
	 * @param front
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void sweep(GraphCrawler crawler) throws IOException, S8ShellStructureException {
		getType().sweep(object, crawler);
	}
	
	
	
	/**
	 * 
	 * @param references
	 * @throws IOException
	 */
	public void sweepReferences(Queue<String> references) {
		try {
			getType().collectReferencedBlocks(object, references);
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	public void getByteCount(MemoryFootprint footprint) {
		try {
			getType().computeFootprint(object, footprint);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void advertise(long event) {
		this.event |= event;
		
		// internal notification schema
		if(!isUnpublished) {
			branch.outbound.reportActivity(this);
			isUnpublished = true;
		}
	}



	
	/**
	 * 
	 * @param outflow
	 * @param object
	 * @throws BkException
	 * @throws IOException 
	 * @throws S8Exception 
	 */
	public void publish(ByteOutflow outflow) throws S8BuildException, IOException, S8Exception {

		if(isUnpublished) {

			LiOutbound outbound = branch.outbound;
			
			// type composer
			if(typeComposer == null) {
				typeComposer = outbound.getComposer(object.getClass());
			}
			
			/* publish header */
			if(isCreateUnpublished) {
				typeComposer.publish_CREATE_NODE(outflow, object.S8_index);
				isCreateUnpublished = false;
			}
			else {
				typeComposer.publish_UPDATE_NODE(outflow, object.S8_index);
			}
			
			
			/* <fields> */
			long mask;

			LiFieldComposer[] fieldComposers = typeComposer.fieldComposers;
			int nFields = fieldComposers.length;

			for(int i=0; i<nFields; i++) {

				LiFieldComposer fieldComposer = fieldComposers[i];
				mask = fieldComposer.getField().mask;

				if((mask & event) == mask) {

					// output field encoding
					fieldComposer.compose(object, outflow, outbound);
				}		
			}
			
			
			
			/* clear event */
			event = 0x00000L;
			 
			/* </fields> */

			typeComposer.publishCloseNode(outflow);
			
			// expose if necessary
			if(isExposeUnpublished) {
				typeComposer.publish_EXPOSE_NODE(outflow, object.S8_index, slot);
				isExposeUnpublished = false;
			}
			
			// all changes now published, so clear flags
			isUnpublished = false;
		}
	}

	
	
	public LiBranch getBranch() {
		return branch;
	}


	
	public void expose(int slot) {
		this.isExposeUnpublished = true;
		this.isUnpublished = true;
		this.slot = slot;
		branch.exposure[slot] = this;
	}
	
	
	
	public void unexpose() {
		this.isExposeUnpublished = true;
		this.isUnpublished = true;
		this.slot = -1;
		branch.exposure[slot] = null;	
	}
	
}
