package ccdr.rainbow.Constants;

public class Constants_Global {

	public static boolean DEBUG = false;
	public static String []AUTHORS ={"朱晓彬","张启欣","袁雨来","焦阳","周佳祥","马雪萍","韩涧武","谭冰晶","陈思哲","陈亮辉","周方云","陈熙杰","罗泽鹏"};
	/**
	 * 是否在进入应用前已经打开蓝牙
	 */
	public static boolean BLUETOOTH_ON_BEFORE = false;
	/**
	 * 应用更新的服务器地址
	 */
	public static String URL="http://10.1.11.38:8888/Demo_Test/servlet/firstServlet?action=version";
	/**
	 * 更新版本的下载目录
	 */
	public static String DOWNLOAD_DIR = "app/download/";
	/**
	 * 与版本更新有关
	 */
	public static int SHOW_Dialog = 0;
	/**
	 * 本地用户当前版本
	 */
	public static int LOCAL_VERSION = 0;
	/**
	 * 服务器最新版本
	 */
	public static int SERVER_VERSION = 0;
	
	/**
	 * 绑定WiFi 客户端Service
	 */
	public static final int Wifi_Client_Service=0;
	/**
	 * 绑定WiFi 服务端Service
	 */
	public static final int Wifi_Server_Service=1;
	/**
	 * 绑定Bluetooth 客户端Service
	 */
	public static final int Bluetooth_Client_Service=2;
	/**
	 * 绑定Bluetooth 服务端Service
	 */
	public static final int Bluetooth_Server_Service=3;
	/**
	 * 绑定Bluetooth PBAPService
	 */
	public static final int Bluetooth_PBAP_Service=4;
}
