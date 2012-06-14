package ccdr.rainbow.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.Contacts;
import ccdr.rainbow.Tool.ListAdapter;
import ccdr.rainbow.Tool.MyLetterListView;
import ccdr.rainbow.Tool.MyLetterListView.OnTouchingLetterChangedListener;
import ccdr.rainbow.Tool.PY_name;
import ccdr.rainbow.Tool.PinYinComparator;
import ccdr.rainbow.Tool.VcardOperator;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LocalBackupActivity extends Activity implements OnItemClickListener,
android.view.View.OnClickListener{
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2,radarback;
	private RelativeLayout choosecontact_back;
	private TextView up_text;
	private ImageButton eee,sss,export_button,export_button1;
	private ImageView imageView1;
	protected VcardOperator myOperator;
	protected Context m_context;
	protected TextView[] contactNames;
	protected ImageView contact_image,contact_image_kong;
	protected boolean[] isChecked;
	protected int[] contactId;
	protected int[] contactPos;
	protected int contactsCount;
	private ListView contactList;
	private ArrayList<String> Name;
	private LinkedList<ContentValues> Pic;
	public   ArrayList<Boolean> checkedItem=new ArrayList<Boolean>();
	 private Bitmap mIcon1;
	 Cursor cursor;
		private Handler handler;
//		private DisapearThread disapearThread;
		private int scrollState;
		private ListAdapter listAdapter;
		private ListView listMain;
		private TextView txtOverlay;
		private WindowManager windowManager;
		public static ChangeSkinUtil changeSkinUtil;
		public static int colorlist;
		private HashMap<String, Integer> alphaIndexer;
		private String [] sections ;
		private OverlayThread overlayThread;
		private MyLetterListView letterListView;
		private int Key_pos;
		private Dialog dialog;
		public static final int COMMON_DIALOG = 111;
		private Handler handler_progress = new Handler();
		private ProgressDialog progressDialog = null;
		private ArrayList<Contacts> contacts_list;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localbackup);
		getintent();
		overlayThread = new OverlayThread();
		  cursor = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED ");
		 
	 


		
		m_context = getApplicationContext();

		myOperator = new VcardOperator(m_context);

		contactsCount = myOperator.getContactsCount();

//		contactList = (ListView) findViewById(R.id.contactList);

		contactNames = new TextView[contactsCount];
//		rl = new RelativeLayout[contactsCount];
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
	
		skin_linearLayout = (LinearLayout)findViewById(R.id.ltest);
		skin_linearLayout1 = (LinearLayout)findViewById(R.id.ltest1);
		skin_linearLayout2 = (LinearLayout)findViewById(R.id.ltest2);
		up_text = (TextView)findViewById(R.id.up_text);
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		 changeSkinUtil = new ChangeSkinUtil(sp);
//		System.out.println(changeSkinUtil.up_view+"--magazineActivityTitle----"+R.drawable.uplay_new);
		skin_linearLayout.setBackgroundResource(changeSkinUtil.up_view);
		skin_linearLayout1.setBackgroundResource(changeSkinUtil.mid_view);
		skin_linearLayout2.setBackgroundResource(changeSkinUtil.down_view);
//		System.out.println(changeSkinUtil.up_text+"--magazineActivityTitle----"+R.color.title);
		up_text.setTextColor(this.getResources().getColor(changeSkinUtil.up_text));
		eee = (ImageButton)findViewById(R.id.eee);
		sss = (ImageButton)findViewById(R.id.sss);
		export_button = (ImageButton)findViewById(R.id.talkbutton);
		export_button1 = (ImageButton)findViewById(R.id.talkbutton1);
		eee.setImageResource(changeSkinUtil.export_contact_all);
		sss.setImageResource(changeSkinUtil.export_contact_deselect);
		export_button.setImageResource(changeSkinUtil.export_contact_back);
		export_button1.setImageResource(changeSkinUtil.export_contact_ok);
//		imageView1 = (ImageView)findViewById(R.id.imageView1);
//		imageView1.setImageResource(changeSkinUtil.imageView1);
		choosecontact_back = (RelativeLayout)findViewById(R.id.choosecontact_back);
		choosecontact_back.setBackgroundResource(changeSkinUtil.choosecontact_back);
		colorlist = getResources().getColor(
				changeSkinUtil.darkgray);
		Name = new ArrayList<String>();
	
		handler = new Handler();
