package ccdr.rainbow.Tool;

//import it.sauronsoftware.base64.Base64;
//import sun.misc.BASE64Decoder;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.BaseTypes;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.text.TextUtils;
import android.util.Log;
public class VcardOperator {
	private static String lasttime;// 用于显示“上次操作时间”，加为static属性，以便所有类使用
	private static final int CONTACTCOUNT = 10;// 联系人计数，以便每50个人insert一次
	private Context m_context;
	private ContentResolver m_contentResolver;
	private String FN_str =null;
	private String N_str = null;
	private String NOTE_str = "";
	//////////////////////Zhangqx Add////////////////////////
	private boolean isExporting=true;			//标识是否正在导出
	private boolean isStopExporting=false;		//标识是否需要停止导出
	private boolean isImporting=true;			//标识是否正在导入
	private boolean isStopImporting=false;		//标识是否需要停止导入
	private boolean isImportingPhoto = false;  //表示是否需要读取图片
	public boolean getIsExporting(){return isExporting;}
	public boolean getIsStopExporting(){return isStopExporting;}
	public boolean getIsImporting(){return isImporting;}
	public boolean getIsStopImporting(){return isStopImporting;}	
	public void setIsExporting(boolean b){isExporting=b;}
	public void setIsStopExporting(boolean b){isStopExporting=b;}
	public void setIsImporting(boolean b){isImporting=b;}
	public void setIsStopImporting(boolean b){isStopImporting=b;}
	//////////////////////Zhangqx Add////////////////////////
	
	public VcardOperator(Context context) {
		m_context = context;
		m_contentResolver = m_context.getContentResolver();
	}

	/************ 得到显示操作的时间 *************/
	public String getlasttime() {
		return lasttime;
	}

	/************ 设置显示操作的时间 *************/
	public void setlasttime(String time) {
		lasttime = time;
	}

