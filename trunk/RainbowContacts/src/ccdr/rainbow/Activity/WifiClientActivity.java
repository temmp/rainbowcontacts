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
	 * Զ��WiFi�豸�б�
	 */
	private List<WifiDevice> m_RemoteWifiDevices=null;
	/**
	 * ������ʾԶ��WiFi�豸���б�
	 */
	private ListView m_WifiDevicesListView;
	/**
	 * �󶨵�Service����
	 */
//	private WifiClientService m_ServiceBinder=null;
	/**
	 * ���ڻ�ȡ�󶨵�Service����
	 */
//	private TransportServiceBinder m_Binder=null;
	/**
	 * ������WiFi�豸����
	 */
//	private List<Map<String,String>> m_WifiDeviceList=null;
	/**
	 * �Ƿ�����ˢ��WiFi�豸�б�
	 */
	private boolean isRefreshingDevice=false;
	/**
	 * �Ƿ��һ�ΰ��·��ؼ�
	 */
	private boolean isFirstClick=true;
	
	
	
	/**
	 * �豸�б����
	 */
	protected LinearLayout DeviceLayout;

	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2,radarback;
	private TextView up_text,radartext2;
	private LinearLayout search_button,search_button1;
	private int radartext1_id,bondstatetv_id,onlyoneuncheckedclick_id,onlyonecheckedclick_id;
	
	private ProgressDialog myProgressDialog = null;
	
	/**
	 * �״�ͼƬ����
	 */
	protected ImageView imageRader = null;
	/**
	 * �״�ͼƬ����
	 */
	protected AnimationDrawable animationRader = null;
	/**
	 * �����ɰ��²���
	 */
	protected LinearLayout beginsearchLayout;
	/**
	 * �������֣�����˴����²����豸��
	 */
	protected TextView uplayText = null;
	
	
	
	
	
	/**
	 * ��¼��������WiFi�豸��Ϣ
	 */
