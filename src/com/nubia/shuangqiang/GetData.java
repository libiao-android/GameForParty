package com.nubia.shuangqiang;

public class GetData {
	private int[] mMessage1 = new int[4];
	private String mResult;
	public GetData(int[] message1){
		this.mMessage1 = message1;
	}
	
	public String getString(String title, int messageNO){
		int color;
		int value;
		String colorString;
		String valueString;
		//分别取得花色和数值
		color = mMessage1[messageNO] / 13;
		value = mMessage1[messageNO] % 13;
		if(color == 0)
			colorString = "黑桃";
		else if(color == 1)
			colorString = "红桃";
		else if(color == 2)
			colorString = "梅花";
		else if(color == 3)
			colorString = "方块";
		else
			colorString ="";
		
		if(value == 0)
			valueString = "A";
		else if(value == 1)
			valueString = "2";
		else if(value == 2)
			valueString = "3";
		else if(value == 3)
			valueString = "4";
		else if(value == 4)
			valueString = "5";
		else if(value == 5)
			valueString = "6";
		else if(value == 6)
			valueString = "7";
		else if(value == 7)
			valueString = "8";
		else if(value == 8)
			valueString = "9";
		else if(value == 9)
			valueString = "10";
		else if(value == 10)
			valueString = "J";
		else if(value == 11)
			valueString = "Q";
		else if(value == 12)
			valueString = "K";
		else {
			valueString ="";
		}
		
		if(mMessage1[messageNO] == 52)
			valueString = "小王";
		if(mMessage1[messageNO] == 53)
			valueString = "大王";
		
		mResult = title +"  "+ colorString+valueString;
		return mResult;
	}
	
	public String getString(String title, int messageNO_1, int messageNO_2){
		mResult = title+"  "+getString("", messageNO_1)+getString("", messageNO_2);
		return mResult;
	}
	
}
