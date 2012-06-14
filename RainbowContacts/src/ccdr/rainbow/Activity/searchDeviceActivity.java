package ccdr.rainbow.Activity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.UnitSize;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class searchDeviceActivity extends Activity {
	/**
	 * 连接pbap服务端所用的uuid
	 */
	protected final UUID link_PSE = UUID
			.fromString("0000112f-0000-1000-8000-00805f9b34fb");
	/**
	 * 包含linearlayout的container
	 */
	protected LinearLayout radartextcon = null;
	protected RelativeLayout relativelayout1 = null;
	protected RelativeLayout relativelayout2 = null;
	/**
	 * 雷达图片对象
	 */
	protected ImageView imageView = null;
	/**
	 * 雷达旁边的文字对象
	 */
	protected TextView radartext2 = null;
	protected TextView radartext1 = null;
	/**
	 * 雷达图片动画
	 */
	protected AnimationDrawable animationDrawable = null;
	/**
	 * 顶栏文字
	 */
	protected TextView uplayText = null;
	/**
	 * 设备列表版面
	 */
	protected LinearLayout DeviceList;
	/**
	 * 顶栏可按下部分
	 */
	protected LinearLayout beginsearchLayout;
	/**
	 * 每个元素里面包含了一个搜索到的设备实体
	 */
	protected List<RelativeLayout> rl = new LinkedList<RelativeLayout>();
	/**
	 * 记录搜索到的蓝牙设备信息
	 */
	protected List<BluetoothDevice> DevicesList = new LinkedList<BluetoothDevice>();
	
	/**
	 * 记录搜索到多少个设备
	 */
	protected int count = -1;
	/**
	 * 记录用户点选的设备
	 */
	protected int chooseid = -1;
	/**
	 * 用于提供各种蓝牙操作
	 */
	BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	/**
	 * 搜索完后右边的小字
	 */
	TextView tv = null;
	
	int search_text = R.string.searchedtips;
	/**
	 * 当搜索到设备的时候做出的反应
	 */
//	IntentFilter discoverystartFilter = null;

//	IntentFilter discoveryFilter = null;
	
	/**
	 * 是否已注册BraodcastReceiver
	 */
//	private boolean hasRegister=false;

	IntentFilter foundFilter = null;
	
	private boolean init = true;
	/*****************************************************************/
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2,radarback;
	private TextView up_text;
	private ImageButton search_button,search_button1;
	private int radartext1_id,bondstatetv_id,onlyoneuncheckedclick_id,onlyonecheckedclick_id;
	
	private class Constant
	{
		/**
		 * 蓝牙设备搜索完毕
		 */
		public static final int SEARCH_FINISH = 100;
		/**
		 * 发出消息Message让handler启动动画
		 */
		public static final int MOVING_FRAMES = 101;
	}
	
	/*******************************************************************/
	protected BroadcastReceiver foundReceiver = new BroadcastReceiver() {
		
		@Override
		public synchronized void onReceive(Context context, Intent intent) {
			System.out.println("open--broadcast");
			System.out.println(intent);
			if(intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
			{
				mHandler.obtainMessage(Constant.SEARCH_FINISH).sendToTarget();
				return;
			}
			/* 从intent中取得搜索结果数据 */
			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			System.out.println(device);
			/* 将结果添加到列表中 */
			// 名字
			TextView nametv = new TextView(context);
			// 绑定状态
			TextView bondstatetv = new TextView(context);
			// 对应图片,手机是手机图片,电脑是电脑图片
			ImageView image = new ImageView(context);
			// 将要包含以上元素的一个relativelayout
			final RelativeLayout rltmp = new RelativeLayout(context);

			// 搜到重复的设备则丢弃，搜到名字为空的，没有实际用途，也进行丢弃
			if (device.getName() == null)
				return;
			if (!ExistedinList(device))
				DevicesList.add(device);
			else
				return;
			count++;

			// 检测是不是手机
			boolean isphone = false;
			final int deviceid = count;
			isphone = isPhone(device);
			setDeviceImage(image, isphone);

			// 判断是否已经配对过
			String bondstate = getDeviceBond(device, context);
			// 设置绑定状态的文字属性
			bondstatetv.setText(bondstate);
			bondstatetv.setTextColor(getResources().getColor(bondstatetv_id));
			bondstatetv.setTextSize(TypedValue.COMPLEX_UNIT_SP,
					UnitSize.devicebondstatesize);
			bondstatetv.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			// 设置设备名字的状态属性,为了不影响美观,将多余部分的内容去掉
			String devicename = device.getName();
			if (devicename.length() > 13) {
				devicename = devicename.substring(0, 12) + "...";
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
			UnitSize.mysetMargins(searchDeviceActivity.this, namelp,
					UnitSize.devicenamemargins);
			rltmp.addView(nametv, namelp);

			// 将设备的状态加到单个设备的layout中
			RelativeLayout.LayoutParams statelp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, // width
					ViewGroup.LayoutParams.WRAP_CONTENT // height

			);
			UnitSize.mysetMargins(searchDeviceActivity.this, statelp,
					UnitSize.devicebondmargins);
			rltmp.addView(bondstatetv, statelp);

			// 将设备的图片加到单个设备的layout中
			RelativeLayout.LayoutParams imagelp = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, // width
					ViewGroup.LayoutParams.WRAP_CONTENT // height

			);
			if (isphone)
				UnitSize.mysetMargins(searchDeviceActivity.this, imagelp,
						UnitSize.devicemarginsmobile);
			else
				UnitSize.mysetMargins(searchDeviceActivity.this, imagelp,
						UnitSize.devicemarginslaptop);

			rltmp.addView(image, imagelp);

			// 将设备的layout加到整个设备列表的版面
			DeviceList.addView(rltmp);

		}
	};

//	protected BroadcastReceiver _discoverystartReceiver = new BroadcastReceiver() {
//		public synchronized void onReceive(Context context, Intent intent) {
//			Log.d("uiadjust", "what");
//		}
//	};

	/**
	 * 获得远端设备与本机的绑定状态
	 * 
	 * @param device
	 *            远端设备
	 * @param context
	 *            上下文
	 * @return
	 */
	@SuppressWarnings("static-access")
	protected String getDeviceBond(BluetoothDevice device, Context context) {
		String bondstate = null;
		if (device.BOND_BONDED == device.getBondState()) {
			bondstate = context.getString(R.string.bonded);
		} else if (device.BOND_NONE == device.getBondState()) {
			bondstate = context.getString(R.string.bondnone);
		}
		return bondstate;
	}

	//检测是否已有设备存在设备列表中
	protected boolean ExistedinList(BluetoothDevice device) {
		boolean existed = false;
		for (BluetoothDevice btmp : DevicesList) {
			if (btmp.getAddress().equals(device.getAddress()))
				existed = true;
		}
		return existed;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		System.out.println("oncreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchdevice);
//		radartextcon = (LinearLayout) findViewById(R.id.linearLayout9);
		relativelayout1 = (RelativeLayout)findViewById(R.id.relative1);
		relativelayout2 = (RelativeLayout)findViewById(R.id.relative2);
		if (m_BluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF){
		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivity(enabler);
		}
		tv = new TextView(searchDeviceActivity.this);
		Log.d("pagebt", "enablebt2");
		radartext2 = (TextView) findViewById(R.id.searchingdevicetext);
		radartext1 = new TextView(searchDeviceActivity.this);
		uplayText = (TextView) findViewById(R.id.uplaytext);
		imageView = (ImageView) findViewById(R.id.radarView);
		imageView.setBackgroundResource(R.drawable.radar_anim);
		animationDrawable = (AnimationDrawable) imageView.getBackground();
		DeviceList = (LinearLayout) findViewById(R.id.DevicesList);
		beginsearchLayout = (LinearLayout) findViewById(R.id.searchlinearLayout);
		beginsearchLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					onbeginsearch();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		radartext2.setText(getString(R.string.searchtips).toString());
		
		skin_linearLayout = (LinearLayout)findViewById(R.id.ltest);
		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		radarback = (LinearLayout)findViewById(R.id.radarback);
		search_button = (ImageButton)findViewById(R.id.search_button);
		search_button1 = (ImageButton)findViewById(R.id.search_button1);
		up_text = (TextView)findViewById(R.id.up_text);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		System.out.println(changeSkinUtil.up_view+"--magazineActivityTitle----"+R.drawable.uplay_new);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		radarback.setBackgroundResource(changeSkinUtil.radarback);
		search_button.setImageResource(changeSkinUtil.search_button);
		search_button1.setImageResource(changeSkinUtil.search_button1);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		uplayText.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		radartext2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		radartext1_id = changeSkinUtil.uplayText;
		bondstatetv_id = changeSkinUtil.bondstatetv_id;
		onlyonecheckedclick_id = changeSkinUtil.onlyonecheckedclick_id;
		onlyoneuncheckedclick_id = changeSkinUtil.onlyoneuncheckedclick_id;
		
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		Log.i("aaa", "onPause-searchDeviceActivity");
		////////////////////////
//		ensureUnregister();
//		m_BluetoothAdapter.cancelDiscovery();
//		unregisterReceiver(foundReceiver);
		
	 	///////////////////////
		// TODO Auto-generated method stub
		super.onPause();

		Log.d("pagebt", "cancel in2");
//		if (m_BluetoothAdapter.isDiscovering()) {
//			m_BluetoothAdapter.cancelDiscovery();
//		}
		m_BluetoothAdapter.cancelDiscovery();
		unregisterReceiver(foundReceiver);
	}

	
	
	@Override
	protected void onDestroy() {
		Log.i("aaa", "onDestroy-searchDeviceActivity");
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
	
//		ensureUnregister();
		
	}

	@SuppressWarnings("static-access")
	protected void onbeginsearch() throws InterruptedException {


		relativelayout2.removeView(tv);
		relativelayout1.removeView(imageView);
		relativelayout1.removeView(radartext1);
		relativelayout1.addView(imageView);
		radartext2.setGravity(Gravity.CENTER_VERTICAL);


Log.e("fff", "onbeginsearch");

		relativelayout2.removeView(radartext2);
		radartext2.setText(getString(R.string.searchingdevice).toString());
		radartext2.setGravity(Gravity.CENTER_VERTICAL);
		relativelayout2.addView(radartext2);
		

		// 开始搜索后改变雷达右边的文字
		

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "搜索蓝牙设备异常！！！"+e.getMessage());
		}
		

//		if(!hasRegister)
//		{	
			System.out.println("has");
		
//		}

//		hasRegister=true;
		///////////////////////////////////////////////

		
		
		//清空页面，初始化相关数值
		count = -1;
		chooseid = -1;
		DeviceList.removeAllViews();
		imageView.setVisibility(View.VISIBLE);

		rl.clear();
		DevicesList.clear();
	
		if (m_BluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF)
			return;

		//雷达动画效果，通过线程实现
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.println("filter3--"+foundFilter);
				imageView.setBackgroundResource(R.drawable.radar_anim);
				animationDrawable = (AnimationDrawable) imageView
						.getBackground();
				animationDrawable.start();
			}
		}, 50);

		// 开始搜索后让顶栏部分不可按下,颜色变透明
		uplayText.setTextColor(getResources().getColor(R.color.touming));
		beginsearchLayout.setClickable(false);
		Log.d("uiadjust", "search1");

		m_BluetoothAdapter.startDiscovery();
		Log.d("uiadjust", "search2");
		System.out.println("bc");
	}

	@Override
	protected void onResume() {

	

		Log.i("aaa", "onResume-searchDeviceActivity");

		// TODO Auto-generated method stub
	
		if (m_BluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF
				&& init == false) {
			Toast.makeText(getApplicationContext(), R.string.openbtcaution,
					Toast.LENGTH_SHORT).show();
			onBackPressed();
		}
		
		
		registerReceiver(foundReceiver, foundFilter);
				
		init = false;
		super.onResume();
		
		try {
			onbeginsearch();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "onbeginsearch异常"+e.getMessage());
		}
	}
 
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		System.out.println("onstart");
		super.onStart();
		foundFilter=new IntentFilter();
		foundFilter.addAction(BluetoothDevice.ACTION_FOUND);
		foundFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	}

	@Override
	protected void onStop() {

		

		Log.i("aaa", "onStop-searchDeviceActivity");
		///////////////////
//		ensureUnregister();
		///////////////////

		// TODO Auto-generated method stub
		super.onStop();
//		if (m_BluetoothAdapter.isDiscovering()) {
//			m_BluetoothAdapter.cancelDiscovery();
//
//		}
//		tv.setVisibility(View.INVISIBLE);
		imageView.setVisibility(View.VISIBLE);
		Log.d("bond", "cancel");
//		if(NewMain.onstopfinish)finish();
	}

	@Override
	public void onBackPressed(){	
Log.e("bbb", "onBackPressed");

//		if (m_BluetoothAdapter.isDiscovering()) {
//			System.out.println("closeble");
//			m_BluetoothAdapter.cancelDiscovery();
//		} 
		super.onBackPressed();
	}
	
	protected boolean isPhone(BluetoothDevice mDevice) {

		boolean isphone = false;
		isphone = mDevice.getBluetoothClass().hasService(
				BluetoothClass.Service.TELEPHONY);
		Log.d("bluetoothpage", "findsuccess" + "  " + mDevice.getName());
		return isphone;

	}

	
	protected final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case Constant.MOVING_FRAMES:
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				imageView.setBackgroundResource(R.drawable.radar_anim);
				animationDrawable = (AnimationDrawable) imageView
						.getBackground();
				animationDrawable.start();
				break;
			case Constant.SEARCH_FINISH:
