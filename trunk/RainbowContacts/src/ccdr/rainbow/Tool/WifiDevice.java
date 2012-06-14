package ccdr.rainbow.Tool;

import android.os.Parcel;
import android.os.Parcelable;

public class WifiDevice implements Parcelable {
	private String m_IPAddress;
	private String m_MacAddress;
	private String m_DeviceName;
	
	public WifiDevice(String name,String ip,String mac)
	{
		m_DeviceName=name;
		m_IPAddress=ip;
		m_MacAddress=mac;
	}
	
	public WifiDevice(Parcel p)
	{
		m_DeviceName=p.readString();
		m_IPAddress=p.readString();
		m_MacAddress=p.readString();
	}
	
	public String getWifiDeviceName(){return m_DeviceName;}
	public String getWifiDeviceIPAddress(){return m_IPAddress;}
	public String getWifiDeviceMacAddress(){return m_MacAddress;}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(m_DeviceName);
		arg0.writeString(m_IPAddress);
		arg0.writeString(m_MacAddress);
	}
	
	public static final Parcelable.Creator<WifiDevice> CREATOR = new Parcelable.Creator<WifiDevice>() { 

        @Override 
        public WifiDevice createFromParcel(Parcel source) { 
                return new WifiDevice(source); 
        } 

        @Override 
        public WifiDevice[] newArray(int size) { 
                return new WifiDevice[size]; 
        } 

};
}
