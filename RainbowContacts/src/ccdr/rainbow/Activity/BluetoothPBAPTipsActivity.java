package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.MapEngineHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BluetoothPBAPTipsActivity extends Activity {

	private TextView contactCountText, lasttimetext;
//	private VcardOperator myOperator;
//	private String lasttime;
//	private Context context;
//	AppExcepiton appExcepiton =AppExcepiton.getInstance();
//	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	private static final int ABOUT = 1;  
//	private static final int BACK = 2;
//    private static final int EXIT = 2;   
    private static final int ABOUT_DIALOG = 11;
//	public static boolean onstopfinish = false;
    
    
    
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	
	private ImageButton onekeysharecontact_tip_BackButton,onekeysharecontact_tip_OkButton;
	
	SQLiteDatabase sqld = null;// 得到一个数据库对象，之所以声明为全局变量是为了便于close
	MapEngineHelper db = null;// 声明一个数据库助手类的对象，之所以声明为全局变量是为了便于close
	

	// 软件第一次运行就为其创建“映射引擎”数据库
	
//	private PowerManager mPowerManager;// 电源控制管理器
//	private WakeLock mWakeLock;// 待机锁

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothpbaptips);
		TextView text = (TextView)findViewById(R.id.text);
		TextView text1 = (TextView)findViewById(R.id.text1);
		TextView text2 = (TextView)findViewById(R.id.text2);
		TextView text3 = (TextView)findViewById(R.id.text3);
		text1.setText(getString(R.string.main_text));
		text2.setText(getString(R.string.main_text1));
		text3.setText(getString(R.string.main_text2));
		/******************************************/
		onekeysharecontact_tip_BackButton = (ImageButton)findViewById(R.id.onekeysharecontact_tip_BackButton);
		onekeysharecontact_tip_OkButton = (ImageButton)findViewById(R.id.onekeysharecontact_tip_OkButton);
		
		/*********************************************/
		/****************************************************/
		
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
		
		/********************************************************************/
		onekeysharecontact_tip_BackButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_BackButton);
		onekeysharecontact_tip_OkButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_OkButton);
		text.setTextColor(this.getResources().getColor(changeSkinUtil.onekeysharecontact_text_color));
		text1.setTextColor(this.getResources().getColor(changeSkinUtil.onekeysharecontact_text_color));
		text2.setTextColor(this.getResources().getColor(changeSkinUtil.onekeysharecontact_text_color));
		text3.setTextColor(this.getResources().getColor(changeSkinUtil.onekeysharecontact_text_color));
		/***************************************************************/
		//开始监听Exception并准备日志线程
		
//		if(Constant.isDebug)appExcepiton.init(getApplicationContext());
//		query();// 程序启动时，查看当前有无“映射引擎”数据库，如果没有就为其创建一个

		//在根目录下创建程序的工程文件，用于保存电话本以及相关记录等操作
//		File pDir = new File(BluetoothFile.PROJECT_FILE);
//		if (!pDir.exists())
//			pDir.mkdir();
		//新建用于管理以及记录通讯录的操作集
//		myOperator = new VcardOperator(this);
//		contactCountText = (TextView) findViewById(R.id.contactCountText);
//		lasttimetext = (TextView) findViewById(R.id.lasttime);

		//显示联系人数量以及最后一次操作时间
		
//		lasttime = myOperator.loadTime(ExplainActivity.this);
//		lasttimetext.setText(lasttime);
//
//		myOperator.setlasttime(lasttime);// 设置要显示的操作时间，以便所有类使用
//		
//		//保持屏幕长亮
//		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		mWakeLock = mPowerManager.newWakeLock(
//				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
//		mWakeLock.acquire();

	}

