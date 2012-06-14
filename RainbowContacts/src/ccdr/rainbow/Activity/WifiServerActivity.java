package ccdr.rainbow.Activity;

import java.io.IOException;


import com.ccdr.rainbowcontacts.R;
import com.ccdr.rainbowcontacts.RainbowContactsActivity;

import ccdr.rainbow.Service.WifiServerService;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.TransportServiceBinder;
import ccdr.rainbow.Tool.UnitSize;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WifiServerActivity extends Activity {
	
	private TextView ReceiveRemind = null;// 提醒客户用的text
	private RelativeLayout progresscontainer = null;// 包含进度条的layout
	private ProgressBar Cyclepb = null;// 等待对方连接圆形进度
	private ProgressBar Horipb = null;// 接受信息进度
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private LinearLayout button;
	private TextView btservertext;
	
	
	public class MSG
	{

	}
	
	public class DLG
	{
		/**
		 * 接受文件失败
		 */
		public static final int FAILED = 902;
		/**
		 * 接受文件成功
		 */
		public static final int SUCCESS = 903;
	}
	
	
	/**
	 * 绑定的Service对象
	 */
//	private WifiTransportService m_ServiceBinder=null;
	/**
	 * 用于获取绑定的Service对象
	 */
	private TransportServiceBinder m_Binder=null;
	
	private BroadcastReceiver m_File_Receiver=new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context,Intent intent)
		{
			String action=intent.getAction();
			if(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_REQUEST.equals(action))
			{
//				m_File_Receive_Handler.sendEmptyMessage(0);
				String sender_device_name=intent.getStringExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_NAME);
				String sender_device_ipaddress=intent.getStringExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_IPADDRESS);
				String sender_device_macaddress=intent.getStringExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_MACADDRESS);
				//用户选择是否接收文件的对话框
				new AlertDialog.Builder(WifiServerActivity.this)
				.setMessage(new String(
						getString(R.string.sender)+sender_device_name.trim()+"\n"+
						getString(R.string.senderip)+sender_device_ipaddress.trim()+"\n"+
						getString(R.string.sendermac)+sender_device_macaddress.trim()))
				.setTitle(R.string.recv_file)
				.setCancelable(false)
				.setPositiveButton(R.string.yes, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int whitch)
					{
						stopsendingbroadcast(null);
//						m_ServiceBinder=(WifiTransportService) m_Binder.getServiceBinder();
//						m_ServiceBinder.setUserChoice(0);
						((WifiServerService)m_Binder.getServiceBinder()).setUserChoice(0);
						receiveFile(Constants_File.Path.WIFI_RECV);
					}
				})
				.setNegativeButton(R.string.no, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog,int whitch)
					{
//						m_ServiceBinder=(WifiTransportService) m_Binder.getServiceBinder();
//						m_ServiceBinder.setUserChoice(1);
						((WifiServerService)m_Binder.getServiceBinder()).setUserChoice(1);
						
						startActivity(new Intent(WifiServerActivity.this,RainbowContactsActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
					}
				})
				.create().show();
			}
			else if(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_SUCCESS.equals(action))
			{
//				showDialog(DLG.SUCCESS);
				/**
				 * 跳转到导入界面
				 */
				startActivity(new Intent(WifiServerActivity.this,importContactsActivity.class)
				.putExtra("filepath", Constants_File.Path.WIFI_RECV)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
			else if(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_FAILED.equals(action))
			{
				showDialog(DLG.FAILED);
			}
			else if(Constants_Interaction.Action.Wifi.WifiServer.START_SERVICE_FAILED.equals(action))
			{
//				new AlertDialog.Builder(WifiServerActivity.this)
//				.setTitle("打开Service服务失败！！！").create().show();
			}
		}
	};
	
	private Handler m_File_Receive_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:break;
			case 1:break;
			default:break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiserver);
		
		m_Binder=new TransportServiceBinder(this,Constants_Global.Wifi_Server_Service);
		
		progresscontainer = (RelativeLayout) findViewById(R.id.progresscontainer);
		Horipb = (ProgressBar) findViewById(R.id.importbar);
		Cyclepb = (ProgressBar) findViewById(R.id.cyclebar);
		ReceiveRemind = (TextView) findViewById(R.id.btservertext);
		String ss = getString(R.string.building_server);
		skin_linearLayout = (LinearLayout)findViewById(R.id.parentid);
//		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
//		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		up_text = (TextView)findViewById(R.id.up_text);
		down_text = (TextView)findViewById(R.id.down_text);
		down_text1 = (TextView)findViewById(R.id.down_text1);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
