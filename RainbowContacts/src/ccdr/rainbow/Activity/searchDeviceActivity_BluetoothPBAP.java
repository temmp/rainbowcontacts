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
		// 没有选任何设备
		System.out.println("2313123");
		if (chooseid == -1 || DeviceList == null) {
			Toast.makeText(this, this.getString(R.string.choosedevice),
					Toast.LENGTH_LONG).show();
		} else {
			final BluetoothDevice devicedchoose = DevicesList.get(chooseid);
			if(Constants_Global.DEBUG){
							// 获取所选设备的ID
							
							// 新建一个编辑框，默认显示设备名称+"_"
							final EditText deviceTypeEdit = new EditText(this);
							deviceTypeEdit.setText(devicedchoose.getName());
							// 把光标定位到最后一个字符后面
							deviceTypeEdit.setSelection(devicedchoose.getName().length());
							// 弹出对话框要求用户输入设备型号或标识
							new AlertDialog.Builder(searchDeviceActivity_BluetoothPBAP.this).setTitle(R.string.input_device_type).setView(
									deviceTypeEdit).setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {
				
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// 获取用户输入后的设备类型
											String devicetype = deviceTypeEdit.getText()
													.toString().trim();
											// 改一下要传递的类，device是parcelable
											Intent in = new Intent(searchDeviceActivity_BluetoothPBAP.this,
													BluetoothPBAPActivity.class);
											in.putExtra("bluetoothdevice", devicedchoose);
											// 多传递一个用户输入的设备类型
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
