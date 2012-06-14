package ccdr.rainbow.Activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;

public class selectContactsActivity_BluetoothClient extends selectContactsActivity {

	//标识远程蓝牙设备
	private BluetoothDevice bd = null;
	
	@Override
	public void onexporttolocalcancelbtn(View view) {
		onBackPressed();
	}

	//导出指定的联系人信息
	@Override
	public void onexporttolocalbtn(View view) {
		int exportcontactcount = 0;
		for (int i = 0; i < contactsCount; i++) {
			if (checkedItem.get(i)) {
				exportcontactcount++;
			}
		}
		int[] exportcontactid = new int[exportcontactcount];
		contactPos = new int[exportcontactcount];
		int j = 0;
		for (int i = 0; i < contactsCount; i++) {
			if (checkedItem.get(i)) {
				exportcontactid[j] = contactId[i];
				contactPos[j] = i;

				j++;
			}
		}
		Intent i = new Intent(selectContactsActivity_BluetoothClient.this,
				BluetoothClientActivity.class);
		//索引ID
		i.putExtra("exportcontactid", exportcontactid);
		//被选中人数
		i.putExtra("exportcontactcount", exportcontactcount);
		//选取的人位置
		i.putExtra("contactPos", contactPos);
		i.putExtra("device", bd);
		startActivity(i);
	}

@Override
public void getintent(){
	Intent i = getIntent();
	bd = (BluetoothDevice) i.getParcelableExtra("device");
	}
	
}
