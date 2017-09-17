package com.nubia.gameforparty;

import java.util.ArrayList;

import com.nubia.gameforparty.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FragmentPairAdapter extends BaseAdapter{
	private ArrayList<BluetoothInfo> mPairList;
	private LayoutInflater mInflater;
	
	public FragmentPairAdapter(Context con,ArrayList<BluetoothInfo> mBondedList){
		this.mPairList=mBondedList;
		mInflater=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mPairList.size();
	}

	@Override
	public Object getItem(int position) {
		return mPairList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView deviceName = null;
		ProgressBar pb=null;
		if(convertView == null){
			
			convertView = mInflater.inflate(R.layout.host_bonded_item, null);
			
			deviceName = (TextView) convertView.findViewById(R.id.host_bonded_item_tv);
			pb = (ProgressBar) convertView.findViewById(R.id.pair_pb_id);
			convertView.setTag(new DataWrapper(deviceName,pb));
		}else{
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			deviceName = dataWrapper.deviceName;
			pb=dataWrapper.pb;
		}
		BluetoothInfo info=mPairList.get(position);
		deviceName.setText(info.getmDeviceName());
		if(info.ismIsShowPb()){
			pb.setVisibility(View.VISIBLE);
		}else{
			pb.setVisibility(View.INVISIBLE);
		}
		if(info.ismIsPair()){
			deviceName.setBackgroundResource(R.drawable.nopair);
		}else{
			deviceName.setBackgroundResource(R.drawable.paired);
		}
		return convertView;
	}
	private final class DataWrapper{
		public TextView deviceName;
		public ProgressBar pb;
		public DataWrapper(TextView textView,ProgressBar pb) {
			this.deviceName = textView;
			this.pb = pb;
		}
	}
}
