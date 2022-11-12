package com.s8.io.bohr.tests.neodymium;

import java.io.IOException;
import java.io.OutputStreamWriter;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.demos.neodymium.repo2.MyBuilding;
import com.s8.io.bohr.neodymium.branch.NdBranch;
import com.s8.io.bohr.neodymium.codebase.NdCodebase;
import com.s8.io.bohr.neodymium.codebase.NdCodebaseBuilder;
import com.s8.io.bohr.neodymium.codebase.NdCodebaseBuilder.UpperLevel;
import com.s8.io.bohr.neodymium.exceptions.NdBuildException;
import com.s8.io.bohr.neodymium.object.NdObject;
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
		
		
		NdBranch originBranch = new NdBranch(codebase, "com.toto.123.098", "master", 0x24);
		
		NdBranch workingBranch = originBranch.deepClone();
		
	
		MyBuilding building = MyBuilding.create();
		workingBranch.update(0x25, new NdObject[] { null, building});
		
		// test copy
		NdBranch testCopy = workingBranch.deepClone();
		workingBranch.deepCompare(testCopy, writer);
		
		
		originBranch.commit(workingBranch);
		LinkedByteOutflow outflow = new LinkedByteOutflow(1024);
		originBranch.push(outflow);
		
		LinkedByteInflow inflow = new LinkedByteInflow(outflow.getHead());
		
		
		NdBranch branchClone = new NdBranch(codebase, "com.toto.123.098", "master", 0x25);
		branchClone.pull(inflow);
		
		branchClone.roll();
		
		//rBranch.print(new OutputStreamWriter(System.out));
		
		branchClone.deepCompare(originBranch, writer);
		
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
