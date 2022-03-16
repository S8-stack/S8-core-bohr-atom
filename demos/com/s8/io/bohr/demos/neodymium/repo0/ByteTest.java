package com.s8.io.bohr.demos.neodymium.repo0;

public class ByteTest {

	public static void main(String[] args) {

		byte b = (byte) 0xf5;
		System.out.println(b);
		int val = 0xff & b;
		System.out.println(val);
	}

}
