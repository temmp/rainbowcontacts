package ccdr.rainbow.Bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
/**
 * ���ڹ���һ��Socket���ӣ�����Client��Server������Ҫ��Socket������ȡ����
 * @author Administrator
 *
 */
public abstract class BluetoothTransport {
	/**
	 * Զ�������豸
	 */
	protected BluetoothDevice m_BluetoothDevice;
	/**
	 * ��Զ�������豸���ӵ�Socket
	 */
	protected BluetoothSocket m_BluetoothSocket;
	
	public BluetoothTransport(BluetoothDevice bluetoothdevice)
	{
		m_BluetoothDevice=bluetoothdevice;
	}
	/**
	 * ����UUID��Զ���豸����Socket���ӣ�Զ���豸����Ϊ����ˣ�
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
