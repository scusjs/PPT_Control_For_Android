package com.sony.client.entity;

import java.util.LinkedList;
import java.util.List;

/**
 * ppt基本类
 * @author Administrator
 *
 */
public class PPT {
	private int PPT_index = 0;
	private String PPT_speechTime = "";
	private String PPT_comment = "";
	
	//存储编辑好的ppt
	public static List<PPT> pptList = new LinkedList<PPT>();
	
	public PPT(int PPT_index,String PPT_speechTime,String PPT_comment){
		this.PPT_index = PPT_index;
		this.PPT_speechTime = PPT_speechTime;
		this.PPT_comment = PPT_comment;
	}
	
	public int getPPT_index() {
		return PPT_index;
	}

	public void setPPT_index(int pPT_index) {
		PPT_index = pPT_index;
	}

	public String getPPT_speechTime() {
		return PPT_speechTime;
	}

	public void setPPT_speechTime(String pPT_speechTime) {
		PPT_speechTime = pPT_speechTime;
	}

	public String getPPT_comment() {
		return PPT_comment;
	}

	public void setPPT_comment(String pPT_comment) {
		PPT_comment = pPT_comment;
	}

	public String toString(){
		return PPT_index + "#" + PPT_speechTime + "#" + PPT_comment + "#";
	}
}