//		new jumpThread(mHandler).start();
		txtOverlay = (TextView) LayoutInflater.from(this).inflate(R.layout.popup_char_hint, null);
		txtOverlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(txtOverlay, lp);
		alphaIndexer = new HashMap<String, Integer>();
		sections = new String[contactsCount];
		
		
		listMain = (ListView) findViewById(R.id.contactList);
		listMain.setOnItemClickListener(this);
//		listMain.setOnScrollListener(this);
		listMain.setItemsCanFocus(false);
		listMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	
		new jumpThread(mHandler).start();
		progressDialog = ProgressDialog.show(LocalBackupActivity.this, getString(R.string.moment), getString(R.string.getdata), true);
//		changeFastScrollerDrawable(listMain);

//		

	
	}
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMMON_DIALOG:
				//新建Intent并设置跳转
				listMain.setAdapter(listAdapter);
				progressDialog.dismiss();
				break;

			}
		}
	};
	public void getintent(){
		
	}
	class jumpThread extends Thread {

		Handler mhandler;

		jumpThread(Handler mhandler) {
			this.mhandler = mhandler;
		}

		@Override
		public void run() {
			try {
				//睡眠1500毫秒
				sleep(100);
				one();
				createlist();
				
				//向主线程发送跳转消息
				mHandler.obtainMessage(COMMON_DIALOG).sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void createlist(){
		for(int i = 0;i<contacts_list.size();i++){
			checkedItem.add(true);
		}
		listAdapter = new ListAdapter(this, contacts_list,checkedItem,this);
		
		/*	      one();
			        //init the check box status to unchecked
			        for(int i=0;i<Name.size();i++){
			            checkedItem.add(i,false);
			        }
//			        Log.d(TAG,Name.toString());
			       chadapter=new CheckAdapter(this);
			       contactList.setAdapter(chadapter);
			       contactList.setItemsCanFocus(false);
			       contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			       contactList.setOnItemClickListener(new OnItemClickListener(){
			            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			                //update the value of checked items
			                if(checkedItem.get(position)==true){
			                    checkedItem.set(position,false);
//			                    Log.d(TAG,"true");
			                }
			                else{
			                    checkedItem.set(position,true);
//			                    Log.d(TAG,"false");
			                }
			            }
			        });*/

		for (int i = 0; i < contactsCount; i++) {
			try{
			//当前汉语拼音首字母
//			System.out.println("Name--"+Name.get(i));
			String text = contacts_list.get(i).getPy();
//			System.out.println("--overlay--"+PY_name.PY(text.substring(0,1))
//					);
//			String text1 = PY_name.getPingYin((String.valueOf(Name.get(i-1)).substring(0))
//			);
//			String fir_py=PY_name.PY(text.substring(0,1));
			String currentStr = contacts_list.get(i).getPy();
			
			//上一个汉语拼音首字母，如果不存在为“ ”
	        String previewStr = (i - 1) >= 0 ? contacts_list.get(i-1).getPy() : " ";
	        if (!previewStr.equals(currentStr)) {
	        	String name = currentStr;
	        	alphaIndexer.put(name, i);  
	        	sections[i] = name; 
	        }
			}catch(Exception e){
//				Name.set(i, "未命名");
			}
	    }
	
				
//				disapearThread = new DisapearThread();

	}

	/************ *************/
	public void onexporttolocalcancelbtn(View view) {
		onBackPressed();
	}

	/************  *************/
	public void onexporttolocalbtn(View view) {
		int exportcontactcount = 0;
		for (int i = 0; i < contactsCount; i++) {
			try{if (checkedItem.get(i)) {
				exportcontactcount++;
			}
			}catch(Exception e){
				continue;
			}
		}
		int[] exportcontactid = new int[exportcontactcount];
		contactPos = new int[exportcontactcount];
		int j = 0;
		for (int i = 0; i < contactsCount; i++) {
			
			try{
				System.out.println(contactsCount+"----"+i);
			if (checkedItem.get(i)) {
				exportcontactid[j] = contactId[i];
				contactPos[j] = contacts_list.get(i).getPosition();
				j++;
			}}catch(Exception e){
			}
			
		}
		Intent i = new Intent(LocalBackupActivity.this, exportContactsActivity.class);
		i.putExtra("exportcontactid", exportcontactid);
		i.putExtra("exportcontactcount", exportcontactcount);
		i.putExtra("contactPos", contactPos);
		startActivity(i);
	}
//
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		Intent i = new Intent(choosecontactlocal.this, ContactMove.class);
//		startActivity(i);
//		super.onBackPressed();
//	}
    public void one(){
    	contactId = new int[contactsCount];
    	Pic = new LinkedList<ContentValues>() ;
    	contacts_list = new ArrayList<Contacts>();
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex(BaseColumns._ID));
		

//				System.out.println("23123---"+StructuredName.CONTENT_ITEM_TYPE);

				String name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//				System.out.println("name--"+name);
			/*	String[] projection = new String[]{ContactsContract.Data.DATA15};

		        String selection = ContactsContract.Contacts._ID+ "="+id +" AND "+ ContactsContract.Data.DATA15 +">0";    
				Cursor cur = getContentResolver().query(ContactsContract.Data.CONTENT_URI, projection, selection, null, " display_name COLLATE LOCALIZED ");*/
				 String photo_id = null;
				 photo_id = cursor.getString(cursor
                         .getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
				 System.out.println("头像--"+photo_id+"--ming--"+name);
				final int position = cursor.getPosition();
				contactId[position] = id;
				
				String text = PY_name.getPingYin((String.valueOf(name).substring(0))
				);
				Contacts contacts = new Contacts();
				
				contacts.setName(name);
				contacts.setPosition(position);
				contacts.setId(id);
				System.out.println("转--"+photo_id);
				if(photo_id!=null){
					byte[] photo =getPhoto(position,Integer.parseInt(photo_id));
					Bitmap map = BitmapFactory.decodeByteArray(photo, 0,photo.length);

				contacts.setPhoto(map);
				System.out.println("转后--"+contacts.getPhoto());
				}
				System.out.println(contacts.getPhoto());
				String text1 = PY_name.PY(text.substring(0,1));
				if(name==null){
					text1="#";		
					}
				contacts.setPy(text1);
				contacts_list.add(contacts);
				Name.add(name);
				 
				
				
//				cur.moveToFirst();
		/*		String Photo_id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
				System.out.println("--"+name+"--"+Photo_id);
				if(Photo_id!=null){
					System.out.println("--cur!=null");
					ContentValues values = new ContentValues();
					values.put("Name", name);
					values.put("_ID", id);
					Pic.add(values);
				}else{
					System.out.println("--cur==null");
					ContentValues values = new ContentValues();
					values.put("Name", name);
					values.put("_ID", "");
					Pic.add(values);
				}*/
				
			} while (cursor.moveToNext());
		}
		Collections.sort(contacts_list,new PinYinComparator());
		  for (int i=0;i<contacts_list.size();i++) { 


		    } 
    }
private   byte[]  getPhoto(int position,int photo_id){
		
		String[] projection = new String[] {

				  ContactsContract.Data.DATA15

				  };

				  String selection = BaseColumns._ID + " = " + photo_id  ;
//				  System.out.println("iid--"+contacts_list.get(position).getId()+"--name--"+contacts_list.get(position).getName()+"--photo--"+contacts_list.get(position).getPhoto());
				/*  Cursor cur = myOperator.getMyContentResolver().query( ContactsContract.AUTHORITY_URI, projection, 
						  selection, null, null);*/
				  Cursor cur = 
					  getContentResolver().query(
					  ContactsContract.Data.CONTENT_URI, projection, 
					  selection, null,
					  null);

				  cur.moveToFirst();

				  byte[] contactIcon = cur.getBlob(0);

				  System.out.println("conTactIcon:" + contactIcon);

				  if (contactIcon == null) {

				   return null;

				  } else {

				   return contactIcon;

				  }


	}
	
//    public void onClick(View v) {
//        String s="You have choosed ";
//        for(int i=0;i<Name.size();i++){
//            if(checkedItem.get(i)){
//                s=s+","+Name.get(i);
//            }
//        }
//        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//    }

	
/*	private void changeFastScrollerDrawable(ListView list) {
		try {
			Field f = AbsListView.class.getDeclaredField("mFastScroller");
			f.setAccessible(true);
			Object obj = f.get(list);
			f = f.getType().getDeclaredField("mThumbDrawable");
			f.setAccessible(true);
			Drawable drawable = (Drawable) f.get(obj);
			drawable = getResources().getDrawable(R.drawable.fast_scroller_img);
			f.set(obj, drawable);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/
/*
	*//** ListView.OnScrollListener *//*
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
	
	if(visibleItemCount>0){
		String text = PY_name.getPingYin((String.valueOf(Name.get(firstVisibleItem + (visibleItemCount >> 1))).substring(0))
		);
//		System.out.println("--overlay--"+PY_name.PY(text.substring(0,1))
//				);
		
		txtOverlay.setText(PY_name.PY(text.substring(0,1)));
		}
	}*/

	/** ListView.OnScrollListener *//*
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
			handler.removeCallbacks(disapearThread);
					
			boolean bool = handler.postDelayed(disapearThread, 1500);
			Log.d("ANDROID_INFO", "postDelayed=" + bool);
		} else {
			txtOverlay.setVisibility(View.VISIBLE);
//			System.out.println("textoverlay--"+txtOverlay.getText());
		}
	}*/

	/** OnItemClickListener */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String personalName = Name.get(position);
//		String url = ListAdapter.URL_PREFIX + personalName.replace(" ", "%20");
//		showInfo(personalName, url);
     /*   if(ListAdapter.checkedItem[position]==true){
        	ListAdapter.checkedItem[position]=false;
        	ListA
//      System.out.println("false");
        }
        else{
        	ListAdapter.checkedItem[position]=true;
//           System.out.println("true");
        }*/
        if(checkedItem.get(position)==true){
        	checkedItem.set(position,false);
//            Log.d(TAG,"true");
        }
        else{
        	checkedItem.set(position,true);
        }
	}

	/**
	 * View.OnClickListener <br/>
	 * 
	 */
