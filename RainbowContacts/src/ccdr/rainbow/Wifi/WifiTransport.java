package ccdr.rainbow.Wifi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import ccdr.rainbow.Tool.WifiDevice;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;

public abstract class WifiTransport {
	/**
	 * 上层调用者的上下文
	 */
	protected Context m_Context;
	/**
	 * 用于管理本地WiFi的类
	 */
	protected WifiManager m_WifiManager;
	/**
	 * 本地用户设备名称
	 */
	protected String m_UserDeviceName;
	/**
	 * 用户选择的远程WiFi设备
	 */
	protected WifiDevice m_ReceiverDevice=null;
	/**
	 * 远程WiFi设备的列表
	 */
	protected ArrayList<WifiDevice> m_WifiDeviceList;
	/**
	 * 监听客户端连接请求的服务端套接字
	 */
	protected ServerSocket m_ServerConnectSocket=null;
	/**
	 * 用于传输（接收客户端文件）的服务端套接字
	 */
	protected ServerSocket m_ServerTransportSocket=null;
	/**
	 * 服务端获取到客户端的远程WiFi设备
	 */
	protected WifiDevice m_SenderDevice=null;
	/**
	 * 服务端获取到客户端的IP地址
	 */
//	protected String m_ClientIPAddress=null;
	/**
	 * 服务端获取到客户端的设备名称
	 */
//	protected String m_ClientDeviceName=null;
	/**
	 * 是否正在监听UDP广播数据包
	 */
	protected boolean isListeningBroadcast;
	/**
	 * 是否正在广播UDP数据包
	 */
	protected boolean isBroadcasting;
	/**
	 * 
	 */
	protected boolean isAcceptingRequest;
	/**
	 * 是否有接收文件的请求
	 */
	protected boolean hasFileReceiveRequest;
	/**
	 * 用户选择接收或拒绝（1为拒绝，0为接收）
	 */
	protected int m_UserChoice=-1;
	/**
	 * 用于同步的ack
	 */
	protected byte[] ack=new byte[]{'a','c','k'};

	/**
	 * 构造函数
	 * @param context
	 */
	public WifiTransport(Context context)
	{
		m_Context=context;
		m_WifiManager=(WifiManager) m_Context.getSystemService(Context.WIFI_SERVICE);
		m_WifiDeviceList=new ArrayList<WifiDevice>();
		m_UserDeviceName=Build.MODEL;//终端用户名称
		isListeningBroadcast=false;
		isBroadcasting=false;
		isAcceptingRequest=false;
	}
	/**
	 * 关闭监听广播的Socket
	 */
	public abstract void close();
	
	/**
	 * 返回所有远程WiFi设备
	 * @return
	 */
	public List<WifiDevice> getAllRemoteWifiDevice() {
		// TODO Auto-generated method stub
		return m_WifiDeviceList;
	}
	/**
	 * 返回所有远程WiFi设备的数量
	 * @return
	 */
	public int getRemoteWifiDeviceCount() {
		// TODO Auto-generated method stub
		if(null==m_WifiDeviceList){return -1;}
		return m_WifiDeviceList.size();
	}

