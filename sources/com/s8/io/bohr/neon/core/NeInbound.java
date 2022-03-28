package com.s8.io.bohr.neon.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.BOHR_Keywords;
import com.s8.io.bohr.neon.methods.NeFunc;
import com.s8.io.bohr.neon.methods.NeRunnable;
import com.s8.io.bytes.alpha.ByteInflow;



/**
 * 
 * @author pierreconvert
 *
 */
public class NeInbound {

	private final NeBranch branch;

	private final Map<Long, NeObjectTypeHandler> inboundPrototypesByCode;

	private int forkCode;

	private long viewIndex = -1;

	private int methodCode = -1;

	private Object[] params;

	public NeInbound(NeBranch branch) {
		super();
		this.branch = branch;
		inboundPrototypesByCode = new HashMap<>();
	}
	

	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void consume(ByteInflow inflow) throws IOException {

		int code;
		while((code = inflow.getUInt8()) != BOHR_Keywords.CLOSE_JUMP) {
			switch(code) {
			case BOHR_Keywords.DECLARE_TYPE: declareType(inflow); break;
			case BOHR_Keywords.DECLARE_METHOD: declareMethod(inflow); break;
			case BOHR_Keywords.RUN_FUNC : runFunc(inflow); break;
			}
		}
	}

	


	private void declareType(ByteInflow inflow) throws IOException {

		String typename = inflow.getStringUTF8();

		NeObjectTypeHandler prototype = branch.prototypesByName.get(typename);
		if(prototype == null) {
			throw new IOException("Failed to retrieve prototype for typename: "+typename);
		}
		
		long code = inflow.getUInt7x();
		
		// store new entry
		inboundPrototypesByCode.put(code, prototype);

	}

	
	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	private void declareMethod(ByteInflow inflow) throws IOException {

		long typeCode = inflow.getUInt7x();
		
		NeObjectTypeHandler prototype = inboundPrototypesByCode.get(typeCode);
		if(prototype == null) {
			throw new IOException("Failed to retrieve prototype for code: "+typeCode);
		}
		
		prototype.consumeDeclareRunnable(inflow);
	}
	
	
	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	private void runFunc(ByteInflow inflow) throws IOException {
		
		String index = inflow.getStringUTF8();
		NeVertex object = branch.vertices.get(index);
		if(object == null) { throw new IOException("No Object for index = "+index); }
		
		int code = inflow.getUInt8();
		NeRunnable runnable = object.prototype.inboundRunnables[code];
		if(runnable == null) { throw new IOException("No runnable for code = "+code); }
		
		NeFunc func = object.funcs[code];
		if(func == null) { throw new IOException("Missing func @ code = "+code+", for index = "+index); }
		
		/* run function */
		runnable.run(branch, inflow, func);
	}

	
	
	
	/**
	 * 
	 * @return the sub-protocol index within the [0x00, 0xff] index range.
	 */
	public int getForkCode() {
		return forkCode;
	}


	/**
	 * 
	 * @return the view index [0x0, 0xffffffffffffffff]
	 */
	public long getViewIndex() {
		return viewIndex;
	}



	/**
	 * 
	 * @return the method code
	 */
	public int getMethodCode() {
		return methodCode;
	}



	/**
	 * 
	 * @return
	 */
	public Object[] getParameters() {
		return params;
	}

	/**
	 * 
	 * @return
	 */
	public Object getParameter(int index) {
		return params[index];
	}


}
