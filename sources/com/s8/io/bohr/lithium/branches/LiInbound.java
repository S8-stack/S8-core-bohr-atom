package com.s8.io.bohr.lithium.branches;

import static com.s8.io.bohr.atom.BOHR_Keywords.CLOSE_SEQUENCE;
import static com.s8.io.bohr.atom.BOHR_Keywords.OPEN_JUMP;
import static com.s8.io.bohr.atom.BOHR_Keywords.OPEN_SEQUENCE;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.lithium.codebase.LiCodebase;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.type.LiType;
import com.s8.io.bohr.lithium.type.LiTypeParser;
import com.s8.io.bytes.alpha.ByteInflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class LiInbound {




	/**
	 * 
	 */
	private final LiBranch branch;




	/**
	 * code base
	 */
	private final LiCodebase codebase;



	/**
	 * 
	 */
	private final Map<Long, LiTypeParser> typeParsersByCode;




	/**
	 * 
	 * @param codebase
	 * @param graph
	 * @param isVerbose
	 */
	public LiInbound(LiBranch branch) {
		super();
		this.branch = branch;
		this.codebase = branch.codebase;

		typeParsersByCode = new HashMap<Long, LiTypeParser>();
	}



	
	
	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public void parse(ByteInflow inflow) throws IOException {

		int code;

		if((code = inflow.getUInt8()) != OPEN_SEQUENCE) {
			throw new IOException("Sequence mist startwith an open sequecne tag");
		}

		while((code = inflow.getUInt8()) != CLOSE_SEQUENCE) {
			switch(code) {

			case OPEN_JUMP: createBranchParser().parse(inflow); break;

			default : throw new IOException("Unsupported BOHR keyword code: "+Integer.toHexString(code));
			}
		}
	}

	/**
	 * 
	 * @param inflow
	 * @throws IOException
	 */
	public LiTypeParser declareType(ByteInflow inflow) throws IOException {

		/* retrieve name of this newly declared type */
		String typeName = inflow.getStringUTF8();

		/* retrieve code assigned to this newly declared type */
		long typeCode = inflow.getUInt7x();

		/* check that this type code has not already been assigned */
		if(typeParsersByCode.containsKey(typeCode)) {
			throw new LiIOException("A type has already defined for code: "+typeCode+"->"+typeName
					+". See "+typeParsersByCode.get(typeCode).print(typeCode));
		}
		
		/* find corresponding type */
		LiType type = codebase.getTypeBySerialName(typeName);
		if(type == null) {
			throw new LiIOException("Failed to find type for name: "+typeName);
		}

		/* create typeInflow*/
		LiTypeParser typeParser = new LiTypeParser(type);

		/* store this typeInflow for later use */
		typeParsersByCode.put(typeCode, typeParser);
		//typeInflowsByClass.put(type.getRuntimeName(), typeInflow);

		return typeParser;
	}


	/**
	 * 
	 * @param code
	 * @return
	 * @throws LiIOException
	 */
	public LiTypeParser getTypeParserByCode(long code) throws LiIOException {
		LiTypeParser typeParser = typeParsersByCode.get(code);
		if(typeParser == null) {
			throw new LiIOException("Failed to find typeInflow for code: "+Long.toHexString(code));
		}
		return typeParser;
	}	

	
	
	/**
	 * 
	 * @return
	 */
	public LiBranchParser createBranchParser() {
		return new LiBranchParser(branch);
	}
}
