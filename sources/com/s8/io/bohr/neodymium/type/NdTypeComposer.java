package com.s8.io.bohr.neodymium.type;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.exceptions.NdIOException;
import com.s8.io.bohr.neodymium.fields.NdFieldComposer;
import com.s8.io.bytes.alpha.ByteOutflow;

/**
 * 
 * This object represents the I/O mapping that allows to read a version of an
 * object.
 * <p>
 * For instance, let's say that MyS8Object in codebase version 26.8 has the
 * following field mapping:
 * </p>
 * <ul>
 * <li>0 -> alpha</li>
 * <li>1 -> beta</li>
 * <li>2 -> delta</li>
 * </ul>
 * <p>
 * And assume that current codebase (let's say 26.11) is now declaring fields:
 * alpha, gamma, delta. Then we can still map the obect build in version 26.8 to
 * the 26.11 codebase using an TypeVersionIO with the following mapping:
 * </p>
 * * <ul>
 * <li>0 -> alpha (still valid for 26.11)</li>
 * <li>1 -> beta (<b>dismissed</b> in 26.11)</li>
 * <li>gamma: was not known in 26.8, so cannot exist on Object from this time. Discarded.</li>
 * <li>2 -> delta (still valid for 26.11)</li>
 * </ul>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NdTypeComposer {



	public final NdType type;

	/**
	 * <h1>index</h1>
	 * <p>Used for efficient serialization. Live in the [0, 0xff] range.</p>
	 * <ul>
	 * <li>-1: not assigned yet</li>
	 * <li>>=0: assigned.</li>
	 * </ul>
	 */
	public final long typeCode;



	/**
	 * 
	 */
	public final NdFieldComposer[] fieldComposers;


	private boolean isTypeUnpublished = true;



	private class Indexer {
		public int lastFieldCode = 0x00;
	}


	/**
	 * 
	 * @param propertiesMap
	 * @param outflowCode
	 * @param type
	 * @throws NdBuildException 
	 * @throws LithTypeBuildException 
	 * @throws LthSerialException
	 */
	public NdTypeComposer(NdType type, long typeCode) throws NdBuildException {
		super();
		this.type = type;
		this.typeCode = typeCode;

		// compile
		int fieldcount = type.fieldsByName.size();
		this.fieldComposers = new NdFieldComposer[fieldcount];

		Indexer indexer = new Indexer();
		type.fieldsByName.forEach((name, field) -> {
			int code = indexer.lastFieldCode++;
			try {
				fieldComposers[field.ordinal] = field.createComposer(code);
			}
			catch (NdIOException e) {
				e.printStackTrace();
			}	
		});
		if(indexer.lastFieldCode > 0xff) {
			throw new NdBuildException("Field index exceeds 0xFF", type.getBaseType());
		}
	}







	/**
	 * When created
	 * 
	 * @param vertex
	 * @param outflow
	 * @param slot 
	 * @param isExposeUnpublished 
	 * @throws IOException
	 */
	public void publish_CREATE_NODE(ByteOutflow outflow, String index) throws IOException {
		
		/* add type declaration if necessary */
		if(isTypeUnpublished) {
			/* declare Type */
			outflow.putUInt8(BOHR_Keywords.DECLARE_TYPE);

			/* declare type */
			outflow.putStringUTF8(type.getSerialName());

			/* type code */
			outflow.putUInt7x(typeCode);

			isTypeUnpublished = false;
		}

		outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

		/* type code */
		outflow.putUInt7x(typeCode);

		/* define index */
		outflow.putStringUTF8(index);
	}



}
