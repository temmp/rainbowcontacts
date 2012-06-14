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
	 * ���ڹ����������豸
	 */
	private BluetoothAdapter m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

	private static final int JUMP = 1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prestart);
		 System.out.println("11");
		//����������������ȡ����״̬���������״̬����bluetoothonbefore�����
		if (m_BluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
			Constants_Global.BLUETOOTH_ON_BEFORE = true;
		Log.d("devicename", Build.MODEL);
		//�����ն������ҵ����ͺ�
		PhoneType.findtype(Build.MODEL);
		//�½��߳���ת���������Activity����Ҫ��������ʱ1500����
		new jumpThread(mHandler).start();
	        
	} 
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case JUMP:
				//�½�Intent��������ת
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
				//˯��1500����
				sleep(1500);
				//�����̷߳�����ת��Ϣ
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
