package com.s8.io.bohr.neon.methods.primitives;

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
public class StringUTF8NeMethod extends NeMethod {


	public interface Lambda {
		
		public void operate(String arg);
		
	}
	

	public final static long SIGNATURE = BOHR_Types.STRING_UTF8;

	public @Override long getSignature() { return SIGNATURE; }

	
	/**
	 * 
	 * @param prototype
	 * @param name
	 */
	public StringUTF8NeMethod(NeObjectPrototype prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void run(NeBranch branch, ByteInflow inflow, NeFunc func) throws IOException {
		String arg = inflow.getStringUTF8();
		((Lambda) (func.lambda)).operate(arg);
	}
	
}
