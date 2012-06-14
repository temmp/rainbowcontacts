package ccdr.rainbow.Constants;





import android.os.Environment;

public class Constants_Wifi {
	public class Signal
	{
		/**
		 * ���͹㲥�ɹ�
		 */
		public static final int SEND_BROADCAST_SUCCESS=50;
		/**
		 * ���͹㲥�쳣
		 */
		public static final int SEND_BRODCAST_EXCEPTION=51;
	}
	
	public class Port
	{
		public static final int TCP_TRANSMISSIONPORT = 4100;// ���ڴ������ݵĶ˿�
		public static final int UDP_BOADCASTPORT = 4200;// ���ڹ㲥IP��ַ�Ķ˿�
		public static final int TCP_CONNECTPORT = 4300;// ����˫�������������Ķ˿�
	}
	
	public class Broadcast
	{
		///////////////////����չ㲥��صı���///////////////////////
		/**
		 * ����UDP�㲥���ݰ��ĳ�ʱʱ��
		 */
		public static final int DATAGRAMSOCKET_READ_MILLISECOND=4000;
		/**
		 * ÿ�ι㲥UDP���ݰ��ļ��ʱ��
		 */
		public static final int DATAGRAMSOCKET_SEND_MILLISECOND=1000;
		/**
		 * ѭ������UDP�㲥���ݰ�ʱ��������
		 */
		public static final int RECEIVE_DATAGRAMPACKET_SUCCESS=10;
		/**
		 * ����DatagramSocketʧ�ܣ�Ϊnull��
		 */
		public static final int DATAGRAMSOCKET_CREATE_FAILED=11;
		/**
		 * ѭ������UDP�㲥���ݰ�ʱ�����
		 */
		public static final int INTERRUPTED_WHILE_RECEIVING=12;
	}
	
	public class Transport
	{
		///////////////////���ݴ���ǰ���Ͷ���֤�������ر���/////////////////
		/**
		 * Զ�̣��Է����ֻ���������
		 */
		public static final int REMOTE_DEVICE_ACCEPT=20;
		/**
		 * Զ�̣��Է����ֻ��ܾ�����
		 */
		public static final int REMOTE_DEVICE_REFUSE=21;
		/**
		 * ����Զ���ֻ�����ʧ��
		 */
		public static final int RECEIVE_REMOTE_DATA_FAILED=22;
		/**
		 * �������ݵ�Զ���ֻ�ʧ��
		 */
		public static final int SEND_DATA_TO_REMOTE_DEVICE_FAILED=23;
		/**
		 * ��������ʧ��
		 */
		public static final int OPEN_INPUTSTREAM_FAILED=24;
		/**
		 * �������ʧ��
		 */
		public static final int OPEN_OUTPUTSTREAM_FAILED=25;
		/**
		 * �����׽���ʧ��
		 */
		public static final int CREATE_SOCKET_FAILED=26;
		/**
		 * ����������׽���ʧ��
		 */
		public static final int CREATE_SERVER_SOCKET_FAILED=27;
//		/**
//		 * ���ر����Զ��WiFi�豸�������û�ѡȡ���豸����ͬ
//		 */
//		public static final int SELECTED_DEVICE_NOT_EQUAL_PARAM=28;
//		/**
//		 * ���ر����Զ��WiFi�豸��IP��ַ���û�ѡȡ��IP��ַ��ͬ
//		 */
//		public static final int SELECTED_DEVICE_IP_ADDRESS_NOT_EQUAL_PARAM=29;
		/**
		 * ����˽��շ��Ͷ�����ɹ�
		 */
		public static final int ACCEPT_CLIENT_CONNECTION_FAILED=30;
		/**
		 * ����˽��շ��Ͷ�����ʧ��
		 */
		public static final int ACCEPT_CLIENT_CONNECTION_SUCCESS=31;
		/**
		 * ���׽��ֵ����������ʧ��
		 */
		public static final int OPEN_IOSTREAM_FAILED=31;
		/**
		 * �û������Ƿ�����ļ�ʱ�����
		 */
		public static final int INTERRUPTED_WHILE_CHOOSING=32;
	}
	
