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
	 * 用于管理本地WiFi的类
	 */
	private WifiManager m_WifiManager;
	/**
	 * WiFi传输类
	 */
	private WifiServer m_WifiServer;
	/**
	 * 创建用于与Activity交互的binder
	 */
	private final IBinder m_Binder=new WifiServerBinder();
	
	/**
	 * 继承Binder的内部类返回外部类对象实例，用于调用外部类的数据
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
		m_WifiServer=new WifiServer(this);
	}
	
	/**
	 * 相当于旧版本的onStart，每次调用service都会执行
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.e("bbb", "onStart-WifiServerService");
		if(!m_WifiServer.ensureWifiOpen())
		{
			Log.e("bbb", "WiFi模块打开失败！！！");
		}
		//获取Activity传来的参数intent
		Bundle param=intent.getExtras();
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Wifi.Key.WIFI_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * 停止接收WiFi设备广播
		 */
		case Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES:
			Log.i("bbb", "开始停止接受WiFi广播");
			m_WifiServer.setBroadcastReceiveState(false);
			break;
		/**
		 * 获取远程WiFi设备数量	
		 */
		case Constants_Wifi.GET_REMOTE_WIFI_DEVICES_COUNT:
			break;
		/**
		 * 开始发送广播	
		 */
		case Constants_Wifi.START_SENDING_BROADCAST:
			new Thread()
			{
				@Override
				public void run()
				{
					Log.i("bbb", "开始发送UDP广播");
					if(m_WifiServer.startSendingUDPBroadcast()!=Constants_Wifi.Broadcast.RECEIVE_DATAGRAMPACKET_SUCCESS)
					{
						//发送广播异常的消息

					}
				}
			}.start();
			break;
		/**
		 * 停止发送广播	
		 */
		case Constants_Wifi.STOP_SENDING_BROADCAST:
			Log.i("bbb", "开始停止发送UDP广播");
			m_WifiServer.stopSendingBroadcast();
			break;
		 /**
		  * 开始监听请求	
		  */
		case Constants_Wifi.START_ACCEPT_REQUEST:
		new Thread()
		{
			@Override
			public void run()
			{
					Log.i("bbb", "开始监听请求");
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
						//如果监听过程中有
						if(m_WifiServer.getFileReceiveRequestState()==true && m_WifiServer.getUserChoice()==-1)
						{
							//发送广播
							Intent i=new Intent(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_REQUEST);
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_NAME, m_WifiServer.getSenderDevice().getWifiDeviceName());
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_IPADDRESS, m_WifiServer.getSenderDevice().getWifiDeviceIPAddress());
							i.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_MACADDRESS, m_WifiServer.getSenderDevice().getWifiDeviceMacAddress());
							sendBroadcast(i);
							Log.i("bbb", "已发送广播");
							return;
						}
					}
				}
			}.start();
			break;
		/**
		 * 接收文件
		 */
		case Constants_Wifi.RECEIVE_FILE:
			final String receive_file_path=param.getString(Constants_Wifi.Key.RECEIVE_FILE_PATH);
			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("bbb", "开始接收文件");
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
		 * 停止监听请求	
		 */
		case Constants_Wifi.STOP_ACCEPT_REQUEST:
			m_WifiServer.stopAcceptConnectionRequest();
			break;
			
			default:break;
		}
		//返回该值标识要通过显示启动/停止service
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
	 * 设置用户选择是否接收文件
	 * @param choice
	 */
	public void setUserChoice(int choice)
	{
		m_WifiServer.setUserChoice(choice);
	}

}
