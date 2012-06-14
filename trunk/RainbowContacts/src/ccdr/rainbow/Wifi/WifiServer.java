package ccdr.rainbow.Wifi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Tool.WifiDevice;

import android.content.Context;
import android.util.Log;

public class WifiServer extends WifiTransport{
	
	private DatagramSocket ds=null;

	
	public WifiServer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	///////////////////////////////////////////////////////////////////
	//////////////////////////���ն�ʹ��//////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * �㲥UDP���ݰ����������Ǳ����豸����
	 * @return
	 */
	public int startSendingUDPBroadcast()   {
		// TODO Auto-generated method stub
		setBroadcastState(false);
		boolean send_broadcast_failed=false;
		//����UDP�׽���
//		DatagramSocket ds;
		try {
			ds = new DatagramSocket(Constants_Wifi.Port.UDP_BOADCASTPORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new SocketException("����UDP�׽���ʧ�ܣ�"+e.getMessage());
			Log.e("bbb", "����UDP�׽���ʧ�ܣ�"+e.getMessage());
			return Constants_Wifi.Broadcast.DATAGRAMSOCKET_CREATE_FAILED;
		}
		try
		{
		ds.setBroadcast(true);
		//�������ڹ㲥
		setBroadcastState(true);
		while(isBroadcasting)
		{
			//�ѱ�������ת��Ϊbyte�������ڴ���
			byte[] user_device_name_byte=m_UserDeviceName.getBytes();
			
			DatagramPacket dp=new DatagramPacket(user_device_name_byte,
					user_device_name_byte.length,InetAddress.getByName("255.255.255.255"),
					Constants_Wifi.Port.UDP_BOADCASTPORT);
			//�㲥��������
			ds.send(dp);
			Thread.sleep(Constants_Wifi.Broadcast.DATAGRAMSOCKET_SEND_MILLISECOND);
		}
		}
		catch (Exception e)
		{
			Log.e("bbb", e.getMessage());
//			throw new IOException("�㲥�����з����쳣��"+e.getMessage());
			Log.e("bbb", "�㲥�����з����쳣��"+e.getMessage());
			send_broadcast_failed=true;	
		}
		finally
		{
			ds.close();
			ds=null;
			if(send_broadcast_failed)
			{
				return Constants_Wifi.Signal.SEND_BRODCAST_EXCEPTION;
			}
		}
		return Constants_Wifi.Signal.SEND_BROADCAST_SUCCESS;
	}
	
	/**
	 * ���������տͻ��˵�������Ϣ
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int acceptConnectionRequest()
	{
		setAcceptingRequestState(true);
		try {
			m_ServerConnectSocket=new ServerSocket(Constants_Wifi.Port.TCP_CONNECTPORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "������׽��ִ���ʧ�ܣ�����"+e1.getMessage());
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.CREATE_SERVER_SOCKET_FAILED;
		}
		//����ֱ���ͻ��˷�����������
		Socket accept_connect_socket;
		try {
			accept_connect_socket = m_ServerConnectSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				m_ServerConnectSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.ACCEPT_CLIENT_CONNECTION_FAILED;
		}
		//����һ�������رշ�����׽���
		try {
			m_ServerConnectSocket.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Log.e("bbb", e2.getMessage());
			m_ServerConnectSocket=null;
		}
		
		InputStream is_recv_data;
		OutputStream os_send_data;
		try {
			is_recv_data = accept_connect_socket.getInputStream();
			os_send_data=accept_connect_socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "�����������ʧ�ܣ�����"+e.getMessage());
			try {
				m_ServerConnectSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.OPEN_IOSTREAM_FAILED;
		}
		//��ȡ�ͻ����û���
		byte[] remote_device_name_byte=new byte[Constants_Wifi.Value.NAME_BUFFER_LENGTH];
		try {
			is_recv_data.read(remote_device_name_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "��ȡ�ͻ����û���ʧ�ܣ�����"+e.getMessage());
			try {
				is_recv_data.close();
				os_send_data.close();
				accept_connect_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				is_recv_data=null;os_send_data=null;accept_connect_socket=null;
			}
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.RECEIVE_REMOTE_DATA_FAILED;
		}
		//��ȡ�ͻ����豸����
		String m_SenderDeviceName=new String(remote_device_name_byte);
		//��ȡ�ͻ���IP��ַ
		String m_SenderIPAddress=accept_connect_socket.getInetAddress().toString().substring(1);
		m_SenderDevice=new WifiDevice(m_SenderDeviceName
				,m_SenderIPAddress
				,getMacFromArpCache(m_SenderIPAddress));
		//��ֹ�㲥
		setBroadcastState(false);
		Log.i("bbb", getMacFromArpCache(m_SenderDevice.getWifiDeviceIPAddress()));
		
		/**
		 * �������û�����ѡ��
		 */
		hasFileReceiveRequest=true;
		Log.i("bbb", "���յ�����");
		
		
		while(getUserChoice()==-1)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "���շ���ѡ��ʱ����ϣ�����"+e.getMessage());
				setAcceptingRequestState(false);
				return Constants_Wifi.Transport.INTERRUPTED_WHILE_CHOOSING;
			}
		}
		//�û�ѡ����ջ��߾ܾ�
		int user_choice=getUserChoice();
		byte user_choice_byte=(byte)(user_choice & 0x000000FF);
		//��ѡ������Ϊ-1��������
		setUserChoice(-1);
		try {
			os_send_data.write(user_choice_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "�����û�ѡ�񵽷��Ͷ�ʧ�ܣ�����"+e.getMessage());
		}
		
		try {
			is_recv_data.close();
			os_send_data.close();
			accept_connect_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			is_recv_data=null;
			os_send_data=null;
			accept_connect_socket=null;
		}
		setAcceptingRequestState(false);
		return Constants_Wifi.Transport.ACCEPT_CLIENT_CONNECTION_SUCCESS;
	}
	
	/**
	 * �����ļ�
	 * @param recv_file_path
	 * @throws IOException 
	 */
	public void receiveFile(String recv_file_path) throws IOException
	{
		Log.i("bbb", "��ʼ����");
		String file_path;
		m_ServerTransportSocket=new ServerSocket(Constants_Wifi.Port.TCP_TRANSMISSIONPORT);
		
		Socket accept_transport_socket=m_ServerTransportSocket.accept();
		
		InputStream is_recv_data=accept_transport_socket.getInputStream();
		OutputStream os_send_data=accept_transport_socket.getOutputStream();
		
		byte file_type=(byte) is_recv_data.read();
		switch(file_type)
		{
		case Constants_Wifi.File.FILE_TYPE_VCARD:file_path=recv_file_path+Constants_Wifi.File.FILE_SUFFIX_VCARD;break;
		case Constants_Wifi.File.FILE_TYPE_SMS:break;
		case Constants_Wifi.File.FILE_TYPE_VCAL:break;
		
		default:break;
		}
		os_send_data.write(ack);
		
		//��ȡ�ļ�����
		byte[] file_length_byte=new byte[4];
		is_recv_data.read(file_length_byte);
		Log.i("bbb", String.valueOf(file_length_byte));
		int file_length=file_length_byte[3]<<24;
		file_length+=(0x00FF0000 &file_length_byte[2]<<16);
		file_length+=(0x0000FF00 &file_length_byte[1]<<8);
		file_length+=(0x000000FF &file_length_byte[0]);

		Log.i("bbb", String.valueOf(file_length));
		os_send_data.write(ack);
		
		FileOutputStream fos_recv=new FileOutputStream(new File(recv_file_path));
		
		//׼�������ļ�
		byte[] recv_buffer=new byte[Constants_Wifi.Value.WIFI_BUFFER_LENGTH];
		int left_file_data=file_length;
		int recv_length=0;
		//��ʼ�����ļ�
		while(left_file_data>0)
		{
			recv_length=is_recv_data.read(recv_buffer);
			os_send_data.write(ack);
			left_file_data-=recv_length;
			
			fos_recv.write(recv_buffer, 0, recv_length);
		}
		fos_recv.close();
		is_recv_data.close();
		os_send_data.close();
		accept_transport_socket.close();
		m_ServerTransportSocket.close();
		Log.i("bbb", "�������");
	}
	
	@Override
	public void close()
	{
		if(null!=ds){ds.close();}
		if(null!=m_ServerConnectSocket){
			try {
			m_ServerConnectSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
		if(null!=m_ServerTransportSocket)
		{
			try {
				m_ServerTransportSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
