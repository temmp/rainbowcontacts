package ccdr.rainbow.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



import com.ccdr.rainbowcontacts.R;
import com.ccdr.rainbowcontacts.RainbowContactsActivity;

import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Service.BluetoothClientService;
import ccdr.rainbow.Service.WifiClientService;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.TransportServiceBinder;
import ccdr.rainbow.Tool.UnitSize;
import ccdr.rainbow.Tool.WifiDevice;

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
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WifiClientActivity extends Activity {
	/**
	 * 远程WiFi设备列表
	 */
	private List<WifiDevice> m_RemoteWifiDevices=null;
	/**
	 * 用于显示远程WiFi设备的列表
	 */
	private ListView m_WifiDevicesListView;
	/**
	 * 绑定的Service对象
	 */
//	private WifiClientService m_ServiceBinder=null;
	/**
	 * 用于获取绑定的Service对象
	 */
//	private TransportServiceBinder m_Binder=null;
	/**
	 * 保存了WiFi设备对象
	 */
//	private List<Map<String,String>> m_WifiDeviceList=null;
	/**
	 * 是否正在刷新WiFi设备列表
	 */
	private boolean isRefreshingDevice=false;
	/**
	 * 是否第一次按下返回键
	 */
	private boolean isFirstClick=true;
	
	
	
	/**
	 * 设备列表版面
	 */
	protected LinearLayout DeviceLayout;

	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2,radarback;
	private TextView up_text,radartext2;
	private LinearLayout search_button,search_button1;
	private int radartext1_id,bondstatetv_id,onlyoneuncheckedclick_id,onlyonecheckedclick_id;
	
	private ProgressDialog myProgressDialog = null;
	
	/**
	 * 雷达图片对象
	 */
	protected ImageView imageRader = null;
	/**
	 * 雷达图片动画
	 */
	protected AnimationDrawable animationRader = null;
	/**
	 * 顶栏可按下部分
	 */
	protected LinearLayout beginsearchLayout;
	/**
	 * 顶栏文字（点击此处重新查找设备）
	 */
	protected TextView uplayText = null;
	
	
	
	
	
	/**
	 * 记录搜索到的WiFi设备信息
	 */
//	protected List<WifiDevice> DevicesList = new ArrayList<WifiDevice>();
	/**
	 * 记录搜索到多少个设备
	 */
	protected int count = -1;
	/**
	 * 记录用户点选的设备
	 */
	protected int chooseid = -1;
	/**
	 * 设备列表版面
	 */
	protected LinearLayout DeviceList;
	/**
	 * 每个元素里面包含了一个搜索到的设备实体
	 */
	protected List<RelativeLayout> rl = new LinkedList<RelativeLayout>();
	
	
	public class MSG
	{
		/**
		 * 发出消息Message让handler启动动画
		 */
		public static final int MOVING_FRAMES = 800;
		/**
		 * WiFi设备搜索完毕
		 */
		public static final int SEARCH_FINISH_WIFI=801;
		/**
		 * 刷新WiFi设备列表
		 */
		public static final int REFRESH_WIFI_DEVICE_LIST=802;
		/**
		 * 服务端（接收方）同意接受文件
		 */
		public static final int ACCEPT_RECEIVE_FILE=803;
		/**
		 * 服务端（接收方）拒绝接受文件
		 */
		public static final int REFUSE_RECEIVE_FILE=804;
	}
	
	public class DLG
	{
		/**
		 * 服务端（接收方）接受请求
		 */
		public static final int ACCEPT = 900;
		/**
		 * 服务端（接收方）拒绝请求
		 */
		public static final int REFUSE = 901;
		/**
		 * 发送文件失败
		 */
		public static final int FAILED = 902;
		/**
		 * 发送文件成功
		 */
		public static final int SUCCESS = 903;
	}
	
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			//刷新WiFi设备列表
			case MSG.REFRESH_WIFI_DEVICE_LIST:
				refreshwifidevices();
				break;
			case 1:
				break;
			case MSG.MOVING_FRAMES:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageRader.setBackgroundResource(R.drawable.radar_anim);
				animationRader = (AnimationDrawable) imageRader
						.getBackground();
				animationRader.start();
				break;
			case MSG.SEARCH_FINISH_WIFI:
				uplayText.setVisibility(View.VISIBLE);
				beginsearchLayout.setClickable(true);
				break;
//			case MSG.ACCEPT_RECEIVE_FILE:
//				showDialog(DLG.CONFIRM);// 对方手机已经同意Connect请求，弹出是否现在传递数据的询问对话框
//				break;
//			case MSG.REFUSE_RECEIVE_FILE:
//				showDialog(DLG.CANCEL);// 对方手机已经拒绝Connect请求，弹出被拒绝的提示对话框
//				break;
				
				default:break;
			}
		}
	};
	
	private BroadcastReceiver m_Receiver=new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(Constants_Interaction.Action.Wifi.WifiClient.REMOTE_WIFI_DEVICE_FOUND.equals(action))
			{
					
					WifiDevice device=intent.getParcelableExtra(Constants_Interaction.Key.Wifi.WifiClient.SENDER_WIFI_DEVICE);
				
					Log.i("bbb", device.getWifiDeviceName());
					
					m_RemoteWifiDevices.add(device);
				
					m_Handler.sendEmptyMessage(MSG.REFRESH_WIFI_DEVICE_LIST);
			}
			else if(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_SUCCESS.equals(action))
			{
//				new AlertDialog.Builder(WifiClientActivity.this)
//				.setTitle("文件发送成功！").create().show();
				myProgressDialog.dismiss();
				showDialog(DLG.SUCCESS);
//		    	Intent intent = new Intent(WifiClientActivity.this,
//						.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
			}
			else if(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_ACCEPT.equals(action))
			{
//			new AlertDialog.Builder(WifiClientActivity.this)
//			.setTitle("对方接受请求！！！").create().show();
			myProgressDialog.dismiss();
			showDialog(DLG.ACCEPT);
			}
			else if(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED.equals(action))
			{
//				new AlertDialog.Builder(WifiClientActivity.this)
//				.setTitle("文件发送失败！！！").create().show();
				myProgressDialog.dismiss();
				showDialog(DLG.FAILED);
//		    	Intent intent = new Intent(WifiClientActivity.this,
//						.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
			}
			else if(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_REFUSE.equals(action))
			{
//				new AlertDialog.Builder(WifiClientActivity.this)
//				.setTitle("对方拒绝请求！！！").create().show();
				showDialog(DLG.REFUSE);
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wificlient);
		
		DeviceList = (LinearLayout) findViewById(R.id.DevicesList);
		
		m_RemoteWifiDevices=new ArrayList<WifiDevice>(){};
		
		
		
		skin_linearLayout = (LinearLayout)findViewById(R.id.parentid);
