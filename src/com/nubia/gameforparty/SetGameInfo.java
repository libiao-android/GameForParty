package com.nubia.gameforparty;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.nubia.game.RandomNumberGame;
import com.nubia.gameforparty.R;
import com.nubia.lijia.GreatAdventureActivity;
import com.nubia.lijia.Welcome;
import com.nubia.shuangqiang.WhoDrinkActivity;
import com.nubia.zhangbing.StupidActivity;
import com.nubia.zhuli.DiceCastActivity;

public class SetGameInfo {

	public static List<GameInfo> getGameInfo(){

		List<GameInfo> gameList=new ArrayList<GameInfo>();
		gameList.add(new GameInfo(R.drawable.randomgamelogo,"随机数"));
		gameList.add(new GameInfo(R.drawable.logo,"摇骰子"));
		gameList.add(new GameInfo(R.drawable.app_zhangbing,"一愚惊人"));
		gameList.add(new GameInfo(R.drawable.tubiao01,"真心话大冒险"));
		gameList.add(new GameInfo(R.drawable.whodrink,"该谁喝"));
		gameList.add(new GameInfo(R.drawable.ic_launcher,"敬请期待"));

		return gameList;
	}
	public static void runGame(Context con,String name,String address,int mode){
		//System.out.println("name======"+name);
		switch (mode) {
		
		case 0://一个人玩游戏
			//System.out.println(mode);
			if(name.equals("随机数")){
				maybeMyGame(con,address,RandomNumberGame.class);
			}
			if(name.equals("摇骰子")){
				maybeMyGame(con,address,DiceCastActivity.class);
			}
			if(name.equals("一愚惊人")){
				maybeMyGame(con,address,StupidActivity.class);
			}
			if(name.equals("真心话大冒险")){
				maybeMyGame(con,address,Welcome.class);
			}
			if(name.equals("该谁喝")){
				maybeMyGame(con,address,WhoDrinkActivity.class);
			}
			
			break;
		case 1://连接玩游戏
			//System.out.println(mode);
			if(name.equals("随机数")){
				maybeMyGame(con,address,RandomNumberGame.class);
			}
			if(name.equals("摇骰子")){
				maybeMyGame(con,address,DiceCastActivity.class);
			}
			if(name.equals("一愚惊人")){
				maybeMyGame(con,address,StupidActivity.class);
			}
			if(name.equals("真心话大冒险")){
				maybeMyGame(con,address,GreatAdventureActivity.class);
			}
			if(name.equals("该谁喝")){
				maybeMyGame(con,address,WhoDrinkActivity.class);
			}
			break;

		default:
			break;
		}
	}
	private static void maybeMyGame(Context con, String address,Class<?> className) {
		if(address==null){
			Intent in=new Intent(con,className);
			con.startActivity(in);
		}else{
			Intent in=new Intent(con,className);
			in.putExtra("add", address);
			con.startActivity(in);
		}
		
	}

}
