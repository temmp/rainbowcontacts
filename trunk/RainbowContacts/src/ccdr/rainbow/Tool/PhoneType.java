package ccdr.rainbow.Tool;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class PhoneType {
	static List <String> NameList = new LinkedList <String>();
	
	public static final int NEXUS_S = 0;
	public static final String NEXUS_S_NAME = "Nexus S";
	static {NameList.add(NEXUS_S_NAME);}
	
	public static final int MB525 = 1;
	public static final String MB525_NAME = "MB525";
	static {NameList.add(MB525_NAME);}
	
	
	public static final int HTC_DESIRE = 2;
	public static final String HTC_DESIRE_NAME = "HTC Desire";
	static {NameList.add(HTC_DESIRE_NAME);}
	
	
	public static final int I5830 = 3;
	
	public static final int NOKIA_E52 = 4;
	
	public static final int NOKIA_C6 = 5;
	
	public static final int NOKIA_E500 = 6;

	public static final int IPHONE4 = 7;
	
	public static final int NOKIA_5800 = 8;
	
	
//	public static int findtype(){
//		int i = 0;
//		String name = Build.MODEL;
//		for(String listele : NameList){
//			if(listele.equalsIgnoreCase(name)){
//				break; 
//			}
//			i++;
//		}
//		Log.d("devicename", "num "+i);
//		return i;
//	}
	
	
	public static int findtype(String name){
		int i = 0;
		name = name.trim();
		for(String listele : NameList){
			String tmp = listele.trim();
			if(tmp.equalsIgnoreCase(name)){
				break; 
			}
			i++;
		}
		Log.d("devicename", "num "+i);
		return i;
	}
}
