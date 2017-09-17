package com.nubia.zhuli;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class ShakeListener implements SensorEventListener{
    //速度阈值,当摇晃速度达到后骰子开始转动
    private static final int SPEED_THREHORD=2000;
    //两次检测的时间间隔
    private static final int UPDATE_INTERVAL_TIME=70;
    //传感器管理器
    private SensorManager mSensorManager;
    //传感器
    private Sensor mSensor;
    //重力感应监听器
    private OnShakeListener mOnShakeListener;
    
    private final Context mContext; 
    //空间坐标
    private float lastX;
    private float lastY;
    private float lastZ;
    
    private long lastUpdatetime;
    
    public ShakeListener(Context c) {
		// TODO Auto-generated constructor stub
    	mContext=c;  	
    	start();
	}
    //开始
    public void start(){
    	//获得传感器
    	mSensorManager=(SensorManager)mContext.
    			getSystemService(Context.SENSOR_SERVICE);
    	if(mSensorManager!=null){
    		//获取重力传感器
    		mSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    	}
    	if(mSensor!=null){
    		mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_GAME);
    	}
    	
    }
    public void Stop(){
    	mSensorManager.unregisterListener(this);
    }
   
    
    public interface OnShakeListener{
    	 void onShake(int speed);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	 //重力感应器获得变化shuju
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//获取系统当前时间戳
		long currentTime=System.currentTimeMillis();
		//时间间隔
		long timeInterval=currentTime-lastUpdatetime;
		//判断是否达到设定的检测时间间隔
		if(timeInterval<UPDATE_INTERVAL_TIME){
			return;
		}
		lastUpdatetime=currentTime;
		//获取坐标
		float x=event.values[0];
		float y=event.values[1];
		float z=event.values[2];
		
		float changeX=x-lastX;
		float changeY=y-lastY;
		float changeZ=z-lastZ;
		
		lastX=x;
		lastY=y;
		lastZ=z;
		
		double speed=Math.sqrt(changeX*changeX+changeY*changeY+changeZ*changeZ)
				/timeInterval * 10000;		
		//达到速度阈值，开始运动
		if(speed>=SPEED_THREHORD){
			try{
			int currSpeed=(int)((speed-SPEED_THREHORD+50)/50);//保证currspeed大于0			
			mOnShakeListener.onShake(currSpeed);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	 public void setOnShakeListener(OnShakeListener listener){
	    	mOnShakeListener=listener;
	    }
}
