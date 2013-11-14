package com.sony.server.action;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.sony.server.main.StartServer;
import com.sony.server.utils.MessageCode;
import com.sony.server.utils.OperateDataFromSmartPhone;
import com.sony.server.utils.PackMessage;

public class GetKeyCodeFromSmartPhone{
	
	
	public GetKeyCodeFromSmartPhone(){}

	public void listenPort(){

		try {
			//需要监听的端口号:9876
			//TODO
			DatagramSocket serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[1024];//接受的信息
					
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			serverSocket.receive(receivePacket);
			
			//输出接受到的信息
			System.out.println((new String(receivePacket.getData())).trim());

			//获得IP地址
			InetAddress IPAddress = receivePacket.getAddress();
			//获得端口号
			int port = receivePacket.getPort();
			
			/*
			 * 开始解析来自手机的转发信息
			 */
			DatagramPacket sendPacket = null;
			OperateDataFromSmartPhone operate = new OperateDataFromSmartPhone();
			
			if(operate.analysis(new String(receivePacket.getData())).equals(MessageCode.OPERATE_PC)){//成功获得信息
				//测试打印信息
				//operate.printMessage();
				//获得信息
				StartServer.action = operate.getReceiveData();//获得操作键盘的值
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.OPERATE_PC, true,IPAddress, port);
				sendPacket = pm.getSendPacket();
			} else if(!operate.analysis(new String(receivePacket.getData())).equals(MessageCode.FIRST_CONNECT)){
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.OPERATE_PC, false, IPAddress, port);
				sendPacket = pm.getSendPacket();
			}
			
			if(operate.analysis(new String(receivePacket.getData())).equals(MessageCode.FIRST_CONNECT)){
				StartServer.action = operate.getReceiveData();//获得操作键盘的值
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.FIRST_CONNECT, true, IPAddress, port);
				sendPacket = pm.getSendPacket();
			} else if(!operate.analysis(new String(receivePacket.getData())).equals(MessageCode.OPERATE_PC)){
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.FIRST_CONNECT, false, IPAddress, port);
				sendPacket = pm.getSendPacket();
			} 

			StartServer.flag = true;
			//向手机发送消息
			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
