package ccdr.rainbow.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import ccdr.rainbow.Bluetooth.BluetoothServer;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Interaction;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


public class BluetoothServerService extends Service {
	/**
	 * 蓝牙服务端
	 */
	private BluetoothServer m_BluetoothServer;
	/**
	 * 表示本地蓝牙设备
	 */
	private BluetoothAdapter m_BluetoothAdapter;

	/**
	 * 创建用于与Activity交互的binder
	 */
	private final IBinder m_Binder=new BluetoothServerBinder();
	
	/**
	 * 继承Binder的内部类返回外部类对象实例，用于调用外部类的数据
	 * @author Administrator
	 */
	public class BluetoothServerBinder extends Binder
	{
		public BluetoothServerService getService()
		{
			return BluetoothServerService.this;
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

	public class MSG
	{
		/**
		 * 告诉Activity进度条可初始化
		 */
		public static final int INIT_PROGRESS_DIALOG=100;
		/**
		 * 告诉Activity进度条可更新
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
			case MSG.INIT_PROGRESS_DIALOG:
				Intent i_init_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothServer.INIT_BLUETOOTH_SERVER_PROGRESS_BAR);
				i_init_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothServer.TOTAL_DATA_LENGTH, m_BluetoothServer.getTotalLength());
				sendBroadcast(i_init_dlg);
				break;
			
			case MSG.UPDATE_PROGRESS_DIALOG:
				Intent i_update_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothServer.UPDATE_BLUETOOTH_SERVER_PROGRESS_BAR);
				i_update_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothServer.CURRENT_DATA_LENGTH, m_BluetoothServer.getCurrentLength());
				sendBroadcast(i_update_dlg);
				break;
				
				default:break;
			}
		}
	};
	/**
	 * 第一次调用service时执行，若service正在运行，再次调用则直接执行onStartCommand
	 */
	@Override
	public void onCreate()
	{
		try {
			m_BluetoothServer=new BluetoothServer(Constants_Bluetooth.CS.CS_UUID,m_Handler,Constants_File.Path.CS_REV);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", e.getMessage());
			Intent i=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothServer.FILE_NOT_FOUND);
			sendBroadcast(i);
		}
	}
	
	/**
	 * 相当于旧版本的onStart，每次调用service都会执行
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		//获取Activity传来的参数intent
		Bundle param=intent.getExtras();
		
		int option=param.getInt(Constants_Interaction.Key.Bluetooth.Bluetooth_CS_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * 获取联系人数量
		 */
		case Constants_Bluetooth.Option.START_RECEIVING_FILE:

			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("aaa", "开始监听客户端");
						m_BluetoothServer.registerService();
						Log.i("aaa", "开始接受客户端请求");
						m_BluetoothServer.waitAccept();
						Log.i("aaa", "开始接受文件");
						m_BluetoothServer.ReceiveFile();
						Log.i("aaa", "关闭套接字");
						m_BluetoothServer.CloseSocket();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("aaa", e.getMessage());
						Intent i=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothServer.RECEIVE_FILE_FAILED);
						i.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothServer.RECEIVE_FILE_FAILED_REASON, e.getMessage());
						sendBroadcast(i);
						return;
					}
					
					Intent i_receive=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothServer.RECEIVE_FILE_SUCESS);
					sendBroadcast(i_receive);
				}
			}.start();
			break;
			
			default:break;
		}
		//返回该值标识要通过显示启动/停止service
		return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		if(null!=m_BluetoothServer)
		{
			try {
				m_BluetoothServer.CloseSocket();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", "服务端关闭套接字失败！！！"+e.getMessage());
			}
		}
	}
}