//		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
//		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		radarback = (LinearLayout)findViewById(R.id.radarback_wifi);
//		search_button = (ImageButton)findViewById(R.id.search_button);
//		search_button1 = (ImageButton)findViewById(R.id.search_button1);
		up_text = (TextView)findViewById(R.id.up_text);
uplayText = (TextView) findViewById(R.id.uplaytext_wifi);
		
		imageRader = (ImageView) findViewById(R.id.radarView_wifi);
		radartext2 = (TextView) findViewById(R.id.searchingdevicetext_wifi);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
//		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
//		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		radarback.setBackgroundResource(changeSkinUtil.radarback);
//		search_button.setImageResource(changeSkinUtil.search_button);
//		search_button1.setImageResource(changeSkinUtil.search_button1);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		uplayText.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		radartext2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		DeviceLayout = (LinearLayout) findViewById(R.id.DevicesList_wifi);
		onlyonecheckedclick_id = changeSkinUtil.onlyonecheckedclick_id;
		onlyoneuncheckedclick_id = changeSkinUtil.onlyoneuncheckedclick_id;
		radartext1_id = changeSkinUtil.uplayText;
		bondstatetv_id = changeSkinUtil.bondstatetv_id;
//		mListView = (ListView) findViewById(R.id.MyListView2);
//
//		aryAdapter = new ArrayAdapter<String>(WifiClientActivity.this,
//				android.R.layout.simple_list_item_1, aryListTask);
//		mListView.setAdapter(aryAdapter);
		
		
		imageRader.setBackgroundResource(R.drawable.radar_anim);
		animationRader = (AnimationDrawable) imageRader.getBackground();
		beginsearchLayout = (LinearLayout) findViewById(R.id.searchlinearLayout);
		beginsearchLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					onbeginsearch();
			}
		});

