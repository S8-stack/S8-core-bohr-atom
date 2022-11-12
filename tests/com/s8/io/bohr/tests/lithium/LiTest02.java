package com.s8.io.bohr.tests.lithium;

import java.io.OutputStreamWriter;

import com.s8.io.bohr.atom.S8BuildException;
import com.s8.io.bohr.demos.lithium.repo2.MyBuilding;
import com.s8.io.bohr.lithium.branches.LiBranch;
import com.s8.io.bohr.lithium.codebase.LiCodebase;
import com.s8.io.bohr.lithium.codebase.LiCodebaseBuilder;
import com.s8.io.bohr.lithium.codebase.LiCodebaseBuilder.UpperLevel;
import com.s8.io.bytes.linked.LinkedByteInflow;
import com.s8.io.bytes.linked.LinkedByteOutflow;

public class LiTest02 {

	public static void main(String[] args) throws Exception {
		UpperLevel upperLevel = new UpperLevel() {
			
			@Override
			public void pushRowType(Class<?> type) throws S8BuildException {
				// TODO Auto-generated method stub
				
			}
		};
		LiCodebaseBuilder codebaseBuilder = new LiCodebaseBuilder(upperLevel, false);
		codebaseBuilder.pushObjectType(MyBuilding.class);
		LiCodebase codebase = codebaseBuilder.build();
		codebase.DEBUG_print();
		
		MyBuilding building = MyBuilding.create();
		LiBranch branch = new LiBranch("com.toto.123.098", "master", codebase);
		branch.append(building);
		building.expose(2);
		
		
		LinkedByteOutflow outflow = new LinkedByteOutflow(1024);
		branch.push(outflow);
		System.out.println(outflow);
		
		LinkedByteInflow inflow = new LinkedByteInflow(outflow.getHead());
		LiBranch rBranch = new LiBranch("com.toto.123.098", "master", codebase);
		rBranch.pull(inflow);
		System.out.println(rBranch.toString());
		
		//rBranch.print(new OutputStreamWriter(System.out));
		
		OutputStreamWriter writer = new OutputStreamWriter(System.out);
		rBranch.deepCompare(branch, writer);
		writer.close();
	}

}
