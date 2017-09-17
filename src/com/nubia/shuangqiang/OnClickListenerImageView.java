package com.nubia.shuangqiang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.nubia.gameforparty.R;


public class OnClickListenerImageView implements OnClickListener{
	//�洢�����������
	private int[] mMessage1 = new int[4];
	//�洢ͼƬ��ID
	private int[] mPicture = new int[54];
	//���ڼ�¼��Ϸ���ִ�
	private int mCount=0;
	//���ͼƬ��λ��
	private int mPosition = 0;
	//���ڼ�¼������Ϣ�Ƿ��Ѿ����ͣ�false��ʾδ����
	private Boolean[] mFlag = new Boolean[] {false, false, false,false};
	//��ʾ�ִε��ַ���
	private String mTitle;
	//Ҫ��ʾ��ͼƬ
	private int mDrawable;
	//��û�������
    private Context mContext;
    //handler����
    private Handler myHandler;
	//Ҫ��ʾ������
	private String mText;

	public void changeMessage(int[] message1, int count){
		this.mMessage1 = message1;
		this.mCount = count;
		mFlag = new Boolean[]{false, false, false, false};
	}
	
	public void setState(Context content, int[] picture, Handler myHandler){
		this.mContext = content;
		this.mPicture = picture;
		this.myHandler = myHandler;
	}
	@Override
	public void onClick(final View v){
		//��ñ������ͼƬ��λ����Ϣ
		if(((ImageView) v).getId() == R.id.imageView11)
			mPosition = 0;
		else if(((ImageView) v).getId() == R.id.imageView12)
			mPosition = 1;
		else if(((ImageView) v).getId() == R.id.imageView13)
			mPosition = 2;
		else if(((ImageView) v).getId() == R.id.imageView14)
			mPosition = 3;
		else{}
		mDrawable = mPicture[mMessage1[mPosition]];
		//�����Ϸ���״Σ�����д�����ʾ��Ϣ
		if(mCount >= 1 && mCount <= 3){
			if(mCount == 1){
				mTitle = "��һ����Ϸ";
				mText = "���ȷ����һ�ֳ���";
				}
			else if(mCount == 2){
				mTitle = "�ڶ�����Ϸ";
				mText = "���ȷ���ڶ��ֳ���";
				}
			else if(mCount == 3 ){
				mTitle = "��������Ϸ";
				mText = "���ȷ�������ֳ���,�����ƽ�ȫ��";
				}
			else{
				
			}
		}
			
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle);
		builder.setIcon(mDrawable);
		builder.setMessage(mText);
			
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
			//���ȷ��ʱ����ԭ���ĶԻ�������Ϊ���ɼ�����imageViewBig�м���ѡ�е�ͼƬ
			@Override
			public void onClick(DialogInterface dialog, int which){
				//��ѡ��ImageView����Ϊ���ɼ�
				if(mCount != 3)
					((ImageView)v).setVisibility(View.INVISIBLE);
				
				//������������Ϣ
				if(mCount >= 1 && mCount <= 3){
					if(mCount == 1){
						GetData getData = new GetData(mMessage1);
						//System.out.println(getData.getString("��һ�ֳ��ƣ�", position));
						Message m=new Message();
						m.obj=getData.getString("��һ�ֳ��ƣ�", mPosition);
						m.what=2;
						myHandler.sendMessage(m);
						//Log.d("mytay1",getData.getString("��һ�ֳ��ƣ�", position));
						mFlag[mPosition] = true;
					}
					else if(mCount == 2){
						GetData getData = new GetData(mMessage1);
						//System.out.println(getData.getString("�ڶ��ֳ��ƣ�", position));
						Message m=new Message();
						m.obj=getData.getString("�ڶ��ֳ��ƣ�", mPosition);
						m.what=2;
						myHandler.sendMessage(m);
						//Log.d("mytag2",getData.getString("�ڶ��ֳ��ƣ�", position));
						mFlag[mPosition] = true;
					}
					else if(mCount == 3 ){
						String stringAdd = "";
						GetData getData = new GetData(mMessage1);
						for(int i=0; i <4; i++){
							if(mFlag[i] == false)
								stringAdd += getData.getString("", i);
						}
						//Log.d("mytag3", "�����ֳ��ƣ�"+stringAdd);
						//System.out.println("�����ֳ��ƣ�"+stringAdd);
						Message m=new Message();
						m.obj="�����ֳ��ƣ�"+stringAdd;
						m.what=2;
						myHandler.sendMessage(m);
					}
					else{}
				}
				//��������
				Bundle bundle = new Bundle();
				bundle.putInt("data", mDrawable);
				Message message = new Message();
				message.what = 0x1234;
				message.setData(bundle);
				myHandler.sendMessage(message);
				mCount++;
			}
		});
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		builder.create().show();
	}
	
}