package ccdr.rainbow.Tool;

import java.util.ArrayList;
import java.util.LinkedList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
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

/**
 * 
 * @author Chen xijie
 * @author Yuan Yulai (modify)
 * @since  2011/08/01
 * @version 1.0.0
 *
 */
public class ContactInsert {// 批量插入联系人的类

	private Context m_context;
	private ContentResolver m_contentResolver;
	private ArrayList<ContentProviderOperation> ops;
	private int rawContactInsertIndex;

	public ContactInsert(Context context) {
		m_context = context;
		m_contentResolver = m_context.getContentResolver();
	}

	public void contactinsert(LinkedList<ContactClass> list) {// 批量插入联系人List

		ops = new ArrayList<ContentProviderOperation>();
		rawContactInsertIndex = 0;

		for (ContactClass oneContact : list) {
			rawContactInsertIndex = ops.size();// 以每一个联系人的第一条属性的地址作为该联系人的ID
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.RawContacts.CONTENT_URI).withValue(
					ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(
					ContactsContract.RawContacts.ACCOUNT_NAME, null)
					.withYieldAllowed(true).build());

			// 以下每一个get方法就是得到每一个联系人的每一个属性的List
			getFNlist(oneContact);
			getNlist(oneContact);
			getTELlist(oneContact);
			getEMAILlist(oneContact);
			getADRlist(oneContact);
			getURLlist(oneContact);
			getORGlist(oneContact);
			getTITELlist(oneContact);
			getNOTElist(oneContact);
			getIMlist(oneContact);
			getNICKNAMElist(oneContact);
			getEventlist(oneContact);
			getPhotolist(oneContact);
		}
		try {
			// 这里才调用的批量添加
			m_contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}

	}

	/**************** 得到当前联系人的FNList ********************/
	private void getFNlist(ContactClass oneContact) {

		LinkedList<ContentValues> FNlist = oneContact.getListFN();
		for (ContentValues FNcv : FNlist) {
			if (FNcv == null)
			{
				/***
				 * Used to be "break;" in previous version
				 * change to "continue;" to avoid information lost after a null element
				 * by Yuan Yulai 
				 ***/
				continue;
			}
			ops
					.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID,
									rawContactInsertIndex)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
									FNcv
											.getAsString(StructuredName.DISPLAY_NAME))
							.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的NList ********************/
	private void getNlist(ContactClass oneContact) {

		LinkedList<ContentValues> Nlist = oneContact.getListN();
		for (ContentValues Ncv : Nlist) {
			if (Ncv == null)
				continue;
			ops
					.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID,
									rawContactInsertIndex)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(StructuredName.FAMILY_NAME,

							Ncv.getAsString(StructuredName.FAMILY_NAME))
							.withValue(StructuredName.GIVEN_NAME,
									Ncv.getAsString(StructuredName.GIVEN_NAME))
							.withValue(StructuredName.MIDDLE_NAME,
									Ncv.getAsString(StructuredName.MIDDLE_NAME))
							.withValue(StructuredName.PREFIX,
									Ncv.getAsString(StructuredName.PREFIX))
							.withValue(StructuredName.SUFFIX,
									Ncv.getAsString(StructuredName.SUFFIX))
							.withValue(StructuredName.DISPLAY_NAME,
									Ncv.getAsString(StructuredName.DISPLAY_NAME))
//							.withValue(
//									StructuredName.PHONETIC_FAMILY_NAME,
//									Ncv
//											.getAsString(StructuredName.PHONETIC_FAMILY_NAME))
//							.withValue(
//									StructuredName.PHONETIC_MIDDLE_NAME,
//									Ncv
//											.getAsString(StructuredName.PHONETIC_MIDDLE_NAME))
//							.withValue(
//									StructuredName.PHONETIC_GIVEN_NAME,
//									Ncv
//											.getAsString(StructuredName.PHONETIC_GIVEN_NAME))
							.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的TELList ********************/
	private void getTELlist(ContactClass oneContact) {

		LinkedList<ContentValues> TELlist = oneContact.getListTEL();
		for (ContentValues TELcv : TELlist) {
			if (TELcv == null)
				continue;
			ops
					.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID,
									rawContactInsertIndex)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Phone.NUMBER,
									TELcv.getAsString(Phone.NUMBER)).withValue(
									Phone.TYPE, TELcv.getAsString(Phone.TYPE))
							.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的EMAILList ********************/
	private void getEMAILlist(ContactClass oneContact) {

		LinkedList<ContentValues> EMAILlist = oneContact.getListEMAIL();
		for (ContentValues EMAILcv : EMAILlist) {
			if (EMAILcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(
							ContactsContract.Data.MIMETYPE,
							Email.CONTENT_ITEM_TYPE).withValue(Email.DATA,
							EMAILcv.getAsString(Email.DATA)).withValue(
							Email.TYPE, EMAILcv.getAsString(Email.TYPE))
					.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的ADRList ********************/
	private void getADRlist(ContactClass oneContact) {

		LinkedList<ContentValues> ADRlist = oneContact.getListADR();
		for (ContentValues ADRcv : ADRlist) {

			if (ADRcv == null)
				continue;

			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(
							ContactsContract.Data.MIMETYPE,
							StructuredPostal.CONTENT_ITEM_TYPE).withValue(
							StructuredPostal.POBOX,
							ADRcv.getAsString(StructuredPostal.POBOX))
					.withValue(StructuredPostal.STREET,
							ADRcv.getAsString(StructuredPostal.STREET))
					.withValue(StructuredPostal.CITY,
							ADRcv.getAsString(StructuredPostal.CITY))
					.withValue(StructuredPostal.REGION,
							ADRcv.getAsString(StructuredPostal.REGION))
					.withValue(StructuredPostal.POSTCODE,
							ADRcv.getAsString(StructuredPostal.POSTCODE))
					.withValue(StructuredPostal.COUNTRY,
							ADRcv.getAsString(StructuredPostal.COUNTRY))
					.withValue(StructuredPostal.TYPE,
							ADRcv.getAsString(StructuredPostal.TYPE))
					.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的URLList ********************/
	private void getURLlist(ContactClass oneContact) {

		LinkedList<ContentValues> URLlist = oneContact.getListURL();
		for (ContentValues URLcv : URLlist) {
			if (URLcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(
							ContactsContract.Data.MIMETYPE,
							Website.CONTENT_ITEM_TYPE).withValue(Website.URL,
							URLcv.getAsString(Website.URL)).withYieldAllowed(
							true).build());
		}
	}

	/**************** 得到当前联系人的ORGList ********************/
	private void getORGlist(ContactClass oneContact) {

		LinkedList<ContentValues> ORGlist = oneContact.getListORG();
		for (ContentValues ORGcv : ORGlist) {
			if (ORGcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(Data.MIMETYPE,
							Organization.CONTENT_ITEM_TYPE).withValue(
							Organization.COMPANY,
							ORGcv.getAsString(Organization.COMPANY)).withValue(
							Organization.TYPE, Organization.TYPE_WORK)
					.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的TITELList ********************/
	private void getTITELlist(ContactClass oneContact) {

		LinkedList<ContentValues> TITLElist = oneContact.getListTITLE();
		for (ContentValues TITLEcv : TITLElist) {
			if (TITLEcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(Data.MIMETYPE,
							Organization.CONTENT_ITEM_TYPE).withValue(
							Organization.TITLE,
							TITLEcv.getAsString(Organization.TITLE)).withValue(
							Organization.TYPE, Organization.TYPE_WORK)
					.withYieldAllowed(true).build());
		}
	}

	/**************** 得到当前联系人的NOTEList ********************/
	private void getNOTElist(ContactClass oneContact) {

		LinkedList<ContentValues> NOTElist = oneContact.getListNOTE();
		for (ContentValues NOTEcv : NOTElist) {
			if (NOTEcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(Data.MIMETYPE,
							Note.CONTENT_ITEM_TYPE).withValue(Note.NOTE,
							NOTEcv.getAsString(Note.NOTE)).withYieldAllowed(
							true).build());
		}
	}

	/**************** 得到当前联系人的IMList ********************/
	private void getIMlist(ContactClass oneContact) {

		LinkedList<ContentValues> IMlist = oneContact.getListIM();
		for (ContentValues IMcv : IMlist) {
			if (IMcv == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(
							ContactsContract.Data.MIMETYPE,
							Im.CONTENT_ITEM_TYPE).withValue(Im.PROTOCOL,
							IMcv.getAsString(Im.PROTOCOL)).withValue(Im.DATA,
							IMcv.getAsString(Im.DATA)).withYieldAllowed(true)
					.build());
		}
	}

	/**************** 得到当前联系人的NICKNAMEList ********************/
	private void getNICKNAMElist(ContactClass oneContact) {

		LinkedList<ContentValues> NICKNAMElist = oneContact.getListNICKNAME();
		for (ContentValues NICKNAMEcv : NICKNAMElist) {
			if (NICKNAMElist == null)
				continue;
			ops.add(ContentProviderOperation.newInsert(
					ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID,
							rawContactInsertIndex).withValue(
							ContactsContract.Data.MIMETYPE,
							Nickname.CONTENT_ITEM_TYPE).withValue(
							Nickname.DATA,
							NICKNAMEcv.getAsString(Nickname.DATA))
					.withYieldAllowed(true).build());
		}
	}

	/*********************** 得到当前联系人的EventList *******************/
	private void getEventlist(ContactClass oneContact) {
		LinkedList<ContentValues> Eventlist = oneContact.getListEvent();
		for (ContentValues Eventcv : Eventlist) {
			if (Eventcv == null)
				continue;
			ops
					.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID,
									rawContactInsertIndex)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Event.START_DATE,
									Eventcv.getAsString(Event.START_DATE))
							.withValue(Event.TYPE,
									Eventcv.getAsString(Event.TYPE))
							.withYieldAllowed(true).build());

		}
	}

	/**************************** 获得当前联系人的PhotoList ***************/
	private void getPhotolist(ContactClass oneContact) {
		LinkedList<ContentValues> Photolist = oneContact.getListPHOTO();
		for (ContentValues Photocv : Photolist) {
			if (Photocv == null)
				continue;
			ops
					.add(ContentProviderOperation
							.newInsert(ContactsContract.Data.CONTENT_URI)
							.withValueBackReference(
									ContactsContract.Data.RAW_CONTACT_ID,
									rawContactInsertIndex)
							.withValue(
									ContactsContract.Data.MIMETYPE,
									ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
							.withValue(
									ContactsContract.CommonDataKinds.Photo.PHOTO,
									Photocv.get(Photo.PHOTO)).withYieldAllowed(
									true).build());

		}
	}

}
