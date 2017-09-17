package com.nubia.gameforparty;

import java.util.ArrayList;

import com.nubia.gameforparty.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HostBondedAdapter extends BaseAdapter{
	private ArrayList<String> mBondedList;
	private LayoutInflater mInflater;
	public HostBondedAdapter(Context con,ArrayList<String> mBondedList){
		this.mBondedList=mBondedList;
		mInflater=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mBondedList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBondedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView bondedName = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.host_bonded_item, null);
			
			bondedName = (TextView) convertView.findViewById(R.id.host_bonded_item_tv);
			convertView.setTag(new DataWrapper(bondedName));
		}else{
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			bondedName = dataWrapper.bondedName;	
		}
		String s=mBondedList.get(position);
		bondedName.setText(s);
		return convertView;
	}
	private final class DataWrapper{
		public TextView bondedName;
		public DataWrapper(TextView textView) {
			this.bondedName = textView;
		}
	}
}
