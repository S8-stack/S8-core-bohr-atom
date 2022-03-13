package com.s8.io.bohr.atom;



/**
 * 
 * 
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class S8ShellStructureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -934587181022282422L;


	public final Class<?> target;

	public final String baseAddress;

	public final long baseRevision;

	public final String conflictingAddress;

	public final long conflictingRevision;

	public S8ShellStructureException(Class<?> target, 
			String baseAddress, long baseRevision,
			String breakingAddress, long breakingRevision) {
		super(compose(target, baseAddress, baseRevision, breakingAddress, breakingRevision));
		this.target = target;
		this.baseAddress = baseAddress;
		this.baseRevision = baseRevision;
		this.conflictingAddress = breakingAddress;
		this.conflictingRevision = breakingRevision;
	}

	public static String compose(Class<?> target, String baseAddress, long baseRevision,
			String conflictingAddress, long conflictingRevision) {

		StringBuilder builder = new StringBuilder();
		builder.append("Illegal data structure: Trying to insert object (");
		builder.append(target.getName());
		builder.append(")\n");

		// destination
		builder.append("in shell: (");
		builder.append(baseAddress);
		builder.append(", ");
		builder.append(Long.toHexString(baseRevision));
		builder.append(")\n");

		// destination
		builder.append("while being registered in");
		builder.append(conflictingAddress);
		builder.append(", ");
		builder.append(Long.toHexString(conflictingRevision));
		builder.append(")\n");

		return builder.toString();
	}

}
