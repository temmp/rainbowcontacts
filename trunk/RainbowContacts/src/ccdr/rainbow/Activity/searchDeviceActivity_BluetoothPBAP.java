package ccdr.rainbow.Activity;

import com.ccdr.rainbowcontacts.R;
import ccdr.rainbow.Constants.Constants_Global;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class searchDeviceActivity_BluetoothPBAP extends searchDeviceActivity {

	private boolean init = true;
	
	public void oncancelbtn(View view) {
 System.out.println("2121");
		onBackPressed();
	}

	public void onconfirmbtn(View view) {
		// û��ѡ�κ��豸
		System.out.println("2313123");
		if (chooseid == -1 || DeviceList == null) {
			Toast.makeText(this, this.getString(R.string.choosedevice),
					Toast.LENGTH_LONG).show();
		} else {
			final BluetoothDevice devicedchoose = DevicesList.get(chooseid);
			if(Constants_Global.DEBUG){
							// ��ȡ��ѡ�豸��ID
							
							// �½�һ���༭��Ĭ����ʾ�豸����+"_"
							final EditText deviceTypeEdit = new EditText(this);
							deviceTypeEdit.setText(devicedchoose.getName());
							// �ѹ�궨λ�����һ���ַ�����
							deviceTypeEdit.setSelection(devicedchoose.getName().length());
							// �����Ի���Ҫ���û������豸�ͺŻ��ʶ
							new AlertDialog.Builder(searchDeviceActivity_BluetoothPBAP.this).setTitle(R.string.input_device_type).setView(
									deviceTypeEdit).setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
				
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// ��ȡ�û��������豸����
											String devicetype = deviceTypeEdit.getText()
													.toString().trim();
											// ��һ��Ҫ���ݵ��࣬device��parcelable
											Intent in = new Intent(searchDeviceActivity_BluetoothPBAP.this,
													BluetoothPBAPActivity.class);
											in.putExtra("bluetoothdevice", devicedchoose);
											// �ഫ��һ���û�������豸����
											in.putExtra("bluetoothdevicetype", devicetype);
											startActivity(in);
				
										}
									}).setNegativeButton(R.string.cancel, null).show();
						}else{
							Intent in = new Intent(searchDeviceActivity_BluetoothPBAP.this,
									BluetoothPBAPActivity.class);
							in.putExtra("bluetoothdevice", devicedchoose);
							startActivity(in);
						}
		}

	}
}
