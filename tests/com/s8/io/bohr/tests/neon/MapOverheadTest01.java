package com.s8.io.bohr.tests.neon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapOverheadTest01 {

	public static void main(String[] args) throws IOException {
		//run01();
		run02();
	}




	public static void run01() throws IOException {
		PseudoObject_Array[] objects = new PseudoObject_Array[N];
		for(int i=0; i<N; i++) { objects[i] = new PseudoObject_Array(); }
		
		
		// warm-up
		for(int j=0; j<1024; j++) {
			for(int i=0; i<N; i++) {
				PseudoObject_Array object = objects[i];
				for(int k=0; k<DATA_STORED.length; k++) {
					object.setValue(CODES[k], DATA_STORED[k]);
				}
				objects[i] = object;
			}
		}
		
		
		int nFills = 1024;
		long t = System.nanoTime();
		for(int j=0; j<nFills; j++) {

			for(int i=0; i<N; i++) {
				PseudoObject_Array object = objects[i];
				for(int k=0; k<DATA_STORED.length; k++) {
					object.setValue(CODES[k], DATA_STORED[k]);
				}
				objects[i] = object;
			}
		}
		t = (System.nanoTime() - t)/(nFills*N);
		System.out.println("Sample speed test: "+t+" ns/ops");

		System.out.println("All PseudoObject_Array objects now built...");
		System.in.read();

		long d = 0;
		for(int i=0; i<N; i++) {
			d += objects[i].fieldValues[1].hashCode();
		}
		System.out.println("d: "+d);

	}


	public static void run02() throws IOException {
		PseudoObject_Map[] objects = new PseudoObject_Map[N];
		for(int i=0; i<N; i++) { objects[i] = new PseudoObject_Map(); }
		
		// warm-up
		for(int j=0; j<1024; j++) {
			for(int i=0; i<N; i++) {
				PseudoObject_Map object = objects[i];
				for(int k=0; k<DATA_STORED.length; k++) {
					object.setValue(NAMES[k], DATA_STORED[k]);
				}
			}
		}

		int nFills = 1024;
		long t = System.nanoTime();
		for(int j=0; j<nFills; j++) {
			for(int i=0; i<N; i++) {
				PseudoObject_Map object = objects[i];
				for(int k=0; k<DATA_STORED.length; k++) {
					object.setValue(NAMES[k], DATA_STORED[k]);
				}
			}
		}
		t = (System.nanoTime() - t)/(nFills*N);
		System.out.println("Sample speed test: "+t+" ns/ops");

		System.out.println("All PseudoObject_Map objects now built...");
		System.in.read();

		long d = 0;
		for(int i=0; i<N; i++) {
			d += objects[i].fieldValues.get(NAMES[0]).hashCode();
		}
		System.out.println("d: "+d);
	}


	public static int N = 65536;

	public static Object[] DATA_STORED = new Object[] {
			"lkj/com.org.toto.classpat.SomePkg.test",
			new double[] { 0.89879, 987.908098, 275.08, 1209.820000e77 },
			new String[] { "alpha", "beta", "gamma", "epsilon", "phi", "delta"},
			"locker/io.toto.classpat.another.test.SuperTest02",
			"lkj/com.org.toto.classpat.SomePkg.test",
			new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			new String[] { 
					"0x000000ff00aabbcc3344", 
					"0x000000ff00aabbcc3366", 
					"0x000000ff00aabbcc3349",
					"0x000000ff00aabbcc3354", 
					"0x000000ff00aabbcc3222",
					"0x000000ff00aabbcc4321",
					"0xa00000ff00aabbcc3344", 
					"0xa00000ff00aabbcc3366", 
					"0xa00000ff00aabbcc3349",
					"0xa00000ff00aabbcc3354", 
					"0xa00000ff00aabbcc3222",
			"0xa00000ff00aabbcc4321"},
			"locker/io.toto.classpat.another.test.SuperTest02"
	};

	public static int[] CODES = new int[] {
			1, 3, 4, 7, 8, 9, 10, 11
	};

	public static String[] NAMES = new String[] {
			"classpath01",
			"vector0",
			"variables_names",
			"classpath1",
			"classpath2",
			"vertex_attributes_indices",
			"sub-objects#1908",
			"data-types"
	};



	public static class PseudoObject_Array {

		Object[] fieldValues = new Object[0x4f];

		public PseudoObject_Array() {
			super();
		}


		public void setValue(int code, Object value) {
			fieldValues[code] = value;
		}

	}


	public static class PseudoObject_Map {

		Map<String, Object> fieldValues = new HashMap<>(8);

		public PseudoObject_Map() {
			super();
		}


		public void setValue(String name, Object value) {
			fieldValues.put(name, value);
		}

	}

}
