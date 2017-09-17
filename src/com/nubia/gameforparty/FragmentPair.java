package com.nubia.gameforparty;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.gameforparty.R;

@SuppressLint("NewApi")
public class FragmentPair extends Fragment{
	private ListView mFragmentPairListView;
	private ArrayList<BluetoothInfo> mPairList;
	private FragmentPairAdapter mFragmentPairAdapter;
	private BluetoothDevice mDevice;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Button mPairBtn;
	private BluetoothInfo mInfo;
	private BluetoothUtil mUtil;
	private TextView mTimeCountText;
	private MyTimeCount mCountDownTimer;
	private int mId;
	private boolean mCheckPairThread=true;
	private static class MyHandler extends Handler{
		WeakReference<FragmentPair> mFragmentPair;
		public MyHandler(FragmentPair fragmentPair){
			mFragmentPair=new WeakReference<FragmentPair>(fragmentPair);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentPair pair=mFragmentPair.get();
			switch (msg.what) {
			case 0:
				Toast.makeText(pair.getActivity(), "配对成功", Toast.LENGTH_LONG).show();
				pair.mInfo.setmIsPair(true);
				pair.mInfo.setmIsShowPb(false);
				pair.mFragmentPairAdapter.notifyDataSetChanged();
				break;
			case 1:
				Toast.makeText(pair.getActivity(), "配对失败", Toast.LENGTH_LONG).show();
				pair.mInfo.setmIsShowPb(false);
				pair.mFragmentPairAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};
	private MyHandler mHandler=new MyHandler(this);

	private BroadcastReceiver mReceiver=new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {

			String action=intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if(mDevice.getBondState()!=BluetoothDevice.BOND_BONDED){
					if(mDevice.getName()!=null){
						boolean con=isContain(mPairList,mDevice.getAddress());
						if(!con){
							mPairList.add(new BluetoothInfo(mDevice.getName(), mDevice.getAddress(), false,false));
							mFragmentPairAdapter.notifyDataSetChanged();
						}
					}

				}
			}else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			}
		}

		private boolean isContain(ArrayList<BluetoothInfo> mPairList,String address) {
			for(BluetoothInfo in:mPairList){
				if(in.getmAddress().equals(mDevice.getAddress())){
					return true;
				}
			}
			return false;
		}

	};
	public void onCreate(Bundle savedInstanceState) {
		mCountDownTimer=new MyTimeCount(13000, 1000);
		mPairList=new ArrayList<BluetoothInfo>();
		mUtil=new BluetoothUtil();

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND); 
		//filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		getActivity().registerReceiver(mReceiver, filter);
		super.onCreate(savedInstanceState);

	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View pairView=inflater.inflate(R.layout.fragment_pair, container, false);
		mTimeCountText=(TextView) pairView.findViewById(R.id.pair_time_tv_id);
		mFragmentPairListView=(ListView) pairView.findViewById(R.id.fragment_pair_listview);
		mFragmentPairAdapter=new FragmentPairAdapter(getActivity(), mPairList);
		mFragmentPairListView.setAdapter(mFragmentPairAdapter);

		mPairList.addAll(mUtil.getBondedDevice());
		if(mPairList.size()!=0){
			mFragmentPairAdapter.notifyDataSetChanged();
		}else{
			Toast.makeText(getActivity(), "没有已经配对的蓝牙", Toast.LENGTH_LONG).show();
		}


		mPairBtn=(Button) pairView.findViewById(R.id.fragment_pair_btn_id);
		mPairBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPairList.clear();
				mFragmentPairAdapter.notifyDataSetChanged();
				mPairList.addAll(mUtil.getBondedDevice());
				if(mPairList.size()!=0){
					mFragmentPairAdapter.notifyDataSetChanged();
				}else{
					Toast.makeText(getActivity(), "没有已经配对的蓝牙", Toast.LENGTH_LONG).show();
				}
				searchBluetooth();
			}
		});

		mFragmentPairListView.setOnItemClickListener(new MyListItemClick());
		searchBluetooth();
		return pairView;
	}
	private void searchBluetooth() {
		mId=12;
		mBluetoothAdapter.cancelDiscovery();
		mCountDownTimer.start();
		mPairBtn.setEnabled(false);
		mPairBtn.setText("搜索中...");
		mBluetoothAdapter.startDiscovery();
	}

	@Override
	public void onDestroy() {

		mCountDownTimer.cancel();
		mCheckPairThread=false;
		getActivity().unregisterReceiver(mReceiver);
		mHandler=null;
		super.onDestroy();
	}
	class MyTimeCount extends CountDownTimer{

		public MyTimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			mId=12;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//System.out.println(millisUntilFinished);
			mTimeCountText.setText(mId+"");
			mId--;

		}

		@Override
		public void onFinish() {

			mTimeCountText.setText("");
			mPairBtn.setEnabled(true);
			mPairBtn.setText("重新搜索");
			if(mPairList.size()==0){
				Toast.makeText(getActivity(), "在附近暂无搜索到蓝牙设备", Toast.LENGTH_LONG).show();
			}

		}

	}
	class MyListItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mInfo=mPairList.get(position);
			if(mInfo.ismIsPair()){
				Toast.makeText(getActivity(), "已经配对", Toast.LENGTH_LONG).show();
			}else{
				mCheckPairThread=true;
				mInfo.setmIsShowPb(true);
				mFragmentPairAdapter.notifyDataSetChanged();
				try {
					Method creMethod = BluetoothDevice.class.getMethod("createBond"); 
					creMethod.invoke(mBluetoothAdapter.getRemoteDevice(mInfo.getmAddress()));

					new Thread(new Runnable() {

						@Override
						public void run() {
							int i=0;

							while(i<GameForPartyUtils.PAIR_TIME&&mCheckPairThread){
								if(mBluetoothAdapter.getRemoteDevice(mInfo.getmAddress()).getBondState()==BluetoothDevice.BOND_BONDED){
									mHandler.sendEmptyMessage(0);

									break;
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								i++;
							}
							if(i==GameForPartyUtils.PAIR_TIME){
								mHandler.sendEmptyMessage(1);

							}
						}



					}).start();


				} catch (Exception e) {
					//System.out.println("shi bai"+mInfo.getmDeviceName());
					Toast.makeText(getActivity(), "配对失败", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				} 
			}

		}

	}
}

