package ccdr.rainbow.Activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;

public class selectContactsActivity_BluetoothClient extends selectContactsActivity {

	//��ʶԶ�������豸
	private BluetoothDevice bd = null;
	
	@Override
	public void onexporttolocalcancelbtn(View view) {
		onBackPressed();
	}

	//����ָ������ϵ����Ϣ
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
		//����ID
		i.putExtra("exportcontactid", exportcontactid);
		//��ѡ������
		i.putExtra("exportcontactcount", exportcontactcount);
		//ѡȡ����λ��
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
