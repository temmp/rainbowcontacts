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
	 * 用于管理本地WiFi的类
	 */
	private WifiManager m_WifiManager;
	/**
	 * WiFi传输类
	 */
	private WifiClient m_WifiClient;
	/**
	 * 远程WiFi设备的列表
	 */
	private List<WifiDevice> m_WifiDeviceList; 
	/**
	 * 已经发送到Activity的远程WiFi设备的列表，用于过滤相同的WifiDevice
	 */
	private List<WifiDevice> m_WifiDeviceList_hasSent=null; 
	/**
	 * 是否正在获取Wifi设备
	 */
	private boolean isGettingWifiDevice=false;
	/**
	 * 创建用于与Activity交互的binder
	 */
	private final IBinder m_Binder=new WifiClientBinder();
	
	/**
	 * 继承Binder的内部类返回外部类对象实例，用于调用外部类的数据
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
	 * 返回binder与Activity交互
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return m_Binder;
	}

	/**
	 * 第一次调用service时执行，若service正在运行，再次调用则直接执行onStartCommand
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
	 * 相当于旧版本的onStart，每次调用service都会执行
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.e("bbb", "onStart-Service");
		if(!m_WifiClient.ensureWifiOpen())
		{Log.e("bbb", "WiFi模块打开失败！！！");}
		//获取Activity传来的参数intent
		Bundle param=intent.getExtras();
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Wifi.Key.WIFI_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * 创建线程循环接收远程WiFi设备的广播
		 */
		case Constants_Wifi.LISTEN_REMOTE_WIFI_DEVICES:
			new Thread()
			{
				@Override
				public void run()
				{
					if(m_WifiClient.startListeningRemoteWifiDevice()!=Constants_Wifi.Signal.SEND_BROADCAST_SUCCESS)
					{
						//发送搜索WiFi设备异常的消息
						Log.e("bbb", "搜索WiFi设备异常！！！");
					}
				}
			}.start();
			break;
		/**
		 * 创建线程不断获取WiFi设备
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
		 * 终止线程获取WiFi设备
		 */
		case Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES:
			setIsGettingWifiDevice(false);
			break;
		/**
		 * 停止接收WiFi设备广播
		 */
		case Constants_Wifi.STOP_LISTENING_REMOTE_WIFI_DEVICES:
			m_WifiClient.setBroadcastReceiveState(false);
			break;
		/**
		 * 获取远程WiFi设备数量	
		 */
		case Constants_Wifi.GET_REMOTE_WIFI_DEVICES_COUNT:
			break;
		/**
		 * 停止发送广播	
		 */
		case Constants_Wifi.STOP_SENDING_BROADCAST:
			m_WifiClient.stopSendingBroadcast();
			break;
		/**
		 * 发送请求
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
//						Log.e("bbb", "请求连接出错！！！");
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
					//发送完毕后广播
					sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_ACCEPT));
				}
			}.start();
			break;
			/**
			 * 发送文件
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
		 * 停止监听请求	
		 */
		case Constants_Wifi.STOP_ACCEPT_REQUEST:
			m_WifiClient.stopAcceptConnectionRequest();
			break;
			
			default:break;
		}
		//返回该值标识要通过显示启动/停止service
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
	 * 获取远程WiFi设备数量
	 * @return
	 */
	public int getRemoteWifiDeviceCount() {
		// TODO Auto-generated method stub
		return m_WifiClient.getRemoteWifiDeviceCount();
	}

	/**
	 * 获取远程WiFi设备列表
	 * @return
	 */
	public List<WifiDevice> getAllRemoteWifiDevice() {
		// TODO Auto-generated method stub
		return m_WifiClient.getAllRemoteWifiDevice();
	}
	
	/**
	 * 检测WiFi设备是否已经发送到Activity
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
	 * 获取是否正在获取WiFi设备
	 * @return
	 */
	public boolean getIsGettingWifiDevice()
	{
		return isGettingWifiDevice;
	}
	/**
	 * 设置是否正在获取WiFi设备
	 * @param b
	 */
	public void setIsGettingWifiDevice(boolean b)
	{
		isGettingWifiDevice=b;
	}
}
