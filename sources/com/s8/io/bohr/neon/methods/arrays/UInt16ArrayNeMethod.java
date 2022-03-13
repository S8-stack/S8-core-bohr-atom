package com.s8.io.bohr.neon.methods.arrays;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.NeBranch;
import com.s8.io.bohr.neon.core.NeObjectPrototype;
import com.s8.io.bohr.neon.methods.NeFunc;
import com.s8.io.bohr.neon.methods.NeMethod;
import com.s8.io.bytes.alpha.ByteInflow;

/**
 * 
 * @author pierreconvert
 *
 */
public class UInt16ArrayNeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(int[] arg);
		
	}
	

	public final static long SIGNATURE = (BOHR_Types.ARRAY << 8) & BOHR_Types.UINT16;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public UInt16ArrayNeMethod(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	

	@Override
	public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
		int length = (int) inflow.getUInt7x();
		if(length >= 0) {
			int[] arg = new int[length];
			for(int i=0; i<length; i++) { arg[i] = inflow.getUInt16(); }
			((Lambda) (func.lambda)).operate(arg);
		}
		else {
			((Lambda) (func.lambda)).operate(null);
		}
	}
	
}
