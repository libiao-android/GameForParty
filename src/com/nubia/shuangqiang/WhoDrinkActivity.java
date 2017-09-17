package com.nubia.shuangqiang;

import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nubia.bluetooth.BluetoothUtil;
import com.nubia.bluetooth.ClientThread;
import com.nubia.gameforparty.R;
import com.nubia.gameforparty.R.drawable;


//Activty程序
public class WhoDrinkActivity extends Activity {

	//定义抽取按钮
	private Button mButton_draw;
	private Button mButton_round1;
	private Button mButton_round2;
	private Button mBbutton_round3;
	//count用于记录游戏的状态
	private int mCount=0;
	//大显示窗口及四个缩略图矿口
	private ImageView mImageBig; 
	private ImageView[] mImageSmall = new ImageView[4];
	//抽取的四组数据
	private int[] mMessage1 = new int[4];

	//记录图片的ID
	private int[] mPicture = new int[]{
			drawable.a_1, drawable.a_2, drawable.a_3,drawable.a_4,
			drawable.a_5, drawable.a_6, drawable.a_7,drawable.a_8,
			drawable.a_9, drawable.a_10, drawable.a_11,drawable.a_12,
			drawable.a_13,
			drawable.b_1, drawable.b_2, drawable.b_3,drawable.b_4,
			drawable.b_5, drawable.b_6, drawable.b_7,drawable.b_8,
			drawable.b_9, drawable.b_10, drawable.b_11,drawable.b_12,
			drawable.b_13,
			drawable.c_1, drawable.c_2, drawable.c_3,drawable.c_4,
			drawable.c_5, drawable.c_6, drawable.c_7,drawable.c_8,
			drawable.c_9, drawable.c_10, drawable.c_11,drawable.c_12,
			drawable.c_13,
			drawable.d_1, drawable.d_2, drawable.d_3,drawable.d_4,
			drawable.d_5, drawable.d_6, drawable.d_7,drawable.d_8,
			drawable.d_9, drawable.d_10, drawable.d_11,drawable.d_12,
			drawable.d_13,
			drawable.k_s, drawable.k_b
			};
	private ClientThread mClientThread;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private BluetoothUtil mUtil;
	private String mAddress;
	private ProgressDialog mProgressDialog;
	private static class MyHandler extends Handler{
		 
		private  Bundle mBundle; 
		WeakReference<WhoDrinkActivity> mActivity;
		public MyHandler(WhoDrinkActivity activity){
			mActivity=new WeakReference<WhoDrinkActivity>(activity);
		}
		public void handleMessage(android.os.Message msg) {
			WhoDrinkActivity thisActivity=mActivity.get();
			if(msg.what == 0x1233){
				mBundle = msg.getData();
				thisActivity.mMessage1 = mBundle.getIntArray("data");
				for(int i = 0;i < 4;i++)
					thisActivity.mImageSmall[i].setEnabled(true);
				for(int i = 0; i < 4; i++){
					thisActivity.mImageBig.setVisibility(View.VISIBLE);
					//将ImageView进行刷新
					thisActivity.mImageBig.setImageResource(R.drawable.k_n);
					//将四个ImageView全部设为可见
					thisActivity.mImageSmall[i].setVisibility(View.VISIBLE);
					//通过AnimationUtils得到动画配置文件，设置前半转动画，其中画面为背面
					thisActivity.mImageSmall[i].setImageResource(R.drawable.back);
					Animation animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate);
					thisActivity.mImageSmall[i].startAnimation(animation);
					//设置动画的后半转，其中画面为纸牌正面
					thisActivity.mImageSmall[i].setImageResource(thisActivity.mPicture[thisActivity.mMessage1[i]]);
					animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate_back);
					thisActivity.mImageSmall[i].startAnimation(animation);
					
					//计数器更新为1，更新重新抽牌数据
					thisActivity.mCount=1;
					thisActivity.mButton_draw.setEnabled(false);
					thisActivity.mButton_round1.setEnabled(true);
					thisActivity.mButton_round2.setEnabled(false);
					thisActivity.mBbutton_round3.setEnabled(false);
					thisActivity.mOnClickListenerImageView.changeMessage(thisActivity.mMessage1, thisActivity.mCount);
				}
				
			}else if(msg.what == 0x1234){
				mBundle = msg.getData();
				int drawable = mBundle.getInt("data");
				
				if(thisActivity.mCount == 1 ){
					//通过AnimationUtils得到动画配置文件，设置前半转动画，其中画面为背面
					thisActivity.mImageBig.setImageResource(R.drawable.back);
					Animation animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate);
					thisActivity.mImageBig.startAnimation(animation);
					//设置动画的后半转，其中画面为纸牌正面
					thisActivity.mImageBig.setImageResource(drawable);
					animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate_back);
					thisActivity.mImageBig.startAnimation(animation);
					
