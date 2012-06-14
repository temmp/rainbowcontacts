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
	 * ��ʾ��textview
	 */
	private TextView textview = null;
	/**
	 * ����������ʾ�������õ�container
	 */
	RelativeLayout progresscontainer = null;
	/**
	 * ���ӽ׶�ʹ��
	 */
	ProgressBar Cyclepb = null;
	/**
	 * ������Ϣ�׶�ʹ��
	 */
	ProgressBar pbbar = null;
	
	
//	boolean removebondlast = false;
	String remoteDeviceName = null;
	boolean notimeout = false;
	/**
	 * ��Ϊʹ���Զ����ѯ�ʰ󶨣���ʱ֮��ȡ����Ҫ��
	 */
	boolean timeout = false;
	/**
	 * ��ʶ�Ƿ��ڵ�ǰActivity
	 */
	private boolean isCurrentActivity=true;
	/**
	 * �Ƿ����������
	 */
	private boolean isBonding=false;
	/**
	 * �Ƿ����ڴ���
	 */
	private boolean IsInitializing=false;
	/**
	 * ���ô���״̬
	 * @param b
	 */
	private void setIsInitializing(boolean b){IsInitializing=b;}
	/**
	 * ��ȡ����״̬
	 * @return
	 */
	private boolean getIsInitializing(){return IsInitializing;}
	/**
	 * α����������
	 */
	private Thread m_ProgressThread=null;
	
	int timecheck = 40000;
	/**************************************************************/
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private int connecting_id;
	/******************************************************************/
	/**
	 * ���ڻ�ȡ�󶨵�Service����
	 */
//	private TransportServiceBinder m_Binder=null;
	/**
	 * Զ�������豸
	 */
	private BluetoothDevice m_BluetoothDevice=null;
	/**
	 * ���PBAP��Ե��߳�
	 */
	private Thread m_PBAPBondThread=null;
	
	/**
	 * Activity���õ�����Ϣ
	 * @author Administrator
	 *
	 */
	public class MSG
	{
		/**
		 * ��԰󶨳ɹ�
		 */
		public static final int BOND_REMOTEDEVICE_SUCCESS=500;
		/**
		 * ��԰�ʧ��
		 */
		public static final int BOND_REMOTEDEVICE_FAILED=501;
		/**
		 * ��PBAP����˳�ʼ���ɹ�
		 */
		public static final int INIT_PBAP_SERVICE_SUCCESS=502;
		/**
		 * ���豸���ӳɹ�
		 */
		public static final int CONNECT_REMOTE_DEVICE_SUCESS=503;
		/**
		 * ��ȡ�豸�绰���ɹ�
		 */
		public static final int GET_REMOTE_DEVICE_PHONEBOOK_SUCESS=504;
		/**
		 * �Ͽ�PBAP���ӳɹ�
		 */
		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=505;
		/**
		 * ��ʼ���������Ի���
		 */
		public static final int INIT_PROGRESS_DIALOG=506;
		/**
		 * ���½������Ի���
		 */
		public static final int UPDATE_PROGRESS_DIALOG=507;
		/**
		 * ���½�����
		 */
		public static final int UPDATE_PBAP_PROGRESS_BAR=508;
		/**
		 * ��ɽ���������
		 */
		public static final int COMPLETE_PBAP_PROGRESS_BAR=509;
		/**
		 * �����ļ��ɹ�
		 */
		public static final int SEND_FILE_SUCCESS=510;
		/**
		 * �����ļ��ɹ�
		 */
		public static final int RECEIVE_FILE_SUCCESS=511;
	}
	
	/**
	 * �Ի����ID
	 * @author Administrator
	 *
	 */
	public class DLG
	{
		/**
		 * ��ʼ��PBAPʧ�ܵĶԻ���ID
		 */
		public static final int INIT_PBAP_SERVICE_FAILED=601;
		/**
		 * ����PBAPʧ�ܵĶԻ���ID
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
			//��԰󶨳ɹ�
			case MSG.BOND_REMOTEDEVICE_SUCCESS:
		
				setIsInitializing(true);
				initPBAPService();
				break;
//			case MSG.UPDATE_PBAP_PROGRESS_BAR:
//				break;
			case MSG.BOND_REMOTEDEVICE_FAILED:
				/**
				 * �����Ի�����ʾ��ʧ�ܣ�
				 */
				break;
//			//��ʼ��PBAP����ɹ�
//			case Message.INIT_PBAP_SERVICE_SUCCESS:
//				Log.i("aaa", "��ʼ����PBAP����");
//				connectPBAPService();
//				break;
//			//����PBAP�ɹ�
//			case Message.CONNECT_REMOTE_DEVICE_SUCESS:
//				getPhonebookData();
//				break;
//			//��ȡ�绰�����ݳɹ�
//			case Message.GET_REMOTE_DEVICE_PHONEBOOK_SUCESS:
//				disconnectPBAPService();
//				break;
//			//�Ͽ����ӳɹ�
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
		
        //��ȡ��һActivity��������Զ�������豸����
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
		
    	//�趨���˽�����
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
    			//�жϱ�����Զ���豸��������
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
    			Log.i("aaa", "��PBAP����˰󶨳ɹ�");
    			//����ɹ�������Ϣ��Handler
    			m_Handler.obtainMessage(MSG.BOND_REMOTEDEVICE_SUCCESS).sendToTarget();
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
//		//���һ��������ʾ���Ƿ��жϴ��䲢����
//		abortGettingPhonebookData();
//		}
		super.onBackPressed();
	}
	
	/**
	 * ��ʼ��PBAP����
	 */
    public void initPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.BLUETOOTH_DEVICE, m_BluetoothDevice);
		startService(intent);
    }
	
	/**
	 * ����Զ�������豸��PBAP����
	 */
    public void connectPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.CONNECT_REMOTE_DEVICE);
		startService(intent);
    }
    
    /**
     * ��ȡ��ϵ������
     */
    public void getAllContactsCount()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.GET_REMOTE_DEVICE_CONTACTS_COUNT);
		startService(intent);
    }
    
    /**
     * ��ȡ�豸�绰��
     */
    public void getPhonebookData()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.GET_REMOTE_DEVICE_PHONEBOOK_DATA);
		intent.putExtra(Constants_Interaction.Key.Bluetooth.BluetoothPBAP.PBAP_RECEIVE_FILE_PATH, Constants_File.Path.PBAP_REV);
    	startService(intent);
    }
    
    /**
     * �ر�����
     */
    public void disconnectPBAPService()
    {
    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.DISCONNECT_REMOTE_DEVICE);
		startService(intent);
    }
    
    /**
     * �ж�PBAP�绰������
     */
