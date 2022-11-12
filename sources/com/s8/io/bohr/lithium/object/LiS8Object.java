package com.s8.io.bohr.lithium.object;

import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.lithium.branches.LiVertex;

/**
 * <p>
 * <code>S8Shell</code> represents an enclosure of a graph of S8Object. This
 * shell has multiple role:
 * </p>
 * <ul>
 * <li>#1 LOGICAL UNIT: It defines a logical unit from the application logic
 * point of view</li>
 * <li>#2 SYNCHRONIZATION: It is the primary scope of lock enabling async
 * processes</li>
 * <li>#3 STORAGE UNIT: It acts a persistency bucket (and therefore must be
 * considered the main element to understand the latency of the application
 * implementation.)</li>
 * <li>#4 VERSION UNIT: It is the key building block for substitution pattern,
 * that acts as a repository state.</li>
 * <li>#5 ENCAPSULATION: It is the primary encapsulation control unit. Note that
 * we cannot access (from a logical point of view the inner nodes of the graph
 * encapsulated by the shell, unless they are specifically designated as logical
 * ports of the shell.</li>
 * </ul>
 * <p>
 * Note that <code>S8object</code> objects used as logical ports SHOULD refer
 * the shell (typically as a field).
 * </p>
 * 
 * 
 * 
/**
 * <p>Unpinned object applications:</p>
 * <ul>
 * <li><b>transient</b>: object created on the fly that don't need any S8Shell handling and management</li>
 * <li><b>private</b>: none of <code>S8Struct</code> object method are exposed for direct request</li>
 * <li><b>managed</b>: acess control is non-existent (for access control, upgrade to <code>S8Object</code></li>
 * <li><b>leaves</b>: typical used as leaves of a tree which nodes are <code>S8Object</code></li>
 * </ul>
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
@S8ObjectType(name = "alpha/S8Object", sub= {})
public class LiS8Object {
	
	
	/* <field-properties> */
	
	public final static long REF = 0x0000000000000001L;
	
	
	/* </field-properties> */
	
	
	/**
	 * <h1>DO NOT USE THIS FIELD: SYSTEM ONLY</h1>
	 * <p>
	 * This index acts as an internal identifier and is automatically assigned at
	 * commit time.
	 * </p>
	 */
	public String S8_index = null;


	/**
	 * <h1>id field</h1>
	 * <p>(Do not use this field)</p>
	 * <p>For internal use only</p>
	 */
	public boolean S8_spin = false;
	
	
	
	
	/**
	 * Object listeners
	 */
	public LiVertex S8_vertex;
	
	
	/**
	 * Perform initial binding of object with vertex, vertex itself being bound to its shell
	 * @param graph
	 */
	public LiS8Object() {
	}
	
	
	
	/**
	 * 
	 * @param event
	 */
	public void advertise(long event) {
		if(S8_vertex!=null) { S8_vertex.advertise(event); }
	}
	
	
	

	/**
	 * 
	 * @param slot
	 */
	public void expose(int slot) {
		if(S8_vertex!=null) { S8_vertex.expose(slot); }
	}
	
}
