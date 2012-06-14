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
	 * ���������
	 */
	private BluetoothServer m_BluetoothServer;
	/**
	 * ��ʾ���������豸
	 */
	private BluetoothAdapter m_BluetoothAdapter;

	/**
	 * ����������Activity������binder
	 */
	private final IBinder m_Binder=new BluetoothServerBinder();
	
	/**
	 * �̳�Binder���ڲ��෵���ⲿ�����ʵ�������ڵ����ⲿ�������
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
	 * ����binder��Activity����
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return m_Binder;
	}

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
	 * ��һ�ε���serviceʱִ�У���service�������У��ٴε�����ֱ��ִ��onStartCommand
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
	 * �൱�ھɰ汾��onStart��ÿ�ε���service����ִ��
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		//��ȡActivity�����Ĳ���intent
		Bundle param=intent.getExtras();
		
		int option=param.getInt(Constants_Interaction.Key.Bluetooth.Bluetooth_CS_SERVICE_OPERATION);
		switch(option)
		{
		/**
		 * ��ȡ��ϵ������
		 */
		case Constants_Bluetooth.Option.START_RECEIVING_FILE:

			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("aaa", "��ʼ�����ͻ���");
						m_BluetoothServer.registerService();
						Log.i("aaa", "��ʼ���ܿͻ�������");
						m_BluetoothServer.waitAccept();
						Log.i("aaa", "��ʼ�����ļ�");
						m_BluetoothServer.ReceiveFile();
						Log.i("aaa", "�ر��׽���");
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
		//���ظ�ֵ��ʶҪͨ����ʾ����/ֹͣservice
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
				Log.e("aaa", "����˹ر��׽���ʧ�ܣ�����"+e.getMessage());
			}
		}
	}
}
