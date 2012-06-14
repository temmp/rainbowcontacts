package ccdr.rainbow.Activity;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.ccdr.rainbowcontacts.R;
import com.ccdr.rainbowcontacts.RainbowContactsActivity;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Tool.BzeeLog;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.VcardOperator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class importContactsActivity extends Activity {

	private TextView textview, textview2,textview3;
	private int importcontactcount = 0;
	private String importingContactName = " ";
	private VcardOperator myOperator;
	private boolean importsuccess = false;
	private int importingContactCount = 0;
	private ImageButton returnbtn;
	private int tempNum = 0;
	private String tempName = "";
	ProgressBar bar = null;
	private File f = null;
	private String path = null;
	private Set<String> author_name = new HashSet<String>();
	////////////////Zhangqx Add/////////////////////
	private final int IMPORT_CONTACTS_BY_FETCHING_SUCCESS=100;			//导入联系人成功消息
	private final int IMPORT_CONTACTS_BY_FETCHING_ABORT=102;			//导入联系人中断消息
	Dialog dd = null;
	private Thread m_ImportContactsByFetchingThread=null;			//导入PBAP所获取的联系人到本地的线程
	private Thread m_RefreshImportByFetchingProgressBarThread=null; //导入电话簿时更新进度条的线程
	
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	 
	private Context m_Context=this;
	
	private class Constant
	{
		public final static int REFRESH = 100;
	}
	
	Handler m_ImportedHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			
			switch(msg.what)
			{
			case IMPORT_CONTACTS_BY_FETCHING_SUCCESS:
				BzeeLog.mark("importsuccess");
				importsuccess = true;
				returnbtn.setVisibility(View.VISIBLE);
				bar.setVisibility(View.INVISIBLE);
				textview.setVisibility(View.INVISIBLE);
				textview.setText("");
				textview3.setVisibility(View.INVISIBLE);
				textview.setText("");
				textview2.setText(getString(R.string.imported1)
						+ importcontactcount + getString(R.string.imported2));
				
				if(author_name!=null&&author_name.size()>0){
					Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
			        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启    
			        vibrator.vibrate(pattern,2); 
					double r = Math.random();
					if(r<0.5){
					Builder b = new AlertDialog.Builder(importContactsActivity.this);
					b.setIcon(android.R.drawable.ic_menu_myplaces);
					b.setTitle(R.string.painted_eggshell);
					b.setMessage(getString(R.string.painted_eggshell_message)+author_name.toString().substring(1, author_name.toString().length()-1)+getString(R.string.painted_eggshell_message1));
					b.setPositiveButton(R.string.painted_eggshell_ok, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dd.dismiss();
							
							dd = null;
							
						}
					});
					 dd = b.create();
					

					dd.show();
					}else{
					 Toast.makeText(getApplicationContext(), getString(R.string.painted_eggshell_message)+author_name.toString().substring(1, author_name.toString().length()-1)+getString(R.string.painted_eggshell_message1),
						     Toast.LENGTH_LONG).show();
					}
					try {
						Thread.sleep(1000);
						vibrator.cancel();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//				textview4.setText(path);
//				textview5.setVisibility(View.VISIBLE);
				break;
			case IMPORT_CONTACTS_BY_FETCHING_ABORT:
				importsuccess = true;
				returnbtn.setVisibility(View.VISIBLE);
				bar.setVisibility(View.INVISIBLE);
				textview.setVisibility(View.INVISIBLE);
				textview.setText("");
				textview3.setVisibility(View.INVISIBLE);
				textview.setText("");
				textview2.setGravity(Gravity.CENTER_HORIZONTAL);
				textview2.setText(getString(R.string.stop_import)
				//getString(R.string.imported1) + importcontactcount + getString(R.string.imported2)
				
						);
				break;
			}
			super.handleMessage(msg);
		}
	};
	////////////////Zhangqx Add/////////////////////
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.REFRESH:
				textview.setText(importingContactName
						+ getString(R.string.pleasewait3));
				textview3.setText(importingContactCount + "/" + importcontactcount);
				bar.setProgress(importingContactCount);
				
				// BzeeLog.mark("来信息");
				break;
			
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.importcontacts);
		Intent fpin = getIntent();
		 
		path = fpin.getStringExtra("filepath");
		textview = (TextView) findViewById(R.id.importingstatus);
		textview2 = (TextView) findViewById(R.id.importingstatus1);
		textview3 =(TextView) findViewById(R.id.importingstatus2);
