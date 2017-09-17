package com.nubia.zhuli;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class ShakeListener implements SensorEventListener{
    //�ٶ���ֵ,��ҡ���ٶȴﵽ�����ӿ�ʼת��
    private static final int SPEED_THREHORD=2000;
    //���μ���ʱ����
    private static final int UPDATE_INTERVAL_TIME=70;
    //������������
    private SensorManager mSensorManager;
    //������
    private Sensor mSensor;
    //������Ӧ������
    private OnShakeListener mOnShakeListener;
    
    private final Context mContext; 
    //�ռ�����
    private float lastX;
    private float lastY;
    private float lastZ;
    
    private long lastUpdatetime;
    
    public ShakeListener(Context c) {
		// TODO Auto-generated constructor stub
    	mContext=c;  	
    	start();
	}
    //��ʼ
    public void start(){
    	//��ô�����
    	mSensorManager=(SensorManager)mContext.
    			getSystemService(Context.SENSOR_SERVICE);
    	if(mSensorManager!=null){
    		//��ȡ����������
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
	 //������Ӧ����ñ仯shuju
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//��ȡϵͳ��ǰʱ���
		long currentTime=System.currentTimeMillis();
		//ʱ����
		long timeInterval=currentTime-lastUpdatetime;
		//�ж��Ƿ�ﵽ�趨�ļ��ʱ����
		if(timeInterval<UPDATE_INTERVAL_TIME){
			return;
		}
		lastUpdatetime=currentTime;
		//��ȡ����
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
		//�ﵽ�ٶ���ֵ����ʼ�˶�
		if(speed>=SPEED_THREHORD){
			try{
			int currSpeed=(int)((speed-SPEED_THREHORD+50)/50);//��֤currspeed����0			
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
