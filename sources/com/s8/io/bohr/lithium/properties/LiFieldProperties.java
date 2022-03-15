package com.s8.io.bohr.lithium.properties;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.annotations.S8Field;
import com.s8.io.bohr.atom.annotations.S8Getter;
import com.s8.io.bohr.atom.annotations.S8Setter;
import com.s8.io.bohr.lithium.exceptions.LiBuildException;
import com.s8.io.bohr.lithium.fields.EmbeddedTypeNature;
import com.s8.io.bohr.lithium.fields.LiFieldPrototype;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public abstract class LiFieldProperties {

	
	
	private LiFieldPrototype prototype;


	public final static int FIELD = 0x02, METHODS = 0x04;

	private final int mode; 

	/**
	 * 
	 */
	private boolean isNameDefined = false;

	/**
	 * 
	 */
	private String name;



	private boolean isFlowDefined = false;

	/**
	 * 
	 */
	private String flow;


	private boolean isMaskDefined = false;


	/**
	 * 
	 */
	private long mask;


	private boolean isFlagsDefined = false;

	/**
	 * 
	 */
	private long flags;



	public abstract EmbeddedTypeNature getEmbeddedTypeNature();
	
	

	public abstract Class<?> getEmbeddedType();

	public abstract Class<?> getBaseType();

	public abstract Class<?> getParameterType1();

	public abstract Class<?> getParameterType2();



	/**
	 * 
	 * @param mode
	 */
	public LiFieldProperties(LiFieldPrototype prototype, int mode) {
		super();
		this.prototype = prototype;
		this.mode = mode;
	}




	/**
	 * 
	 * @return
	 */
	public LiFieldPrototype getPrototype() {
		return prototype;
	}


	/**
	 * 
	 * @return
	 */
	public int getMode() {
		return mode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
		this.isNameDefined = true;
	}



	public String getFlow() {
		return flow;
	}


	public void setFlow(String flow) {
		this.flow = flow;
		this.isFlowDefined = true;
	}



	public long getMask() {
		return mask;
	}


	public void setMask(long mask) {
		this.mask = mask;
		this.isMaskDefined = true;
	}



	public long getFlags() {
		return flags;
	}


	public void setFlags(long flags) {
		this.flags = flags;
		this.isFlagsDefined = true;
	}




	/**
	 * 
	 * @param right
	 * @throws S8BuildException 
	 */
	public void merge(LiFieldProperties right) throws LiBuildException {
		
		if(mode != right.mode) {
			throw new LiBuildException("Cannot mix FIELD and GETTER_SETTER_PARI approach");
		}
		
		if(!getBaseType().equals(right.getBaseType())) {
			throw new LiBuildException("Base type discrepancy: "+getBaseType()+" <-> "+right.getBaseType());
		}
		
		if(!getParameterType1().equals(right.getParameterType1())) {
			throw new LiBuildException("<T1> type discrepancy: "+getParameterType1()+" <-> "+right.getParameterType1());
		}
		
		if(!getParameterType2().equals(right.getParameterType2())) {
			throw new LiBuildException("<T2> type discrepancy: "+getParameterType2()+" <-> "+right.getParameterType2());
		}
	

		// name
		if(!isNameDefined && right.isNameDefined) {
			setName(right.name);
		}
		else if(isNameDefined && right.isNameDefined && !name.equals(right.name)) {
			throw new LiBuildException("<name> discrepancy: "+name+" <-> "+right.name);
		}

		// flow
		if(!isFlowDefined && right.isFlowDefined) {
			setFlow(right.name);
		}
		else if(isFlowDefined && right.isFlowDefined && !flow.equals(right.flow)) {
			throw new LiBuildException("<flow> discrepancy: "+flow+" <-> "+right.flow);
		}

		// mask
		if(!isMaskDefined && right.isMaskDefined) {
			setMask(right.mask);
		}
		else if(isMaskDefined && right.isMaskDefined && (mask != right.mask)) {
			throw new LiBuildException("<mask> discrepancy: "+mask+" <-> "+right.mask);
		}

		// flags
		if(!isFlagsDefined && right.isFlagsDefined) {
			setFlags(right.flags);
		}
		else if(isFlagsDefined && right.isFlagsDefined && (flags != right.flags)) {
			throw new LiBuildException("<flags> discrepancy: "+flags+" <-> "+right.flags);
		}
	}





	/**
	 * 
	 * @param annotation
	 * @return
	 * @throws LiBuildException 
	 */
	public void setFieldAnnotation(S8Field annotation) {
		setName(annotation.name());
		setFlow(annotation.flow());
		setMask(annotation.mask());
		setFlags(annotation.props());
	}


	/**
	 * 
	 * @param annotation
	 * @return
	 * @throws LiBuildException 
	 */
	public void setGetterAnnotation(S8Getter annotation) {
		setName(annotation.name());
		setFlow(annotation.flow());
		setMask(annotation.mask());
		setFlags(annotation.flags());
	}

	/**
	 * 
	 * @param annotation
	 * @return
	 * @throws LiBuildException 
	 */
	public void setSetterAnnotation(S8Setter annotation) {
		setName(annotation.name());
	}

}