@Override
public void onClick(View view) {
		
		if (view instanceof ImageView) {
			int position = ((Integer) view.getTag()).intValue();
			Cursor Tels = getContentResolver().query
            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " +
          contacts_list.get(position).getId(), null, null);  
			String contentTels ="";    
         
            if(Tels.moveToFirst()){
                do{
                 String phoneNumber= Tels.getString(Tels.getColumnIndex
(ContactsContract.CommonDataKinds.Phone.NUMBER));   
                 if(!contentTels.contains(phoneNumber)){
                 contentTels = contentTels + "Tel:"+phoneNumber +"\n";
                 }
                
                }while(Tels.moveToNext());     
               }
			showInfo(contacts_list.get(position).getName(), contentTels);
		}
		
		
			/*int position = ((Integer) view.getTag()).intValue();
			ActionItem actionAdd = new ActionItem(getResources().getDrawable(R.drawable.icon_info),
					"Info", this);
//			ActionItem actionWeb = new ActionItem(getResources().getDrawable(R.drawable.icon_web),
//					"Web", this);
//			ActionItem actionEMail = new ActionItem(getResources().getDrawable(
//					R.drawable.icon_email), "Email", this);
			QuickActionBar qaBar = new QuickActionBar(view, position);
			qaBar.setEnableActionsLayoutAnim(true);
			qaBar.addActionItem(actionAdd);
//			qaBar.addActionItem(actionWeb);
//			qaBar.addActionItem(actionEMail);
			qaBar.show();*/
