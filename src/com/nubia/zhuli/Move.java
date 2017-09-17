package com.nubia.zhuli;

import java.util.Random;

/*计算骰子的运动轨迹，模拟现实
 * 保证骰子不越界，重叠
 * 骰子运动的方向和速度随机
 * 随着运动时间变长，速度变小，直至停止
 */
class Move {
    private Dice[] mDice;
    public static final int PADDING=50;//骰子运动边界宽度
    
    public Move(Dice[] dice) {
		// TODO Auto-generated constructor stub
    	mDice=dice;
	}
    //初始化骰子的运动方向和速度，以及初始化点数
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
    		//运动速度小于0，停止
    		if(mDice[i].getPathX()<=0 && mDice[i].getPathY()<=0){
    			//判断位置是否有效
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
    				//如果超出最左边，则变换方向向右
    				if(mDice[i].getLeft()<=PADDING){
    					mDice[i].setOrienX(1);
    				}else if(mDice[i].getLeft()>=mDice[i].getScreenW()-PADDING-mDice[i].getPicWidth()){
    					mDice[i].setOrienX(-1);
    				}
    			}else if(inside(i)!=-1){ //和其他骰子重叠
    				if(mDice[i].getLeft()<=mDice[inside(i)].getLeft()){
    					mDice[i].setOrienX(-1);
    				}else{
    					mDice[i].setOrienX(1);
    				}
    			}    		
    		moveY(i);
    		//Y越界，矫正方向
    		if(!insideY(i)){
				//如果超出最上边，则变换方向向下
				if(mDice[i].getTop()<=PADDING){
					mDice[i].setOrieny(1);
				}else if(mDice[i].getTop()>=mDice[i].getScreenH()-PADDING-mDice[i].getPicHeight()){
					mDice[i].setOrieny(-1);
				}
			}else if(inside(i)!=-1){ //和其他骰子重叠
				if(mDice[i].getTop()<=mDice[inside(i)].getTop()){
					mDice[i].setOrieny(-1);
				}else{
					mDice[i].setOrieny(1);
				}
			}
    		}
    	} 
    }
    //x轴运动计算
    private void moveX(int i){
    	mDice[i].setLeft(mDice[i].getLeft() + mDice[i].getOrienX() *
				mDice[i].getPathX());
        //运动减速
    	if(mDice[i].getPathX()<1){
    		mDice[i].setPathX(0);
    	}else{
    		mDice[i].setPathX(mDice[i].getPathX() - 1);
    	}
    }
    //y轴运动计算,每次速度减少1
    private void moveY(int i){
    	mDice[i].setTop(mDice[i].getTop()+mDice[i].getOrieny()*
    			mDice[i].getPathY());
        //运动减速
    	if(mDice[i].getPathY()<1){
    		mDice[i].setPathY(0);
    	}else{
    		mDice[i].setPathY(mDice[i].getPathY()-1);
    	}
    }
    //x轴越界判断
    private boolean insideX(int i){
    	if(mDice[i].getLeft()<=(mDice[i].getScreenW()-mDice[i].getPicWidth()-PADDING)&&
    			mDice[i].getLeft()>=PADDING){
    		return true;
    	}
    	return false;
    }
    //Y轴越界判断
    private boolean insideY(int i){
    	if((mDice[i].getTop()<=mDice[i].getScreenH()-mDice[i].getPicHeight()-PADDING)&&
    			mDice[i].getTop()>=PADDING){
    		return true;
    	}
    	return false;
    }
    //判断是否和其他骰子重叠，不重叠返回-1
    private int inside(int i){
    	for(int j=0;j<Dices.NUM;j++){
    		if((!mDice[i].isValid(mDice[j]))&&i!=j){
    			return j;
    		}
    	}
    	return -1;
    }
    //判断骰子位置是否有效
    private boolean isValid(int i){
    	return (insideX(i)&&insideY(i)&&(inside(i)==-1));
    }
    
    //判断骰子是否全部有效
    public boolean isQuiet(){
    	for(int i=0;i<Dices.NUM;i++){
    		if(isValid(i)){
    			continue;
    		}
    		return false;
    	}
    	return true;
    }
    //是否全部都停止运动
    public boolean isAllNotRun(){
    	for(int i=0;i<Dices.NUM;i++){
    		if(mDice[i].isState()){
    			return false;
    		}
    	}
    	return true;
    }
}
