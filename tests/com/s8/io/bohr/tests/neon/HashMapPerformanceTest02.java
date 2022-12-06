package com.s8.io.bohr.tests.neon;

import java.util.HashMap;
import java.util.Map;

public class HashMapPerformanceTest02 {

	
	
	/**
	 * 8ns per look-up on "small" map. => really fast
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Long> map = new HashMap<String, Long>();
		
		int nKeys = 64;
		long a = 197987, b = 98098, x = 98756;
		String[] keys = new String[nKeys];
		for(int i=0; i<nKeys; i++) {
			String key = Long.toHexString(System.nanoTime());
			keys[i] = key;
			map.put(key, x % 717L);
			x = a * x + b;
		}
		
		int nLookUps = 65536;
		String[] keyTestSet = new String[nLookUps];
		for(int i=0; i<nLookUps; i++) {
			keyTestSet[i] = keys[(int) (nKeys * Math.random())];
		}
		
		
		int nRuns = 2048;
		long sum;
		
		sum = 0;
		for(int r=0; r<nRuns; r++) {
			for(int i=0; i<nLookUps; i++) { sum += map.get(keyTestSet[i]); }
		}
		System.out.println(sum);
		
		long time = System.nanoTime();
		sum = 0;
		for(int r=0; r<nRuns; r++) {
			for(int i=0; i<nLookUps; i++) { sum += map.get(keyTestSet[i]); }
		}
		time = System.nanoTime() - time;
		System.out.println(sum);
		System.out.println("time per look-up os: "+(time)/(nRuns*nLookUps)+" ns");
		
	}

}
