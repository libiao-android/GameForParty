package com.nubia.gameforparty;

public class GameInfo {
	private int drawableId;
	private String gameName;
	public GameInfo(int drawableId, String gameName) {
		this.drawableId = drawableId;
		this.gameName = gameName;
	}
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
}
