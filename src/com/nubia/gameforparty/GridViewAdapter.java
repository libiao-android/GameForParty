package com.nubia.gameforparty;

import java.util.List;

import com.nubia.gameforparty.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{
	private List<GameInfo> mGameList;
	
	private LayoutInflater mInflater;
	public GridViewAdapter(Context con){
		mGameList=SetGameInfo.getGameInfo();
		mInflater=(LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return mGameList.size();
	}

	@Override
	public Object getItem(int position) {
		return mGameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView gameIcon = null;
		TextView gameName = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.gridview_item, null);
			gameIcon = (ImageView) convertView.findViewById(R.id.gridview_item_imageview);
			gameName = (TextView) convertView.findViewById(R.id.gridview_item_textview);
			convertView.setTag(new DataWrapper(gameIcon, gameName));
		}else{
			DataWrapper dataWrapper = (DataWrapper) convertView.getTag();
			gameIcon = dataWrapper.gameIcon;
			gameName = dataWrapper.gameName;	
		}
		GameInfo info=mGameList.get(position);
		gameIcon.setImageResource(info.getDrawableId());
		gameName.setText(info.getGameName());
		return convertView;
	}
	private final class DataWrapper{
		public ImageView gameIcon;
		public TextView gameName;
		public DataWrapper(ImageView imageView, TextView textView) {
			this.gameIcon = imageView;
			this.gameName = textView;
		}
	}
}
