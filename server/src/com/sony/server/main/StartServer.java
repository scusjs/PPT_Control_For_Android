package com.sony.server.main;

import com.sony.server.action.DoAction;
import com.sony.server.action.GetKeyCodeFromSmartPhone;

public class StartServer {
	public static boolean flag = false;
	public static String action = "";
	
	public static void startServer() throws InterruptedException{
		while(true){
			if(flag){
				DoAction da = new DoAction(action);
				da.doAction();
				Thread.sleep(1000);
				flag = false;
			}else{
				GetKeyCodeFromSmartPhone gk = new GetKeyCodeFromSmartPhone();
				gk.listenPort();
				//flag = true;
			}
		}
		
	}
}
