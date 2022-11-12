package com.s8.io.bohr.neodymium.type;

import java.io.IOException;
import java.io.Writer;

import com.s8.io.bohr.atom.S8ShellStructureException;
import com.s8.io.bohr.neodymium.fields.NdField;
import com.s8.io.bohr.neodymium.object.NdObject;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class DebugModule {


	/**
	 * 
	 */
	private final NdType type;


	/**
	 * 
	 * @param type
	 */
	public DebugModule(NdType type) {
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
	public void print(NdObject object, Writer writer) throws IOException, S8ShellStructureException {
		
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
	public void deepCompare(NdObject left, NdObject right, Writer writer) throws IOException, S8ShellStructureException {

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
				writer.write(" index="+(left.S8_index.toString()));
				writer.write(" {");

				// loop through fields
				for(int i2=0; i2<nFields; i2++) {
					NdField field = type.fields[i2];
					if(field.hasDiff(left, right)) {
						writer.append("\n\t");
						field.print(left, writer);
						writer.append("\n\t");
						field.print(right, writer);
						writer.append('\n');	
					}
				}
				writer.write("}\n");		
			}
		}
	}
}
