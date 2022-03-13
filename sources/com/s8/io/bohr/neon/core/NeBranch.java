package com.s8.io.bohr.neon.core;

import java.util.HashMap;
import java.util.Map;

import com.s8.io.bohr.atom.S8Branch;
import com.s8.io.bohr.atom.S8Exception;
import com.s8.io.bohr.atom.S8Index;
import com.s8.io.bohr.atom.S8Object;

/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class NeBranch extends S8Branch {
	
	
	
	final Map<String, NeObjectPrototype> prototypes;
	
	
	/**
	 * 
	 */
	final Map<S8Index, NeObject> objects;

	
	/**
	 * 
	 */
	public long highestObjectId = 0x02L;
	
	public long highestTypeCode = 0x02L;
	
	
	public NeBranch(String address, String id) {
		super(address, id);
		prototypes = new HashMap<>();
		this.objects = new HashMap<>();
	}



	/**
	 * 
	 * @return
	 */
	public S8Index createNewIndex() {
		return new S8Index(id, ++highestObjectId);
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
	S8Index appendObject(NeObject object) {
		S8Index index = createNewIndex();
		objects.put(index, object);
		return index;
	}
	
	

	/**
	 * 
	 * @param index
	 * @return
	 */
	public NeObject getVertex(S8Index index) {
		return objects.get(index);
	}



	/**
	 * 
	 * @param typename
	 * @return
	 */
	public NeObjectPrototype getObjectPrototype(String typename) {
		return prototypes.computeIfAbsent(typename, name -> new NeObjectPrototype(name, highestTypeCode++));
	}


}
