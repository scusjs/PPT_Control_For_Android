package com.sony.client.utils;

import android.os.Message;

public class MySplit {
	private String operate;
	private String msgFromPC;
	private String text;
	
	
	public MySplit() {
		operate = null;
		msgFromPC = null;
		text = null;
	}
	public MySplit(String str) {
		setP(str);
	}
	public String getOperate() {
		return operate;
	}
	public String getMsgFromPC() {
		return msgFromPC;
	}
	public String getText() {
		return text;
	}
	public void setP(String str) {
		String msgStr = str;
		String stringarray[]=msgStr.split("#");
		operate = stringarray[0];
		msgFromPC = stringarray[1];
		if (operate.equals(Code.FIRST_CONNECTION)) {
			text  = stringarray[2];
		}
	}
	


	
}
