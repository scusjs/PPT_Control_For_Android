package com.sony.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.sony.client.entity.PPT;

public class FileUtilsForMemory {
	
	private String MyPPTData = "";
	private Context mContext;
	
	public FileUtilsForMemory(List<PPT> list,Context mContext){
		this.mContext = mContext;
		
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
	
	public void saveFile() throws IOException{
		File file = new File("//data//data//com.sony.client//files//test.txt");
		System.out.println(file.exists());
		if(file.exists()){
			file.delete();
		} else {
			FileOutputStream fos = (FileOutputStream)mContext.openFileOutput(
					"test.txt", Context.MODE_PRIVATE + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
			fos.write(MyPPTData.getBytes());
			fos.close();
		}
		System.out.println(mContext.getFilesDir());
	}
	
	public void readFile() throws Exception{
		FileInputStream fis = mContext.openFileInput("//data//data//com.sony.client//files//test.txt");
		byte buffer[] = new byte[1024];
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		int len;
		while((len=fis.read(buffer))!=-1){
		    bos.write(buffer, 0, len);
		}
		fis.close();
		bos.close();
		System.out.println("read: " + new String(bos.toByteArray()));
	}
}
