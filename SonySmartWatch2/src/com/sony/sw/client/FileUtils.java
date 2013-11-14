package com.sony.sw.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;

public class FileUtils {
	private String MyPPTData = "";
	
	public FileUtils(List<PPT> list){
		JSONArray array = new JSONArray();
		try {
			for(int i = 0;i < list.size();i++){
				JSONObject object = new JSONObject();
				object.put("ppt_info",list.get(i).toString());
				array.put(object);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyPPTData = array.toString();
		System.out.println("write:" + MyPPTData);
	}
	
	public void saveFile(){
		
		if (Environment.getExternalStorageState().equals( 
				
				Environment.MEDIA_MOUNTED)) { 
				File file_name = null; 
				try { 
				    file_name = new File("/sdcard/" + "Test.txt"); 
					if (!file_name.exists()) { 
						file_name.createNewFile(); 
					} 
					//System.out.println(file_name); 
				} catch (IOException e1) { 
					// TODO Auto-generated catch block 
					e1.printStackTrace(); 
				} 
				try { 
					OutputStream os = new FileOutputStream(file_name); 
					os.write(MyPPTData.getBytes()); 
					os.close(); 
					} catch (final Exception e) { 
						e.printStackTrace(); 
					} 
				}
	}
	
	public void readFile(){
		File file_name = new File("//data//data//com.sony.client//files//test.txt");
		try {
			InputStream fin = new FileInputStream(file_name);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);     
			JSONArray array = new JSONArray(EncodingUtils.getString(buffer, "UTF-8"));
			for(int i = 0;i < array.length();i++){
				JSONObject temp = array.getJSONObject(i);
				String[] arrayTemp = temp.get("ppt_info").toString().split("_");
				//向链表中添加对象，由于这个链表是个类变量，所以可以直接使用
				PPT.pptList.add(new PPT(Integer.parseInt(arrayTemp[0]),arrayTemp[1],arrayTemp[2]));
			}
			System.out.println("read: " + PPT.pptList);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