					thisActivity.mButton_draw.setEnabled(false);
					thisActivity.mButton_round1.setEnabled(false);
					thisActivity.mButton_round2.setEnabled(true);
					thisActivity.mBbutton_round3.setEnabled(false);
				}else if(thisActivity.mCount == 2){
					//通过AnimationUtils得到动画配置文件，设置前半转动画，其中画面为背面
					thisActivity.mImageBig.setImageResource(R.drawable.back);
					Animation animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate);
					thisActivity.mImageBig.startAnimation(animation);
					//设置动画的后半转，其中画面为纸牌正面
					thisActivity.mImageBig.setImageResource(drawable);
					animation = AnimationUtils.loadAnimation(thisActivity, R.anim.rotate_back);
					thisActivity.mImageBig.startAnimation(animation);
					
					thisActivity.mButton_draw.setEnabled(false);
					thisActivity.mButton_round1.setEnabled(false);
					thisActivity.mButton_round2.setEnabled(false);
					thisActivity.mBbutton_round3.setEnabled(true);
					
				}
				else if(thisActivity.mCount == 3){
					//最后两轮将ImageBig设置为不可见
					thisActivity.mImageBig.setVisibility(View.INVISIBLE);
					
					thisActivity.mButton_draw.setEnabled(true);
					thisActivity.mButton_round1.setEnabled(false);
					thisActivity.mButton_round2.setEnabled(false);
					thisActivity.mBbutton_round3.setEnabled(false);
					
					for(int i = 0;i < 4;i++)
						thisActivity.mImageSmall[i].setEnabled(false);
				}
				//根据计数器判断下面按钮的可见性
				thisActivity.mCount++;
			}
			
			if(msg.what==0){
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(), "连接成功", Toast.LENGTH_LONG).show();
			}
			if(msg.what==1){
				thisActivity.mProgressDialog.cancel();
				Toast.makeText(thisActivity.getApplicationContext(), "连接失败，请重新连接", Toast.LENGTH_LONG).show();
				thisActivity.finish();
				
			}
			if(msg.what==2){
				if(thisActivity.mAddress!=null){
					thisActivity.mUtil.sendMessage(thisActivity.mClientThread.getmSocket(), msg.obj.toString());
				}
				
			}
		}
	};
	private MyHandler mHandler=new MyHandler(this);
	private OnClickListenerImageView mOnClickListenerImageView = new OnClickListenerImageView();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_who_drink);
		mUtil=new BluetoothUtil();
		Intent in=getIntent();
		mAddress=in.getStringExtra("add");
		if(mAddress!=null){
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mAddress);
			mClientThread=new ClientThread(device,mHandler); 
			mClientThread.start();
			mProgressDialog = new ProgressDialog(this);  
			mProgressDialog.setTitle("稍等片刻");  
			mProgressDialog.setMessage("正在连接中...");  
			mProgressDialog.setIndeterminate(true);  
			mProgressDialog.setCancelable(true);  
			mProgressDialog.show();
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		//获取各个按钮
		mButton_draw = (Button) findViewById(R.id.button02);
		
		mButton_round1 = (Button) findViewById(R.id.button12);
		mButton_round2 = (Button) findViewById(R.id.button22);
		mBbutton_round3 = (Button) findViewById(R.id.button32);
		//获取各个窗口
		mImageBig = (ImageView) findViewById(R.id.imageView01);
		mImageSmall[0] = (ImageView) findViewById(R.id.imageView11);
		mImageSmall[1] = (ImageView) findViewById(R.id.imageView12);
		mImageSmall[2] = (ImageView) findViewById(R.id.imageView13);
		mImageSmall[3] = (ImageView) findViewById(R.id.imageView14);
		//imageSmall[1].setVisibility(View.INVISIBLE);
		mImageSmall[0].setOnClickListener(mOnClickListenerImageView);
		mImageSmall[1].setOnClickListener(mOnClickListenerImageView);
		mImageSmall[2].setOnClickListener(mOnClickListenerImageView);
		mImageSmall[3].setOnClickListener(mOnClickListenerImageView);
		//对监听器状态进行静态更新
		mOnClickListenerImageView.setState(this, mPicture, mHandler);
		//只将抽取动作作为可见
		if(mCount == 0){
			mButton_draw.setEnabled(true);
			mButton_round1.setEnabled(false);
			mButton_round2.setEnabled(false);
			mBbutton_round3.setEnabled(false);
		}
	}
	
	//实现button的点击监听器
	public void clickButtonCheck(View v){
		
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setTitle("重新发牌").setIcon(R.drawable.check).
			setMessage("点击确定按钮将会重新发牌···" +
					"当一局没有玩完之前，切勿重新发牌，你的酒友会很失望的！！！");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					MyThread mth = new MyThread(mHandler);
					mth.start();
				}
			});
			setNegativeButton(builder).create().show();
	}
	
	
	//第一轮出牌监听器
	public void clickButtonRound1(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setTitle("第一轮规则").setIcon(R.drawable.round1).
			setMessage("玩家出一张牌，四个玩家中出牌最小者喝酒···" +
					"但是如果有两个及两个以上玩家出牌一样，则出牌一样的玩家喝酒！！！");
		
		setPositiveButton(builder);
		setNegativeButton(builder).create().show();
	}
	
	//第二轮出牌监听器
	public void clickButtonRound2(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setTitle("第二轮规则").setIcon(R.drawable.round2).
			setMessage("玩家出一张牌，顺次累加猜测四张牌的不超过的点数，由玩家的下家判断上位玩家是否超过点数···" +
					"若超过，前位玩家喝酒，若没有超过则后一位玩家喝酒，后位玩家也可以选择不猜测，继续累加点数！！！");
		setPositiveButton(builder);
		setNegativeButton(builder).create().show();
	}
	
	//第三轮出牌监听器
	public void clickButtonRound3(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setTitle("第三轮规则").setIcon(R.drawable.round3).
			setMessage("玩家假想一张牌，加上手上仅剩的两张牌组成金花，" +
					"想要谁喝酒就和另一位玩家比大小，" +
					"若玩家认怂，则罚酒一杯，若玩家不服，可加罚喝酒量，然后开牌！！！");
		setPositiveButton(builder);
		setNegativeButton(builder).create().show();
	}
	
	private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder){
		return builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				
			}
		});
	}
	
	private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder){
		return builder.setNegativeButton("取消",new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				
			}
		});
	}
	@Override
	protected void onDestroy() {
		if(mAddress!=null){
			if(mClientThread.getmSocket()!=null){
				try {
					mClientThread.getmSocket().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mHandler=null;
		
		super.onDestroy();
	}
}










