package ccdr.rainbow.Bluetooth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

/**
 * �����ͻ��ˣ����ͷ����Ĳ���
 * @author Administrator
 *
 */
public class BluetoothClient extends BluetoothCS {
	/**
	 * ��Ե�Զ���豸
	 */
	private final BluetoothDevice mDevice; 
	/**
	 * �ͻ����׽���
	 */
	private BluetoothSocket ClientSocket;
	public BluetoothClient(BluetoothDevice btDevice,UUID serviceuuid){
		super(serviceuuid);
		mDevice = btDevice;
	}
	
	public void CloseSocket() throws IOException{
		if(ClientSocket != null)ClientSocket.close();
	}
	/**
	 * �������ṩ����Ķ˿ڲ�����׽���
	 */
	public void ConnectService(){
		try {
			ClientSocket = mDevice.createRfcommSocketToServiceRecord(ServiceUUID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("aaa", "���ӷ����ʧ�ܣ�����"+e.getMessage());
			try {
				ClientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Log.e("aaa", "�ر��׽���ʧ�ܣ�����"+e.getMessage());
			}
			e.printStackTrace();
		}
		
	}
	public byte read() throws IOException{
		InputStream ins = ClientSocket.getInputStream();
		return (byte) ins.read();
	}
	public void Connect() throws IOException{
		ClientSocket.connect();
	}
	/**
	 * ���û���׽��ְ���Э�鷢���ļ�
	 * @param file
	 * @param type
	 */
	public void SendFile(File file,byte type){
		OutputStream outStrm = null;
		FileInputStream fis = null;
		try {
			//��������
			
			outStrm = ClientSocket.getOutputStream();
			fis = new FileInputStream(file);
			
			
			String name = Build.MODEL;
			byte namebyte[] = name.getBytes();
			int len = namebyte.length;
			//outStrm.write((byte) (len >> 8));
			outStrm.write((byte) (len));
			outStrm.write(namebyte);
			outStrm.flush();
			
			
			outStrm.write(type & 0x00ff);
			outStrm.flush();
			int fileLength = (int) file.length();
			byte highhigh = (byte) (fileLength >> 24);
			byte highlow = (byte) (fileLength >> 16);
			byte lowhigh = (byte) (fileLength >> 8);
			byte lowlow = (byte)fileLength;
			outStrm.write(highhigh & 0x000000ff);
			outStrm.write(highlow & 0x000000ff);
			outStrm.write(lowhigh & 0x000000ff);
			outStrm.write(lowlow & 0x000000ff);
			outStrm.flush();
			Log.d("TestCS", fileLength+""+highhigh + " " + highlow + " " + lowhigh + " " + lowlow);
			int left = fileLength;
			int sendonce = 0;
			byte[] Buffer = new byte[BUFFER_LENGTH];
			while(left > 0){
				sendonce = fis.read(Buffer);
				Log.d("TestCS", "left" + left);
				outStrm.write(Buffer,0,sendonce);
				outStrm.flush();
				left = left - sendonce;
			}
		} catch (IOException e) {
			try {
				outStrm.close();
				fis.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		/**
		 * ���û���׽��ְ���Э�鷢���ļ�
		 * @param file
		 * @param type
		 */
		public void SendFile(InputStream inputStream,byte type,int Length){
			OutputStream outStrm = null;
			try {
				ClientSocket.connect();
				outStrm = ClientSocket.getOutputStream();
				outStrm.write(type & 0x00ff);
				outStrm.flush();
			
				byte highhigh = (byte) (Length >> 24);
				byte highlow = (byte) (Length >> 16);
				byte lowhigh = (byte) (Length >> 8);
				byte lowlow = (byte)Length;
				outStrm.write(highhigh & 0x000000ff);
				outStrm.write(highlow & 0x000000ff);
				outStrm.write(lowhigh & 0x000000ff);
				outStrm.write(lowlow & 0x000000ff);
				outStrm.flush();
				int left = Length;
				int sendonce = 0;
				byte[] Buffer = new byte[BUFFER_LENGTH];
				while(left > 0){
					sendonce = inputStream.read(Buffer);
					outStrm.write(Buffer,0,sendonce);
					outStrm.flush();
					left = left - sendonce;
				}
			} catch (IOException e) {
				try {
					outStrm.close();
					inputStream.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
