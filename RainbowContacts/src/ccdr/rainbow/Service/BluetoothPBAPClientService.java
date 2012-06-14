package ccdr.rainbow.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import ccdr.rainbow.Bluetooth.BluetoothPBAP;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Exception.BluetoothException;
import ccdr.rainbow.Tool.BluetoothHiddenMethod;
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

public class BluetoothPBAPClientService extends Service {

	/**
	 * 是否第一次运行
	 */
	private boolean isFirstTime=true;
	/**
	 * Sevice是否已销毁
	 */
	private boolean isDestroyed=false;
	/**
	 * 本地蓝牙适配器
	 */
	private BluetoothAdapter m_BluetoothAdapter;
	/**
	 * 远程蓝牙设备
	 */
	public BluetoothDevice m_BluetoothDevice;
	/**
	 * 创建用于与Activity交互的binder
	 */
	private final IBinder m_Binder=new BluetoothPBAPBinder();
	/**
	 * PBAP操作类
	 */
	private BluetoothPBAP m_BluetoothPBAP;
	/**
	 * PBAP初始化的线程
	 */
	private Thread m_PBAPInitThread=null;
	/**
	 * 继承Binder的内部类返回外部类对象实例，用于调用外部类的数据
	 * @author Administrator
	 */
	public class BluetoothPBAPBinder extends Binder
	{
		public BluetoothPBAPClientService getService()
		{
			return BluetoothPBAPClientService.this;
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
	 * Activity所用到的消息
	 * @author Administrator
	 *
	 */
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
			case MSG.UPDATE_PROGRESS_DIALOG:
				Intent i_update_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR);
				i_update_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.CURRENT_PHONEBOOK_COUNT, m_BluetoothPBAP.getCurrentPhonebookCount());
				sendBroadcast(i_update_dlg);
				break;
			case MSG.INIT_PROGRESS_DIALOG:
				Intent i_init_dlg=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_BLUETOOTH_PBAP_PROGRESS_BAR);
				i_init_dlg.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.TOTAL_PHONEBOOK_COUNT, m_BluetoothPBAP.getTotalPhonebookCount());
				sendBroadcast(i_init_dlg);
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
		Log.i("aaa", "onCreate-PBAPClientService");
//		BluetoothPBAP=new BluetoothPBAP(this);
		m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	}
	
	/**
	 * 相当于旧版本的onStart，每次调用service都会执行
	 */
	@SuppressWarnings("finally")
	@Override
	public int onStartCommand(Intent intent,int flags,int startId)
	{
		Log.i("aaa", "onStart-PBAPClientService");
		//获取Activity传来的参数intent
		final Bundle param=intent.getExtras();
		if(isFirstTime)
		{
			m_PBAPInitThread=new Thread()
			{
				@Override
				public void run()
				{
					isFirstTime=false;
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
					
			Log.i("aaa", "第一次运行Service");
			m_BluetoothDevice=(BluetoothDevice)param.getParcelable(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE);
			boolean init_success=true;
			try {
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
				
				m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
			} catch (BluetoothException e1) {
				// TODO Auto-generated catch block
				init_success=false;
				e1.printStackTrace();Log.e("aaa", e1.getMessage());
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
				
				//重新处理，重启蓝牙后再次创建
				m_BluetoothAdapter.disable();
				Log.i("aaa", "关闭蓝牙");
				while(m_BluetoothAdapter.getState()!=BluetoothAdapter.STATE_OFF)
				{
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
					
				}
				m_BluetoothAdapter.enable();
				
				if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
				
				Log.i("aaa", "打开蓝牙");
				while(m_BluetoothAdapter.getState()!=BluetoothAdapter.STATE_ON)
				{
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
					
				}
				try {
					
					if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
					
					m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
					init_success=true;
					Log.i("aaa", "重新创建PBAP客户端操作类成功");
				} catch (BluetoothException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("aaa", "重新创建PBAP客户端操作类失败！！！"+e.getMessage());
					try {
						try {
							
							if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
							
						Log.d("aaa", "开始取消配对");
						if(!BluetoothHiddenMethod.RemoveBondWithTimeout(m_BluetoothDevice,7000))
						{
							Log.e("aaa","取消蓝牙配对失败或超时！！！");
						}
						Log.i("aaa", "取消配对成功");
					} catch (IllegalArgumentException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					} catch (SecurityException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					} catch (ClassNotFoundException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					} catch (IllegalAccessException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					} catch (InvocationTargetException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					} catch (NoSuchMethodException e11) {
						// TODO Auto-generated catch block
						e11.printStackTrace();Log.e("aaa", "取消配对异常！！！"+e11.getMessage());
					}
						/**
						 * 暂时没用
						 */
						while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_NONE)
		    			{
							
							if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
							
		    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
		    				try {
								Thread.sleep(1000);
							} catch (InterruptedException e11) {
								// TODO Auto-generated catch block
								e11.printStackTrace();
							}
		    			}
					
//					try {
//						Log.i("aaa", "开始创建配对");
//						BluetoothHiddenMethod.CreateBond(m_BluetoothDevice);
//						Log.i("aaa", "创建配对成功");
						
//						while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_BONDED)
//		    			{
//		    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
//		    				try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e111) {
//								// TODO Auto-generated catch block
//								Log.e("aaa", "与远程蓝牙设备绑定失败！！！"+e111.getMessage());
//								sendBroadcast(new Intent(Constants_Interaction.Action.Bluetooth.BOND_REMOTE_DEVICE_FAILED));
//////				    			m_Handler.obtainMessage(CONNECT_REMOTEDEVICE_FAILED).sendToTarget();
//								e111.printStackTrace();
//							}
//		    			}
						
						if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
						
						m_BluetoothPBAP=new BluetoothPBAP(m_BluetoothDevice);
						init_success=true;
					} catch (BluetoothException e2) {
						// TODO Auto-generated catch block
						Log.e("aaa", "重新配对后创建PBAP客户端失败！！！"+e2.getMessage());
						e2.printStackTrace();
					}
				}
				
			}
//			finally
//			{			
//				isFirstTime=false;		
//			}
			
			if(getIsDestroyed()){Log.i("aaa", "PBAP初始化线程已销毁");return;}
			
			if(init_success)
			{
			Log.i("aaa", "PBAP初始化成功");
			Intent i_init=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_SUCCESS);
			sendBroadcast(i_init);
			}
			else
			{	
				Intent i_init=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_FAILED);
				sendBroadcast(i_init);
			}
			
			
				}
			};
			m_PBAPInitThread.start();
			
			return START_STICKY;
		}
