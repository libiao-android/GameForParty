package com.nubia.shuangqiang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class MyThread extends Thread{
	
	private Handler myHandler;
	
	public MyThread(Handler handler){
		this.myHandler = handler;
	}
	
	//���ڴ洢��ȡ������
	private int[] mMessage1 = new int[4];
	@Override
	public void run(){
		//���������ѡ�����
		int[] choice = new int[54];
		for(int i=0; i<54; i++)
			choice[i]=i;
		
		//�����ĸ�1-choice.length֮��Ĳ��غ�α�����
		for(int i=0; i<4; i++){
			//����0~choice.length-i֮���α�����
			int j = (int)(Math.random() * (choice.length-i));
			//System.out.println("i = "+i+"  j = "+j);
			mMessage1[i] = choice[j];
			choice[j] = choice[choice.length-i-1];
		}
		//��������
		Bundle bundle = new Bundle();
		bundle.putIntArray("data", mMessage1);
		Message message = new Message();
		message.what = 0x1233;
		message.setData(bundle);
		myHandler.sendMessage(message);
		//��������������
		GetData getData = new GetData(mMessage1);
		String stringAdd = "";
		for(int i = 0; i < 4; i++)
			stringAdd += getData.getString("", i);
		//Log.d("mytag", "���ƽ��Ϊ��"+stringAdd);
		//System.out.println("���ƽ��Ϊ��"+stringAdd);
		Message m=new Message();
		m.obj="���ƽ��Ϊ��"+stringAdd;
		m.what=2;
		myHandler.sendMessage(m);
	}
};
