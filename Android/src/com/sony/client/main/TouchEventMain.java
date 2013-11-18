package com.sony.client.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gesture.BuileGestureExt;
import com.sony.client.R;
import com.sony.client.entity.PPT;
import com.sony.client.utils.Code;
import com.sony.client.utils.MySplit;
import com.sony.client.utils.MyThread;

public class TouchEventMain extends Activity {
	String str = "null";
	// SendMessageToPCServerThread sm2pc;
	// Handler handler;
	EditText et;// 备注
	TextView tv_pages;// 页数
	//WakeLock wakeLock = null; //获取电源锁，保持屏幕熄灭时仍然获取CPU时，保持运行
	MySplit mysplit = new MySplit();
	public static int pages = 1;
	String operator = Code.KEY_DOWN;
	PPT ppt;
	private GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//acquireWakeLock();
		setContentView(R.layout.pptinfo);
		View v = findViewById(R.id.layout_pptinfo);
		v.getBackground().setAlpha(100);
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
					/*if (Code.KEY_UP.equals(operator)) {

						
					} else if (Code.KEY_DOWN.equals(operator)) {
						
					}*/
					Log.e("key", String.valueOf(pages));
					ppt = PPT.pptList.get(pages - 1);
					et.setText(ppt.getPPT_comment());
					tv_pages.setText(String.valueOf(ppt.getPPT_index()));
					break;
				}

			}
		};

		
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
			//sendMesage(Code.KEY_DOWN);
			if (pages < PPT.pptList.size()) {
				pages += 1;
			} else {
				Toast.makeText(TouchEventMain.this, "已经到了最后一页了",
						Toast.LENGTH_SHORT).show();
			}
			Log.e("pages", String.valueOf(PPT.pptList.size()));
			ppt = PPT.pptList.get(pages - 1);
			et.setText(ppt.getPPT_comment());
			tv_pages.setText(String.valueOf(ppt.getPPT_index()));
			

		} else if ("3".equals(value)) {
			if (pages > 1) {
				pages -= 1;
			} else {
				Toast.makeText(TouchEventMain.this, "已经到了第一页了",
						Toast.LENGTH_SHORT).show();
			}
			ppt = PPT.pptList.get(pages - 1);
			et.setText(ppt.getPPT_comment());
			tv_pages.setText(String.valueOf(ppt.getPPT_index()));
			
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		// 音量减小
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			sendMesage(Code.KEY_DOWN);
			return true;
			// 音量增大
		case KeyEvent.KEYCODE_VOLUME_UP:
			//sendMesage(Code.KEY_UP);
			sendMesage(Code.KEY_UP);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	private void sendMesage(String strmsg) {
		try {
			Message sendmsg = new Message();
			sendmsg.what = Code.MSG_SEND;
			String string = strmsg;
			operator = strmsg;
			sendmsg.obj = string;
			MyThread.sm2pc.revHandler.sendMessage(sendmsg);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyThread.sm2pc.stop = true;
		//releaseWakeLock();
		//PPT.pptList.removeAll(); 
		
		super.onDestroy();
	}
	


}