//		textview4 = (TextView)findViewById(R.id.number_text);
		bar = (ProgressBar) findViewById(R.id.bar2);
		returnbtn = (ImageButton) findViewById(R.id.returnbtn2);
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
		textview.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		textview2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		textview3.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		returnbtn.setImageResource(changeSkinUtil.returnbtn);
		ChangeSkinUtil.able= getResources().getConfiguration().locale.getCountry();
//		textview5 = (TextView)findViewById(R.id.path_message);
		f = new File(path);
		
		myOperator = new VcardOperator(this);

		try {
			importcontactcount = myOperator.getVcardContactsCount(f);
			// 计算导入的联系人数量
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		bar.setMax(importcontactcount);
		textview3.setText("0/" + importcontactcount);

		m_ImportContactsByFetchingThread=new Thread()
		{
			@Override
			public void run()
			{
				try {
					if(myOperator.importVcardByMyself(importContactsActivity.this,f))
					{m_ImportedHandler.obtainMessage(IMPORT_CONTACTS_BY_FETCHING_SUCCESS).sendToTarget();}
					else
					{m_ImportedHandler.obtainMessage(IMPORT_CONTACTS_BY_FETCHING_ABORT).sendToTarget();}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		m_ImportContactsByFetchingThread.start();
		
		m_RefreshImportByFetchingProgressBarThread=new Thread()
		{
			@Override
			public void run()
			{
				while (true) {
					if (!importsuccess) {
						tempNum = myOperator.getImportingContactsCount();
						if (importingContactCount != tempNum) {
							importingContactCount = tempNum;
							Message message = new Message();
							message.what = Constant.REFRESH;
							myHandler.sendMessage(message);
						}

						tempName = myOperator.getImportingContactsFn();
						if (!importingContactName.equals(tempName)) {
							importingContactName = tempName;
							for(String author:Constants_Global.AUTHORS){
								
								if(author.trim().equals(importingContactName.trim().replace(" ", ""))){
								System.out.println(author+"-----hello--------------------"+importingContactName);
								author_name.add(importingContactName);}
							}
							Message message = new Message();
							message.what = Constant.REFRESH;
							myHandler.sendMessage(message);
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						return;
					}
				}
			}
		};
		m_RefreshImportByFetchingProgressBarThread.start();
//		AsyncTask();// 创建并调用异步任务函数
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(dd!=null){
			dd.dismiss();
		}
	
		super.onPause();
	}


	/************** 点击“返回” ***************/
	public void onimportedbtn(View view) {
		if (importsuccess) {
			Intent intent=new Intent();
			intent.setClass(importContactsActivity.this, RainbowContactsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			onBackPressed();
		}
	}
//
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (importsuccess) {
			Intent intent=new Intent();
			intent.setClass(importContactsActivity.this, RainbowContactsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			super.onBackPressed();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(importsuccess)
			{
				Intent intent=new Intent();
				intent.setClass(importContactsActivity.this, RainbowContactsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			//设置导出标识以暂停导出
			myOperator.setIsImporting(false);				
			new AlertDialog.Builder(importContactsActivity.this).setCancelable(false)

				.setTitle(getString(R.string.import_stoptitle))//设置标题
				.setMessage(getString(R.string.import_stopmessage))//设置内容
				.setPositiveButton(getString(R.string.ok),//设置“是”按钮

				new DialogInterface.OnClickListener() 
					{
						@Override
						public void onClick(DialogInterface dialog, int whichButton)
						{
							
							//设置停止导入操作
							myOperator.setIsStopImporting(true);
							//将线程释放
							m_ImportContactsByFetchingThread=null;
							m_RefreshImportByFetchingProgressBarThread=null;
							
							((Activity) m_Context).onBackPressed();
							
						}
					})

					.setNeutralButton(getString(R.string.back), 

				new DialogInterface.OnClickListener() 
					{
					@Override
					public void onClick(DialogInterface dialog, int whichButton)
					{
						myOperator.setIsStopImporting(false);myOperator.setIsImporting(true);
						//点击"否"按钮之后退出Dialog，并继续导入。
						dialog.dismiss();
						
					}
				}).create().show();
			return false;
		}
			return super.onKeyDown(keyCode, event);
	}
}
