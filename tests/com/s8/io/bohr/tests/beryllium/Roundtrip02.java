package com.s8.io.bohr.tests.beryllium;

import java.io.IOException;

import com.s8.io.bohr.beryllium.types.BeTypeBuildException;
import com.s8.io.bohr.demos.beryllium.MyStorageEntry;

public class Roundtrip02 extends Workbench {

	public static void main(String[] args) throws IOException {

		new Roundtrip02().test01();
	}

	public Roundtrip02() throws BeTypeBuildException {
		super(MyStorageEntry.class);
	}
	
	
	public void test01() throws IOException {
		
		/*
		String address = "002134";
		Path path = Path.of("data/table01/");
	
		int nObjects = 100000;
		MyStorageEntry[] objects = new MyStorageEntry[nObjects];
		for(int i=0; i<nObjects; i++) {
			objects[i] = MyStorageEntry.generateRandom();
		}

		
		BerylTable table = createSerie(address, path, MyStorageEntry.class);
		
		MyStorageEntry entry;
		for(int i=0; i<nObjects; i++) {
			entry = objects[i];
			new PutM3Request<Object>(table, entry.id, entry, false).serve();
			if(i % 1000 == 0) {
				System.out.println("Added: "+i);
			}
		}
		
		table.save();

		
		table = new BerylTable(address, path, codebase);
		table.load();

		System.out.println("Check started...");
		for(int i=0; i<nObjects; i++) {
			MyStorageEntry entry0 = objects[i];
			GetM3Request<Object> request = new GetM3Request<Object>(table, entry0.id);
			MyStorageEntry entry1 = (MyStorageEntry) request.getValue();
			if(type.hasDiff(entry0, entry1, true)) {
				System.out.println("Diff!!!");
			}
			else {
				
			}
		}
		System.out.println("Check done!");
		*/
	}


}