//    public void abortGettingPhonebookData()
//    {
//    	Intent intent=new Intent(BluetoothPBAPActivity.this,BluetoothPBAPClientService.class);
//    	intent.putExtra(Constants_Interaction.Key.Bluetooth.Bluetooth_PBAP_SERVICE_OPERATION, Constants_Bluetooth.Option.ABORT_GETTING_PHONEBOOK_DATA);
//		startService(intent);
//    }
    
    //ע��㲥�����������ڽ���Service�˹㲥��Ϣ
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
				Log.i("aaa", "��ʼ��ʼ��PBAP����");
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
			 * ����PBAP����˳ɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_SUCCESS.equals(action))
			{			
				getPhonebookData();
			}
			/**
			 * ����PBAP�����ʧ��
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.CONNECT_REMOTE_DEVICE_PBAP_FAILED.equals(action))
			{
				setIsInitializing(true);
				showDialog(DLG.CONNECT_PBAP_SERVICE_FAILED);
			}
			/**
			 * ��ʼ��PBAP����ɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_SUCCESS.equals(action))
			{
				setIsInitializing(false);
				connectPBAPService();
			}
			/**
			 * ��ʼ��PBAP����ʧ��
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.INIT_PBAP_SERVICE_FAILED.equals(action))
			{
				setIsInitializing(true);
				showDialog(DLG.INIT_PBAP_SERVICE_FAILED);
			}
			/**
			 * ��ȡ��ϵ�������ɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT.equals(action))
			{
				//��ϵ������
//				int count=intent.getIntExtra(Constants_Bluetooth.Key.REMOTE_DEVICE_CONTACTS_COUNT, -1);
			}
			/**
			 * ��ȡ�绰���ɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA.equals(action))
			{
				//��ȡ��ϵ�����ݳɹ�����ʼ����
				disconnectPBAPService();
			}
			/**
			 * �Ͽ�PBAP���ӳɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.DISCONNECT_REMOTE_DEVICE_SUCCESS.equals(action))
			{
				startActivity(new Intent(BluetoothPBAPActivity.this,importContactsActivity.class)
				.putExtra("filepath", Constants_File.Path.PBAP_REV)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
			/**
			 * �ж�PBAP�绰������ɹ�
			 */
			else if(Constants_Interaction.Action.Bluetooth.BluetoothPBAP.ABORT_GETTING_PHONEBOOK_DATA_SUCCESS.equals(action))
			{
				Log.i("aaa", "�ж�PBAP�绰������ɹ�");
			}
		}

    };
    
    /************** ��ʾ�Ի��� ***************/
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
