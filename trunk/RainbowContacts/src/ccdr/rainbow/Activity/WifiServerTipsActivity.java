package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.VcardOperator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WifiServerTipsActivity extends Activity {
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private TextView sharesend_text,sharesend_text1,sharesend_text2,sharesend_text3;
	private ImageButton sharesend_BackButton,sharesend_NextButton;
	
	private VcardOperator myOperator;
	
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	
	private String username=null;
	
	private ProgressDialog m_OpenWifiProgress;
	private Builder m_TimeoutDialog=null;
	private Builder m_OpenWifiDialog=null;
	
	private Thread m_TimeoutThread=null;

	private Handler m_Handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				mWifiInfo=mWifiManager.getConnectionInfo();
				((TextView) findViewById(R.id.contactCountText)).setText(mWifiInfo.getSSID());
				((TextView) findViewById(R.id.lasttime)).setText(intToIp(mWifiInfo.getIpAddress()));
				break;
			case 1:
				m_TimeoutDialog=new AlertDialog.Builder(WifiServerTipsActivity.this){};
				m_TimeoutDialog.setTitle(R.string.alertdialog_title);
				m_TimeoutDialog.setMessage(R.string.network_connect_timeout_wifi);
				m_TimeoutDialog.setPositiveButton(R.string.ok, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onBackPressed();
					}
				});
				m_TimeoutDialog.setCancelable(false);
				m_TimeoutDialog.create();
				m_TimeoutDialog.show();
				break;
			}
		}
	};
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifireceive_tips_new);
		
		
		
		mWifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo=mWifiManager.getConnectionInfo();
		
		m_OpenWifiProgress=new ProgressDialog(WifiServerTipsActivity.this){};
		m_OpenWifiProgress.setTitle(R.string.open_title_wifi);
		m_OpenWifiProgress.setMessage(getString(R.string.open_message_wifi));
		
		username=Build.MODEL;
		
		myOperator = new VcardOperator(this);

		sharesend_text = (TextView)findViewById(R.id.sharesend_text_wifi_receive_tips_new);
		sharesend_text1 = (TextView)findViewById(R.id.sharesend_text1_wifi_receive_tips_new);
		sharesend_text2 = (TextView)findViewById(R.id.sharesend_text2_wifi_receive_tips_new);
//		sharesend_text3 = (TextView)findViewById(R.id.sharesend_text3_wifi_receive_tips_new);
		skin_linearLayout = (LinearLayout)findViewById(R.id.ltest_wifi_receive_tips_new);
		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1_wifi_receive_tips_new);
		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2_wifi_receive_tips_new);
		up_text = (TextView)findViewById(R.id.up_text_wifi_receive_tips_new);
		down_text = (TextView)findViewById(R.id.down_text_wifi_receive_tips_new);
		down_text1 = (TextView)findViewById(R.id.down_text1_wifi_receive_tips_new);
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
		sharesend_BackButton = (ImageButton)findViewById(R.id.sharesend_BackButton_wifi_receive_tips_new);
		sharesend_NextButton = (ImageButton)findViewById(R.id.sharesend_NextButton_wifi_receive_tips_new);
		sharesend_BackButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_BackButton);
		sharesend_NextButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_NextButton);
		sharesend_text.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		sharesend_text1.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		sharesend_text2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
//		sharesend_text3.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));

		 ((TextView) findViewById(R.id.contactCountText)).setText(mWifiInfo.getSSID());
		 ((TextView) findViewById(R.id.lasttime)).setText(intToIp(mWifiInfo.getIpAddress()));
		 ((TextView) findViewById(R.id.contactCountText)).setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		 ((TextView) findViewById(R.id.contactCountText_first)).setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		 ((TextView) findViewById(R.id.lasttime_first)).setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		 ((TextView) findViewById(R.id.lasttime)).setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		 
	}

	/************** 点击了“确定”按钮 ***************/
	public void onokbtnwifireceivenew(View view) {
		Intent i = new Intent(WifiServerTipsActivity.this,
				WifiServerActivity.class);
		i.putExtra("username", username);
		startActivity(i);
	}

	/************** 点击了“返回”按钮 ***************/
	public void onbackbtnwifireceivenew(View view) {
		onBackPressed();
	}
	
	@Override
	protected void onResume() {
			super.onResume();
			if(false==mWifiManager.isWifiEnabled())
			{
				m_OpenWifiProgress.show();
			}
			else if(mWifiManager.getConnectionInfo().getIpAddress()==0)
			{
				m_OpenWifiDialog=new AlertDialog.Builder(WifiServerTipsActivity.this){};
				m_OpenWifiDialog.setTitle(R.string.alertdialog_title);
				m_OpenWifiDialog.setMessage(R.string.ask_open_message_wifi);
				m_OpenWifiDialog.setPositiveButton(R.string.ok, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					}
				});
				m_OpenWifiDialog.setNegativeButton(R.string.back, new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						onBackPressed();
					}
				});
				m_OpenWifiDialog.setCancelable(false);
				m_OpenWifiDialog.create();
				m_OpenWifiDialog.show();
			}
			
			m_TimeoutThread=new Thread()
			{
				@Override
				public void run()
				{
					int MaxTimeout=12000;
					if(mWifiManager.isWifiEnabled()==false)
					{
						mWifiManager.setWifiEnabled(true);
					}
					while(!(mWifiManager.isWifiEnabled()==true && 
							mWifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED &&
							mWifiManager.getConnectionInfo().getSupplicantState()==SupplicantState.COMPLETED &&
							mWifiManager.getConnectionInfo().getIpAddress()!=0))
					{
						if(MaxTimeout>0)
						{
							try {
								Thread.sleep(200);
								MaxTimeout-=200;
							} catch (InterruptedException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
								return;
							}
						}
						else
						{
							m_Handler.sendEmptyMessage(1);
							break;
						}
					}
					m_OpenWifiProgress.dismiss();
					m_Handler.sendEmptyMessage(0);
				}
			};
			m_TimeoutThread.start();
			
			mWifiInfo = mWifiManager.getConnectionInfo();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		m_TimeoutThread.interrupt();
		m_TimeoutThread=null;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		m_OpenWifiDialog=null;
		m_TimeoutDialog=null;
	}
	
	private String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + ((i >> 24) & 0xFF);

	}
}
