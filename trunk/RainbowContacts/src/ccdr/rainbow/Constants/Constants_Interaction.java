package ccdr.rainbow.Constants;

/**
 * Activity��Service���н�������
 * @author Administrator
 *
 */
public class Constants_Interaction {
	/**
	 * Activity��Service���н�����Action
	 * @author Administrator
	 *
	 */
	public static class Action
	{
		public static class Bluetooth
		{
			/**
			 *  ��Զ�������豸��ʧ��
			 */
			public static final String BOND_REMOTE_DEVICE_FAILED="bondremotedeicefailed";
			
			public static class BluetoothClient
			{
				/**
				 * ���ӷ���˳ɹ�
				 */
				public static final String CONNECT_TO_SERVER_SUCESS="connecttoserversuccess";
				/**
				 * �����ļ��ɹ�
				 */
				public static final String SEND_FILE_SUCESS="sendfilesuccess";
				/**
				 * �����ļ�ʧ��
				 */
				public static final String SEND_FILE_FAILED="sendfilefailed";
				
			}
		
			public static class BluetoothServer
			{
				/**
				 * �Ҳ����ļ�����������������USB�洢
				 */
				public static final String FILE_NOT_FOUND="filenotfound";
				/**
				 * ��ʼ������ˣ����շ����Ľ�����
				 */
				public static final String INIT_BLUETOOTH_SERVER_PROGRESS_BAR="initbluetoothserverprogressbar";
				/**
				 * ���·���ˣ����շ����Ľ�����
				 */
				public static final String UPDATE_BLUETOOTH_SERVER_PROGRESS_BAR="updatebluetoothserverprogressbar";
				/**
				 * �����ļ�����ʧ��
				 */
				public static final String RECEIVE_FILE_FAILED="receivefilefailed";
				/**
				 * ����˽����ļ��ɹ�
				 */
				public static final String RECEIVE_FILE_SUCESS="receivefilesuccess";
			}
		
			public static class BluetoothPBAP
			{
				/**
				 * ��ʼ��PBAP����ɹ�
				 */
				public static final String INIT_PBAP_SERVICE_SUCCESS="initpbapservicesuccess";
				/**
				 * ��ʼ��PBAP����ʧ��
				 */
				public static final String INIT_PBAP_SERVICE_FAILED="initpbapservicefailed";
				/**
				 * ��Զ���豸PBAP��������ӳɹ�
				 */
				public static final String CONNECT_REMOTE_DEVICE_PBAP_SUCCESS="connectremotedevicesuccess";
				/**
				 * ��Զ���豸PBAP���������ʧ��
				 */
				public static final String CONNECT_REMOTE_DEVICE_PBAP_FAILED="connectremotedevicepbapfailed";
				/**
				 * ��ȡԶ���豸��ϵ������
				 */
				public static final String RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT="receiveremotedevicecontactscount";
				/**
				 * ��ȡԶ���豸��ϵ������
				 */
				public static final String RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA="receiveremotedevicephonebookdata";
				/**
				 * ��Զ�������豸���ӳɹ�
				 */
				public static final String CONNECT_REMOTE_DEVICE_SUCCESS="connectremotedevicesucess";
				/**
				 * ��Զ���豸PBAP����˶Ͽ����ӳɹ�
				 */
				public static final String DISCONNECT_REMOTE_DEVICE_SUCCESS="disconnectremotedevicesuccess";
				/**
				 * �ж�PBAP�绰������ɹ�
				 */
				public static final String ABORT_GETTING_PHONEBOOK_DATA_SUCCESS="abortgettingphonebookdatasuccess";
				/**
				 * ��ʼ��������
				 */
				public static final String INIT_BLUETOOTH_PBAP_PROGRESS_BAR="initbluetoothpbapprogressbar";
				/**
				 * ���½�����
				 */
				public static final String UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR="updatebluetoothpbapprogressbar";
			}
		}
		
		public static class Wifi
		{
			
			
			public static class WifiClient
			{
				/**
				 * �߳���δ�˳��㲥����״̬
				 */
				public static final String STILL_LISTENING_BROADCAST="stilllisteningbroadcast";
				
				/**
				 * �����ļ��ɹ�
				 */
				public static final String FILE_SEND_SUCCESS="filesendsuccess";
				/**
				 * �����ļ�ʧ��
				 */
				public static final String FILE_SEND_FAILED="filesendfailed";
				/**
				 * �����ļ����ܾ�
				 */
				public static final String FILE_SEND_REFUSE="filesendrefuse";
				/**
				 * ��������ɹ�
				 */
				public static final String FILE_SEND_ACCEPT="filesendaccept";
				/**
				 * ����Զ��WiFi�豸
				 */
				public static final String REMOTE_WIFI_DEVICE_FOUND="remotewifidevicefound";
			}
		
