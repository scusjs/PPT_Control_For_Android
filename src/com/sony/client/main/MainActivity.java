package com.sony.client.main;
 
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sony.client.R;
import com.sony.client.network.SendMessageToPCServerThread;
import com.sony.client.utils.Code;
import com.sony.client.utils.MySplit;
 
public class MainActivity extends Activity {
 
        private ViewPager viewPager;//页卡内容
        private ImageView imageView;// 动画图片
        private TextView pptInfo,operate;
        private List<View> views;// Tab页面列表
        private int offset = 0;// 动画图片偏移量
        private int currIndex = 0;// 当前页卡编号
        private int bmpW;// 动画图片宽度
        private View view1,view2;//各个页卡
        String str = "null";
        SendMessageToPCServerThread sm2pc;
    	Handler handler;
    	EditText et;//备注
    	TextView tv_pages;//页数
    	MySplit mysplit = new MySplit();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.main_viewpager);
                
                handler = new Handler(){
        			public void handleMessage(Message msg){
        				//str = msg.obj.toString();
        				//mysplit.setP(msg);
        			}
        		};              
        		sm2pc = new SendMessageToPCServerThread(handler);
        		new Thread(sm2pc).start();
                InitImageView();
                InitTextView();
                InitViewPager();
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT);
        		
        }
 
        private void InitViewPager() {
                viewPager=(ViewPager) findViewById(R.id.vPager);
                views=new ArrayList<View>();
                LayoutInflater inflater=getLayoutInflater();
                view1=inflater.inflate(R.layout.pptinfo, null);
                view2=inflater.inflate(R.layout.operate, null);
                views.add(view1);
                views.add(view2);
                viewPager.setAdapter(new MyViewPagerAdapter(views));
                viewPager.setCurrentItem(0);
                viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
                
        		
                
        }
         /**
          *  初始化头标
          */
 
        private void InitTextView() {
        		pptInfo = (TextView) findViewById(R.id.ppt_info);
                operate = (TextView) findViewById(R.id.operate);
 
                pptInfo.setOnClickListener(new MyOnClickListener(0));
                operate.setOnClickListener(new MyOnClickListener(1));
                
        }
 
        /**
         2      * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
         3 */
 
        private void InitImageView() {
                imageView= (ImageView) findViewById(R.id.cursor);
                bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenW = dm.widthPixels;// 获取分辨率宽度
                offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
                Matrix matrix = new Matrix();
                matrix.postTranslate(offset, 0);
                imageView.setImageMatrix(matrix);// 设置动画初始位置
        }

        /** 
         *     
         * 头标点击监听 3 */
        private class MyOnClickListener implements OnClickListener{
        private int index=0;
        public MyOnClickListener(int i){
                index=i;
        }
                public void onClick(View v) {
                        viewPager.setCurrentItem(index);                        
                }
                 
        }
         
        public class MyViewPagerAdapter extends PagerAdapter{
                private List<View> mListViews;
                 
                public MyViewPagerAdapter(List<View> mListViews) {
                        this.mListViews = mListViews;
                }
 
                @Override
                public void destroyItem(ViewGroup container, int position, Object object)         {        
                        container.removeView(mListViews.get(position));
                }
 
 
                @Override
                public Object instantiateItem(ViewGroup container, int position) {                        
                         container.addView(mListViews.get(position), 0);
                         return mListViews.get(position);
                }
 
                @Override
                public int getCount() {                        
                        return  mListViews.size();
                }
                 
                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {                        
                        return arg0==arg1;
                }
        }
 
    public class MyOnPageChangeListener implements OnPageChangeListener{
 
            int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
            int two = one * 2;// 页卡1 -> 页卡3 偏移量
                public void onPageScrollStateChanged(int arg0) {
                         
                         
                }
 
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                         
                         
                }
 
                public void onPageSelected(int arg0) {
                        Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//显然这个比较简洁，只有一行代码。
                        currIndex = arg0;
                        animation.setFillAfter(true);// True:图片停在动画结束位置
                        animation.setDuration(300);
                        imageView.startAnimation(animation);
                        Toast.makeText(MainActivity.this, "您选择了"+ viewPager.getCurrentItem()+"页卡", Toast.LENGTH_SHORT).show();
                        if (viewPager.getCurrentItem() == 0) {
                        	
							/*if (mysplit.getMsgFromPC().equals(Code.SUCCESS)) {
								et = (EditText) findViewById(R.id.main_comment_textView);
								et.setText(mysplit.getText());
								tv_pages = (TextView) findViewById(R.id.main_index_textView);
								tv_pages.setText(mysplit.getPages());
							}*/
						}
                }
             
    }
    public void buttonLeft(View v){
    	try {
			Message sendmsg = new Message();
			sendmsg.what = Code.MSG_SEND;
			String string = Code.KEY_UP;
			sendmsg.obj = string;
			sm2pc.revHandler.sendMessage(sendmsg);
			//Toast.makeText(MainActivity.this, "上一页", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    public void buttonRight(View v){
    	//Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
    	try {
			Message sendmsg = new Message();
			sendmsg.what = Code.MSG_SEND;
			String string = Code.KEY_DOWN;
			sendmsg.obj = string;
			sm2pc.revHandler.sendMessage(sendmsg);
			//Toast.makeText(MainActivity.this, "下一页", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
}
