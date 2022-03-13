package com.s8.io.bohr.neon.web;

import com.s8.io.bytes.linked.LinkedByteInflow;
import com.s8.io.bytes.linked.LinkedBytes;


/**
 * 
 * @author pierreconvert
 *
 */
public class LinkedNeInflow extends LinkedByteInflow implements NeInflow {

	public LinkedNeInflow(LinkedBytes head) {
		super(head);
	}

}
