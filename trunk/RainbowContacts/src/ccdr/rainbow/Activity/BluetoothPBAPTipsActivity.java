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
	
	SQLiteDatabase sqld = null;// �õ�һ�����ݿ����֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	MapEngineHelper db = null;// ����һ�����ݿ�������Ķ���֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	

	// �����һ�����о�Ϊ�䴴����ӳ�����桱���ݿ�
	
//	private PowerManager mPowerManager;// ��Դ���ƹ�����
//	private WakeLock mWakeLock;// ������

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
		//��ʼ����Exception��׼����־�߳�
		
//		if(Constant.isDebug)appExcepiton.init(getApplicationContext());
//		query();// ��������ʱ���鿴��ǰ���ޡ�ӳ�����桱���ݿ⣬���û�о�Ϊ�䴴��һ��

		//�ڸ�Ŀ¼�´�������Ĺ����ļ������ڱ���绰���Լ���ؼ�¼�Ȳ���
//		File pDir = new File(BluetoothFile.PROJECT_FILE);
//		if (!pDir.exists())
//			pDir.mkdir();
		//�½����ڹ����Լ���¼ͨѶ¼�Ĳ�����
//		myOperator = new VcardOperator(this);
//		contactCountText = (TextView) findViewById(R.id.contactCountText);
//		lasttimetext = (TextView) findViewById(R.id.lasttime);

		//��ʾ��ϵ�������Լ����һ�β���ʱ��
		
//		lasttime = myOperator.loadTime(ExplainActivity.this);
//		lasttimetext.setText(lasttime);
//
//		myOperator.setlasttime(lasttime);// ����Ҫ��ʾ�Ĳ���ʱ�䣬�Ա�������ʹ��
//		
//		//������Ļ����
//		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
//		mWakeLock = mPowerManager.newWakeLock(
//				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
//		mWakeLock.acquire();

	}

/*
	*//************* ������ӳ�����桱���ݿ⣬���ڴ�Ÿ����͵��ֻ��ͺ����� *************//*
	private void query() {

		db = new MapEngineHelper(ExplainActivity.this, Constant.DataBase);
		sqld = db.getReadableDatabase();

		Cursor cursor = sqld.query("MapEngine", new String[] { "id as _id" },
				null, null, null, null, null);

		if (!cursor.moveToNext()) {// ��ʾû��ӳ������
			Log.v("û��ӳ������", "û��ӳ������");
			MapEngine_Insert mapEngine_Insert = new MapEngine_Insert();
			mapEngine_Insert.Insert(ExplainActivity.this);
			Log.v("����ӳ������", "ӳ����������");
		}
		cursor.close();// close�α�cursor
		close();// close���ݿ��Լ�DBHelper
	}
*/
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		contactCountText.setText(myOperator.getContactsCount() + "");
	}

	/************ �ر���������������� *************//*
	private void close() {
		db.close();
		sqld.close();
	}*/

/*	*//************ �����һ��������ϵ�ˡ� *************//*
	public void onekeysharecontact(View view) {
		Intent i = new Intent(ExplainActivity.this, Temp_CS.class);
		startActivity(i);
	}*/

	/************ �����ȷ���� *************/
	public void contactonekeyget(View view) {
//		Intent i = new Intent(MainActivity.this,searchDevices.class);
		Intent i = new Intent(BluetoothPBAPTipsActivity.this, searchDeviceActivity_BluetoothPBAP.class);
		startActivity(i);
	}
	public void onbackbtn(View view){
		onBackPressed();
	}
/*	*//************ �������ϵ�˱���ת�ơ� *************//*
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
	        // �������ʹ��xml�ļ�Ҳ����ʹ�ô��뷽ʽ�����뷽ʽ�Ƚ����һЩ~~~   
	        // MenuInflater inflater = new MenuInflater(getApplicationContext());   
	        // inflater.inflate(R.menu.options_menu, menu);   
	        menu.add(0, ABOUT, 1, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);   
//	        menu.add(0, BACK, 2, "��ҳ").setIcon(android.R.drawable.ic_menu_set_as);
//	        menu.add(0, EXIT, 2, "�˳�").setIcon(android.R.drawable.ic_lock_power_off);   
	  
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
//	       case BACK:
//	    	   Intent intent=new Intent();
//				intent.setClass(this, CustomizeMenu.class);
//				intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//	       case EXIT: //ͬ��   
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
