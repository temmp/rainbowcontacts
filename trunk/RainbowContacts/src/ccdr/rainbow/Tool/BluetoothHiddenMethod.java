package ccdr.rainbow.Tool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class BluetoothHiddenMethod {
	/**
	 * 调用removeBond等待解除配对时每次循环等待的毫秒数
	 */
	static public final int REMOVEBOND_SLEEP_MILLISECONDS=300;
	
    /**
     * 提取隐藏的CreateBond方法
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
			Log.e("aaa", "无法找到包中的类!!!"+e.getMessage());return false;
		}
    	Method createBond = null;
		try {
			createBond = bluetoothDevice.getMethod("createBond");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "获取createBond方法失败！！！"+e.getMessage());return false;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "获取createBond方法失败！！！"+e.getMessage());return false;
		}
    	Boolean createBondSuccess = null;
		try {
			createBondSuccess = (Boolean) createBond.invoke(device);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用createBond方法失败！！！"+e.getMessage());return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用createBond方法失败！！！"+e.getMessage());return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用createBond方法失败！！！"+e.getMessage());return false;
		}
    	return createBondSuccess;
    }
    
    /**
     * 提取隐藏的RemoveBind方法
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
			Log.e("aaa", "无法找到包中的类!!!"+e.getMessage());return false;
		}
    	Method removeBond = null;
		try {
			removeBond = bluetoothDevice.getMethod("removeBond");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "获取removeBond方法失败！！！"+e.getMessage());return false;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "获取removeBond方法失败！！！"+e.getMessage());return false;
		}
    	Boolean removeBondSuccess = null;
		try {
			removeBondSuccess = (Boolean) removeBond.invoke(remote_device);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用removeBond方法失败！！！"+e.getMessage());return false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用removeBond方法失败！！！"+e.getMessage());return false;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "调用removeBond方法失败！！！"+e.getMessage());return false;
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
    					e.printStackTrace();Log.e("removeBond", "取消配对过程中被中断！！！");
    					return false;/**removeBond方法调用成功，但是在等待过程中被中断**/
    				}
    				times++;
    			}
				else if(remote_device.getBondState()==BluetoothDevice.BOND_NONE){return true;}
    		}
			return false;/**removeBond方法调用成功，但是取消配对过程超时**/
    	}
    	else{return false;}/**removeBond方法调用不成功**/
    }
}
