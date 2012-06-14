package ccdr.rainbow.Constants;

import java.util.UUID;

public class Constants_Bluetooth {

	public static class PBAP
	{
		/**
		 * ���ڱ�ʶ��OBEX����˽���PBAP�����128λUUID
		 */
		public static final byte[] PBAP_VALIDATE_TARGET = new byte[] { 0x79, 0x61,
				0x35, (byte) 0xf0, (byte) 0xf0, (byte) 0xc5, 0x11, (byte) 0xd8,
				0x09, 0x66, 0x08, 0x00, 0x20, 0x0c, (byte) 0x9a, 0x66 };
		/**
		 * ����PBAPЭ���UUID
		 */
		public final static String PBAP_STRING="0000112f-0000-1000-8000-00805f9b34fb";
		public final static UUID PBAP_UUID=UUID.fromString(PBAP_STRING);
		
			
		public class Name{
		/**
		 * Ĭ�ϵĵ绰��·��
		 */
		public static final String PHONEBOOK_NAME="telecom/pb.vcf";
		/**
		 * Ĭ�ϵĽ���绰��ʷ��¼·��
		 */
		public static final String INCOMING_CALL_HISTORY_NAME="telecom/ich.vcf";
		/**
		 * Ĭ�ϵĴ���绰��ʷ��¼·��
		 */
		public static final String OUTGOING_CALL_HISTORY_NAME="telecom/och.vcf";
		/**
		 * Ĭ�ϵ�δ�ӵ绰��ʷ��¼·��
		 */
		public static final String MISSED_CALL_HISTORY_NAME="telecom/mch.vcf";
		/**
		 * Ĭ�ϵĻ�ϵ绰��ʷ��¼·�����������еĽӡ������δ�ӵ绰��ʷ��¼��
		 */
		public static final String COMBINED_CALL_HISTORY_NAME="telecom/cch.vcf";
		}
		
			
		public class Path{
		/**
		 * ���ص绰����Ŀ¼
		 */
		public static final String DEFAULT_PHONE_PATH="telecom";
		/**
		 * ���ص绰��Ŀ¼
		 */
		public static final String DEFALUT_PHONEBOOK_PATH="pb";
		/**
		 * ���ؽ���绰��ʷ��¼Ŀ¼
		 */
		public static final String DEFALUT_INCOMING_CALLS_HISTORY_PATH="ich";
		/**
		 * ���ش���绰��ʷ��¼Ŀ¼
		 */
		public static final String DEFALUT_OUTGOING_CALLS_HISTORY_PATH="och";
		/**
		 * ����δ�ӵ绰��ʷ��¼Ŀ¼
		 */
		public static final String DEFALUT_MISSED_CALLS_HISTORY_PATH="mch";
		/**
		 * ���ػ�ϵ绰��ʷ��¼Ŀ¼
		 */
		public static final String DEFALUT_COMBINED_CALLS_HISTORY_PATH="cch";
		}
		
		
		public class Type{
		/**
		 * Ĭ�ϵĵ绰������
		 */
		public static final String PHONEBOOK_TYPE="x-bt/phonebook";
		/**
		 * Ĭ�ϵĵ绰���б�����
		 */
		public static final String PHONEBOOK_LIST_TYPE="x-bt/vcard-listing";
		/**
		 * ָ��VCard��Ŀ����
		 */
		public static final String VCARD_TYPE="x-bt/vcard";
		}
		
			
		public class Suffix{
		/**
		 * Vcard�ļ���׺��
		 */
		public static final String VCARD_FILE_SUFFIX=".vcf";
		/**
		 * Vcard-Listing�ļ���׺����XML��
		 */
		public static final String VCARD_LISTING_FILE_SUFFIX=".xml";
		}
	}
	
	
	public static class CS
	{
		/**
		 * CSģʽʹ�õ�UUID
		 */
		public final static UUID CS_UUID=UUID.fromString("3B1500F4-A516-BD71-9997-D259E3C0D162");
	}
	
	
//	public class Key
//	{
//		/**
//		 * Activity��BluetoothService����BluetoothDevice�����key
//		 */
//		public static final String BLUETOOTH_DEVICE="bluetoothdevice";
//		/**
//		 * Activity��BluetoothPBAPService���ݲ���ѡ���key
//		 */
//		public static final String Bluetooth_PBAP_SERVICE_OPERATION="bluetoothpbapserviceoperation";
//		/**
//		 * Activity��BluetoothCS���ݲ���ѡ���key
//		 */
//		public static final String Bluetooth_CS_SERVICE_OPERATION="bluetoothcsserviceoperation";
//		/**
//		 * Զ���豸��ϵ��������key
//		 */
//		public static final String REMOTE_DEVICE_CONTACTS_COUNT="remotedevicecontactscount";
//		/**
//		 * PBAP��ȡ����ļ�����·����key
//		 */
//		public static final String PBAP_RECEIVE_FILE_PATH="pbapreceivefilepath";
//		/**
//		 * �����ļ����ܳ���
//		 */
//		public static final String TOTAL_DATA_LENGTH="totaldatalength";
//		/**
//		 * �ѽ��յ��ļ�����
//		 */
//		public static final String CURRENT_DATA_LENGTH="currentdatalength";
//		/**
//		 * �����ļ�ʧ��ԭ��
//		 */
//		public static final String RECEIVE_FILE_FAILED_REASON="receivefilefailedreason";
//	}
	
	
//	public class Action
//	{
//		/**
//		 *  ��Զ�������豸��ʧ��
//		 */
//		public static final String BOND_REMOTEDEVICE_FAILED="bondremotedeicefailed";
//		/**
//		 * ��ʼ��PBAP����ɹ�
//		 */
//		public static final String INIT_PBAP_SERVICE_SUCCESS="initpbapservicesuccess";
//		/**
//		 * ��Զ���豸PBAP��������ӳɹ�
//		 */
//		public static final String CONNECT_REMOTE_DEVICE_PBAP_SUCCESS="connectremotedevicesuccess";
//		/**
//		 * ��ȡԶ���豸��ϵ������
//		 */
//		public static final String RECEIVE_REMOTE_DEVICE_CONTACTS_COUNT="receiveremotedevicecontactscount";
//		/**
//		 * ��ȡԶ���豸��ϵ������
//		 */
//		public static final String RECEIVE_REMOTE_DEVICE_PHONEBOOK_DATA="receiveremotedevicephonebookdata";
//		/**
//		 * ��Զ�������豸���ӳɹ�
//		 */
//		public static final String CONNECT_REMOTE_DEVICE_SUCCESS="connectremotedevicesucess";
//		/**
//		 * ��Զ���豸PBAP����˶Ͽ����ӳɹ�
//		 */
//		public static final String DISCONNECT_REMOTE_DEVICE_SUCCESS="disconnectremotedevicesuccess";
//		/**
//		 * ��ʼ��������
//		 */
//		public static final String INIT_PROGRESS_DIALOG="initprogressdialog";
//		/**
//		 * ���½�����
//		 */
//		public static final String UPDATE_PROGRESS_DIALOG="updateprogressdialog";
//		/**
//		 * ����˽����ļ��ɹ�
//		 */
//		public static final String RECEIVE_FILE_SUCESS="receivefilesuccess";
//		/**
//		 * �Ҳ����ļ�����������������USB�洢
//		 */
//		public static final String FILE_NOT_FOUND="filenotfound";
//		/**
//		 * �����ļ�����ʧ��
//		 */
//		public static final String RECEIVE_FILE_FAILED="receivefilefailed";
//	}
	
	
	public class Option
	{
		/**
		 * PBAP��ȡ��ϵ������
		 */
		public static final int GET_REMOTE_DEVICE_CONTACTS_COUNT=1001;
		/**
		 * PBAP��ȡ������ϵ��
		 */
		public static final int GET_REMOTE_DEVICE_PHONEBOOK_DATA=1002;
		/**
		 * ��PBAP���������
		 */
		public static final int CONNECT_REMOTE_DEVICE=1003;
		/**
		 * ��PBAP����˶Ͽ�����
		 */
		public static final int DISCONNECT_REMOTE_DEVICE=1004;
		/**
		 * ����˿�ʼ�����ļ�
		 */
		public static final int START_RECEIVING_FILE=1005;
		/**
		 * �ͻ��˿�ʼ�����ļ�
		 */
		public static final int START_SENDING_FILE=1006;
		/**
		 * �ж�PBAP�绰������
		 */
		public static final int ABORT_GETTING_PHONEBOOK_DATA=1007;
	}
	
	
//	public class Message
//	{
//		/**
//		 * ��԰󶨳ɹ�
//		 */
//		public static final int BOND_REMOTEDEVICE_SUCCESS=500;
//		/**
//		 * ��԰�ʧ��
//		 */
//		public static final int BOND_REMOTEDEVICE_FAILED=501;
//		/**
//		 * ��PBAP����˳�ʼ���ɹ�
//		 */
//		public static final int INIT_PBAP_SERVICE_SUCCESS=502;
//		/**
//		 * ���豸���ӳɹ�
//		 */
//		public static final int CONNECT_REMOTE_DEVICE_SUCESS=503;
//		/**
//		 * ��ȡ�豸�绰���ɹ�
//		 */
//		public static final int GET_REMOTE_DEVICE_PHONEBOOK_SUCESS=504;
//		/**
//		 * �Ͽ�PBAP���ӳɹ�
//		 */
//		public static final int DISCONNECT_REMOTE_DEVICE_SUCCESS=505;
//		/**
//		 * ��ʼ���������Ի���
//		 */
//		public static final int INIT_PROGRESS_DIALOG=506;
//		/**
//		 * ���½������Ի���
//		 */
//		public static final int UPDATE_PROGRESS_DIALOG=507;
//		/**
//		 * ���½�����
//		 */
//		public static final int UPDATE_PBAP_PROGRESS_BAR=508;
//		/**
//		 * ��ɽ���������
//		 */
//		public static final int COMPLETE_PBAP_PROGRESS_BAR=509;
//		/**
//		 * �����ļ��ɹ�
//		 */
//		public static final int SEND_FILE_SUCCESS=510;
//		/**
//		 * �����ļ��ɹ�
//		 */
//		public static final int RECEIVE_FILE_SUCCESS=511;
//	}
}
