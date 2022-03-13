package com.s8.io.bohr.neodymium.objects;

import java.io.IOException;

import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.neodymium.branches.NdBranch;
import com.s8.io.bohr.neodymium.branches.NdOutbound;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.type.BuildScope;
import com.s8.io.bytes.alpha.ByteOutflow;
import com.s8.io.bytes.alpha.MemoryFootprint;

public abstract class NdObjectDelta {

	public final S8Index index;



	public NdObjectDelta(S8Index index) {
		super();
		this.index = index;
	}


	/**
	 * 
	 * @param shell
	 * @return
	 * @throws NdIOException 
	 * @throws IOException 
	 */
	public abstract void consume(NdBranch branch, BuildScope scope) throws NdIOException;

	
	/**
	 * 
	 * @param outbound
	 * @param outflow
	 * @throws IOException
	 */
	public abstract void serialize(NdOutbound outbound, ByteOutflow outflow) throws IOException;

	
	/**
	 * 
	 * @param weight
	 */
	public abstract void computeFootprint(MemoryFootprint weight);
}
