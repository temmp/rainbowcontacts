package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class searchDeviceActivity_BluetoothClient extends searchDeviceActivity {
	public void oncancelbtn(View view) {
		onBackPressed();
	}

	public void onconfirmbtn(View view) {
		// û��ѡ�κ��豸
		if (chooseid == -1 || DeviceList == null) {
			Toast.makeText(this, this.getString(R.string.choosedevice),
					Toast.LENGTH_LONG).show();
		} else {
			BluetoothDevice devicedchoose = DevicesList.get(chooseid);
			// ��һ��Ҫ���ݵ��࣬device��parcelable
			Intent in = new Intent(searchDeviceActivity_BluetoothClient.this,
					selectContactsActivity_BluetoothClient.class);
			in.putExtra("device", devicedchoose);
			startActivity(in);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {

		// TODO Auto-generated method stub
		super.onResume();

	}
}
