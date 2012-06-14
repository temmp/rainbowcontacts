package ccdr.rainbow.Tool;

import java.io.UnsupportedEncodingException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;

public class ValuesCatch {
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
	private  ContentValues onepeopleValues ;
	public boolean afterStringNotEnd = false;
	private String importingContactN = "";
	private ContentResolver cr = null;
	ValuesCatch(long rawContactId, ContentResolver cr){
		onepeopleValues = new ContentValues();
		onepeopleValues.put(Data.RAW_CONTACT_ID, rawContactId);
		onepeopleValues.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		this.cr = cr;
	}
	
	
//	public long createNewContact() {
//		ContentValues values = new ContentValues();
//		Uri rawContactUri = m_contentResolver.catch(RawContacts.CONTENT_URI,
//				values);
//		long rawContactId = ContentUris.parseId(rawContactUri);
//		return rawContactId;
//	}
	public void insert(){
		cr.insert(
				android.provider.ContactsContract.Data.CONTENT_URI,onepeopleValues);
	}
	private boolean isCompleteUTF8String(String s) {
		if (s.contains("=")) {
			s = s.substring(s.lastIndexOf("="));
			if (s.length() != 3) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
	
	public String encodingUTF8(String incoming)throws UnsupportedEncodingException {
	if (!isCompleteUTF8String(incoming)) {
			return "";
		} else {
			String newIncoming = incoming.replaceAll("=", "%");
			return java.net.URLDecoder.decode(newIncoming, "utf-8");
		}
	}
	
	public boolean catchFN(String str , boolean isUTF8) {
		String FN = str;
		if (isUTF8) {
			try {
				FN = encodingUTF8(FN);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//importingContactFN = FN;
		onepeopleValues.put(StructuredName.DISPLAY_NAME, FN);		
		return true;
	}

	public boolean catchURL(String str) {
		onepeopleValues.put(Website.URL, str);
		return true;
	}

	public boolean catchORG(String str, boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onepeopleValues.put(Organization.COMPANY, str);
		return true;
	}

	public boolean catchTitle(String str, boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onepeopleValues.put(Organization.TITLE, str);
		return true;
	}

	public boolean catchNote(String str, boolean isUTF8) {
		if (isUTF8) {
			try {
				str = encodingUTF8(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onepeopleValues.put(Note.NOTE, str);
		return true;
	}
	
	
	private int getTelType(String before) {
		if (before.contains("HOME")) {
			if (before.contains("VOICE")) {
				return TYPE_TEL_HOME_VOICE;
			} else if (before.contains("FAX")) {
				return TYPE_TEL_HOME_FAX;
			}
		} else if (before.contains("WORK")) {
			if (before.contains("VOICE")) {
				return TYPE_TEL_WORK_VOICE;
			} else if (before.contains("FAX")) {
				return TYPE_TEL_WORK_FAX;
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
		return TYPE_TEL_DEFAULT;
	}
	
	
	public boolean catchTEL(String before, String after) {
		onepeopleValues.put(Phone.NUMBER, after);
		int type = getTelType(before);
		switch (type) {
		case TYPE_TEL_HOME_VOICE: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_HOME);
			break;
		}
		case TYPE_TEL_CELL: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_MOBILE);
			break;
		}
		case TYPE_TEL_WORK_VOICE: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_WORK);
			break;
		}
		case TYPE_TEL_WORK_FAX: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_FAX_WORK);
			break;
		}
		case TYPE_TEL_HOME_FAX: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_FAX_HOME);
			break;
		}
		case TYPE_TEL_PAGER: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_PAGER);
			break;
		}
		case TYPE_TEL_VOICE: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_OTHER);
			break;
		}
		case TYPE_TEL_ISDN: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_ISDN);
			break;
		}
		case TYPE_TEL_DEFAULT: {
			onepeopleValues.put(Phone.TYPE, Phone.TYPE_OTHER);
			break;
		}
		}
		return true;
	}

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
		return TYPE_EMAIL_HOME;
	}
	
	public boolean catchEmail(String before, String after) {
		onepeopleValues.put(Email.DATA, after);
		int type = getEmailType(before);
		switch (type) {
		case TYPE_EMAIL_HOME: {
			onepeopleValues.put(Email.TYPE, Email.TYPE_HOME);
			break;
		}
		case TYPE_EMAIL_WORK: {
			onepeopleValues.put(Email.TYPE, Email.TYPE_WORK);
			break;
		}
		case TYPE_EMAIL_INTERNET: {
			onepeopleValues.put(Email.TYPE, Email.TYPE_OTHER);
			break;
		}
		case TYPE_EMAIL_CELL: {
			onepeopleValues.put(Email.TYPE, Email.TYPE_MOBILE);
			break;
		}
		}
		return true;
	}

	private boolean isAfterStringComplete(String after, int type) {
		if (type == TYPE_N) {
			int fenhaoCount = 0;
			while (after.contains(";")) {
				fenhaoCount++;
				after = after.substring(after.indexOf(";") + 1);
			}
			if (fenhaoCount == 4) {
				return true;
			} else {
				return false;
			}
		} else if (type == TYPE_ADR) {
			int fenhaoCount = 0;
			while (after.contains(";")) {
				fenhaoCount++;
				after = after.substring(after.indexOf(";") + 1);
			}
			if (fenhaoCount == 6) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public boolean catchN(String after, boolean isUTF8) {
		if (!isAfterStringComplete(after, TYPE_N)) {
			// tempAfterString = after;
			// afterStringNotEnd = true;
			// temptype = TYPE_N;
			return false;
		}
		afterStringNotEnd = false;
		// Log.e("调试", "catchN");
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

		onepeopleValues.put(StructuredName.FAMILY_NAME, familyName);
		onepeopleValues.put(StructuredName.GIVEN_NAME, givenName);
		onepeopleValues.put(StructuredName.MIDDLE_NAME, middleName);
		onepeopleValues.put(StructuredName.PREFIX, namePrefix);
		onepeopleValues.put(StructuredName.SUFFIX, nameSuffix);

		onepeopleValues.put(StructuredName.PHONETIC_FAMILY_NAME, familyName);
		onepeopleValues.put(StructuredName.PHONETIC_MIDDLE_NAME, middleName);
		onepeopleValues.put(StructuredName.PHONETIC_GIVEN_NAME, givenName);

		/*
		 * onepeopleValues.put(StructuredName.PHONETIC_FAMILY_NAME , familyName +
		 * givenName); onepeopleValues.put(StructuredName.PHONETIC_GIVEN_NAME ,
		 * familyName + givenName);
		 * onepeopleValues.put(StructuredName.PHONETIC_MIDDLE_NAME , familyName +
		 * givenName);
		 */

		return true;
	}

	public boolean catchADR(String before, String after,
			boolean isUTF8) {
		// BzeeLog.show("isAfterStringComplete(after, TYPE_ADR)",
		// isAfterStringComplete(after, TYPE_ADR));
		if (!isAfterStringComplete(after, TYPE_ADR)) {
			// tempAfterString = after;
			// afterStringNotEnd = true;
			// temptype = TYPE_ADR;
			return false;
		}
		afterStringNotEnd = false;

		String pobox, street, city, state, zipCode, country;
		pobox = after.substring(0, after.indexOf(";"));
		after = after.substring(after.indexOf(";") + 1);
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

		onepeopleValues.put(StructuredPostal.POBOX, pobox);
		onepeopleValues.put(StructuredPostal.STREET, street);
		onepeopleValues.put(StructuredPostal.CITY, city);
		onepeopleValues.put(StructuredPostal.REGION, state);
		onepeopleValues.put(StructuredPostal.POSTCODE, zipCode);
		onepeopleValues.put(StructuredPostal.COUNTRY, country);
		if (before.contains("HOME")) {
			onepeopleValues.put(StructuredPostal.TYPE, StructuredPostal.TYPE_HOME);
		} else if (before.contains("WORK")) {
			onepeopleValues.put(StructuredPostal.TYPE, StructuredPostal.TYPE_WORK);
		} else {
			onepeopleValues.put(StructuredPostal.TYPE, StructuredPostal.TYPE_OTHER);
		}
		return true;
	}
}
