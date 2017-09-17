package com.nubia.gameforparty;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.nubia.bluetooth.ServerThread;
import com.nubia.gameforparty.R;

public class HostActivity extends Activity{
	private ListView mBluetoothBonded;
	private ListView mGameOutInfo;
	private ServerThread mServerThread;
	private ArrayList<String> mBondedList;
	private HostBondedAdapter mBluetoothBondedAdapter;
	private Button mClearBtn;
	private ArrayList<String> mInfoList;
	private HostInfoAdapter mInfoAdapter;
	private static class MyHandler extends Handler{
		WeakReference<HostActivity> mActivity;
		public MyHandler(HostActivity activity){
			mActivity=new WeakReference<HostActivity>(activity);
		}
		public void handleMessage(android.os.Message msg) {
			HostActivity thisActivity=mActivity.get();
			switch (msg.what) {
			case GameForPartyUtils.HANDLER_BONDED_DEVICENAME:
				thisActivity.mBondedList.add((String) msg.obj);
				thisActivity.mBluetoothBondedAdapter.notifyDataSetChanged();
				break;
			case GameForPartyUtils.HANDLER_DEVICE_OUTINFO:
				thisActivity.mInfoList.add((String) msg.obj);
				thisActivity.mInfoAdapter.notifyDataSetChanged();
				break;
			case GameForPartyUtils.HANDLER_CLEAR_OUTINFO:
				thisActivity.mInfoList.clear();
				thisActivity.mInfoAdapter.notifyDataSetChanged();
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.host_view);
		setupViews();
	}
	private void setupViews() {
		mClearBtn=(Button) findViewById(R.id.host_btn_id);
		
		mBondedList=new ArrayList<String>();
		mInfoList=new ArrayList<String>();
		mServerThread=new  ServerThread(mHandler);
		mServerThread.start();

		mBluetoothBonded=(ListView) findViewById(R.id.host_bluetooth_bonded_lv);
		mBluetoothBondedAdapter=new HostBondedAdapter(this, mBondedList);
		mBluetoothBonded.setAdapter(mBluetoothBondedAdapter);

		mGameOutInfo=(ListView) findViewById(R.id.host_info_lv);
		mInfoAdapter=new HostInfoAdapter(this, mInfoList);
		mGameOutInfo.setAdapter(mInfoAdapter);
		
		mClearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mHandler.sendEmptyMessage(GameForPartyUtils.HANDLER_CLEAR_OUTINFO);
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		mServerThread.setmAcceptFlag(false);
		if(mServerThread.getMserverSocket()!=null){
			try {
				mServerThread.getMserverSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mHandler=null;
		super.onDestroy();
	}
}
