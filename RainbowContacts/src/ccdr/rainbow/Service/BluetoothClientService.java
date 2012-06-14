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
	 * �Ƿ��һ������
	 */
	private boolean isFirstTime=true;
	/**
	 * ��������������
	 */
	private BluetoothAdapter m_BluetoothAdapter;
	/**
	 * �����ͻ���
	 */
	private BluetoothClient m_BluetoothClient;
	/**
	 * Զ�������豸
	 */
	public BluetoothDevice m_BluetoothDevice;
	/**
	 * ����������Activity������binder
	 */
	private final IBinder m_Binder=new BluetoothClientBinder();
	
	/**
	 * �̳�Binder���ڲ��෵���ⲿ�����ʵ�������ڵ����ⲿ�������
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
	 * ����binder��Activity����
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
	 * ��һ�ε���serviceʱִ�У���service�������У��ٴε�����ֱ��ִ��onStartCommand
	 */
	@Override
	public void onCreate()
	{
//		m_BluetoothClient=new BluetoothClient(Constants_Bluetooth.CS.CS_UUID);
	}
	
	/**
	 * �൱�ھɰ汾��onStart��ÿ�ε���service����ִ��
	 */
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		//��ȡActivity�����Ĳ���intent
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
		 * �ͻ��˿�ʼ�����ļ�
		 */
		case Constants_Bluetooth.Option.START_SENDING_FILE:

			new Thread()
			{
				@Override
				public void run()
				{
					try {
						Log.i("aaa", "��ʼ�����˴�������");
						m_BluetoothClient.ConnectService();
						Log.i("aaa","��ʼ���ӷ����");
						m_BluetoothClient.Connect();
						
						Intent i=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothClient.CONNECT_TO_SERVER_SUCESS);
						sendBroadcast(i);
						
						Log.i("aaa", "��ʼ�����ļ�");
						m_BluetoothClient.SendFile(new File(Constants_File.Path.CS_SEND), BluetoothCS.TYPE_VCARD);
						Log.i("aaa", "��ʼ�ر�����");
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
		//���ظ�ֵ��ʶҪͨ����ʾ����/ֹͣservice
		return START_STICKY;
	}
}
