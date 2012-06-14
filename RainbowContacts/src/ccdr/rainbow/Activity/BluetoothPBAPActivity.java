package ccdr.rainbow.Activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Service.BluetoothPBAPClientService;
import ccdr.rainbow.Tool.BluetoothHiddenMethod;
import ccdr.rainbow.Tool.BluetoothTools;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.UnitSize;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothPBAPActivity extends Activity {
	
	BluetoothAdapter m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	
	String Exceptionfile = null;
	
	PrintWriter p =null;
	/**
	 * 提示用textview
	 */
	private TextView textview = null;
	/**
	 * 进度条、提示文字所用的container
	 */
	RelativeLayout progresscontainer = null;
	/**
	 * 连接阶段使用
	 */
	ProgressBar Cyclepb = null;
	/**
	 * 接受信息阶段使用
	 */
	ProgressBar pbbar = null;
	
	
//	boolean removebondlast = false;
	String remoteDeviceName = null;
	boolean notimeout = false;
	/**
	 * 因为使用自定义的询问绑定，超时之后取消绑定要求
	 */
	boolean timeout = false;
	/**
	 * 标识是否在当前Activity
	 */
	private boolean isCurrentActivity=true;
	/**
	 * 是否正在配对中
	 */
	private boolean isBonding=false;
	/**
	 * 是否正在传输
	 */
	private boolean IsInitializing=false;
	/**
	 * 设置传输状态
	 * @param b
	 */
	private void setIsInitializing(boolean b){IsInitializing=b;}
	/**
	 * 获取传输状态
	 * @return
	 */
	private boolean getIsInitializing(){return IsInitializing;}
	/**
	 * 伪进度条更新
	 */
	private Thread m_ProgressThread=null;
	
	int timecheck = 40000;
	/**************************************************************/
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private int connecting_id;
	/******************************************************************/
	/**
	 * 用于获取绑定的Service对象
	 */
//	private TransportServiceBinder m_Binder=null;
	/**
	 * 远程蓝牙设备
	 */
	private BluetoothDevice m_BluetoothDevice=null;
	/**
	 * 检测PBAP配对的线程
	 */
	private Thread m_PBAPBondThread=null;
	
	/**
	 * Activity所用到的消息
	 * @author Administrator
	 *
	 */
	public class MSG
	{
		/**
		 * 配对绑定成功
		 */
		public static final int BOND_REMOTEDEVICE_SUCCESS=500;
		/**
		 * 配对绑定失败
		 */
		public static final int BOND_REMOTEDEVICE_FAILED=501;
		/**
		 * 与PBAP服务端初始化成功
		 */
		public static final int INIT_PBAP_SERVICE_SUCCESS=502;
		/**
		 * 与设备连接成功
		 */
		public static final int CONNECT_REMOTE_DEVICE_SUCESS=503;
		/**
		 * 获取设备电话簿成功
		 */
		public static final int GET_REMOTE_DEVICE_PHONEBOOK_SUCESS=504;
		/**
		 * 断开PBAP连接成功
		 */
		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=505;
		/**
		 * 初始化进度条对话框
		 */
		public static final int INIT_PROGRESS_DIALOG=506;
		/**
		 * 更新进度条对话框
		 */
		public static final int UPDATE_PROGRESS_DIALOG=507;
		/**
		 * 更新进度条
		 */
		public static final int UPDATE_PBAP_PROGRESS_BAR=508;
		/**
		 * 完成进度条更新
		 */
		public static final int COMPLETE_PBAP_PROGRESS_BAR=509;
		/**
		 * 发送文件成功
		 */
		public static final int SEND_FILE_SUCCESS=510;
		/**
		 * 接收文件成功
		 */
		public static final int RECEIVE_FILE_SUCCESS=511;
	}
	
	/**
	 * 对话框的ID
	 * @author Administrator
	 *
	 */
	public class DLG
	{
		/**
		 * 初始化PBAP失败的对话框ID
		 */
		public static final int INIT_PBAP_SERVICE_FAILED=601;
		/**
		 * 连接PBAP失败的对话框ID
		 */
		public static final int CONNECT_PBAP_SERVICE_FAILED=602;
	}
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			//配对绑定成功
			case MSG.BOND_REMOTEDEVICE_SUCCESS:
		
				setIsInitializing(true);
				initPBAPService();
				break;
//			case MSG.UPDATE_PBAP_PROGRESS_BAR:
//				break;
			case MSG.BOND_REMOTEDEVICE_FAILED:
				/**
				 * 弹出对话框提示绑定失败！
				 */
				break;
//			//初始化PBAP服务成功
//			case Message.INIT_PBAP_SERVICE_SUCCESS:
//				Log.i("aaa", "开始连接PBAP服务");
//				connectPBAPService();
//				break;
//			//连接PBAP成功
//			case Message.CONNECT_REMOTE_DEVICE_SUCESS:
//				getPhonebookData();
//				break;
//			//获取电话簿数据成功
//			case Message.GET_REMOTE_DEVICE_PHONEBOOK_SUCESS:
//				disconnectPBAPService();
//				break;
//			//断开连接成功
//			case Message.DISCONNECT_REMOTE_DEVICE_SUCCESS:
//				startActivity(new Intent(BluetoothPBAPActivity.this,importContactsActivity.class)
//				.putExtra("filepath", Constants_File.Path.SDROOTPATH
//						+ File.separator
//						+ "PBAPReceive.vcf")
//				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//				break;
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothpbap);
		
        //获取上一Activity传过来的远程蓝牙设备对象
		Intent i=getIntent();
		m_BluetoothDevice=(BluetoothDevice)i.getParcelableExtra("bluetoothdevice");
		remoteDeviceName = i.getStringExtra("bluetoothdevicetype");
		
		progresscontainer = (RelativeLayout) findViewById(R.id.progresscontainer);
		Cyclepb = (ProgressBar) findViewById(R.id.cyclebar);
		pbbar = (ProgressBar) findViewById(R.id.pbbar);

		

		skin_linearLayout = (LinearLayout)findViewById(R.id.ltest);
		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		up_text = (TextView)findViewById(R.id.up_text);
		down_text = (TextView)findViewById(R.id.down_text);
		down_text1 = (TextView)findViewById(R.id.down_text1);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		System.out.println(changeSkinUtil.up_view+"--magazineActivityTitle----"+R.drawable.uplay_new);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		System.out.println(changeSkinUtil.up_text+"--magazineActivityTitle----"+R.color.title);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		down_text.setTextColor(this.getResources().getColor(changeSkinUtil.down_text));
		down_text1.setTextColor(this.getResources().getColor(changeSkinUtil.down_text1));
		connecting_id = changeSkinUtil.connecting_id;
		if(Constants_Global.DEBUG){
			Exceptionfile = Constants_File.Path.SDROOTPATH + File.separator + "PBAPException@" + Build.MODEL+"@"+remoteDeviceName+" "+
			BluetoothTools.convertLongToRFC2445DateTime(System.currentTimeMillis())+".txt";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(Exceptionfile));
				p = new PrintWriter(writer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				if(Constants_Global.DEBUG){e1.printStackTrace(p); p.flush();}else e1.printStackTrace();
			}
		}
		
		if(remoteDeviceName == null)remoteDeviceName = "";
		
		textview = new TextView(this);
		textview.setText(getString(R.string.connecting));
		textview.setTextColor(getResources().getColor(connecting_id));
		textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, UnitSize.devicebondstatesize);

		RelativeLayout.LayoutParams barlp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		UnitSize.mysetMargins(BluetoothPBAPActivity.this,barlp,135, 100, 0, 0);
		progresscontainer.removeView(Cyclepb);
		progresscontainer.addView(Cyclepb, barlp);

		RelativeLayout.LayoutParams txtlp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		textview.setGravity(Gravity.CENTER_HORIZONTAL);
		UnitSize.mysetMargins(BluetoothPBAPActivity.this,txtlp,0, 170, 0, 0);
		progresscontainer.addView(textview, txtlp);
		Cyclepb.setVisibility(View.VISIBLE);
		isCurrentActivity=true;

		isBonding=true;
		
		
		

