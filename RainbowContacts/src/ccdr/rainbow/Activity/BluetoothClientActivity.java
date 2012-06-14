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
	 * ��ʱʱ��
	 */
	int timecheck = 30000;
	
	/**
	 * ������ʱ��������ʾ�û���text
	 */
	private TextView exportcount, wait;
	/**
	 * ��¼��������ϵ������
	 */
	private int exportcontactcount = 0;
	/**
	 * ��¼��������ϵ��λ��
	 */
	private int[] contactPos;
	/**
	 * ����������
	 */
	private VcardOperator myOperator;
	/**
	 * �����Ƿ�ɹ�
	 */
	private boolean exportsuccess = false;
	/**
	 * ���ڵ���������
	 */
	private int exportingContactCount = 0;
	/**
	 * ���ذ���
	 */
	private ImageButton returnbtn;
	/**
	 * ���������м�¼����
	 */
	private int tempNum = 0;
	/**
	 * �����Ľ���
	 */
	ProgressBar bar = null;
	/**
	 * �����Ž�������layout
	 */
	RelativeLayout progresscontainer = null;
	
	/**
	 * ���͹��̵Ľ�����
	 */
	ProgressBar Cyclepb = null;
	/**
	 * ���͹����е�״̬��������
	 */
	private TextView CStxt = null;
	Dialog dialog_message;
	
	////////////////Zhangqx Add/////////////////////
	/**
	 * �����绰�����ݵ��߳�
	 */
	private Thread m_ExportContactsToServerThread=null;
	/**
	 * ���ݵ�ǰ��������ϵ���������½��������߳�
	 */ 
	private Thread m_RefreshExportProgressBarThread=null;
	/**
	 * �����绰�����ݵ�Զ���豸��Ĵ���
	 */	
	/**
	 * ��ʶ�Ƿ��ڵ�ǰActivity
	 */
	private boolean isCurrentActivity;
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private int connecting_id,uplayText;
	
	private Context m_Context=this;
	/**
	 * ��ʶ�Ƿ���һ�η��ؼ�
	 */
//	private boolean hasKeyBackDown=false;
	/**
	 * ��ʶ�Ƿ�Ҫ���½�����
	 */
//	private boolean isRefreshing=true;

	/**
	 * �ڷ������ӳɹ��������쳣֮����Ҫ�ټ��㳬ʱ
	 */
	private boolean notimeout = false;
	
	public class MSG
	{
		/**
		 * ��Զ���豸����Գɹ�
		 */
		public static final int BOND_REMOTE_DEVICE_SUCCESS=10;
		/**
		 * ��Զ���豸�����ʧ��
		 */
		public static final int BOND_REMOTE_DEVICE_FAILED=11;
		/**
		 * ��Զ���豸�����������ӳɹ�
		 */
		public static final int CONNECT_REMOTE_DEVICE_SUCESS=12;
		/**
		 * ��Զ���豸�Ͽ��������ӳɹ�
		 */
		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=13;
		
		public class Export
		{
		/**
		 * ������ϵ�˳ɹ�
		 */
		public static final int EXPORT_CONTACTS_SUCCESS=100;
		/**
		 *  ������ϵ���ж�
		 */
		public static final int EXPORT_CONTACTS_ABORT=101;
		/**
		 * ������ϵ��ʧ��
		 */
		public static final int EXPORT_CONTACTS_FAILED=102;
		/**
		 * ���½�����
		 */
		public static final int REFRESH=103;
		/**
		 * ���ӳɹ�
		 */
		public static final int MESSAGE_CONNECTED=104;
		/**
		 * ���ͳɹ�
		 */
		public static final int MESSAGE_SENDED=105;
		/**
		 * ���ӳ�ʱ
		 */
		public static final int CONNECT_TIMEOUT=106;
		/**
		 * ����ʧ��
		 */
		public static final int SDFDIALOG=107;
		}				
	}
	
	/**
	 * �Ի����ID
	 * @author Administrator
	 *
	 */
	public class DLG
	{
		/**
		 * �����ļ��ɹ��ĶԻ���ID
		 */
		public static final int SEND_FILE_SUCCESS=200;
		/**
		 * �����ļ�ʧ�ܵĶԻ���ID
		 */
		public static final int SEND_FILE_FAILED=201;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * �����ͻ��ˣ����ͷ���������
	 */
	private BluetoothClient m_BluetoothClient=null;
	/**
	 * Զ�������豸
	 */
	private BluetoothDevice m_BluetoothDevice=null;
	/**
	 * �����Ե��߳�
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

				//��������ɹ���������
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
			//��԰󶨳ɹ�
			case MSG.BOND_REMOTE_DEVICE_SUCCESS:
//				connectServerService();
				sendFileToServer();
				break;
			//���ӳɹ�
			case MSG.CONNECT_REMOTE_DEVICE_SUCESS:
//				sendFileToServer();
//				disconnectServerService();
				break;
			//�Ͽ����ӳɹ�
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
		//�����绰�����ݵ��߳�
		m_ExportContactsToServerThread=new Thread()
		{
			@Override
			public void run()
			{
				try {
					//������ϵ�˵�SD���У���Vcard��ʽ����
					if(myOperator.exportContactOneByOneAsVcardFileToSD(
							Constants_File.Path.CS_SEND, contactPos))
					{//���͵����ɹ���Ϣ
					m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_SUCCESS).sendToTarget();}
					else
					{m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_ABORT).sendToTarget();}
				}
				catch(InterruptedException e)
					//����ж�����ʧ����Ϣ
				{	m_ExportedHandler.obtainMessage(MSG.Export.EXPORT_CONTACTS_FAILED).sendToTarget();return;}
				catch (Exception e) {
					// TODO Auto-generated catch block
					if(Constants_Global.DEBUG){e.printStackTrace(p); p.flush();}else e.printStackTrace();
				}
			}
		};
		m_ExportContactsToServerThread.start();

		//���̲߳��ϻ�ȡ������ϵ�����������͸���UI����Ϣ��ģ�������
		m_RefreshExportProgressBarThread=new Thread()
		{
			@Override
			public void run()
			{
					while (true) 
					{
						if (!exportsuccess) 
						{
							//��ȡ������ϵ������
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
    			//�жϱ�����Զ���豸��������
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
    			//����ɹ�������Ϣ��Handler
    			m_Handler.obtainMessage(MSG.BOND_REMOTE_DEVICE_SUCCESS).sendToTarget();
    		}
    	};
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		//���
//		BluetoothHiddenMethod.CreateBond(m_BluetoothDevice);
		//�����̼߳��������
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
	 * �����ˣ����շ�������
	 */
	public void connectServerService()
	{
		
	}
	
	//�����ء���ť
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
			//���õ�����ʶ����ͣ����
			myOperator.setIsExporting(false);				
			dialog_message =	new AlertDialog.Builder(BluetoothClientActivity.this).setCancelable(false)
				.setTitle(R.string.alertdialog_title)//���ñ���
				.setMessage(R.string.ask_return_and_stop_exporting)//��������
				.setPositiveButton(R.string.yes,//���á��ǡ���ť
				new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int whichButton)
						{
							//����ֹͣ��������
							myOperator.setIsStopExporting(true);
							//���߳��ͷ�
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
						//���"��"��ť֮���˳�Dialog��������������
						dialog.dismiss();
					}
				}).create();
			dialog_message.show();
			return false;
		}
			return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * �����ļ�������ˣ����շ���
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
