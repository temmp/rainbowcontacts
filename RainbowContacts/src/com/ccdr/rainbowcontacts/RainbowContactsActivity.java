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
	SQLiteDatabase sqld = null;// �õ�һ�����ݿ����֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	MapEngineHelper db = null;// ����һ�����ݿ�������Ķ���֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	private static final int Conn = 22; 
	private String Flag = null;
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private ImageButton talkbutton,talkbutton1,talkbutton2;
	private static int skin=-1;
	// �����һ�����о�Ϊ�䴴����ӳ�����桱���ݿ�
	
//	private PowerManager mPowerManager;// ��Դ���ƹ�����
//	private WakeLock mWakeLock;// ������

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
		//��ʼ����Exception��׼����־�߳�
//		if(Constant.isDebug)appExcepiton.init(getApplicationContext());
		query();// ��������ʱ���鿴��ǰ���ޡ�ӳ�����桱���ݿ⣬���û�о�Ϊ�䴴��һ��

		//�ڸ�Ŀ¼�´�������Ĺ����ļ������ڱ���绰���Լ���ؼ�¼�Ȳ���
		File pDir = new File(Constants_File.Path.PROJECT_FILE);
		if (!pDir.exists())
			pDir.mkdir();
		//�½����ڹ����Լ���¼ͨѶ¼�Ĳ�����
		myOperator = new VcardOperator(this);


		//��ʾ��ϵ�������Լ����һ�β���ʱ��
		
		lasttime = myOperator.loadTime(RainbowContactsActivity.this);
		lasttimetext.setText(lasttime);

		myOperator.setlasttime(lasttime);// ����Ҫ��ʾ�Ĳ���ʱ�䣬�Ա�������ʹ��
		Flag = null;
//		Intent i = getIntent();
//		Flag = i.getStringExtra("Flagone");
//		System.out.println("flag--"+Flag);
//		if(Flag!=null && Flag.equals("right")){
//		new jumpThread(mHandler).start();
//		}
//		Flag = null;
//		//������Ļ����
//		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		mWakeLock = mPowerManager.newWakeLock(
//				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
//		mWakeLock.acquire();

	}
	




	/************* ������ӳ�����桱���ݿ⣬���ڴ�Ÿ����͵��ֻ��ͺ����� *************/
	private void query() {

		db = new MapEngineHelper(RainbowContactsActivity.this, Constants_Database.DATABASE);
		sqld = db.getReadableDatabase();

		Cursor cursor = sqld.query("MapEngine", new String[] { "id as _id" },
				null, null, null, null, null);

		if (!cursor.moveToNext()) {// ��ʾû��ӳ������
			Log.v("û��ӳ������", "û��ӳ������");
			MapEngine_Insert mapEngine_Insert = new MapEngine_Insert();
			mapEngine_Insert.Insert(RainbowContactsActivity.this);
			Log.v("����ӳ������", "ӳ����������");
		}
		cursor.close();// close�α�cursor
		close();// close���ݿ��Լ�DBHelper
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
				//˯��1500����
				
				getServer = new GetServerData();
		        message = getServer.getServerMessage(url);
//		        System.out.println(message[0]+message[1]+message[2]);
		        sleep(1500);//!!!!!//�����̷߳�����ת��Ϣ		
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





	/************ �ر���������������� *************/
	private void close() {
		db.close();
		sqld.close();
	}
	/************ �������ϵ��һ����á� *************/
	public void contactonekeyget(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, BluetoothPBAPTipsActivity.class);
		startActivity(i);
	}

	/************ �����һ��������ϵ�ˡ� *************/
	public void onekeysharecontact(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, CSActivity.class);
//		Intent i = new Intent(RainbowContactsActivity.this, ShareContacts.class);
		startActivity(i);
	}


	/************ �������ϵ�˱���ת�ơ� *************/
	public void contactmove(View view) {
		Intent i = new Intent(RainbowContactsActivity.this, LocalBackupAndRestoreActivity.class);
		startActivity(i);
	}
	
	 @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {   
	        // TODO Auto-generated method stub   
	        // �������ʹ��xml�ļ�Ҳ����ʹ�ô��뷽ʽ�����뷽ʽ�Ƚ����һЩ~~~   
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
	       case ABOUT: //���ʹ��xml��ʽ���������ʹ��R.id.about   
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
	       case EXIT: //ͬ��   
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
    	  Constants_Global.LOCAL_VERSION = getPackageManager().getPackageInfo(getPackageName(),0).versionCode; //���ñ��ذ汾��
          Constants_Global.SERVER_VERSION = Integer.parseInt(message[1]);//�ٶ��������汾Ϊ2�����ذ汾Ĭ����1
      }catch (Exception ex){
          ex.printStackTrace();
      }
  }
}