//		m_Binder=new TransportServiceBinder(this,Constants_Global.Bluetooth_PBAP_Service);
		
    	//设定过滤接收器
    	IntentFilter filter=new IntentFilter();
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_SUCCESS);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_FAILED);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_SUCCESS);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.DISCONNECT_REMOTE_DEVICE_SUCCESS);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.ABORT_GETTING_PHONEBOOK_DATA_SUCCESS);
    	
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_BLUETOOTH_PBAP_PROGRESS_BAR);
    	registerReceiver(m_BluetoothReceiver,filter);
    	
    	m_PBAPBondThread=new Thread()
    	{
    		@Override
    		public void run()
    		{
    			//判断本机与远程设备的配对情况
//    			while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_BONDED)
//    			{
//    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
//    				try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//		    			m_Handler.obtainMessage(MSG.BOND_REMOTEDEVICE_FAILED).sendToTarget();
//						e.printStackTrace();
//					}
//    			}
    			Log.i("aaa", "与PBAP服务端绑定成功");
    			//如果成功则发送消息给Handler
    			m_Handler.obtainMessage(MSG.BOND_REMOTEDEVICE_SUCCESS).sendToTarget();
    		}
    	};
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		//配对
//		BluetoothHiddenMethod.CreateBond(m_BluetoothDevice);
		//创建线程检查配对情况
		m_PBAPBondThread.start();	
	}
	
	@Override
	public void onStop()
	{
		stopService(new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class));
		unregisterReceiver(m_BluetoothReceiver);
		super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed()
	{
//		if(!getIsInitializing())
//		{
//		//添加一个操作提示框，是否中断传输并返回
//		abortGettingPhonebookData();
//		}
		super.onBackPressed();
	}
	
	/**
	 * 初始化PBAP服务
	 */
    public void initPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE, m_BluetoothDevice);
		startService(intent);
    }
	
	/**
	 * 连接远程蓝牙设备的PBAP服务
	 */
    public void connectPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.CONNECT_REMOTE_DEVICE);
		startService(intent);
    }
    
    /**
     * 获取联系人数量
     */
    public void getAllContactsCount()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.GET_REMOTE_DEVICE_CONTACTS_COUNT);
		startService(intent);
    }
    
    /**
     * 获取设备电话簿
     */
    public void getPhonebookData()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.GET_REMOTE_DEVICE_PHONEBOOK_DATA);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.PBAP_RECEIVE_FILE_PATH, Constants_File.Path.PBAP_REV);
    	startService(intent);
    }
    
    /**
     * 关闭连接
     */
    public void disconnectPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.DISCONNECT_REMOTE_DEVICE);
		startService(intent);
    }
    
    /**
     * 中断PBAP电话簿传输
     */
