package ccdr.rainbow.Tool;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PY_name {
	 public static String getPingYin(String src) {
			
	        char[] t1 = src.toCharArray();
	        
	        String[] t2 = new String[t1.length];
	        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	        //t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	        t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
	        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	        String t4 = "";
	        try {
	            for (int i = 0; i <t1.length; i++) {
	         
	            	
	                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
	                	
	                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);  
	                    t4 += t2[0]+" ";
	                } else {
	                    t4 += java.lang.Character.toString(t1[i])+" ";
	                }
	            }
	            return t4;
	        } catch (BadHanyuPinyinOutputFormatCombination e1) {
	            System.out.println("---");
	        }
	        return t4;
	    }

	 public static String PY(String src){
		 char zim = src.toUpperCase().toCharArray()[0];
		 if (zim >= 'A' && zim <= 'Z')
		 {	
//			 System.out.println("--"+String.valueOf(zim));
          return String.valueOf(zim);
		 }
		 
		return "#";
		 
	 }
}
