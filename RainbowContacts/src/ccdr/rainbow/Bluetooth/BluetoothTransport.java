package ccdr.rainbow.Bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
/**
 * 用于管理一个Socket连接，由于Client和Server都有需要该Socket，固提取出来
 * @author Administrator
 *
 */
public abstract class BluetoothTransport {
	/**
	 * 远程蓝牙设备
	 */
	protected BluetoothDevice m_BluetoothDevice;
	/**
	 * 与远程蓝牙设备连接的Socket
	 */
	protected BluetoothSocket m_BluetoothSocket;
	
	public BluetoothTransport(BluetoothDevice bluetoothdevice)
	{
		m_BluetoothDevice=bluetoothdevice;
	}
	/**
	 * 根据UUID与远程设备建立Socket连接，远程设备可视为服务端？
	 * @param uuid
	 * @return
	 * @throws IOException
	 */
	public BluetoothSocket createSocket(UUID uuid) throws IOException
	{
		m_BluetoothSocket=m_BluetoothDevice.createRfcommSocketToServiceRecord(uuid);
		return m_BluetoothSocket;
	}
}
