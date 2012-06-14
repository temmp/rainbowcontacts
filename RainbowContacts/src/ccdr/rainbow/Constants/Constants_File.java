package ccdr.rainbow.Constants;

import java.io.File;


import android.os.Environment;

public class Constants_File {
	
	public static class Path {
		/**
		 * 以工程名命名的目录
		 */
		public static final String PROJECT_NAME = "com.ccdr.rainbowcontacts";
		/**
		 * SD卡根目录
		 */
		public static final String SDROOTPATH = Environment
				.getExternalStorageDirectory().getAbsolutePath();
		/**
		 * 存放数据的根目录
		 */
		public static final String ROOTPATH = "/data/data";
		/**
		 * 工程相关的文件目录
		 */
		public static final String PROJECT_FILE = ROOTPATH + File.separator
				+ PROJECT_NAME + File.separator + "files";
		/**
		 * PBAP获取到VCard文件的路径
		 */
		public static final String PBAP_REV = PROJECT_FILE + File.separator
				+ "PBAPReceive.vcf";
		/**
		 * C/S模式发送端导出的VCard文件路径
		 */
		public static final String CS_SEND = PROJECT_FILE + File.separator
				+ "Vcard_Send.vcf";
		/**
		 * C/S模式接收端导入的VCard文件路径
		 */
		public static final String CS_REV = PROJECT_FILE + File.separator
				+ "Vcard_Receive.vcf";
		/**
		 * 本地默认备份路径
		 */
		public static final String LOCALPATH = PROJECT_FILE + File.separator
				+ "localVcard.vcf";
		/**
		 * 本地默认备份SD卡路径
		 */
		public static final String SDLOCALPATH = SDROOTPATH + File.separator
				+ "localVcard.vcf";
		
		/**
		 * WiFiC/S模式发送端导出的VCard文件路径
		 */
		public static final String WIFI_SEND = PROJECT_FILE + File.separator
				+ "WifiSend.vcf";
		/**
		 * WiFiC/S模式接收端导入的VCard文件路径
		 */
		public static final String WIFI_RECV = PROJECT_FILE + File.separator
				+ "WifiReceive.vcf";
	}
	
	public static class Dialog
	{
		/**
		 * 显示VCF文件对话框
		 */
		public final static int VCFDIALOG = 100;
		/**
		 * 显示提示对话框
		 */
		public final static int HINTDIALOG = 101;
	}
}
