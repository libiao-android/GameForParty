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
		gameList.add(new GameInfo(R.drawable.randomgamelogo,"�����"));
		gameList.add(new GameInfo(R.drawable.logo,"ҡ����"));
		gameList.add(new GameInfo(R.drawable.app_zhangbing,"һ�޾���"));
		gameList.add(new GameInfo(R.drawable.tubiao01,"���Ļ���ð��"));
		gameList.add(new GameInfo(R.drawable.whodrink,"��˭��"));
		gameList.add(new GameInfo(R.drawable.ic_launcher,"�����ڴ�"));

		return gameList;
	}
	public static void runGame(Context con,String name,String address,int mode){
		//System.out.println("name======"+name);
		switch (mode) {
		
		case 0://һ��������Ϸ
			//System.out.println(mode);
			if(name.equals("�����")){
				maybeMyGame(con,address,RandomNumberGame.class);
			}
			if(name.equals("ҡ����")){
				maybeMyGame(con,address,DiceCastActivity.class);
			}
			if(name.equals("һ�޾���")){
				maybeMyGame(con,address,StupidActivity.class);
			}
			if(name.equals("���Ļ���ð��")){
				maybeMyGame(con,address,Welcome.class);
			}
			if(name.equals("��˭��")){
				maybeMyGame(con,address,WhoDrinkActivity.class);
			}
			
			break;
		case 1://��������Ϸ
			//System.out.println(mode);
			if(name.equals("�����")){
				maybeMyGame(con,address,RandomNumberGame.class);
			}
			if(name.equals("ҡ����")){
				maybeMyGame(con,address,DiceCastActivity.class);
			}
			if(name.equals("һ�޾���")){
				maybeMyGame(con,address,StupidActivity.class);
			}
			if(name.equals("���Ļ���ð��")){
				maybeMyGame(con,address,GreatAdventureActivity.class);
			}
			if(name.equals("��˭��")){
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
