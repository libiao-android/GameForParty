package com.nubia.zhuli;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.bluetooth.ClientThread;
import com.nubia.gameforparty.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class DiceCastActivity extends Activity{
	private Dices mDices;
    private static final String[] DICES_NUM={"一颗骰子","两颗骰子","三颗骰子","四颗骰子","五颗骰子","六颗骰子"};
	private ClientThread mClientThread;
	private BluetoothUtil mUtil;
	private String mAddress;
	private ProgressDialog mProgressDialog;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	public static boolean  mFlag=true;
	private static class MyHandler extends Handler{
		WeakReference<DiceCastActivity> mActivity;
		public MyHandler(DiceCastActivity activity){
			mActivity=new WeakReference<DiceCastActivity>(activity);
		}
		public void handleMessage(android.os.Message msg) {
			DiceCastActivity thisActivity=mActivity.get();
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
			case 2:
				if(thisActivity.mAddress!=null){
					thisActivity.mUtil.sendMessage( thisActivity.mClientThread.getmSocket(), msg.obj.toString());
				}
				break;

			default:
				break;
			}
		}
	};
	private MyHandler mHandler=new MyHandler(this);
     @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.dice_view_activity);
    	mFlag=true;
    	mUtil=new BluetoothUtil();
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

		mDices=(Dices)findViewById(R.id.surface_view);
		mDices.setHandler(mHandler);
		Spinner spinner=(Spinner)findViewById(R.id.dice_num_spiner);

	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinner_check_text,
    			                          DICES_NUM);
    	//设置显示格式   	
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setSelection(5,true);
    	Dices.NUM=6;
		mDices.reCanvasDices();
		mDices.setSpinner(spinner);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    		@Override
			public void onNothingSelected(AdapterView<?> v) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, int item, long arg3) {
				// TODO Auto-generated method stub
				Dices.NUM=item+1;
				mDices.reCanvasDices();
				Toast.makeText(DiceCastActivity.this, DICES_NUM[item], Toast.LENGTH_SHORT).show();				
			}
		});
    }
     @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    }
     @Override
 	protected void onDestroy() {
    	 mFlag=false;
 		if(mAddress!=null){
 			if(mClientThread.getmSocket()!=null){
 				try {
 					mClientThread.getmSocket().close();
 				} catch (IOException e) {
 					e.printStackTrace();
 				}
 			}
 		}
 		
 		super.onDestroy();
 		if(mDices!=null){
			mDices=null;
 		}
 		mHandler=null;
 	}
}
