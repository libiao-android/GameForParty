package com.nubia.zhuli;

import java.util.Random;
import android.graphics.Bitmap;

class Location {
     private int left; 
     private int top;
     private int picWidth;
     private int picHeight;
     
     
     public Location(int screenW,int screenH,Bitmap bmp) {
		// TODO Auto-generated constructor stub
		 Random mRandom=new Random();
    	 this.picWidth=bmp.getWidth();
    	 this.picHeight=bmp.getHeight();
    	 //随机产生骰子初始位置
    	 this.left=mRandom.nextInt(screenW-picWidth-Move.PADDING*2)+Move.PADDING;
    	 this.top=mRandom.nextInt(screenH-picHeight-Move.PADDING*2)+Move.PADDING;
	}
     //判断两个骰子是否重叠,false位重叠
     public boolean isValid(Location location){
    	 if(Math.abs(left-location.left)<picWidth && Math.abs(top-location.top)<picHeight){
    		 return false;
    	 }
    	 return true;
     }
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}
	public int getPicHeight() {
		return picHeight;
	}
	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}
     
}
