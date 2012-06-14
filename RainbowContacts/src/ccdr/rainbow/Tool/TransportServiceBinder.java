package ccdr.rainbow.Tool;

import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Service.BluetoothClientService;
import ccdr.rainbow.Service.BluetoothServerService;
import ccdr.rainbow.Service.WifiClientService;
import ccdr.rainbow.Service.WifiServerService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Activity用于获取Service引用的操作
 * @author Administrator
 *
 */
public class TransportServiceBinder {

	/**
	 * 绑定的Service对象
	 */
	private Service m_ServiceBinder=null;
	
	private ServiceConnection m_Connection=null;
	/**
	 * 调用该服务的调用者的上下文
	 */
	private Context m_Context=null;
	/**
	 * 用于绑定Service的Intent
	 */
	private Intent m_Intent=null;
	/**
	 * 是否已经绑定Service
	 */
	private boolean m_BindState=false;
	
	public TransportServiceBinder(Context context,final int Service_Type)
	{
		m_Context=context;
		
		m_Connection=new ServiceConnection()
		{
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				switch(Service_Type)
				{
				case Constants_Global.Wifi_Client_Service:
					m_ServiceBinder=((WifiClientService.WifiClientBinder)service).getService();
					break;
				case Constants_Global.Wifi_Server_Service:
					m_ServiceBinder=((WifiServerService.WifiServerBinder)service).getService();
					break;
				case Constants_Global.Bluetooth_Client_Service:
					m_ServiceBinder=((BluetoothClientService.BluetoothClientBinder)service).getService();
					break;
				case Constants_Global.Bluetooth_Server_Service:
					m_ServiceBinder=((BluetoothServerService.BluetoothServerBinder)service).getService();
					break;
				default:break;
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				m_ServiceBinder=null;
			}
		};
		
		m_Intent=new Intent();
		switch(Service_Type)
		{
		case Constants_Global.Wifi_Client_Service:
			m_Intent.setClass(m_Context, WifiClientService.class);
			break;
		case Constants_Global.Wifi_Server_Service:
			m_Intent.setClass(m_Context, WifiServerService.class);
			break;
		case Constants_Global.Bluetooth_Client_Service:
			m_Intent.setClass(m_Context, BluetoothClientService.class);
			break;
		case Constants_Global.Bluetooth_Server_Service:
			m_Intent.setClass(m_Context, BluetoothServerService.class);
			break;
			default:break;
		}
		m_BindState=m_Context.bindService(m_Intent,m_Connection,Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 绑定状态
	 * @return
	 */
	public boolean getBindState(){return m_BindState;}
	
	/**
	 * 返回绑定的Service
	 * @return
	 */
	public Service getServiceBinder(){return m_ServiceBinder;}
	/**
	 * 返回Service类对象
	 * @return
	 */
	public Class getServiceClass(){return m_ServiceBinder.getClass();}
	/**
	 * 重新绑定Service
	 */
	public void reBindService(){m_Context.bindService(m_Intent, m_Connection, Context.BIND_AUTO_CREATE);}
	/**
	 * 取消与Service的绑定
	 */
	public void unBindService(){m_Context.unbindService(m_Connection);}
	/**
	 * 停止Service运行
	 */
	public void stopBindedService(){m_Context.stopService(m_Intent);}
}
