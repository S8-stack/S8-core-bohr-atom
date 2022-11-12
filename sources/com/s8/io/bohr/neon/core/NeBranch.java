package com.s8.io.bohr.neon.core;

import java.util.HashMap;
import java.util.Map;

import com.s8.io.bytes.base64.Base64Generator;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NeBranch {
	
	
	public final String address;
	
	public final String id;
	
	
	final Map<String, NeObjectTypeHandler> prototypesByName;
	
	final Map<Long, NeObjectTypeHandler> prototypesByCode;
	
	
	/**
	 * 
	 */
	final Map<String, NeVertex> vertices;

	
	/**
	 * 
	 */
	public long highestObjectId = 0x02L;
	
	public long highestTypeCode = 0x02L;
	
	
	
	public final NeInbound inbound;
	
	public final NeOutbound outbound;
	
	
	private final Base64Generator idxGen;
	
	public NeBranch(String address, String id) {
		super();
		this.address = address;
		this.id = id;
		
		prototypesByName = new HashMap<>();
		prototypesByCode = new HashMap<>();
		vertices = new HashMap<>();
		
		
		/* outbound */
		this.inbound = new NeInbound(this);
		this.outbound = new NeOutbound();
		
		idxGen = new Base64Generator(id);
	}



	/**
	 * 
	 * @return
	 */
	public String createNewIndex() {
		return idxGen.generate(++highestObjectId);
	}
	

	
	
	/**
	 * 
	 * @param vertex
	 * @return
	 */
	String appendObject(NeVertex object) {
		
		String index = createNewIndex();
		
		vertices.put(index, object);
		
		outbound.notifyChanged(object);
		
		return index;
	}
	
	

	/**
	 * 
	 * @param index
	 * @return
	 */
	public NeVertex getVertex(String index) {
		return vertices.get(index);
	}
	
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public NeObject getObject(String index) {
		NeVertex vertex = vertices.get(index);
		return vertex != null ? vertex.object : null;
	}



	/**
	 * 
	 * @param typename
	 * @return
	 */
	public NeObjectTypeHandler retrieveObjectPrototype(String typename) {
		return prototypesByName.computeIfAbsent(typename, name -> {
			NeObjectTypeHandler proto = new NeObjectTypeHandler(name, highestTypeCode++);
			
			// store by code (so share code with front)
			prototypesByCode.put(proto.code, proto);
			return proto;
		});
	}


}
