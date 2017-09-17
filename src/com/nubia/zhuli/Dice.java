package com.nubia.zhuli;

import android.graphics.Bitmap;

class Dice {
    private Face mFace;
    private Location mLocation; //当前位置
    private int mScreenW;
    private int mScreenH;
    private int mPathX,mPathY;//运动一次位移长度
    private int mOrienX,mOrieny;//运动方向（正反）
    private boolean mState;//是否在运动
    
    public static int mCount=0;
    
    public Dice(int screenW,int screenH,Bitmap bmp){
    	mFace=new Face();
    	mLocation=new Location(screenW, screenH, bmp);
    	mScreenH=screenH;
    	mScreenW=screenW;
    	mState=false;
    	mCount++;
    }
    
    public boolean isValid(Dice dice){
      return this.mLocation.isValid(dice.mLocation);	
    }
    
    public int getLeft(){
    	return mLocation.getLeft();
    }
    
    public void setLeft(int left){
    	mLocation.setLeft(left);
    }
	
     
	public int getTop(){
		return mLocation.getTop();
	}
	
	public void setTop(int top){
	  mLocation.setTop(top);	
	}
	
	public Face getFace() {
		return mFace;
	}


	public int getScreenW() {
		return mScreenW;
	}

	public int getScreenH() {
		return mScreenH;
	}


	public int getPathX() {
		return mPathX;
	}

	public void setPathX(int pathX) {
		mPathX = pathX;
	}

	public int getPathY() {
		return mPathY;
	}

	public void setPathY(int pathY) {
		mPathY = pathY;
	}

	public int getOrienX() {
		return mOrienX;
	}

	public void setOrienX(int orienX) {
		mOrienX = orienX;
	}

	public int getOrieny() {
		return mOrieny;
	}

	public void setOrieny(int orieny) {
		mOrieny = orieny;
	}

	public boolean isState() {
		return mState;
	}

	public void setState(boolean state) {
		mState = state;
	}
    public int getPicWidth(){
    	return this.mLocation.getPicWidth();
    }

    public int getPicHeight(){
    	return this.mLocation.getPicHeight();
    }
}