//    public void abortGettingPhonebookData()
//    {
//    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
//    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.ABORT_GETTING_PHONEBOOK_DATA);
//		startService(intent);
//    }
    
    //注册广播接收器，用于接收Service端广播消息
    private BroadcastReceiver m_BluetoothReceiver=new BroadcastReceiver()
    {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			Log.i("aaa", action);
			if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR.equals(action))
			{
				int current_count=intent.getIntExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.CURRENT_PHONEBOOK_COUNT,-1);
				pbbar.setProgress(current_count);
			}
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_BLUETOOTH_PBAP_PROGRESS_BAR.equals(action))
			{
				Log.i("aaa", "开始初始化PBAP服务");
				textview.setText(getString(R.string.pbapreceiving));
				textview.setTextColor(getResources().getColor(connecting_id));
				Cyclepb.setVisibility(View.INVISIBLE);
				progresscontainer.removeView(Cyclepb);

				RelativeLayout.LayoutParams barlp = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, // width
						ViewGroup.LayoutParams.WRAP_CONTENT // height

				);
				
				
				UnitSize.mysetMargins(BluetoothPBAPActivity.this,barlp,20, 100, 20, 0);
				progresscontainer.removeView(pbbar);
				progresscontainer.addView(pbbar, barlp);
				pbbar.setVisibility(View.VISIBLE);
				
				int max_count=intent.getIntExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.TOTAL_PHONEBOOK_COUNT,-1);
				pbbar.setMax(max_count);
			}
			/**
			 * 连接PBAP服务端成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_SUCCESS.equals(action))
			{			
				getPhonebookData();
			}
			/**
			 * 连接PBAP服务端失败
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_FAILED.equals(action))
			{
				setIsInitializing(true);
				showDialog(DLG.CONNECT_PBAP_SERVICE_FAILED);
			}
			/**
			 * 初始化PBAP服务成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_SUCCESS.equals(action))
			{
				setIsInitializing(false);
				connectPBAPService();
			}
			/**
			 * 初始化PBAP服务失败
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_FAILED.equals(action))
			{
				setIsInitializing(true);
				showDialog(DLG.INIT_PBAP_SERVICE_FAILED);
			}
			/**
			 * 获取联系人总数成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT.equals(action))
			{
				//联系人数量
//				int count=intent.getIntExtra(Constants_Bluetooth.Key.REMOTE_DEVICE_CONTACTS_COUNT, -1);
			}
			/**
			 * 获取电话簿成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA.equals(action))
			{
				//获取联系人数据成功，开始导入
				disconnectPBAPService();
			}
			/**
			 * 断开PBAP连接成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.DISCONNECT_REMOTE_DEVICE_SUCCESS.equals(action))
			{
				startActivity(new Intent(BluetoothPBAPActivity.this,importContactsActivity.class)
				.putExtra("filepath", Constants_File.Path.PBAP_REV)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
			/**
			 * 中断PBAP电话簿传输成功
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.ABORT_GETTING_PHONEBOOK_DATA_SUCCESS.equals(action))
			{
				Log.i("aaa", "中断PBAP电话簿传输成功");
			}
		}

    };
    
    /************** 提示对话框 ***************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DLG.INIT_PBAP_SERVICE_FAILED:
			return new AlertDialog.Builder(BluetoothPBAPActivity.this).setTitle(R.string.alertdialog_title)
					.setMessage(getString(R.string.init_pbap_service_failed))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onBackPressed();
						}
					})
					.setCancelable(false)
					.create();
		case DLG.CONNECT_PBAP_SERVICE_FAILED:
			return new AlertDialog.Builder(BluetoothPBAPActivity.this).setTitle(R.string.alertdialog_title)
					.setMessage(getString(R.string.connect_pbap_service_failed))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onBackPressed();
						}
					})
					.setCancelable(false)
					.create();
		default:
			return null;
		}
	}
}
