package com.ccdr.rainbowcontacts;

import java.io.File;

import ccdr.rainbow.Activity.BluetoothPBAPTipsActivity;
import ccdr.rainbow.Activity.CSActivity;
import ccdr.rainbow.Activity.LocalBackupAndRestoreActivity;
import ccdr.rainbow.Constants.Constants_Database;
import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Tool.AppExcepiton;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.GetServerData;
import ccdr.rainbow.Tool.MapEngineHelper;
import ccdr.rainbow.Tool.MapEngine_Insert;
import ccdr.rainbow.Tool.UpdateService;
import ccdr.rainbow.Tool.VcardOperator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RainbowContactsActivity extends Activity {

	private BluetoothAdapter m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	private TextView contactCountText, lasttimetext ,contactCountText_first,lasttime_first;
	private VcardOperator myOperator;
	private String lasttime;
	private Context context;
	AppExcepiton appExcepiton =AppExcepiton.getInstance();
	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	private static final int ABOUT = 1;  
	private static final int CHANGE_SKIN = 2;
    private static final int EXIT = 3;   
    private static final int FAQ= 4;
    private static final int ABOUT_DIALOG = 11;
    private static final int FAQ_DIALOG= 12;
//	public static boolean onstopfinish = false;
    GetServerData getServer;
	String []message;
	String url = Constants_Global.URL;
	SQLiteDatabase sqld = null;// 得到一个数据库对象，之所以声明为全局变量是为了便于close
	MapEngineHelper db = null;// 声明一个数据库助手类的对象，之所以声明为全局变量是为了便于close
	private static final int Conn = 22; 
	private String Flag = null;
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private ImageButton talkbutton,talkbutton1,talkbutton2;
	private static int skin=-1;
	// 软件第一次运行就为其创建“映射引擎”数据库
	
//	private PowerManager mPowerManager;// 电源控制管理器
//	private WakeLock mWakeLock;// 待机锁

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		skin_linearLayout = (LinearLayout)findViewById(R.id.ltest);
		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		talkbutton = (ImageButton)findViewById(R.id.talkbutton);
		talkbutton1 = (ImageButton)findViewById(R.id.talkbutton1);
		talkbutton2 = (ImageButton)findViewById(R.id.talkbutton2);
		up_text = (TextView)findViewById(R.id.up_text);
		down_text = (TextView)findViewById(R.id.down_text);
		down_text1 = (TextView)findViewById(R.id.down_text1);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		System.out.println(changeSkinUtil.up_view+"--magazineActivityTitle----"+R.drawable.uplay_new);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		talkbutton.setImageResource(changeSkinUtil.talkbutton);
		talkbutton1.setImageResource(changeSkinUtil.talkbutton1);
		talkbutton2.setImageResource(changeSkinUtil.talkbutton2);
		System.out.println(changeSkinUtil.up_text+"--magazineActivityTitle----"+R.color.title);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		down_text.setTextColor(this.getResources().getColor(changeSkinUtil.down_text));
		down_text1.setTextColor(this.getResources().getColor(changeSkinUtil.down_text1));
		contactCountText = (TextView) findViewById(R.id.contactCountText);
		lasttimetext = (TextView) findViewById(R.id.lasttime);
		lasttime_first = (TextView)findViewById(R.id.lasttime_first);
		System.out.println("---"+changeSkinUtil.numbercount_text+"---");
		contactCountText_first = (TextView)findViewById(R.id.contactCountText_first);
		lasttimetext.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		System.out.println("---"+changeSkinUtil.numbercount_text+"---");
		lasttime_first.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		contactCountText.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		contactCountText_first.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		//开始监听Exception并准备日志线程
//		if(Constant.isDebug)appExcepiton.init(getApplicationContext());
		query();// 程序启动时，查看当前有无“映射引擎”数据库，如果没有就为其创建一个

		//在根目录下创建程序的工程文件，用于保存电话本以及相关记录等操作
		File pDir = new File(Constants_File.Path.PROJECT_FILE);
		if (!pDir.exists())
			pDir.mkdir();
		//新建用于管理以及记录通讯录的操作集
		myOperator = new VcardOperator(this);


		//显示联系人数量以及最后一次操作时间
		
		lasttime = myOperator.loadTime(RainbowContactsActivity.this);
		lasttimetext.setText(lasttime);

		myOperator.setlasttime(lasttime);// 设置要显示的操作时间，以便所有类使用
		Flag = null;
//		Intent i = getIntent();
//		Flag = i.getStringExtra("Flagone");
//		System.out.println("flag--"+Flag);
//		if(Flag!=null && Flag.equals("right")){
//		new jumpThread(mHandler).start();
//		}
//		Flag = null;
//		//保持屏幕长亮
//		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		mWakeLock = mPowerManager.newWakeLock(
//				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
//		mWakeLock.acquire();

	}
	




	/************* 创建“映射引擎”数据库，用于存放各类型的手机型号属性 *************/
	private void query() {

		db = new MapEngineHelper(RainbowContactsActivity.this, Constants_Database.DATABASE);
		sqld = db.getReadableDatabase();

		Cursor cursor = sqld.query("MapEngine", new String[] { "id as _id" },
				null, null, null, null, null);

		if (!cursor.moveToNext()) {// 表示没有映射引擎
			Log.v("没有映射引擎", "没有映射引擎");
			MapEngine_Insert mapEngine_Insert = new MapEngine_Insert();
			mapEngine_Insert.Insert(RainbowContactsActivity.this);
			Log.v("创建映射引擎", "映射引擎有了");
		}
		cursor.close();// close游标cursor
		close();// close数据库以及DBHelper
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Conn:
				System.out.println("dfafd");
		        if(Constants_Global.SHOW_Dialog == 0){
		        	System.out.println(message[0]);
//		        	System.out.println(message[0].toString().equals("false"));
		        if(message!=null&&!message[0].toString().equals("false")){	
		        	System.out.println(message[0]);
						checkVersion();	
		        }
		        }
		        
				break;

			}
		}
	};

	class jumpThread extends Thread {

		Handler mhandler;

		jumpThread(Handler mhandler) {
			this.mhandler = mhandler;
		}

		@Override
		public void run() {
			try {
				//睡眠1500毫秒
				
				getServer = new GetServerData();
		        message = getServer.getServerMessage(url);
//		        System.out.println(message[0]+message[1]+message[2]);
		        sleep(1500);//!!!!!//向主线程发送跳转消息		
				mHandler.obtainMessage(Conn).sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("--------------------------------------------------sdasd------------------------------------------------------");
		contactCountText.setText(myOperator.getContactsCount() + "");
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
		talkbutton.setImageResource(changeSkinUtil.talkbutton);
		talkbutton1.setImageResource(changeSkinUtil.talkbutton1);
		talkbutton2.setImageResource(changeSkinUtil.talkbutton2);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		down_text.setTextColor(this.getResources().getColor(changeSkinUtil.down_text));
		down_text1.setTextColor(this.getResources().getColor(changeSkinUtil.down_text1));
	}
	

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Flag = null;
	}





	/************ 关闭数据与助手类对象 *************/
	private void close() {
		db.close();
		sqld.close();
	}
	/************ 点击“联系人一键获得” *************/
	public void contactonekeyget(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, BluetoothPBAPTipsActivity.class);
		startActivity(i);
	}

	/************ 点击“一键分享联系人” *************/
	public void onekeysharecontact(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, CSActivity.class);
