package com.nubia.zhuli;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.nubia.gameforparty.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Spinner;

public class Dices extends SurfaceView implements Runnable,Callback{
	
	public static int NUM = 6; //骰子个数
	private static int screenW;
	private static int screenH;	
	private SurfaceHolder msfh;
	private Paint mPaint;
	private Thread mThread;
	private boolean flag;
	private Canvas mCanvas;
	private Move move;
	private Handler mHandler;
	private Spinner mSpinner;
	
	private Context mContext;
	private ShakeListener mShakeListener;//摇手机
	private List<MediaPlayer> mp;
	private int mpNum; 
	//摇动音乐
	private Bitmap mCity;
	private Bitmap mBackgroud_Pic;
	private int mChange; //骰子个数变换
	private boolean isLeopard; //三个以上是否摇出豹子
	private boolean isFirstEnter;
	private boolean isEnd;
	
	private Dice[] mDice=new Dice[6];
	private Random random=new Random();
	private int[] mResults;
	
	public Dices(Context context,AttributeSet attr){
		super(context,attr);
		mContext=context;
		msfh=this.getHolder();
		msfh.addCallback(this);
		mPaint=new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(40);
		mPaint.setTextAlign(Align.CENTER);
		mp=new ArrayList<MediaPlayer>();
		setFocusable(true);
		Dice.mCount=0;
		isLeopard=false;
		isFirstEnter=true;
		isEnd=false;		
		mResults=new int[6];
		mBackgroud_Pic=BitmapFactory.decodeResource(getResources(), R.drawable.sun);
		mCity=BitmapFactory.decodeResource(getResources(), R.drawable.city);
	    
		mShakeListener=new ShakeListener(mContext);
		mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			
			@Override
			public void onShake(int speed) {
				// TODO Auto-generated method stub
				if(flag){
				isFirstEnter=false;
				isEnd=true;
				mChange=0;
				move.init(speed);				
				move.start();
				//System.out.println("zheng zai yao");
				if(DiceCastActivity.mFlag){
					MediaPlayer m=MediaPlayer.create(mContext, R.raw.sound_dice);
					mp.add(m);
				}
				
				
				mpNum++;
     			mp.get(mpNum).start();	
				}
			}
		});		
	      setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mChange++;
				if(mChange==2){
					NUM=(NUM==6 ? 1 : 6);
					mChange=0;
					mSpinner.setSelection(NUM-1, true);
					reCanvasDices();
				}
			}
		});
	}
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		//System.out.println("surfaceCreated");
		Dice.mCount=0;
		screenW=this.getWidth();
		screenH=this.getHeight();
		flag=true;
		mThread=new Thread(this);
		mThread.start();
	}
     
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	//	System.out.println("surfaceDestroyed");
		isEnd=true;
		flag=false;
		mShakeListener.Stop();
		//mShakeListener=null;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mThread=Thread.currentThread();	
		if(!isEnd){
		Dice.mCount=0;
		init();
		move=new Move(mDice);		
		MediaPlayer m=MediaPlayer.create(mContext, R.raw.sound_dice);
		mp.add(m);
		mpNum=0;
		mp.get(mpNum).start();
		}
		long start,end;
		while(flag){		    
			try{
				start=System.currentTimeMillis();
				RollingDice();
				move.start();						
			    end=System.currentTimeMillis();
				if(end-start<20){
					Thread.sleep(20-(end-start));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			shake();
		}	
	}
	private void shake(){
		for(int i=0;i<Dices.NUM;i++){
			if(!mDice[i].isState()){
				continue;
			}
			return;
		}
		isEnd=false;
		//清空所有声音播放
		if(!mp.isEmpty()){
			for(MediaPlayer m : mp){
				m.stop();
			}
			mp.clear();
			mpNum=-1;
		}else if(move.isQuiet()){
			while(mp.isEmpty()&&!isEnd){
				if(mShakeListener!=null){
				   mShakeListener.start();
				}
			}
		}
	}
	//初始化骰子随机位置
	private void init(){
		Dice.mCount=0;
		for(int i=0;i<Dices.NUM;i++){
			if(mDice[i]!=null){
				mDice[i]=null;
			}
			mDice[i]=new Dice(screenW, screenH, BitmapFactory.decodeResource(getResources(), 
					          Face.face[i]));
			for(int j=0;j<Dice.mCount;j++){
				if(mDice[i].isValid(mDice[j])|| i==j){
					continue;
				}
				i--;
				Dice.mCount--;
				break;
			}
		}
	}
	//绘制骰子
	private void RollingDice(){
		try{
			mCanvas=msfh.lockCanvas();
			if(mCanvas!=null){
				mCanvas.drawColor(0XffD8BFD8);				
				mCanvas.drawBitmap(mBackgroud_Pic, screenW/2-mBackgroud_Pic.getWidth()/2,
						           screenH/2-mBackgroud_Pic.getHeight()/2,mPaint);
				mCanvas.drawBitmap(mCity, 0, screenH-mCity.getHeight(),mPaint);
				for(int i=0;i<Dices.NUM;i++){
					if(mDice[i].isState()){
						rollingDice(mCanvas, i);
					}else{
						stopDice(mCanvas, i);
					}
				}
				if(move.isAllNotRun()&&!isFirstEnter){
					StringBuffer[] results=result();
					mPaint.setColor(Color.RED);
					mPaint.setTextSize(50);
				    mCanvas.drawText(results[0].toString(), screenW / 2,
								screenH - 50, mPaint);
				    Message message=new Message();
				    message.what=2;
				    message.obj=results[0].append(results[1]).toString();				   
				    mHandler.sendMessage(message);   
				}
			}
		}catch(Exception e){
			 e.printStackTrace();
		}finally {
			if(mCanvas!=null){
				msfh.unlockCanvasAndPost(mCanvas);
			}
		}
	}
	
	//绘制初始化的骰子
	private void rollingDice(Canvas canvas,int i){
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				Face.rollface[random.nextInt(9)]), 
				mDice[i].getLeft(), mDice[i].getTop(),mPaint);		
	}
	//绘制停止的骰子
	private void stopDice(Canvas canvas,int i){
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
				Face.face[mDice[i].getFace().getFaceValue()]), 
				mDice[i].getLeft(), mDice[i].getTop(),mPaint);		
	}
	//骰子个数变换后重新绘制骰子
	public void reCanvasDices(){
		try{
			mCanvas=msfh.lockCanvas();
			if(mCanvas!=null){
				mCanvas.drawColor(0XffD8BFD8);				
				mCanvas.drawBitmap(mBackgroud_Pic, screenW/2-mBackgroud_Pic.getWidth()/2,
						           screenH/2-mBackgroud_Pic.getHeight()/2,mPaint);
				mCanvas.drawBitmap(mCity, 0, screenH-mCity.getHeight(),mPaint);
				init();
				for(int i=0;i<Dices.NUM;i++){					
				    stopDice(mCanvas,i);					
				}
			}
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				if(mCanvas!=null){
					msfh.unlockCanvasAndPost(mCanvas);
				}
			}
	}
	//结果反馈
	private StringBuffer[] result(){
		int value=0;
		int totle=0;
		isLeopard=false;
		StringBuffer[] result=new StringBuffer[2];
		result[0]=new StringBuffer();
		result[1]=new StringBuffer();
		for(int i=0;i<6;i++){
			mResults[i]=0;
		}
		for(int i=0;i<NUM;i++){
			value=++mResults[mDice[i].getFace().getFaceValue()];
			totle=totle+mDice[i].getFace().getFaceValue()+1;
			if(NUM>2 && (value==NUM)){
				isLeopard=true;
			}
		}
		if(isLeopard){
			result[0].append("你摇出了豹子！！");
		}else{
			result[0].append("总点数为： ");
			result[0].append(totle);
			result[0].append(" 点   ");
		}
		for(int i=0;i<6;i++){
			if(mResults[i]>0){
				result[1].append(mResults[i]);
				result[1].append("个"+(i+1)+",");
			}
		}
		return result;
	}
	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void setSpinner(Spinner spinner) {
		mSpinner = spinner;
	}
	
	
}