//		Context context=(Context)param.get(Constants_Wifi.WIFI_ACTIVITY_CONTEXT);
		int option=param.getInt(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION);
		switch(option)
		{
		case Constants_Bluetooth.Option.CONNECT_REMOTE_DEVICE:	
			Log.i("aaa", "与PBAP服务端建立连接");
			new Thread() {
				@Override
				public void run() {
					// 发出连接请求建立连接，并生成一个connection ID
					try {
						m_BluetoothPBAP.connectForPBAPService();
						Intent i_coon = new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_SUCCESS);
						sendBroadcast(i_coon);
					} catch (BluetoothException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("aaa", e.getMessage());
						Intent i_coon = new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_FAILED);
						sendBroadcast(i_coon);
					}
				}
			}.start();
			break;
		/**
		 * 获取联系人数量
		 */
		case Constants_Bluetooth.Option.GET_REMOTE_DEVICE_CONTACTS_COUNT:
			new Thread()
			{
				@Override
				public void run()
				{
			int count=-1;
			try {
				count = m_BluetoothPBAP.getAllContactsCount();
				Intent i_count=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT);
				i_count.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.REMOTE_DEVICE_CONTACTS_COUNT, count);
				sendBroadcast(i_count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}.start();
			break;
			/**
			 * 获取所有联系人
			 */
		case Constants_Bluetooth.Option.GET_REMOTE_DEVICE_PHONEBOOK_DATA:
			Log.i("aaa", "开始获取电话簿数据");
			final String file_path=param.getString(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.PBAP_RECEIVE_FILE_PATH);
			new Thread()
			{
				@Override
				public void run()
				{
			try {
				m_BluetoothPBAP.getAllContactsCountPro(m_Handler);
				m_BluetoothPBAP.getPhonebookDataPro(new File(file_path),m_Handler);
				Intent i_data=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA);
				sendBroadcast(i_data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}.start();
			break;
			/**
			 * 中断电话簿传输
			 */
//		case Constants_Bluetooth.Option.ABORT_GETTING_PHONEBOOK_DATA:
//			m_BluetoothPBAP.abortGettingPhonebookData();
//			m_BluetoothPBAP.disconnectToServer();
//			Intent i_abort=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.ABORT_GETTING_PHONEBOOK_DATA_SUCCESS);
//			sendBroadcast(i_abort);
//			break;
			/**
			 * 关闭连接
			 */
		case Constants_Bluetooth.Option.DISCONNECT_REMOTE_DEVICE:
			//断开PBAP连接
			m_BluetoothPBAP.disconnectToServer();
			Intent i_disconnect=new Intent(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.DISCONNECT_REMOTE_DEVICE_SUCCESS);
			sendBroadcast(i_disconnect);
			break;
			
			default:break;
		}
		//返回该值标识要通过显示启动/停止service
		return START_STICKY;
	}
	
	@Override
	public void onDestroy()
	{
		setIsDestroyed(true);
		if(null!=m_BluetoothPBAP)
		{
			m_BluetoothPBAP.disconnectToServer();
			m_BluetoothPBAP=null;
		}
		m_PBAPInitThread.interrupt();
		m_PBAPInitThread=null;
		super.onDestroy();
	}
	
	/**
	 * 设置Sevice是否已销毁，用于判断是否停止线程
	 * @param b
	 */
	public void setIsDestroyed(boolean b)
	{
		isDestroyed=b;
	}
	/**
	 * 获取Service是否已销毁，用于判断是否停止线程
	 * @return
	 */
	public boolean getIsDestroyed()
	{
		return isDestroyed;
	}
}
