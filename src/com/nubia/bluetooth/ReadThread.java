package com.nubia.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.nubia.gameforparty.GameForPartyUtils;

public class ReadThread extends Thread{

	private BluetoothSocket mSocket;
	private Handler mHandler;
	private String mDeviceName;
	private boolean mReadFlag=true;
	
	public ReadThread(BluetoothSocket socket,Handler h,String name) {

		this.mSocket=socket;
		this.mHandler=h;
		this.mDeviceName=name;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[1024];
		int bytes;
		InputStream mmInStream = null;
		try {
			mmInStream = mSocket.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		while (mReadFlag) {
			try {
				// Read from the InputStream
				if( (bytes = mmInStream.read(buffer)) > 0 ){
					byte[] buf_data = new byte[bytes];
					for(int i=0; i<bytes; i++){
						buf_data[i] = buffer[i];
					}
					String s = new String(buf_data);
					Message msg = new Message();
					msg.obj=mDeviceName+": "+s;
					msg.what=GameForPartyUtils.HANDLER_DEVICE_OUTINFO;
					mHandler.sendMessage(msg);
					Log.i("server", "output="+s);
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			mmInStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public boolean ismReadFlag() {
		return mReadFlag;
	}

	public void setmReadFlag(boolean mReadFlag) {
		this.mReadFlag = mReadFlag;
	}
}
