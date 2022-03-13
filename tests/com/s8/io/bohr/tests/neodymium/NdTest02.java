package com.s8.io.bohr.tests.neodymium;

import java.io.IOException;
import java.io.OutputStreamWriter;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.atom.S8Object;
import com.s8.io.bohr.demos.repo2.MyBuilding;
import com.s8.io.bohr.neodymium.branches.NdBranch;
import com.s8.io.bohr.neodymium.codebase.NdCodebase;
import com.s8.io.bohr.neodymium.codebase.NdCodebaseBuilder;
import com.s8.io.bohr.neodymium.codebase.NdCodebaseBuilder.UpperLevel;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bytes.linked.LinkedByteInflow;
import com.s8.io.bytes.linked.LinkedByteOutflow;

public class NdTest02 {
	
	
	private static OutputStreamWriter writer;
	private static NdCodebase codebase;

	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		initialize(MyBuilding.class, false);
		
		
		NdBranch origin = new NdBranch(codebase, "com.toto.123.098", "master", 0x24);
		
		NdBranch working = origin.deepClone();
		
	
		MyBuilding building = MyBuilding.create();
		working.change(0x25, new S8Object[] { null, building});
		
		// test copy
		NdBranch testCopy = working.deepClone();
		working.deepCompare(testCopy, writer);
		
		
		origin.commit(working);
		LinkedByteOutflow outflow = new LinkedByteOutflow(1024);
		origin.push(outflow);
		
		LinkedByteInflow inflow = new LinkedByteInflow(outflow.getHead());
		
		
		NdBranch clone = new NdBranch(codebase, "com.toto.123.098", "master", 0x25);
		clone.pull(inflow);
		
		clone.roll();
		
		//rBranch.print(new OutputStreamWriter(System.out));
		
		clone.deepCompare(origin, writer);
		
		terminate();
	}

	
	
	/**
	 * 
	 * @param rootClass
	 * @param isVerbose
	 * @throws NdBuildException
	 */
	private static void initialize(Class<?> rootClass, boolean isVerbose) throws NdBuildException {
		UpperLevel upperLevel = new UpperLevel() {
			public @Override void pushRowType(Class<?> type) throws S8BuildException { }
		};
		NdCodebaseBuilder codebaseBuilder = new NdCodebaseBuilder(upperLevel, false);
		codebaseBuilder.pushObjectType(MyBuilding.class);
		codebase = codebaseBuilder.build();
		
		if(isVerbose) {
			codebase.DEBUG_print();	
		}
		
		writer = new OutputStreamWriter(System.out);
	}
	
	
	private static void terminate() throws IOException {
		writer.close();
	}
	
	
	
}