/*		} else if (view instanceof LinearLayout) {
			
			LinearLayout actionsLayout = (LinearLayout) view;
			QuickActionBar bar = (QuickActionBar) actionsLayout.getTag();
			bar.dismissQuickActionBar();
			int listItemIdx = bar.getListItemIndex();
			TextView txtView = (TextView) actionsLayout.findViewById(R.id.qa_actionItem_name);
			String actionName = txtView.getText().toString();
			String personalName = Name.get(listItemIdx);
			String url =  personalName;
			System.out.println("---contactID--"+contactId[listItemIdx]);
			 Cursor Tels = getContentResolver().query
             (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " +
           contactId[listItemIdx], null, null);  
			String contentTels ="";    
             int i =1;
             if(Tels.moveToFirst()){
                 do{
                  String phoneNumber= Tels.getString(Tels.getColumnIndex
(ContactsContract.CommonDataKinds.Phone.NUMBER));     
                  contentTels = contentTels + "Tel:"+phoneNumber +"\n";
                  i++;
                 }while(Tels.moveToNext());     
                }

			if (actionName.equals("Info")) {
				showInfo(personalName, contentTels);
			} 
		}*/
	}

	public void showInfo(String name, String Tels) {
		String contentTels = "";
		String content ="";
		if(Tels.length()>=1){
	content = "Name:" + name + "\n"//
				+ Tels //
		;}
		else{
			content = "Name:" + name ;
		}
		Dialog dialog = new Dialog(LocalBackupActivity.this);
		dialog.setContentView(R.layout.info_dialog);
		dialog.setTitle("Personal Information");
		dialog.setCanceledOnTouchOutside(true);
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(content);
		text.setGravity(Gravity.CENTER_VERTICAL);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.icon_info);
		dialog.show();
	}


