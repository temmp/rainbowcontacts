package ccdr.rainbow.Activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.ccdr.rainbowcontacts.R;
import com.ccdr.rainbowcontacts.RainbowContactsActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import ccdr.rainbow.Bluetooth.BluetoothClient;
import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Constants.Constants_Interaction;
import ccdr.rainbow.Service.BluetoothClientService;
import ccdr.rainbow.Tool.BluetoothHiddenMethod;
import ccdr.rainbow.Tool.BluetoothTools;
import ccdr.rainbow.Tool.BzeeLog;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.VcardOperator;

public class BluetoothClientActivity extends Activity{
	
	String Exceptionfile = null; 
	PrintWriter p =null;
	/**
	 * 超时时间
	 */
	int timecheck = 30000;
	
	/**
	 * 导出的时候用于提示用户的text
	 */
	private TextView exportcount, wait;
	/**
	 * 记录导出的联系人数量
	 */
	private int exportcontactcount = 0;
	/**
	 * 记录导出的联系人位置
	 */
	private int[] contactPos;
	/**
	 * 导出操作类
	 */
	private VcardOperator myOperator;
	/**
	 * 导出是否成功
	 */
	private boolean exportsuccess = false;
	/**
	 * 正在导出的数量
	 */
	private int exportingContactCount = 0;
	/**
	 * 返回按键
	 */
	private ImageButton returnbtn;
	/**
	 * 导出过程中记录个数
	 */
	private int tempNum = 0;
	/**
	 * 导出的进度
	 */
	ProgressBar bar = null;
	/**
	 * 包含着进度条的layout
	 */
	RelativeLayout progresscontainer = null;
	
	/**
	 * 发送过程的进度条
	 */
	ProgressBar Cyclepb = null;
	/**
	 * 发送过程中的状态提醒文字
	 */
	private TextView CStxt = null;
	Dialog dialog_message;
	
	////////////////Zhangqx Add/////////////////////
	/**
	 * 导出电话簿数据的线程
	 */
	private Thread m_ExportContactsToServerThread=null;
	/**
	 * 根据当前导出的联系人数量更新进度条的线程
	 */ 
	private Thread m_RefreshExportProgressBarThread=null;
	/**
	 * 导出电话簿数据到远程设备后的处理
	 */	
	/**
	 * 标识是否在当前Activity
	 */
	private boolean isCurrentActivity;
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private int connecting_id,uplayText;
	
	private Context m_Context=this;
	/**
	 * 标识是否按下一次返回键
	 */
//	private boolean hasKeyBackDown=false;
	/**
	 * 标识是否要更新进度条
	 */
//	private boolean isRefreshing=true;

	/**
	 * 在发生连接成功、出现异常之后不需要再计算超时
	 */
	private boolean notimeout = false;
	
	public class MSG
	{
		/**
		 * 与远程设备绑定配对成功
		 */
		public static final int BOND_REMOTE_DEVICE_SUCCESS=10;
		/**
		 * 与远程设备绑定配对失败
		 */
		public static final int BOND_REMOTE_DEVICE_FAILED=11;
		/**
		 * 与远程设备进行蓝牙连接成功
		 */
		public static final int CONNECT_REMOTE_DEVICE_SUCESS=12;
		/**
		 * 与远程设备断开蓝牙连接成功
		 */
		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=13;
		
		public class Export
		{
		/**
		 * 导出联系人成功
		 */
		public static final int EXPORT_CONTACTS_SUCCESS=100;
		/**
		 *  导出联系人中断
		 */
		public static final int EXPORT_CONTACTS_ABORT=101;
		/**
		 * 导出联系人失败
		 */
		public static final int EXPORT_CONTACTS_FAILED=102;
		/**
		 * 更新进度条
		 */
		public static final int REFRESH=103;
		/**
		 * 连接成功
		 */
		public static final int MESSAGE_CONNECTED=104;
		/**
		 * 发送成功
		 */
		public static final int MESSAGE_SENDED=105;
		/**
		 * 连接超时
		 */
		public static final int CONNECT_TIMEOUT=106;
		/**
		 * 发送失败
		 */
		public static final int SDFDIALOG=107;
		}				
	}
	
