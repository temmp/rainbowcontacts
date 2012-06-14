package ccdr.rainbow.Tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class BluetoothHiddenMethod {
	/**
	 * ����removeBond�ȴ�������ʱÿ��ѭ���ȴ��ĺ�����
	 */
	static public final int REMOVEBOND_SLEEP_MILLISECONDS=300;
	
    /**
     * ��ȡ���ص�CreateBond����
     * @param device
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static boolean CreateBond(BluetoothDevice device)
    {
    	Class bluetoothDevice = null;
		try {
			bluetoothDevice = Class.forName("android.bluetooth.BluetoothDevice");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "�޷��ҵ����е���!!!"+e.getMessage());return false;
		}
    	Method createBond = null;
		try {
			createBond = bluetoothDevice.getMethod("createBond");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "��ȡcreateBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "��ȡcreateBond����ʧ�ܣ�����"+e.getMessage());return false;
		}
    	Boolean createBondSuccess = null;
		try {
			createBondSuccess = (Boolean) createBond.invoke(device);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����createBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����createBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����createBond����ʧ�ܣ�����"+e.getMessage());return false;
		}
    	return createBondSuccess;
    }
    
    /**
     * ��ȡ���ص�RemoveBind����
     * @param device
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static boolean RemoveBond(BluetoothDevice remote_device)
    {
    	Class bluetoothDevice = null;
		try {
			bluetoothDevice = Class.forName("android.bluetooth.BluetoothDevice");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "�޷��ҵ����е���!!!"+e.getMessage());return false;
		}
    	Method removeBond = null;
		try {
			removeBond = bluetoothDevice.getMethod("removeBond");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "��ȡremoveBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "��ȡremoveBond����ʧ�ܣ�����"+e.getMessage());return false;
		}
    	Boolean removeBondSuccess = null;
		try {
			removeBondSuccess = (Boolean) removeBond.invoke(remote_device);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����removeBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����removeBond����ʧ�ܣ�����"+e.getMessage());return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "����removeBond����ʧ�ܣ�����"+e.getMessage());return false;
		}
    	return removeBondSuccess;
    }
    
    public static boolean RemoveBondWithTimeout(BluetoothDevice remote_device,int milliseconds) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
    {
    	if(BluetoothDevice.BOND_NONE==remote_device.getBondState()){return true;}
    	Class bluetoothDevice=Class.forName("android.bluetooth.BluetoothDevice");
    	Method removeBond=bluetoothDevice.getMethod("removeBond");
    	Boolean removeBondSuccess=(Boolean) removeBond.invoke(remote_device);
    	if(removeBondSuccess)
    	{
    		int times=0;
    		int max_times=milliseconds/REMOVEBOND_SLEEP_MILLISECONDS;
			while(times<max_times)
			{
				if(remote_device.getBondState()==BluetoothDevice.BOND_BONDED
						|| remote_device.getBondState()==BluetoothDevice.BOND_BONDING)
				{
    				try {
    					Thread.sleep(REMOVEBOND_SLEEP_MILLISECONDS);
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();Log.e("removeBond", "ȡ����Թ����б��жϣ�����");
    					return false;/**removeBond�������óɹ��������ڵȴ������б��ж�**/
    				}
    				times++;
    			}
				else if(remote_device.getBondState()==BluetoothDevice.BOND_NONE){return true;}
    		}
			return false;/**removeBond�������óɹ�������ȡ����Թ��̳�ʱ**/
    	}
    	else{return false;}/**removeBond�������ò��ɹ�**/
    }
}
