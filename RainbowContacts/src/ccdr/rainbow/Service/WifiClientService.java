package ccdr.rainbow.Service;

import java.util.ArrayList;
import java.util.List;

import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Tool.WifiDevice;
import ccdr.rainbow.Wifi.WifiClient;
import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

public class WifiClientService extends Service {

	/**
	 * ���ڹ�����WiFi����
	 */
	private WifiManager m_WifiManager;
	/**
	 * WiFi������
	 */
	private WifiClient m_WifiClient;
	/**
	 * Զ��WiFi�豸���б�
	 */
	private List<WifiDevice> m_WifiDeviceList; 
	/**
	 * �Ѿ����͵�Activity��Զ��WiFi�豸���б����ڹ�����ͬ��WifiDevice
	 */
	private List<WifiDevice> m_WifiDeviceList_hasSent=null; 
	/**
	 * �Ƿ����ڻ�ȡWifi�豸
	 */
	private boolean isGettingWifiDevice=false;
	/**
	 * ����������Activity������binder
	 */
	private final IBinder m_Binder=new WifiClientBinder();
	
	/**
	 * �̳�Binder���ڲ��෵���ⲿ�����ʵ�������ڵ����ⲿ�������
	 * @author Administrator
	 */
	public class WifiClientBinder extends Binder
	{
		public WifiClientService getService()
		{
			return WifiClientService.this;
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
		m_WifiClient=new WifiClient(this);
		
		m_WifiDeviceList_hasSent=new ArrayList<WifiDevice>(){};
	}
	
	/**
	 * �൱�ھɰ汾��onStart��ÿ�ε���service����ִ��
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.e("bbb", "onStart-Service");
		if(!m_WifiClient.ensureWifiOpen())
		{Log.e("bbb", "WiFiģ���ʧ�ܣ�����");}
		//��ȡActivity�����Ĳ���intent
		Bundle param=intent.getExtras();
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Wifi.Key.WIFI_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * �����߳�ѭ������Զ��WiFi�豸�Ĺ㲥
		 */
		case Constants_Wifi.LISTEN_REMOTE_WIFI_DEVICES:
			new Thread()
			{
				@Override
				public void run()
				{
					if(m_WifiClient.startListeningRemoteWifiDevice()!=Constants_Wifi.Signal.SEND_BROADCAST_SUCCESS)
					{
						//��������WiFi�豸�쳣����Ϣ
						Log.e("bbb", "����WiFi�豸�쳣������");
					}
				}
			}.start();
			break;
		/**
		 * �����̲߳��ϻ�ȡWiFi�豸
		 */
		case Constants_Wifi.GET_REMOTE_WIFI_DEVICES:
			if(getIsGettingWifiDevice())
			{
				m_WifiDeviceList_hasSent.clear();
				break;
			}
			
			setIsGettingWifiDevice(true);
			new Thread()
			{
				@Override
				public void run()
				{
					while(getIsGettingWifiDevice())
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.e("bbb", e.getMessage());
						}
						
						for(int n=0;n<m_WifiClient.getRemoteWifiDeviceCount();++n)
						{
							WifiDevice device=m_WifiClient.getAllRemoteWifiDevice().get(n);
							if(!hasSentDevice(device))
							{
//								Bundle b=new Bundle();
//								b.putParcelable(Constants_Interaction.Key.Wifi.WifiClient.SENDER_WIFI_DEVICE, device);
								Intent i=new Intent(Constants_Interaction.Action.Wifi.WifiClient.REMOTE_WIFI_DEVICE_FOUND);
								i.putExtra(Constants_Interaction.Key.Wifi.WifiClient.SENDER_WIFI_DEVICE, device);
								sendBroadcast(i);
								m_WifiDeviceList_hasSent.add(device);
							}
						}
					}
				}
			}.start();
			break;
		/**
		 * ��ֹ�̻߳�ȡWiFi�豸
		 */
		case Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES:
			setIsGettingWifiDevice(false);
			break;
		/**
		 * ֹͣ����WiFi�豸�㲥
		 */
		case Constants_Wifi.STOP_LISTENING_REMOTE_WIFI_DEVICES:
			m_WifiClient.setBroadcastReceiveState(false);
			break;
		/**
		 * ��ȡԶ��WiFi�豸����	
		 */
		case Constants_Wifi.GET_REMOTE_WIFI_DEVICES_COUNT:
			break;
		/**
		 * ֹͣ���͹㲥	
		 */
		case Constants_Wifi.STOP_SENDING_BROADCAST:
			m_WifiClient.stopSendingBroadcast();
			break;
		/**
		 * ��������
		 */
		case Constants_Wifi.SEND_REQUEST:
