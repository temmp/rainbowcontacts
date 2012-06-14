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
 * ����PBAP�Ĳ���
 * @author Administrator
 *
 */
public class BluetoothPBAP extends BluetoothTransport {
	/**
	 * ����OBEXЭ��Ĵ���,��ʵ���Ƿ�װ��һ��BluetoothSocket�Ĳ�����
	 */
	private BluetoothPbapRfcommTransport m_BPRT;
	/**
	 * ����OBEXЭ���ͨѶ����������connect��get��put�ȷ���
	 */
	private ClientSession m_ClientSession;
	/**
	 * ��������绰�����ļ������
	 */
//	private FileOutputStream m_PhonebookOutput;
	/**
	 * �������ӣ�connect��ʱ�����ص�connection ID�����ڱ�ʾ������
	 */
	private long m_ConnectionID=-1;
	/**
	 * ���ȡ����ϵ������
	 */
	private int m_TotalPhonebookCount=-1;
	/**
	 * ��ǰ��ȡ����ϵ������
	 */
	private int m_CurrentPhonebookCount=-1;
	
	/**
	 * ���캯��
	 * @param bluetoothdevice ����һ��Զ���豸����
	 * @throws PBAPException
	 * �ϲ�Ӧ�ò�׽PBAPException�����PBAP���������Ӳ��ɹ���һ�����Service dicovery failed�쳣��
	 * ���2���������Ӷ����ɹ������캯���׳��쳣�����ϲ㲶�񣬲������Գ��������������������ӡ���һ��ֻ��Ħ��������ɵ�ƻ���������⣡������ 
	 */
	public BluetoothPBAP(BluetoothDevice bluetoothdevice) throws BluetoothException
	{
		super(bluetoothdevice);
		//��������PBAPͨѶ���׽���
		BluetoothSocket btsocket = null;
		try {
			btsocket = super.createSocket(Constants_Bluetooth.PBAP.PBAP_UUID);
			Log.i("aaa", "����PBAP�׽������");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("aaa", "�����׽���ʧ�ܣ�����"+e1.getMessage());
		}
		//���PBAP������׽��ִ���ʧ�����׳��쳣
		if(null==btsocket){Log.e("aaa", "PBAP�׽���Ϊnull������");}
		//����ֱ������һ���׽��ֵĴ�������
		try {
			btsocket.connect();
			Log.i("aaa", "PBAP�׽����������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP�׽�������ʧ�ܣ�����"+e.getMessage());
			try {
				btsocket.close();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
				Log.e("aaa", "PBAP�׽��ֹر�����ʧ�ܣ�����"+e3.getMessage());
			}
			btsocket=null;
			/**
			 * ��������
			 */
			try {
				btsocket= super.createSocket(Constants_Bluetooth.PBAP.PBAP_UUID);
				btsocket.connect();
				Log.i("aaa", "PBAP�׽��������������");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("aaa", "PBAP�׽�����������ʧ�ܣ�����"+e1.getMessage());
				/*try {
					btsocket.close();
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
					Log.e("aaa", "PBAP�׽������¹ر�����ʧ�ܣ�����"+e4.getMessage());
				}*/
				/**
				 * 2����������
				 */
//				try {
//					btsocket.connect();
//					Log.i("aaa", "PBAP�׽���2�������������");
//				} catch (IOException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//					Log.e("aaa", "PBAP�׽���2����������ʧ�ܣ�����"+e2.getMessage());
//					try {
//						btsocket.close();
//					} catch (IOException e5) {
//						// TODO Auto-generated catch block
//						e5.printStackTrace();
//						Log.e("aaa", "PBAP�׽��ֹر�����ʧ�ܣ�����"+e5.getMessage());
//					}
//					//���2���������Ӷ�ʧ�����׳��쳣
//					throw new BluetoothException("PBAP socket connect failed");
//				}
				throw new BluetoothException("PBAP socket connect failed");
			}
		}
		m_BPRT=new BluetoothPbapRfcommTransport(btsocket);
		try {
			m_ClientSession=new ClientSession(m_BPRT);
			Log.i("aaa", "����OBEX�ĻỰ�ɹ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "����OBEX�ĻỰʧ�ܣ�����"+e.getMessage());
		}
	}
	
	/**
	 * ��������PBAP�Ự���ӵ����󣬲����ݷ��ص�HeaderSet����һ��connection ID
	 * @throws BluetoothException 
	 * @throws IOException
	 */
	public void connectForPBAPService() throws BluetoothException
	{
		//��ʼ��PBAP�����HeaderSet
		HeaderSet pbap_request_header=new HeaderSet();
		pbap_request_header.setHeader(HeaderSet.TARGET, Constants_Bluetooth.PBAP.PBAP_VALIDATE_TARGET);
		//���շ���˷��ص�HeaderSet
		try {
			HeaderSet return_header=m_ClientSession.connect(pbap_request_header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP��������ʧ�ܣ�����"+e.getMessage());
			try {
				m_ClientSession.connect(pbap_request_header);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("aaa", "PBAP2����������ʧ�ܣ�����"+e.getMessage());
				throw new BluetoothException("Session connect failed");
			}
		}
		//��ȡ���ӵ�ID�����ں��������ݴ���
		m_ConnectionID=m_ClientSession.getConnectionID();
	}

	/**
	 * �ӷ���˻�ȡ��ϵ�˵�����
	 * @return
	 * @throws IOException 
	 */
	public int getAllContactsCount() throws IOException
	{
		//PBAP����ApplicationParameter��HeaderSet
		HeaderSet get_phonebook_size_header=new HeaderSet();
		//�Ѵ����õ�IDת��Ϊ�ֽ������ٸ�ֵ��HeaderSet��ConnectionID
		get_phonebook_size_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_size_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_size_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook_size=new ApplicationParameter();
		//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
		param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
		//����HeaderSet��Ӧ�ó������
		get_phonebook_size_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
		//ʵ����һ��ClientOperation����������������˷��ص����ݣ�δ���������ӣ�
		ClientOperation co_getsize=(ClientOperation) m_ClientSession.get(get_phonebook_size_header);
		//�򿪸����ӵ�InputStream�������޷���ȡHeaderSet�����������ӣ�
		InputStream is_size=co_getsize.openInputStream();
		//��ȡ���ص�HeaderSet
		HeaderSet hs_paramheader=co_getsize.getReceivedHeader();
		//��ȡHeaderSet�е�APPLICATION_PARAMETER
		byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
		//��ϵ������
		int contactssize=0;
		//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
		for(int i=0;i<rc_param.length;++i)
		{
			if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
			{
				//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
				//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
				contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
				break;
			}
		}
		//�ر�ClientOperation����
		co_getsize.close();
		return contactssize;
	}
	
	/**
	 * �ӷ���˻�ȡ��ϵ�˵�����
	 * @return
	 * @throws IOException 
	 */
	public int getAllContactsCountPro(Handler handler) throws IOException
	{
		//PBAP����ApplicationParameter��HeaderSet
		HeaderSet get_phonebook_size_header=new HeaderSet();
		//�Ѵ����õ�IDת��Ϊ�ֽ������ٸ�ֵ��HeaderSet��ConnectionID
		get_phonebook_size_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_size_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_size_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook_size=new ApplicationParameter();
		//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
		param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
		//����HeaderSet��Ӧ�ó������
		get_phonebook_size_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
		//ʵ����һ��ClientOperation����������������˷��ص����ݣ�δ���������ӣ�
		ClientOperation co_getsize=(ClientOperation) m_ClientSession.get(get_phonebook_size_header);
		//�򿪸����ӵ�InputStream�������޷���ȡHeaderSet�����������ӣ�
		InputStream is_size=co_getsize.openInputStream();
		//��ȡ���ص�HeaderSet
		HeaderSet hs_paramheader=co_getsize.getReceivedHeader();
		//��ȡHeaderSet�е�APPLICATION_PARAMETER
		byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
		//��ϵ������
		int contactssize=0;
		//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
		for(int i=0;i<rc_param.length;++i)
		{
			if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
			{
				//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
				//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
				contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
				break;
			}
		}
		//�ر�ClientOperation����
		co_getsize.close();
		//���û�ȡ�������ϵ������
		setTotalPhonebookCount(contactssize);
		//������Ϣ��Service֪ͨActivity��ʼ��������
		handler.sendEmptyMessage(BluetoothPBAPClientService.MSG.INIT_PROGRESS_DIALOG);
		return contactssize;
	}
	
	
	

	/**
	 * ��ȡ�绰�����ݲ�д��VCard�ļ�
	 * @param pbapreceive_path ����PBAP��ȡ��VCard�ļ�·��
	 * @throws IOException 
	 */
	public void getPhonebookData(File phonebook_receive_file) throws IOException
	{
		//��ʼ���ͻ�ȡ�绰�����ݵ�����
		HeaderSet get_phonebook_header=new HeaderSet();
		get_phonebook_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook=new ApplicationParameter();
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0xFF,(byte)0xFF});
		//ListStartOffset��ֵ��ʾΪ����ListStartOffset+1����ϵ�˿�ʼ��ȡ����Ϊ��1����ϵ��ϵͳ��Ĭ��Ϊ���ҡ����ߡ��ҵ����֡���
		//���Ե�2����ϵ�˲����û��ֶ�����ģ����һ���ListStartOffset����Ϊ1���ӵ�2����ϵ�˿�ʼ��ȡ���������ã�ListStartOffsetĬ��Ϊ0��
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH, new byte[]{(byte)0x00,(byte)0x01});
		get_phonebook_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook.getAPPparam());		
		ClientOperation co_getphonebook=(ClientOperation) m_ClientSession.get(get_phonebook_header);
		InputStream is_phonebook=co_getphonebook.openInputStream();
		//��ȡ�绰������ʱ���õ��Ļ�����
		byte[] buffer=new byte[2*1024];
		//ÿ�ζ�ȡ�ĳ���
		int readlen=0;		
		FileOutputStream fo_phonebook=new FileOutputStream(phonebook_receive_file);
		//�ѽ��յ�������д��VCard�ļ�
		while((readlen=is_phonebook.read(buffer))!=-1)
		{
			fo_phonebook.write(buffer, 0, readlen);
		}
		co_getphonebook.close();
	}
	
	/**
	 * ��ȡ�绰�����ݲ�д��VCard�ļ�
	 * @param pbapreceive_path ����PBAP��ȡ��VCard�ļ�·��
	 * @throws IOException 
	 */
	public void getPhonebookDataPro(File phonebook_receive_file,Handler handler) throws IOException
	{
		//��ʼ���ͻ�ȡ�绰�����ݵ�����
		HeaderSet get_phonebook_header=new HeaderSet();
		get_phonebook_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		get_phonebook_header.setHeader(HeaderSet.NAME, Constants_Bluetooth.PBAP.Name.PHONEBOOK_NAME);
		get_phonebook_header.setHeader(HeaderSet.TYPE, Constants_Bluetooth.PBAP.Type.PHONEBOOK_TYPE);
		ApplicationParameter param_phonebook=new ApplicationParameter();
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0xFF,(byte)0xFF});
		//ListStartOffset��ֵ��ʾΪ����ListStartOffset+1����ϵ�˿�ʼ��ȡ����Ϊ��1����ϵ��ϵͳ��Ĭ��Ϊ���ҡ����ߡ��ҵ����֡���
		//���Ե�2����ϵ�˲����û��ֶ�����ģ����һ���ListStartOffset����Ϊ1���ӵ�2����ϵ�˿�ʼ��ȡ���������ã�ListStartOffsetĬ��Ϊ0��
		param_phonebook.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.LISTSTARTOFFSET_TAGID, 
				ApplicationParameter.TRIPLET_LENGTH.LISTSTARTOFFSET_LENGTH, new byte[]{(byte)0x00,(byte)0x01});
		get_phonebook_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook.getAPPparam());		
		ClientOperation co_getphonebook=(ClientOperation) m_ClientSession.get(get_phonebook_header);
		InputStream is_phonebook=co_getphonebook.openInputStream();
		//��ȡ�绰������ʱ���õ��Ļ�����
		byte[] buffer=new byte[2*1024];
		//ÿ�ζ�ȡ�ĳ���
		int readlen=0;
		//ͬ��ͳ�Ƶ�ǰ��ȡ��ϵ������
		int receive_count=0;
		
		//"BEGIN:VCARD"���ֽ���
		byte []target ={(byte)0x42,(byte)0x45,(byte)0x47,(byte)0x49,(byte)0x4E,(byte)0x3A};
		
		FileOutputStream fo_phonebook=new FileOutputStream(phonebook_receive_file);
		//�ѽ��յ�������д��VCard�ļ�
		while((readlen=is_phonebook.read(buffer))!=-1)
		{
			fo_phonebook.write(buffer, 0, readlen);
			//���ҵ�ǰ�ѽ�����ϵ������
			receive_count+=BluetoothTools.SearchSubByteArray(buffer, target);
			//�����ѽ��ܵ���ϵ������
			setCurrentPhonebookCount(receive_count);
			//������Ϣ��Service֪ͨActivity���½�����
			handler.sendEmptyMessage(BluetoothPBAPClientService.MSG.UPDATE_PROGRESS_DIALOG);
		}
		co_getphonebook.close();
	}
	
	
	
	
	/**
	 * ��������Ŀ¼�ṹ�еĸ�Ŀ¼
	 */
	public void setRootDirectory()
	{
		HeaderSet set_phonebook_directory_header=new HeaderSet();
		set_phonebook_directory_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		try {
			m_ClientSession.setPath(set_phonebook_directory_header, false, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "���ظ�Ŀ¼ʧ�ܣ�����"+e.getMessage());
		}
	}
	/**
	 * ������Ŀ¼�ṹ�����õ�ǰĿ¼
	 * @param dir Ŀ¼��
	 */
	public void setDirectory(String dir)
	{
		HeaderSet set_phonebook_directory_header=new HeaderSet();
		set_phonebook_directory_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		set_phonebook_directory_header.setHeader(HeaderSet.NAME, dir);
		try {
			//���õ�ǰĿ¼·��
			m_ClientSession.setPath(set_phonebook_directory_header, false, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();Log.e("aaa", "���õ�ǰĿ¼ʧ�ܣ�����"+e.getMessage());
		}
	}
	/**
	 * �ѵ�ǰĿ¼����Ϊ�ֻ��绰����Ĭ��Ŀ¼
	 */
	public void setDefaultPhonebookDirectory()
	{
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_PHONEBOOK_PATH);
	}
	
	/**
	 * �ӷ���˻�ȡ�绰���б���󣨵绰���б������XML��ʽ���أ�
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
	 * ��ȡָ��VCard��Ŀ
	 * @param index vCard���������
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
	 * ��ȡ����ָ��VCard��Ŀ
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
	////////////////////////////��ϵ绰��ʷ��¼/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ��ȡ��ϵ绰��ʷ��¼���б�
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
			Log.e("aaa", "��ȡ��ϵ绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡ��ϵ绰��ʷ��¼������
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
			//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//����HeaderSet��Ӧ�ó������
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//��ȡ���ص�HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//��ȡHeaderSet�е�APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//��ϵ������
			int contactssize=0;
			//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
					//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//�ر�ClientOperation����
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * ��ȡ���ָ����ϵ绰��ʷ��¼
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
	 * ��ȡ����ָ����ϵ绰��ʷ��¼
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
		
		//������·��Ϊ��ϵ绰��ʷ��¼����·��
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_COMBINED_CALLS_HISTORY_PATH);
		
		getSpecifyCombinedCallsHistoryArray(combined_calls_array,all_combined_calls_history_file);
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////����绰��ʷ��¼/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * ��ȡ����绰��ʷ��¼���б�
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
			Log.e("aaa", "��ȡ����绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡ����绰��ʷ��¼������
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
			//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//����HeaderSet��Ӧ�ó������
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//��ȡ���ص�HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//��ȡHeaderSet�е�APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//��ϵ������
			int contactssize=0;
			//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
					//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//�ر�ClientOperation����
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * ��ȡ���ָ������绰��ʷ��¼
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
	 * ��ȡ����ָ������绰��ʷ��¼
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
		
		//������·��Ϊ��ϵ绰��ʷ��¼����·��
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_INCOMING_CALLS_HISTORY_PATH);
		
		getSpecifyIncomingCallsHistoryArray(incoming_calls_array,all_incoming_calls_history_file);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////����绰��ʷ��¼/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * ��ȡ����绰��ʷ��¼���б�
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
			Log.e("aaa", "��ȡ����绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡ����绰��ʷ��¼������
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
			//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//����HeaderSet��Ӧ�ó������
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//��ȡ���ص�HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//��ȡHeaderSet�е�APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//��ϵ������
			int contactssize=0;
			//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
					//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//�ر�ClientOperation����
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * ��ȡ���ָ������绰��ʷ��¼
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
	 * ��ȡ����ָ������绰��ʷ��¼
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
		
		//������·��Ϊ��ϵ绰��ʷ��¼����·��
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_OUTGOING_CALLS_HISTORY_PATH);
		
		getSpecifyOutgoingCallsHistoryArray(outgoing_calls_array,all_outgoing_calls_history_file);
	}
	
	
	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////δ�ӵ绰��ʷ��¼/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * ��ȡδ�ӵ绰��ʷ��¼���б�
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
			Log.e("aaa", "��ȡδ�ӵ绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡδ�ӵ绰��ʷ��¼������
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
			//MaxListCountΪ0�����ص�Application Parameters���PhonebookSize����ʵ���Ѿ�ʹ�õ���ϵ������
			param_phonebook_size.addAPPHeader(ApplicationParameter.TRIPLET_TAGID.MAXLISTCOUNT_TAGID,
					ApplicationParameter.TRIPLET_LENGTH.MAXLISTCOUNT_LENGTH, new byte[]{(byte)0x00,(byte)0x00});
			//����HeaderSet��Ӧ�ó������
			get_specify_vcard_header.setHeader(HeaderSet.APPLICATION_PARAMETER, param_phonebook_size.getAPPparam());
			
			ClientOperation co_getspecifyvcard=(ClientOperation)m_ClientSession.get(get_specify_vcard_header);
			
			InputStream is_specifyvcard=co_getspecifyvcard.openInputStream();
			
			//��ȡ���ص�HeaderSet
			HeaderSet hs_paramheader=co_getspecifyvcard.getReceivedHeader();
			//��ȡHeaderSet�е�APPLICATION_PARAMETER
			byte[] rc_param=(byte[]) hs_paramheader.getHeader(HeaderSet.APPLICATION_PARAMETER);
			//��ϵ������
			int contactssize=0;
			//��APPLICATION_PARAMETER���ֽ������У��ҵ���ʾ��ϵ��������16λ�ֽڣ���0x08��ͷ
			for(int i=0;i<rc_param.length;++i)
			{
				if(ApplicationParameter.TRIPLET_TAGID.PHONEBOOKSIZE_TAGID==rc_param[i])
				{
					//0x08֮��������ֽڱ�ʾ��ϵ�����������������ֽ�ת��Ϊint�����ֽ�����8λ������λ���㣩������ֽڣ���λ���㣩���
					//��byteת��Ϊint�ͣ�Ĭ�ϸ�24λ����
					contactssize=(((rc_param[i+2])<<8)+(0x00FF&rc_param[i+3]));
					break;
				}
			}
			//�ر�ClientOperation����
			co_getspecifyvcard.close();
			return contactssize;
	}
	/**
	 * ��ȡ���ָ��δ�ӵ绰��ʷ��¼
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
	 * ��ȡ����ָ��δ�ӵ绰��ʷ��¼
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
		
		//������·��Ϊ��ϵ绰��ʷ��¼����·��
		setRootDirectory();
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFAULT_PHONE_PATH);
		setDirectory(Constants_Bluetooth.PBAP.Path.DEFALUT_MISSED_CALLS_HISTORY_PATH);
		
		getSpecifyMissedCallsHistoryArray(missed_calls_array,all_missed_calls_history_file);
	}
	
	
	
	
	
	
	/**
	 * ��װ��ȡĬ��Ŀ¼�绰���б�Ĳ�������������·���ͻ�ȡ�绰���б�
	 * @param phonebook_list_receive_file
	 * @throws IOException
	 */
	/*public void getDefaultPhonebookList(File phonebook_list_receive_file) throws IOException
	{
		setPhonebookDefaultDirectory();
		getPhonebookList(phonebook_list_receive_file);
	}*/

	/**
	 * �����˶Ͽ�����
	 * @throws IOException 
	 */
	public void disconnectToServer()
	{
		//�Ͽ�����
		HeaderSet disconnect_header=new HeaderSet();
		disconnect_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
		try {
			m_ClientSession.disconnect(disconnect_header);
			Log.i("aaa", "PBAP�����˶Ͽ����ӳɹ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "PBAP�����˶Ͽ�����ʧ�ܣ�����"+e.getMessage());
		}
		try {
			m_ClientSession.close();
			Log.i("aaa", "�ͷ�PBAP����������ɹ�");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("aaa", "�ͷ�PBAP���������ʧ�ܣ�����"+e.getMessage());
		}
	}
	
	
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////������/////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	
	
	
	/**
	 * ��ȡ��ϵ绰��ʷ��¼
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
			Log.e("aaa", "��ȡ��ϵ绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡδ�ӵ绰��ʷ��¼
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
			Log.e("aaa", "��ȡδ�ӵ绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡ����绰��ʷ��¼
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
			Log.e("aaa", "��ȡ����绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * ��ȡ����绰��ʷ��¼
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
			Log.e("aaa", "��ȡ����绰��ʷ��¼ʧ�ܣ�����"+e.getMessage());
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
	 * �������ȡ����ϵ������
	 */
	public void setTotalPhonebookCount(int total)
	{
		m_TotalPhonebookCount=total;
	}
	/**
	 * �õ����ȡ��ϵ�˵�����
	 */
	public int getTotalPhonebookCount()
	{
		return m_TotalPhonebookCount;
	}
	/**
	 * ���õ�ǰ��ȡ������ϵ������
	 */
	public void setCurrentPhonebookCount(int current)
	{
		m_CurrentPhonebookCount=current;
	}
	/**
	 * �õ���ǰ��ȡ������ϵ������
	 */
	public int getCurrentPhonebookCount()
	{
		return m_CurrentPhonebookCount;
	}
	/**
	 * �ж�PBAP�绰������
	 */
//	public void abortGettingPhonebookData()
//	{
//		HeaderSet abort_header=new HeaderSet();
//		abort_header.mConnectionID=ObexHelper.convertToByteArray(m_ConnectionID);
//		
//		try {
//			ClientOperation co_abort=new ClientOperation(0, m_ClientSession, abort_header, false);
//			co_abort.abort();
//			Log.i("aaa", "�ж�PBAP�绰������ɹ�");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.e("aaa", "�ж�PBAP�绰������ʧ�ܣ�����"+e.getMessage());
//		}
//	}
}
