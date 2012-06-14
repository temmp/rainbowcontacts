package ccdr.rainbow.Bluetooth;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ccdr.rainbow.Constants.Constants_File;
import ccdr.rainbow.Constants.Constants_Global;
import ccdr.rainbow.Service.BluetoothServerService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ��������ˣ����ܷ����Ĳ���
 * @author Administrator
 *
 */
public class BluetoothServer extends BluetoothCS {
	private BluetoothAdapter m_BluetoothAdapter;				//����������������
	private BluetoothServerSocket ServerSocket;					//���ڼ��������豸�Ա���������
	private FileOutputStream m_ReceiveStream = null;			//�ļ�����������ڽ�Զ���豸�ж����Vcard����д������
	private List<Byte> Bl = new LinkedList<Byte>();
	private BluetoothSocket exchangeSocket = null;
	private String remotename;
	
	private Handler m_Handler;									//��Service������Handler
	
	private int m_TotalLength=-1;								//�ļ��ܳ���
	private int m_CurrentLength=-1;								//��ǰ�ѻ�ȡ�ļ�����
	
	public BluetoothServer(UUID serviceuuid,Handler handler,String filepath) throws FileNotFoundException{
		super(serviceuuid);
		m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		m_Handler=handler;
		m_ReceiveStream = new FileOutputStream(new File(filepath));	//��ָ��·�������ƴ���һ���ļ������
	}
	public String getName(){
		return remotename;
	}
	public void CloseSocket() throws IOException{
		if(exchangeSocket != null)exchangeSocket.close();
			if(ServerSocket != null)ServerSocket.close();
	}
	/**
	 * ��ReceiveFile()ִ����֮���ý����ļ������ݣ���byte[]����ʽ
	 * @return
	 */
	public byte[] getReceiveResultByte(){
		byte[] wb = new byte[Bl.size()];
		 Log.d("TestCS",wb.length+"bytes  received");
		 for(int i = 0; i < wb.length; i++){
			 wb[i] = Bl.get(i);
		 }
		 return wb;
	}
	/**
	 * ��ReceiveFile()ִ����֮���ý����ļ������ݣ���Stream����ʽ
	 * @return
	 */
	public InputStream getReceiResultIns(){
		return new ByteArrayInputStream(getReceiveResultByte());
		
	}
	/**
	 * �������ȶ���õ�uuidע�����
	 * @throws IOException 
	 */
	public void registerService() throws IOException {
		
			//����һ�����ڼ����İ�ȫ�Ĵ��з����¼��������Ƶͨ�������ӿ�
			ServerSocket = m_BluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICENAME, ServiceUUID);
		
	}
	public void waitAccept() throws IOException{
		//�������ӵ����󣬽�������֮ǰ���ûᱻ��ϣ�ֱ�������µ�BluetoothSocket���������
		try{
		exchangeSocket = ServerSocket.accept();
		
		}catch(NullPointerException e ){
			
		}
	}
	
	public void sendEndTag() throws IOException{
		OutputStream outs = exchangeSocket.getOutputStream();
		outs.write(BluetoothCS.END);
		outs.flush();
	}
	/**
	 * �ȴ��ͻ�������������󣬲��������ӣ��ȴ��Է�������Ϣ�����浽Byte�б�bl��
	 * @return
	 */
	@SuppressWarnings("finally")
	public byte ReceiveFile() {
		if(ServerSocket == null){
			return NULL_SOCKET;
		}
		
		byte headtype = 0;
		try {
			//��������
			//�򿪶˿ڵ�����������
			InputStream instrm = exchangeSocket.getInputStream();
			//byte high = (byte) instrm.read();
			//byte low = (byte) instrm.read();
			int len = (byte) instrm.read();
				//TwoBytetoInt(high,low);
			byte []nameb = new byte[len];
			instrm.read(nameb, 0, len);
			remotename = new String(nameb);
			Log.d("remotename", remotename);
			
			headtype = (byte)instrm.read();
			ReceiveContent(headtype,instrm);
			
			sendEndTag();
			if(exchangeSocket != null)exchangeSocket.close();
			if(ServerSocket != null)ServerSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(exchangeSocket != null)exchangeSocket.close();
			if(ServerSocket != null)ServerSocket.close();
			e.printStackTrace();
		}finally{
			return  headtype;
		}
	}
	
	private void ReceiveContent(byte headtype,InputStream instrm) {
		
		if(headtype == END)return;
		byte lengthhighhigh;
		try {

			String testfilename = Constants_File.Path.SDROOTPATH + File.separator + "CS@" + Build.MODEL +"@"
			+remotename+" "+convertLongToRFC2445DateTime(System.currentTimeMillis())+".vcf";
			FileOutputStream fouts = null;
			if(Constants_Global.DEBUG) fouts = new FileOutputStream(new File(testfilename));
			Log.d("pagebt", "available0 length is " + instrm.available());
			//��ȡ�������ݵĳ��ȣ���4���ֽڱ�ʾ�䳤��
			lengthhighhigh = (byte) instrm.read();
			byte lengthhighlow = (byte) instrm.read();
			byte lengthlowhigh = (byte) instrm.read();
			byte lengthlowlow = (byte) instrm.read();
			//�ѱ�ʾ���ȵ�4���ֽ�ת��Ϊһ��int���͵���ֵ
			int ContentLength = fourBytetoInt(lengthhighhigh,lengthhighlow,lengthlowhigh,lengthlowlow);
			
			Log.d("pagebt", "length is " +ContentLength);
			
			//���ͳ�ʼ����������Ϣ
			Message msg = m_Handler.obtainMessage(BluetoothServerService.MSG.INIT_PROGRESS_DIALOG);
//			Bundle b = new Bundle();
//			b.putInt(Constants_Bluetooth.Key.TOTAL_DATA_LENGTH, ContentLength); 
//			msg.setData(b);
			msg.sendToTarget();
			
			setTotalLength(ContentLength);
			
			
			
			
			byte[] buffer = new byte[BUFFER_LENGTH];
			int left = ContentLength;
			int readonce = 0;
			Message msg2 = null;
			Bundle b2 = new Bundle();
			//��ʼѭ����ȡ�ļ�����
			while(left > 0){
				//��ȡ����
				readonce = instrm.read(buffer);
				left = left - readonce;
				Log.d("pagebt", "left1 length is " +left);
				
				msg2 = m_Handler.obtainMessage(BluetoothServerService.MSG.UPDATE_PROGRESS_DIALOG);
//				b2.putInt("Total", ContentLength);
//				b2.putInt("Progress", ContentLength - left);
//				msg2.setData(b2);
				msg2.sendToTarget();
				
				setCurrentLength(ContentLength - left);
				
				//д������
				
				m_ReceiveStream.write(buffer,0,readonce);
				if(Constants_Global.DEBUG)fouts.write(buffer, 0,readonce);
				//�Ѷ�ȡ����ÿһ���ֽڵ����ݷ���List��
//				for(int i = 0; i < readonce; i++){
//					Bl.add(new Byte(buffer[i]));
//				}
				Log.d("pagebt", "left2 length is " +left);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				instrm.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}
	
	/**
	 * �����ļ��ܳ���
	 * @param count
	 */
	public void setTotalLength(int length)
	{
		m_TotalLength=length;
	}
	/**
	 * ���õ�ǰ��ȡ����
	 * @param length
	 */
	public void setCurrentLength(int length)
	{
		m_CurrentLength=length;
	}
	/**
	 * ��ȡ�ļ��ܳ���
	 * @return
	 */
	public int getTotalLength()
	{
		return m_TotalLength;
	}
	/**
	 * ��ȡ��ǰ�ļ���ȡ����
	 * @return
	 */
	public int getCurrentLength()
	{
		return m_CurrentLength;
	}
}
