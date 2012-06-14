package ccdr.rainbow.Wifi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import ccdr.rainbow.Constants.Constants_Wifi;
import ccdr.rainbow.Tool.WifiDevice;


import android.content.Context;
import android.util.Log;

public class WifiClient extends WifiTransport{

	private Socket client_send_connection_socket=null;
	/**
	 * 创建用于接收广播的DatagramSocket
	 */
	DatagramSocket dgsocket = null;
	
	public WifiClient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	///////////////////////////////////////////////////////////////////
	//////////////////////////发送端使用//////////////////////////////////
	///////////////////////////////////////////////////////////////////	
	/**
	 * 发送文件
	 */
	public int sendFile(/*File file*/String send_file_path,byte file_type,String ip_addr) {
		/**
		 * 考虑是否需要增加一个ensurSeverConfirm函数来确认远程设备接受了连接请求
		 * 若加入，则需一个boolean值标识server端是否接受，
		 * 并在sendConnectionRequest中更改该值
		 */
		//如果保存的已选WiFi设备的IP地址与用户传过来的WiFi设备IP地址不一样则返回false
//		if(!m_SelectedWifiDevice.getWifiDeviceIPAddress().equals(ip_addr))
//		{return Constants_Wifi.Transport.SELECTED_DEVICE_IP_ADDRESS_NOT_EQUAL_PARAM;}
		
		Socket client_send_data_socket=null;
		InputStream i_data_stream=null;
		OutputStream o_date_stream=null;
		FileInputStream fis=null;
		//ack用于验证同步
		byte[] ack=new byte[3]; 
		
		boolean send_data_failed=false;
		
		//创建传输数据的socket
		try{
		client_send_data_socket=new Socket(ip_addr,Constants_Wifi.Port.TCP_TRANSMISSIONPORT);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb", "发送文件时创建套接字失败！！！"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb", "发送文件时创建套接字失败！！！"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
	}
		
	//创建写入输出数据（发送数据）的输出流
	try{	
		o_date_stream=client_send_data_socket.getOutputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		send_data_failed=true;
		Log.e("bbb", "创建输出流失败！！！"+e.getMessage());
		//释放资源返回
		o_date_stream=null;
		try {
			client_send_data_socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "关闭套接字失败！！！"+e1.getMessage());
		}
	}
	finally
		{
		if(send_data_failed)
			{
			client_send_data_socket=null;
			return Constants_Wifi.Transport.OPEN_OUTPUTSTREAM_FAILED;
			}
		}
	