//			final String send_file_path=param.getString(Constants_Wifi.Key.SEND_FILE_PATH);
			final String receiver_ip=param.getString(Constants_Wifi.Key.RECEIVER_WIFI_DEVICE_IPADDRESS);
			final String sender_name=param.getString(Constants_Wifi.Key.SENDER_WIFI_DEVICE_NAME);
//			final byte send_file_type=param.getByte(Constants_Wifi.Key.SEND_FILE_TYPE);
			
			new Thread()
			{
				@Override
				public void run()
				{
					int validate_result=m_WifiClient.sendConnectionRequest(receiver_ip, sender_name);
//					if(validate_result!=WifiConstants.REMOTE_DEVICE_ACCEPT)
//					{
//						Log.e("bbb", "�������ӳ�������");
//						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED));
//						return;
//					}
					switch(validate_result)
					{
					case Constants_Wifi.Transport.REMOTE_DEVICE_ACCEPT:
						break;
					case Constants_Wifi.Transport.REMOTE_DEVICE_REFUSE:
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_REFUSE));
						return;
					default:
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED));
						return;
					}
//					int send_result=m_WifiClient.sendFile(send_file_path, send_file_type, receiver_ip);
//					Log.i("bbb", "Send_Result:"+String.valueOf(send_result));
					//������Ϻ�㲥
					sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_ACCEPT));
				}
			}.start();
			break;
			/**
			 * �����ļ�
			 */
			case Constants_Wifi.SEND_FILE:
				final String send_file_path=param.getString(Constants_Wifi.Key.SEND_FILE_PATH);
				final String recv_ip=param.getString(Constants_Wifi.Key.RECEIVER_WIFI_DEVICE_IPADDRESS);
				final byte send_file_type=param.getByte(Constants_Wifi.Key.SEND_FILE_TYPE);
				
				new Thread()
				{
					@Override
					public void run()
					{
						int send_result=m_WifiClient.sendFile(send_file_path, send_file_type, recv_ip);
						Log.i("bbb", "Send_Result:"+String.valueOf(send_result));
						if(send_result==Constants_Wifi.File.SEND_FILE_SUCCESS)
						{
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_SUCCESS));
						}
						else
						{
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED));
						}
					}
				}.start();
				break;
		/**
		 * ֹͣ��������	
		 */
		case Constants_Wifi.STOP_ACCEPT_REQUEST:
			m_WifiClient.stopAcceptConnectionRequest();
			break;
			
			default:break;
		}
		//���ظ�ֵ��ʶҪͨ����ʾ����/ֹͣservice
		return START_STICKY;
	}
	
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		setIsGettingWifiDevice(false);
		m_WifiClient.setAcceptingRequestState(false);
		m_WifiClient.setBroadcastReceiveState(false);
		m_WifiClient.setBroadcastState(false);
		m_WifiClient.close();
	}



	/**
	 * ��ȡԶ��WiFi�豸����
	 * @return
	 */
	public int getRemoteWifiDeviceCount() {
		// TODO Auto-generated method stub
		return m_WifiClient.getRemoteWifiDeviceCount();
	}

	/**
	 * ��ȡԶ��WiFi�豸�б�
	 * @return
	 */
	public List<WifiDevice> getAllRemoteWifiDevice() {
		// TODO Auto-generated method stub
		return m_WifiClient.getAllRemoteWifiDevice();
	}
	
	/**
	 * ���WiFi�豸�Ƿ��Ѿ����͵�Activity
	 * @return
	 */
	public boolean hasSentDevice(WifiDevice d)
	{
		boolean hasSent=false;
		for(int n=0;n<m_WifiDeviceList_hasSent.size();++n)
		{
			if(d.equals(m_WifiDeviceList_hasSent.get(n)))
			{
				hasSent=true;break;
			}
		}
		return hasSent;
	}
	/**
	 * ��ȡ�Ƿ����ڻ�ȡWiFi�豸
	 * @return
	 */
	public boolean getIsGettingWifiDevice()
	{
		return isGettingWifiDevice;
	}
	/**
	 * �����Ƿ����ڻ�ȡWiFi�豸
	 * @param b
	 */
	public void setIsGettingWifiDevice(boolean b)
	{
		isGettingWifiDevice=b;
	}
}
