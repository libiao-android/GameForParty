package com.nubia.gameforparty;

public class BluetoothInfo {
	private String mDeviceName;
	private String mAddress;
	private boolean mIsPair;
	private boolean mIsShowPb;
	public BluetoothInfo(String mDeviceName, String mAddress,boolean mIsPair,boolean mIsShowPb) {
		this.mDeviceName = mDeviceName;
		this.setmAddress(mAddress);
		this.mIsPair = mIsPair;
		this.setmIsShowPb(mIsShowPb);
	}
	public String getmDeviceName() {
		return mDeviceName;
	}
	public void setmDeviceName(String mDeviceName) {
		this.mDeviceName = mDeviceName;
	}
	public boolean ismIsPair() {
		return mIsPair;
	}
	public void setmIsPair(boolean mIsPair) {
		this.mIsPair = mIsPair;
	}
	public String getmAddress() {
		return mAddress;
	}
	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}
	public boolean ismIsShowPb() {
		return mIsShowPb;
	}
	public void setmIsShowPb(boolean mIsShowPb) {
		this.mIsShowPb = mIsShowPb;
	}
	
}
