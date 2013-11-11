package com.sony.client.utils;

/**
 * 消息头
 * @author Administrator
 *
 */
public class Code {
	public static final String SUCCESS = "SUCCESS";//传递成功
	public static final String FAILURE = "FAILURE";//传递失败
	public static final String KEY_UP  = "operatePC#key_UP#";
	public static final String KEY_DOWN  = "operatePC#key_DOWN#";
	public static final String KEY_FULLSCREEN  = "operatePC#key_FULLSCREEN#";
	public static final String ALLINFO  = "firstConnect#ALLINFO#";
	public static final String FIRST_CONNECTION = "firstConnect";
	public static final int MSG_SEND = 0x345;//发送消息
	public static final int MSG_REV_FAILURE = 0x111;//服务器返回错误时消息
	public static final int MSG_REV_SUCCESS_CONNECTION = 0x222;//服务器返回正确时消息
	public static final int MSG_REV_SUCCESS_OPERATEPC = 0x333;//服务器返回正确时消息
	
	
	
}