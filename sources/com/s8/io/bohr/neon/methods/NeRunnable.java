package com.s8.io.bohr.neon.methods;

import java.io.IOException;

import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bytes.alpha.ByteInflow;

public interface NeRunnable {
	
	
	/**
	 * 
	 * @param branch
	 * @param inflow
	 * @param func
	 * @throws IOException
	 */
	public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException;
	
	
}
