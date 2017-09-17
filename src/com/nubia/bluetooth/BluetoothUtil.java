package com.nubia.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import com.nubia.gameforparty.BluetoothInfo;

public class BluetoothUtil {
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	public void sendMessage(BluetoothSocket socket,String s){
		if(socket!=null){
			try {				
				OutputStream os = socket.getOutputStream(); 
				os.write(s.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}else{
			
		}
	}
	public  ArrayList<BluetoothInfo> getBondedDevice(){
		ArrayList<BluetoothInfo> pairList=new ArrayList<BluetoothInfo>();
		if(mBluetoothAdapter.isEnabled()){
			Set<BluetoothDevice> set=mBluetoothAdapter.getBondedDevices();
			for(BluetoothDevice b:set){
				pairList.add(new BluetoothInfo(b.getName(),b.getAddress(),true,false));
			}
		}
		return pairList;
	}
	public void openDiscover(Context con){
		Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		con.startActivity(enabler);
		Intent enablerTime = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		enablerTime.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）
		con.startActivity(enablerTime);
	}
}
