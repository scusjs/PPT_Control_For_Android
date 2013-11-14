package com.sony.test;

public class TestOX {
	public static void main(String[] args){
		int a = 0x1234;
		String b = String.valueOf(a);
		System.out.println(b);
		int c = Integer.parseInt(b);
		System.out.println(a == c);
	}
}