			public static class WifiServer
			{
				/**
				 * �����ļ��ɹ�
				 */
				public static final String FILE_RECEIVE_SUCCESS="filereceivesuccess";
				/**
				 * �����ļ�ʧ��
				 */
				public static final String FILE_RECEIVE_FAILED="filereceivefailed";
				/**
				 * �����ļ�����Ĺ㲥
				 */
				public static final String FILE_RECEIVE_REQUEST="filereceiverequest";
				/**
				 * ��Service����ʱ
				 */
				public static final String START_SERVICE_FAILED="startservicefailed";
			}
		}
	}
	/**
	 * Activity��Service���ݲ���ʱ���õ�Key
	 * @author Administrator
	 *
	 */
	public static class Key
	{
		public static class Bluetooth
		{
			/**
			 * Activity��BluetoothService����BluetoothDevice�����key
			 */
			public static final String BLUETOOTH_DEVICE="bluetoothdevice";
			/**
			 * Activity��BluetoothPBAPService���ݲ���ѡ���key
			 */
			public static final String Bluetooth_PBAP_SERVICE_OPERATION="bluetoothpbapserviceoperation";
			/**
			 * Activity��BluetoothCS���ݲ���ѡ���key
			 */
			public static final String Bluetooth_CS_SERVICE_OPERATION="bluetoothcsserviceoperation";
			
			public static class BluetoothClient
			{
				/**
				 * �����ļ�ʧ��ԭ���Key
				 */
				public static final String SEND_FILE_FAILED_REASON="sendfilefailedreason";
			}
		
			public static class BluetoothServer
			{
				/**
				 * �����ļ����ܳ���
				 */
				public static final String TOTAL_DATA_LENGTH="totaldatalength";
				/**
				 * �ѽ��յ��ļ�����
				 */
				public static final String CURRENT_DATA_LENGTH="currentdatalength";
				/**
				 * �����ļ�ʧ��ԭ��
				 */
				public static final String RECEIVE_FILE_FAILED_REASON="receivefilefailedreason";
			}
		
			public static class BluetoothPBAP
			{
				/**
				 * Զ���豸��ϵ��������key
				 */
				public static final String REMOTE_DEVICE_CONTACTS_COUNT="remotedevicecontactscount";
				/**
				 * PBAP��ȡ����ļ�����·����key
				 */
				public static final String PBAP_RECEIVE_FILE_PATH="pbapreceivefilepath";
				/**
				 * ��ǰ��ȡ������ϵ��������Key
				 */
				public static final String CURRENT_PHONEBOOK_COUNT="currentphonebookcount";
				/**
				 * ���ȡ����ϵ��������Key
				 */
				public static final String TOTAL_PHONEBOOK_COUNT="totalphonebookcount";
			}
		}
		
		public static class Wifi
		{
			/**
			 * Activity��Service����Bundle����ʱʹ�õ�key
			 */
			public static final String WIFI_SERVICE_OPERATION="wifiserviceoperation";
			/**
			 * Activity��Service����Acticity������ʱʹ�õ�key
			 */
			public static final String WIFI_ACTIVITY_CONTEXT="wifiactivitycontext";
			
			public static class WifiClient
			{
				/**
				 * ���ն˵�WiFi�豸
				 */
				public static final String SENDER_WIFI_DEVICE="senderwifidevice";
				/**
				 * ���ڰ󶨽��ն˵�WiFi�豸����Ϣ
				 */
				public static final String SENDER_WIFI_DEVICE_BUNDLE="senderwifidevicebundle";
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
				 * ���͵��ļ�·��
				 */
				public static final String SEND_FILE_PATH="sendfilepath";
				/**
				 * ���͵��ļ�����
				 */
				public static final String SEND_FILE_TYPE="sendfiletype";
			}
		
			public static class WifiServer
			{
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
				 * ���յ��ļ�·��
				 */
				public static final String RECEIVE_FILE_PATH="receivefilepath";
			}
		}
	}
	
	/**
	 * Service������ʶ����ֲ���
	 * @author Administrator
	 *
	 */
	public static class Option
	{
		public static class Bluetooth
		{
			public static class BluetoothClient
			{
			
			}
		
			public static class BluetoothServer
			{
			
			}
		
			public static class BluetoothPBAP
			{
			
			}
		}
		
		public static class Wifi
		{
			public static class WifiClient
			{
			
			}
		
			public static class WifiServer
			{
			
			}
		}
	}

}
