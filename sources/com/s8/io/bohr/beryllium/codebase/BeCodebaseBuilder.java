package com.s8.io.bohr.beryllium.codebase;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.Queue;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.annotations.S8RowType;
import com.s8.io.bohr.beryllium.fields.BeFieldFactory;
import com.s8.io.bohr.beryllium.object.BeSerialException;
import com.s8.io.bohr.beryllium.types.BeType;
import com.s8.io.bohr.beryllium.types.BeTypeBuildException;
import com.s8.io.bohr.beryllium.types.BeTypeBuilder;

/**
 * 
 * @author pierreconvert
 *
 */
public class BeCodebaseBuilder {


	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public interface UpperLevel {

		public void pushObjectType(Class<?> type) throws S8BuildException;
	}


	private final UpperLevel upperLevel;

	/**
	 * factory
	 */
	private BeFieldFactory fieldFactory;

	
	private boolean isFinished;

	private boolean isBuildingInProgress;


	/**
	 * 
	 */
	private BeCodebase codebase;


	/**
	 * 
	 */
	private Queue<BeTypeBuilder> queue;


	private boolean isVerbose;

	/**
	 * 
	 */
	public BeCodebaseBuilder(UpperLevel upperLevel, boolean isVerbose) {
		super();


		this.upperLevel = upperLevel;

		// init context
		codebase = new BeCodebase(isVerbose);

		// factory
		fieldFactory = new BeFieldFactory();

		// queue
		queue = new ArrayDeque<BeTypeBuilder>();


		this.isVerbose = isVerbose;
		
		isFinished = false;
	}



	/**
	 * 
	 * @return
	 */
	public BeFieldFactory getFieldFactory() {
		return fieldFactory;
	}




	public void pushTableTypes(Class<?>... types) throws BeTypeBuildException, BeSerialException {
		if(isFinished) {
			throw new BeSerialException("codebase construction process is now terminated");
		}
		for(Class<?> type : types) {
			pushRowType(type);
		}
	}


	/**
	 * 
	 * @param type
	 * @throws BeSerialException 
	 * @throws GphTypeBuildException
	 */
	public void pushRowType(Class<?> type) throws BeTypeBuildException {
		// must not have already been added
		if(!codebase.isTypeKnown(type)) {

			// must be annotated
			if(type.isAnnotationPresent(S8RowType.class)) {

				if(!isAbstract(type)) {
					if(isStatic(type)) {
						queue.add(new BeTypeBuilder(type, isVerbose));			
					}
					else if(isVerbose) {
						System.out.println("Type "+type+" rejected as not static");
					}
				}
			}
			else if(isVerbose) {
				System.out.println("Type "+type+" has not been accepted because of lacking annotation");
			}

			if(!isBuildingInProgress) {
				build();	
			}
		}
	}


	/**
	 * 
	 * @param type
	 * @throws BeTypeBuildException
	 */
	public void pushObjectType(Class<?> type) throws BeTypeBuildException {
		if(isFinished) {
			throw new BeTypeBuildException("codebase construction process is now terminated", type);
		}
		try {
			upperLevel.pushObjectType(type);
		} catch (IOException e) {
			if(isVerbose) {
				e.printStackTrace();	
			}
			throw new BeTypeBuildException(e.getMessage(), type);
		}
	}


	private static boolean isAbstract(Class<?> type) {
		return type.isInterface() || Modifier.isAbstract(type.getModifiers());
	}

	private static boolean isStatic(Class<?> type) {
		return !type.isMemberClass() || (type.isMemberClass() && Modifier.isStatic(type.getModifiers()));
	}


	/**
	 * 
	 * @return
	 * @throws GphSerialException
	 * @throws GphTypeBuildException
	 * @throws BohrTypeBuildingException
	 */
	private void build() throws BeTypeBuildException {

		isBuildingInProgress = true;

		int count = 0;
		BeTypeBuilder typeBuilder;
		while(!queue.isEmpty() && count<65536) {
			typeBuilder = queue.poll();

			typeBuilder.build(this);


			// check that not already built
			if(!codebase.isTypeKnown(typeBuilder.getBaseType())) {
				// type
				typeBuilder.build(this);

				// retrieve type
				BeType tableType = typeBuilder.getType();

				// register
				codebase.put(tableType);			
			}

			count++;
		}

		isBuildingInProgress = false;
	}



	/**
	 * 
	 * @return
	 * @throws BeSerialException
	 */
	public BeCodebase finish() throws BeTypeBuildException {
		if(isFinished) {
			throw new BeTypeBuildException("codebase construction process is now terminated");
		}
		isFinished = true;
		codebase.nTypes = codebase.typesByRuntimeName.size();
		return codebase;
	}

}
