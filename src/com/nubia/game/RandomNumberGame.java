package com.nubia.game;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.bluetooth.ClientThread;
import com.nubia.gameforparty.R;

public class RandomNumberGame extends Activity{
	private Button mBtn;
	private TextView mTv;
	private Random mRandom;
	private int mInt;
	private ClientThread mClientThread;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothUtil mUtil;
	private String mAddress;
	private ProgressDialog mProgressDialog;
	private static class MyHandler extends Handler{
		WeakReference<RandomNumberGame> mActivity;
		public MyHandler(RandomNumberGame activity){
			mActivity=new WeakReference<RandomNumberGame>(activity);
		}
		public void handleMessage(android.os.Message msg) {
			RandomNumberGame thisActivity=mActivity.get();
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
	private MyHandler mHandler = new MyHandler(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_randomnumber);
		setupViews();
	}
	private void setupViews() {
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
		mBtn=(Button) findViewById(R.id.game_random_btn);
		mTv=(TextView) findViewById(R.id.game_random_tv);
		mRandom=new Random();
		mBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mInt=mRandom.nextInt(1000);
				if(mAddress!=null){
					mUtil.sendMessage(mClientThread.getmSocket(),mInt+"");
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTv.setText(mInt+"");
					}
				});
			}
		});
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
		
		super.onDestroy();
	}
}
