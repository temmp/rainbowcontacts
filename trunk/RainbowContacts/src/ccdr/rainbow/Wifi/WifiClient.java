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
	 * �������ڽ��չ㲥��DatagramSocket
	 */
	DatagramSocket dgsocket = null;
	
	public WifiClient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	///////////////////////////////////////////////////////////////////
	//////////////////////////���Ͷ�ʹ��//////////////////////////////////
	///////////////////////////////////////////////////////////////////	
	/**
	 * �����ļ�
	 */
	public int sendFile(/*File file*/String send_file_path,byte file_type,String ip_addr) {
		/**
		 * �����Ƿ���Ҫ����һ��ensurSeverConfirm������ȷ��Զ���豸��������������
		 * �����룬����һ��booleanֵ��ʶserver���Ƿ���ܣ�
		 * ����sendConnectionRequest�и��ĸ�ֵ
		 */
		//����������ѡWiFi�豸��IP��ַ���û���������WiFi�豸IP��ַ��һ���򷵻�false
//		if(!m_SelectedWifiDevice.getWifiDeviceIPAddress().equals(ip_addr))
//		{return Constants_Wifi.Transport.SELECTED_DEVICE_IP_ADDRESS_NOT_EQUAL_PARAM;}
		
		Socket client_send_data_socket=null;
		InputStream i_data_stream=null;
		OutputStream o_date_stream=null;
		FileInputStream fis=null;
		//ack������֤ͬ��
		byte[] ack=new byte[3]; 
		
		boolean send_data_failed=false;
		
		//�����������ݵ�socket
		try{
		client_send_data_socket=new Socket(ip_addr,Constants_Wifi.Port.TCP_TRANSMISSIONPORT);
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb", "�����ļ�ʱ�����׽���ʧ�ܣ�����"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb", "�����ļ�ʱ�����׽���ʧ�ܣ�����"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
	}
		
	//����д��������ݣ��������ݣ��������
	try{	
		o_date_stream=client_send_data_socket.getOutputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		send_data_failed=true;
		Log.e("bbb", "���������ʧ�ܣ�����"+e.getMessage());
		//�ͷ���Դ����
		o_date_stream=null;
		try {
			client_send_data_socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "�ر��׽���ʧ�ܣ�����"+e1.getMessage());
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
	
	//������ȡ�������ݣ��������ݣ���������
	try{
		i_data_stream=client_send_data_socket.getInputStream();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		send_data_failed=true;
		Log.e("bbb", "�����������ʧ�ܣ�����"+e.getMessage());
		//�ͷ���Դ����
		try {
			o_date_stream.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "�ر������ʧ�ܣ�����"+e1.getMessage());
			o_date_stream=null;
		}i_data_stream=null;
		try {
			client_send_data_socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("bbb", "�ر��׽���ʧ�ܣ�����"+e1.getMessage());
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
	//�������͵�VCard�ļ�������
	File file=null;
	try {
		file=new File(send_file_path);
		fis=new FileInputStream(file);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Log.e("bbb",  "�������͵�VCard�ļ�������ʧ�ܣ�����"+e.getMessage());
		return Constants_Wifi.File.VCARD_FILE_NOT_FOUND;
	}
	//VCard�ļ����ȣ�ΪʲôҪǿ��ת��Ϊint��
	int file_length=(int)file.length();
	//��int�����ļ�����ת��Ϊbyte�������ݣ����ڴ���
	byte[] file_length_byte=new byte[4];
	file_length_byte[3]=(byte)(file_length >> 24);
	file_length_byte[2]=(byte)(file_length >> 16);
	file_length_byte[1]=(byte)(file_length >> 8);
	file_length_byte[0]=(byte)(file_length);
	/**
	 * ��ʼ��������
	 */
	try
	{
	//����1�ֽڵ�����
	o_date_stream.write(file_type & 0x00FF);
	o_date_stream.flush();
	
	//����ack
	i_data_stream.read(ack, 0, 3);
	
	//����4�ֽ��ļ�����
	o_date_stream.write(file_length_byte);
	o_date_stream.flush();
	i_data_stream.read(ack, 0, 3);
	
	//�����ļ�����
	int left_file_data=file_length;//ʣ����ļ����ͳ���
	byte[] send_buffer=new byte[Constants_Wifi.Value.WIFI_BUFFER_LENGTH];//ÿ�η����������û������Ĵ�С
	
	int send_length=0;//ÿ�η������ݵĳ���
	
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
		Log.e("bbb", "�����ļ�����ʱ�����쳣������"+e.getMessage());
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
	 * ����Զ��WiFi�豸��������������豸�㲥������ӽ�List��
	 */
	public int startListeningRemoteWifiDevice() {
		// TODO Auto-generated method stub
		setBroadcastReceiveState(false);
		//���List�е�����Ԫ��
		m_WifiDeviceList.clear();
			try {
				dgsocket = new DatagramSocket(Constants_Wifi.Port.UDP_BOADCASTPORT);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "����DatagramSocketʧ�ܣ�����"+e.getMessage());
				//���ʹ�����Ϣ
			}
			if(null==dgsocket)
			{
				//����޷�����DatagramSocket������Ӧ����
				Log.e("bbb", "���ڽ��չ㲥��DatagramSocketΪnull������");
				return Constants_Wifi.Broadcast.DATAGRAMSOCKET_CREATE_FAILED;
			}
			//���ÿɽ��չ㲥
			try {
				dgsocket.setBroadcast(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("bbb", "���ý��չ㲥ʧ�ܣ�����"+e.getMessage());
				//���ʹ�����Ϣ
			}
			finally
			{}
			//����UDP�㲥���ݰ��Ļ�����
			byte[] buffer=new byte[Constants_Wifi.Value.NAME_BUFFER_LENGTH];
			//����Ϊ���ڼ���UDP�㲥���ݰ�
			setBroadcastReceiveState(true);
			while(getBroadcastReceiveState())
			{
			//����DatagramSocket��ʼ����UDP�㲥�����ݰ�
				try {
					Thread.sleep(2000);
					//�����߳��Ƿ�������
					Log.e("bbb", "�����㲥�߳�������...");
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
				Log.i("bbb", "��ʼ����Զ��WiFi�豸��UDP�㲥");
				try {
					//���ó�ʱ
					dgsocket.setSoTimeout(Constants_Wifi.Broadcast.DATAGRAMSOCKET_READ_MILLISECOND);
					//��������ó�ʱ��������ֱ�����յ�UDP���ݰ�
					dgsocket.receive(dgpacket_recv);
					//��UDP���ݰ���ȡ����
					//��ȡԶ�̹㲥�豸IP��ַ
					String ip_addr_temp=dgpacket_recv.getAddress().toString();
					String ip_addr=ip_addr_temp.substring(1, ip_addr_temp.length());
					//��ȡԶ�̹㲥�豸����
					String data=new String(dgpacket_recv.getData(),0,dgpacket_recv.getLength());
					String remote_device_name=data;
					//��ʶ��ǰ�յ���UDP�㲥�������Ƿ������б���
					boolean is_exist_device=false;
					//����List���Ƿ��Ѵ��ڸù㲥�豸��Ϣ
					for(int i=0;i<m_WifiDeviceList.size();++i)
					{
						if( m_WifiDeviceList.get(i).getWifiDeviceIPAddress().equals(ip_addr))
						{
							//���IP��ַ�����ֶ���ȣ���ʾ�豸�����б��У���ı��ʶ������Ӹ��豸���б�
							if(m_WifiDeviceList.get(i).getWifiDeviceName().equals(remote_device_name))
							{
								is_exist_device=true;
							}
							//���IP��ַ��ͬ�����ֲ���ͬ�����ʾ�豸�Ѿ��������߲��������У�ɾ����ӦIP��ַ��WifiDevice
							else{m_WifiDeviceList.remove(i);/*��Ϊ�Ƴ���һ��Ԫ�أ�����i-1����ȷ���Ƿ���ȷ*/i--;}
						}
					}
					if(!is_exist_device){m_WifiDeviceList.add(new WifiDevice(remote_device_name,ip_addr,""));}
				}
				catch(InterruptedIOException e1)
				{
					Log.e("bbb", "����UDP�㲥���ݰ���ʱ������"+e1.getMessage());
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("bbb", "����Զ���豸UDP�㲥�жϣ�����"+e.getMessage());
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
	 * ��������������Ҫ����֤��Ϊ������׼��
	 */
	public int sendConnectionRequest(/*WifiDevice wifidevice*/String ip_addr,String userdevicename) {
		//����������ѡWiFi�豸���û���������WiFi�豸��һ���򷵻�false
//		if(!m_SelectedWifiDevice.equals(wifidevice)){return Constants_Wifi.Transport.SELECTED_DEVICE_NOT_EQUAL_PARAM;}
		
//		String ip_addr=wifidevice.getWifiDeviceIPAddress();
//		String remote_device_name=wifidevice.getWifiDeviceName();
//		String mac_addr=wifidevice.getWifiDeviceMacAddress();
		
		
		InputStream i_connection_stream=null;
		OutputStream o_connection_stream=null;
		
		//��������������Ƿ���ִ���
		boolean connect_failed=false;
		/**
		 * ������Դ
		 */
		try {
			//��������������׽���
			client_send_connection_socket=new Socket(ip_addr,Constants_Wifi.Port.TCP_CONNECTPORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "��������ʱ�����׽���ʧ�ܣ�����"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("bbb", "��������ʱ�����׽���ʧ�ܣ�����"+e.getMessage());return Constants_Wifi.Transport.CREATE_SOCKET_FAILED;
		}
		
		try {
			//����д��������ݣ��������ݣ��������
			o_connection_stream=client_send_connection_socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "���������ʧ�ܣ�����"+e.getMessage());
			//�ͷ���Դ����
			o_connection_stream=null;
			try {
				client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "�ر��׽���ʧ�ܣ�����"+e1.getMessage());
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
			//������ȡ�������ݣ��������ݣ���������
			i_connection_stream=client_send_connection_socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "�����������ʧ�ܣ�����"+e.getMessage());
			//�ͷ���Դ����
			try {
				o_connection_stream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "�ر������ʧ�ܣ�����"+e1.getMessage());
				o_connection_stream=null;
			}i_connection_stream=null;
			try {
				client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "�ر��׽���ʧ�ܣ�����"+e1.getMessage());
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
		 * ��������
		 */
		try {
			//���ͱ������Ƶ���ѡWiFi�豸����������
			o_connection_stream.write(userdevicename.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "�������ݣ��������ƣ�ʧ�ܣ�����"+e.getMessage());
			try {
				o_connection_stream.close();i_connection_stream.close();client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "��������ʧ�ܺ��ͷ���Դʧ�ܣ�����"+e1.getMessage());	
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
		 * ��������
		 */
		//�û����ص�ѡ����1�ֽڱ�ʾ
		byte data=(byte) 0xFF;
		try {
			//��ȡ���շ����ص����ݣ�1�ֽڣ���ʾ�Ƿ���ձ��������ͷ������͵����ݣ��ļ���
			data=(byte) i_connection_stream.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			connect_failed=true;
			Log.e("bbb", "�������ݣ��Է�ѡ��ʧ�ܣ�����"+e.getMessage());
			try {
				o_connection_stream.close();i_connection_stream.close();client_send_connection_socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("bbb", "��������ʧ�ܺ��ͷ���Դʧ�ܣ�����"+e1.getMessage());
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
			Log.e("bbb", "�������ݳɹ����ͷ���Դʧ�ܣ�����"+e.getMessage());
			o_connection_stream=null;i_connection_stream=null;client_send_connection_socket=null;
		}
		//�ж϶Է���ѡ��
		int option=0x000000FF & data;
		//ΪO��ʾ���ܣ�Ϊ1��ʾ�ܾ�
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
