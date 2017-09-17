package com.nubia.zhuli;

import java.util.Random;

/*�������ӵ��˶��켣��ģ����ʵ
 * ��֤���Ӳ�Խ�磬�ص�
 * �����˶��ķ�����ٶ����
 * �����˶�ʱ��䳤���ٶȱ�С��ֱ��ֹͣ
 */
class Move {
    private Dice[] mDice;
    public static final int PADDING=50;//�����˶��߽���
    
    public Move(Dice[] dice) {
		// TODO Auto-generated constructor stub
    	mDice=dice;
	}
    //��ʼ�����ӵ��˶�������ٶȣ��Լ���ʼ������
    public void init(int speed){
        Random mRandom =new Random();
    	for(int i=0;i<Dices.NUM;i++){
    		mDice[i].setOrienX(mRandom.nextInt(2)==0?1:-1);
    		mDice[i].setOrieny(mRandom.nextInt(2)==0?1:-1);
    		mDice[i].setPathX(speed+20);
    		mDice[i].setPathY(speed+20);
    		mDice[i].getFace().setFaceValue(mRandom.nextInt(6));
    		mDice[i].setState(true);
    	}
    }
    
    public void start(){
    	for(int i=0;i<Dices.NUM;i++){
    		//�˶��ٶ�С��0��ֹͣ
    		if(mDice[i].getPathX()<=0 && mDice[i].getPathY()<=0){
    			//�ж�λ���Ƿ���Ч
    			if(!isValid(i)){
    				if(mDice[i].getPathX()==0){
    					mDice[i].setPathX(1);
    				}
    				if(mDice[i].getPathY()==0){
    					mDice[i].setPathY(1);
    				}
    			}else{
    				mDice[i].setState(false);
    			}
    		}
    		if(!mDice[i].isState()){
    			continue;
    		}else{
    			moveX(i);
    			if(!insideX(i)){
    				//�����������ߣ���任��������
    				if(mDice[i].getLeft()<=PADDING){
    					mDice[i].setOrienX(1);
    				}else if(mDice[i].getLeft()>=mDice[i].getScreenW()-PADDING-mDice[i].getPicWidth()){
    					mDice[i].setOrienX(-1);
    				}
    			}else if(inside(i)!=-1){ //�����������ص�
    				if(mDice[i].getLeft()<=mDice[inside(i)].getLeft()){
    					mDice[i].setOrienX(-1);
    				}else{
    					mDice[i].setOrienX(1);
    				}
    			}    		
    		moveY(i);
    		//YԽ�磬��������
    		if(!insideY(i)){
				//����������ϱߣ���任��������
				if(mDice[i].getTop()<=PADDING){
					mDice[i].setOrieny(1);
				}else if(mDice[i].getTop()>=mDice[i].getScreenH()-PADDING-mDice[i].getPicHeight()){
					mDice[i].setOrieny(-1);
				}
			}else if(inside(i)!=-1){ //�����������ص�
				if(mDice[i].getTop()<=mDice[inside(i)].getTop()){
					mDice[i].setOrieny(-1);
				}else{
					mDice[i].setOrieny(1);
				}
			}
    		}
    	} 
    }
    //x���˶�����
    private void moveX(int i){
    	mDice[i].setLeft(mDice[i].getLeft() + mDice[i].getOrienX() *
				mDice[i].getPathX());
        //�˶�����
    	if(mDice[i].getPathX()<1){
    		mDice[i].setPathX(0);
    	}else{
    		mDice[i].setPathX(mDice[i].getPathX() - 1);
    	}
    }
    //y���˶�����,ÿ���ٶȼ���1
    private void moveY(int i){
    	mDice[i].setTop(mDice[i].getTop()+mDice[i].getOrieny()*
    			mDice[i].getPathY());
        //�˶�����
    	if(mDice[i].getPathY()<1){
    		mDice[i].setPathY(0);
    	}else{
    		mDice[i].setPathY(mDice[i].getPathY()-1);
    	}
    }
    //x��Խ���ж�
    private boolean insideX(int i){
    	if(mDice[i].getLeft()<=(mDice[i].getScreenW()-mDice[i].getPicWidth()-PADDING)&&
    			mDice[i].getLeft()>=PADDING){
    		return true;
    	}
    	return false;
    }
    //Y��Խ���ж�
    private boolean insideY(int i){
    	if((mDice[i].getTop()<=mDice[i].getScreenH()-mDice[i].getPicHeight()-PADDING)&&
    			mDice[i].getTop()>=PADDING){
    		return true;
    	}
    	return false;
    }
    //�ж��Ƿ�����������ص������ص�����-1
    private int inside(int i){
    	for(int j=0;j<Dices.NUM;j++){
    		if((!mDice[i].isValid(mDice[j]))&&i!=j){
    			return j;
    		}
    	}
    	return -1;
    }
    //�ж�����λ���Ƿ���Ч
    private boolean isValid(int i){
    	return (insideX(i)&&insideY(i)&&(inside(i)==-1));
    }
    
    //�ж������Ƿ�ȫ����Ч
    public boolean isQuiet(){
    	for(int i=0;i<Dices.NUM;i++){
    		if(isValid(i)){
    			continue;
    		}
    		return false;
    	}
    	return true;
    }
    //�Ƿ�ȫ����ֹͣ�˶�
    public boolean isAllNotRun(){
    	for(int i=0;i<Dices.NUM;i++){
    		if(mDice[i].isState()){
    			return false;
    		}
    	}
    	return true;
    }
}