	public class File
	{
		///////////////////���ͺͽ����ļ�����ر���/////////////////
		/**
		 * �绰���ļ�����
		 */
		public static final byte FILE_TYPE_VCARD=(byte)0xE0;
		/**
		 * �����ļ�����
		 */
		public static final byte FILE_TYPE_SMS=(byte)0xE1;
		/**
		 * �绰�ļ�����
		 */
		public static final byte FILE_TYPE_VCAL=(byte)0xE2;
		/**
		 * �绰���ļ���׺
		 */
		public static final String FILE_SUFFIX_VCARD=".vcf";
		/**
		 * �޷��򿪱���VCard�ļ����ļ�������
		 */
		public static final int VCARD_FILE_NOT_FOUND=29;
		/**
		 * �����ļ��ɹ�
		 */
		public static final int SEND_FILE_SUCCESS=40;
		/**
		 * �����ļ�ʧ��
		 */
		public static final int SEND_FILE_CONTENT_FAILED=41;
	}
	
	
	public class Key
	{
	/**
	 * Activity��Service����Bundle����ʱʹ�õ�key
	 */
	public static final String WIFI_SERVICE_OPERATION="wifiserviceoperation";
	/**
	 * Activity��Service����Acticity������ʱʹ�õ�key
	 */
	public static final String WIFI_ACTIVITY_CONTEXT="wifiactivitycontext";
	/**
	 * ���Ͷ�WiFi�豸����
	 */
	public static final String SENDER_WIFI_DEVICE_NAME="senderwifidevicename";
	/**
	 * ���ն�WiFi�豸IP��ַ
	 */
	public static final String SENDER_WIFI_DEVICE_IPADDRESS="senderwifideviceipaddress";
	/**
	 * ���ն�WiFi�豸MAC��ַ
	 */
	public static final String SENDER_WIFI_DEVICE_MACADDRESS="senderwifidevicemacaddress";
	/**
	 * ���Ͷ�WiFi�豸����
	 */
	public static final String RECEIVER_WIFI_DEVICE_NAME="receiverwifidevicename";
	/**
	 * ���ն�WiFi�豸IP��ַ
	 */
	public static final String RECEIVER_WIFI_DEVICE_IPADDRESS="receiverwifideviceipaddress";
	/**
	 * ���ն�WiFi�豸MAC��ַ
	 */
	public static final String RECEIVER_WIFI_DEVICE_MACADDRESS="receiverwifidevicemacaddress";
	/**
	 * ���͵��ļ�·��
	 */
	public static final String SEND_FILE_PATH="sendfilepath";
	/**
	 * ���͵��ļ�����
	 */
	public static final String SEND_FILE_TYPE="sendfiletype";
	/**
	 * ���յ��ļ�·��
	 */
	public static final String RECEIVE_FILE_PATH="receivefilepath";
	}
	
	
	/**
	 * ����Զ��WiFi�豸��Service��onStartCommand�������в����жϵ�ֵ��
	 */
	public static final int LISTEN_REMOTE_WIFI_DEVICES=100;
	/**
	 * ��ȡԶ��WiFi�豸��Service��onStartCommand�������в����жϵ�ֵ��
	 */
	public static final int GET_REMOTE_WIFI_DEVICES=101;
	/**
	 * ֹͣ����Զ��WiFi�豸��Service��onStartCommand�������в����жϵ�ֵ��
	 */
	public static final int STOP_LISTENING_REMOTE_WIFI_DEVICES=102;
	/**
	 * ��ȡԶ��WiFi�豸������Service��onStartCommand�������в����жϵ�ֵ��
	 */
	public static final int GET_REMOTE_WIFI_DEVICES_COUNT=103;
	/**
	 * ���ն˿�ʼ�㲥
	 */
	public static final int START_SENDING_BROADCAST=104;
	/**
	 * ���ն�ֹͣ�㲥
	 */
	public static final int STOP_SENDING_BROADCAST=105;
	/**
	 * ���ն˽�������
	 */
	public static final int START_ACCEPT_REQUEST=106;
	/**
	 * ֹͣ��������
	 */
	public static final int STOP_ACCEPT_REQUEST=107;
	/**
	 * �����ļ�
	 */
	public static final int SEND_FILE=108;
	/**
	 * �����ļ�
	 */
	public static final int RECEIVE_FILE=109;
	/**
	 * ��������
	 */
	public static final int SEND_REQUEST=110;
	/**
	 * ֹͣ��ȡԶ��WiFi�豸��Service��onStartCommand�������в����жϵ�ֵ��
	 */
	public static final int STOP_GETTING_REMOTE_WIFI_DEVICES=111;
	
	
	
//	public class Action
//	{
//	/**
//	 * �߳���δ�˳��㲥����״̬
//	 */
//	public static final String STILL_LISTENING_BROADCAST="stilllisteningbroadcast";
//	/**
//	 * �����ļ�����Ĺ㲥
//	 */
//	public static final String FILE_RECEIVE_REQUEST="filereceiverequest";
//	/**
//	 * �����ļ��ɹ�
//	 */
//	public static final String FILE_SEND_SUCESS="filesendsuccess";
//	/**
//	 * �����ļ�ʧ��
//	 */
//	public static final String FILE_SEND_FAILED="filesendfailed";
//	/**
//	 * �����ļ����ܾ�
//	 */
//	public static final String FILE_SEND_REFUSE="filesendrefuse";
//	/**
//	 * �����ļ��ɹ�
//	 */
//	public static final String FILE_RECEIVE_SUCCESS="filereceivesuccess";
//	/**
//	 * �����ļ�ʧ��
//	 */
//	public static final String FILE_RECEIVE_FAILED="filereceivefailed";
//	/**
//	 * ��Service����ʱ
//	 */
//	public static final String START_SERVICE_FAILED="startservicefailed";
//	}
	
//	public class Path
//	{
//		public final String SDROOTPATH = Environment
//				.getExternalStorageDirectory().getAbsolutePath();
//		public final String WIFISENDPATH = SDROOTPATH + java.io.File.separator
//				+ "WifiSend.vcf";
//
//		public final String WIFIRECEIVEPATH = SDROOTPATH + java.io.File.separator
//				+ "WifiReceive.vcf";
//	}
	
	public class Value
	{
		public static final int WIFI_BUFFER_LENGTH = 1024;
		public static final int NAME_BUFFER_LENGTH = 8192;
	}
}
