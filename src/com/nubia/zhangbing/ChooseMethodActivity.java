package com.nubia.zhangbing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nubia.gameforparty.R;

public class ChooseMethodActivity extends Activity implements OnClickListener {
	//private static final  String Tag_Method="TAG_CHOOSEMETHOD";
	private int mIsteamFengPlaying, mFengScore, mShaScore;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private TextView mTeamName,mTeamScore,mActionCategory;
	private boolean mAnswerIsRight=false;
	private myDialog mDialog;
	private TextView mViewTimeCounting,mSpecificMethod;
	private ImageView mAnswerRight,mAnswerWrong,mImaGoBack;
	private Button mButtonStart;
	
	private String [] mCategory ;
	private String [] mJokes;
	private String [] mBody ;
	private String [] mDrawing;
	private String [] mLips;
	private String [] mSpeaking ;
	
	private boolean mNotPaused = true;
	private TimeThread mThread;
    private Handler mHandler=new Handler();
    private boolean mIsStartPressed=false;
    private boolean mThreadFlag=true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.choosemethod); 
		mCategory = getResources().getStringArray(R.array.action);
		mJokes = getResources().getStringArray(R.array.jokes);
		mBody = getResources().getStringArray(R.array.body);
		mDrawing = getResources().getStringArray(R.array.draw);
		mLips = getResources().getStringArray(R.array.lips);
		mSpeaking = getResources().getStringArray(R.array.speak);
		//Log.v(Tag_Method, "ChooseMethod_ONCREAT");
		mTeamName = (TextView)findViewById(R.id.choosedTeam2);
		mTeamScore = (TextView)findViewById(R.id.TeamScore2);
		mActionCategory = (TextView)findViewById(R.id.Expressmethod2);
		mSharedPreferences = getSharedPreferences(
				"shareddata", Activity.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		
		mIsteamFengPlaying = mSharedPreferences.getInt("teamFengIsPlaying",0);
		if(0 == mIsteamFengPlaying){
			//是沙之队
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
		
		int actionkind = mSharedPreferences.getInt("ActionCategory", 0);
		mActionCategory.setText(mCategory[actionkind]);
		
		mViewTimeCounting=(TextView)findViewById(R.id.timeleft);
		mSpecificMethod=(TextView)findViewById(R.id.nowMethod);
		mButtonStart=(Button)findViewById(R.id.start1);
		mAnswerRight=(ImageView)findViewById(R.id.answer_right);
		mAnswerWrong=(ImageView)findViewById(R.id.answer_wrong);
		mImaGoBack=(ImageView)findViewById(R.id.goback2);
		
	    switch (actionkind){
	        //此处需要排除数组长度为零的情形，待完成
	    case 0:
	    	mSpecificMethod.setText(mJokes[(int) (mJokes.length * Math.random())]);
			break;
		case 1:
			mSpecificMethod.setText(mBody[(int) (mBody.length * Math.random())]);
			break;
		case 2:
			mSpecificMethod.setText(mDrawing[(int) (mDrawing.length * Math.random())]);
			break;
		case 3:
			mSpecificMethod.setText(mLips[(int) (mLips.length * Math.random())]);
			break;
		case 4:
			mSpecificMethod.setText(mSpeaking[(int) (mSpeaking.length * Math.random())]);
			break;
	    }
	    
	    mButtonStart.setOnClickListener(ChooseMethodActivity.this);
	    mAnswerRight.setOnClickListener(ChooseMethodActivity.this);
	    mAnswerWrong.setOnClickListener(ChooseMethodActivity.this);
	    mImaGoBack.setOnClickListener(ChooseMethodActivity.this);
		
		//开启线程放在这里合适吗？
	    mThread=new TimeThread();
		mThread.start();
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Log.v(Tag_Method, "ChooseMethod_ONSTART");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mThreadFlag = false;
		//Log.v(Tag_Method, "ChooseMethod_ONDESTROY");
	}
  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	     
			switch (v.getId()) {
			case R.id.start1:
				 if(mNotPaused)
			     {  
					 mIsStartPressed = true;
			    	 mNotPaused = false;
			     }
			     else{
			    	 mIsStartPressed = false;
			    	 mButtonStart.setText("开始");
			    	 mNotPaused = true;
			     }
				 break;
			case R.id.answer_right:
				mAnswerIsRight = true;
				//处理分数
				if(0 == mIsteamFengPlaying)
				{
					mShaScore++;	
					mEditor.putInt("teamShaScore",mShaScore);
				}
				else{
					mFengScore++;
					mEditor.putInt("teamFengScore",mFengScore);
				}
				mEditor.commit();// 提交修改
				mDialog = new myDialog(ChooseMethodActivity.this);
				mDialog.show();
				break;
			case R.id.answer_wrong:
				mDialog = new myDialog(ChooseMethodActivity.this);
				mDialog.show();
				break;
			case R.id.goback2:
				//保存数据
				if(0 == mIsteamFengPlaying)
				{
					mEditor.putInt("teamShaScore",mShaScore);
				}
				else{
					mEditor.putInt("teamFengScore",mFengScore);
				}
				mEditor.commit();// 提交修改
				finish();
				break;
			
			}
		 
	}
	
	class myDialog extends Dialog implements OnClickListener {  
	   private  Context mContext;  
	   private TextView mTextView;
	   private ImageView mImage;

	    public myDialog (Context context){  
	        super(context);  
	        mContext = context;  
	    }  
	    public myDialog(Context context, int theme) {  
	        super(context, theme);  
	        mContext = context;  
	    }  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	  
	        LayoutInflater inflater = (LayoutInflater) mContext  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        View layout = inflater.inflate(R.layout.result, null);  
	        this.setContentView(layout);  
	        
	        mTextView = (TextView)findViewById(R.id.ShowResult);
	        mImage = (ImageView)findViewById(R.id.goback3); 
	        if(mAnswerIsRight){
	        	mTextView.setText((getResources().getString(R.string.success)));
	        }
	        else{
	        	mTextView.setText((getResources().getString(R.string.fail)));
	        }
	        mImage.setOnClickListener(this);
	    }
	    @Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.goback3:
					this.dismiss();
					finish();
					//Log.v(Tag_Method, "DIALOG_FINISH");
					//intent = new Intent(ChooseMethodActivity.this,ChooseActionActivity.class);
					//startActivity(intent);
					break;
				}
			
		}
		}
	
	class TimeThread extends Thread{
	    //int totalCountTime = 60;
	    private int mNowTime = 60;
		@Override
		public void run() {
				while(mThreadFlag  ){
					while(mIsStartPressed  && mNowTime>=0){
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								mButtonStart.setText("暂停");
								mViewTimeCounting.setText(mNowTime+"");
							}
							
						});
						try {
							Thread.sleep(1000);
							//nowtime++;
							mNowTime--;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
		}
	}
	

	

}







 
