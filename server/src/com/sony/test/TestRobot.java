package com.sony.test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class TestRobot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			// TODO Auto-generated method stub

			Robot robot;
			try {
				robot = new Robot();
				int i = 0;
				while(i < 10){	
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					robot.keyPress(KeyEvent.VK_DOWN);
					//System.out.println(KeyEvent.VK_F5);
					robot.keyRelease(KeyEvent.VK_DOWN);
					i++;
				}
				//robot.keyRelease(KeyEvent.VK_F5);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