	/**
	 * 对话框的ID
	 * @author Administrator
	 *
	 */
	public class DLG
	{
		/**
		 * 发送文件成功的对话框ID
		 */
		public static final int SEND_FILE_SUCCESS=200;
		/**
		 * 发送文件失败的对话框ID
		 */
		public static final int SEND_FILE_FAILED=201;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 蓝牙客户端（发送方）操作类
	 */
	private BluetoothClient m_BluetoothClient=null;
	/**
	 * 远程蓝牙设备
	 */
	private BluetoothDevice m_BluetoothDevice=null;
	/**
	 * 检测配对的线程
	 */
	private Thread m_ClientBondThread=null;
	
	Handler m_ExportedHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case  MSG.Export.EXPORT_CONTACTS_SUCCESS:
				BzeeLog.mark("exportsuccess");
				exportsuccess = true;
				returnbtn.setVisibility(View.VISIBLE);
				bar.setVisibility(View.INVISIBLE);
				exportcount.setVisibility(View.INVISIBLE);

				//如果导出成功则发送数据
				BluetoothHiddenMethod.CreateBond(m_BluetoothDevice);
				m_ClientBondThread.start();
				Log.d("pagebt", "-2");
				break;
				
			case MSG.Export.EXPORT_CONTACTS_ABORT:
				Toast.makeText(getBaseContext(), 
					R.string.export_contacts_abort_caused, Toast.LENGTH_LONG).show();break;
				
			case MSG.Export.EXPORT_CONTACTS_FAILED:Toast.makeText(getBaseContext(), 
					R.string.export_contacts_failed_caused, Toast.LENGTH_LONG).show();break;
			default:break;
			}
		}
	};
	
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG.Export.REFRESH:
				exportcount.setText( exportingContactCount + "/"
						+ exportcontactcount);
				bar.setProgress(exportingContactCount);
				break;
			case MSG.Export.MESSAGE_CONNECTED:
				notimeout = true;
				wait.setVisibility(View.INVISIBLE);
				Cyclepb.setVisibility(View.VISIBLE);

				progresscontainer.removeView(exportcount);

				RelativeLayout.LayoutParams txtlp = new RelativeLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT, // width
						ViewGroup.LayoutParams.WRAP_CONTENT // height
				);
				txtlp.setMargins(0, 100, 0, 0);
				CStxt.setTextColor(getResources().getColor(connecting_id));
				CStxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
				CStxt.setText(getString(R.string.sending));
				CStxt.setGravity(Gravity.CENTER);
				CStxt.setVisibility(View.VISIBLE);

				progresscontainer.addView(CStxt, txtlp);
				break;
			case MSG.Export.MESSAGE_SENDED:
				notimeout = true;
				Cyclepb.setVisibility(View.INVISIBLE);
				CStxt.setText(getString(R.string.exported_searchdevices2)
						+ "\n" + m_BluetoothDevice.getName());
				CStxt.setGravity(Gravity.CENTER);
				CStxt.setTextColor(getResources().getColor(uplayText));
				CStxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
				break;
			case MSG.Export.CONNECT_TIMEOUT:
					AlertDialog dlg4 = new AlertDialog.Builder(BluetoothClientActivity.this)
					.setTitle(getString(R.string.TOtitle)).setMessage(
							getString(R.string.TOContent))
					.setPositiveButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent intent=new Intent();
									intent.setClass(BluetoothClientActivity.this, CSActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
									dialog.dismiss();
								}
							}).create();
			dlg4.show();


				break;
			case MSG.Export.SDFDIALOG:
				notimeout = true;
				
				AlertDialog dlg1 = new AlertDialog.Builder(BluetoothClientActivity.this)

						.setTitle(getString(R.string.SDFtitle)).setMessage(
								getString(R.string.SDFContent))
						.setPositiveButton(getString(R.string.ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent=new Intent();
										intent.setClass(BluetoothClientActivity.this, RainbowContactsActivity.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
										dialog.dismiss();
									}
								}).create();
				dlg1.show();
				break;
				
			}
			super.handleMessage(msg);
		}
	};
	
	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			//配对绑定成功
			case MSG.BOND_REMOTE_DEVICE_SUCCESS:
