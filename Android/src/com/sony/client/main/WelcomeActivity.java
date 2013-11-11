package com.sony.client.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sony.client.R;
import com.sony.client.entity.PPT;
import com.sony.client.network.SendMessageToPCServerThread;
import com.sony.client.utils.AnalysisData;
import com.sony.client.utils.Code;
import com.sony.client.utils.MyThread;

@SuppressLint("ShowToast")
public class WelcomeActivity extends Activity{
	
	private Button bn_aboutUS;
	private Button bn_start_button;
	private Button bn_getAllInfo_button;
	
	
	//SendMessageToPCServerThread sm2pc;
	//Handler handler;
	//Thread thread;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        
        MyThread.handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case Code.MSG_REV_SUCCESS_CONNECTION:
						//sm2pc.stop = true;
						AnalysisData as = new AnalysisData();
						as.analysisDataFromPC(msg.obj.toString());
						//Log.e("key",PPT.pptList.toString());
						Toast.makeText(WelcomeActivity.this, "成功获取全部消息", Toast.LENGTH_SHORT).show();
						//WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this,StartView.class));
						//finish();
				}
			}
		};
        
        init();
    }
    
    public void init(){
    	bn_aboutUS = (Button) findViewById(R.id.bn_aboutUS_button);
    	bn_start_button = (Button) findViewById(R.id.bn_start_button);
    	bn_getAllInfo_button = (Button) findViewById(R.id.bn_getAllInfo_button);
    	
    	bn_aboutUS.setOnClickListener(new MyClickListener());
    	bn_start_button.setOnClickListener(new MyClickListener());
    	bn_getAllInfo_button.setOnClickListener(new MyClickListener());
    	
    	MyThread.sm2pc = new SendMessageToPCServerThread(MyThread.handler);
    	MyThread.thread = new Thread(MyThread.sm2pc);
    	MyThread.thread.start();
    }
    
    private class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.bn_aboutUS_button:
				Toast.makeText(WelcomeActivity.this, "还没做", Toast.LENGTH_SHORT).show();
				break;
			case R.id.bn_start_button:
				if(PPT.pptList.size() == 0) {
					Toast.makeText(WelcomeActivity.this, "请先获取信息再开始演示！", Toast.LENGTH_SHORT).show();
					break;
				}
				WelcomeActivity.this.startActivity(new Intent(WelcomeActivity.this,StartView.class));
				finish();
				break;
			case R.id.bn_getAllInfo_button:
				
				try {
					Message sendmsg = new Message();
					sendmsg.what = Code.MSG_SEND;
					String string = Code.ALLINFO;
					sendmsg.obj = string;
					MyThread.sm2pc.revHandler.sendMessage(sendmsg);
				} catch (Exception e) {
					// TODO: handle exception
				}
				//Toast.makeText(WelcomeActivity.this, "还没做", Toast.LENGTH_SHORT).show();
				break;
			}
		}
    	
    }
}
