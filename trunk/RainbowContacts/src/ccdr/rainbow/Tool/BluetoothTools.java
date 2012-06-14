package ccdr.rainbow.Tool;

import android.text.format.Time;

/**
 * һЩ�������䶼Ҫ�õ��ļ򵥲��������������
 * @author Lianghui
 *
 */
public class BluetoothTools {
	/**
	 * combine two byte to compose a unsigned Integer
	 * @param positiveHigh higher 8 bits of the unsigned Integer
	 * @param positiveLow lower  8 bits of the unsigned Integer
	 * @return
	 */
	public static int TwoBytetoInt(byte positiveHigh,byte positiveLow){
		int Int = 0;
		Int = positiveHigh;
		Int = (Int << 8);
		Int = Int + (0x00FF & positiveLow);
		return Int;
	}
	 
	 public static int fourBytetoInt(byte Highhigh,byte Highlow,byte Lowhigh,byte Lowlow){
			int Int = 0;
			Int = Highhigh;
			Int = (Int << 24);
			Int = Int + (0x00FF0000 & (Highlow << 16));
			Int = Int + (0x0000FF00 & (Lowhigh << 8));
			Int = Int + (0x000000FF & Lowlow);
			return Int;
		}
	  /**
	   * ������������ת��Ϊ16���Ƶ��ַ���
	   * @param b
	   * @return
	   */
	  public static String  HexString(byte[] b)
		{
			String whole = "";
			for (int i = 0; i < b.length; i++){
				String hex = Integer.toHexString(b[i] & 0xFF);
				if (hex.length() == 1)
				{
					hex = '0' + hex;
				}
				whole = whole + hex;
			}
			return whole;	
		}
	   public static String convertLongToRFC2445DateTime(long mills) {
	        Time time = new Time();

	        time.set(mills);
	        return time.format("%Y%m%dT%H%M%SZ");
	    }
	  
	  /**
		 * ��byte������whole�в����ж��ٸ���byte�������target
		 * @param whole
		 * @param target
		 * @return ����
		 */
		public static int SearchSubByteArray(byte []whole,byte []target){
			 int count = 0;
			 int tl = target.length;
			 int wl = whole.length;
			 boolean allmatch = false;
			 for(int i = 0; i < wl ; i ++){
				 allmatch = false;
				 if(whole[i] == target[0]){
					allmatch = true;
					 for(int j = 0; j < tl ; j++){
						 if(i + j >= whole.length){
							 allmatch = false;
							 break;
						 }
						 if(whole[i+j] != target[j]){
							 allmatch = false;
							 break;
						 }
				 	}
				 }
				 if(allmatch)count++;
			 }
			 return count;
		 }
}

