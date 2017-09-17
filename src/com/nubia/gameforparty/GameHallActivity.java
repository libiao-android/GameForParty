package com.nubia.gameforparty;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.nubia.bluetooth.BluetoothUtil;


@SuppressLint("NewApi")
public class GameHallActivity extends Activity{
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private FragmentHall mFragmentHall;
	private FragmentPair mFragmentPair;
	private Button mBluetoothPairBtn;
	private Button mModifyBtn;
	private boolean mMotifyBtnFlag=true;
	private EditText mBluetoothEditText;
	private FragmentManager mFm = getFragmentManager();  
	private FragmentTransaction mTransaction ;
	private boolean mFlag=true;//Fragment切换标识
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamehall_view);
		
		setupViews();
		setDefaultFragment(); 

	}
	private void setDefaultFragment() {
		mTransaction = mFm.beginTransaction();
		mFragmentHall = new FragmentHall();  
		mTransaction.replace(R.id.content, mFragmentHall);  
		mTransaction.commit(); 

	}
	private void setupViews() {
		mBluetoothEditText=(EditText) findViewById(R.id.app_buttom_et_id);
		mModifyBtn=(Button) findViewById(R.id.app_bottom_btn_id);
		mModifyBtn.setOnClickListener(new MyClickListener());
		mBluetoothEditText.setEnabled(false);
		mBluetoothEditText.setText(mBluetoothAdapter.getName());
		BluetoothUtil util=new BluetoothUtil();
		util.openDiscover(this);
		mFragmentPair=new FragmentPair();
		mBluetoothPairBtn=(Button) findViewById(R.id.hall_title_btn_id);
		mBluetoothPairBtn.setOnClickListener(new MyClickListener() );
	}
	class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.app_bottom_btn_id:
				if(mMotifyBtnFlag){
					mModifyBtn.setText("确定");
					mBluetoothEditText.setEnabled(true);
					mBluetoothEditText.requestFocus();
					InputMethodManager imm=(InputMethodManager) mBluetoothEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
					mMotifyBtnFlag=false;
				}else{
					mBluetoothAdapter.setName(mBluetoothEditText.getText().toString());
					mModifyBtn.setText("修改");
					mBluetoothEditText.setEnabled(false);
					mBluetoothEditText.clearFocus();
					mMotifyBtnFlag=true;
				}
				break;
			case R.id.hall_title_btn_id:
//	mTransaction.setCustomAnimations(R.anim.slide_fragment_vertical_left_in, R.anim.slide_fragment_vertical_right_out,R.anim.slide_fragment_vertical_right_in, R.anim.slide_fragment_vertical_left_out);

				if (mFlag) {
				mBluetoothPairBtn.setBackgroundResource(R.drawable.frompair);
				mTransaction = mFm.beginTransaction();
				mTransaction.setCustomAnimations(R.anim.slide_in_down,R.anim.slide_out_down);
				mTransaction.replace(R.id.content, mFragmentPair);
				mTransaction.commit();
				mFlag=false;
			}else{
				mBluetoothPairBtn.setBackgroundResource(R.drawable.topair);
				mTransaction = mFm.beginTransaction();
				mTransaction.setCustomAnimations(R.anim.slide_in_up,R.anim.slide_out_up);
				mTransaction.replace(R.id.content, mFragmentHall);
				mTransaction.commit();
				mFlag=true;
			}
				break;

			default:
				break;
			}
		}
	}
}
