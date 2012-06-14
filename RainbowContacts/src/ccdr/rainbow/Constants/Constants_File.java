package ccdr.rainbow.Constants;

import java.io.File;


import android.os.Environment;

public class Constants_File {
	
	public static class Path {
		/**
		 * �Թ�����������Ŀ¼
		 */
		public static final String PROJECT_NAME = "com.ccdr.rainbowcontacts";
		/**
		 * SD����Ŀ¼
		 */
		public static final String SDROOTPATH = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		/**
		 * ������ݵĸ�Ŀ¼
		 */
		public static final String ROOTPATH = "/data/data";
		/**
		 * ������ص��ļ�Ŀ¼
		 */
		public static final String PROJECT_FILE = ROOTPATH + File.separator
				+ PROJECT_NAME + File.separator + "files";
		/**
		 * PBAP��ȡ��VCard�ļ���·��
		 */
		public static final String PBAP_REV = PROJECT_FILE + File.separator
				+ "PBAPReceive.vcf";
		/**
		 * C/Sģʽ���Ͷ˵�����VCard�ļ�·��
		 */
		public static final String CS_SEND = PROJECT_FILE + File.separator
				+ "Vcard_Send.vcf";
		/**
		 * C/Sģʽ���ն˵����VCard�ļ�·��
		 */
		public static final String CS_REV = PROJECT_FILE + File.separator
				+ "Vcard_Receive.vcf";
		/**
		 * ����Ĭ�ϱ���·��
		 */
		public static final String LOCALPATH = PROJECT_FILE + File.separator
				+ "localVcard.vcf";
		/**
		 * ����Ĭ�ϱ���SD��·��
		 */
		public static final String SDLOCALPATH = SDROOTPATH + File.separator
				+ "localVcard.vcf";
		
		/**
		 * WiFiC/Sģʽ���Ͷ˵�����VCard�ļ�·��
		 */
		public static final String WIFI_SEND = PROJECT_FILE + File.separator
				+ "WifiSend.vcf";
		/**
		 * WiFiC/Sģʽ���ն˵����VCard�ļ�·��
		 */
		public static final String WIFI_RECV = PROJECT_FILE + File.separator
				+ "WifiReceive.vcf";
	}
	
	public static class Dialog
	{
		/**
		 * ��ʾVCF�ļ��Ի���
		 */
		public final static int VCFDIALOG = 100;
		/**
		 * ��ʾ��ʾ�Ի���
		 */
		public final static int HINTDIALOG = 101;
	}
}
