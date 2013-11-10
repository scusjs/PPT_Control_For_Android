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
 * ��PCServer��������
 * @author Administrator
 *
 */
public class SendMessageToPCServerThread implements Runnable{

	private Socket s;
	// ������UI�̷߳�����Ϣ��Handler����
	private Handler handler;
	// �������UI�̵߳���Ϣ��Handler����
	public Handler revHandler;
	// ���߳��������Socket����Ӧ��������
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
			// ����һ�����߳�����ȡ��������Ӧ������
			new Thread()
			{
				@Override
				public void run()
				{
					if (stop) {
						return;
					}
					String content = null;
					// ���϶�ȡSocket�������е����ݡ�
					try
					{
						while ((content = br.readLine()) != null)
						{
							if (stop) {
								return;
							}
							// ÿ���������Է�����������֮�󣬷�����Ϣ֪ͨ���������ʾ������
							Message msg = new Message();
							/*msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);*/
							
							MySplit ms = new MySplit(content.toString());
							//ʧ��
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
			// Ϊ��ǰ�̳߳�ʼ��Looper
			Looper.prepare();
			if (stop) {
				return;
			}
			// ����revHandler����
			revHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					// ���յ�UI�߳����û����������
					if (msg.what == Code.MSG_SEND)
					{
						// ���û����ı��������������д������
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
			// ����Looper
			Looper.loop();
		}
		catch (SocketTimeoutException e1)
		{
			System.out.println("�������ӳ�ʱ����");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}