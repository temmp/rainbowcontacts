package ccdr.rainbow.Activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Bluetooth.BluetoothServer;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Service.BluetoothServerService;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.UnitSize;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BluetoothServerActivity extends Activity{
	String Exceptionfile = null;
	PrintWriter p = null;
	BufferedWriter writer = null;
	String remotename = null;
	int timeout_mil = 50000; // 超时时间

	/**
	 * 判断是否第一次调用onresume
	 */
	private boolean init = true;
	/**
	 * 提醒客户用的text
	 */
	private TextView ReceiveRemind = null;
	/**
	 * 包含进度条的layout
	 */
	RelativeLayout progresscontainer = null;
	/**
	 * 等待对方连接圆形进度
	 */
	ProgressBar Cyclepb = null;
	/**
	 * 接受信息进度
	 */
	ProgressBar Horipb = null;

	/**
	 * 初始化进度条
	 */

	private boolean isCurrentActivity = true;

	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private ImageButton button;
	private TextView btservertext;
	
	
	
	/**
	 * 本地蓝牙设备
	 */
	private BluetoothAdapter m_BluetoothAdapter=null;
	/**
	 * 蓝牙服务端（接收方）操作类
	 */
	private BluetoothServer m_BluetoothServer=null;
	/**
	 * 检测服务端（接收方）接收文件的线程
	 */
//	private Thread m_ServerReceiveThread=null;
	
	public class MSG
	{
		/**
		 * 接收文件成功
		 */
		public static final int RECEIVE_FILE_SUCCESS=100;
	}
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
//			case Constants_Bluetooth.Message.INIT_PROGRESS_DIALOG:
//
//				break;
//			
//			case Constants_Bluetooth.Message.UPDATE_PROGRESS_DIALOG:
//
//				break;
//				
//			case Constants_Bluetooth.Message.RECEIVE_FILE_SUCCESS:
//				break;
				
				default:break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothserver);
		
		progresscontainer = (RelativeLayout) findViewById(R.id.progresscontainer);
		Horipb = (ProgressBar) findViewById(R.id.importbar);
		Cyclepb = (ProgressBar) findViewById(R.id.cyclebar);
		ReceiveRemind = (TextView) findViewById(R.id.btservertext);
		String ss = getString(R.string.building_server);
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
		ReceiveRemind.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		btservertext = (TextView)findViewById(R.id.btservertext);
		btservertext.setTextColor(this.getResources().getColor(changeSkinUtil.connecting_id));
		button = (ImageButton)findViewById(R.id.backbutton);
		button.setImageResource(changeSkinUtil.btserver_backbutton);
		
		if (Constants_Global.DEBUG) {
			Exceptionfile = Constants_File.Path.SDROOTPATH + File.separator
					+ "CSReceiveException@" + Build.MODEL + "@";

			try {
				writer = new BufferedWriter(new FileWriter(Exceptionfile));
				p = new PrintWriter(writer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				if (Constants_Global.DEBUG) {
					e1.printStackTrace(p);
					p.flush();
				} else
					e1.printStackTrace();
			}
		}

		progresscontainer.removeView(Horipb);
		ReceiveRemind.setText(ss);
		RelativeLayout.LayoutParams barlp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, // width
				ViewGroup.LayoutParams.WRAP_CONTENT // height

		);
		UnitSize.mysetMargins(BluetoothServerActivity.this, barlp, 135, 100, 0, 0);
		progresscontainer.removeView(Cyclepb);
		progresscontainer.addView(Cyclepb, barlp);

		Cyclepb.setVisibility(View.VISIBLE);
		isCurrentActivity = true;

		
		
		m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		
		
		IntentFilter filter=new IntentFilter();
    	
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothServer.INIT_BLUETOOTH_SERVER_PROGRESS_BAR);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothServer.UPDATE_BLUETOOTH_SERVER_PROGRESS_BAR);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothServer.RECEIVE_FILE_SUCESS);
    	registerReceiver(m_BluetoothReceiver,filter);
		
//		m_ServerReceiveThread=new Thread()
//    	{
//    		@Override
//    		public void run()
//    		{
//    			//接收文件
//    			receiveFileFromClient();
//    			//如果成功则发送消息给Handler
//    			m_Handler.obtainMessage(MSG.RECEIVE_FILE_SUCCESS).sendToTarget();
//    		}
//    	};
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		//创建线程接收文件
//		m_ServerReceiveThread.start();
		
		
		receiveFileFromClient();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if (m_BluetoothAdapter.getScanMode() 
				!= BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE ) {
		if(init==true)
		{
		init=false;
		Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		enabler.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
		startActivity(enabler);
		}
		else
		{
			onBackPressed();
		}
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		stopService(new Intent(BluetoothServerActivity.this,BluetoothServerService.class));
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		unregisterReceiver(m_BluetoothReceiver);
	}
	
	public void onmain(View view) {
		onBackPressed();
	}
	
	/**
	 * 从客户端（发送方）接收文件
	 */
	public void receiveFileFromClient()
	{
		Intent intent=new Intent(BluetoothServerActivity.this,BluetoothServerService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_CS_SERVICE_OPERATION, Constants_Bluetooth.Option.START_RECEIVING_FILE);
		startService(intent);
	}
	
	private BroadcastReceiver m_BluetoothReceiver=new BroadcastReceiver()
    {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			Log.i("aaa", action);
			if(Constants_Interaction.Action.Bluetooth.BluetoothServer.UPDATE_BLUETOOTH_SERVER_PROGRESS_BAR.equals(action))
			{
				int current_size=intent.getIntExtra(Constants_Interaction.Key.Bluetooth.BluetoothServer.TOTAL_DATA_LENGTH,0);
				//初始化进度条
				Horipb.setProgress(current_size);
			}
			else if(Constants_Interaction.Action.Bluetooth.BluetoothServer.INIT_BLUETOOTH_SERVER_PROGRESS_BAR.equals(action))
			{
				int total_size=intent.getIntExtra(Constants_Interaction.Key.Bluetooth.BluetoothServer.CURRENT_DATA_LENGTH, 0);
				//更新进度条
				Horipb.setMax(total_size);
			}
			else if(Constants_Interaction.Action.Bluetooth.BluetoothServer.RECEIVE_FILE_SUCESS.equals(action))
			{
				//发送成功后开始导入
				startActivity(new Intent(BluetoothServerActivity.this,importContactsActivity.class)
				.putExtra("filepath", Constants_File.Path.CS_REV)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		}

    };
}
