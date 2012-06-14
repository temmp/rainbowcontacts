package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Tool.ChangeSkinUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BluetoothClientTipsActivity extends Activity {

	private LinearLayout skin_linearLayout ,skin_linearLayout1,skin_linearLayout2;
	private TextView up_text,down_text,down_text1;
	private TextView sharesend_text,sharesend_text1,sharesend_text2,sharesend_text3;
	private ImageButton sharesend_BackButton,sharesend_NextButton;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothclienttips);
		sharesend_text = (TextView)findViewById(R.id.sharesend_text);
		sharesend_text1 = (TextView)findViewById(R.id.sharesend_text1);
		sharesend_text2 = (TextView)findViewById(R.id.sharesend_text2);
		sharesend_text3 = (TextView)findViewById(R.id.sharesend_text3);
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
		sharesend_BackButton = (ImageButton)findViewById(R.id.sharesend_BackButton);
		sharesend_NextButton = (ImageButton)findViewById(R.id.sharesend_NextButton);
		sharesend_BackButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_BackButton);
		sharesend_NextButton.setImageResource(changeSkinUtil.onekeysharecontact_tip_NextButton);
		sharesend_text.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		sharesend_text1.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		sharesend_text2.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
		sharesend_text3.setTextColor(this.getResources().getColor(changeSkinUtil.uplayText));
	}

	/************** 点击了“确定”按钮 ***************/
	public void onokbtn(View view) {
		Intent i = new Intent(BluetoothClientTipsActivity.this,
				searchDeviceActivity_BluetoothClient.class);
		startActivity(i);
	}

	/************** 点击了“返回”按钮 ***************/
	public void onbackbtn(View view) {
		onBackPressed();
	}

}
