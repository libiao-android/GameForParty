package com.nubia.lijia;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.nubia.bluetooth.ClientThread;
import com.nubia.gameforparty.R;

public class GreatAdventureActivity extends Activity implements View.OnClickListener {
	private String[] mTruthTitle;
	private String[] mTruthContent;
	private String[] mDareTitle;
	public String[] mDareContent;
	public static ClientThread mClientThread;

	public static String mAddress;
	private ProgressDialog mProgressDialog;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private static class MyHandler extends Handler{
		WeakReference<GreatAdventureActivity> mActivity;
		public MyHandler(GreatAdventureActivity activity){
			mActivity=new WeakReference<GreatAdventureActivity>(activity);
		}
		public void handleMessage(android.os.Message msg) {
			GreatAdventureActivity thisActivity=mActivity.get();
			switch (msg.what) {
			case 0:
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
				break;
			case 1:
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(), "连接失败，请重新连接", Toast.LENGTH_LONG).show();
				thisActivity.finish();
				break;
		
			default:
				break;
			}
		}
	};
	private MyHandler mHandler=new MyHandler(this);
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.greatadventure);
        Intent in=getIntent();
		mAddress=in.getStringExtra("add");
		if(mAddress!=null){
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);
			mClientThread=new ClientThread(device,mHandler); 
			mClientThread.start();
			mProgressDialog = new ProgressDialog(this);  
			mProgressDialog.setTitle("稍等片刻");  
			mProgressDialog.setMessage("正在连接中...");  
			mProgressDialog.setIndeterminate(true);  
			mProgressDialog.setCancelable(true);  
			mProgressDialog.show();
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
        
		mTruthTitle=DifficultDegreeValue.TRUTH_TITLE_ARRAY_EASY;
		mTruthContent=DifficultDegreeValue.TRUTH_CONTENT_ARRAY_EASY;
		mDareTitle=DifficultDegreeValue.DARE_TITLE_ARRAY_EASY;
		mDareContent=DifficultDegreeValue.DARE_CONTENT_ARRAY_EASY;
    	
        findViewById(R.id.truth).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.dare).setOnClickListener(this);
        findViewById(R.id.easy).setOnClickListener(this);
        findViewById(R.id.medium).setOnClickListener(this);
        findViewById(R.id.hard).setOnClickListener(this);
        findViewById(R.id.seemessage).setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
    	Intent intent = new Intent();
    	Bundle b1 = new Bundle();
    	intent.setClass(GreatAdventureActivity.this, WheelViewTimeActivity.class);
        switch (v.getId()) {
        case R.id.easy:
        	mTruthTitle=DifficultDegreeValue.TRUTH_TITLE_ARRAY_EASY;
    		mTruthContent=DifficultDegreeValue.TRUTH_CONTENT_ARRAY_EASY;
    		mDareTitle=DifficultDegreeValue.DARE_TITLE_ARRAY_EASY;
    		mDareContent=DifficultDegreeValue.DARE_CONTENT_ARRAY_EASY;
        	break;
        case R.id.medium:
        	mTruthTitle=DifficultDegreeValue.TRUTH_TITLE_ARRAY_MEDIUM;
    		mTruthContent=DifficultDegreeValue.TRUTH_CONTENT_ARRAY_MEDIUM;
    		mDareTitle=DifficultDegreeValue.DARE_TITLE_ARRAY_MEDIUM;
    		mDareContent=DifficultDegreeValue.DARE_CONTENT_ARRAY_MEDIUM;
        	break;
        case R.id.hard:
        	mTruthTitle=DifficultDegreeValue.TRUTH_TITLE_ARRAY_HARD;
    		mTruthContent=DifficultDegreeValue.TRUTH_CONTENT_ARRAY_HARD;
    		mDareTitle=DifficultDegreeValue.DARE_TITLE_ARRAY_HARD;
    		mDareContent=DifficultDegreeValue.DARE_CONTENT_ARRAY_HARD;
        	break;
        case R.id.truth:
            b1.putStringArray("truthTitleArray",mTruthTitle);
            b1.putStringArray("truthContentArray",mTruthContent);
            intent.putExtras(b1);
            startActivity(intent);         
            break;
        case R.id.dare:
            b1.putStringArray("dareTitleArray",mDareTitle);
            b1.putStringArray("dareContentArray",mDareContent);
            intent.putExtras(b1);
            startActivity(intent);
            break;
        case R.id.seemessage:
        	startActivity(intent.setClass(GreatAdventureActivity.this, QueryTerms.class));
            break;
        case R.id.exit:
            finish();
            break;
        }
    }
    @Override
    protected void onDestroy() {
    	if(mAddress!=null){
 			if(mClientThread.getmSocket()!=null){
 				try {
 					mClientThread.getmSocket().close();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 			}
 		}
 		mHandler=null;
    	super.onDestroy();
    }
}
