package com.nubia.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class ClientThread extends Thread{
	private final BluetoothSocket mSocket;
	private BluetoothDevice mDevice;

	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Handler mHandler;
	public ClientThread(BluetoothDevice device,Handler h){
		this.mDevice=device;
	
		this.mHandler=h;
		BluetoothSocket tmp= null;
		try {
			tmp = mDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSocket=tmp;
	}

	public BluetoothSocket getmSocket() {
		return mSocket;
	}

	@Override
	public void run() {
		
		
		mBluetoothAdapter.cancelDiscovery();
		try {
			mSocket.connect();
			
			mHandler.sendEmptyMessage(0);
		}catch (IOException e){
			
			e.printStackTrace();
			mHandler.sendEmptyMessage(1);
			Log.i("server", "连接服务端异常！断开连接重新试一试");
		} 
	}
}
