package ccdr.rainbow.Service;

import java.io.File;
import java.io.IOException;
import ccdr.rainbow.Bluetooth.BluetoothCS;
import ccdr.rainbow.Bluetooth.BluetoothClient;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Interaction;
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

public class BluetoothClientService extends Service {
	/**
	 * 是否第一次运行
	 */
	private boolean isFirstTime=true;
	/**
	 * 本地蓝牙适配器
	 */
	private BluetoothAdapter m_BluetoothAdapter;
	/**
	 * 蓝牙客户端
	 */
	private BluetoothClient m_BluetoothClient;
	/**
	 * 远程蓝牙设备
	 */
	public BluetoothDevice m_BluetoothDevice;
	/**
	 * 创建用于与Activity交互的binder
	 */
	private final IBinder m_Binder=new BluetoothClientBinder();
	
	/**
	 * 继承Binder的内部类返回外部类对象实例，用于调用外部类的数据
	 * @author Administrator
	 */
	public class BluetoothClientBinder extends Binder
	{
		public BluetoothClientService getService()
		{
			return BluetoothClientService.this;
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
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:break;
			case 1:break;
				default:
			}
		}
	};
	
	/**
	 * 第一次调用service时执行，若service正在运行，再次调用则直接执行onStartCommand
	 */
	@Override
	public void onCreate()
	{
//		m_BluetoothClient=new BluetoothClient(Constants_Bluetooth.CS.CS_UUID);
	}
	
	/**
	 * 相当于旧版本的onStart，每次调用service都会执行
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		//获取Activity传来的参数intent
		Bundle param=intent.getExtras();

		if(isFirstTime)
		{
			m_BluetoothDevice=(BluetoothDevice)param.getParcelable(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE);
			m_BluetoothClient=new BluetoothClient(m_BluetoothDevice,Constants_Bluetooth.CS.CS_UUID);
		}
		int option=param.getInt(Constants_Interaction.Key.Bluetooth.Bluetooth_CS_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * 客户端开始发送文件
		 */
		case Constants_Bluetooth.Option.START_SENDING_FILE:

			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("aaa", "开始与服务端创建连接");
						m_BluetoothClient.ConnectService();
						Log.i("aaa","开始连接服务端");
						m_BluetoothClient.Connect();
						
						Intent i=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothClient.CONNECT_TO_SERVER_SUCESS);
						sendBroadcast(i);
						
						Log.i("aaa", "开始发送文件");
						m_BluetoothClient.SendFile(new File(Constants_File.Path.CS_SEND), BluetoothCS.TYPE_VCARD);
						Log.i("aaa", "开始关闭连接");
						m_BluetoothClient.CloseSocket();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("aaa", e.getMessage());
						Intent i=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_FAILED);
						i.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothClient.SEND_FILE_FAILED_REASON, e.getMessage());
						sendBroadcast(i);
						return;
					}
					
					Intent i_receive=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_SUCESS);
					sendBroadcast(i_receive);
				}
			}.start();
			break;
			
			default:break;
		}
		//返回该值标识要通过显示启动/停止service
		return START_STICKY;
	}
}