/*
	*//************* 创建“映射引擎”数据库，用于存放各类型的手机型号属性 *************//*
	private void query() {

		db = new MapEngineHelper(ExplainActivity.this, Constant.DataBase);
		sqld = db.getReadableDatabase();

		Cursor cursor = sqld.query("MapEngine", new String[] { "id as _id" },
				null, null, null, null, null);

		if (!cursor.moveToNext()) {// 表示没有映射引擎
			Log.v("没有映射引擎", "没有映射引擎");
			MapEngine_Insert mapEngine_Insert = new MapEngine_Insert();
			mapEngine_Insert.Insert(ExplainActivity.this);
			Log.v("创建映射引擎", "映射引擎有了");
		}
		cursor.close();// close游标cursor
		close();// close数据库以及DBHelper
	}
*/
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		contactCountText.setText(myOperator.getContactsCount() + "");
	}

	/************ 关闭数据与助手类对象 *************//*
	private void close() {
		db.close();
		sqld.close();
	}*/

/*	*//************ 点击“一键分享联系人” *************//*
	public void onekeysharecontact(View view) {
		Intent i = new Intent(ExplainActivity.this, Temp_CS.class);
		startActivity(i);
	}*/

	/************ 点击“确定” *************/
	public void contactonekeyget(View view) {
//		Intent i = new Intent(MainActivity.this,searchDevices.class);
		Intent i = new Intent(BluetoothPBAPTipsActivity.this, searchDeviceActivity_BluetoothPBAP.class);
		startActivity(i);
	}
	public void onbackbtn(View view){
		onBackPressed();
	}
/*	*//************ 点击“联系人本地转移” *************//*
	public void contactmove(View view) {
		Intent i = new Intent(ExplainActivity.this, ContactMove.class);
		startActivity(i);
	}*/
	
//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//		mWakeLock.acquire();
//	}

//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
////		System.out.println("---onstop"+onstopfinish);
////		if(onstopfinish)
////			finish();
//		
//		mWakeLock.release();
//	}
	 @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {   
	        // TODO Auto-generated method stub   
	        // 这里可以使用xml文件也可以使用代码方式，代码方式比较灵活一些~~~   
	        // MenuInflater inflater = new MenuInflater(getApplicationContext());   
	        // inflater.inflate(R.menu.options_menu, menu);   
	        menu.add(0, ABOUT, 1, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);   
//	        menu.add(0, BACK, 2, "首页").setIcon(android.R.drawable.ic_menu_set_as);
//	        menu.add(0, EXIT, 2, "退出").setIcon(android.R.drawable.ic_lock_power_off);   
	  
//	        setMenuBackgroud();   
	        return true;   
	    }   
	 @Override  
	   public boolean onOptionsItemSelected(MenuItem item) {   
	       // TODO Auto-generated method stub   
	       int id = item.getItemId();   
	       switch (id) {   
	       case ABOUT: //如果使用xml方式，这里可以使用R.id.about   
	    	   showDialog(ABOUT_DIALOG);
	           break;   
//	       case BACK:
//	    	   Intent intent=new Intent();
//				intent.setClass(this, CustomizeMenu.class);
//				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//	       case EXIT: //同上   
//	           android.os.Process.killProcess(android.os.Process.myPid());   
	       }   
	       return super.onOptionsItemSelected(item);   
	   }   
	 @Override
		protected Dialog onCreateDialog(int id) {
			// TODO Auto-generated method stub
			Dialog dialog = null;
			LayoutInflater inflater = LayoutInflater.from(this);
	        final View textEntryView = inflater.inflate(
	                R.layout.about, null);
			System.out.println(id);
			switch(id){
				case ABOUT_DIALOG:
					Builder b =new AlertDialog.Builder(BluetoothPBAPTipsActivity.this);
					b.setIcon(android.R.drawable.ic_dialog_info);
					b.setTitle(R.string.about);
					b.setView(textEntryView);
					b.setPositiveButton(R.string.ok,new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					dialog =b.create();
					break;
					default:
						break;
			}
			return dialog;
		}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	 
	
}
