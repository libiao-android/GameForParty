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
	//存储随机数的数组
	private int[] mMessage1 = new int[4];
	//存储图片的ID
	private int[] mPicture = new int[54];
	//用于记录游戏的轮次
	private int mCount=0;
	//点击图片的位置
	private int mPosition = 0;
	//用于记录出牌信息是否已经发送，false表示未发送
	private Boolean[] mFlag = new Boolean[] {false, false, false,false};
	//显示轮次的字符串
	private String mTitle;
	//要显示的图片
	private int mDrawable;
	//获得环境变量
    private Context mContext;
    //handler变量
    private Handler myHandler;
	//要显示的内容
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
		//获得被点击的图片的位置信息
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
		//获得游戏的伦次，并填写相关提示信息
		if(mCount >= 1 && mCount <= 3){
			if(mCount == 1){
				mTitle = "第一轮游戏";
				mText = "点击确定第一轮出牌";
				}
			else if(mCount == 2){
				mTitle = "第二轮游戏";
				mText = "点击确定第二轮出牌";
				}
			else if(mCount == 3 ){
				mTitle = "第三轮游戏";
				mText = "点击确定第三轮出牌,两张牌将全出";
				}
			else{
				
			}
		}
			
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mTitle);
		builder.setIcon(mDrawable);
		builder.setMessage(mText);
			
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			//点击确定时，将原来的对话框设置为不可见，在imageViewBig中加载选中的图片
			@Override
			public void onClick(DialogInterface dialog, int which){
				//将选中ImageView设置为不可见
				if(mCount != 3)
					((ImageView)v).setVisibility(View.INVISIBLE);
				
				//向蓝牙传输信息
				if(mCount >= 1 && mCount <= 3){
					if(mCount == 1){
						GetData getData = new GetData(mMessage1);
						//System.out.println(getData.getString("第一轮出牌：", position));
						Message m=new Message();
						m.obj=getData.getString("第一轮出牌：", mPosition);
						m.what=2;
						myHandler.sendMessage(m);
						//Log.d("mytay1",getData.getString("第一轮出牌：", position));
						mFlag[mPosition] = true;
					}
					else if(mCount == 2){
						GetData getData = new GetData(mMessage1);
						//System.out.println(getData.getString("第二轮出牌：", position));
						Message m=new Message();
						m.obj=getData.getString("第二轮出牌：", mPosition);
						m.what=2;
						myHandler.sendMessage(m);
						//Log.d("mytag2",getData.getString("第二轮出牌：", position));
						mFlag[mPosition] = true;
					}
					else if(mCount == 3 ){
						String stringAdd = "";
						GetData getData = new GetData(mMessage1);
						for(int i=0; i <4; i++){
							if(mFlag[i] == false)
								stringAdd += getData.getString("", i);
						}
						//Log.d("mytag3", "第三轮出牌："+stringAdd);
						//System.out.println("第三轮出牌："+stringAdd);
						Message m=new Message();
						m.obj="第三轮出牌："+stringAdd;
						m.what=2;
						myHandler.sendMessage(m);
					}
					else{}
				}
				//传出数据
				Bundle bundle = new Bundle();
				bundle.putInt("data", mDrawable);
				Message message = new Message();
				message.what = 0x1234;
				message.setData(bundle);
				myHandler.sendMessage(message);
				mCount++;
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		});
		builder.create().show();
	}
	
}