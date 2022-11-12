package com.s8.io.bohr.lithium.type;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.lithium.branches.LiVertex;
import com.s8.io.bohr.lithium.fields.LiField;
import com.s8.io.bohr.lithium.object.LiS8Object;


/**
 * 
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 *
 */
public class DebugModule {


	/**
	 * 
	 */
	private final LiType type;


	/**
	 * 
	 * @param type
	 */
	public DebugModule(LiType type) {
		super();
		this.type = type;
	}




	/**
	 * 
	 * @param object
	 * @param writer
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void print(LiS8Object object, Writer writer) throws IOException, S8ShellStructureException {
		
		// advertise class
		writer.write('(');
		writer.write(object.getClass().getCanonicalName());
		writer.write(')');

		// adevrtise index
		writer.write(" index="+object.S8_index.toString());
		writer.write(" {\n");

		// loop through fields
		int nFields = type.fields.length;
		for(int i=0; i<nFields; i++) {
			writer.append('\t');
			type.fields[i].print(object, writer);
			writer.append('\n');
		}
		writer.write("}\n");
	}



	/**
	 * 
	 * @param value
	 * @param inflow
	 * @param scope
	 * @throws IOException
	 * @throws S8ShellStructureException 
	 */
	public void deepCompare(LiS8Object left, LiS8Object right, Writer writer) throws IOException, S8ShellStructureException {

		if(left!=null && right==null) {
			// advertise class
			writer.write('(');
			writer.write(left.getClass().getCanonicalName());
			writer.write(") is now null");
		}
		else if(left==null && right!=null) {
			// advertise class
			writer.write('(');
			writer.write(right.getClass().getCanonicalName());
			writer.write(") is now non-null");
		}
		else {
			boolean hasDiff = !left.getClass().equals(right.getClass());
			int nFields = type.fields.length, i=0;
			while(!hasDiff && i<nFields) {
				hasDiff = type.fields[i].hasDiff(left, right);
				i++;
			}

			if(hasDiff) {
				// advertise class
				writer.write('(');
				writer.write(left.getClass().getCanonicalName());
				writer.write(')');

				// adevrtise index
				writer.write(" index="+((LiVertex) left.S8_vertex).object.S8_index.toString());
				writer.write(" {\n");

				// loop through fields
				for(int i2=0; i2<nFields; i2++) {
					LiField field = type.fields[i2];
					if(field.hasDiff(left, right)) {
						writer.append('\t');
						field.print(left, writer);
						field.print(right, writer);
						writer.append('\n');	
					}
				}
				writer.write("}\n");		
			}
		}
	}
}
