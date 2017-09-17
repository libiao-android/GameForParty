package com.nubia.zhangbing;

import com.nubia.gameforparty.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class GameIntroActivity extends Activity implements OnClickListener {
	private ImageView mImaback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gameintroduce);
		mImaback = (ImageView) findViewById(R.id.gobackIntro);
		mImaback.setOnClickListener(GameIntroActivity.this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.gobackIntro:
			      finish();
		          break;
		
		
		}
		
		
		
	}

}