	/************ 保存当前操作的时间 *************/
	public void saveTime(Context context) {

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy.MM.dd  HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String thislasttime = formatter.format(curDate);
		Properties properties = new Properties();
		properties.put("lasttime", thislasttime);
		try {
			FileOutputStream stream = context.openFileOutput("properties.cfg",
					Context.MODE_WORLD_WRITEABLE);
			properties.store(stream, "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	/******************通话记录************************/
	public  Set getCalls(){
		Cursor cursor = getMyContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, "name is not null", null, CallLog.Calls.DEFAULT_SORT_ORDER);
		Set calls = new HashSet<String>();
		int i = 1 ;
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor
						.getColumnIndex("number"));
				// BzeeLog.show("id",id);
				String name = cursor
						.getString(cursor
								.getColumnIndex("name"));
				// BzeeLog.show("name",name);
				System.out.println(id+"--"+name);
				calls.add(name);
				i++;
				if(i==20){
					return calls;
				}
			} while (cursor.moveToNext());
		}
		return calls;
		}
	/************ 从文件中得到用于显示的操作时间 *************/
	public String loadTime(Context context) {
		Properties properties = new Properties();
		String time = null;
		try {
			FileInputStream stream = context.openFileInput("properties.cfg");
			properties.load(stream);
			time = properties.get("lasttime").toString();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return time;
	}

	public void importVcardFileFromSD(String filePath) {
		Intent i = new Intent();
		i.setAction(android.content.Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath), "text/x-vcard");
		m_context.startActivity(i);
	}

	public void importVcardFileFromInputstream(InputStream inputstream)
			throws IOException {
		inputstreamToCachefile(inputstream);
		Intent i = new Intent();
		i.setAction(android.content.Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + m_context.getCacheDir()
				+ "cachefile"), "text/x-vcard");
		m_context.startActivity(i);
	}

	public boolean exportAllContactAsVcardFileToSD(String filePath)
			throws IOException {
		File f = new File("/sdcard/" + filePath + ".vcf");
		if (f.exists()) {
			f.delete();
		}
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		Cursor cur = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED ");
		if (cur.moveToFirst()) {
			do {
				String lookupKey = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				BzeeLog.show("lookupKey", lookupKey);
				// BzeeLog.show("id",
				// cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)));
				Uri uri = Uri.withAppendedPath(
						ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
				AssetFileDescriptor fd = m_contentResolver
						.openAssetFileDescriptor(uri, "r");
				FileInputStream fis = fd.createInputStream();
				BufferedInputStream bfi = new BufferedInputStream(fis);
				InputStreamReader ipr = new InputStreamReader(bfi);
				int temp;
				while ((temp = ipr.read()) != -1) {
					bw.write(temp);
					bw.flush();
				}
				bfi.close();
			} while (cur.moveToNext());
			bw.close();
			return true;
		} else {
			return false;
		}
	}

	public boolean exportSingleContactAsVcardFileToSD(String id, String filePath)
			throws IOException {
		File f = new File("/sdcard/" + filePath + ".vcf");
		if (f.exists()) {
			f.delete();
		}
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		Cursor cur = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null,
				BaseColumns._ID + "=" + id, null, " display_name COLLATE LOCALIZED ");
		cur.moveToFirst();
		String lookupKey = cur.getString(cur
				.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
		BzeeLog.mark(lookupKey);
		Uri uri = Uri.withAppendedPath(
				ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
		AssetFileDescriptor fd = m_contentResolver.openAssetFileDescriptor(uri,
				"r");
		FileInputStream fis = fd.createInputStream();
		BufferedInputStream bfi = new BufferedInputStream(fis);
		InputStreamReader ipr = new InputStreamReader(bfi);
		int temp;
		while ((temp = ipr.read()) != -1) {
			bw.write(temp);
			bw.flush();
		}
		bfi.close();
		bw.close();
		return true;
	}

	public OutputStream exportAllContactToVcardFileAsOutputstream()
			throws IOException {
		File file = new File(m_context.getCacheDir(), "cachefile");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		Cursor cur = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED ");
		if (cur.moveToFirst()) {
			do {
				String lookupKey = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
				Uri uri = Uri.withAppendedPath(
						ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
				AssetFileDescriptor fd = m_contentResolver
						.openAssetFileDescriptor(uri, "r");
				FileInputStream fis = fd.createInputStream();
				BufferedInputStream bfi = new BufferedInputStream(fis);
				InputStreamReader ipr = new InputStreamReader(bfi);
				int temp;
				while ((temp = ipr.read()) != -1) {
					bw.write(temp);
					bw.flush();
				}
				bfi.close();
			} while (cur.moveToNext());
			bw.close();
			FileOutputStream fo = new FileOutputStream(file);
			return fo;
		} else {
			return null;
		}
	}

	public OutputStream exportSingleContactToVcardFileAsOutputstream(String id)
			throws IOException {
		File file = new File(m_context.getCacheDir(), "cachefile");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		Cursor cur = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null,
				BaseColumns._ID + "=" + id, null, " display_name COLLATE LOCALIZED ");
		if (cur.moveToFirst()) {
			String lookupKey = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
			AssetFileDescriptor fd = m_contentResolver.openAssetFileDescriptor(
					uri, "r");
			FileInputStream fis = fd.createInputStream();
			BufferedInputStream bfi = new BufferedInputStream(fis);
			InputStreamReader ipr = new InputStreamReader(bfi);
			int temp;
			while ((temp = ipr.read()) != -1) {
				bw.write(temp);
				bw.flush();
			}
			bfi.close();
			bw.close();
			FileOutputStream fo = new FileOutputStream(file);
			return fo;
		} else {
			return null;
		}
	}

	public boolean deleteContactById(String id) {
		if (id.equals("") || id == null) {
			return false;
		}
		Uri uri = ContentUris.withAppendedId(
				ContactsContract.RawContacts.CONTENT_URI, Integer.parseInt(id));
		Uri.Builder b = uri.buildUpon();
		b.appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true");
		uri = b.build();
		m_contentResolver.delete(uri, null, null);
		return true;
	}

	public boolean deleteAllContacts() {
		// 这里只能用RawContacts.CONTENT_URI
		Uri uri = ContactsContract.RawContacts.CONTENT_URI.buildUpon()
				.appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER,
						"true").build();
		m_contentResolver.delete(uri, null, null);
		return true;
	}

	public String getRawIdByFn(String fn) {
		String id = null;
		String selection = ContactsContract.Data.MIMETYPE
				+ "='"
				+ ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
				+ "'" + " AND " + ContactsContract.Data.DISPLAY_NAME + "='"
				+ fn + "'";
		// + " LIKE " + "'%" + fn + "%'";
		Cursor cursor = m_contentResolver.query(
				ContactsContract.Data.CONTENT_URI,
				new String[] { ContactsContract.Data.RAW_CONTACT_ID },
				selection, null, null);
		if (cursor.moveToFirst()) {
			id = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
			/*
			 * do{
			 * 
			 * } while(cursor.moveToNext());
			 */
		}
		return id;
	}

	public boolean addXbyContactName(String xTypeName, String xValue, String fn)
			throws RemoteException, OperationApplicationException {
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Data.RAW_CONTACT_ID, getRawIdByFn(fn));
		values.put(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, xValue);
		values.put(Phone.TYPE, BaseTypes.TYPE_CUSTOM);
		values.put(Phone.LABEL, xTypeName);
		m_contentResolver.insert(ContactsContract.Data.CONTENT_URI, values);
		return true;
	}

	public void xTypeVcardImport(File vcardFile) {
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////

	private final int TYPE_URL = 1;
	private final int TYPE_FN = 2;
	private final int TYPE_TEL_HOME_VOICE = 3;
	private final int TYPE_TEL_CELL = 4;
	private final int TYPE_TEL_WORK_VOICE = 5;
	private final int TYPE_TEL_WORK_FAX = 6;
	private final int TYPE_TEL_HOME_FAX = 7;
	private final int TYPE_TEL_PAGER = 8;
	private final int TYPE_TEL_VOICE = 9;
	private final int TYPE_TEL_ISDN = 10;
	private final int TYPE_TEL_DEFAULT = 11;
	private final int TYPE_EMAIL_HOME = 12;
	private final int TYPE_EMAIL_WORK = 13;
	private final int TYPE_EMAIL_INTERNET = 14;
	private final int TYPE_EMAIL_CELL = 15;
	private final int TYPE_ORG = 16;
	private final int TYPE_TITLE = 17;
	private final int TYPE_NOTE = 18;
	private final int TYPE_N = 19;
	private final int TYPE_ADR = 20;
	private final int TYPE_IS_TEL = 21;
	private final int TYPE_IS_EMAIL = 22;
	

	private final int TYPE_X_AIM = 23;
	private final int TYPE_X_MSN = 24;
	private final int TYPE_X_YAHOO = 25;
	private final int TYPE_X_QQ = 26;
	private final int TYPE_X_ICQ = 27;
	private final int TYPE_X_JABBER = 28;
	private final int TYPE_X_GOOGLE_TALK = 29;
	private final int TYPE_X_SKYPE = 30;

	private final int TYPE_X_IMPP = 31;// 这个字段是NOKIA才有的(用于显示“及时消息”)

	private final int TYPE_X_CUSTOM = 32;
	private final int TYPE_NICKNAME = 33;

	private final int TYPE_PHOTO = 34;
	private final int TYPE_EVENT_BDAY = 35;
	private int importingContactCount = 0;
	private String importingContactFN = "";

	/*
	 * public ArrayList<String> cutBigVcardToManyContactVcardString(File
	 * vcardFile){ ArrayList<String> contactsVcardStringList = new
	 * ArrayList<String>(); String bigString = vcardfileToString(vcardFile);
	 * if(bigString.indexOf("BEGIN:VCARD") < bigString.indexOf("END:VCARD")){
	 * 
	 * } String temp = null; bigString.indexOf("BEGIN:VCARD" ,
	 * bigString.indexOf("END:VCARD")); contactsVcardStringList.add(object)
	 * return contactsVcardStringList; }
	 * 
	 * public ArrayList<String> cutBigVcardStringToManyContactVcardString(String
	 * vcardString){ ArrayList<String> contactsVcardStringList = new
	 * ArrayList<String>();
	 * 
	 * return contactsVcardStringList; }
	 */

	public File StringToCachefile(String str) throws IOException {
		File file = new File(m_context.getCacheDir(), "cacheStrfile");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(str);
		bw.flush();
		bw.close();
		return file;
	}

	public String vcardfileToString(File vcardFile) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader(vcardFile));
		String content = "";
		StringBuilder sb = new StringBuilder();
		while (content != null) {
			content = bf.readLine();
			if (content == null) {
				break;
			}
			sb.append(content.trim());
			sb.append("\r\n");
		}
		bf.close();
		return sb.toString();
	}

	public int getVcardContactsCount(File vcardFile) throws IOException {
		int beginCount = 0;
		int endCount = 0;
		int count = 0;
		BufferedReader bf = new BufferedReader(new FileReader(vcardFile));
		String content;
		while ((content = bf.readLine()) != null) {
			if (content.contains("BEGIN:VCARD")) {
				beginCount++;
			} else if (content.contains("END:VCARD")) {
				endCount++;
			}
		}
		bf.close();
		if (beginCount > endCount) {
			count = beginCount;
		} else {
			count = endCount;
		}

		return count;
	}

	public File inputstreamToCachefile(InputStream inputstream)
			throws IOException {
		File file = new File(m_context.getCacheDir(), "cachefile");
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedInputStream bfi = new BufferedInputStream(inputstream);
		InputStreamReader ipr = new InputStreamReader(bfi);
		int temp;
		while ((temp = ipr.read()) != -1) {
			bw.write(temp);
			bw.flush();
		}
		bfi.close();
		return file;
	}

	/**************** 对Vcard文件进行解析与导入 ******************/
	public boolean importVcardByMyself(Context context, File vcardFile)
			throws IOException {
		String total = "";
		
		/////////////Zhangqx Add/////////////////
		//每次执行前先把标识设置回默认以免受到修改后的影响
		setIsImporting(true);
		setIsStopImporting(false);
		/////////////Zhangqx Add/////////////////

//		MapEngineExchange exchange = new MapEngineExchange();
//		exchange.Exchange(context, vcardFile, "LG", Build.MODEL);

		// 利用映射引擎对Vcard文件进行转换，其中Build.MODEL得到自己本手机型号

		importingContactCount = 0;
		BufferedReader bf = new BufferedReader(new FileReader(vcardFile));
		long rawContactId = queryMaxContact();// 导入时先获取当前联系人列表中当前最大的ID值
		String content;

		LinkedList<ContentValues> ListFN = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListN = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListTEL = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListEMAIL = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListADR = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListURL = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListORG = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListTITLE = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListNOTE = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListNICKNAME = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListEvent = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListPHOTO = new LinkedList<ContentValues>();
		LinkedList<ContentValues> ListIM = new LinkedList<ContentValues>();


		// 以上这些ArrayList是一个联系人的所有属性，定义每一个属性为一个List
		LinkedList<ContactClass> CC = new LinkedList<ContactClass>();
		// CC这个List用于存放所有的联系人，以便后边批量插入
		CC.clear();

		int ContactCout = 0;// 对分析出来的联系人进行计数，每50个insert一次
//		String firststr = null;
//		String endstr = null;
//		String firstN = null;
//		boolean tag_pass = false;
		while ((content = bf.readLine()) != null) {
			/////////////Zhangqx Add/////////////////
//			firststr = endstr;
//			endstr = content;
//			if((firststr!=null)&&(firststr.trim()).equals(endstr.trim())){
//				continue;
//			}		
//			if(tag_pass == true){
//				if(content.contains("END:VCARD")){
//					tag_pass = false;
//					continue;
//				}else{
//					continue;
//				}
//			}
			read_photo:
			if(isImportingPhoto){
				
				if(content.contains(":")){
					isImportingPhoto = false;
					
					ListPHOTO.add(insertPHOTO(total, rawContactId));	
					break read_photo;
				}
				
				total += content;
				continue;
			}

			if(!isImporting)
			{
				while(true)
				{
				if(isImporting){break;}
				if(isStopImporting){
				ListFN = null;ListN = null;ListTEL = null;ListEMAIL = null;ListADR = null;
				ListURL = null;ListORG = null;ListTITLE = null;ListNOTE = null;ListIM = null;ListEvent = null;ListPHOTO = null;
				bf.close();
				return false;}
				}
			}
			/////////////Zhangqx Add///////////////////
			if (( !content.contains("PHOTO"))&&content.endsWith(":")) {
				continue;
			}
			if (afterStringNotEnd) {
				// BzeeLog.mark("tempAfterString");
				tempAfterString = tempAfterString + content;
				BzeeLog.mark(tempAfterString);
				ListN.add(insertN(tempAfterString, rawContactId, true));
				ContactClass contactclass = new ContactClass();// 这是一个联系人

				ContactCout++;
				contactclass.setListN(ListN);
				CC.add(contactclass);// 添加一个联系人进入List

				if (ContactCout == CONTACTCOUNT) {
					ContactCout = 0;
					ContactInsert contactinsert = new ContactInsert(m_context);
					contactinsert.contactinsert(CC);// 批量插入联系人List
					CC.clear();
				}
				continue;
			}
			content = content.trim();
			// BzeeLog.mark(content);
			if (content.contains("BEGIN:VCARD")) {
				importingContactCount++;
				rawContactId++;
				// rawContactId = createNewContact();
				FN_str = null;
				N_str = null;
				NOTE_str = "";
				ListFN = null;
				ListN = null;
				ListTEL = null;
				ListEMAIL = null;
				ListADR = null;
				ListURL = null;
				ListORG = null;
				ListTITLE = null;
				ListNOTE = null;
				ListNICKNAME = null;
				ListEvent = null;
				ListPHOTO = null;
				ListIM = null;


				ListFN = new LinkedList<ContentValues>();
				ListN = new LinkedList<ContentValues>();
				ListTEL = new LinkedList<ContentValues>();
				ListEMAIL = new LinkedList<ContentValues>();
				ListADR = new LinkedList<ContentValues>();
				ListURL = new LinkedList<ContentValues>();
				ListORG = new LinkedList<ContentValues>();
				ListTITLE = new LinkedList<ContentValues>();
				ListNOTE = new LinkedList<ContentValues>();
				ListNICKNAME = new LinkedList<ContentValues>();
				ListEvent = new LinkedList<ContentValues>();
				ListPHOTO = new LinkedList<ContentValues>();
				ListIM = new LinkedList<ContentValues>();


			} else if (content.contains("END:VCARD")) {
				ContactCout++;// 计数
				ContactClass contactclass = new ContactClass();// 这是一个联系人
				contactclass.setListN(ListN);	
				System.out.println("-----NOTE_str---"+NOTE_str);
				if(!NOTE_str.equals("")){
				ListNOTE.add(insertNote(NOTE_str, rawContactId, true));
				}
				if(N_str != null){
//					System.out.println(ListFN==null);
					System.out.println("N--2321----"+N_str);
					ListFN = new LinkedList<ContentValues>();
				}
				System.out.println("---ListFN---"+ListFN.toString());
				System.out.println("---ListN---"+ListN.toString());
				System.out.println("---ListTEL---"+ListTEL.toString());
				System.out.println("---ListEMAIL---"+ListEMAIL.toString());
				System.out.println("---ListADR---"+ListADR.toString());
				System.out.println("---ListURL---"+ListURL.toString());
				System.out.println("---ListORG---"+ListORG.toString());
				System.out.println("---ListTITLE---"+ListTITLE.toString());
				System.out.println("---ListNOTE---"+ListNOTE.toString());
				System.out.println("---ListNICKNAME---"+ListNICKNAME.toString());
				System.out.println("---ListEvent---"+ListEvent.toString());
				System.out.println("---ListPHOTO---"+ListPHOTO.toString());
				System.out.println("---ListIM---"+ListIM.toString());	
				contactclass.setListFN(ListFN);	
				FN_str = null;
				N_str = null;
				NOTE_str = "";
				contactclass.setListTEL(ListTEL);
				contactclass.setListEMAIL(ListEMAIL);
				contactclass.setListADR(ListADR);
				contactclass.setListURL(ListURL);
				contactclass.setListORG(ListORG);
				contactclass.setListTITLE(ListTITLE);
//				ListNOTE = new LinkedList<ContentValues>();
				contactclass.setListNOTE(ListNOTE);
				contactclass.setListNICKNAME(ListNICKNAME);
				contactclass.setListEvent(ListEvent);
				contactclass.setListPHOTO(ListPHOTO);

				contactclass.setListIM(ListIM);
				if((ListFN.toString()+ListADR.toString()+ListEMAIL.toString()+ListEvent.toString()
						+ListIM.toString()+ListN.toString()+ListNICKNAME.toString()+ListNOTE.toString()+ListORG.toString()+ListPHOTO.toString()
						+ListTEL.toString()+ListTITLE.toString()+ListURL.toString()).length()<30){
					continue;
				}
				CC.add(contactclass);// 添加一个联系人进入List

				if (ContactCout == CONTACTCOUNT) {
					ContactCout = 0;
					ContactInsert contactinsert = new ContactInsert(m_context);
					contactinsert.contactinsert(CC);// 批量插入联系人List
					CC.clear();
				}
			} else if (content.contains(":")) {
			System.out.println("----sd");
				int maohaoPosition = 0;
				String before = null;
				String after = null;
				maohaoPosition = content.indexOf(":");
				before = content.substring(0, maohaoPosition).trim();
				Log.e("before", before);
				after = content.substring(maohaoPosition + 1).trim();
				Log.e("after", after);
				System.out.println("1--before--"+before);
				int type = getTypeOfBefore(before);
				switch (type) {
				case TYPE_FN: {
					String result = "";
					while(after.endsWith("=")) {
						result = result + after.substring(0, after.lastIndexOf("="));
						after = bf.readLine();
					}
					result = result + after;
					Log.v("整合为一行：", result);
					System.out.println("FNFNFNFNFNFNNFNFNFNF"+result);
					ListFN.add(insertFN(result, rawContactId, true));
					break;
				}
				case TYPE_N: {
					String result = "";
					while(after.endsWith("=")) {
						result = result + after.substring(0, after.lastIndexOf("="));
						after = bf.readLine();
					}
					System.out.println("after--"+after);
//					if(after.contains(";")&&(after.indexOf(";")==0)){
//						after = after.substring(1).trim();
//						System.out.println(after+"--if");
//					}
					result = result + after;
//					if(firstN==null){
//						firstN = result;
//					}else if(firstN.equals(result)){
//						tag_pass = true;
//					}else{
//						firstN = result;
//					}
					
					System.out.println("result--11--"+result);
					Log.v("整合为一行：", result);
					if(!result.equals("")){
					ListN.add(insertN(result, rawContactId, true));
					}
					break;
				}
				case TYPE_IS_TEL: {
					ListTEL.add(insertTEL(before, after, rawContactId));
					break;
				}
				case TYPE_IS_EMAIL: {
					ListEMAIL.add(insertEmail(before, after, rawContactId, true));
					break;
				}
				case TYPE_ADR: {
					String result = "";
					while(after.endsWith("=")) {
						result = result + after.substring(0, after.lastIndexOf("="));
						after = bf.readLine();
					}
					result = result + after;
					Log.v("整合为一行：", result);
					
					ListADR.add(insertADR(before, result, rawContactId, true));
					break;
				}
				case TYPE_URL: {
					ListURL.add(insertURL(after, rawContactId));
					break;
				}
				case TYPE_ORG: {
					String result = "";
					while(after.endsWith("=")) {
						result = result + after.substring(0, after.lastIndexOf("="));
						after = bf.readLine();
					}
					result = result + after;
					Log.v("整合为一行：", result);
					ListORG.add(insertORG(result, rawContactId, true));
					break;
				}
				case TYPE_TITLE: {
						ListTITLE.add(insertTitle(after, rawContactId, true));
					break;
				}
				case TYPE_NOTE: {
					String result = "";
					while(after.endsWith("=")) {
						result = result + after.substring(0, after.lastIndexOf("="));
						after = bf.readLine();
					}
					result = result + after;
					Log.v("整合为一行：", result);
					NOTE_str +=result; 
					System.out.println("1111fh---------fdf-df-df-daf-f--f-daf-d-f-"+ListNOTE.toString());
					
					System.out.println("2222fh---------fdf-df-df-daf-f--f-daf-d-f-"+ListNOTE.toString());
					break;
				}

				case TYPE_X_AIM: {
					ListIM.add(insertIM(TYPE_X_AIM, after, rawContactId, true));
					break;

				}
				case TYPE_X_MSN: {
					ListIM.add(insertIM(TYPE_X_MSN, after, rawContactId, true));
					break;
				}
				case TYPE_X_YAHOO: {
					ListIM
							.add(insertIM(TYPE_X_YAHOO, after, rawContactId,
									true));
					break;
				}
				case TYPE_X_QQ: {
					ListIM.add(insertIM(TYPE_X_QQ, after, rawContactId, true));
					break;
				}
				case TYPE_X_GOOGLE_TALK: {
					ListIM.add(insertIM(TYPE_X_GOOGLE_TALK, after,
							rawContactId, true));
					break;
				}
				case TYPE_X_ICQ: {
					ListIM.add(insertIM(TYPE_X_ICQ, after, rawContactId, true));
					break;
				}
				case TYPE_X_JABBER: {
					ListIM.add(insertIM(TYPE_X_JABBER, after, rawContactId,
							true));
					break;
				}
				case TYPE_X_SKYPE: {
					ListIM.add(insertIM(TYPE_X_SKYPE, after, rawContactId,
									true));
					break;
				}
				case TYPE_X_IMPP: {
					// 这个type是用来处理NOKIA中的AIM、YAHOO等字段，因为它的格式特殊，所以只能写在解析器中
					int maohao = 0;
					String beforetype = null;
					String data = null;
					maohao = after.indexOf(":");
					beforetype = after.substring(0, maohao).trim();
					data = after.substring(maohao + 1).trim();

					int IMPPTYPE = -9999;

					if (beforetype.equals("MSN")) {
						IMPPTYPE = TYPE_X_MSN;
					} else if (beforetype.equals("Yahoo")) {
						IMPPTYPE = TYPE_X_YAHOO;
					} else if (beforetype.equals("Google")) {
						IMPPTYPE = TYPE_X_GOOGLE_TALK;
					} else if (beforetype.equals("Skype")) {
						IMPPTYPE = TYPE_X_SKYPE;
					} else {
						IMPPTYPE = TYPE_X_CUSTOM;//这是“自定义”的字段
					}

					try {
						data = encodingUTF8(data);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					ListIM.add(insertIM(IMPPTYPE, data, rawContactId, true));

					break;
				}
				case TYPE_NICKNAME: {
					ListNICKNAME.add(insertNICKNAME(after, rawContactId,
							true));
					break;
				}
				case TYPE_EVENT_BDAY: {
//					ListEvent.add(insertBDAY(before, after, rawContactId));
					break;
				}
				case TYPE_PHOTO: {
					isImportingPhoto = true;
					total = after;

					break;
				}
				}

			} else {
				continue;
			}
		}
		ContactInsert contactinsert = new ContactInsert(m_context);
		contactinsert.contactinsert(CC);// 批量插入联系人List
		bf.close();
		return true;
	}

	/***************** 获取当前联系人列表中当前最大的ID值 *********************/
	private long queryMaxContact() {

		Cursor cursor = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null,
				BaseColumns._ID);// 遍历Contract的SQLite的游标
		if (cursor.moveToLast()) {
			/******* 获取最后一个用户的ID号 *******/
			long contactId = cursor.getLong(cursor
					.getColumnIndex(BaseColumns._ID));
			return contactId;
		} else {
			return 0;
		}
	}

	/***************** 获取当前联系人的NICKNAME *********************/
	private ContentValues insertNICKNAME(String str, long rawContactId,
			boolean isUTF8) {
		if(str.contains(";")) {
			int fenhaoPosition = 0;
			fenhaoPosition = str.indexOf(";");
			str = str.substring(fenhaoPosition + 1).trim();
			fenhaoPosition = str.indexOf(";");
			str = str.substring(0, fenhaoPosition).trim();
		}
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Nickname.CONTENT_ITEM_TYPE);
		values.put(Nickname.NAME, str);
		
		return values;
	}
	
	/******************获取当前联系人的Event*******************/
	private ContentValues insertBDAY(String before,String after, long rawContactId) {
		
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE);
		values.put(Event.START_DATE, after);
