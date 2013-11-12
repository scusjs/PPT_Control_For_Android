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
 * 向PCServer发送数据
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
	// 定义向UI线程发送消息的Handler对象
	private Handler handler;
	// 定义接收UI线程的消息的Handler对象
	public Handler revHandler;
	// 该线程所处理的Socket所对应的输入流
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
				// 启动一条子线程来读取服务器响应的数据
				new Thread() {
					@Override
					public void run() {
						while (!stop) {
							if (stop) {
								return;
							}
							String content = null;
							Log.e("AllInfo", "receive");
							// 不断读取Socket输入流中的内容。
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
								// 失败
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
				// 为当前线程初始化Looper
				Looper.prepare();
				if (stop) {
					return;
				}
				// 创建revHandler对象
				revHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 接收到UI线程中用户输入的数据
						if (msg.what == Code.MSG_SEND) {
							// 将用户在文本框内输入的内容写入网络
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
				// 启动Looper
				Looper.loop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}