//	protected List<WifiDevice> DevicesList = new ArrayList<WifiDevice>();
	/**
	 * ��¼���������ٸ��豸
	 */
	protected int count = -1;
	/**
	 * ��¼�û���ѡ���豸
	 */
	protected int chooseid = -1;
	/**
	 * �豸�б����
	 */
	protected LinearLayout DeviceList;
	/**
	 * ÿ��Ԫ�����������һ�����������豸ʵ��
	 */
	protected List<RelativeLayout> rl = new LinkedList<RelativeLayout>();
	
	
	public class MSG
	{
		/**
		 * ������ϢMessage��handler��������
		 */
		public static final int MOVING_FRAMES = 800;
		/**
		 * WiFi�豸�������
		 */
		public static final int SEARCH_FINISH_WIFI=801;
		/**
		 * ˢ��WiFi�豸�б�
		 */
		public static final int REFRESH_WIFI_DEVICE_LIST=802;
		/**
		 * ����ˣ����շ���ͬ������ļ�
		 */
		public static final int ACCEPT_RECEIVE_FILE=803;
		/**
		 * ����ˣ����շ����ܾ������ļ�
		 */
		public static final int REFUSE_RECEIVE_FILE=804;
	}
	
	public class DLG
	{
		/**
		 * ����ˣ����շ�����������
		 */
		public static final int ACCEPT = 900;
		/**
		 * ����ˣ����շ����ܾ�����
		 */
		public static final int REFUSE = 901;
		/**
		 * �����ļ�ʧ��
		 */
		public static final int FAILED = 902;
		/**
		 * �����ļ��ɹ�
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
			//ˢ��WiFi�豸�б�
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
//				showDialog(DLG.CONFIRM);// �Է��ֻ��Ѿ�ͬ��Connect���󣬵����Ƿ����ڴ������ݵ�ѯ�ʶԻ���
//				break;
//			case MSG.REFUSE_RECEIVE_FILE:
//				showDialog(DLG.CANCEL);// �Է��ֻ��Ѿ��ܾ�Connect���󣬵������ܾ�����ʾ�Ի���
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
//				.setTitle("�ļ����ͳɹ���").create().show();
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
//			.setTitle("�Է��������󣡣���").create().show();
			myProgressDialog.dismiss();
			showDialog(DLG.ACCEPT);
			}
			else if(Constants_Interaction.Action.Wifi.WifiClient.FILE_SEND_FAILED.equals(action))
			{
//				new AlertDialog.Builder(WifiClientActivity.this)
//				.setTitle("�ļ�����ʧ�ܣ�����").create().show();
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
//				.setTitle("�Է��ܾ����󣡣���").create().show();
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
		
		//����WiFi�豸�б���߳�

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
	 * ˢ��WiFi�豸�б�
	 * @param view
	 */
	public void refreshwifidevices()
	{
		/* ��intent��ȡ������������� */
//		BluetoothDevice device = intent
//				.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			DeviceLayout.removeAllViews();
			rl.clear();
			count=-1;
			
			System.out.println("ִ��Handler");
//			String sg = (String) msg.obj;
//			aryListTask.add(sg);
		for(int i=0;i<m_RemoteWifiDevices.size();++i)
		{	
			WifiDevice device=m_RemoteWifiDevices.get(i);			
			String name=device.getWifiDeviceName();
			String ip_address=device.getWifiDeviceIPAddress();
		/* �������ӵ��б��� */
		// ����
		TextView nametv = new TextView(WifiClientActivity.this);
		// ��״̬
		TextView bondstatetv = new TextView(WifiClientActivity.this);
		// ��ӦͼƬ,�ֻ����ֻ�ͼƬ,�����ǵ���ͼƬ
		ImageView image = new ImageView(WifiClientActivity.this);
		// ��Ҫ��������Ԫ�ص�һ��relativelayout
		final RelativeLayout rltmp = new RelativeLayout(WifiClientActivity.this);

		// �ѵ��ظ����豸�������ѵ�����Ϊ�յģ�û��ʵ����;��Ҳ���ж���
		if (device.getWifiDeviceName() == null)
			return;
		count++;

		// ����ǲ����ֻ�
		boolean isphone = false;
		final int deviceid = count;
		isphone = isPhone(device);
		isphone =true;
		setDeviceImage(image, isphone);

		// �ж��Ƿ��Ѿ���Թ�
//		String bondstate = getDeviceBond(device, context);
		// ���ð�״̬����������
		bondstatetv.setText(ip_address);
		bondstatetv.setTextColor(getResources().getColor(bondstatetv_id));
		bondstatetv.setTextSize(TypedValue.COMPLEX_UNIT_SP,
				UnitSize.devicebondstatesize);
		bondstatetv.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// �����豸���ֵ�״̬����,Ϊ�˲�Ӱ������,�����ಿ�ֵ�����ȥ��
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

		// ����relativelayout�����Ժ͵���¼�
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

		// �������豸��layout�ӵ�list��
		rl.add(rltmp);

		// ���豸�����ּӵ������豸��layout��
		RelativeLayout.LayoutParams namelp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height
		);
		UnitSize.mysetMargins(WifiClientActivity.this, namelp,
				UnitSize.devicenamemargins);
		rltmp.addView(nametv, namelp);

		// ���豸��״̬�ӵ������豸��layout��
		RelativeLayout.LayoutParams statelp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		UnitSize.mysetMargins(WifiClientActivity.this, statelp,
				UnitSize.devicebondmargins);
		rltmp.addView(bondstatetv, statelp);

		// ���豸��ͼƬ�ӵ������豸��layout��
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

		// ���豸��layout�ӵ������豸�б�İ���
		DeviceLayout.addView(rltmp);
		}
		
	}
	
	/**
	 * ��ȡWiFi�豸
	 */
	public void getremotedevice()
	{
		Intent intent=new Intent();
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.GET_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	/**
	 * ֹͣ��ȡWiFi�豸
	 */
	public void stopgettingremotedevice()
	{
		Intent intent=new Intent();
		intent.setClass(WifiClientActivity.this, WifiClientService.class);
		intent.putExtra(Constants_Wifi.Key.WIFI_SERVICE_OPERATION, Constants_Wifi.STOP_GETTING_REMOTE_WIFI_DEVICES);
		startService(intent);
	}
	/**
	 * ��ʼ���չ㲥
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
	 * ֹͣ���չ㲥
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
	 * �����ļ�
	 * @param path �����ļ���·��
	 * @param ip Ŀ��IP��ַ
	 * @param type �ļ�����
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
 * ��������
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
	 * �����Ƿ�����ˢ���豸�б�
	 * @return
	 */
	public boolean getRefreshingState()
	{
		return isRefreshingDevice;
	}
	/**
	 * �����Ƿ�����ˢ���豸�б�
	 * @param state
	 */
	public void setRefreshingState(boolean state)
	{
		isRefreshingDevice=state;
	}
	/**
	 * ֹͣˢ���豸�б�
	 */
	public void stopRefreshing()
	{
		setRefreshingState(false);
	}
    /**
     * ��ʾ
     * @param toast
     */
    public void DisplayToast(String toast)
    {
    	Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }
    
    
    /**
     * ��ȷ������ť
     * @param view
     */
    public void onconfirmbtn(View view)
    {
    	// û��ѡ�κ��豸
    			if (chooseid == -1 || m_RemoteWifiDevices == null) {
    				Toast.makeText(this, this.getString(R.string.choosedevice),
    						Toast.LENGTH_LONG).show();
    			} else {
    				final WifiDevice devicedchoose = m_RemoteWifiDevices.get(chooseid);
    				/**
					 * �û�ѡ�������ֹͣ���չ㲥
					 */
					stopreceivebroadcast();
					
					myProgressDialog = ProgressDialog.show(WifiClientActivity.this,
							getString(R.string.dialog_title_wifi), getString(R.string.waiting_response_wifi), true);
					myProgressDialog.setCancelable(false);
					
					//��������
					sendRequest(devicedchoose.getWifiDeviceIPAddress(),devicedchoose.getWifiDeviceName());

    			}
    }
    @Override
    public void onBackPressed()
    {
    	if(isFirstClick)
    	{
    		Toast.makeText(WifiClientActivity.this, "�ٰ�һ�η���������", Toast.LENGTH_LONG).show();
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
     * �����ء���ť
     * @param view
     */
    public void oncancelbtn(View view)
    {
    	onBackPressed();
    }
    
    
	//����Զ���豸��ϢΪ���������豸������ʾͼƬ�����绰���ߵ��ԣ�
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
		// ��ʼ������ı��״��ұߵ�����
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		//���ҳ�棬��ʼ�������ֵ
		count = -1;
		chooseid = -1;
		DeviceLayout.removeAllViews();
		imageRader.setVisibility(View.VISIBLE);

		rl.clear();
		m_RemoteWifiDevices.clear();
//		aryListTask.clear();
	
		if (!((WifiManager) getSystemService(Context.WIFI_SERVICE)).isWifiEnabled())
			return;

		//�״ﶯ��Ч����ͨ���߳�ʵ��
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

	/************** ��ʾ�Ի��� ***************/
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
					.setMessage(getString(R.string.confirm_to_send_data_wifi) + m_RemoteWifiDevices.get(chooseid).getWifiDeviceName() + "��")
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
