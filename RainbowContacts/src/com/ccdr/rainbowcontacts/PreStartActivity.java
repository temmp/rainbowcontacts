package com.ccdr.rainbowcontacts;

import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Tool.PhoneType;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PreStartActivity extends Activity {
	/**
	 * 用于管理本地蓝牙设备
	 */
	private BluetoothAdapter m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

	private static final int JUMP = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prestart);
		 System.out.println("11");
		//根据蓝牙适配器获取蓝牙状态，根据其打开状态设置bluetoothonbefore的真假
		if (m_BluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
			Constants_Global.BLUETOOTH_ON_BEFORE = true;
		Log.d("devicename", Build.MODEL);
		//根据终端名字找到其型号
		PhoneType.findtype(Build.MODEL);
		//新建线程跳转到主界面的Activity，主要作用是延时1500毫秒
		new jumpThread(mHandler).start();
	        
	} 
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JUMP:
				//新建Intent并设置跳转
				Intent in = new Intent(PreStartActivity.this, RainbowContactsActivity.class);
				in.putExtra("Flagone", "right");
				startActivity(in);

				break;

			}
		}
	};

	class jumpThread extends Thread {

		Handler mhandler;

		jumpThread(Handler mhandler) {
			this.mhandler = mhandler;
		}

		@Override
		public void run() {
			try {
				//睡眠1500毫秒
				sleep(1500);
				//向主线程发送跳转消息
				mHandler.obtainMessage(JUMP).sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}
}
