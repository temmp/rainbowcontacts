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
 * 蓝牙服务端（接受方）的操作
 * @author Administrator
 *
 */
public class BluetoothServer extends BluetoothCS {
	private BluetoothAdapter m_BluetoothAdapter;				//代表本机蓝牙适配器
	private BluetoothServerSocket ServerSocket;					//用于监听其他设备对本机的请求
	private FileOutputStream m_ReceiveStream = null;			//文件输出流，用于将远程设备中读入的Vcard数据写进本地
	private List<Byte> Bl = new LinkedList<Byte>();
	private BluetoothSocket exchangeSocket = null;
	private String remotename;
	
	private Handler m_Handler;									//从Service传来的Handler
	
	private int m_TotalLength=-1;								//文件总长度
	private int m_CurrentLength=-1;								//当前已获取文件长度
	
	public BluetoothServer(UUID serviceuuid,Handler handler,String filepath) throws FileNotFoundException{
		super(serviceuuid);
		m_BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		m_Handler=handler;
		m_ReceiveStream = new FileOutputStream(new File(filepath));	//用指定路径和名称创建一个文件输出流
	}
	public String getName(){
		return remotename;
	}
	public void CloseSocket() throws IOException{
		if(exchangeSocket != null)exchangeSocket.close();
			if(ServerSocket != null)ServerSocket.close();
	}
	/**
	 * 在ReceiveFile()执行完之后获得接收文件的内容，以byte[]的形式
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
	 * 在ReceiveFile()执行完之后获得接收文件的内容，以Stream的形式
	 * @return
	 */
	public InputStream getReceiResultIns(){
		return new ByteArrayInputStream(getReceiveResultByte());
		
	}
	/**
	 * 利用事先定义好的uuid注册服务
	 * @throws IOException 
	 */
	public void registerService() throws IOException {
		
			//创建一个正在监听的安全的带有服务记录的无线射频通信蓝牙接口
			ServerSocket = m_BluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICENAME, ServiceUUID);
		
	}
	public void waitAccept() throws IOException{
		//监听链接的请求，建立链接之前调用会被阻断，直到返回新的BluetoothSocket管理该请求
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
	 * 等待客户端提出连接请求，并建立连接，等待对方发送信息并保存到Byte列表bl中
	 * @return
	 */
	@SuppressWarnings("finally")
	public byte ReceiveFile() {
		if(ServerSocket == null){
			return NULL_SOCKET;
		}
		
		byte headtype = 0;
		try {
			//阻塞运行
			//打开端口的输入数据流
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
			//获取发送内容的长度，用4个字节表示其长度
			lengthhighhigh = (byte) instrm.read();
			byte lengthhighlow = (byte) instrm.read();
			byte lengthlowhigh = (byte) instrm.read();
			byte lengthlowlow = (byte) instrm.read();
			//把表示长度的4个字节转化为一个int类型的数值
			int ContentLength = fourBytetoInt(lengthhighhigh,lengthhighlow,lengthlowhigh,lengthlowlow);
			
			Log.d("pagebt", "length is " +ContentLength);
			
			//发送初始化进度条消息
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
			//开始循环读取文件数据
			while(left > 0){
				//读取数据
				readonce = instrm.read(buffer);
				left = left - readonce;
				Log.d("pagebt", "left1 length is " +left);
				
				msg2 = m_Handler.obtainMessage(BluetoothServerService.MSG.UPDATE_PROGRESS_DIALOG);
//				b2.putInt("Total", ContentLength);
//				b2.putInt("Progress", ContentLength - left);
//				msg2.setData(b2);
				msg2.sendToTarget();
				
				setCurrentLength(ContentLength - left);
				
				//写入数据
				
				m_ReceiveStream.write(buffer,0,readonce);
				if(Constants_Global.DEBUG)fouts.write(buffer, 0,readonce);
				//把读取到的每一个字节的数据放入List中
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
	 * 设置文件总长度
	 * @param count
	 */
	public void setTotalLength(int length)
	{
		m_TotalLength=length;
	}
	/**
	 * 设置当前读取长度
	 * @param length
	 */
	public void setCurrentLength(int length)
	{
		m_CurrentLength=length;
	}
	/**
	 * 获取文件总长度
	 * @return
	 */
	public int getTotalLength()
	{
		return m_TotalLength;
	}
	/**
	 * 获取当前文件读取长度
	 * @return
	 */
	public int getCurrentLength()
	{
		return m_CurrentLength;
	}
}
