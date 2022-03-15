package com.s8.io.bohr.neodymium.codebase;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.annotations.S8ObjectType;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.fields.NdFieldFactory;
import com.s8.io.bohr.neodymium.type.NdTypeBuilder;

/**
 *
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 */
public class NdCodebaseBuilder {
	
	
	public interface UpperLevel {
		
		public void pushRowType(Class<?> type) throws S8BuildException;
		
	}

	
	private final UpperLevel upperLevel;

	/**
	 * factory
	 */
	private NdFieldFactory fieldFactory;
	

	/**
	 * 
	 */
	private NdCodebase codebase;


	/**
	 * 
	 */
	private Queue<NdTypeBuilder> queue;

	
	private boolean isFinished;
	private boolean isBuildingInProgress;

	private boolean isVerbose;

	/**
	 * 
	 */
	public NdCodebaseBuilder(UpperLevel upperLevel, boolean isVerbose) {
		super();

		
		this.upperLevel = upperLevel;
		
		// init context
		codebase = new NdCodebase(isVerbose);
		
		// factory
		fieldFactory = new NdFieldFactory();
		
		// queue
		queue = new ArrayDeque<NdTypeBuilder>();


		this.isVerbose = isVerbose;
		
		isBuildingInProgress = false;
	}



	/**
	 * 
	 * @return
	 */
	public NdFieldFactory getGphFieldFactory() {
		return fieldFactory;
	}
	


	public void pushObjectTypes(Class<?>... types) throws NdBuildException {
		if(isFinished) {
			throw new NdBuildException("codebase construction process is now terminated");
		}
		
		for(Class<?> type : types) {
			pushObjectType(type);
		}
	}
	
	

	/**
	 * 
	 * @param type
	 * @throws LithTypeBuildException
	 * @throws LthSerialException 
	 */
	public void pushObjectType(Class<?> type) throws NdBuildException {
		if(isFinished) {
			throw new NdBuildException("codebase construction process is now terminated");
		}
		
		// must not have already been added
		if(!codebase.isTypeKnown(type)) {
			
			// must be annotated
			if(type.isAnnotationPresent(S8ObjectType.class)) {
				queue.add(new NdTypeBuilder(type, isVerbose));
			}
			else if(isVerbose) {
				System.out.println("Type "+type+" has not been accepted because of lacking annotation");
			}	
		}
		
		if(!isBuildingInProgress) {
			buildBuffered();
		}
	}
	

	public void pushRowType(Class<?> type) throws NdBuildException {
		if(isFinished) {
			throw new NdBuildException("codebase construction process is now terminated");
		}
		
		try {
			upperLevel.pushRowType(type);
		} 
		catch (IOException e) {
			new NdBuildException(e.getMessage(), type);
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws LthSerialException
	 * @throws LithTypeBuildException
	 * @throws BohrTypeBuildingException
	 */
	private void buildBuffered() throws NdBuildException {

		isBuildingInProgress = true;

		int count = 0;
		NdTypeBuilder typeBuilder;
		while(!queue.isEmpty() && count<65536) {
			typeBuilder = queue.poll();

		
			// check that not already built
			if(!codebase.isTypeKnown(typeBuilder.getRawType())) {
				
				// type
				boolean isBuildable = typeBuilder.process(this);

				// register
				if(isBuildable) { codebase.put(typeBuilder.build()); }
			}

			count++;
		}
		
		isBuildingInProgress = false;
	}
	
	
	public NdCodebase build() throws NdBuildException {
		if(isFinished) {
			throw new NdBuildException("codebase construction process is already terminated");
		}
		codebase.nTypes = codebase.typesByRuntimeName.size();
		isFinished = true;
		return codebase;
	}
}
