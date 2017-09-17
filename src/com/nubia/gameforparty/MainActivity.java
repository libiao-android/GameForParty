package com.nubia.gameforparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView mTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mTime=(TextView) findViewById(R.id.time_tv_id);
		CountDownTimer timer=new CountDownTimer(4000,1000) {
			int i=3;
			@Override
			public void onTick(long millisUntilFinished) {
				mTime.setText(i+"");
				i--;
				
			}
			
			@Override
			public void onFinish() {
				Intent in=new Intent(MainActivity.this,GameHallActivity.class);
				startActivity(in);
				finish();
				
				
			}
		};
		timer.start();
		
	}



}
