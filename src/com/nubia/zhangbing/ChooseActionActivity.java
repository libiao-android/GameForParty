package com.nubia.zhangbing;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.gameforparty.R;

public class ChooseActionActivity extends Activity implements OnClickListener{
	
	private BluetoothUtil mUtil=new BluetoothUtil();
	private String mRequiredData;
	private int mIsteamFengPlaying,mFengScore,mShaScore;
	private TextView mTeamName,mTeamScore,mActionCategory;
	private ImageView mTurnPic,mGoNext,mGoBack,mSendData;
	private int mRandomNum=0;
	private int mAngle = 0;
	private String [] mActionCat;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private Intent mIntent;
	private boolean mActionIsChoosed = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooseaction);
		mActionCat = this.getResources().getStringArray(R.array.action);
		
		
		mTeamName = (TextView)findViewById(R.id.choosedTeam1);
		mTeamScore = (TextView)findViewById(R.id.TeamScore1);
		mActionCategory = (TextView)findViewById(R.id.Expressmethod1);
		
		mTurnPic = (ImageView)findViewById(R.id.turnable);
		mGoNext = (ImageView)findViewById(R.id.ToSeeAction);
		mGoBack = (ImageView)findViewById(R.id.goback2);
		mSendData = (ImageView)findViewById(R.id.senddata);
		mTurnPic.setOnClickListener(ChooseActionActivity.this);
		mGoBack.setOnClickListener(ChooseActionActivity.this);
		mGoNext.setOnClickListener(ChooseActionActivity.this);
		mSendData.setOnClickListener(ChooseActionActivity.this);
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mActionCategory.setText(null);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mSharedPreferences = getSharedPreferences(
				"shareddata", Activity.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		// 此时若取数据失败，如何处理
		mIsteamFengPlaying = mSharedPreferences.getInt("teamFengIsPlaying",0);
		if(0 == mIsteamFengPlaying){
			//是沙之队
			//Log.v(TAG_Action, "ChooseActionActivity_ONCREAT");
			mShaScore = mSharedPreferences.getInt("teamShaScore", 0);
			mTeamName.setText(getResources().getString(R.string.TeamSha));
			mTeamScore.setText(mShaScore+""+"分");
		}
		else {
			//是风之队
			mFengScore = mSharedPreferences.getInt("teamFengScore", 0);
			mTeamName.setText(getResources().getString(R.string.TeamFeng));
			mTeamScore.setText(mFengScore+""+"分");
		}	
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.turnable:
				AnimationSet rotate_animation_set = new AnimationSet(true);
	            //设定随机数，确定转动后位置
				mRandomNum = (int)(Math.random()*mActionCat.length);
				switch (mRandomNum) {
				case 0:
					mAngle = 360*5;
					break;
				case 1:
					mAngle = 360*5+72;
					break;
				case 2:
					mAngle = 360*5+72*2;
					break;
				case 3:
					mAngle = 360*5+72*3;
					break;
				case 4:
					mAngle = 360*5+72*4;
					break;

	            }
				RotateAnimation mRotate_Animation = new RotateAnimation(0, mAngle, 
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);    
                        rotate_animation_set.addAnimation(mRotate_Animation);  
                        rotate_animation_set.setDuration(3000);
                        rotate_animation_set.setFillAfter(true);
                        rotate_animation_set.setFillBefore(true);
                        mTurnPic.startAnimation(rotate_animation_set);
                        
                        
                 new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mActionCategory.setText(mActionCat[mRandomNum]);
						mActionIsChoosed = true;
					}
                 },3000);         
                 mEditor.putInt("ActionCategory",mRandomNum);
                 mEditor.commit();// 提交修改
        		
				break;
			case R.id.ToSeeAction:
				if(mActionIsChoosed){
					mActionIsChoosed = false;
					mIntent = new Intent(ChooseActionActivity.this,ChooseMethodActivity.class);
					startActivity(mIntent);
				}
				else{

					Toast.makeText(ChooseActionActivity.this,
							getResources().getString(R.string.tips1),
							Toast.LENGTH_SHORT).show();
				}
				break;
            case R.id.goback2:
				finish();
				break;
            case R.id.senddata:
				//此处向游戏大厅发送数据
            	if(0 == mIsteamFengPlaying){
            		//是沙之队
            		mRequiredData = getResources().getString(R.string.TeamSha)+" "+String.valueOf(mShaScore)+"分";
            		
            		if(StupidActivity.mClientThread!=null){
            			mUtil.sendMessage(StupidActivity.mClientThread.getmSocket(), mRequiredData);
            			Toast.makeText(ChooseActionActivity.this,"数据已发送",Toast.LENGTH_SHORT).show();
            		}else{
            			Toast.makeText(ChooseActionActivity.this,"未连接，无法发送",Toast.LENGTH_SHORT).show();
            		}
            		
            	}
            	else{
            		//风之队
            		mRequiredData = getResources().getString(R.string.TeamFeng)+" "+String.valueOf(mFengScore)+"分";
            		
            		if(StupidActivity.mClientThread!=null){
            			mUtil.sendMessage(StupidActivity.mClientThread.getmSocket(), mRequiredData);
            			Toast.makeText(ChooseActionActivity.this,"数据已发送",Toast.LENGTH_SHORT).show();
            		}else{
            			Toast.makeText(ChooseActionActivity.this,"未连接，无法发送",Toast.LENGTH_SHORT).show();
            		}
            		
            	}
            	
				break;
			}
	}}
	
	
	
	