/*
	private class DisapearThread implements Runnable {
		public void run() {
			if (scrollState == ListView.OnScrollListener.SCROLL_STATE_IDLE) {
				txtOverlay.setVisibility(View.INVISIBLE);
			}
		}
	}*/

	@Override
	public void onDestroy() {
		super.onDestroy();
		txtOverlay.setVisibility(View.INVISIBLE);
		windowManager.removeView(txtOverlay);
	}

	public void onselectall(View view) {
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		if(Name.size() != 0) {
			
	        int count = listMain.getChildCount();
	        for(int i = 0; i < count; i++){
	            LinearLayout layout = (LinearLayout)listMain.getChildAt(i);
	            int c = layout.getChildCount();
	            
	            for(int j = 0; j < c; j++){
	            	if(layout.getChildAt(j) instanceof LinearLayout){
	            	LinearLayout layout1 = (LinearLayout)layout.getChildAt(j);
	            	int d = layout1.getChildCount();
	            	for(int k = 0;k<d;k++){
	                View view1 = layout1.getChildAt(k);
	                if(view1 instanceof CheckedTextView){
	               
	                	
	                    ((CheckedTextView)view1).setChecked(true);
	                    for(int h = 0;h<checkedItem.size();h++){
	                    	checkedItem.set(h, true);
	                    }
	                    break;
	                }
	            	}
	                }
	            }
//	        	  LinearLayout layout = (LinearLayout)listMain.getChildAt(i);
//		            int c = layout.getChildCount();
		     /*       System.out.println("---2--"+i);
		          
		                View view1 = listMain.getChildAt(i);
		                if(view1 instanceof CheckedTextView){
		                	System.out.println("---3");
		                    ((CheckBox)view).setChecked(true);
		                    break;
		                
		            }*/
	        }
		}
	}
	
	public void onnotselectall(View view) {
		SharedPreferences sp = getSharedPreferences(ChangeSkinUtil.changeSkin,ChangeSkinUtil.popedom);
		ChangeSkinUtil changeSkinUtil = new ChangeSkinUtil(sp);
		if(Name.size() != 0) {
			
		        int count = listMain.getChildCount();
		        for(int i = 0; i < count; i++){
		            LinearLayout layout = (LinearLayout)listMain.getChildAt(i);
		            int c = layout.getChildCount();
		            
		            for(int j = 0; j < c; j++){
		            	if(layout.getChildAt(j) instanceof LinearLayout){
		            	LinearLayout layout1 = (LinearLayout)layout.getChildAt(j);
		            	int d = layout1.getChildCount();
		            	for(int k = 0;k<d;k++){
		                View view1 = layout1.getChildAt(k);
		                if(view1 instanceof CheckedTextView){
		                	
		                    ((CheckedTextView)view1).setChecked(false);
		                    for(int h = 0;h<checkedItem.size();h++){
		                    	checkedItem.set(h, false);
		                    }
		                    break;
		                }
		            	}
		                }
		            }
//		        	  LinearLayout layout = (LinearLayout)listMain.getChildAt(i);
//			            int c = layout.getChildCount();
			     /*       System.out.println("---2--"+i);
			          
			                View view1 = listMain.getChildAt(i);
			                if(view1 instanceof CheckedTextView){
			                	System.out.println("---3");
			                    ((CheckBox)view).setChecked(true);
			                    break;
			                
			            }*/
		        }	}
	}


	 private class LetterListViewListener implements OnTouchingLetterChangedListener{

			@Override
			public void onTouchingLetterChanged(final String s) {
				if(alphaIndexer.get(s) != null) {
					Key_pos = alphaIndexer.get(s);
//					listMain.setSelection(position);
					txtOverlay.setText(sections[Key_pos]);
					txtOverlay.setVisibility(View.VISIBLE);
					handler.removeCallbacks(overlayThread);
					//延迟一秒后执行，让overlay为不可见
					handler.postDelayed(overlayThread, 500);
				}
				
			}
	    	
	    }
	 
	    
	    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyUp(keyCode, event);
	}


		//设置overlay不可见
	    private class OverlayThread implements Runnable {

			@Override
			public void run() {
				txtOverlay.setVisibility(View.GONE);
				listMain.setSelection(Key_pos);
			}
	    	
	    }
	    

}
