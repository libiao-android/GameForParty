package com.nubia.lijia;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.gameforparty.R;

public class WheelViewTimeActivity extends Activity {
	private BluetoothUtil mUtil=new BluetoothUtil();
    private String[] mTruthTitleArray;
    private String[] mTruthContentArray;

    private WheelView mTruthTitle = null;
    private WheelView mTruthContent = null;
    private Button mStart = null;
    private Button mExitBtn = null;

    //private View mDecorView = null;    
    private NumberAdapter mTruthTitleAdapter;
    private NumberAdapter mTruthContentAdapter;
    int mPos1;
    int mPos2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wheel_time);
        Intent intent = this.getIntent();
        Bundle b1 = intent.getExtras();
        mTruthTitleArray = b1.getStringArray("truthTitleArray");
        if(mTruthTitleArray!=null){
        	mTruthContentArray = b1.getStringArray("truthContentArray");
        }else{
        	mTruthTitleArray = b1.getStringArray("dareTitleArray");
        	mTruthContentArray = b1.getStringArray("dareContentArray");
        }
        mTruthTitle = (WheelView) findViewById(R.id.wheel1);
        mTruthContent = (WheelView) findViewById(R.id.wheel2);
        
        mStart=(Button)findViewById(R.id.startbtn);
        mStart.setOnClickListener(new MyListener());
        mExitBtn=(Button)findViewById(R.id.exitbtn);
        mExitBtn.setOnClickListener(new MyListener());

        mTruthTitle.setScrollCycle(true);
        mTruthContent.setScrollCycle(true);
        
        mTruthTitleAdapter = new NumberAdapter(mTruthTitleArray);
        mTruthContentAdapter = new NumberAdapter(mTruthContentArray);

        mTruthTitle.setAdapter(mTruthTitleAdapter);
        mTruthContent.setAdapter(mTruthContentAdapter);
        
        mTruthTitle.setSelection(0, true);
        mTruthContent.setSelection(0, true);
        
        ((WheelTextView)mTruthTitle.getSelectedView()).setTextSize(20);
        ((WheelTextView)mTruthContent.getSelectedView()).setTextSize(20);
        
        mTruthContent.setOnItemSelectedListener(mListener);
        mTruthTitle.setOnItemSelectedListener(mListener);
       
        
        mTruthTitle.setUnselectedAlpha(0.5f);
        mTruthContent.setUnselectedAlpha(0.5f);
        

       // mDecorView = getWindow().getDecorView();

    }
    
    private class MyListener implements View.OnClickListener{
    	public void onClick(View v) { 
    	
				 switch (v.getId()) {
				 case R.id.startbtn:
					   int i=new Random().nextInt(6);
					   mTruthContent.onFling(null,null,0,5000+i*1000);
					    mTruthTitle.onFling(null,null,0,5000+i*1000);
			    		 new Timer().schedule(new TimerTask() {  
			 	            @Override  
			 	            public void run() {  
			 	              
			 	               String s=mTruthTitleArray[mPos1]+":"+mTruthContentArray[mPos2];
			 	              if(GreatAdventureActivity.mClientThread!=null){
			            			mUtil.sendMessage(GreatAdventureActivity.mClientThread.getmSocket(), s);
			            			
			            		}
			 	            }  
			 	        }, 2000);
			    		break;
			        case R.id.exitbtn:
			        	 finish();
			            break;
			        }
        }  
	}

    private TosAdapterView.OnItemSelectedListener mListener = new TosAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
            ((WheelTextView)view).setTextSize(20);
            
            int index = Integer.parseInt(view.getTag().toString());
            int count = parent.getChildCount();
            if(index < count-1){
                ((WheelTextView)parent.getChildAt(index+1)).setTextSize(20);
            }
            if(index>0){
                ((WheelTextView)parent.getChildAt(index-1)).setTextSize(20);
            }
           
            formatData();
        }
        
        @Override
        public void onNothingSelected(TosAdapterView<?> parent) {
            
        }
    }; 

    private void formatData() {
    	mPos1 = mTruthTitle.getSelectedItemPosition();
    	mPos2 = mTruthContent.getSelectedItemPosition();
    }


    private class NumberAdapter extends BaseAdapter {
        int mHeight = 150;
        String[] mData = null;

        public NumberAdapter(String[] data) {
            mHeight = (int) Utils.dipToPx(WheelViewTimeActivity.this, mHeight);
            this.mData = data;
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.length : 0;
        }

        @Override
        public View getItem(int arg0) {
            return getView(arg0, null, null);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WheelTextView textView = null;
            if (null == convertView) {
                convertView = new WheelTextView(WheelViewTimeActivity.this);
                convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
                textView = (WheelTextView) convertView;
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER);
            }
            
            
            String text = mData[position];
            if (null == textView) {
                textView = (WheelTextView) convertView;
            }
            
            textView.setText(text);
            return convertView;
        }
    }
}
