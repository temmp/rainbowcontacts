package ccdr.rainbow.Bluetooth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.obex.ApplicationParameter;
import javax.obex.BluetoothPbapRfcommTransport;
import javax.obex.ClientOperation;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.ObexHelper;

import ccdr.rainbow.Constants.Constants_Bluetooth;
import ccdr.rainbow.Exception.BluetoothException;
import ccdr.rainbow.Service.BluetoothPBAPClientService;
import ccdr.rainbow.Tool.BluetoothTools;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

/**
 * 蓝牙PBAP的操作
 * @author Administrator
 *
 */
public class BluetoothPBAP extends BluetoothTransport {
	/**
	 * 用于OBEX协议的传输,事实上是封装了一个BluetoothSocket的操作类
	 */
	private BluetoothPbapRfcommTransport m_BPRT;
	/**
	 * 用于OBEX协议的通讯操作，包括connect，get，put等方法
	 */
	private ClientSession m_ClientSession;
	/**
	 * 用于输出电话簿的文件输出流
	 */
//	private FileOutputStream m_PhonebookOutput;
	/**
	 * 建立连接（connect）时所返回的connection ID，用于标示该连接
	 */
	private long m_ConnectionID=-1;
	/**
	 * 需获取的联系人总数
	 */
	private int m_TotalPhonebookCount=-1;
	/**
	 * 当前获取的联系人数量
	 */
	private int m_CurrentPhonebookCount=-1;
	
