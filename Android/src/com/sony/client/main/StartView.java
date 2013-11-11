package com.sony.client.main;

import com.sony.client.R;
import com.sony.client.network.SendMessageToPCServerThread;
import com.sony.client.utils.Code;
import com.sony.client.utils.MyThread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class StartView extends Activity{
	private ImageButton startButton;
	private ProgressDialog progressDialog;
	
	
	//SendMessageToPCServerThread sm2pc;
	//Handler handler;
	//Thread thread;
	
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.start);
	    MyThread.handler = new Handler(){
			public void handleMessage(Message msg){
				switch(msg.what){
					case Code.MSG_REV_SUCCESS_OPERATEPC:
						//sm2pc.stop = true;
						StartView.this.startActivity(new Intent(StartView.this,TouchEventMain.class));
						progressDialog.dismiss();
						finish();
				}
			}
		};
	    //sm2pc = new SendMessageToPCServerThread(handler);
		MyThread.sm2pc.setHandler(MyThread.handler);
		//thread = new Thread(sm2pc);
		//thread.start();
	    Init();
	}
	
	public void Init(){

		progressDialog = new ProgressDialog(StartView.this);
		startButton = (ImageButton) findViewById(R.id.start_startImageButton);
		startButton.setOnClickListener(new ButtonListener());
	}

	private class ButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			progressDialog.setTitle("最大化操作中");
			progressDialog.setMessage("请稍后......");
			progressDialog.show();
			
			//thread.start();
			
			
			
			/**
			 * by SHEN
			 */
			
			try {
				Message sendmsg = new Message();
				sendmsg.what = 0x345;
				String string = Code.KEY_FULLSCREEN;
				sendmsg.obj = string;
				MyThread.sm2pc.revHandler.sendMessage(sendmsg);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
