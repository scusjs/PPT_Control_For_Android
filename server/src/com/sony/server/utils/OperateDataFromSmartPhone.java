package com.sony.server.utils;

/**
 * ���������ֻ���ת����Ϣ
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
	 * ���������ֻ���ת����Ϣ
	 * @param data ת����Ϣ
	 * @return
	 */
	public String analysis(String data){
		String[] array = data.trim().split("#");
		operateCode = array[0];//��һ��ֵ����Ϣͷ
		if(operateCode.equals(MessageCode.OPERATE_PC)){
			receiveData = array[1];//�ڶ�������Ϣ����
			if(receiveData.equals(MessageCode.KEY_UP)||
					receiveData.equals(MessageCode.KEY_DOWN)||
					receiveData.equals(MessageCode.KEY_FULLSCREEN)){
				return MessageCode.OPERATE_PC;
			} else{
				return MessageCode.FAILURE;
			}
		} else if(operateCode.equals(MessageCode.FIRST_CONNECT)){
			receiveData = array[1];//�ڶ�������Ϣ����
			return MessageCode.FIRST_CONNECT;
		} else {
			return MessageCode.FAILURE;
		}
	}

	/**
	 * ��ӡ������ķ�����Ϣ
	 */
	public void printMessage(){
		System.out.println(operateCode + " " + receiveData);
	}
	
	public String getReceiveData(){
		return receiveData;
	}
}