//		int type = getTypeOfBefore(before);
//		switch (type) {
//			case TYPE_EVENT_BDAY:{
				values.put(Event.TYPE, Event.TYPE_BIRTHDAY);
//				System.out.println("----123values---"+values);
//			break;
//			}
//		}
		return values;
	}

	/***************** 获取当前联系人的IM *********************/
	private ContentValues insertIM(int TYPE, String str, long rawContactId,
			boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int type = getIMType(TYPE);

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
		values.put(Im.DATA, str);

		values.put(Im.PROTOCOL, type);
		return values;
	}

	/***************** 获取当前联系人IM的类型 *********************/
	private int getIMType(int TYPE) {
		if (TYPE == TYPE_X_AIM) {
			return Im.PROTOCOL_AIM;
		} else if (TYPE == TYPE_X_MSN) {
			return Im.PROTOCOL_MSN;
		} else if (TYPE == TYPE_X_YAHOO) {
			return Im.PROTOCOL_YAHOO;
		} else if (TYPE == TYPE_X_QQ) {
			return Im.PROTOCOL_QQ;
		} else if (TYPE == TYPE_X_GOOGLE_TALK) {
			return Im.PROTOCOL_GOOGLE_TALK;
		} else if (TYPE == TYPE_X_ICQ) {
			return Im.PROTOCOL_ICQ;
		} else if (TYPE == TYPE_X_JABBER) {
			return Im.PROTOCOL_JABBER;
		} else if (TYPE == TYPE_X_SKYPE) {
			return Im.PROTOCOL_SKYPE;
		} else if (TYPE == TYPE_X_CUSTOM) {
			return Im.PROTOCOL_CUSTOM;
		}
		return Im.PROTOCOL_CUSTOM;
	}


	/***************** 获取当前联系人的电话号码 *********************/
	private ContentValues insertTEL(String before, String after,
			long rawContactId) {
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, after);
		int type = getTelType(before);
		switch (type) {
		case TYPE_TEL_HOME_VOICE: {
			values.put(Phone.TYPE, Phone.TYPE_HOME);
			
			break;
		}
		case TYPE_TEL_CELL: {
			values.put(Phone.TYPE, Phone.TYPE_MOBILE);
			break;
		}
		case TYPE_TEL_WORK_VOICE: {
			values.put(Phone.TYPE, Phone.TYPE_WORK);
			break;
		}
		case TYPE_TEL_WORK_FAX: {
			values.put(Phone.TYPE, Phone.TYPE_FAX_WORK);
			break;
		}
		case TYPE_TEL_HOME_FAX: {
			values.put(Phone.TYPE, Phone.TYPE_FAX_HOME);
			break;
		}
		case TYPE_TEL_PAGER: {
			values.put(Phone.TYPE, Phone.TYPE_PAGER);
			break;
		}
		case TYPE_TEL_VOICE: {
			values.put(Phone.TYPE, Phone.TYPE_OTHER);
			break;
		}
		case TYPE_TEL_ISDN: {
			values.put(Phone.TYPE, Phone.TYPE_ISDN);
			break;
		}
		case TYPE_TEL_DEFAULT: {
			values.put(Phone.TYPE, Phone.TYPE_OTHER);
			break;
		}
		}
