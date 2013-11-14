package com.sony.server.utils;

/**
 * 解析来自手机的转发信息
 * @author Administrator
 *
 */
public class OperateDataFromSmartPhone {
	private String operateCode;
	private String receiveData;
	
	public OperateDataFromSmartPhone(){
		operateCode = "";
		receiveData = "";
	}
	
	/**
	 * 解析来自手机的转发信息
	 * @param data 转发信息
	 * @return
	 */
	public String analysis(String data){
		String[] array = data.trim().split("#");
		operateCode = array[0];//第一个值是消息头
		if(operateCode.equals(MessageCode.OPERATE_PC)){
			receiveData = array[1];//第二个是消息内容
			if(receiveData.equals(MessageCode.KEY_UP)||
					receiveData.equals(MessageCode.KEY_DOWN)||
					receiveData.equals(MessageCode.KEY_FULLSCREEN)){
				return MessageCode.OPERATE_PC;
			} else{
				return MessageCode.FAILURE;
			}
		} else if(operateCode.equals(MessageCode.FIRST_CONNECT)){
			receiveData = array[1];//第二个是消息内容
			return MessageCode.FIRST_CONNECT;
		} else {
			return MessageCode.FAILURE;
		}
	}

	/**
	 * 打印解析后的返回信息
	 */
	public void printMessage(){
		System.out.println(operateCode + " " + receiveData);
	}
	
	public String getReceiveData(){
		return receiveData;
	}
}
