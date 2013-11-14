package com.sony.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sony.server.entity.PPT;

public class TestJSON {
	public static void main(String[] args) throws JSONException{
		JSONArray array = new JSONArray();
		for(int i = 0;i < 10;i++){
			JSONObject temp = new JSONObject();
			temp.put("test", i + "_" + i + "_" + i + "_");
			array.put(temp);
		}
		String data = "operatePC" + "#" + "SUCCESS" + "#" + array.toString() + "#";
		System.out.println(data);
		
		String[] array2 = data.trim().split("#");
		System.out.println(array2[0]);
		System.out.println(array2[1]);
		System.out.println(array2[2]);
		
		if(array2[0].equals("operatePC")){
			if(array2[1].equals("SUCCESS")){
				try {
					JSONArray jsonArray = new JSONArray(array2[2]);
					for(int i = 0;i < jsonArray.length();i++){
						JSONObject temp = jsonArray.getJSONObject(i);
						String[] arrayTemp = temp.get("test").toString().split("_");
						//向链表中添加对象，由于这个链表是个类变量，所以可以直接使用
						PPT.pptList.add(new PPT(Integer.parseInt(arrayTemp[0]),arrayTemp[1],arrayTemp[2]));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		for(int i = 0;i < PPT.pptList.size();i++){
			System.out.println(PPT.pptList.get(i));
		}
		
	}
}
