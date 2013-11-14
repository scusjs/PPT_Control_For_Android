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
			//��Ҫ�����Ķ˿ں�:9876
			//TODO
			DatagramSocket serverSocket = new DatagramSocket(9876);
			byte[] receiveData = new byte[1024];//���ܵ���Ϣ
					
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			serverSocket.receive(receivePacket);
			
			//������ܵ�����Ϣ
			System.out.println((new String(receivePacket.getData())).trim());

			//���IP��ַ
			InetAddress IPAddress = receivePacket.getAddress();
			//��ö˿ں�
			int port = receivePacket.getPort();
			
			/*
			 * ��ʼ���������ֻ���ת����Ϣ
			 */
			DatagramPacket sendPacket = null;
			OperateDataFromSmartPhone operate = new OperateDataFromSmartPhone();
			
			if(operate.analysis(new String(receivePacket.getData())).equals(MessageCode.OPERATE_PC)){//�ɹ������Ϣ
				//���Դ�ӡ��Ϣ
				//operate.printMessage();
				//�����Ϣ
				StartServer.action = operate.getReceiveData();//��ò������̵�ֵ
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.OPERATE_PC, true,IPAddress, port);
				sendPacket = pm.getSendPacket();
			} else if(!operate.analysis(new String(receivePacket.getData())).equals(MessageCode.FIRST_CONNECT)){
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.OPERATE_PC, false, IPAddress, port);
				sendPacket = pm.getSendPacket();
			}
			
			if(operate.analysis(new String(receivePacket.getData())).equals(MessageCode.FIRST_CONNECT)){
				StartServer.action = operate.getReceiveData();//��ò������̵�ֵ
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.FIRST_CONNECT, true, IPAddress, port);
				sendPacket = pm.getSendPacket();
			} else if(!operate.analysis(new String(receivePacket.getData())).equals(MessageCode.OPERATE_PC)){
				PackMessage pm = new PackMessage();
				pm.pack(MessageCode.FIRST_CONNECT, false, IPAddress, port);
				sendPacket = pm.getSendPacket();
			} 

			StartServer.flag = true;
			//���ֻ�������Ϣ
			serverSocket.send(sendPacket);
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
