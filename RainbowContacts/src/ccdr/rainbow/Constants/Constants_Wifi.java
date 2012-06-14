package ccdr.rainbow.Constants;





import android.os.Environment;

public class Constants_Wifi {
	public class Signal
	{
		/**
		 * 发送广播成功
		 */
		public static final int SEND_BROADCAST_SUCCESS=50;
		/**
		 * 发送广播异常
		 */
		public static final int SEND_BRODCAST_EXCEPTION=51;
	}
	
	public class Port
	{
		public static final int TCP_TRANSMISSIONPORT = 4100;// 用于传输数据的端口
		public static final int UDP_BOADCASTPORT = 4200;// 用于广播IP地址的端口
		public static final int TCP_CONNECTPORT = 4300;// 用于双方完成连接请求的端口
	}
	
	public class Broadcast
	{
		///////////////////与接收广播相关的变量///////////////////////
		/**
		 * 接收UDP广播数据包的超时时间
		 */
		public static final int DATAGRAMSOCKET_READ_MILLISECOND=4000;
		/**
		 * 每次广播UDP数据包的间隔时间
		 */
		public static final int DATAGRAMSOCKET_SEND_MILLISECOND=1000;
		/**
		 * 循环接收UDP广播数据包时正常返回
		 */
		public static final int RECEIVE_DATAGRAMPACKET_SUCCESS=10;
		/**
		 * 创建DatagramSocket失败（为null）
		 */
		public static final int DATAGRAMSOCKET_CREATE_FAILED=11;
		/**
		 * 循环接收UDP广播数据包时被打断
		 */
		public static final int INTERRUPTED_WHILE_RECEIVING=12;
	}
	
	public class Transport
	{
		///////////////////数据传输前发送端验证请求的相关变量/////////////////
		/**
		 * 远程（对方）手机接受请求
		 */
		public static final int REMOTE_DEVICE_ACCEPT=20;
		/**
		 * 远程（对方）手机拒绝请求
		 */
		public static final int REMOTE_DEVICE_REFUSE=21;
		/**
		 * 接收远程手机数据失败
		 */
		public static final int RECEIVE_REMOTE_DATA_FAILED=22;
		/**
		 * 发送数据到远程手机失败
		 */
		public static final int SEND_DATA_TO_REMOTE_DEVICE_FAILED=23;
		/**
		 * 打开输入流失败
		 */
		public static final int OPEN_INPUTSTREAM_FAILED=24;
		/**
		 * 打开输出流失败
		 */
		public static final int OPEN_OUTPUTSTREAM_FAILED=25;
		/**
		 * 创建套接字失败
		 */
		public static final int CREATE_SOCKET_FAILED=26;
		/**
		 * 创建服务端套接字失败
		 */
		public static final int CREATE_SERVER_SOCKET_FAILED=27;
//		/**
//		 * 本地保存的远程WiFi设备对象与用户选取的设备对象不同
//		 */
//		public static final int SELECTED_DEVICE_NOT_EQUAL_PARAM=28;
//		/**
//		 * 本地保存的远程WiFi设备的IP地址与用户选取的IP地址不同
//		 */
//		public static final int SELECTED_DEVICE_IP_ADDRESS_NOT_EQUAL_PARAM=29;
		/**
		 * 服务端接收发送端请求成功
		 */
		public static final int ACCEPT_CLIENT_CONNECTION_FAILED=30;
		/**
		 * 服务端接收发送端请求失败
		 */
		public static final int ACCEPT_CLIENT_CONNECTION_SUCCESS=31;
		/**
		 * 打开套接字的输入输出流失败
		 */
		public static final int OPEN_IOSTREAM_FAILED=31;
		/**
		 * 用户考虑是否接收文件时被打断
		 */
		public static final int INTERRUPTED_WHILE_CHOOSING=32;
	}
	