//				connectServerService();
				sendFileToServer();
				break;
			//连接成功
			case MSG.CONNECT_REMOTE_DEVICE_SUCESS:
//				sendFileToServer();
//				disconnectServerService();
				break;
			//断开连接成功
			case MSG.DISCONNECT_REMOTE_DEVICE_SUCCESS:
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothclient);
		CStxt = new TextView(this);
		progresscontainer = (RelativeLayout) findViewById(R.id.Sendprogress);
		exportcount = (TextView) findViewById(R.id.exportingstatus);
		wait = (TextView) findViewById(R.id.exportingstatus1);
		bar = (ProgressBar) findViewById(R.id.bar);
		returnbtn = (ImageButton) findViewById(R.id.returnbtn);
		Intent i = getIntent();
		exportcontactcount = i.getIntExtra("exportcontactcount", 0);
		contactPos = i.getIntArrayExtra("contactPos");
		m_BluetoothDevice = (BluetoothDevice) i.getParcelableExtra("device");
		bar.setMax(exportcontactcount);
		exportcount.setText("0/" + exportcontactcount);
		myOperator = new VcardOperator(this);
		Cyclepb = (ProgressBar) findViewById(R.id.cyclebar);
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
		exportcount.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		wait.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		returnbtn.setImageResource(changeSkinUtil.returnbtn);
		uplayText = changeSkinUtil.uplayText;
		connecting_id = changeSkinUtil.connecting_id;
		
		
		IntentFilter filter=new IntentFilter();
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothClient.CONNECT_TO_SERVER_SUCESS);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_FAILED);
    	filter.addAction(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_SUCESS);
    	
    	registerReceiver(m_BluetoothReceiver,filter);
    	
		////////////////Zhangqx Add/////////////////////
		isCurrentActivity=true;
		if(Constants_Global.DEBUG){
			Exceptionfile = Constants_File.Path.SDROOTPATH + File.separator + "CSSendException@" + Build.MODEL+"@"+m_BluetoothDevice.getName()+" "+
			BluetoothTools.convertLongToRFC2445DateTime(System.currentTimeMillis())+".txt";
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(Exceptionfile));
				p = new PrintWriter(writer);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				if(Constants_Global.DEBUG){e1.printStackTrace(p); p.flush();}else e1.printStackTrace();
			}
		}
		//导出电话簿数据的线程
		m_ExportContactsToServerThread=new Thread()
		{
			@Override
			public void run()
			{
				try {
					//导出联系人到SD卡中，以Vcard形式保存
					if(myOperator.exportContactOneByOneAsVcardFileToSD(
							Constants_File.Path.CS_SEND, contactPos))
					{//发送导出成功消息
					m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_SUCCESS).sendToTarget();}
					else
					{m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_ABORT).sendToTarget();}
				}
				catch(InterruptedException e)
					//如果中断则发送失败消息
				{	m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_FAILED).sendToTarget();return;}
				catch (Exception e) {
					// TODO Auto-generated catch block
					if(Constants_Global.DEBUG){e.printStackTrace(p); p.flush();}else e.printStackTrace();
				}
			}
		};
		m_ExportContactsToServerThread.start();

		//用线程不断获取导出联系人数量并发送更新UI的消息以模拟进度条
		m_RefreshExportProgressBarThread=new Thread()
		{
			@Override
			public void run()
			{
					while (true) 
					{
						if (!exportsuccess) 
						{
							//获取导出联系人数量
						tempNum = myOperator.getExportingContactCount();
							if (exportingContactCount != tempNum)
							{
								exportingContactCount = tempNum;
								myHandler.obtainMessage(MSG.Export.REFRESH).sendToTarget();
							}
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								if(Constants_Global.DEBUG){e.printStackTrace(p); p.flush();}else e.printStackTrace();
							}
						}
						else {return;}
					}
			}
		};
		m_RefreshExportProgressBarThread.start();
		////////////////Zhangqx Add/////////////////////
		
    	m_ClientBondThread=new Thread()
    	{
    		@Override
    		public void run()
    		{
    			//判断本机与远程设备的配对情况
    			while(m_BluetoothDevice.getBondState()!=BluetoothDevice.BOND_BONDED)
    			{
    				Log.e("bondstate", String.valueOf(m_BluetoothDevice.getBondState()));
    				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
		    			m_Handler.obtainMessage(MSG.BOND_REMOTE_DEVICE_FAILED).sendToTarget();
						e.printStackTrace();
					}
    			}
    			//如果成功则发送消息给Handler
    			m_Handler.obtainMessage(MSG.BOND_REMOTE_DEVICE_SUCCESS).sendToTarget();
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
//		m_ClientBondThread.start();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	@Override
	public void onStop()
	{
		unregisterReceiver(m_BluetoothReceiver);
		
		if(p!=null)p.close();
		super.onStop();
		stopService(new Intent(BluetoothClientActivity.this,BluetoothClientService.class));
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	/**
	 * 与服务端（接收方）连接
	 */
	public void connectServerService()
	{
		
	}
	
	//“返回”按钮
	public void onexportedbtn(View view) {
		if (exportsuccess) {
			Intent intent=new Intent();
			intent.setClass(BluetoothClientActivity.this, RainbowContactsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(exportsuccess)
			{
				Intent intent=new Intent();
				intent.setClass(BluetoothClientActivity.this, RainbowContactsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			//设置导出标识以暂停导出
			myOperator.setIsExporting(false);				
			dialog_message =	new AlertDialog.Builder(BluetoothClientActivity.this).setCancelable(false)
				.setTitle(R.string.alertdialog_title)//设置标题
				.setMessage(R.string.ask_return_and_stop_exporting)//设置内容
				.setPositiveButton(R.string.yes,//设置“是”按钮
				new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int whichButton)
						{
							//设置停止导出操作
							myOperator.setIsStopExporting(true);
							//将线程释放
							m_ExportContactsToServerThread=null;
							m_RefreshExportProgressBarThread=null;
							isCurrentActivity=false;
							((Activity) m_Context).onBackPressed();
							dialog.dismiss();
						}
					})
					.setNeutralButton(R.string.no, 
				new DialogInterface.OnClickListener() 
					{
					@Override
					public void onClick(DialogInterface dialog, int whichButton)
					{
						myOperator.setIsStopExporting(false);myOperator.setIsExporting(true);
						//点击"否"按钮之后退出Dialog，并继续导出。
						dialog.dismiss();
					}
				}).create();
			dialog_message.show();
			return false;
		}
			return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 发送文件给服务端（接收方）
	 */
	public void sendFileToServer()
	{
		Intent intent=new Intent(BluetoothClientActivity.this,BluetoothClientService.class);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE, m_BluetoothDevice);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_CS_SERVICE_OPERATION, Constants_Bluetooth.Option.START_SENDING_FILE);
		startService(intent);
	}
	
	private BroadcastReceiver m_BluetoothReceiver=new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		String action=intent.getAction();
		Log.i("aaa", action);
		if(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_FAILED.equals(action))
		{
			String failed_reason=intent.getStringExtra(Constants_Interaction.Key.Bluetooth.BluetoothClient.SEND_FILE_FAILED_REASON);
			showDialog(DLG.SEND_FILE_FAILED);
		}
		else if(Constants_Interaction.Action.Bluetooth.BluetoothClient.SEND_FILE_SUCESS.equals(action))
		{
//			showDialog(DLG.SEND_FILE_SUCESS);
			myHandler.obtainMessage(MSG.Export.MESSAGE_SENDED).sendToTarget();
		}
		else if(Constants_Interaction.Action.Bluetooth.BluetoothClient.CONNECT_TO_SERVER_SUCESS.equals(action))
		{
			myHandler.obtainMessage(MSG.Export.MESSAGE_CONNECTED).sendToTarget();
		}
		}
		
	};
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
		case DLG.SEND_FILE_FAILED:
			return new AlertDialog.Builder(BluetoothClientActivity.this).setTitle(R.string.alertdialog_title)
					.setMessage(getString(R.string.send_file_failed))
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							onBackPressed();
						}
					})
					.setCancelable(false)
					.create();
			default:return null;
		}
	}
}
