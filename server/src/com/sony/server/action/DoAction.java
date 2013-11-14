package com.sony.server.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class DoAction{
	private Robot robot;
	private int keyCode = 0;
	
	public DoAction(String action){
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		//System.out.println(action);
		setKeyCode(action);
	}

	/**
	 * @param action
	 * ���Ƽ��̲���
	 */
	public void doAction(){
		if(keyCode == 0){
			System.out.println("�޷�ʶ�𰴼�!");
		}else{
			robot.keyPress(keyCode);
			System.out.println(keyCode);
			robot.keyRelease(keyCode);
		}
	}
	
	/**
	 * ��ȡ����
	 * @param action
	 */
	public void setKeyCode(String action){
		if(action.equals("key_UP")){
			keyCode = KeyEvent.VK_UP;
		} else if(action.equals("key_DOWN")){
			keyCode = KeyEvent.VK_DOWN;
		} else if(action.equals("key_FULLSCREEN")){
			keyCode = KeyEvent.VK_F5;
		}
	}
}