//		Intent i = new Intent(RainbowContactsActivity.this, ShareContacts.class);
		startActivity(i);
	}


	/************ 点击“联系人本地转移” *************/
	public void contactmove(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, LocalBackupAndRestoreActivity.class);
		startActivity(i);
	}
	
	 @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {   
	        // TODO Auto-generated method stub   
	        // 这里可以使用xml文件也可以使用代码方式，代码方式比较灵活一些~~~   
	        // MenuInflater inflater = new MenuInflater(getApplicationContext());   
	        // inflater.inflate(R.menu.options_menu, menu);   
	        menu.add(1, ABOUT, 1, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);   
	        menu.add(1, CHANGE_SKIN, 2, R.string.changeSkin).setIcon(android.R.drawable.ic_menu_set_as);
	        menu.add(2, FAQ, 3, R.string.faq).setIcon(android.R.drawable.ic_menu_help);
	        menu.add(2, EXIT, 4, R.string.quit).setIcon(android.R.drawable.ic_lock_power_off);   
	        
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
	       case CHANGE_SKIN:
	    	   showDialog(CHANGE_SKIN);
	    	   break;
	    	   /*Intent intent =new Intent();
	    	   intent.setClass(RainbowContactsActivity.this, ChangeSkin.class);
	    	   startActivity(intent);*/
//	    	   Intent intent=new Intent();
//				intent.setClass(this, CustomizeMenu.class);
//				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
	       case FAQ:
	    	   showDialog(FAQ_DIALOG);
	    	   break;
	       case EXIT: //同上   
	           android.os.Process.killProcess(android.os.Process.myPid());   
	           break;
	           default:
	        	   break;
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
	        TextView menu_text = (TextView)textEntryView.findViewById(R.id.about_text);
//	        String text_message = getString(R.string.about_message);
//	        TextView text_about = (TextView)findViewById(R.id.about_text);
//	        text_about.setText(text_message)
			System.out.println(id);
			switch(id){
				case ABOUT_DIALOG:
					Builder b =new AlertDialog.Builder(RainbowContactsActivity.this);
					b.setIcon(android.R.drawable.ic_dialog_info);
					b.setTitle(R.string.about);
					menu_text.setText(getString(R.string.about_message));
					b.setView(textEntryView);
					
//					b.setMessage(getString(text_message));
					b.setPositiveButton(R.string.ok,new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					dialog =b.create();
					break;
				case FAQ_DIALOG:
					Builder c =new AlertDialog.Builder(RainbowContactsActivity.this);
					c.setIcon(android.R.drawable.ic_menu_help);
					c.setTitle(R.string.faq);
					c.setView(textEntryView);
//					b.setMessage(getString(text_message));
					menu_text.setText(getString(R.string.faq_message));
					c.setPositiveButton(R.string.ok,new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					dialog =c.create();
					break;
				case CHANGE_SKIN:
					final CharSequence [] items = { getString(R.string.changeSkin_number1) ,  getString(R.string.changeSkin_number2) }; 
					SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
					ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
					int a =0;
					 System.out.println("----skin----");
					if(sp.getString("skindata", "").equals("")){
						a=0;
				}  
				else{
				    if(sp.getString("skindata", "").trim().equals("1")){

				    	a=0;
				    }
				    if(sp.getString("skindata", "").trim().equals("2")){
				    	a=1;
				    }
				}
				System.out.println("--213");
					AlertDialog . Builder builder = new AlertDialog . Builder ( RainbowContactsActivity.this ); 
					builder . setTitle ( "Pick a color" ); 
					builder . setSingleChoiceItems ( items , a , new DialogInterface . OnClickListener () { 
					    @Override
						public void onClick ( DialogInterface dialog , int item ) { 
					        if(item == 0){
					        	skin = 0;
					        }
					        if(item==1){	
					        	skin=1;
					        }
					    } 
					}); 
					builder.setPositiveButton(R.string.ok,new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
							ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
							if(skin==0){
								ChangeSkinUtil.setSkin(sp, ChangeSkinUtil.grassland);	
							}else if(skin==1){
								ChangeSkinUtil.setSkin(sp, ChangeSkinUtil.star);
							}
							onResume();
						}
					});
					dialog = builder.create (); 
					default:
						break;
			}
			return dialog;
		}

	@Override

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog dlg = new AlertDialog.Builder(RainbowContactsActivity.this).setTitle(
				getString(R.string.exitornot)).setPositiveButton(
				getString(R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (Constants_Global.BLUETOOTH_ON_BEFORE == false) {
							m_BluetoothAdapter.disable();
						}
						myOperator.saveTime(RainbowContactsActivity.this);
						//Log.d("count", "finish");
						
						finish();
//						System.out.println("---finish"+onstopfinish);
						android.os.Process.killProcess(android.os.Process
								.myPid());
					System.exit(0);
						
					}
				}).setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create();
		dlg.show();
	}
	  public void checkVersion(){
  		initGlobal();
  		if(Constants_Global.LOCAL_VERSION<Constants_Global.SERVER_VERSION){
  		AlertDialog.Builder alert = new AlertDialog.Builder(RainbowContactsActivity.this);
  		alert.setTitle(R.string.software_upgrade).setMessage(message[0]).setIcon(android.R.drawable.presence_online)
  		.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					try {
						Constants_Global.SERVER_VERSION =getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent updateIntent =new Intent(RainbowContactsActivity.this, UpdateService.class);
					Bundle mBundle = new Bundle();  
					mBundle.putString("titleId", message[2]);
					mBundle.putString("apkurl", message[3]);
					updateIntent.putExtras(mBundle);
					updateIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  
                 
                  startService(updateIntent);

				}
			}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			}).show();
  		}
  		
  }
  public void initGlobal(){
      try{
    	  Constants_Global.LOCAL_VERSION = getPackageManager().getPackageInfo(getPackageName(),0).versionCode; //设置本地版本号
          Constants_Global.SERVER_VERSION = Integer.parseInt(message[1]);//假定服务器版本为2，本地版本默认是1
      }catch (Exception ex){
          ex.printStackTrace();
      }
  }
}