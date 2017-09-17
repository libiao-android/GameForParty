package com.nubia.gameforparty;

import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FragmentHall extends Fragment{
	private GridView mGridView;
	private int mIndex=0;//记录dialog单选的索引
	private int mConnIndex=0;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private String mList[]=null;
	private String mList2[]=null;
	private List<GameInfo> gameList;
	private GameInfo mGame;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View hallView=inflater.inflate(R.layout.fragment_hall, container,false);
		
		mGridView=(GridView) hallView.findViewById(R.id.gridview);
		mGridView.setAdapter(new GridViewAdapter(getActivity()));
		mGridView.setOnItemClickListener(new MyItemClickListener());
		gameList=SetGameInfo.getGameInfo();
		return hallView;
	}
	class MyItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mGame=gameList.get(position);
			if(mGame.getGameName().equals("敬请期待")){
				Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_LONG).show();
			}else{
				mIndex=0;
				new AlertDialog.Builder(getActivity()).setTitle("身份选择").
				setSingleChoiceItems(new String[] { "我要参与游戏", "我要主持游戏" }, 0, new MyDialogClick()).
				setPositiveButton("确定", new MyDialogClick())
				.setNegativeButton("取消", new MyDialogClick()).show();
			}
			

		}

	}
	class MyDialogClick implements OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {

			switch (which) {
			case 0:
				mIndex=0;
				break;
			case 1:
				mIndex=1;
				break;
			case DialogInterface.BUTTON_POSITIVE:
				mConnIndex=0;
				dialog.cancel();
				if(mIndex==0){

					Set<BluetoothDevice> bonded=mBluetoothAdapter.getBondedDevices();
					if(bonded.size()==0){
						mList=new String[2];
					}else{
						mList=new String[bonded.size()+1];
						mList2=new String[bonded.size()+1];
					}

					mList[0]=("我想一个人玩游戏");
					if (bonded.size() > 0) {
						int i=1;
						for (BluetoothDevice device : bonded) {
							mList[i]=(device.getName());
							mList2[i]=device.getAddress();
							i++;
						}
					} else {
						mList[1]=("没有设备已经配对");
					}
					new AlertDialog.Builder(getActivity()).setTitle("与主持者进行蓝牙连接").
					setSingleChoiceItems(mList, 0, new MyConnDialogClick()).
					setPositiveButton("确定", new MyConnDialogClick())
					.setNegativeButton("取消", null).show();
					//Toast.makeText(GameHallActivity.this, "我要参与游戏", 1).show();

				}
				if(mIndex==1){
					//Toast.makeText(GameHallActivity.this, "我要主持游戏", 1).show();
					if(mBluetoothAdapter.isEnabled()){
						Intent in=new Intent(getActivity(),HostActivity.class);
						startActivity(in);
					}else{
						Toast.makeText(getActivity(), "请先开启蓝牙", Toast.LENGTH_LONG).show();
					}
					
				}
				break;
			case DialogInterface.BUTTON_NEGATIVE:

				dialog.cancel();
				break;

			default:
				break;
			}

		}

	}
	class MyConnDialogClick implements OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {

			if(which>=0){
				mConnIndex=which;
			}
			if(which==DialogInterface.BUTTON_POSITIVE){
				if(mConnIndex==0){
					dialog.cancel();
					SetGameInfo.runGame(getActivity(), mGame.getGameName(), null, 0);
					return;
				}
				dialog.cancel();
				String s=mList[mConnIndex];
				if(mConnIndex==1){
					if(s.equals("没有设备已经配对")){
						return;
					}
				}
				String address=mList2[mConnIndex];
				SetGameInfo.runGame(getActivity(), mGame.getGameName(), address, 1);
			}

		}

	}
}
