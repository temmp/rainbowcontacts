package ccdr.rainbow.Constants;

import java.util.UUID;

public class Constants_Bluetooth {

	public static class PBAP
	{
		/**
		 * 用于标识与OBEX服务端进行PBAP传输的128位UUID
		 */
		public static final byte[] PBAP_VALIDATE_TARGET = new byte[] { 0x79, 0x61,
				0x35, (byte) 0xf0, (byte) 0xf0, (byte) 0xc5, 0x11, (byte) 0xd8,
				0x09, 0x66, 0x08, 0x00, 0x20, 0x0c, (byte) 0x9a, 0x66 };
		/**
		 * 用于PBAP协议的UUID
		 */
		public final static String PBAP_STRING="0000112f-0000-1000-8000-00805f9b34fb";
		public final static UUID PBAP_UUID=UUID.fromString(PBAP_STRING);
		
			
		public class Name{
		/**
		 * 默认的电话簿路径
		 */
		public static final String PHONEBOOK_NAME="telecom/pb.vcf";
		/**
		 * 默认的接入电话历史记录路径
		 */
		public static final String INCOMING_CALL_HISTORY_NAME="telecom/ich.vcf";
		/**
		 * 默认的打出电话历史记录路径
		 */
		public static final String OUTGOING_CALL_HISTORY_NAME="telecom/och.vcf";
		/**
		 * 默认的未接电话历史记录路径
		 */
		public static final String MISSED_CALL_HISTORY_NAME="telecom/mch.vcf";
		/**
		 * 默认的混合电话历史记录路径（包括所有的接、打出、未接电话历史记录）
		 */
		public static final String COMBINED_CALL_HISTORY_NAME="telecom/cch.vcf";
		}
		
			
		public class Path{
		/**
		 * 本地电话数据目录
		 */
		public static final String DEFAULT_PHONE_PATH="telecom";
		/**
		 * 本地电话簿目录
		 */
		public static final String DEFALUT_PHONEBOOK_PATH="pb";
		/**
		 * 本地接入电话历史记录目录
		 */
		public static final String DEFALUT_INCOMING_CALLS_HISTORY_PATH="ich";
		/**
		 * 本地打出电话历史记录目录
		 */
		public static final String DEFALUT_OUTGOING_CALLS_HISTORY_PATH="och";
		/**
		 * 本地未接电话历史记录目录
		 */
		public static final String DEFALUT_MISSED_CALLS_HISTORY_PATH="mch";
		/**
		 * 本地混合电话历史记录目录
		 */
		public static final String DEFALUT_COMBINED_CALLS_HISTORY_PATH="cch";
		}
		
		
		public class Type{
		/**
		 * 默认的电话簿类型
		 */
		public static final String PHONEBOOK_TYPE="x-bt/phonebook";
		/**
		 * 默认的电话簿列表类型
		 */
		public static final String PHONEBOOK_LIST_TYPE="x-bt/vcard-listing";
		/**
		 * 指定VCard条目类型
		 */
		public static final String VCARD_TYPE="x-bt/vcard";
		}
		
			
		public class Suffix{
		/**
		 * Vcard文件后缀名
		 */
		public static final String VCARD_FILE_SUFFIX=".vcf";
		/**
		 * Vcard-Listing文件后缀名（XML）
		 */
		public static final String VCARD_LISTING_FILE_SUFFIX=".xml";
		}
	}
	
	
	public static class CS
	{
		/**
		 * CS模式使用的UUID
		 */
		public final static UUID CS_UUID=UUID.fromString("3B1500F4-A516-BD71-9997-D259E3C0D162");
	}
	
	
//	public class Key
//	{
//		/**
//		 * Activity与BluetoothService传递BluetoothDevice对象的key
//		 */
//		public static final String BLUETOOTH_DEVICE="bluetoothdevice";
//		/**
//		 * Activity与BluetoothPBAPService传递操作选项的key
//		 */
//		public static final String Bluetooth_PBAP_SERVICE_OPERATION="bluetoothpbapserviceoperation";
//		/**
//		 * Activity与BluetoothCS传递操作选项的key
//		 */
//		public static final String Bluetooth_CS_SERVICE_OPERATION="bluetoothcsserviceoperation";
//		/**
//		 * 远程设备联系人数量的key
//		 */
//		public static final String REMOTE_DEVICE_CONTACTS_COUNT="remotedevicecontactscount";
//		/**
//		 * PBAP拉取后的文件保存路径的key
//		 */
//		public static final String PBAP_RECEIVE_FILE_PATH="pbapreceivefilepath";
//		/**
//		 * 传输文件的总长度
//		 */
//		public static final String TOTAL_DATA_LENGTH="totaldatalength";
//		/**
//		 * 已接收的文件长度
//		 */
//		public static final String CURRENT_DATA_LENGTH="currentdatalength";
//		/**
//		 * 接收文件失败原因
//		 */
//		public static final String RECEIVE_FILE_FAILED_REASON="receivefilefailedreason";
//	}
	
	
//	public class Action
//	{
//		/**
//		 *  与远程蓝牙设备绑定失败
//		 */
//		public static final String BOND_REMOTEDEVICE_FAILED="bondremotedeicefailed";
//		/**
//		 * 初始化PBAP服务成功
//		 */
//		public static final String INIT_PBAP_SERVICE_SUCCESS="initpbapservicesuccess";
//		/**
//		 * 与远程设备PBAP服务端连接成功
//		 */
//		public static final String CONNECT_REMOTE_DEVICE_PBAP_SUCCESS="connectremotedevicesuccess";
//		/**
//		 * 获取远程设备联系人数量
//		 */
//		public static final String RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT="receiveremotedevicecontactscount";
//		/**
//		 * 获取远程设备联系人数据
//		 */
//		public static final String RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA="receiveremotedevicephonebookdata";
//		/**
//		 * 与远程蓝牙设备连接成功
//		 */
//		public static final String CONNECT_REMOTE_DEVICE_SUCCESS="connectremotedevicesucess";
//		/**
//		 * 与远程设备PBAP服务端断开连接成功
//		 */
//		public static final String DISCONNECT_REMOTE_DEVICE_SUCCESS="disconnectremotedevicesuccess";
//		/**
//		 * 初始化进度条
//		 */
//		public static final String INIT_PROGRESS_DIALOG="initprogressdialog";
//		/**
//		 * 更新进度条
//		 */
//		public static final String UPDATE_PROGRESS_DIALOG="updateprogressdialog";
//		/**
//		 * 服务端接收文件成功
//		 */
//		public static final String RECEIVE_FILE_SUCESS="receivefilesuccess";
//		/**
//		 * 找不到文件，可能是正在连接USB存储
//		 */
//		public static final String FILE_NOT_FOUND="filenotfound";
//		/**
//		 * 接收文件过程失败
//		 */
//		public static final String RECEIVE_FILE_FAILED="receivefilefailed";
//	}
	
	
	public class Option
	{
		/**
		 * PBAP获取联系人数量
		 */
		public static final int GET_REMOTE_DEVICE_CONTACTS_COUNT=1001;
		/**
		 * PBAP获取所有联系人
		 */
		public static final int GET_REMOTE_DEVICE_PHONEBOOK_DATA=1002;
		/**
		 * 与PBAP服务端连接
		 */
		public static final int CONNECT_REMOTE_DEVICE=1003;
		/**
		 * 与PBAP服务端断开连接
		 */
		public static final int DISCONNECT_REMOTE_DEVICE=1004;
		/**
		 * 服务端开始接收文件
		 */
		public static final int START_RECEIVING_FILE=1005;
		/**
		 * 客户端开始发送文件
		 */
		public static final int START_SENDING_FILE=1006;
		/**
		 * 中断PBAP电话簿传输
		 */
		public static final int ABORT_GETTING_PHONEBOOK_DATA=1007;
	}
	
	
//	public class Message
//	{
//		/**
//		 * 配对绑定成功
//		 */
//		public static final int BOND_REMOTEDEVICE_SUCCESS=500;
//		/**
//		 * 配对绑定失败
//		 */
//		public static final int BOND_REMOTEDEVICE_FAILED=501;
//		/**
//		 * 与PBAP服务端初始化成功
//		 */
//		public static final int INIT_PBAP_SERVICE_SUCCESS=502;
//		/**
//		 * 与设备连接成功
//		 */
//		public static final int CONNECT_REMOTE_DEVICE_SUCESS=503;
//		/**
//		 * 获取设备电话簿成功
//		 */
//		public static final int GET_REMOTE_DEVICE_PHONEBOOK_SUCESS=504;
//		/**
//		 * 断开PBAP连接成功
//		 */
//		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=505;
//		/**
//		 * 初始化进度条对话框
//		 */
//		public static final int INIT_PROGRESS_DIALOG=506;
//		/**
//		 * 更新进度条对话框
//		 */
//		public static final int UPDATE_PROGRESS_DIALOG=507;
//		/**
//		 * 更新进度条
//		 */
//		public static final int UPDATE_PBAP_PROGRESS_BAR=508;
//		/**
//		 * 完成进度条更新
//		 */
//		public static final int COMPLETE_PBAP_PROGRESS_BAR=509;
//		/**
//		 * 发送文件成功
//		 */
//		public static final int SEND_FILE_SUCCESS=510;
//		/**
//		 * 接收文件成功
//		 */
//		public static final int RECEIVE_FILE_SUCCESS=511;
//	}
}