//		Intent intent = getIntent();
//		username = intent.getStringExtra("username");
		
		
		
		
		
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Log.i("bbb", "onResume-WifiClientActivity");
		
		onbeginsearch();
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_SUCCESS);
		filter.addAction(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED);
		filter.addAction(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_REFUSE);
		filter.addAction(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_ACCEPT);
		filter.addAction(Constants_Interaction.Action.Wifi.WifiClient.REMOTE_WIFI_DEVICE_FOUND);
		registerReceiver(m_Receiver, filter);
		
		
		receivebroadcast();
		getremotedevice();
		setRefreshingState(true);
		
		//更新WiFi设备列表的线程

	}

	@Override
	public void onPause()
	{
		super.onPause();
		stopreceivebroadcast();
		stopgettingremotedevice();
		unregisterReceiver(m_Receiver);
	}
	@Override
	public void onDestroy()
	{
		stopRefreshing();		
		stopService(new Intent(WifiClientActivity.this,WifiClientService.class));
//		m_Binder.unBindService();
//		m_Binder.stopBindedService();
		Log.e("bbb", "onDestroy-WifiClientActivity");
		super.onDestroy();
	}
	/**
	 * 刷新WiFi设备列表
	 * @param view
	 */
	public void refreshwifidevices()
	{
		/* 从intent中取得搜索结果数据 */
//		BluetoothDevice device = intent
//				.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			DeviceLayout.removeAllViews();
			rl.clear();
			count=-1;
			
			System.out.println("执行Handler");
//			String sg = (String) msg.obj;
//			aryListTask.add(sg);
		for(int i=0;i<m_RemoteWifiDevices.size();++i)
		{	
			WifiDevice device=m_RemoteWifiDevices.get(i);			
			String name=device.getWifiDeviceName();
			String ip_address=device.getWifiDeviceIPAddress();
		/* 将结果添加到列表中 */
		// 名字
		TextView nametv = new TextView(WifiClientActivity.this);
		// 绑定状态
		TextView bondstatetv = new TextView(WifiClientActivity.this);
		// 对应图片,手机是手机图片,电脑是电脑图片
		ImageView image = new ImageView(WifiClientActivity.this);
		// 将要包含以上元素的一个relativelayout
		final RelativeLayout rltmp = new RelativeLayout(WifiClientActivity.this);

		// 搜到重复的设备则丢弃，搜到名字为空的，没有实际用途，也进行丢弃
		if (device.getWifiDeviceName() == null)
			return;
		count++;

		// 检测是不是手机
		boolean isphone = false;
		final int deviceid = count;
		isphone = isPhone(device);
		isphone =true;
		setDeviceImage(image, isphone);

		// 判断是否已经配对过
//		String bondstate = getDeviceBond(device, context);
		// 设置绑定状态的文字属性
		bondstatetv.setText(ip_address);
		bondstatetv.setTextColor(getResources().getColor(bondstatetv_id));
		bondstatetv.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				UnitSize.devicebondstatesize);
		bondstatetv.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// 设置设备名字的状态属性,为了不影响美观,将多余部分的内容去掉
		String devicename = device.getWifiDeviceName();
		if (devicename.length() > 15) {
			devicename = devicename.substring(0, 14) + "...";
		}
		nametv.setText(devicename);
		nametv.setTextColor(getResources().getColor(radartext1_id));
		nametv.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				UnitSize.devicenamesize);
		nametv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// 设置relativelayout的属性和点击事件
		rltmp.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		// rltmp.setBackgroundResource(R.drawable.clicklayout);
		rltmp.setBackgroundResource(onlyoneuncheckedclick_id);
		rltmp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean cancel = false;
				// TODO Auto-generated method stub
				for (int i = 0; i < rl.size(); i++) {
					RelativeLayout onerl = rl.get(i);
					if (i == deviceid) {
						Log.d("bluetoothpage", deviceid + "  " + chooseid);
						if (deviceid == chooseid) {
							onerl
									.setBackgroundResource(onlyoneuncheckedclick_id);

							cancel = true;
						} else
							onerl
									.setBackgroundResource(onlyonecheckedclick_id);

					} else
						onerl
								.setBackgroundResource(onlyoneuncheckedclick_id);
				}
				if (cancel)
					chooseid = -1;
				else
					chooseid = deviceid;
			}
		});

		// 将单个设备的layout加到list中
		rl.add(rltmp);

		// 将设备的名字加到单个设备的layout中
		RelativeLayout.LayoutParams namelp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height
		);
		UnitSize.mysetMargins(WifiClientActivity.this, namelp,
				UnitSize.devicenamemargins);
		rltmp.addView(nametv, namelp);

		// 将设备的状态加到单个设备的layout中
		RelativeLayout.LayoutParams statelp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		UnitSize.mysetMargins(WifiClientActivity.this, statelp,
				UnitSize.devicebondmargins);
		rltmp.addView(bondstatetv, statelp);

		// 将设备的图片加到单个设备的layout中
		RelativeLayout.LayoutParams imagelp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		if (isphone)
			UnitSize.mysetMargins(WifiClientActivity.this, imagelp,
					UnitSize.devicemarginsmobile);
		else
			UnitSize.mysetMargins(WifiClientActivity.this, imagelp,
					UnitSize.devicemarginslaptop);

		rltmp.addView(image, imagelp);

		// 将设备的layout加到整个设备列表的版面
		DeviceLayout.addView(rltmp);
		}
		
	}
	
	/**
	 * 获取WiFi设备
	 */
	public void getremotedevice()
	{
		Intent intent=new Intent();
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.GET_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	/**
	 * 停止获取WiFi设备
	 */
	public void stopgettingremotedevice()
	{
		Intent intent=new Intent();
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	/**
	 * 开始接收广播
	 * @param view
	 */
	public void receivebroadcast()
	{
		Intent intent=new Intent();
//		intent.setClass(this, m_Binder.getServiceClass());
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.LISTEN_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	
	/**
	 * 停止接收广播
	 * @param view
	 */
	public void stopreceivebroadcast()
	{
		Intent intent=new Intent();
//		intent.setClass(this, m_Binder.getServiceClass());
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.STOP_LISTENING_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	/**
	 * 发送文件
	 * @param path 发送文件的路径
	 * @param ip 目的IP地址
	 * @param type 文件类型
	 */
	public void sendFile(String path,String ip,byte type)
	{
		Intent intent=new Intent();
//		intent.setClass(this, m_Binder.getServiceClass());
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.SEND_FILE);
		
		intent.putExtra(Constants_Wifi.Key.SEND_FILE_PATH,path);
		intent.putExtra(Constants_Wifi.Key.RECEIVER_WIFI_DEVICE_IPADDRESS,ip);
		intent.putExtra(Constants_Wifi.Key.SEND_FILE_TYPE,type);
		startService(intent);
	}
/**
 * 发送请求
 * @param ip
 * @param name
 */
public void sendRequest(String ip,String name)
{
	Intent intent=new Intent();
	intent.setClass(WifiClientActivity.this, WifiClientService.class);
	intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.SEND_REQUEST);
	
	intent.putExtra(Constants_Wifi.Key.RECEIVER_WIFI_DEVICE_IPADDRESS,ip);
	intent.putExtra(Constants_Wifi.Key.SENDER_WIFI_DEVICE_NAME,name);
	startService(intent);
}

	/**
	 * 返回是否正在刷新设备列表
	 * @return
	 */
	public boolean getRefreshingState()
	{
		return isRefreshingDevice;
	}
	/**
	 * 设置是否正在刷新设备列表
	 * @param state
	 */
	public void setRefreshingState(boolean state)
	{
		isRefreshingDevice=state;
	}
	/**
	 * 停止刷新设备列表
	 */
	public void stopRefreshing()
	{
		setRefreshingState(false);
	}
    /**
     * 提示
     * @param toast
     */
    public void DisplayToast(String toast)
    {
    	Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }
    
    
    /**
     * “确定”按钮
     * @param view
     */
    public void onconfirmbtn(View view)
    {
    	// 没有选任何设备
    			if (chooseid == -1 || m_RemoteWifiDevices == null) {
    				Toast.makeText(this, this.getString(R.string.choosedevice),
    						Toast.LENGTH_LONG).show();
    			} else {
    				final WifiDevice devicedchoose = m_RemoteWifiDevices.get(chooseid);
    				/**
					 * 用户选择后马上停止接收广播
					 */
					stopreceivebroadcast();
					
					myProgressDialog = ProgressDialog.show(WifiClientActivity.this,
							getString(R.string.dialog_title_wifi), getString(R.string.waiting_response_wifi), true);
					myProgressDialog.setCancelable(false);
					
					//发送请求
					sendRequest(devicedchoose.getWifiDeviceIPAddress(),devicedchoose.getWifiDeviceName());

    			}
    }
    @Override
    public void onBackPressed()
    {
    	if(isFirstClick)
    	{
    		Toast.makeText(WifiClientActivity.this, "再按一次返回主界面", Toast.LENGTH_LONG).show();
    		isFirstClick=false;
    		return;
    	}
    	super.onBackPressed();
    	Intent intent = new Intent(WifiClientActivity.this,
				RainbowContactsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
    /**
     * “返回”按钮
     * @param view
     */
    public void oncancelbtn(View view)
    {
    	onBackPressed();
    }
    
    
	//根据远程设备信息为搜索到的设备设置显示图片。（电话或者电脑）
	protected boolean setDeviceImage(ImageView image, boolean isphone) {

		try {

			if (isphone) {
				image.setBackgroundResource(R.drawable.phone);
			} else {
				image.setBackgroundResource(R.drawable.pc);
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isphone;
	}
	protected boolean isPhone(WifiDevice mDevice) {

		boolean isphone = false;

		return isphone;

	}

	protected void onbeginsearch()
	{
		m_RemoteWifiDevices.clear();
		getremotedevice();
		uplayText.setVisibility(View.INVISIBLE);
		// 开始搜索后改变雷达右边的文字
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//清空页面，初始化相关数值
		count = -1;
		chooseid = -1;
		DeviceLayout.removeAllViews();
		imageRader.setVisibility(View.VISIBLE);

		rl.clear();
		m_RemoteWifiDevices.clear();
//		aryListTask.clear();
	
		if (!((WifiManager) getSystemService(Context.WIFI_SERVICE)).isWifiEnabled())
			return;

		//雷达动画效果，通过线程实现
		m_Handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				imageRader.setBackgroundResource(R.drawable.radar_anim);
				animationRader = (AnimationDrawable) imageRader
						.getBackground();
				animationRader.start();
			}
		}, 50);

		new Thread()
		{
			@Override
			public void run()
			{
				try {
					Thread.sleep(6000);
					m_Handler.sendEmptyMessage(MSG.SEARCH_FINISH_WIFI);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		beginsearchLayout.setClickable(false);
	}

	/************** 提示对话框 ***************/
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DLG.SUCCESS:
			return new AlertDialog.Builder(WifiClientActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(R.string.send_file_success).setIcon(
							android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// onBackPressed();
							Intent intent = new Intent(WifiClientActivity.this,
									RainbowContactsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					})
					.setCancelable(false)
					.create();
		case DLG.ACCEPT:
			return new AlertDialog.Builder(WifiClientActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(getString(R.string.confirm_to_send_data_wifi) + m_RemoteWifiDevices.get(chooseid).getWifiDeviceName() + "！")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							myProgressDialog = ProgressDialog.show(WifiClientActivity.this,
									getString(R.string.dialog_title_wifi), getString(R.string.sending), true);
							myProgressDialog.setCancelable(false);
							
							sendFile(Constants_File.Path.WIFI_SEND,m_RemoteWifiDevices.get(chooseid).getWifiDeviceIPAddress(),Constants_Wifi.File.FILE_TYPE_VCARD);
//							Intent intent = new Intent(WifiClientActivity.this,
//									selectContactsActivity_WifiClient.class);
//							intent.putExtra("OtherIP", m_RemoteWifiDevices.get(chooseid).getWifiDeviceName());
//							startActivity(intent);
						}
					})
					.setCancelable(false)
					.create();
		case DLG.REFUSE:
			return new AlertDialog.Builder(WifiClientActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(R.string.receiver_refuse_wifi).setIcon(
							android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// onBackPressed();
							Intent intent = new Intent(WifiClientActivity.this,
									RainbowContactsActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
						}
					})
					.setCancelable(false)
					.create();
		case DLG.FAILED:
			return new AlertDialog.Builder(WifiClientActivity.this).setTitle(R.string.dialog_title_wifi)
					.setMessage(R.string.send_file_failed).setIcon(
							android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// onBackPressed();
							Intent intent = new Intent(WifiClientActivity.this,
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
