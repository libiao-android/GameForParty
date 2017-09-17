package com.nubia.zhuli;

import java.util.Random;
import com.nubia.gameforparty.R;
class Face {
   private int mFaceValue;
   //骰子点数
   public final static int[] face={R.drawable.value1,R.drawable.value2,
		   R.drawable.value3,R.drawable.value4,R.drawable.value5,R.drawable.value6};
   //旋转图片
   public final static int[] rollface={R.drawable.roll1,R.drawable.roll2,
		   R.drawable.roll3,R.drawable.roll4,R.drawable.roll5,
		   R.drawable.roll6,R.drawable.roll7,R.drawable.roll8,
		   R.drawable.roll9,};
   
   public Face(){
      Random mRandom=new Random();
	   mFaceValue=mRandom.nextInt(6); //随机抽取一个点数
   }

   public int getFaceValue() {
	return mFaceValue;
   }

   public void setFaceValue(int faceValue) {
	mFaceValue = faceValue;
   }  
   }