	//创建读取输入数据（接收数据）的输入流
	try{
		i_data_stream=client_send_data_socket.getInputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		send_data_failed=true;
		Log.e("bbb", "创建输出入流失败！！！"+e.getMessage());
		//释放资源返回
		try {
			o_date_stream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "关闭输出流失败！！！"+e1.getMessage());
			o_date_stream=null;
		}i_data_stream=null;
		try {
			client_send_data_socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "关闭套接字失败！！！"+e1.getMessage());
		} 
	}
	finally
	{
		if(send_data_failed)
		{
			o_date_stream=null;client_send_data_socket=null;
			return Constants_Wifi.Transport.OPEN_INPUTSTREAM_FAILED;
		}
	}
	//创建发送的VCard文件输入流
	File file=null;
	try {
		file=new File(send_file_path);
		fis=new FileInputStream(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb",  "创建发送的VCard文件输入流失败！！！"+e.getMessage());
		return Constants_Wifi.File.VCARD_FILE_NOT_FOUND;
	}
	//VCard文件长度，为什么要强制转换为int？
	int file_length=(int)file.length();
	//将int类型文件长度转换为byte类型数据，用于传输
	byte[] file_length_byte=new byte[4];
	file_length_byte[3]=(byte)(file_length >> 24);
	file_length_byte[2]=(byte)(file_length >> 16);
	file_length_byte[1]=(byte)(file_length >> 8);
	file_length_byte[0]=(byte)(file_length);
	/**
	 * 开始发送数据
	 */
	try
	{
	//发送1字节的类型
	o_date_stream.write(file_type & 0x00FF);
	o_date_stream.flush();
	
	//接收ack
	i_data_stream.read(ack, 0, 3);
	
	//发送4字节文件长度
	o_date_stream.write(file_length_byte);
	o_date_stream.flush();
	i_data_stream.read(ack, 0, 3);
	
	//发送文件内容
	int left_file_data=file_length;//剩余的文件发送长度
	byte[] send_buffer=new byte[Constants_Wifi.Value.WIFI_BUFFER_LENGTH];//每次发送数据所用缓冲区的大小
	
	int send_length=0;//每次发送数据的长度
	
		while(left_file_data>0)
		{
			send_length=fis.read(send_buffer);
			o_date_stream.write(send_buffer,0,send_length);
			o_date_stream.flush();
			i_data_stream.read(ack, 0, 3);
			left_file_data-=send_length;
		}
	}
	catch (Exception e)
	{
		Log.e("bbb", "发送文件内容时发生异常！！！"+e.getMessage());
		send_data_failed=true;
	}
	finally
	{
	try {
		fis.close();
		o_date_stream.close();
		i_data_stream.close();
		client_send_data_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(send_data_failed)
		{
			fis=null;o_date_stream=null;i_data_stream=null;client_send_data_socket=null;
			return Constants_Wifi.File.SEND_FILE_CONTENT_FAILED;
		}
	}
	return Constants_Wifi.File.SEND_FILE_SUCCESS;
	}
	
	/**
	 * 监听远程WiFi设备连接情况，若有设备广播，则添加进List中
	 */
	public int startListeningRemoteWifiDevice() {
		// TODO Auto-generated method stub
		setBroadcastReceiveState(false);
		//清楚List中的所有元素
		m_WifiDeviceList.clear();
			try {
				dgsocket = new DatagramSocket(Constants_Wifi.Port.UDP_BOADCASTPORT);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "创建DatagramSocket失败！！！"+e.getMessage());
				//发送错误消息
			}
			if(null==dgsocket)
			{
				//如果无法创建DatagramSocket则做相应处理
				Log.e("bbb", "用于接收广播的DatagramSocket为null！！！");
				return Constants_Wifi.Broadcast.DATAGRAMSOCKET_CREATE_FAILED;
			}
			//设置可接收广播
			try {
				dgsocket.setBroadcast(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "设置接收广播失败！！！"+e.getMessage());
				//发送错误消息
			}
			finally
			{}
			//接收UDP广播数据包的缓冲区
			byte[] buffer=new byte[Constants_Wifi.Value.NAME_BUFFER_LENGTH];
			//设置为正在监听UDP广播数据包
			setBroadcastReceiveState(true);
			while(getBroadcastReceiveState())
			{
			//创建DatagramSocket后开始监听UDP广播的数据包
				try {
					Thread.sleep(2000);
					//测试线程是否在运行
					Log.e("bbb", "监听广播线程运行中...");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("bbb", e.getMessage());
					if(dgsocket!=null)
					{
					dgsocket.close();
					dgsocket=null;
					setBroadcastReceiveState(false);
					return Constants_Wifi.Broadcast.INTERRUPTED_WHILE_RECEIVING;
					}
				}
				DatagramPacket dgpacket_recv=new DatagramPacket(buffer,buffer.length);
				Log.i("bbb", "开始监听远程WiFi设备的UDP广播");
				try {
					//设置超时
					dgsocket.setSoTimeout(Constants_Wifi.Broadcast.DATAGRAMSOCKET_READ_MILLISECOND);
					//如果不设置超时，则阻塞直到接收到UDP数据包
					dgsocket.receive(dgpacket_recv);
					//从UDP数据包读取数据
					//获取远程广播设备IP地址
					String ip_addr_temp=dgpacket_recv.getAddress().toString();
					String ip_addr=ip_addr_temp.substring(1, ip_addr_temp.length());
					//获取远程广播设备名称
					String data=new String(dgpacket_recv.getData(),0,dgpacket_recv.getLength());
					String remote_device_name=data;
					//标识当前收到的UDP广播发送者是否已在列表中
					boolean is_exist_device=false;
					//查找List中是否已存在该广播设备信息
					for(int i=0;i<m_WifiDeviceList.size();++i)
					{
						if( m_WifiDeviceList.get(i).getWifiDeviceIPAddress().equals(ip_addr))
						{
							//如果IP地址和名字都相等，表示设备已在列表中，则改变标识，不添加该设备到列表
							if(m_WifiDeviceList.get(i).getWifiDeviceName().equals(remote_device_name))
							{
								is_exist_device=true;
							}
							//如果IP地址相同，名字不相同，则表示设备已经改名或者不在网段中，删除相应IP地址的WifiDevice
							else{m_WifiDeviceList.remove(i);/*因为移除了一个元素，所以i-1，不确定是否正确*/i--;}
						}
					}
					if(!is_exist_device){m_WifiDeviceList.add(new WifiDevice(remote_device_name,ip_addr,""));}
				}
				catch(InterruptedIOException e1)
				{
					Log.e("bbb", "监听UDP广播数据包超时！！！"+e1.getMessage());
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("bbb", "监听远程设备UDP广播中断！！！"+e.getMessage());
				}   
			}
			if(dgsocket!=null)
			{
				dgsocket.close();
				dgsocket=null;
			}
			setBroadcastReceiveState(false);
			return Constants_Wifi.Broadcast.RECEIVE_DATAGRAMPACKET_SUCCESS;
	}
	/**
	 * 发送连接请求，主要是验证，为传输做准备
	 */
	public int sendConnectionRequest(/*WifiDevice wifidevice*/String ip_addr,String userdevicename) {
		//如果保存的已选WiFi设备与用户传过来的WiFi设备不一样则返回false
//		if(!m_SelectedWifiDevice.equals(wifidevice)){return Constants_Wifi.Transport.SELECTED_DEVICE_NOT_EQUAL_PARAM;}
		
//		String ip_addr=wifidevice.getWifiDeviceIPAddress();
//		String remote_device_name=wifidevice.getWifiDeviceName();
//		String mac_addr=wifidevice.getWifiDeviceMacAddress();
		
		
		InputStream i_connection_stream=null;
		OutputStream o_connection_stream=null;
		
		//发送请求过程中是否出现错误
		boolean connect_failed=false;
		/**
		 * 创建资源
		 */
		try {
			//创建发送请求的套接字
			client_send_connection_socket=new Socket(ip_addr,Constants_Wifi.Port.TCP_CONNECTPORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "请求连接时创建套接字失败！！！"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "请求连接时创建套接字失败！！！"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
		}
		
		try {
			//创建写入输出数据（发送数据）的输出流
			o_connection_stream=client_send_connection_socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "创建输出流失败！！！"+e.getMessage());
			//释放资源返回
			o_connection_stream=null;
			try {
				client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "关闭套接字失败！！！"+e1.getMessage());
			}
		}
		finally
			{
			if(connect_failed)
				{
				client_send_connection_socket=null;
				return Constants_Wifi.Transport.OPEN_OUTPUTSTREAM_FAILED;
				}
			}
		
		try {
			//创建读取输入数据（接收数据）的输入流
			i_connection_stream=client_send_connection_socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "创建输出入流失败！！！"+e.getMessage());
			//释放资源返回
			try {
				o_connection_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "关闭输出流失败！！！"+e1.getMessage());
				o_connection_stream=null;
			}i_connection_stream=null;
			try {
				client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "关闭套接字失败！！！"+e1.getMessage());
			} 
		}
		finally
		{
			if(connect_failed)
			{
				o_connection_stream=null;client_send_connection_socket=null;
				return Constants_Wifi.Transport.OPEN_INPUTSTREAM_FAILED;
			}
		}
		/**
		 * 发送数据
		 */
		try {
			//发送本机名称到已选WiFi设备，阻塞运行
			o_connection_stream.write(userdevicename.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "发送数据（本机名称）失败！！！"+e.getMessage());
			try {
				o_connection_stream.close();i_connection_stream.close();client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "发送数据失败后释放资源失败！！！"+e1.getMessage());	
			}
		}
		finally
		{
			if(connect_failed)
			{
				o_connection_stream=null;i_connection_stream=null;client_send_connection_socket=null;
				return Constants_Wifi.Transport.SEND_DATA_TO_REMOTE_DEVICE_FAILED;
			}
		}
		/**
		 * 接收数据
		 */
		//用户返回的选择，用1字节表示
		byte data=(byte) 0xFF;
		try {
			//读取接收方返回的数据，1字节，表示是否接收本机（发送方）发送的数据（文件）
			data=(byte) i_connection_stream.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "接收数据（对方选择）失败！！！"+e.getMessage());
			try {
				o_connection_stream.close();i_connection_stream.close();client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "接收数据失败后释放资源失败！！！"+e1.getMessage());
			}
		}
		finally
		{
			if(connect_failed)
			{
			o_connection_stream=null;i_connection_stream=null;client_send_connection_socket=null;
			return Constants_Wifi.Transport.RECEIVE_REMOTE_DATA_FAILED;
			}
		}
		try {
			o_connection_stream.close();i_connection_stream.close();client_send_connection_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "接收数据成功后释放资源失败！！！"+e.getMessage());
			o_connection_stream=null;i_connection_stream=null;client_send_connection_socket=null;
		}
		//判断对方的选择
		int option=0x000000FF & data;
		//为O表示接受，为1表示拒绝
		if(0==option)
		{
			return Constants_Wifi.Transport.REMOTE_DEVICE_ACCEPT;
		}
		else if(1==option)
		{
			return Constants_Wifi.Transport.REMOTE_DEVICE_REFUSE;
		}
		return Constants_Wifi.Transport.REMOTE_DEVICE_ACCEPT;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if(null!=dgsocket)
		{
			dgsocket.close();
		}
		if(null!=client_send_connection_socket)
		{
			try {
				client_send_connection_socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
