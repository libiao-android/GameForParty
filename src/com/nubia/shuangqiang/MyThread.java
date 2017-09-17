package com.nubia.shuangqiang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class MyThread extends Thread{
	
	private Handler myHandler;
	
	public MyThread(Handler handler){
		this.myHandler = handler;
	}
	
	//用于存储抽取的数据
	private int[] mMessage1 = new int[4];
	@Override
	public void run(){
		//产生随机数选择矩阵
		int[] choice = new int[54];
		for(int i=0; i<54; i++)
			choice[i]=i;
		
		//产生四个1-choice.length之间的不重合伪随机数
		for(int i=0; i<4; i++){
			//产生0~choice.length-i之间的伪随机数
			int j = (int)(Math.random() * (choice.length-i));
			//System.out.println("i = "+i+"  j = "+j);
			mMessage1[i] = choice[j];
			choice[j] = choice[choice.length-i-1];
		}
		//传出数据
		Bundle bundle = new Bundle();
		bundle.putIntArray("data", mMessage1);
		Message message = new Message();
		message.what = 0x1233;
		message.setData(bundle);
		myHandler.sendMessage(message);
		//向蓝牙传输数据
		GetData getData = new GetData(mMessage1);
		String stringAdd = "";
		for(int i = 0; i < 4; i++)
			stringAdd += getData.getString("", i);
		//Log.d("mytag", "抽牌结果为："+stringAdd);
		//System.out.println("抽牌结果为："+stringAdd);
		Message m=new Message();
		m.obj="抽牌结果为："+stringAdd;
		m.what=2;
		myHandler.sendMessage(m);
	}
};
