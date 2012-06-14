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
	//////////////////////////接收端使用//////////////////////////////////
	///////////////////////////////////////////////////////////////////
	/**
	 * 广播UDP数据包，数据中是本地设备名字
	 * @return
	 */
	public int startSendingUDPBroadcast()   {
		// TODO Auto-generated method stub
		setBroadcastState(false);
		boolean send_broadcast_failed=false;
		//创建UDP套接字
//		DatagramSocket ds;
		try {
			ds = new DatagramSocket(Constants_Wifi.Port.UDP_BOADCASTPORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new SocketException("创建UDP套接字失败！"+e.getMessage());
			Log.e("bbb", "创建UDP套接字失败！"+e.getMessage());
			return Constants_Wifi.Broadcast.DATAGRAMSOCKET_CREATE_FAILED;
		}
		try
		{
		ds.setBroadcast(true);
		//设置正在广播
		setBroadcastState(true);
		while(isBroadcasting)
		{
			//把本机名称转换为byte类型用于传输
			byte[] user_device_name_byte=m_UserDeviceName.getBytes();
			
			DatagramPacket dp=new DatagramPacket(user_device_name_byte,
					user_device_name_byte.length,InetAddress.getByName("255.255.255.255"),
					Constants_Wifi.Port.UDP_BOADCASTPORT);
			//广播本机名称
			ds.send(dp);
			Thread.sleep(Constants_Wifi.Broadcast.DATAGRAMSOCKET_SEND_MILLISECOND);
		}
		}
		catch (Exception e)
		{
			Log.e("bbb", e.getMessage());
//			throw new IOException("广播过程中发生异常！"+e.getMessage());
			Log.e("bbb", "广播过程中发生异常！"+e.getMessage());
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
	 * 监听并接收客户端的请求信息
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
			Log.e("bbb", "服务端套接字创建失败！！！"+e1.getMessage());
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.CREATE_SERVER_SOCKET_FAILED;
		}
		//监听直到客户端发送连接请求
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
		//接收一个请求后关闭服务端套接字
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
			Log.e("bbb", "打开输入输出流失败！！！"+e.getMessage());
			try {
				m_ServerConnectSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setAcceptingRequestState(false);
			return Constants_Wifi.Transport.OPEN_IOSTREAM_FAILED;
		}
		//获取客户端用户名
		byte[] remote_device_name_byte=new byte[Constants_Wifi.Value.NAME_BUFFER_LENGTH];
		try {
			is_recv_data.read(remote_device_name_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "获取客户端用户名失败！！！"+e.getMessage());
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
		//获取客户端设备名称
		String m_SenderDeviceName=new String(remote_device_name_byte);
		//获取客户端IP地址
		String m_SenderIPAddress=accept_connect_socket.getInetAddress().toString().substring(1);
		m_SenderDevice=new WifiDevice(m_SenderDeviceName
				,m_SenderIPAddress
				,getMacFromArpCache(m_SenderIPAddress));
		//终止广播
		setBroadcastState(false);
		Log.i("bbb", getMacFromArpCache(m_SenderDevice.getWifiDeviceIPAddress()));
		
		/**
		 * 这里让用户做出选择
		 */
		hasFileReceiveRequest=true;
		Log.i("bbb", "接收到请求");
		
		
		while(getUserChoice()==-1)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "接收方做选择时被打断！！！"+e.getMessage());
				setAcceptingRequestState(false);
				return Constants_Wifi.Transport.INTERRUPTED_WHILE_CHOOSING;
			}
		}
		//用户选择接收或者拒绝
		int user_choice=getUserChoice();
		byte user_choice_byte=(byte)(user_choice & 0x000000FF);
		//把选择设置为-1，不可用
		setUserChoice(-1);
		try {
			os_send_data.write(user_choice_byte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "发送用户选择到发送端失败！！！"+e.getMessage());
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
	 * 接收文件
	 * @param recv_file_path
	 * @throws IOException 
	 */
	public void receiveFile(String recv_file_path) throws IOException
	{
		Log.i("bbb", "开始接收");
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
		
		//获取文件长度
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
		
		//准备接收文件
		byte[] recv_buffer=new byte[Constants_Wifi.Value.WIFI_BUFFER_LENGTH];
		int left_file_data=file_length;
		int recv_length=0;
		//开始接收文件
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
		Log.i("bbb", "接收完毕");
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
