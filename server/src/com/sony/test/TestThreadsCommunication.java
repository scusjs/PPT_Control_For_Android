package com.sony.test;

public class TestThreadsCommunication {
	static boolean flag = false;
	static int i = 0;
	
	static class Thread1 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int a = 0;a < 10;a++){
				System.out.println("1");
			}
			flag = true;
		}
	}
	
	static class Thread2 implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int a = 0;a < 10;a++){
				System.out.println("2");
			}
			flag = false;
		}
	}
	
	public static void main(String[] args){
		Runnable r1 = new Thread1();
		Runnable r2 = new Thread2();
		while(i < 10){
			System.out.println("--" + i + "--");
			if(flag){
				r2.run();
			}
			r1.run();
			i++;
		}
	}
}
