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
	 * �ϲ�����ߵ�������
	 */
	protected Context m_Context;
	/**
	 * ���ڹ�����WiFi����
	 */
	protected WifiManager m_WifiManager;
	/**
	 * �����û��豸����
	 */
	protected String m_UserDeviceName;
	/**
	 * �û�ѡ���Զ��WiFi�豸
	 */
	protected WifiDevice m_ReceiverDevice=null;
	/**
	 * Զ��WiFi�豸���б�
	 */
	protected ArrayList<WifiDevice> m_WifiDeviceList;
	/**
	 * �����ͻ�����������ķ�����׽���
	 */
	protected ServerSocket m_ServerConnectSocket=null;
	/**
	 * ���ڴ��䣨���տͻ����ļ����ķ�����׽���
	 */
	protected ServerSocket m_ServerTransportSocket=null;
	/**
	 * ����˻�ȡ���ͻ��˵�Զ��WiFi�豸
	 */
	protected WifiDevice m_SenderDevice=null;
	/**
	 * ����˻�ȡ���ͻ��˵�IP��ַ
	 */
//	protected String m_ClientIPAddress=null;
	/**
	 * ����˻�ȡ���ͻ��˵��豸����
	 */
//	protected String m_ClientDeviceName=null;
	/**
	 * �Ƿ����ڼ���UDP�㲥���ݰ�
	 */
	protected boolean isListeningBroadcast;
	/**
	 * �Ƿ����ڹ㲥UDP���ݰ�
	 */
	protected boolean isBroadcasting;
	/**
	 * 
	 */
	protected boolean isAcceptingRequest;
	/**
	 * �Ƿ��н����ļ�������
	 */
	protected boolean hasFileReceiveRequest;
	/**
	 * �û�ѡ����ջ�ܾ���1Ϊ�ܾ���0Ϊ���գ�
	 */
	protected int m_UserChoice=-1;
	/**
	 * ����ͬ����ack
	 */
	protected byte[] ack=new byte[]{'a','c','k'};

	/**
	 * ���캯��
	 * @param context
	 */
	public WifiTransport(Context context)
	{
		m_Context=context;
		m_WifiManager=(WifiManager) m_Context.getSystemService(Context.WIFI_SERVICE);
		m_WifiDeviceList=new ArrayList<WifiDevice>();
		m_UserDeviceName=Build.MODEL;//�ն��û�����
		isListeningBroadcast=false;
		isBroadcasting=false;
		isAcceptingRequest=false;
	}
	/**
	 * �رռ����㲥��Socket
	 */
	public abstract void close();
	
	/**
	 * ��������Զ��WiFi�豸
	 * @return
	 */
	public List<WifiDevice> getAllRemoteWifiDevice() {
		// TODO Auto-generated method stub
		return m_WifiDeviceList;
	}
	/**
	 * ��������Զ��WiFi�豸������
	 * @return
	 */
	public int getRemoteWifiDeviceCount() {
		// TODO Auto-generated method stub
		if(null==m_WifiDeviceList){return -1;}
		return m_WifiDeviceList.size();
	}

	/**
	 * ȷ��WiFiģ���Ѿ���
	 * @return
	 */
	public boolean ensureWifiOpen()
	{
		boolean is_wifi_enabled=true;
		if(!m_WifiManager.isWifiEnabled())
		{
			is_wifi_enabled=false;
			//��WiFi
			is_wifi_enabled=m_WifiManager.setWifiEnabled(true);
		}
		return is_wifi_enabled;
	}
	/**
	 * �����û�ѡ���Զ��Wifi�豸
	 * @param device
	 */
//	public void setSelectedWifiDevice(WifiDevice device)
//	{
//		m_SelectedWifiDevice=device;
//	}
	/**
	 * �����û�ѡ���Զ��Wifi�豸
	 * @return
	 */
//	public WifiDevice getSelectedWifiDevice()
//	{
//		return m_SelectedWifiDevice;
//	}
	/**
	 * ���ط��Ͷ�Wifi�豸
	 * @return
	 */
	public WifiDevice getSenderDevice()
	{
		return m_SenderDevice;
	}
	/**
	 * ���ؽ��շ�WiFi�豸
	 * @return
	 */
	public WifiDevice getReceiverDevice()
	{
		return m_ReceiverDevice;
	}
	/**
	 * ���ý��շ�WiFi�豸
	 * @param device
	 */
	public void setReceiverDevice(WifiDevice device)
	{
		m_ReceiverDevice=device;
	}
	/**
	 * �����Ƿ����ڼ���
	 * @return
	 */
	public boolean getBroadcastReceiveState()
	{
		return isListeningBroadcast;
	}
	/**
	 * �����Ƿ����ڹ㲥
	 * @return
	 */
	public boolean getBroadcastState()
	{
		return isBroadcasting;
	}
	/**
	 * �����Ƿ����ڼ�����Ϊfalseʱ��������ѭ��
	 */
	public void setBroadcastReceiveState(boolean state)
	{
		isListeningBroadcast=state;
	}
	/**
	 * �����Ƿ����ڹ㲥��Ϊfalseʱ��������ѭ��
	 */
	public void setBroadcastState(boolean state)
	{
		isBroadcasting=state;
	}
	/**
	 * �����Ƿ��н����ļ�����
	 * @return
	 */
	public boolean getFileReceiveRequestState()
	{
		return hasFileReceiveRequest;
	}
	/**
	 * �����Ƿ��н����ļ�����
	 * @param state
	 */
	public void setFileReceiveRequestState(boolean state)
	{
		hasFileReceiveRequest=state;
	}
	/**
	 * ��ȡ����˽����ļ���ѡ��
	 * @return
	 */
	public int getUserChoice()
	{
		return m_UserChoice;
	}
	/**
	 * ���÷���˽����ļ���ѡ�0Ϊ���գ�1Ϊ�����գ�
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
	 * ֹͣ����Զ���豸��UDP�㲥���ݰ�����ʵ�������ü���״̬Ϊfalse
	 */
	public void stopListeningRemoteWifiDevice()
	{
		setBroadcastReceiveState(false);
	}
	/**
	 * ֹͣ����UDP�㲥���ݰ�����ʵ�������ù㲥״̬Ϊfalse
	 */
	public void stopSendingBroadcast()
	{
		setBroadcastState(false);
	}
	/**
	 * ֹͣ�����ͻ�������
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
	 * �����Ƿ����ڼ����ͻ�������
	 * @return
	 */
	public boolean getAcceptRequestState()
	{
		return isAcceptingRequest;
	}
	/**
	 * �����Ƿ����ڼ�������Ϊfalseʱ��������ѭ��
	 * @param state
	 */
	public void setAcceptingRequestState(boolean state)
	{
		isAcceptingRequest=state;
	}
	
	
	/**
	 * ���ݾ�������IP��ַ���Mac��ַ
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