	public class File
	{
		///////////////////发送和接收文件的相关变量/////////////////
		/**
		 * 电话簿文件类型
		 */
		public static final byte FILE_TYPE_VCARD=(byte)0xE0;
		/**
		 * 短信文件类型
		 */
		public static final byte FILE_TYPE_SMS=(byte)0xE1;
		/**
		 * 电话文件类型
		 */
		public static final byte FILE_TYPE_VCAL=(byte)0xE2;
		/**
		 * 电话簿文件后缀
		 */
		public static final String FILE_SUFFIX_VCARD=".vcf";
		/**
		 * 无法打开本地VCard文件或文件不存在
		 */
		public static final int VCARD_FILE_NOT_FOUND=29;
		/**
		 * 发送文件成功
		 */
		public static final int SEND_FILE_SUCCESS=40;
		/**
		 * 发送文件失败
		 */
		public static final int SEND_FILE_CONTENT_FAILED=41;
	}
	
	
	public class Key
	{
	/**
	 * Activity与Service传递Bundle参数时使用的key
	 */
	public static final String WIFI_SERVICE_OPERATION="wifiserviceoperation";
	/**
	 * Activity与Service传递Acticity上下文时使用的key
	 */
	public static final String WIFI_ACTIVITY_CONTEXT="wifiactivitycontext";
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
	 * 发送的文件路径
	 */
	public static final String SEND_FILE_PATH="sendfilepath";
	/**
	 * 发送的文件类型
	 */
	public static final String SEND_FILE_TYPE="sendfiletype";
	/**
	 * 接收的文件路径
	 */
	public static final String RECEIVE_FILE_PATH="receivefilepath";
	}
	
	
	/**
	 * 监听远程WiFi设备（Service中onStartCommand方法进行操作判断的值）
	 */
	public static final int LISTEN_REMOTE_WIFI_DEVICES=100;
	/**
	 * 获取远程WiFi设备（Service中onStartCommand方法进行操作判断的值）
	 */
	public static final int GET_REMOTE_WIFI_DEVICES=101;
	/**
	 * 停止监听远程WiFi设备（Service中onStartCommand方法进行操作判断的值）
	 */
	public static final int STOP_LISTENING_REMOTE_WIFI_DEVICES=102;
	/**
	 * 获取远程WiFi设备数量（Service中onStartCommand方法进行操作判断的值）
	 */
	public static final int GET_REMOTE_WIFI_DEVICES_COUNT=103;
	/**
	 * 接收端开始广播
	 */
	public static final int START_SENDING_BROADCAST=104;
	/**
	 * 接收端停止广播
	 */
	public static final int STOP_SENDING_BROADCAST=105;
	/**
	 * 接收端接收请求
	 */
	public static final int START_ACCEPT_REQUEST=106;
	/**
	 * 停止接收请求
	 */
	public static final int STOP_ACCEPT_REQUEST=107;
	/**
	 * 发送文件
	 */
	public static final int SEND_FILE=108;
	/**
	 * 接收文件
	 */
	public static final int RECEIVE_FILE=109;
	/**
	 * 发送请求
	 */
	public static final int SEND_REQUEST=110;
	/**
	 * 停止获取远程WiFi设备（Service中onStartCommand方法进行操作判断的值）
	 */
	public static final int STOP_GETTING_REMOTE_WIFI_DEVICES=111;
	
	
	
//	public class Action
//	{
//	/**
//	 * 线程尚未退出广播监听状态
//	 */
//	public static final String STILL_LISTENING_BROADCAST="stilllisteningbroadcast";
//	/**
//	 * 发送文件请求的广播
//	 */
//	public static final String FILE_RECEIVE_REQUEST="filereceiverequest";
//	/**
//	 * 发送文件成功
//	 */
//	public static final String FILE_SEND_SUCESS="filesendsuccess";
//	/**
//	 * 发送文件失败
//	 */
//	public static final String FILE_SEND_FAILED="filesendfailed";
//	/**
//	 * 发送文件被拒绝
//	 */
//	public static final String FILE_SEND_REFUSE="filesendrefuse";
//	/**
//	 * 接收文件成功
//	 */
//	public static final String FILE_RECEIVE_SUCCESS="filereceivesuccess";
//	/**
//	 * 接收文件失败
//	 */
//	public static final String FILE_RECEIVE_FAILED="filereceivefailed";
//	/**
//	 * 打开Service服务超时
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
