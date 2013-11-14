/**
 * 数据直接抓取，此文件与数据库操作无关
 * By 沈津生
 * 2013.11.14
 */
package com.sony.server.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sony.server.entity.PPT;

/**
 * 从数据库中读数据
 * @author Administrator
 *
 */
public class ReadDataFromDB {
	
	private JSONArray array;
	
	public ReadDataFromDB(){
		array = new JSONArray();
	}
	
	/**
	 * 读数据
	 * @return
	 */
	public boolean read(){
		/*Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM ppt_edit";
		
		try{
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				JSONObject objectTemp = new JSONObject();
				objectTemp.put("ppt_info",
						rs.getInt("PPT_index") + "_" + 
						rs.getString("PPT_speechTime") + "_" + 
						rs.getString("PPT_comment") + "_");
				array.put(objectTemp);
			}
				
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}*/
		
		for (int i = 0; i < PPT.pptList.size(); i++) {
			JSONObject objectTemp = new JSONObject();
			PPT ppt = PPT.pptList.get(i);
			try {
				objectTemp.put("ppt_info",
							ppt.getPPT_index() + "_" + 
							ppt.getPPT_speechTime() + "_" + 
							ppt.getPPT_comment() + "_");
				array.put(objectTemp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return true;
	}
	
	public String getSendData(){
		return array.toString();
	}
}
