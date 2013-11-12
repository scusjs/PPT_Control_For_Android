package com.sony.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.sony.client.utils.Code;
import com.sony.client.utils.MySplit;

/**
 * ��PCServer��������
 * 
 * @author Administrator
 * 
 */
public class SendMessageToPCServerThread implements Runnable {

	// private Socket s;
	DatagramSocket s;
	DatagramPacket packet;
	int port = 9876;
	String ip = Code.IPADDRESS;
	// ������UI�̷߳�����Ϣ��Handler����
	private Handler handler;
	// �������UI�̵߳���Ϣ��Handler����
	public Handler revHandler;
	// ���߳��������Socket����Ӧ��������
	BufferedReader br = null;
	OutputStream os = null;
	public Boolean stop = false;

	public SendMessageToPCServerThread(Handler handler) {
		this.handler = handler;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public void setIP(String ip) {
		this.ip = ip;
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				
				Log.e("UDP", "OK1");
				// ����һ�����߳�����ȡ��������Ӧ������
				new Thread() {
					@Override
					public void run() {
						while (!stop) {
							if (stop) {
								return;
							}
							String content = null;
							Log.e("AllInfo", "receive");
							// ���϶�ȡSocket�������е����ݡ�
							try {
								if (s == null) {
									s = new DatagramSocket(null);
									s.setReuseAddress(true);
									s.bind(new InetSocketAddress(port));
								}

								byte data[] = new byte[2048];
								DatagramPacket packet = new DatagramPacket(
										data, data.length);
								s.receive(packet);

								content = (new String(packet.getData(),"gbk")).trim();
								Log.e("All", content);
								Message msg = new Message();
								/*
								 * msg.what = 0x123; msg.obj = content;
								 * handler.sendMessage(msg);
								 */

								MySplit ms = new MySplit(content.toString());
								// ʧ��
								if (ms.getOperate().equals(Code.FAILURE)) {
									msg.what = Code.MSG_REV_FAILURE;
									msg.obj = null;
									handler.sendMessage(msg);
									return;
								}
								if (ms.getOperate().equals(
										Code.FIRST_CONNECTION)) {
									msg.what = Code.MSG_REV_SUCCESS_CONNECTION;
								} else {
									msg.what = Code.MSG_REV_SUCCESS_OPERATEPC;
								}
								msg.obj = content;
								handler.sendMessage(msg);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
				// Ϊ��ǰ�̳߳�ʼ��Looper
				Looper.prepare();
				if (stop) {
					return;
				}
				// ����revHandler����
				revHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// ���յ�UI�߳����û����������
						if (msg.what == Code.MSG_SEND) {
							// ���û����ı��������������д������
							try {
								if (stop) {
									return;
								}
								
								if(s==null){
									s = new DatagramSocket(null);
									s.setReuseAddress(true);
									s.bind(new InetSocketAddress(port));
								}
								Log.e("UDP", "OK");
								InetAddress serverAddress = InetAddress
										.getByName(ip);
								byte data[] = (msg.obj.toString() + "\r\n")
										.getBytes("utf-8");
								packet = new DatagramPacket(data, data.length,
										serverAddress, port);
								s.send(packet);
								msg = null;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				if (stop) {
					return;
				}
				// ����Looper
				Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}