	/**
	 * 构造函数
	 * @param bluetoothdevice 传入一个远程设备对象
	 * @throws PBAPException
	 * 上层应该捕捉PBAPException，如果PBAP的请求连接不成功，一般会有Service dicovery failed异常，
	 * 如果2次重新连接都不成功，则构造函数抛出异常，由上层捕获，捕获后可以尝试重启蓝牙并重新连接。（一般只有摩托罗拉的傻逼机器会出问题！！！） 
	 */
	public BluetoothPBAP(BluetoothDevice bluetoothdevice) throws BluetoothException
	{
		super(bluetoothdevice);
		//创建蓝牙PBAP通讯的套接字
		BluetoothSocket btsocket = null;
		try {
			btsocket = super.createSocket(Constants_Bluetooth.PBAP.PBAP_UUID);
			Log.i("aaa", "创建PBAP套接字完成");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("aaa", "创建套接字失败！！！"+e1.getMessage());
		}
		//如果PBAP传输的套接字创建失败则抛出异常
		if(null==btsocket){Log.e("aaa", "PBAP套接字为null！！！");}
		//阻塞直到创建一个套接字的传输链接
		try {
			btsocket.connect();
			Log.i("aaa", "PBAP套接字连接完成");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP套接字连接失败！！！"+e.getMessage());
			try {
				btsocket.close();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				Log.e("aaa", "PBAP套接字关闭连接失败！！！"+e3.getMessage());
			}
			btsocket=null;
			/**
			 * 重新连接
			 */
			try {
				btsocket= super.createSocket(Constants_Bluetooth.PBAP.PBAP_UUID);
				btsocket.connect();
				Log.i("aaa", "PBAP套接字重新连接完成");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("aaa", "PBAP套接字重新连接失败！！！"+e1.getMessage());
				/*try {
					btsocket.close();
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
					Log.e("aaa", "PBAP套接字重新关闭连接失败！！！"+e4.getMessage());
				}*/
				/**
				 * 2次重新连接
				 */
//				try {
//					btsocket.connect();
//					Log.i("aaa", "PBAP套接字2次重新连接完成");
//				} catch (IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//					Log.e("aaa", "PBAP套接字2次重新连接失败！！！"+e2.getMessage());
//					try {
//						btsocket.close();
//					} catch (IOException e5) {
//						// TODO Auto-generated catch block
//						e5.printStackTrace();
//						Log.e("aaa", "PBAP套接字关闭连接失败！！！"+e5.getMessage());
//					}
//					//如果2次重新连接都失败则抛出异常
//					throw new BluetoothException("PBAP socket connect failed");
//				}
				throw new BluetoothException("PBAP socket connect failed");
			}
		}
		m_BPRT=new BluetoothPbapRfcommTransport(btsocket);
		try {
			m_ClientSession=new ClientSession(m_BPRT);
			Log.i("aaa", "创建OBEX的会话成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "创建OBEX的会话失败！！！"+e.getMessage());
		}
	}
	
	/**
	 * 发出建立PBAP会话连接的请求，并根据返回的HeaderSet构造一个connection ID
	 * @throws BluetoothException 
	 * @throws IOException
	 */
	public void connectForPBAPService() throws BluetoothException
	{
		//初始化PBAP请求的HeaderSet
		HeaderSet pbap_request_header=new HeaderSet();
		pbap_request_header.setHeader(HeaderSet.TARGET, Constants_Bluetooth.PBAP.PBAP_VALIDATE_TARGET);
		//接收服务端返回的HeaderSet
		try {
			HeaderSet return_header=m_ClientSession.connect(pbap_request_header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP请求连接失败！！！"+e.getMessage());
			try {
				m_ClientSession.connect(pbap_request_header);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("aaa", "PBAP2次请求连接失败！！！"+e.getMessage());
				throw new BluetoothException("Session connect failed");
			}
		}
		//获取连接的ID，用于后续的数据传输
		m_ConnectionID=m_ClientSession.getConnectionID();
	}

	/**
	 * 从服务端获取联系人的数量
	 * @return
	 * @throws IOException 
	 */
	public int getAllContactsCount() throws IOException
	{
		//PBAP请求ApplicationParameter的HeaderSet
		HeaderSet get_phonebook_size_header=new HeaderSet();
		//把传输用的ID转换为字节数组再赋值给HeaderSet的ConnectionID
		get_phonebook_size_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_size_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_size_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook_size=new ApplicationParameter();
		//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
		param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
		//设置HeaderSet的应用程序参数
		get_phonebook_size_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
		//实例化一个ClientOperation操作类来操作服务端返回的数据（未与服务端连接）
		ClientOperation co_getsize=(ClientOperation) m_ClientSession.get(get_phonebook_size_header);
		//打开该连接的InputStream，否则无法获取HeaderSet（与服务端连接）
		InputStream is_size=co_getsize.openInputStream();
		//获取返回的HeaderSet
		HeaderSet hs_paramheader=co_getsize.getReceivedHeader();
		//获取HeaderSet中的APPLICATION_PARAMETER
		byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
		//联系人数量
		int contactssize=0;
		//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
		for(int i=0;i<rc_param.length;++i)
		{
			if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
			{
				//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
				//把byte转换为int型，默认高24位补零
				contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
				break;
			}
		}
		//关闭ClientOperation操作
		co_getsize.close();
		return contactssize;
	}
	
	/**
	 * 从服务端获取联系人的数量
	 * @return
	 * @throws IOException 
	 */
	public int getAllContactsCountPro(Handler handler) throws IOException
	{
		//PBAP请求ApplicationParameter的HeaderSet
		HeaderSet get_phonebook_size_header=new HeaderSet();
		//把传输用的ID转换为字节数组再赋值给HeaderSet的ConnectionID
		get_phonebook_size_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_size_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_size_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook_size=new ApplicationParameter();
		//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
		param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
		//设置HeaderSet的应用程序参数
		get_phonebook_size_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
		//实例化一个ClientOperation操作类来操作服务端返回的数据（未与服务端连接）
		ClientOperation co_getsize=(ClientOperation) m_ClientSession.get(get_phonebook_size_header);
		//打开该连接的InputStream，否则无法获取HeaderSet（与服务端连接）
		InputStream is_size=co_getsize.openInputStream();
		//获取返回的HeaderSet
		HeaderSet hs_paramheader=co_getsize.getReceivedHeader();
		//获取HeaderSet中的APPLICATION_PARAMETER
		byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
		//联系人数量
		int contactssize=0;
		//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
		for(int i=0;i<rc_param.length;++i)
		{
			if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
			{
				//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
				//把byte转换为int型，默认高24位补零
				contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
				break;
			}
		}
		//关闭ClientOperation操作
		co_getsize.close();
		//设置获取的最大联系人数量
		setTotalPhonebookCount(contactssize);
		//发送消息让Service通知Activity初始化进度条
		handler.sendEmptyMessage(BluetoothPBAPClientService.MSG.INIT_PROGRESS_DIALOG);
		return contactssize;
	}
	
	
	

	/**
	 * 拉取电话簿数据并写入VCard文件
	 * @param pbapreceive_path 保存PBAP拉取的VCard文件路径
	 * @throws IOException 
	 */
	public void getPhonebookData(File phonebook_receive_file) throws IOException
	{
		//开始发送获取电话簿数据的请求
		HeaderSet get_phonebook_header=new HeaderSet();
		get_phonebook_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook=new ApplicationParameter();
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0xFF,(byte)0xFF});
		//ListStartOffset的值表示为从睇ListStartOffset+1个联系人开始获取，因为第1个联系人系统是默认为“我”或者“我的名字”，
		//所以第2个联系人才是用户手动输入的，因此一般把ListStartOffset设置为1，从第2个联系人开始获取（若不设置，ListStartOffset默认为0）
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH, new byte[]{(byte)0x00,(byte)0x01});
		get_phonebook_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook.getAPPparam());		
		ClientOperation co_getphonebook=(ClientOperation) m_ClientSession.get(get_phonebook_header);
		InputStream is_phonebook=co_getphonebook.openInputStream();
		//读取电话簿内容时所用到的缓冲区
		byte[] buffer=new byte[2*1024];
		//每次读取的长度
		int readlen=0;		
		FileOutputStream fo_phonebook=new FileOutputStream(phonebook_receive_file);
		//把接收到的数据写入VCard文件
		while((readlen=is_phonebook.read(buffer))!=-1)
		{
			fo_phonebook.write(buffer, 0, readlen);
		}
		co_getphonebook.close();
	}
	
	/**
	 * 拉取电话簿数据并写入VCard文件
	 * @param pbapreceive_path 保存PBAP拉取的VCard文件路径
	 * @throws IOException 
	 */
	public void getPhonebookDataPro(File phonebook_receive_file,Handler handler) throws IOException
	{
		//开始发送获取电话簿数据的请求
		HeaderSet get_phonebook_header=new HeaderSet();
		get_phonebook_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook=new ApplicationParameter();
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0xFF,(byte)0xFF});
		//ListStartOffset的值表示为从睇ListStartOffset+1个联系人开始获取，因为第1个联系人系统是默认为“我”或者“我的名字”，
		//所以第2个联系人才是用户手动输入的，因此一般把ListStartOffset设置为1，从第2个联系人开始获取（若不设置，ListStartOffset默认为0）
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH, new byte[]{(byte)0x00,(byte)0x01});
		get_phonebook_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook.getAPPparam());		
		ClientOperation co_getphonebook=(ClientOperation) m_ClientSession.get(get_phonebook_header);
		InputStream is_phonebook=co_getphonebook.openInputStream();
		//读取电话簿内容时所用到的缓冲区
		byte[] buffer=new byte[2*1024];
		//每次读取的长度
		int readlen=0;
		//同于统计当前获取联系人数量
		int receive_count=0;
		
		//"BEGIN:VCARD"的字节码
		byte []target ={(byte)0x42,(byte)0x45,(byte)0x47,(byte)0x49,(byte)0x4E,(byte)0x3A};
		
		FileOutputStream fo_phonebook=new FileOutputStream(phonebook_receive_file);
		//把接收到的数据写入VCard文件
		while((readlen=is_phonebook.read(buffer))!=-1)
		{
			fo_phonebook.write(buffer, 0, readlen);
			//查找当前已接受联系人数量
			receive_count+=BluetoothTools.SearchSubByteArray(buffer, target);
			//设置已接受的联系人数量
			setCurrentPhonebookCount(receive_count);
			//发送消息让Service通知Activity更新进度条
			handler.sendEmptyMessage(BluetoothPBAPClientService.MSG.UPDATE_PROGRESS_DIALOG);
		}
		co_getphonebook.close();
	}
	
	
	
	
	/**
	 * 返回虚拟目录结构中的根目录
	 */
	public void setRootDirectory()
	{
		HeaderSet set_phonebook_directory_header=new HeaderSet();
		set_phonebook_directory_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		try {
			m_ClientSession.setPath(set_phonebook_directory_header, false, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "返回根目录失败！！！"+e.getMessage());
		}
	}
	/**
	 * 在虚拟目录结构中设置当前目录
	 * @param dir 目录名
	 */
	public void setDirectory(String dir)
	{
		HeaderSet set_phonebook_directory_header=new HeaderSet();
		set_phonebook_directory_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		set_phonebook_directory_header.setHeader(HeaderSet.NAME, dir);
		try {
			//设置当前目录路径
			m_ClientSession.setPath(set_phonebook_directory_header, false, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "设置当前目录失败！！！"+e.getMessage());
		}
	}
	/**
	 * 把当前目录设置为手机电话簿的默认目录
	 */
	public void setDefaultPhonebookDirectory()
	{
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_PHONEBOOK_PATH);
	}
	
	/**
	 * 从服务端获取电话簿列表对象（电话簿列表对象以XML形式返回）
	 * @throws IOException 
	 */
	public void getPhonebookList(File phonebook_list_receive_file) throws IOException
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_LIST_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		
		ClientOperation co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();
		
		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_phonebooklist=new FileOutputStream(phonebook_list_receive_file);
		
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_phonebooklist.write(buffer,0,readlen);
		}
		co_getphonebooklist.close();
	}
	
	/**
	 * 获取指定VCard条目
	 * @param index vCard对象的名字
	 * @param vcard_file
	 * @throws IOException
	 */
	public void getSpecifyVcard(String index,File vcard_file) throws IOException
	{
		HeaderSet get_specify_vcard_header=new HeaderSet();
		get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
		get_specify_vcard_header.setHeader(HeaderSet.NAME, index);
		
		ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
		
		InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
		
		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_specifyvcard=new FileOutputStream(vcard_file);
		while((readlen=is_specifyvcard.read(buffer))!=-1)
		{
			fo_specifyvcard.write(buffer,0,readlen);
		}
		co_getspecifyvcard.close();
	}
	
	/**
	 * 获取所有指定VCard条目
	 * @param index_array
	 * @param vcard_array_file
	 * @throws IOException
	 */
	public void getSpecifyVcardArray(String[] index_array,File vcard_array_file) throws IOException
	{
		FileOutputStream fo_specifyvcardarray=new FileOutputStream(vcard_array_file);
		for(int i=0;i<index_array.length;++i)
		{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, index_array[i]);
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			byte[] buffer=new byte[2*1024];
			int readlen=0;

			while((readlen=is_specifyvcard.read(buffer))!=-1)
			{
				fo_specifyvcardarray.write(buffer,0,readlen);
			}
			co_getspecifyvcard.close();
		}
	}

	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////混合电话历史记录/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 获取混合电话历史记录的列表
	 * @param combined_calls_history_file
	 * @throws IOException
	 */
	public void getCombinedCallsHistoryList(File combined_calls_history_list_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_LIST_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.COMBINED_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(combined_calls_history_list_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取混合电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	/**
	 * 获取混合电话历史记录的数量
	 * @return
	 * @throws IOException
	 */
	public int getCombinedCallsHistorySize() throws IOException
	{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.COMBINED_CALL_HISTORY_NAME);
			ApplicationParameter param_phonebook_size=new ApplicationParameter();
			//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//设置HeaderSet的应用程序参数
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//获取返回的HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//获取HeaderSet中的APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//联系人数量
			int contactssize=0;
			//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
					//把byte转换为int型，默认高24位补零
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//关闭ClientOperation操作
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * 获取多个指定混合电话历史记录
	 * @param index_array
	 * @param combinedcallshistory_array_file
	 * @throws IOException
	 */
	public void getSpecifyCombinedCallsHistoryArray(String[] index_array,File combined_calls_history_array_file) throws IOException
	{
		FileOutputStream fo_specifyvcardarray=new FileOutputStream(combined_calls_history_array_file);
		for(int i=0;i<index_array.length;++i)
		{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, index_array[i]);
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			byte[] buffer=new byte[2*1024];
			int readlen=0;

			while((readlen=is_specifyvcard.read(buffer))!=-1)
			{
				fo_specifyvcardarray.write(buffer,0,readlen);
			}
			co_getspecifyvcard.close();
		}
	}
	/**
	 * 获取所有指定混合电话历史记录
	 * @param all_combined_calls_history_file
	 * @throws IOException
	 */
	public void getAllCombinedCallsHistory(File all_combined_calls_history_file) throws IOException
	{
		int combined_calls_size=getCombinedCallsHistorySize();
		String[] combined_calls_array=new String[combined_calls_size];
		for(int i=1;i<=combined_calls_size;++i)
		{
			combined_calls_array[i-1]=String.valueOf(i)+Constants_Bluetooth.PBAP.Suffix.VCARD_FILE_SUFFIX;
		}
		
		//先设置路径为混合电话历史记录所在路径
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_COMBINED_CALLS_HISTORY_PATH);
		
		getSpecifyCombinedCallsHistoryArray(combined_calls_array,all_combined_calls_history_file);
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////接入电话历史记录/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取接入电话历史记录的列表
	 * @param incoming_calls_history_list_file
	 */
	public void getIncomingCallsHistoryList(File incoming_calls_history_list_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_LIST_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.INCOMING_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(incoming_calls_history_list_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取接入电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	/**
	 * 获取接入电话历史记录的数量
	 * @return
	 * @throws IOException
	 */
	public int getIncomingCallsHistorySize() throws IOException
	{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.INCOMING_CALL_HISTORY_NAME);
			ApplicationParameter param_phonebook_size=new ApplicationParameter();
			//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//设置HeaderSet的应用程序参数
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//获取返回的HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//获取HeaderSet中的APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//联系人数量
			int contactssize=0;
			//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
					//把byte转换为int型，默认高24位补零
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//关闭ClientOperation操作
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * 获取多个指定接入电话历史记录
	 * @param index_array
	 * @param incoming_calls_history_array_file
	 * @throws IOException
	 */
	public void getSpecifyIncomingCallsHistoryArray(String[] index_array,File incoming_calls_history_array_file) throws IOException
	{
		FileOutputStream fo_specifyvcardarray=new FileOutputStream(incoming_calls_history_array_file);
		for(int i=0;i<index_array.length;++i)
		{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, index_array[i]);
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			byte[] buffer=new byte[2*1024];
			int readlen=0;

			while((readlen=is_specifyvcard.read(buffer))!=-1)
			{
				fo_specifyvcardarray.write(buffer,0,readlen);
			}
			co_getspecifyvcard.close();
		}
	}
	/**
	 * 获取所有指定接入电话历史记录
	 * @param all_incoming_calls_history_file
	 * @throws IOException
	 */
	public void getAllIncomingCallsHistory(File all_incoming_calls_history_file) throws IOException
	{
		int incoming_calls_size=getIncomingCallsHistorySize();
		String[] incoming_calls_array=new String[incoming_calls_size];
		for(int i=1;i<=incoming_calls_size;++i)
		{
			incoming_calls_array[i-1]=String.valueOf(i)+Constants_Bluetooth.PBAP.Suffix.VCARD_FILE_SUFFIX;
		}
		
		//先设置路径为混合电话历史记录所在路径
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_INCOMING_CALLS_HISTORY_PATH);
		
		getSpecifyIncomingCallsHistoryArray(incoming_calls_array,all_incoming_calls_history_file);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////打出电话历史记录/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取打出电话历史记录的列表
	 * @param outgoing_calls_history_list_file
	 */
	public void getOutgoingCallsHistoryList(File outgoing_calls_history_list_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_LIST_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.OUTGOING_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(outgoing_calls_history_list_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取打出电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	/**
	 * 获取打出电话历史记录的数量
	 * @return
	 * @throws IOException
	 */
	public int getOutgoingCallsHistorySize() throws IOException
	{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.OUTGOING_CALL_HISTORY_NAME);
			ApplicationParameter param_phonebook_size=new ApplicationParameter();
			//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//设置HeaderSet的应用程序参数
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//获取返回的HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//获取HeaderSet中的APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//联系人数量
			int contactssize=0;
			//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
					//把byte转换为int型，默认高24位补零
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//关闭ClientOperation操作
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * 获取多个指定打出电话历史记录
	 * @param index_array
	 * @param outgoing_calls_history_array_file
	 * @throws IOException
	 */
	public void getSpecifyOutgoingCallsHistoryArray(String[] index_array,File outgoing_calls_history_array_file) throws IOException
	{
		FileOutputStream fo_specifyvcardarray=new FileOutputStream(outgoing_calls_history_array_file);
		for(int i=0;i<index_array.length;++i)
		{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, index_array[i]);
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			byte[] buffer=new byte[2*1024];
			int readlen=0;

			while((readlen=is_specifyvcard.read(buffer))!=-1)
			{
				fo_specifyvcardarray.write(buffer,0,readlen);
			}
			co_getspecifyvcard.close();
		}
	}
	/**
	 * 获取所有指定打出电话历史记录
	 * @param all_outgoing_calls_history_file
	 * @throws IOException
	 */
	public void getAllOutgoingCallsHistory(File all_outgoing_calls_history_file) throws IOException
	{
		int outgoing_calls_size=getOutgoingCallsHistorySize();
		String[] outgoing_calls_array=new String[outgoing_calls_size];
		for(int i=1;i<=outgoing_calls_size;++i)
		{
			outgoing_calls_array[i-1]=String.valueOf(i)+Constants_Bluetooth.PBAP.Suffix.VCARD_FILE_SUFFIX;
		}
		
		//先设置路径为混合电话历史记录所在路径
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_OUTGOING_CALLS_HISTORY_PATH);
		
		getSpecifyOutgoingCallsHistoryArray(outgoing_calls_array,all_outgoing_calls_history_file);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////未接电话历史记录/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取未接电话历史记录的列表
	 * @param missed_calls_history_list_file
	 */
	public void getMissedCallsHistoryList(File missed_calls_history_list_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_LIST_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.MISSED_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(missed_calls_history_list_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取未接电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	/**
	 * 获取未接电话历史记录的数量
	 * @return
	 * @throws IOException
	 */
	public int getMissedCallsHistorySize() throws IOException
	{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.MISSED_CALL_HISTORY_NAME);
			ApplicationParameter param_phonebook_size=new ApplicationParameter();
			//MaxListCount为0，返回的Application Parameters里的PhonebookSize就是实际已经使用的联系人数量
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//设置HeaderSet的应用程序参数
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//获取返回的HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//获取HeaderSet中的APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//联系人数量
			int contactssize=0;
			//从APPLICATION_PARAMETER的字节数组中，找到表示联系人数量的16位字节，以0x08开头
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08之后的两个字节表示联系人数量，将这两个字节转化为int，高字节左移8位（其余位补零）后与低字节（高位补零）相加
					//把byte转换为int型，默认高24位补零
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//关闭ClientOperation操作
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * 获取多个指定未接电话历史记录
	 * @param index_array
	 * @param missed_calls_history_array_file
	 * @throws IOException
	 */
	public void getSpecifyMissedCallsHistoryArray(String[] index_array,File missed_calls_history_array_file) throws IOException
	{
		FileOutputStream fo_specifyvcardarray=new FileOutputStream(missed_calls_history_array_file);
		for(int i=0;i<index_array.length;++i)
		{
			HeaderSet get_specify_vcard_header=new HeaderSet();
			get_specify_vcard_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
			get_specify_vcard_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.VCARD_TYPE);
			get_specify_vcard_header.setHeader(HeaderSet.NAME, index_array[i]);
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			byte[] buffer=new byte[2*1024];
			int readlen=0;

			while((readlen=is_specifyvcard.read(buffer))!=-1)
			{
				fo_specifyvcardarray.write(buffer,0,readlen);
			}
			co_getspecifyvcard.close();
		}
	}
	/**
	 * 获取所有指定未接电话历史记录
	 * @param all_missed_calls_history_file
	 * @throws IOException
	 */
	public void getAllMissedCallsHistory(File all_missed_calls_history_file) throws IOException
	{
		int missed_calls_size=getMissedCallsHistorySize();
		String[] missed_calls_array=new String[missed_calls_size];
		for(int i=1;i<=missed_calls_size;++i)
		{
			missed_calls_array[i-1]=String.valueOf(i)+Constants_Bluetooth.PBAP.Suffix.VCARD_FILE_SUFFIX;
		}
		
		//先设置路径为混合电话历史记录所在路径
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_MISSED_CALLS_HISTORY_PATH);
		
		getSpecifyMissedCallsHistoryArray(missed_calls_array,all_missed_calls_history_file);
	}
	
	
	
	
	
	
	/**
	 * 封装获取默认目录电话簿列表的操作，包括设置路径和获取电话簿列表
	 * @param phonebook_list_receive_file
	 * @throws IOException
	 */
	/*public void getDefaultPhonebookList(File phonebook_list_receive_file) throws IOException
	{
		setPhonebookDefaultDirectory();
		getPhonebookList(phonebook_list_receive_file);
	}*/

	/**
	 * 与服务端断开连接
	 * @throws IOException 
	 */
	public void disconnectToServer()
	{
		//断开连接
		HeaderSet disconnect_header=new HeaderSet();
		disconnect_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		try {
			m_ClientSession.disconnect(disconnect_header);
			Log.i("aaa", "PBAP与服务端断开连接成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP与服务端断开连接失败！！！"+e.getMessage());
		}
		try {
			m_ClientSession.close();
			Log.i("aaa", "释放PBAP输入输出流成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "释放PBAP输入输出流失败！！！"+e.getMessage());
		}
	}
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////有问题/////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * 获取混合电话历史记录
	 * @param combined_calls_history_file
	 * @throws IOException
	 */
	public void getCombinedCallsHistory(File combined_calls_history_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.COMBINED_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(combined_calls_history_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取混合电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	
	/**
	 * 获取未接电话历史记录
	 * @param combined_calls_history_file
	 * @throws IOException
	 */
	public void getMissedCallsHistory(File missed_calls_history_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.MISSED_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(missed_calls_history_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch (Exception e)
		{
			Log.e("aaa", "获取未接电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	
	/**
	 * 获取接入电话历史记录
	 * @param combined_calls_history_file
	 * @throws IOException
	 */
	public void getIncomingCallsHistory(File incoming_calls_history_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.INCOMING_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(incoming_calls_history_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch(Exception e)
		{
			Log.e("aaa", "获取接入电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	
	/**
	 * 获取打出电话历史记录
	 * @param combined_calls_history_file
	 * @throws IOException
	 */
	public void getOutgoingCallsHistory(File outgoing_calls_history_file)
	{
		HeaderSet get_phonebook_list_header=new HeaderSet();
		get_phonebook_list_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_list_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		get_phonebook_list_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.OUTGOING_CALL_HISTORY_NAME);
		
		ClientOperation co_getphonebooklist=null;
		try
		{
		co_getphonebooklist=(ClientOperation)m_ClientSession.get(get_phonebook_list_header);
		
		InputStream is_phonebooklist=co_getphonebooklist.openInputStream();

		byte[] buffer=new byte[2*1024];
		int readlen=0;
		FileOutputStream fo_getcombinedcallshistory=new FileOutputStream(outgoing_calls_history_file);
		while((readlen=is_phonebooklist.read(buffer))!=-1)
		{
			fo_getcombinedcallshistory.write(buffer, 0, readlen);
		}
		}
		catch(Exception e)
		{
			Log.e("aaa", "获取打出电话历史记录失败！！！"+e.getMessage());
		}
		finally
		{
			try {
				co_getphonebooklist.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("aaa", e.getMessage());
			}
		}
	}
	
	
	/**
	 * 设置需获取的联系人总数
	 */
	public void setTotalPhonebookCount(int total)
	{
		m_TotalPhonebookCount=total;
	}
	/**
	 * 得到需获取联系人的总数
	 */
	public int getTotalPhonebookCount()
	{
		return m_TotalPhonebookCount;
	}
	/**
	 * 设置当前获取到的联系人数量
	 */
	public void setCurrentPhonebookCount(int current)
	{
		m_CurrentPhonebookCount=current;
	}
	/**
	 * 得到当前获取到的联系人数量
	 */
	public int getCurrentPhonebookCount()
	{
		return m_CurrentPhonebookCount;
	}
	/**
	 * 中断PBAP电话簿传输
	 */
//	public void abortGettingPhonebookData()
//	{
//		HeaderSet abort_header=new HeaderSet();
//		abort_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
//		
//		try {
//			ClientOperation co_abort=new ClientOperation(0, m_ClientSession, abort_header, false);
//			co_abort.abort();
//			Log.i("aaa", "中断PBAP电话簿传输成功");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e("aaa", "中断PBAP电话簿传输失败！！！"+e.getMessage());
//		}
//	}
}
