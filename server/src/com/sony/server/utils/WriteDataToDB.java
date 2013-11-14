/**
 * 数据直接抓取，此文件停止使用
 * By 沈津生
 * 2013.11.14
 */
package com.sony.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.sony.server.entity.PPT;

/**
 * 将数据写入数据库
 * @author Administrator
 *
 */
public class WriteDataToDB {
	
	/**
	 * 写数据
	 * @return
	 */
	public boolean writeData(List<PPT> pptList){
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "INSERT INTO ppt_edit VALUES(?,?,?)";
		
		//打开数据库连接
		try {
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//插入数据
		for(int i = 0;i < pptList.size();i++){
			if(pptList.get(i).getPPT_index() == 0){
				return false;
			} else{
				try {
					ps.setInt(1, pptList.get(i).getPPT_index());
					ps.setString(2, pptList.get(i).getPPT_speechTime());
					ps.setString(3,pptList.get(i).getPPT_comment());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		
		//关闭数据库连接
		try{
			ps.close();
			conn.close();
		} catch(SQLException se){
			se.printStackTrace();
		}
		
		return true;
	}
}
