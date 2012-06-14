package ccdr.rainbow.Constants;

/**
 * Activity与Service进行交互的类
 * @author Administrator
 *
 */
public class Constants_Interaction {
	/**
	 * Activity与Service进行交互的Action
	 * @author Administrator
	 *
	 */
	public static class Action
	{
		public static class Bluetooth
		{
			/**
			 *  与远程蓝牙设备绑定失败
			 */
			public static final String BOND_REMOTE_DEVICE_FAILED="bondremotedeicefailed";
			
			public static class BluetoothClient
			{
				/**
				 * 连接服务端成功
				 */
				public static final String CONNECT_TO_SERVER_SUCESS="connecttoserversuccess";
				/**
				 * 发送文件成功
				 */
				public static final String SEND_FILE_SUCESS="sendfilesuccess";
				/**
				 * 发送文件失败
				 */
				public static final String SEND_FILE_FAILED="sendfilefailed";
				
			}
		
			public static class BluetoothServer
			{
				/**
				 * 找不到文件，可能是正在连接USB存储
				 */
				public static final String FILE_NOT_FOUND="filenotfound";
				/**
				 * 初始化服务端（接收方）的进度条
				 */
				public static final String INIT_BLUETOOTH_SERVER_PROGRESS_BAR="initbluetoothserverprogressbar";
				/**
				 * 更新服务端（接收方）的进度条
				 */
				public static final String UPDATE_BLUETOOTH_SERVER_PROGRESS_BAR="updatebluetoothserverprogressbar";
				/**
				 * 接收文件过程失败
				 */
				public static final String RECEIVE_FILE_FAILED="receivefilefailed";
				/**
				 * 服务端接收文件成功
				 */
				public static final String RECEIVE_FILE_SUCESS="receivefilesuccess";
			}
		
			public static class BluetoothPBAP
			{
				/**
				 * 初始化PBAP服务成功
				 */
				public static final String INIT_PBAP_SERVICE_SUCCESS="initpbapservicesuccess";
				/**
				 * 初始化PBAP服务失败
				 */
				public static final String INIT_PBAP_SERVICE_FAILED="initpbapservicefailed";
				/**
				 * 与远程设备PBAP服务端连接成功
				 */
				public static final String CONNECT_REMOTE_DEVICE_PBAP_SUCCESS="connectremotedevicesuccess";
				/**
				 * 与远程设备PBAP服务端连接失败
				 */
				public static final String CONNECT_REMOTE_DEVICE_PBAP_FAILED="connectremotedevicepbapfailed";
				/**
				 * 获取远程设备联系人数量
				 */
				public static final String RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT="receiveremotedevicecontactscount";
				/**
				 * 获取远程设备联系人数据
				 */
				public static final String RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA="receiveremotedevicephonebookdata";
				/**
				 * 与远程蓝牙设备连接成功
				 */
				public static final String CONNECT_REMOTE_DEVICE_SUCCESS="connectremotedevicesucess";
				/**
				 * 与远程设备PBAP服务端断开连接成功
				 */
				public static final String DISCONNECT_REMOTE_DEVICE_SUCCESS="disconnectremotedevicesuccess";
				/**
				 * 中断PBAP电话簿传输成功
				 */
				public static final String ABORT_GETTING_PHONEBOOK_DATA_SUCCESS="abortgettingphonebookdatasuccess";
				/**
				 * 初始化进度条
				 */
				public static final String INIT_BLUETOOTH_PBAP_PROGRESS_BAR="initbluetoothpbapprogressbar";
				/**
				 * 更新进度条
				 */
				public static final String UPDATE_BLUETOOTH_PBAP_PROGRESS_BAR="updatebluetoothpbapprogressbar";
			}
		}
		
		public static class Wifi
		{
			
			
			public static class WifiClient
			{
				/**
				 * 线程尚未退出广播监听状态
				 */
				public static final String STILL_LISTENING_BROADCAST="stilllisteningbroadcast";
				
				/**
				 * 发送文件成功
				 */
				public static final String FILE_SEND_SUCCESS="filesendsuccess";
				/**
				 * 发送文件失败
				 */
				public static final String FILE_SEND_FAILED="filesendfailed";
				/**
				 * 发送文件被拒绝
				 */
				public static final String FILE_SEND_REFUSE="filesendrefuse";
				/**
				 * 发送请求成功
				 */
				public static final String FILE_SEND_ACCEPT="filesendaccept";
				/**
				 * 发现远程WiFi设备
				 */
				public static final String REMOTE_WIFI_DEVICE_FOUND="remotewifidevicefound";
			}
		
