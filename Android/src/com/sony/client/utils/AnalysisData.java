package com.sony.client.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.sony.client.entity.PPT;

public class AnalysisData {
	public void analysisDataFromPC(String data,Context context){
		String[] array = data.trim().split("#");
		if(array[0].equals(MessageCode.FIRST_CONNECT)){
			if(array[1].equals(MessageCode.SUCCESS)){
				try {
					JSONArray jsonArray = new JSONArray(array[2]);
					for(int i = 0;i < jsonArray.length();i++){
						JSONObject temp = jsonArray.getJSONObject(i);
						String[] arrayTemp = temp.get("ppt_info").toString().split("_");
						//向链表中添加对象，由于这个链表是个类变量，所以可以直接使用
						PPT.pptList.add(new PPT(Integer.parseInt(arrayTemp[0]),arrayTemp[1],arrayTemp[2]));
						//System.out.println(PPT.pptList.toString());
						
					}
					FileUtilsForMemory fu = new FileUtilsForMemory(PPT.pptList,context);
					fu.saveFile();
					fu.readFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{
				//TODO 失败处理
			}
		}else if(array[0].equals(MessageCode.OPERATE_PC)){
			if(array[1].equals(MessageCode.SUCCESS)){
				
			} else{
				//TODO 失败处理
			}
		}
	}
}
