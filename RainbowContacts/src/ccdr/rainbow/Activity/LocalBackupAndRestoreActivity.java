package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import ccdr.rainbow.Tool.VcardOperator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



public class LocalBackupAndRestoreActivity extends Activity {
	

	private TextView contactCountText, lasttimetext ,contactCountText_first,lasttime_first;
	private VcardOperator myOperator;
	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private ImageButton import_contacts,export_contacts;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.localbackupandrestore);
		
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
		import_contacts.setImageResource(changeSkinUtil.import_contactsbutton);
		export_contacts.setImageResource(changeSkinUtil.export_contactsbutton);
		contactCountText.setText(myOperator.getContactsCount() + "");
		lasttimetext.setText(myOperator.getlasttime());
	}

	/************ 点击“本地导入联系人” *************/
	public void onexportcontact(View view) {
		Intent i = new Intent(LocalBackupAndRestoreActivity.this, LocalRestoreActivity.class);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		contactCountText.setText(myOperator.getContactsCount() + "");
	}

	
	/************ 点击“导出联系人到本地” *************/
	public void onimportcontact(View view) {
		Intent i = new Intent(LocalBackupAndRestoreActivity.this, LocalBackupActivity.class);
		startActivity(i);
	}

}