			public static class WifiServer
			{
				/**
				 * 接收文件成功
				 */
				public static final String FILE_RECEIVE_SUCCESS="filereceivesuccess";
				/**
				 * 接收文件失败
				 */
				public static final String FILE_RECEIVE_FAILED="filereceivefailed";
				/**
				 * 发送文件请求的广播
				 */
				public static final String FILE_RECEIVE_REQUEST="filereceiverequest";
				/**
				 * 打开Service服务超时
				 */
				public static final String START_SERVICE_FAILED="startservicefailed";
			}
		}
	}
	/**
	 * Activity与Service传递参数时所用的Key
	 * @author Administrator
	 *
	 */
	public static class Key
	{
		public static class Bluetooth
		{
			/**
			 * Activity与BluetoothService传递BluetoothDevice对象的key
			 */
			public static final String BLUETOOTH_DEVICE="bluetoothdevice";
			/**
			 * Activity与BluetoothPBAPService传递操作选项的key
			 */
			public static final String Bluetooth_PBAP_SERVICE_OPERATION="bluetoothpbapserviceoperation";
			/**
			 * Activity与BluetoothCS传递操作选项的key
			 */
			public static final String Bluetooth_CS_SERVICE_OPERATION="bluetoothcsserviceoperation";
			
			public static class BluetoothClient
			{
				/**
				 * 发送文件失败原因的Key
				 */
				public static final String SEND_FILE_FAILED_REASON="sendfilefailedreason";
			}
		
			public static class BluetoothServer
			{
				/**
				 * 传输文件的总长度
				 */
				public static final String TOTAL_DATA_LENGTH="totaldatalength";
				/**
				 * 已接收的文件长度
				 */
				public static final String CURRENT_DATA_LENGTH="currentdatalength";
				/**
				 * 接收文件失败原因
				 */
				public static final String RECEIVE_FILE_FAILED_REASON="receivefilefailedreason";
			}
		
			public static class BluetoothPBAP
			{
				/**
				 * 远程设备联系人数量的key
				 */
				public static final String REMOTE_DEVICE_CONTACTS_COUNT="remotedevicecontactscount";
				/**
				 * PBAP拉取后的文件保存路径的key
				 */
				public static final String PBAP_RECEIVE_FILE_PATH="pbapreceivefilepath";
				/**
				 * 当前获取到的联系人数量的Key
				 */
				public static final String CURRENT_PHONEBOOK_COUNT="currentphonebookcount";
				/**
				 * 需获取的联系人总数的Key
				 */
				public static final String TOTAL_PHONEBOOK_COUNT="totalphonebookcount";
			}
		}
		
		public static class Wifi
		{
			/**
			 * Activity与Service传递Bundle参数时使用的key
			 */
			public static final String WIFI_SERVICE_OPERATION="wifiserviceoperation";
			/**
			 * Activity与Service传递Acticity上下文时使用的key
			 */
			public static final String WIFI_ACTIVITY_CONTEXT="wifiactivitycontext";
			
			public static class WifiClient
			{
				/**
				 * 接收端的WiFi设备
				 */
				public static final String SENDER_WIFI_DEVICE="senderwifidevice";
				/**
				 * 用于绑定接收端的WiFi设备的信息
				 */
				public static final String SENDER_WIFI_DEVICE_BUNDLE="senderwifidevicebundle";
				/**
				 * 发送端WiFi设备名称
				 */
				public static final String SENDER_WIFI_DEVICE_NAME="senderwifidevicename";
				/**
				 * 接收端WiFi设备IP地址
				 */
				public static final String SENDER_WIFI_DEVICE_IPADDRESS="senderwifideviceipaddress";
				/**
				 * 接收端WiFi设备MAC地址
				 */
				public static final String SENDER_WIFI_DEVICE_MACADDRESS="senderwifidevicemacaddress";
				/**
				 * 发送的文件路径
				 */
				public static final String SEND_FILE_PATH="sendfilepath";
				/**
				 * 发送的文件类型
				 */
				public static final String SEND_FILE_TYPE="sendfiletype";
			}
		
			public static class WifiServer
			{
				/**
				 * 发送端WiFi设备名称
				 */
				public static final String RECEIVER_WIFI_DEVICE_NAME="receiverwifidevicename";
				/**
				 * 接收端WiFi设备IP地址
				 */
				public static final String RECEIVER_WIFI_DEVICE_IPADDRESS="receiverwifideviceipaddress";
				/**
				 * 接收端WiFi设备MAC地址
				 */
				public static final String RECEIVER_WIFI_DEVICE_MACADDRESS="receiverwifidevicemacaddress";
				/**
				 * 接收的文件路径
				 */
				public static final String RECEIVE_FILE_PATH="receivefilepath";
			}
		}
	}
	
	/**
	 * Service里用于识别何种操作
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
