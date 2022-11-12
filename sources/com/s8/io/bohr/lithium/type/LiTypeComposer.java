package com.s8.io.bohr.lithium.type;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Keywords;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.exceptions.LiIOException;
import com.s8.io.bohr.lithium.fields.LiFieldComposer;
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
public class LiTypeComposer {



	public final LiType type;

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
	public final LiFieldComposer[] fieldComposers;


	private boolean isTypeUnpublished = true;



	private class Indexer {
		public int lastFieldCode = 0x00;
	}


	/**
	 * 
	 * @param propertiesMap
	 * @param outflowCode
	 * @param type
	 * @throws LiBuildException 
	 * @throws LithTypeBuildException 
	 * @throws LthSerialException
	 */
	public LiTypeComposer(LiType type, long typeCode) throws LiBuildException {
		super();
		this.type = type;
		this.typeCode = typeCode;

		// compile
		int fieldcount = type.fieldsByName.size();
		this.fieldComposers = new LiFieldComposer[fieldcount];

		Indexer indexer = new Indexer();
		type.fieldsByName.forEach((name, field) -> {
			int code = indexer.lastFieldCode++;
			try {
				fieldComposers[field.ordinal] = field.createComposer(code);
			}
			catch (LiIOException e) {
				e.printStackTrace();
			}	
		});
		if(indexer.lastFieldCode > 0xff) {
			throw new LiBuildException("Field index exceeds 0xFF", type.getBaseType());
		}
	}


	


	/**
	 * 
	 * @param outflow
	 * @param index
	 * @throws IOException
	 */
	public void publish_CREATE_NODE(ByteOutflow outflow, String index) throws IOException {

		if(isTypeUnpublished) {
			
			/* keyword */
			outflow.putUInt8(BOHR_Keywords.DECLARE_TYPE);

			/* declare type */
			outflow.putStringUTF8(type.getSerialName());

			/* type code */
			outflow.putUInt7x(typeCode);

			isTypeUnpublished = false;
		}

		/* create NODE */
		outflow.putUInt8(BOHR_Keywords.CREATE_NODE);

		/* declare type */
		outflow.putUInt7x(typeCode);

		/* define index */
		outflow.putStringUTF8(index);

	}




	/**
	 * 
	 * @param outflow
	 * @param index
	 * @throws IOException
	 */
	public void publish_UPDATE_NODE(ByteOutflow outflow, String index) throws IOException {

		/* pass flag */
		outflow.putUInt8(BOHR_Keywords.UPDATE_NODE);

		/* pass index */
		outflow.putStringUTF8(index);
	}



	/**
	 * 
	 * @param outflow
	 * @param index
	 * @param slot
	 * @throws IOException
	 */
	public void publish_EXPOSE_NODE(ByteOutflow outflow, String index, int slot) throws IOException {

		/* UPDATE_AND_EXPOSE_NODE */
		outflow.putUInt8(BOHR_Keywords.EXPOSE_NODE);

		/* pass index */
		outflow.putStringUTF8(index);

		/* pass index */
		outflow.putUInt8(slot);	
	}




	/**
	 * Close Node
	 */
	public void publishCloseNode(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Keywords.CLOSE_NODE);
	}
}
