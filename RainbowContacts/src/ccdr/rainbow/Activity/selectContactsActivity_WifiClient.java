package ccdr.rainbow.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class selectContactsActivity_WifiClient extends selectContactsActivity {
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
		Intent i = new Intent(selectContactsActivity_WifiClient.this,
				exportContactsActivity_WifiClient.class);
		//����ID
		i.putExtra("exportcontactid", exportcontactid);
		//��ѡ������
		i.putExtra("exportcontactcount", exportcontactcount);
		//ѡȡ����λ��
		i.putExtra("contactPos", contactPos);
		startActivity(i);
	}
}
