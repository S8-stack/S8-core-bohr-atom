package com.s8.io.bohr.neodymium.branch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.neodymium.codebase.NdCodebase;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.type.NdType;
import com.s8.io.bohr.neodymium.type.NdTypeComposer;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class NdOutbound {

	private final NdBranch branch;

	private final NdCodebase codebase;

	private long typeCode = 0;

	/**
	 * 
	 */
	private final Map<String, NdTypeComposer> composers;


	public NdOutbound( NdBranch branch) {
		super();
		this.branch = branch;
		this.codebase = branch.codebase;
		this.composers = new HashMap<>();
	}



	/**
	 * 
	 * @param type
	 * @return
	 * @throws NdIOException
	 */
	public NdTypeComposer getComposer(Class<?> type) throws NdIOException {
		String runtimeName = type.getName();
		return getComposer(runtimeName);
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws NdIOException
	 */
	public NdTypeComposer getComposer(String runtimeTypeName) throws NdIOException {
		NdTypeComposer composer = composers.computeIfAbsent(runtimeTypeName, name -> {
			NdType nType = codebase.getTypeByRuntimeName(name);
			try {
				return new NdTypeComposer(nType, ++typeCode);
			} 
			catch (NdBuildException e) {
				e.printStackTrace();
				return null;
			}
		});
		if(composer != null) { return composer; }
		else {
			throw new NdIOException("failed to build composer");
		}
	}



	public void compose(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Keywords.OPEN_SEQUENCE);
		NdBranchDelta delta;
		Queue<NdBranchDelta> deltas = branch.deltas;
		while((delta = deltas.poll()) != null){
			delta.serialize(this, outflow);
		}
		outflow.putUInt8(BOHR_Keywords.CLOSE_SEQUENCE);
	}

}