//		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
//		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		System.out.println(changeSkinUtil.up_text+"--magazineActivityTitle----"+R.color.title);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		down_text.setTextColor(this.getResources().getColor(changeSkinUtil.down_text));
		down_text1.setTextColor(this.getResources().getColor(changeSkinUtil.down_text1));
		ReceiveRemind.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
//		btservertext = (TextView)findViewById(R.id.btservertext);
//		btservertext.setTextColor(this.getResources().getColor(changeSkinUtil.connecting_id));
//		button = (LinearLayout)findViewById(R.id.backbutton);
//		button.setImageResource(changeSkinUtil.btserver_backbutton);

		progresscontainer.removeView(Horipb);
		ReceiveRemind.setText(ss);
		RelativeLayout.LayoutParams barlp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		UnitSize.mysetMargins(WifiServerActivity.this, barlp, 135, 100, 0, 0);
		progresscontainer.removeView(Cyclepb);
		progresscontainer.addView(Cyclepb, barlp);

		Cyclepb.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_REQUEST);//文件发送请求
		filter.addAction(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_SUCCESS);//文件接收成功
		filter.addAction(Constants_Interaction.Action.Wifi.WifiServer.FILE_RECEIVE_FAILED);//文件接收失败
		filter.addAction(Constants_Interaction.Action.Wifi.WifiServer.START_SERVICE_FAILED);//文件接收失败
		registerReceiver(m_File_Receiver, filter);
		
		//等Activity获得Service对象后再开始接收
		new Thread()
		{
			@Override
			public void run()
			{
				int Timeout=7000;
				
				while(m_Binder.getServiceBinder()==null)
				{
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiServer.START_SERVICE_FAILED));
						return;
					}
					Timeout-=300;
					if(Timeout<=0)
					{
						sendBroadcast(new Intent(Constants_Interaction.Action.Wifi.WifiServer.START_SERVICE_FAILED));
						return;
					}
				}
				startacceptrequest(null);
				startsendingbroadcast(null);
			}
		}.start();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		unregisterReceiver(m_File_Receiver);
	}
	
	@Override
	public void onDestroy()
	{
		stopsendingbroadcast(null);
		stopAcceptConnectionRequest();
//		m_Binder.unBindService();
//		m_Binder.stopBindedService();
		stopService(new Intent(WifiServerActivity.this,WifiServerService.class));
		super.onDestroy();
	}
	/*************** 点击了“返回”按钮 *****************/
	public void onmain(View view) {
		onBackPressed();
	}
	
	/**
	 * 开始广播
	 * @param view
	 */
	public void startsendingbroadcast(View view)
	{
		Intent intent=new Intent();
		intent.setClass(this, m_Binder.getServiceClass());
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.START_SENDING_BROADCAST);
		startService(intent);
	}
	
	/**
	 * 停止广播
	 * @param view
	 */
	public void stopsendingbroadcast(View view)
	{
		Intent intent=new Intent();
		intent.setClass(this, m_Binder.getServiceClass());
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.STOP_SENDING_BROADCAST);
		startService(intent);
	}
	
	/**
	 * 接收文件
	 * @param path 接收文件的路径
	 */
	public void receiveFile(String path)
	{
		Intent intent=new Intent();
		intent.setClass(this, m_Binder.getServiceClass());
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.RECEIVE_FILE);
		
		intent.putExtra(Constants_Wifi.Key.RECEIVE_FILE_PATH,path);
		startService(intent);
	}
	
	/**
	 * 接收请求
	 * @param view
	 */
	public void startacceptrequest(View view)
	{
		Intent intent=new Intent();
		intent.setClass(this, m_Binder.getServiceClass());
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.START_ACCEPT_REQUEST);
		startService(intent);
	}
	/**
	 * 停止接收请求
	 */
	public void stopAcceptConnectionRequest()
	{
		Intent intent=new Intent();
		intent.setClass(this, m_Binder.getServiceClass());
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.STOP_ACCEPT_REQUEST);
		startService(intent);
	}
	
	/************** 提示对话框 ***************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DLG.SUCCESS:
			return new AlertDialog.Builder(WifiServerActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(R.string.recv_file_success).setIcon(
							android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// onBackPressed();
							Intent intent = new Intent(WifiServerActivity.this,
									RainbowContactsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					})
					.setCancelable(false)
					.create();
		case DLG.FAILED:
			return new AlertDialog.Builder(WifiServerActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(R.string.recv_file_failed).setIcon(
							android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// onBackPressed();
							Intent intent = new Intent(WifiServerActivity.this,
									RainbowContactsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					})
					.setCancelable(false)
					.create();

		default:
			return null;
		}
	}
}
