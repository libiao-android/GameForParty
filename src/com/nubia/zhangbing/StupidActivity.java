package com.nubia.zhangbing;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nubia.bluetooth.ClientThread;
import com.nubia.gameforparty.R;

public class StupidActivity extends Activity implements OnClickListener {

	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private Intent mIntent;
	public static ClientThread mClientThread;
	private ImageView mImageTeamFeng, mImageTeamSha, mImageGoBack1, mImageHelp,
			mImageSetting1;

	public static String mAddress;
	private ProgressDialog mProgressDialog;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();

	private static class MyHandler extends Handler {
		WeakReference<StupidActivity> mActivity;

		public MyHandler(StupidActivity activity) {
			mActivity = new WeakReference<StupidActivity>(activity);
		}

		public void handleMessage(android.os.Message msg) {
			StupidActivity thisActivity = mActivity.get();
			switch (msg.what) {
			case 0:
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(), "连接成功",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(),
						"连接失败，请重新连接", Toast.LENGTH_LONG).show();
				thisActivity.finish();
				break;

			default:
				break;
			}
		}
	};

	private MyHandler mHandler = new MyHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chooseteam);

		Intent in = getIntent();
		mAddress = in.getStringExtra("add");
		if (mAddress != null) {
			BluetoothDevice device = mBluetoothAdapter
					.getRemoteDevice(mAddress);
			mClientThread = new ClientThread(device, mHandler);
			mClientThread.start();
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setTitle("稍等片刻");
			mProgressDialog.setMessage("正在连接中...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			mProgressDialog.setCanceledOnTouchOutside(false);
		}

		mImageTeamFeng = (ImageView) findViewById(R.id.teamFeng);
		mImageTeamSha = (ImageView) findViewById(R.id.teamSha);
		mImageGoBack1 = (ImageView) findViewById(R.id.goback1);
		mImageHelp = (ImageView) findViewById(R.id.help1);
		mImageSetting1 = (ImageView) findViewById(R.id.setting1);

		mSharedPreferences = getSharedPreferences("shareddata",
				Activity.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.commit();// 提交修改

		mImageTeamFeng.setOnClickListener(this);
		mImageTeamSha.setOnClickListener(this);
		mImageGoBack1.setOnClickListener(this);
		mImageHelp.setOnClickListener(this);
		mImageSetting1.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		if (mAddress != null) {
			if (mClientThread.getmSocket() != null) {
				try {
					mClientThread.getmSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mHandler = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			switch (v.getId()) {
			case R.id.teamFeng:
				//mIsTeamFeng = true;
				// 保存数据
				mEditor.putInt("teamFengIsPlaying", 1);
				mEditor.commit();// 提交修改
				mIntent = new Intent(StupidActivity.this,
						ChooseActionActivity.class);
				startActivity(mIntent);
				break;
			case R.id.teamSha:
				// 保存数据
				mEditor.putInt("teamFengIsPlaying", 0);
				mEditor.commit();// 提交修改
				mIntent = new Intent(StupidActivity.this,
						ChooseActionActivity.class);
				startActivity(mIntent);
				break;
			case R.id.goback1:
				this.finish();
				break;
			case R.id.help1:
				// 此处跳转到帧动画，播放游戏介绍
				mIntent = new Intent(StupidActivity.this,
						GameIntroActivity.class);
				startActivity(mIntent);
				break;
			case R.id.setting1:
				// 此处弹出dialog
				// Toast.makeText(this, "try dialog",Toast.LENGTH_SHORT);
				CustomDialog mDialog = new CustomDialog(StupidActivity.this);
				mDialog.show();
				break;
			}
		} catch (Exception e) { // big problems here.modify later
			e.printStackTrace();
		}
	}

}

class CustomDialog extends Dialog implements OnClickListener {
	private Context mContext;
	private TextView mViewScore1, mViewScore2;
	private int mTeam1Score;
	private int mTeam2Score;
	private ImageView mImaTeam1Dec, mImaTeam1Inc, mImaTeam2Dec, mImaTeam2Inc,
			mImaCacle, mImaSure;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	public CustomDialog(Context context) {
		super(context);
		mContext = context;
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.settings, null);
		this.setContentView(layout);

		mImaTeam1Dec = (ImageView) findViewById(R.id.scoredec1);
		mImaTeam1Inc = (ImageView) findViewById(R.id.scoreadd1);
		mImaTeam2Dec = (ImageView) findViewById(R.id.scoredec2);
		mImaTeam2Inc = (ImageView) findViewById(R.id.scoreadd2);
		mImaCacle = (ImageView) findViewById(R.id.cancle);
		mImaSure = (ImageView) findViewById(R.id.sure);

		mViewScore1 = (TextView) findViewById(R.id.dia_team1_score);
		mViewScore2 = (TextView) findViewById(R.id.dia_team2_score);

		mImaTeam1Dec.setOnClickListener(this);
		mImaTeam1Inc.setOnClickListener(this);
		mImaTeam2Dec.setOnClickListener(this);
		mImaTeam2Inc.setOnClickListener(this);
		mImaCacle.setOnClickListener(this);
		mImaSure.setOnClickListener(this);
		mSharedPreferences = mContext.getSharedPreferences("shareddata",
				Activity.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mTeam1Score = mSharedPreferences.getInt("teamFengScore", 0);
		mTeam2Score = mSharedPreferences.getInt("teamShaScore", 0);
		mViewScore1.setText(mTeam1Score + "");
		mViewScore2.setText(mTeam2Score + "");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.scoredec1:
			if (mTeam1Score >= 1) {
				mTeam1Score--;
				mViewScore1.setText(mTeam1Score + "");
			} else {
				Toast.makeText(mContext, "分数不可设定为负", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.scoreadd1:
			mTeam1Score++;
			mViewScore1.setText(mTeam1Score + "");
			break;
		case R.id.scoredec2:
			if (mTeam2Score >= 1) {
				mTeam2Score--;
				mViewScore2.setText(mTeam2Score + "");
			} else {
				Toast.makeText(mContext, "分数不可设定为负", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.scoreadd2:
			mTeam2Score++;
			mViewScore2.setText(mTeam2Score + "");
			break;
		case R.id.cancle:
			// 此处如何退出Dialog???
			// dismiss()???
			this.dismiss();
			break;
		case R.id.sure:
			// 保存数据
			mEditor.putInt("teamFengScore", mTeam1Score);
			mEditor.putInt("teamShaScore", mTeam2Score);
			mEditor.commit();// 提交修改
			// 退出Dialog???
			this.dismiss();
			break;
		}

	}

}
