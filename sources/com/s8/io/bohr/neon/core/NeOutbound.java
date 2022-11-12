package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 * @author pierreconvert
 *
 */
public class NeOutbound {


	/**
	 * 
	 */
	private Queue<NeVertex> unpublishedChanges;
	
	
	/**
	 * 
	 */
	public NeOutbound() {
		super();
		unpublishedChanges = new LinkedList<NeVertex>();
	}
	
	
	/**
	 * 
	 * @param outflow
	 * @throws IOException 
	 */
	public void publish(ByteOutflow outflow) throws IOException {
		
		outflow.putUInt8(BOHR_Keywords.OPEN_JUMP);
		
		while(!unpublishedChanges.isEmpty()) {
			unpublishedChanges.poll().publish(outflow);
		}
		
		outflow.putUInt8(BOHR_Keywords.CLOSE_JUMP);
	}



	/**
	 * 
	 * @param object
	 */
	public void notifyChanged(NeVertex object) {
		unpublishedChanges.add(object);
	}
	
}
