package com.s8.io.bohr.neon.core;

import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.S8Branch;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bytes.base64.Base64Generator;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NeBranch extends S8Branch {
	
	
	
	final Map<String, NeObjectPrototype> prototypesByName;
	
	
	/**
	 * 
	 */
	final Map<String, NeObject> objects;

	
	/**
	 * 
	 */
	public long highestObjectId = 0x02L;
	
	public long highestTypeCode = 0x02L;
	
	
	
	public final NeOutbound outbound;
	
	
	private final Base64Generator idxGen;
	
	public NeBranch(String address, String id) {
		super(address, id);
		prototypesByName = new HashMap<>();
		this.objects = new HashMap<>();
		
		
		/* outbound */
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
	
	@Override
	public long getBaseVersion() {
		return 0;
	}


	@Override
	public <T extends S8Object> T access(int port) throws S8Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
	
	/**
	 * 
	 * @param vertex
	 * @return
	 */
	String appendObject(NeObject object) {
		
		String index = createNewIndex();
		
		objects.put(index, object);
		
		outbound.notifyChanged(object);
		
		return index;
	}
	
	

	/**
	 * 
	 * @param index
	 * @return
	 */
	public NeObject getVertex(String index) {
		return objects.get(index);
	}



	/**
	 * 
	 * @param typename
	 * @return
	 */
	public NeObjectPrototype retrieveObjectPrototype(String typename) {
		return prototypesByName.computeIfAbsent(typename, name -> new NeObjectPrototype(name, highestTypeCode++));
	}


}
