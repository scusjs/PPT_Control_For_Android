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
 
        private ViewPager viewPager;//ҳ������
        private ImageView imageView;// ����ͼƬ
        private TextView pptInfo,operate;
        private List<View> views;// Tabҳ���б�
        private int offset = 0;// ����ͼƬƫ����
        private int currIndex = 0;// ��ǰҳ�����
        private int bmpW;// ����ͼƬ���
        private View view1,view2;//����ҳ��
        String str = "null";
        SendMessageToPCServerThread sm2pc;
    	Handler handler;
    	EditText et;//��ע
    	TextView tv_pages;//ҳ��
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
          *  ��ʼ��ͷ��
          */
 
        private void InitTextView() {
        		pptInfo = (TextView) findViewById(R.id.ppt_info);
                operate = (TextView) findViewById(R.id.operate);
 
                pptInfo.setOnClickListener(new MyOnClickListener(0));
                operate.setOnClickListener(new MyOnClickListener(1));
                
        }
 
        /**
         2      * ��ʼ���������������ҳ������ʱ������ĺ���Ҳ������Ч������������Ҫ����һЩ����
         3 */
 
        private void InitImageView() {
                imageView= (ImageView) findViewById(R.id.cursor);
                bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// ��ȡͼƬ���
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
                offset = (screenW / 2 - bmpW) / 2;// ����ƫ����
                Matrix matrix = new Matrix();
                matrix.postTranslate(offset, 0);
                imageView.setImageMatrix(matrix);// ���ö�����ʼλ��
        }

        /** 
         *     
         * ͷ�������� 3 */
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
 
            int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
            int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
                public void onPageScrollStateChanged(int arg0) {
                         
                         
                }
 
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                         
                         
                }
 
                public void onPageSelected(int arg0) {
                        Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//��Ȼ����Ƚϼ�ֻ࣬��һ�д��롣
                        currIndex = arg0;
                        animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
                        animation.setDuration(300);
                        imageView.startAnimation(animation);
                        Toast.makeText(MainActivity.this, "��ѡ����"+ viewPager.getCurrentItem()+"ҳ��", Toast.LENGTH_SHORT).show();
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
			//Toast.makeText(MainActivity.this, "��һҳ", Toast.LENGTH_SHORT).show();
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
			//Toast.makeText(MainActivity.this, "��һҳ", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
}