//		System.out.println("---987values---"+values);
		return values;
	}

	/***************** 获取当前联系人的Email *********************/
	private ContentValues insertEmail(String before, String after,
			long rawContactId,boolean isUTF8) {
		
		if (isUTF8) {
			try {
				after = encodingUTF8(after);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.v("emailafter",after);
		
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
		values.put(Email.DATA, after);
		int type = getEmailType(before);
		switch (type) {
		case TYPE_EMAIL_HOME: {
			values.put(Email.TYPE, Email.TYPE_HOME);
			break;
		}
		case TYPE_EMAIL_WORK: {
			values.put(Email.TYPE, Email.TYPE_WORK);
			break;
		}
		case TYPE_EMAIL_INTERNET: {
			values.put(Email.TYPE, Email.TYPE_OTHER);
			break;
		}
		case TYPE_EMAIL_CELL: {
			values.put(Email.TYPE, Email.TYPE_MOBILE);
			break;
		}
		}
		return values;
	}

	/***************** 获取当前联系人的name *********************/
	private ContentValues insertN(String after, long rawContactId,
			boolean isUTF8) {
		if (!isAfterStringComplete(after, TYPE_N)) {
			// tempAfterString = after;
			// afterStringNotEnd = true;
			// temptype = TYPE_N;
			return null;
		}else{
			N_str = after;
		}
		int fenhaoCount_tag = 0;
		String fenhaostr =after;
		while (fenhaostr.contains(";")) {
			fenhaoCount_tag++;
			fenhaostr = fenhaostr.substring(fenhaostr.indexOf(";") + 1);
		}
			if(fenhaoCount_tag==0){
			
			after = after + ";;;;";
		}else if(fenhaoCount_tag ==1){
			after = after +";;;";
			
		}else if(fenhaoCount_tag ==2){
			after = after +";;";
			
		}else if(fenhaoCount_tag ==3){
			after = after +";";
			
		}
		afterStringNotEnd = false;
		// Log.e("调试", "insertN");
		String displayname = null;
		String familyName, givenName, middleName, namePrefix, nameSuffix;
		familyName = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		givenName = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		middleName = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		namePrefix = after.substring(0, after.indexOf(";"));
		nameSuffix = after.substring(after.indexOf(";") + 1);

		// BzeeLog.mark(familyName);
		// BzeeLog.mark(givenName);
		// BzeeLog.mark(middleName);
		
		if (isUTF8) {
			// BzeeLog.mark("开始解码");
			try {
				familyName = encodingUTF8(familyName);
				givenName = encodingUTF8(givenName);
				middleName = encodingUTF8(middleName);
				namePrefix = encodingUTF8(namePrefix);
				nameSuffix = encodingUTF8(nameSuffix);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		System.out.println("familyName==" + familyName);
//		System.out.println("givenName==" + givenName);
//		System.out.println("middleName==" + middleName);
		// BzeeLog.mark(familyName);
		// BzeeLog.mark(givenName);
		// BzeeLog.mark(middleName);

		if (!givenName.equals("")) {
			importingContactN = givenName;
		} else if (!familyName.equals("")) {
			importingContactN = familyName;
		} else if (middleName.equals("")) {
			importingContactN = middleName;
		} else {
			importingContactN = "no name";
		}
	  /****************************************************/
	        if (!(TextUtils.isEmpty(familyName) && TextUtils.isEmpty(givenName))) {
	            StringBuilder builder = new StringBuilder();
	            List<String> nameList;

	           
	            if(ChangeSkinUtil.able.equals("CN")){
	                if (containsOnlyPrintableAscii(familyName) &&
	                        containsOnlyPrintableAscii(givenName)) {
	                    nameList = Arrays.asList(namePrefix, givenName, middleName, familyName, nameSuffix);
	                    System.out.println("asjflkadsfkldsajfsfjlaskfjadslkfjdklajflkajfdlk;jf;lakjfdk;jfd;lsjdlk;sa;ldj");
	                } else {
	                	System.out.println("4545343502426546745343543054354444444444563333333333333333333");
	                    nameList = Arrays.asList(namePrefix, familyName, middleName, givenName, nameSuffix);
	                }
	            }
	            else{
	            	System.out.println("``````````````````````````````````````````````````````````````````````````````````````");
	                nameList = Arrays.asList(namePrefix, middleName, givenName, familyName, nameSuffix);
	            }
	            boolean first = true;
	            for (String namePart : nameList) {
	                if (!TextUtils.isEmpty(namePart)) {
	                    if (first) {
	                        first = false;
	                    } else {
	                        builder.append(' ');
	                    }
	                    builder.append(namePart);
	                }
	            }
	            displayname = builder.toString();
	        } else if (!TextUtils.isEmpty(FN_str)) {
	        	displayname = FN_str;
	        } 

	        if (displayname == null) {
	        	displayname = "";
	        }
	    /************************************/
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);System.out.println("rawContactId--"+rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);System.out.println("StructuredName.CONTENT_ITEM_TYPE--"+StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.FAMILY_NAME, familyName);System.out.println("familyName--"+familyName);
		values.put(StructuredName.GIVEN_NAME, givenName);System.out.println("givenName--"+givenName);
		values.put(StructuredName.MIDDLE_NAME, middleName);System.out.println("middleName--"+middleName);
		values.put(StructuredName.PREFIX, namePrefix);System.out.println("namePrefix--"+namePrefix);
		values.put(StructuredName.SUFFIX, nameSuffix);System.out.println("nameSuffix--"+nameSuffix);

		values.put(StructuredName.PHONETIC_FAMILY_NAME, familyName);System.out.println("familyName--"+familyName);
		values.put(StructuredName.PHONETIC_MIDDLE_NAME, middleName);System.out.println("middleName--"+middleName);
		values.put(StructuredName.PHONETIC_GIVEN_NAME, givenName);System.out.println("givenName--"+givenName);
		values.put(StructuredName.DISPLAY_NAME,displayname.trim());System.out.println("displayname--"+displayname);
		/*
		 * values.put(StructuredName.PHONETIC_FAMILY_NAME , familyName +
		 * givenName); values.put(StructuredName.PHONETIC_GIVEN_NAME ,
		 * familyName + givenName);
		 * values.put(StructuredName.PHONETIC_MIDDLE_NAME , familyName +
		 * givenName);
		 */

		return values;
	}

	/***************** 获取当前联系人的住址 *********************/
	private ContentValues insertADR(String before, String after,
			long rawContactId, boolean isUTF8) {
		// BzeeLog.show("isAfterStringComplete(after, TYPE_ADR)",
		// isAfterStringComplete(after, TYPE_ADR));
		if (!isAfterStringComplete(after, TYPE_ADR)) {
			// tempAfterString = after;
			// afterStringNotEnd = true;
			// temptype = TYPE_ADR;
			return null;
		}
		int fenhaoAdr_tag = 0;
		String fenhaoAdr_str =after;
		while (fenhaoAdr_str.contains(";")) {
			fenhaoAdr_tag++;
			fenhaoAdr_str = fenhaoAdr_str.substring(fenhaoAdr_str.indexOf(";") + 1);
		}
			if(fenhaoAdr_tag==0){
			
			after = after + ";;;;;;";
		}else if(fenhaoAdr_tag ==1){
			after = after +";;;;;";
			
		}else if(fenhaoAdr_tag ==2){
			after = after +";;;;";
			
		}else if(fenhaoAdr_tag ==3){
			after = after +";;;";
			
		}else if(fenhaoAdr_tag ==4){
			after = after +";;";
			
		}else if(fenhaoAdr_tag ==5){
			after = after +";";
			
		}
		afterStringNotEnd = false;

		String pobox, extendedAddress,street, city, state, zipCode, country;
		pobox = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		extendedAddress = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		street = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		city = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		state = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
		zipCode = after.substring(0, after.indexOf(";"));
		country = after.substring(after.indexOf(";") + 1);

		if (isUTF8) {
			try {
				pobox = encodingUTF8(pobox);
				extendedAddress = encodingUTF8(extendedAddress);
				street = encodingUTF8(street);
				city = encodingUTF8(city);
				state = encodingUTF8(state);
				zipCode = encodingUTF8(zipCode);
				country = encodingUTF8(country);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
		values.put(StructuredPostal.POBOX, pobox);
		values.put(StructuredPostal.STREET, street);
		values.put(StructuredPostal.CITY, city);
		values.put(StructuredPostal.REGION, state);
		values.put(StructuredPostal.POSTCODE, zipCode);
		values.put(StructuredPostal.COUNTRY, country);
		if (before.contains("HOME")) {
			values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_HOME);
		} else if (before.contains("WORK")) {
			values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
		} else {
			values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_OTHER);
		}
		return values;
	}

	// public long createNewContact() {
	// ContentValues values = new ContentValues();
	// Uri rawContactUri = m_contentResolver.insert(RawContacts.CONTENT_URI,
	// values);
	// long rawContactId = ContentUris.parseId(rawContactUri);
	// return rawContactId;
	// }

	/***************** 获取当前联系人的FN *********************/
	private ContentValues insertFN(String str, long rawContactId, boolean isUTF8) {
		 FN_str = str;
		System.out.println("FN--"+str);
		if (isUTF8) {
			try {
				FN_str = encodingUTF8(FN_str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		importingContactFN = FN_str;

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.DISPLAY_NAME, FN_str);
		return values;
	}

	/***************** 获取当前联系人的URL *********************/
	private ContentValues insertURL(String str, long rawContactId) {
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Website.CONTENT_ITEM_TYPE);
		values.put(Website.URL, str);
		return values;
	}

	/***************** 获取当前联系人的公司 *********************/
	private ContentValues insertORG(String str, long rawContactId,
			boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int position = str.indexOf(";");
		if(str.contains(";")) {
			str = str.substring(0, position);
		}

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
		values.put(Organization.COMPANY, str);
		values.put(Organization.TYPE, Organization.TYPE_OTHER);
		return values;
	}
	
	/***************** 获取当前联系人的组织 *********************/
	private ContentValues insertTitle(String str, long rawContactId,
			boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
		values.put(Organization.TITLE, str);
		values.put(Organization.TYPE, Organization.TYPE_OTHER);
		return values;
	}

	/***************** 获取当前联系人的备注 *********************/
	private ContentValues insertNote(String str, long rawContactId,
			boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
		values.put(Note.NOTE, str);
		return values;
	}
	/******************获得当前头像******************************/
	private ContentValues insertPHOTO(String str,long rawContactId){
		ContentValues values = new ContentValues();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
		byte[] b = Base64.decode(str,Base64.DEFAULT);
		values.put(Photo.PHOTO,b);
		return values;
		 
		
	}
	/***************** 获取当前电话号码的类型 *********************/
	private int getTelType(String before) {
		if (before.contains("HOME")) {
			if (before.contains("VOICE")) {
				return TYPE_TEL_HOME_VOICE;
			} else if (before.contains("FAX")) {
				return TYPE_TEL_HOME_FAX;
			} else {
				return TYPE_TEL_HOME_VOICE;
			}
		} else if (before.contains("WORK")) {
			if (before.contains("VOICE")) {
				return TYPE_TEL_WORK_VOICE;
			} else if (before.contains("FAX")) {
				return TYPE_TEL_WORK_FAX;
			} else {
				return TYPE_TEL_WORK_VOICE;
			}
		} else if (before.contains("CELL")) {
			return TYPE_TEL_CELL;
		} else if (before.contains("PAGER")) {
			return TYPE_TEL_PAGER;
		} else if (before.contains("VOICE")) {
			return TYPE_TEL_VOICE;
		} else if (before.contains("ISDN")) {
			return TYPE_TEL_ISDN;
		} else {
			return TYPE_TEL_DEFAULT;
		}
	}

	/***************** 获取当前Email的类型 *********************/
	private int getEmailType(String before) {
		if (before.contains("HOME")) {
			return TYPE_EMAIL_HOME;
		} else if (before.contains("WORK")) {
			return TYPE_EMAIL_WORK;
		} else if (before.contains("INTERNET")) {
			return TYPE_EMAIL_INTERNET;
		} else if (before.contains("CELL")) {
			return TYPE_EMAIL_CELL;
		}
		return TYPE_EMAIL_INTERNET;
	}

	/***************** 获取当前before字符串的属性 *********************/
	private int getTypeOfBefore(String before) {
		if (before.contains(";")) {
			before = before.substring(0, before.indexOf(";")).trim();
		}
		if (before.equals("FN")) {
			return TYPE_FN;
		} else if (before.equals("N")) {
			return TYPE_N;
		} else if (before.equals("TEL")) {
			return TYPE_IS_TEL;
		} else if (before.equals("EMAIL")) {
			return TYPE_IS_EMAIL;
		} else if (before.equals("ADR")) {
			return TYPE_ADR;
		} else if (before.equals("URL")) {
			return TYPE_URL;
		} else if (before.equals("ORG")) {
			return TYPE_ORG;
		} else if (before.equals("TITLE")) {
			return TYPE_TITLE;
		} else if (before.equals("NOTE")) {
			return TYPE_NOTE;
		} else if (before.equals("VERSION")) {
			return 10000;

		} else if (before.equals("X-AIM")) {
			return TYPE_X_AIM;
		} else if (before.equals("X-MSN")) {
			return TYPE_X_MSN;
		} else if (before.equals("X-YAHOO")) {
			return TYPE_X_YAHOO;
		} else if (before.equals("X-QQ")) {
			return TYPE_X_QQ;
		} else if (before.equals("X-GOOGLE-TALK")) {
			return TYPE_X_GOOGLE_TALK;
		} else if (before.equals("X-ICQ")) {
			return TYPE_X_ICQ;
		} else if (before.equals("X-JABBER")) {
			return TYPE_X_JABBER;
		} else if (before.equals("X-SKYPE-USERNAME")) {
			return TYPE_X_SKYPE;
		} else if (before.equals("X-IMPP")) {
			return TYPE_X_IMPP;
		} else if (before.equals("X-CLASS")) {
			return 10000;
		} else if (before.equals("CLASS")) {
			return 10000;
		} else if (before.equals("PHOTO")||before.equals("LOGO")) {
			return TYPE_PHOTO;
		} else if (before.equals("X-NICKNAME")) {
			return TYPE_NICKNAME;
		} else if(before.equals("BDAY")){
			return TYPE_EVENT_BDAY;
		
		}else if(before.equals("X-ANDROID-CUSTOM")) {
			return TYPE_NICKNAME;
		} else if (before.equals("X-PHONETIC-FIRST-NAME")) {
			return 10000;
		} else if (before.equals("X-PHONETIC-MIDDLE-NAME")) {
			return 10000;
		} else if (before.equals("X-PHONETIC-LAST-NAME")) {
			return 10000;
		} else if (before.equals("REV")) {
			return 10000;
		} else if (before.equals("UID")) {
			return 10000;
		}
		return TYPE_NOTE;
	}

	public int getImportingContactsCount() {
		return importingContactCount;
	}

	public String getImportingContactsFn() {
		if (!importingContactFN.equals("")) {
			return importingContactFN;
		} else {
			return importingContactN;
		}

	}

	/***************** 获取当前联系人的个数 *********************/
	public int getContactsCount() {
		Cursor cursor = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED ");
		return cursor.getCount();
	}

	/***************** 获取当前可用的ContentResolver *********************/
	public ContentResolver getMyContentResolver() {
		return m_contentResolver;
	}

	// /////////////////////荒废
	/*
	 * public boolean exportContactOneByOneAsVcardFileToSD(String filePath ,
	 * int[] exportcontactid) throws IOException { File f = new File("/sdcard/"
	 * + filePath + ".vcf"); if (f.exists()) { f.delete(); } FileWriter fw = new
	 * FileWriter(f); BufferedWriter bw = new BufferedWriter(fw); Cursor cur =
	 * m_contentResolver.query( ContactsContract.Contacts.CONTENT_URI, null,
	 * null, null, null); if (cur.moveToFirst()) { do { String lookupKey =
	 * cur.getString(cur .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
	 * Uri uri = Uri.withAppendedPath(
	 * ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
	 * AssetFileDescriptor fd = m_contentResolver .openAssetFileDescriptor(uri,
	 * "r"); FileInputStream fis = fd.createInputStream(); BufferedInputStream
	 * bfi = new BufferedInputStream(fis); InputStreamReader ipr = new
	 * InputStreamReader(bfi); int temp; while ((temp = ipr.read()) != -1) {
	 * bw.write(temp); bw.flush(); } bfi.close(); } while (cur.moveToNext());
	 * bw.close(); return true; } else { return false; }
	 * 
	 * 
	 * 
	 * File f = new File("/sdcard/" + filePath + ".vcf"); if (f.exists()) {
	 * f.delete(); } FileWriter fw = new FileWriter(f); BufferedWriter bw = new
	 * BufferedWriter(fw); for(int i = 0;i<exportcontactid.length;i++){
	 * exportingContactCount = i+1; Cursor cur = m_contentResolver.query(
	 * ContactsContract.Contacts.CONTENT_URI, null,
	 * ContactsContract.Contacts._ID + "=" + exportcontactid[i], null, null);
	 * cur.moveToFirst(); String lookupKey = cur.getString(cur
	 * .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)); Uri uri =
	 * Uri.withAppendedPath( ContactsContract.Contacts.CONTENT_VCARD_URI,
	 * lookupKey); AssetFileDescriptor fd =
	 * m_contentResolver.openAssetFileDescriptor(uri, "r"); BzeeLog.step1();
	 * FileInputStream fis = fd.createInputStream(); BzeeLog.step2();
	 * BufferedInputStream bfi = new BufferedInputStream(fis); BzeeLog.step3();
	 * InputStreamReader ipr = new InputStreamReader(bfi); BzeeLog.step4(); int
	 * temp; while ((temp = ipr.read()) != -1) { bw.write(temp); bw.flush(); }
	 * ipr.close(); } bw.close(); return true; }
	 */

	private int exportingContactCount = 0;

	public int getExportingContactCount() {
		return exportingContactCount;
	}

	public boolean exportContactOneByOneAsVcardFileToSD(String filePath,
			int[] contactPos) throws Exception {
		/////////////Zhangqx Add/////////////////
		//每次执行前先把标识设置回默认以免受到修改后的影响
		setIsExporting(true);
		setIsStopExporting(false);
		/////////////Zhangqx Add/////////////////
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		Cursor cur = m_contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED ");
		for (int i = 0; i < contactPos.length; i++) {
			/////////////Zhangqx Add/////////////////
			if(!isExporting)
			{
				if(isStopExporting){bw.close();return false;}
				--i;continue;
			}
			/////////////Zhangqx Add///////////////////
			exportingContactCount = i + 1;
			cur.moveToPosition(contactPos[i]);
			BzeeLog.mark(cur.getPosition());
			String lookupKey = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
			BzeeLog.mark(lookupKey);
			Uri uri = Uri.withAppendedPath(
					ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
			AssetFileDescriptor fd = m_contentResolver.openAssetFileDescriptor(
					uri, "r");
			FileInputStream fis = fd.createInputStream();
			BufferedInputStream bfi = new BufferedInputStream(fis);
			InputStreamReader ipr = new InputStreamReader(bfi);
			int temp;
			while ((temp = ipr.read()) != -1) {
				bw.write(temp);
				bw.flush();
			}
			ipr.close();
		}
		bw.close();
		return true;
	}

	/***************** 解析中文的UTF8格式 *********************/
	public String encodingUTF8(String incoming)
			throws UnsupportedEncodingException {
		if (!isCompleteUTF8String(incoming)) {
			return "";
		} else {
			String newIncoming = incoming.replaceAll("=", "%");
			String out = null;
			try {
				out = java.net.URLDecoder.decode(newIncoming, "utf-8");
			} catch (IllegalArgumentException ie) {
				out = "";
			}
			return out;
		}
	}
	/******************得到位图字符串****************************/
//	public String getBitmap_str()
	/***************** 解析图片的Base64格式 *********************/
/*	public Bitmap getBitmap(String iconBase64){ 
        byte[] bitmapArray; 
		bitmapArray = Base64.decode(iconBase64.getBytes()); 
		return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length); 
    }
*/


	public int repairVcardFile(File vcardFile) throws Exception {
		int contactCount = getVcardContactsCount(vcardFile);
		BzeeLog.show("contactCount", contactCount);
		String vcardString = vcardfileToString(vcardFile);
		BzeeLog.show("vcardString", vcardString);
		String tempString1 = vcardString;
		String[] temp = new String[getVcardContactsCount(vcardFile)];
		int endCount = 0;
		int brokenBegin = 0;
		for (int i = 0; i < contactCount; i++) {
			if (tempString1.indexOf("END:VCARD") != -1) {
				String temp3;
				// XXXXE
				temp3 = tempString1.substring(0, tempString1
						.indexOf("END:VCARD") + 9);
				BzeeLog.show("temp3", temp3);
				tempString1 = tempString1.substring(tempString1
						.indexOf("END:VCARD") + 9);
				BzeeLog.show("tempString1", tempString1);
				if (temp3.indexOf("BEGIN:VCARD") != -1) {
					// XXXXBEBE->BEBE
					temp3 = temp3.substring(temp3.indexOf("BEGIN:VCARD"));
					BzeeLog.show("temp32", temp3);
					int j = 0;
					while ((j = temp3.indexOf("BEGIN:VCARD", 5)) != -1) {
						BzeeLog.mark(j);
						brokenBegin++;
						temp3 = temp3.substring(j);
						BzeeLog.show("temp33", temp3);
						// return 1;
					}
					temp[endCount] = temp3;
					BzeeLog.show("temp[" + endCount + "]", temp3);
					endCount++;
				}
			} else {
				break;
			}
		}
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < endCount; i++) {
			sb.append(temp[i]);
			sb.append("\r\n");
		}
		String repaired = new String(sb);

		String vcardFilePath = vcardFile.getPath();
		vcardFile.delete();
		File file = new File(vcardFilePath);
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(repaired);
		bw.flush();
		bw.close();
		return contactCount - endCount > brokenBegin ? contactCount - endCount
				: brokenBegin;
	}

	// ////////////2011.7.8改动
	private boolean afterStringNotEnd = false;
	private String tempAfterString = "";
	private String importingContactN = "";

	private boolean isAfterStringComplete(String after, int type) {
		if (type == TYPE_N) {
			int fenhaoCount = 0;
			while (after.contains(";")) {
				fenhaoCount++;
				after = after.substring(after.indexOf(";") + 1);
			}
//			if ((fenhaoCount == 4)|| (fenhaoCount == 0)||(fenhaoCount == 1)) {
//				return true;
//			} else{
//				return false;
//			}
			if (fenhaoCount <= 4) {
				return true;
			} else{
				return false;
			}
		} else if (type == TYPE_ADR) {
			int fenhaoCount = 0;
			while (after.contains(";")) {
				fenhaoCount++;
				after = after.substring(after.indexOf(";") + 1);
			}
			if (fenhaoCount <= 6) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/***************** 判断该字符串是否为完整的UTF8格式 *********************/
	private boolean isCompleteUTF8String(String s) {
		if (s.contains("=")) {
			s = s.substring(s.lastIndexOf("="));
//			if (s.length() != 3) {
//				return false;
//			} else {
//				return true;
//			}
			return true;
		} else {
			return true;
		}
	}

	 public static boolean containsOnlyPrintableAscii(String str) {
	        if (TextUtils.isEmpty(str)) {
	            return true;
	        }
	        
	        final int length = str.length();
	        final int asciiFirst = 0x20;
	        final int asciiLast = 0x126;
	        for (int i = 0; i < length; i = str.offsetByCodePoints(i, 1)) {
	            int c = str.codePointAt(i);
	            if (c < asciiFirst || asciiLast < c) {
	                return false;
	            }
	        }
	        return true;
	    }

	// public boolean importVcardByMyselfViaInputStream(InputStream inputstream)
	// throws IOException {
	// File file = inputstreamToCachefile(inputstream);
	// importVcardByMyself(file);
	// return true;
	// }
}
