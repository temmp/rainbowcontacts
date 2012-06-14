package ccdr.rainbow.Service;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Wifi.WifiServer;


public class WifiServerService extends Service{
	/**
	 * ���ڹ�����WiFi����
	 */
	private WifiManager m_WifiManager;
	/**
	 * WiFi������
	 */
	private WifiServer m_WifiServer;
	/**
	 * ����������Activity������binder
	 */
	private final IBinder m_Binder=new WifiServerBinder();
	
	/**
	 * �̳�Binder���ڲ��෵���ⲿ�����ʵ�������ڵ����ⲿ�������
	 * @author Administrator
	 */
	public class WifiServerBinder extends Binder
	{
		public WifiServerService getService()
		{
			return WifiServerService.this;
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
	 * ��һ�ε���serviceʱִ�У���service�������У��ٴε�����ֱ��ִ��onStartCommand
	 */
	@Override
	public void onCreate()
	{
		Log.e("bbb", "onCreate-Service");
//		m_WifiManager=(WifiManager) getSystemService(WIFI_SERVICE);
		m_WifiServer=new WifiServer(this);
	}
	
	/**
	 * �൱�ھɰ汾��onStart��ÿ�ε���service����ִ��
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.e("bbb", "onStart-WifiServerService");
		if(!m_WifiServer.ensureWifiOpen())
		{
			Log.e("bbb", "WiFiģ���ʧ�ܣ�����");
		}
		//��ȡActivity�����Ĳ���intent
		Bundle param=intent.getExtras();
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Wifi.Key.WIFI_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * ֹͣ����WiFi�豸�㲥
		 */
		case Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES:
			Log.i("bbb", "��ʼֹͣ����WiFi�㲥");
			m_WifiServer.setBroadcastReceiveState(false);
			break;
		/**
		 * ��ȡԶ��WiFi�豸����	
		 */
		case Constants_Wifi.GET_REMOTE_WIFI_DEVICES_COUNT:
			break;
		/**
		 * ��ʼ���͹㲥	
		 */
		case Constants_Wifi.START_SENDING_BROADCAST:
			new Thread()
			{
				@Override
				public void run()
				{
					Log.i("bbb", "��ʼ����UDP�㲥");
					if(m_WifiServer.startSendingUDPBroadcast()!=Constants_Wifi.Broadcast.RECEIVE_DATAGRAMPACKET_SUCCESS)
					{
						//���͹㲥�쳣����Ϣ

					}
				}
			}.start();
			break;
		/**
		 * ֹͣ���͹㲥	
		 */
		case Constants_Wifi.STOP_SENDING_BROADCAST:
			Log.i("bbb", "��ʼֹͣ����UDP�㲥");
			m_WifiServer.stopSendingBroadcast();
			break;
		 /**
		  * ��ʼ��������	
		  */
		case Constants_Wifi.START_ACCEPT_REQUEST:
		new Thread()
		{
			@Override
			public void run()
			{
					Log.i("bbb", "��ʼ��������");
					m_WifiServer.acceptConnectionRequest();
			}
		}.start();
			new Thread()
			{
				@Override
				public void run()
				{
					while(m_WifiServer.getAcceptRequestState())
					{
						//���������������
						if(m_WifiServer.getFileReceiveRequestState()==true && m_WifiServer.getUserChoice()==-1)
						{
							//���͹㲥
							Intent i=new Intent(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_REQUEST);
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_NAME, m_WifiServer.getSenderDevice().getWifiDeviceName());
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_IPADDRESS, m_WifiServer.getSenderDevice().getWifiDeviceIPAddress());
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_MACADDRESS, m_WifiServer.getSenderDevice().getWifiDeviceMacAddress());
							sendBroadcast(i);
							Log.i("bbb", "�ѷ��͹㲥");
							return;
						}
					}
				}
			}.start();
			break;
		/**
		 * �����ļ�
		 */
		case Constants_Wifi.RECEIVE_FILE:
			final String receive_file_path=param.getString(Constants_Wifi.Key.RECEIVE_FILE_PATH);
			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("bbb", "��ʼ�����ļ�");
						m_WifiServer.receiveFile(receive_file_path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("bbb", e.getMessage());
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_FAILED));
						return;
					}
					sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_SUCCESS));
				}
			}.start();
			break;
		/**
		 * ֹͣ��������	
		 */
		case Constants_Wifi.STOP_ACCEPT_REQUEST:
			m_WifiServer.stopAcceptConnectionRequest();
			break;
			
			default:break;
		}
		//���ظ�ֵ��ʶҪͨ����ʾ����/ֹͣservice
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		m_WifiServer.setBroadcastState(false);
		m_WifiServer.setBroadcastReceiveState(false);
		m_WifiServer.setAcceptingRequestState(false);
		m_WifiServer.close();
		super.onDestroy();
	}
	
	/**
	 * �����û�ѡ���Ƿ�����ļ�
	 * @param choice
	 */
	public void setUserChoice(int choice)
	{
		m_WifiServer.setUserChoice(choice);
	}

}
