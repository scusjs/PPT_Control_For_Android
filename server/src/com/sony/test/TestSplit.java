package com.sony.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestSplit {
	@Test
	public void test(){
		String message = "message#";
		assertEquals("message",splitMessage(message));
	}
	
	public String splitMessage(String message){
		String array[] = message.split("#");
		System.out.println(array[0]);
		return array[0];
	}
}
