package com.sony.client.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.sony.client.main.MainActivity;
import com.sony.client.utils.Code;
import com.sony.client.utils.MySplit;

/**
 * 向PCServer发送数据
 * @author Administrator
 *
 */
public class SendMessageToPCServerThread implements Runnable{

	private Socket s;
	// 定义向UI线程发送消息的Handler对象
	private Handler handler;
	// 定义接收UI线程的消息的Handler对象
	public Handler revHandler;
	// 该线程所处理的Socket所对应的输入流
	BufferedReader br = null;
	OutputStream os = null;
	public Boolean stop = false;
	
	public SendMessageToPCServerThread(Handler handler){
		this.handler = handler;
	}
	public void setStop(Boolean stop){
		this.stop = stop;
	}
	
	

	@Override
	public void run()
	{
		try
		{
			s = new Socket("192.168.1.103", 30000);
			br = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
			os = s.getOutputStream();
			// 启动一条子线程来读取服务器响应的数据
			new Thread()
			{
				@Override
				public void run()
				{
					if (stop) {
						return;
					}
					String content = null;
					// 不断读取Socket输入流中的内容。
					try
					{
						while ((content = br.readLine()) != null)
						{
							if (stop) {
								return;
							}
							// 每当读到来自服务器的数据之后，发送消息通知程序界面显示该数据
							Message msg = new Message();
							/*msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);*/
							
							MySplit ms = new MySplit(content.toString());
							//失败
							if (ms.getOperate().equals(Code.FAILURE)) {
								msg.what = Code.MSG_REV_FAILURE;
								msg.obj = null;
								handler.sendMessage(msg);
								return;
							}
							if (ms.getOperate().equals(Code.FIRST_CONNECTION)) {
								msg.what = Code.MSG_REV_SUCCESS_CONNECTION;
							} else {
								msg.what = Code.MSG_REV_SUCCESS_OPERATEPC;
							}
							msg.obj = content;
							handler.sendMessage(msg);
							//return;
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}.start();
			// 为当前线程初始化Looper
			Looper.prepare();
			if (stop) {
				return;
			}
			// 创建revHandler对象
			revHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// 接收到UI线程中用户输入的数据
					if (msg.what == Code.MSG_SEND)
					{
						// 将用户在文本框内输入的内容写入网络
						try
						{
							if (stop) {
								return;
							}
							os.write((msg.obj.toString() + "\r\n")
								.getBytes("utf-8"));
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			};
			if (stop) {
				return;
			}
			//stop = true;
			// 启动Looper
			Looper.loop();
		}
		catch (SocketTimeoutException e1)
		{
			System.out.println("网络连接超时！！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}