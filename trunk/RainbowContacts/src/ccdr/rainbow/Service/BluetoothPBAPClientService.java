package ccdr.rainbow.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ccdr.rainbow.Bluetooth.BluetoothPBAP;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Exception.BluetoothException;
import ccdr.rainbow.Tool.BluetoothHiddenMethod;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class BluetoothPBAPClientService extends Service {

	/**
	 * �Ƿ��һ������
	 */
	private boolean isFirstTime=true;
	/**
	 * Sevice�Ƿ�������
	 */
	private boolean isDestroyed=false;
	/**
	 * ��������������
	 */
	private BluetoothAdapter m_BluetoothAdapter;
	/**
	 * Զ�������豸
	 */
	public BluetoothDevice m_BluetoothDevice;
	/**
	 * ����������Activity������binder
	 */
	private final IBinder m_Binder=new BluetoothPBAPBinder();
	/**
	 * PBAP������
	 */
	private BluetoothPBAP m_BluetoothPBAP;
	/**
	 * PBAP��ʼ�����߳�
	 */
	private Thread m_PBAPInitThread=null;
	/**
	 * �̳�Binder���ڲ��෵���ⲿ�����ʵ�������ڵ����ⲿ�������
	 * @author Administrator
	 */
	public class BluetoothPBAPBinder extends Binder
	{
		public BluetoothPBAPClientService getService()
		{
			return BluetoothPBAPClientService.this;
		}
	}
	/**
	 * ����binder��Activity����
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return m_Binder;
	}

	/**
	 * Activity���õ�����Ϣ
	 * @author Administrator
	 *
	 */
	public class MSG
	{
		/**
		 * ����Activity�������ɳ�ʼ��
		 */
		public static final int INIT_PROGRESS_DIALOG=100;
		/**
		 * ����Activity�������ɸ���
		 */
		public static final int UPDATE_PROGRESS_DIALOG=101;
	}
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MSG.UPDATE_PROGRESS_DIALOG:
				Intent i_update_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR);
				i_update_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.CURRENT_PHONEBOOK_COUNT, m_BluetoothPBAP.getCurrentPhonebookCount());
				sendBroadcast(i_update_dlg);
				break;
			case MSG.INIT_PROGRESS_DIALOG:
				Intent i_init_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_BLUETOOTH_PBAP_PROGRESS_BAR);
				i_init_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.TOTAL_PHONEBOOK_COUNT, m_BluetoothPBAP.getTotalPhonebookCount());
				sendBroadcast(i_init_dlg);
				break;
				default:break;
			}
		}
	};
	/**
	 * ��һ�ε���serviceʱִ�У���service�������У��ٴε�����ֱ��ִ��onStartCommand
	 */
	@Override
	public void onCreate()
	{
		Log.i("aaa", "onCreate-PBAPClientService");
//		BluetoothPBAP=new BluetoothPBAP(this);
		m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	}
	
	/**
	 * �൱�ھɰ汾��onStart��ÿ�ε���service����ִ��
	 */
	@SuppressWarnings("finally")
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.i("aaa", "onStart-PBAPClientService");
		//��ȡActivity�����Ĳ���intent
		final Bundle param=intent.getExtras();
		if(isFirstTime)
		{
			m_PBAPInitThread=new Thread()
			{
				@Override
				public void run()
				{
					isFirstTime=false;
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
					
			Log.i("aaa", "��һ������Service");
			m_BluetoothDevice=(BluetoothDevice)param.getParcelable(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE);
			boolean init_success=true;
			try {
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
				
				m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
			} catch (BluetoothException e1) {
				// TODO Auto-generated catch block
				init_success=false;
				e1.printStackTrace();Log.e("aaa", e1.getMessage());
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
				
				//���´��������������ٴδ���
				m_BluetoothAdapter.disable();
				Log.i("aaa", "�ر�����");
				while(m_BluetoothAdapter.getState()!=BluetoothAdapter.STATE_OFF)
				{
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
					
				}
				m_BluetoothAdapter.enable();
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
				
				Log.i("aaa", "������");
				while(m_BluetoothAdapter.getState()!=BluetoothAdapter.STATE_ON)
				{
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
					
				}
				try {
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
					
					m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
					init_success=true;
					Log.i("aaa", "���´���PBAP�ͻ��˲�����ɹ�");
				} catch (BluetoothException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("aaa", "���´���PBAP�ͻ��˲�����ʧ�ܣ�����"+e.getMessage());
					try {
						try {
							
							if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
							
						Log.d("aaa", "��ʼȡ�����");
						if(!BluetoothHiddenMethod.RemoveBondWithTimeout(m_BluetoothDevice,7000))
						{
							Log.e("aaa","ȡ���������ʧ�ܻ�ʱ������");
						}
						Log.i("aaa", "ȡ����Գɹ�");
					} catch (IllegalArgumentException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					} catch (SecurityException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					} catch (ClassNotFoundException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					} catch (IllegalAccessException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					} catch (InvocationTargetException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					} catch (NoSuchMethodException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "ȡ������쳣������"+e11.getMessage());
					}
						/**
						 * ��ʱû��
						 */
						while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_NONE)
		    			{
							
							if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
							
		    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
		    				try {
								Thread.sleep(1000);
							} catch (InterruptedException e11) {
								// TODO Auto-generated catch block
								e11.printStackTrace();
							}
		    			}
					
//					try {
//						Log.i("aaa", "��ʼ�������");
//						BluetoothHiddenMethod.CreateBond(m_BluetoothDevice);
//						Log.i("aaa", "������Գɹ�");
						
//						while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_BONDED)
//		    			{
//		    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
//		    				try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e111) {
//								// TODO Auto-generated catch block
//								Log.e("aaa", "��Զ�������豸��ʧ�ܣ�����"+e111.getMessage());
//								sendBroadcast(new Intent(Constants_Interaction.Action.Bluetooth.BOND_REMOTE_DEVICE_FAILED));
//////				    			m_Handler.obtainMessage(CONNECT_REMOTEDEVICE_FAILED).sendToTarget();
//								e111.printStackTrace();
//							}
//		    			}
						
						if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
						
						m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
						init_success=true;
					} catch (BluetoothException e2) {
						// TODO Auto-generated catch block
						Log.e("aaa", "������Ժ󴴽�PBAP�ͻ���ʧ�ܣ�����"+e2.getMessage());
						e2.printStackTrace();
					}
				}
				
			}
//			finally
//			{			
//				isFirstTime=false;		
//			}
			
			if(getIsDestroyed()){Log.i("aaa", "PBAP��ʼ���߳�������");return;}
			
			if(init_success)
			{
			Log.i("aaa", "PBAP��ʼ���ɹ�");
			Intent i_init=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_SUCCESS);
			sendBroadcast(i_init);
			}
			else
			{	
				Intent i_init=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_FAILED);
				sendBroadcast(i_init);
			}
			
			
				}
			};
			m_PBAPInitThread.start();
			
			return START_STICKY;
		}
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION);
		switch(option)
		{
		case Constants_Bluetooth.Option.CONNECT_REMOTE_DEVICE:	
			Log.i("aaa", "��PBAP����˽�������");
			new Thread() {
				@Override
				public void run() {
					// �����������������ӣ�������һ��connection ID
					try {
						m_BluetoothPBAP.connectForPBAPService();
						Intent i_coon = new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_SUCCESS);
						sendBroadcast(i_coon);
					} catch (BluetoothException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("aaa", e.getMessage());
						Intent i_coon = new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_FAILED);
						sendBroadcast(i_coon);
					}
				}
			}.start();
			break;
		/**
		 * ��ȡ��ϵ������
		 */
		case Constants_Bluetooth.Option.GET_REMOTE_DEVICE_CONTACTS_COUNT:
			new Thread()
			{
				@Override
				public void run()
				{
			int count=-1;
			try {
				count = m_BluetoothPBAP.getAllContactsCount();
				Intent i_count=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT);
				i_count.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.REMOTE_DEVICE_CONTACTS_COUNT, count);
				sendBroadcast(i_count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}.start();
			break;
			/**
			 * ��ȡ������ϵ��
			 */
		case Constants_Bluetooth.Option.GET_REMOTE_DEVICE_PHONEBOOK_DATA:
			Log.i("aaa", "��ʼ��ȡ�绰������");
			final String file_path=param.getString(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.PBAP_RECEIVE_FILE_PATH);
			new Thread()
			{
				@Override
				public void run()
				{
			try {
				m_BluetoothPBAP.getAllContactsCountPro(m_Handler);
				m_BluetoothPBAP.getPhonebookDataPro(new File(file_path),m_Handler);
				Intent i_data=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA);
				sendBroadcast(i_data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}.start();
			break;
			/**
			 * �жϵ绰������
			 */
//		case Constants_Bluetooth.Option.ABORT_GETTING_PHONEBOOK_DATA:
//			m_BluetoothPBAP.abortGettingPhonebookData();
//			m_BluetoothPBAP.disconnectToServer();
//			Intent i_abort=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.ABORT_GETTING_PHONEBOOK_DATA_SUCCESS);
//			sendBroadcast(i_abort);
//			break;
			/**
			 * �ر�����
			 */
		case Constants_Bluetooth.Option.DISCONNECT_REMOTE_DEVICE:
			//�Ͽ�PBAP����
			m_BluetoothPBAP.disconnectToServer();
			Intent i_disconnect=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.DISCONNECT_REMOTE_DEVICE_SUCCESS);
			sendBroadcast(i_disconnect);
			break;
			
			default:break;
		}
		//���ظ�ֵ��ʶҪͨ����ʾ����/ֹͣservice
		return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		setIsDestroyed(true);
		if(null!=m_BluetoothPBAP)
		{
			m_BluetoothPBAP.disconnectToServer();
			m_BluetoothPBAP=null;
		}
		m_PBAPInitThread.interrupt();
		m_PBAPInitThread=null;
		super.onDestroy();
	}
	
	/**
	 * ����Sevice�Ƿ������٣������ж��Ƿ�ֹͣ�߳�
	 * @param b
	 */
	public void setIsDestroyed(boolean b)
	{
		isDestroyed=b;
	}
	/**
	 * ��ȡService�Ƿ������٣������ж��Ƿ�ֹͣ�߳�
	 * @return
	 */
	public boolean getIsDestroyed()
	{
		return isDestroyed;
	}
}
