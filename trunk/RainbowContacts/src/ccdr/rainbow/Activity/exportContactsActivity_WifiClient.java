package ccdr.rainbow.Activity;

	import java.io.File;

	import com.ccdr.rainbowcontacts.R;
	import com.ccdr.rainbowcontacts.RainbowContactsActivity;
	import ccdr.rainbow.Constants.Constants_File;
	import ccdr.rainbow.Tool.BzeeLog;
	import ccdr.rainbow.Tool.ChangeSkinUtil;
	import ccdr.rainbow.Tool.VcardOperator;
	import android.app.Activity;
	import android.app.AlertDialog;
	import android.content.Context;
	import android.content.DialogInterface;
	import android.content.Intent;
	import android.content.SharedPreferences;
	import android.os.Bundle;
	import android.os.Environment;
	import android.os.Handler;
	import android.os.Message;
	import android.view.KeyEvent;
	import android.view.View;
	import android.widget.ImageButton;
	import android.widget.LinearLayout;
	import android.widget.ProgressBar;
import android.widget.TextView;

	public class exportContactsActivity_WifiClient extends Activity {

		private TextView textview, textview2,textview3;
		private int exportcontactcount = 0;
		private int[] exportcontactid;
		private String exportingContactName = null;
		private int[] contactPos;
		private VcardOperator myOperator;
		private boolean exportsuccess = false;
		private int exportingContactCount = 0;
		private ImageButton returnbtn;
		private int tempNum = 0;
		ProgressBar bar = null;
		private String path = null;
		// ////////////////////Zhangqx Add////////////////////////
		private Thread m_ExportContactsToLocalThread = null; // 导入电话簿到本地的线程
		private Thread m_RefreshExportToLocalProgressBarThread = null;// 导入电话簿时更新进度条的线程

		private final int EXPORT_CONTACTS_TO_LOCAL_SUCCESS = 100; // 导入电话簿成功
		
		private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
		private TextView up_text,down_text,down_text1;
		private Context m_Context = this;
		
		private static class Constant
		{
//			public static final String LOCALPATH = Constants_File.Path.PROJECT_FILE + File.separator
//					+ "localVcard.vcf";
//			public static final String SDLOCALPATH = Constants_File.Path.SDROOTPATH + File.separator
//					+ "localVcard.vcf";
			
			public static final String WIFI_SEND_PATH = Constants_File.Path.PROJECT_FILE + File.separator
			+ "WifiSend.vcf";
//			public static final String SD_WIFI_SEND_PATH = Constants_File.Path.SDROOTPATH + File.separator
//			+ "WifiSend.vcf";
			
			public final static int REFRESH = 100;
		}
		
		Handler m_ExportedHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case EXPORT_CONTACTS_TO_LOCAL_SUCCESS:
//					BzeeLog.mark("exportsuccess");
//					exportsuccess = true;
//					// returnbtn.setBackgroundResource(R.drawable.norbtn9);
//					// returnbtn.invalidate();
//					returnbtn.setVisibility(View.VISIBLE);
//					bar.setVisibility(View.INVISIBLE);
//					textview.setVisibility(View.INVISIBLE);
//					textview.setText("");
//					textview2.setText(getString(R.string.exported1)
//							+ exportcontactcount + getString(R.string.exported2));
//					String path_new;
//					if(path!=null&&path.length()>32){
//						
//						 path_new =path.substring(0,24) +"...";
//					}else{
//						 path_new =path;
//					}
//					textview3.setText(getString(R.string.import_path)+"\n"+path_new);
					
					
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.setClass(exportContactsActivity_WifiClient.this, WifiClientActivity.class);
					startActivity(intent);
					
					break;
				}
				super.handleMessage(msg);
			}
		};
		// ////////////////////Zhangqx Add////////////////////////

		Handler myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.REFRESH:
					textview.setText(exportingContactCount + "/"
							+ exportcontactcount);
					bar.setProgress(exportingContactCount);
					// BzeeLog.mark("来信息");
					break;
				}
				super.handleMessage(msg);
			}
		};

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.exportcontacts);
			textview3 = (TextView)findViewById(R.id.path_text);
			textview = (TextView) findViewById(R.id.exportingstatus);
			textview2 = (TextView) findViewById(R.id.exportingstatus1);
			bar = (ProgressBar) findViewById(R.id.bar);
			returnbtn = (ImageButton) findViewById(R.id.returnbtn);

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
			Intent i = getIntent();
			exportcontactcount = i.getIntExtra("exportcontactcount", 0);
			exportcontactid = i.getIntArrayExtra("exportcontactid");
			contactPos = i.getIntArrayExtra("contactPos");
			// BzeeLog.step(1);

			bar.setMax(exportcontactcount);

			textview.setText("0/" + exportcontactcount);

			myOperator = new VcardOperator(this);

			// ////////////////////Zhangqx Add////////////////////////
			m_ExportContactsToLocalThread = new Thread() {
				@Override
				public void run() {
					try {

							myOperator.exportContactOneByOneAsVcardFileToSD(
									Constant.WIFI_SEND_PATH, contactPos);
							path = Constant.WIFI_SEND_PATH;
						
						m_ExportedHandler.obtainMessage(
								EXPORT_CONTACTS_TO_LOCAL_SUCCESS).sendToTarget();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			m_ExportContactsToLocalThread.start();

			m_RefreshExportToLocalProgressBarThread = new Thread() {
				@Override
				public void run() {
					while (true) {
						if (!exportsuccess) {
							tempNum = myOperator.getExportingContactCount();
							if (exportingContactCount != tempNum) {
								exportingContactCount = tempNum;
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
			m_RefreshExportToLocalProgressBarThread.start();
			// ////////////////////Zhangqx Add////////////////////////
			// AsyncTask();// 创建并调用异步任务函数
		}

		/************** 创建异步任务实现导入进度操作 ***************/
		/*
		 * public void AsyncTask() {
		 * 
		 * new AsyncTask<Integer, Integer, Boolean>() {
		 * 
		 * @Override protected void onPostExecute(Boolean result) {
		 * BzeeLog.mark("exportsuccess"); exportsuccess = true; //
		 * returnbtn.setBackgroundResource(R.drawable.norbtn9); //
		 * returnbtn.invalidate(); returnbtn.setVisibility(View.VISIBLE);
		 * bar.setVisibility(View.INVISIBLE);
		 * textview.setVisibility(View.INVISIBLE); textview.setText("");
		 * textview2.setText(getString(R.string.exported1) + exportcontactcount +
		 * getString(R.string.exported2)); super.onPostExecute(result); }
		 * 
		 * @Override protected Boolean doInBackground(Integer... params) { try {
		 * myOperator.exportContactOneByOneAsVcardFileToSD(LOCALPATH, contactPos); }
		 * catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } return true; } }.execute(1);
		 * 
		 * // new Thread(new GameThread()).start();
		 * 
		 * new AsyncTask<Integer, Integer, Boolean>() {
		 * 
		 * @Override protected void onPostExecute(Boolean result) {
		 * super.onPostExecute(result); }
		 * 
		 * @Override protected Boolean doInBackground(Integer... params) { while
		 * (true) { if (!exportsuccess) { tempNum =
		 * myOperator.getExportingContactCount(); if (exportingContactCount !=
		 * tempNum) { // BzeeLog.mark(exportingContactCount); exportingContactCount
		 * = tempNum; Message message = new Message(); message.what =
		 * exporting.REFRESH; myHandler.sendMessage(message); //
		 * textview.setText(exportingContactCount + "/" + // exportcontactcount); //
		 * bar.setProgress(exportingContactCount); } try { Thread.sleep(10); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } else { return true; } } } }.execute(1); }
		 */

		/************** 点击“返回” ***************/
		public void onexportedbtn(View view) {
			if (exportsuccess) {
				// startActivity(new Intent(exporting.this, ContactMove.class));
				Intent intent = new Intent();
				intent.setClass(exportContactsActivity_WifiClient.this, RainbowContactsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				// onBackPressed();
			}
		}

		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			// if (exportsuccess) {
			// startActivity(new Intent(exporting.this, ContactMove.class));
			super.onBackPressed();
			// }
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				if (exportsuccess) {
					Intent intent = new Intent();
					intent.setClass(exportContactsActivity_WifiClient.this, RainbowContactsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
				// 设置导出标识以暂停导出
				myOperator.setIsExporting(false);
				new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.alertdialog_title)// 设置标题
						.setMessage(R.string.ask_return_and_stop_exporting)// 设置内容
						.setPositiveButton(R.string.yes,// 设置“是”按钮
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// 设置停止导出操作
										myOperator.setIsStopExporting(true);
										// 将线程释放
										m_ExportContactsToLocalThread = null;
										m_RefreshExportToLocalProgressBarThread = null;
										((Activity) m_Context).onBackPressed();
									}
								}).setNeutralButton(R.string.no,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										myOperator.setIsStopExporting(false);
										myOperator.setIsExporting(true);
										// 点击"否"按钮之后退出Dialog，并继续导入。
										dialog.dismiss();
									}
								}).create().show();
				return false;
			}
			return super.onKeyDown(keyCode, event);
		}
	

}
