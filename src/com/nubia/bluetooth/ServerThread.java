package com.nubia.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nubia.gameforparty.GameForPartyUtils;

public class ServerThread extends Thread{
	public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
	private final BluetoothServerSocket mserverSocket;
	private BluetoothSocket socket = null;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	private Handler mHandler;
	private boolean mAcceptFlag=true;
	public ServerThread(Handler h){
		
		this.mHandler=h;
		BluetoothServerSocket tmp = null; 
		try {
			tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
					UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		mserverSocket=tmp;
	}
	
	public BluetoothServerSocket getMserverSocket() {
		return mserverSocket;
	}
	
	public void setmAcceptFlag(boolean mAcceptFlag) {
		this.mAcceptFlag = mAcceptFlag;
	}

	@Override
	public void run() {
		while(mAcceptFlag){
			try {
				/* 创建一个蓝牙服务器 
				 * 参数分别：服务器名称、UUID	 */	
			
				Log.d("server", "wait cilent connect...");
				socket = mserverSocket.accept();
				
				String deviceName=socket.getRemoteDevice().getName();
				Message msg = new Message();
				msg.obj=deviceName;
				msg.what=GameForPartyUtils.HANDLER_BONDED_DEVICENAME;
				mHandler.sendMessage(msg);
				Log.d("server", "accept success !");
				ReadThread mreadThread = new ReadThread(socket,mHandler,deviceName);
				mreadThread.start();
				Thread.sleep(1000);
			} catch (IOException e) {
				e.printStackTrace();
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
