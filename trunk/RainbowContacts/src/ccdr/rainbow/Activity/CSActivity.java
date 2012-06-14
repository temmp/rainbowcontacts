package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.VcardOperator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CSActivity extends Activity {
	
	private TextView contactCountText, lasttimetext ,contactCountText_first,lasttime_first;
	private VcardOperator myOperator;
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private ImageButton import_contacts,export_contacts;
	private static final int BLUE_DIALOG = 1;
	private static final int WIFI_DIALOG = 2;
	private static final int ABOUT_DIALOG = 11;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.cs);
		
		myOperator = new VcardOperator(this);


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
		contactCountText = (TextView) findViewById(R.id.contactCountText);
		lasttimetext = (TextView) findViewById(R.id.lasttime);
		lasttime_first = (TextView)findViewById(R.id.lasttime_first);
		contactCountText_first = (TextView)findViewById(R.id.contactCountText_first);
		lasttimetext.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		lasttime_first.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		contactCountText.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		contactCountText_first.setTextColor(this.getResources().getColor(changeSkinUtil.numbercount_text));
		import_contacts = (ImageButton)findViewById(R.id.talkbutton);
		export_contacts = (ImageButton)findViewById(R.id.talkbutton1);
		import_contacts.setImageResource(changeSkinUtil.list1);
		export_contacts.setImageResource(changeSkinUtil.list2);
		contactCountText.setText(myOperator.getContactsCount() + "");
		lasttimetext.setText(myOperator.getlasttime());
	}

	/************ 点击“本地导入联系人” *************/
	public void onexportcontact(View view) {
		
		showDialog(BLUE_DIALOG);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		contactCountText.setText(myOperator.getContactsCount() + "");
	}

	 @Override
		protected Dialog onCreateDialog(int id) {
			// TODO Auto-generated method stub
		 Dialog dialog = null;
			Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.custom_dialog,
			                               null);

			Button image1 = (Button) layout.findViewById(R.id.image1);
			image1.setText(getString(R.string.cs_send_contacts));
			image1.setTextSize(19);
			image1.setTextColor(this.getResources().getColor(R.color.cs_dialogbutton));
			Button image2 = (Button) layout.findViewById(R.id.image2);
			image2.setText(getString(R.string.cs_receive_contacts));
			image2.setTextColor(this.getResources().getColor(R.color.cs_dialogbutton));
			image2.setTextSize(19);
			switch(id){
				case BLUE_DIALOG:

					image1.setOnClickListener(new OnClickListener() { 
				                
				                @Override 
				                public void onClick(View arg0) { 
				             
				                    	
				                    		//蓝牙发送联系人
				                    		Intent i = new Intent(CSActivity.this, BluetoothClientTipsActivity.class);
				                    		startActivity(i);
				                    
				                    
				                } 
				            });
					image2.setOnClickListener(new OnClickListener() { 
			            
			            @Override 
			            public void onClick(View arg0) { 
			         
			                	
			                		//蓝牙发送联系人
			                		Intent i = new Intent(CSActivity.this, BluetoothServerActivity.class);
			                		startActivity(i);
			                
			                
			            } 
			        });  
				Builder	builder = new AlertDialog.Builder(CSActivity.this);
					builder.setView(layout);
					builder.setIcon(R.drawable.bluetooth_icon);
					builder.setTitle(getString(R.string.bluetooth_contacts));
					dialog = builder.create();
					break;
				case WIFI_DIALOG:
					image1.setOnClickListener(new OnClickListener() { 
		                
		                @Override 
		                public void onClick(View arg0) { 
		             
		                    	
		                    		//蓝牙发送联系人
		                    		Intent i = new Intent(CSActivity.this, WifiClientTipsActivity.class);
		                    		startActivity(i);
		                    
		                    
		                } 
		            });
			image2.setOnClickListener(new OnClickListener() { 
	            
	            @Override 
	            public void onClick(View arg0) { 
	         
	                	
	                		//蓝牙发送联系人
	                		Intent i = new Intent(CSActivity.this, WifiServerTipsActivity.class);
	                		startActivity(i);
	                
	                
	            } 
	        });  
		Builder	builder1 = new AlertDialog.Builder(CSActivity.this);
		builder1.setIcon(R.drawable.wifi_icon);

		builder1.setTitle(getString(R.string.wifi_contacts));
			builder1.setView(layout);
			dialog = builder1.create();
			break;
				case ABOUT_DIALOG:
					LayoutInflater inflater1 = LayoutInflater.from(this);
			        final View textEntryView = inflater.inflate(
			                R.layout.about, null);
			        Builder b =new AlertDialog.Builder(CSActivity.this);
					b.setIcon(android.R.drawable.ic_dialog_info);
					b.setTitle(R.string.about);
					b.setView(textEntryView);
//					b.setMessage(getString(text_message));
					b.setPositiveButton(R.string.ok, null);
					dialog =b.create();
					
				break;
					default:
						break;
			}
			return dialog;
		}
	/************ 点击“导出联系人到本地” *************/
	public void onimportcontact(View view) {
		showDialog(WIFI_DIALOG);
		

	}
}
