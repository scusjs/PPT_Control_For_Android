package com.sony.client.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gesture.BuileGestureExt;
import com.sony.client.R;
import com.sony.client.entity.PPT;
import com.sony.client.network.SendMessageToPCServerThread;
import com.sony.client.utils.Code;
import com.sony.client.utils.MySplit;
import com.sony.client.utils.MyThread;

public class TouchEventMain extends Activity {
	String str = "null";
	//SendMessageToPCServerThread sm2pc;
	//Handler handler;
	EditText et;// 备注
	TextView tv_pages;// 页数
	MySplit mysplit = new MySplit();
	public static int pages = 1;
	String operator = Code.KEY_DOWN;
	PPT ppt;
	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pptinfo);
		ppt = PPT.pptList.get(0);
		et = (EditText) findViewById(R.id.main_comment_textView);
		tv_pages = (TextView) findViewById(R.id.main_index_textView);
		et.setText(ppt.getPPT_comment());
		tv_pages.setText(String.valueOf(ppt.getPPT_index()));
		// et.setText(PPT.pptList.toString());
		MyThread.handler = new Handler() {
			public void handleMessage(Message msg) {
				// 操作成功
				switch (msg.what) {
				case Code.MSG_REV_SUCCESS_OPERATEPC:

					Log.e("msg", String.valueOf(msg.what));
					if (Code.KEY_UP.equals(operator)) {
						
						if (pages > 1) {
							pages -= 1;
						} else {
							Toast.makeText(TouchEventMain.this,"已经到了第一页了", Toast.LENGTH_SHORT).show();
						}
					} else if (Code.KEY_DOWN.equals(operator)) {
						if (pages  < PPT.pptList.size()) {
							pages += 1;
						} else {
							Toast.makeText(TouchEventMain.this,"已经到了最后一页了", Toast.LENGTH_SHORT).show();
						}
					}
					Log.e("key", String.valueOf(pages));
					ppt = PPT.pptList.get(pages - 1);
					et.setText(ppt.getPPT_comment());
					tv_pages.setText(String.valueOf(ppt.getPPT_index()));
					break;
				}

			}
		};

		/*if (MyThread.sm2pc.stop) {
			MyThread.sm2pc.stop = false;
			//MyThread.sm2pc = new SendMessageToPCServerThread(MyThread.handler);
	    	MyThread.thread = new Thread(MyThread.sm2pc);
	    	MyThread.thread.start();
		}*/
		//sm2pc = new SendMessageToPCServerThread(handler);
		//new Thread(sm2pc).start();
		MyThread.sm2pc.setHandler(MyThread.handler);
		gestureDetector = new BuileGestureExt(this,
				new BuileGestureExt.OnGestureResult() {
					@Override
					public void onGestureResult(int direction) {
						show(Integer.toString(direction));
					}
				}).Buile();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	private void show(String value) {

		if ("2".equals(value)) {
			try {
				Message sendmsg = new Message();
				sendmsg.what = Code.MSG_SEND;
				String string = Code.KEY_DOWN;
				operator = Code.KEY_DOWN;
				sendmsg.obj = string;
				MyThread.sm2pc.revHandler.sendMessage(sendmsg);
				// Toast.makeText(MainActivity.this, "下一页",
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else if ("3".equals(value)) {

			try {
				Message sendmsg = new Message();
				sendmsg.what = Code.MSG_SEND;
				String string = Code.KEY_UP;
				operator = Code.KEY_UP;
				sendmsg.obj = string;
				MyThread.sm2pc.revHandler.sendMessage(sendmsg);
				// Toast.makeText(MainActivity.this, "上一页",
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyThread.sm2pc.stop = true;
		super.onDestroy();
	}

}