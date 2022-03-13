package com.s8.io.bohr.tests.neon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestbench00 {

	public static void main(String[] args) {

		Pattern pattern = Pattern.compile("([float|int|\\[0-9*\\]])*");
		Matcher m = pattern.matcher("float[5]");
		
		System.out.println(m.find());
		while(m.find()) {
			int gc = m.groupCount();
			System.out.println(gc);
			
		}
		
		
	}

}