//				if(hasRegister)
//				{
//				unregisterReceiver(foundReceiver);
//				hasRegister=false;
//				 foundFilter = null;
//				}
				
				//显示重新搜索按钮
				beginsearchLayout.setClickable(true);
				relativelayout1.removeView(imageView);
				relativelayout1.removeView(radartext1);
				radartext1.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
				
				radartext1.setTextColor(getResources().getColor(radartext1_id));
				
				radartext1.setText(getString(R.string.searchend).toString());
				radartext1.setGravity(Gravity.CENTER);
				relativelayout1.addView(radartext1);
//				radartext1.setTextColor(R.color.little);
//				radartext1.setGravity(Gravity.CENTER_VERTICAL);			
				relativelayout2.removeView(tv);
				relativelayout2.removeView(radartext2);
				System.out.println("2222");
				tv.setText(search_text);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tv.setGravity(Gravity.CENTER_VERTICAL);
				relativelayout2.addView(tv);
				

				if (animationDrawable != null)
					animationDrawable.stop();
				imageView.setBackgroundResource(R.drawable.radar3);
				uplayText.setTextColor(getResources().getColor(radartext1_id));
				break;
			}
		}
	};
	
//	public void ensureUnregister()
//	{
//	 	if(foundFilter != null){
//	 		if(hasRegister)
//	 		{
//	 		unregisterReceiver(foundReceiver);
//	 		foundFilter = null;hasRegister=false;
//	 		}
//	 	}
//	}
}