	/**
	 * 确保WiFi模块已经打开
	 * @return
	 */
	public boolean ensureWifiOpen()
	{
		boolean is_wifi_enabled=true;
		if(!m_WifiManager.isWifiEnabled())
		{
			is_wifi_enabled=false;
			//打开WiFi
			is_wifi_enabled=m_WifiManager.setWifiEnabled(true);
		}
		return is_wifi_enabled;
	}
	/**
	 * 设置用户选择的远程Wifi设备
	 * @param device
	 */
//	public void setSelectedWifiDevice(WifiDevice device)
//	{
//		m_SelectedWifiDevice=device;
//	}
	/**
	 * 返回用户选择的远程Wifi设备
	 * @return
	 */
//	public WifiDevice getSelectedWifiDevice()
//	{
//		return m_SelectedWifiDevice;
//	}
	/**
	 * 返回发送端Wifi设备
	 * @return
	 */
	public WifiDevice getSenderDevice()
	{
		return m_SenderDevice;
	}
	/**
	 * 返回接收方WiFi设备
	 * @return
	 */
	public WifiDevice getReceiverDevice()
	{
		return m_ReceiverDevice;
	}
	/**
	 * 设置接收方WiFi设备
	 * @param device
	 */
	public void setReceiverDevice(WifiDevice device)
	{
		m_ReceiverDevice=device;
	}
	/**
	 * 返回是否正在监听
	 * @return
	 */
	public boolean getBroadcastReceiveState()
	{
		return isListeningBroadcast;
	}
	/**
	 * 返回是否正在广播
	 * @return
	 */
	public boolean getBroadcastState()
	{
		return isBroadcasting;
	}
	/**
	 * 设置是否正在监听，为false时用于跳出循环
	 */
	public void setBroadcastReceiveState(boolean state)
	{
		isListeningBroadcast=state;
	}
	/**
	 * 设置是否正在广播，为false时用于跳出循环
	 */
	public void setBroadcastState(boolean state)
	{
		isBroadcasting=state;
	}
	/**
	 * 返回是否有接收文件请求
	 * @return
	 */
	public boolean getFileReceiveRequestState()
	{
		return hasFileReceiveRequest;
	}
	/**
	 * 设置是否有接收文件请求
	 * @param state
	 */
	public void setFileReceiveRequestState(boolean state)
	{
		hasFileReceiveRequest=state;
	}
	/**
	 * 获取服务端接收文件的选项
	 * @return
	 */
	public int getUserChoice()
	{
		return m_UserChoice;
	}
	/**
	 * 设置服务端接收文件的选项（0为接收，1为不接收）
	 */
	public void setUserChoice(int choice)
	{
		if(choice==1 || choice==0)
		{
			m_UserChoice=choice;
		}
		else
		{
			m_UserChoice=-1;
		}
	}
	/**
	 * 停止监听远程设备的UDP广播数据包，事实上是设置监听状态为false
	 */
	public void stopListeningRemoteWifiDevice()
	{
		setBroadcastReceiveState(false);
	}
	/**
	 * 停止发送UDP广播数据包，事实上是设置广播状态为false
	 */
	public void stopSendingBroadcast()
	{
		setBroadcastState(false);
	}
	/**
	 * 停止监听客户端请求
	 */
	public void stopAcceptConnectionRequest()
	{
		try {
			m_ServerConnectSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAcceptingRequestState(false);
//		m_ServerConnectSocket=null;
	}
	/**
	 * 返回是否正在监听客户端请求
	 * @return
	 */
	public boolean getAcceptRequestState()
	{
		return isAcceptingRequest;
	}
	/**
	 * 设置是否正在监听请求，为false时用于跳出循环
	 * @param state
	 */
	public void setAcceptingRequestState(boolean state)
	{
		isAcceptingRequest=state;
	}
	
	
	/**
	 * 根据局域网内IP地址获得Mac地址
	 * @param ip
	 * @return
	 */
	public String getMacFromArpCache(String ip) {  
		if (ip == null)  
//		return null;
		return "None";
		BufferedReader br = null;  
		try {  
		br = new BufferedReader(new FileReader("/proc/net/arp"));  
		String line;  
		while ((line = br.readLine()) != null) {  
		String[] splitted = line.split(" +");  
		if (splitted != null && splitted.length >= 4 && ip.equals(splitted[0])) {  
		// Basic sanity check  
		String mac = splitted[3];  
		if (mac.matches("..:..:..:..:..:..")) {  
		return mac;  
		} else {  
//		return null;
		return "UnKnown";
		}  
		}  
		}  
		} catch (Exception e) {  
		e.printStackTrace();  
		} finally {  
		try {  
		br.close();  
		} catch (IOException e) {  
		e.printStackTrace();  
		}  
		}  
//		return null;
		return "UnKnown";
		}
}
