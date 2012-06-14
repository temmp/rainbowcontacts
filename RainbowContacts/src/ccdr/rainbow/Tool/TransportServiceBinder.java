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
 * Activity���ڻ�ȡService���õĲ���
 * @author Administrator
 *
 */
public class TransportServiceBinder {

	/**
	 * �󶨵�Service����
	 */
	private Service m_ServiceBinder=null;
	
	private ServiceConnection m_Connection=null;
	/**
	 * ���ø÷���ĵ����ߵ�������
	 */
	private Context m_Context=null;
	/**
	 * ���ڰ�Service��Intent
	 */
	private Intent m_Intent=null;
	/**
	 * �Ƿ��Ѿ���Service
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
	 * ��״̬
	 * @return
	 */
	public boolean getBindState(){return m_BindState;}
	
	/**
	 * ���ذ󶨵�Service
	 * @return
	 */
	public Service getServiceBinder(){return m_ServiceBinder;}
	/**
	 * ����Service�����
	 * @return
	 */
	public Class getServiceClass(){return m_ServiceBinder.getClass();}
	/**
	 * ���°�Service
	 */
	public void reBindService(){m_Context.bindService(m_Intent, m_Connection, Context.BIND_AUTO_CREATE);}
	/**
	 * ȡ����Service�İ�
	 */
	public void unBindService(){m_Context.unbindService(m_Connection);}
	/**
	 * ֹͣService����
	 */
	public void stopBindedService(){m_Context.stopService(m_Intent);}
}
