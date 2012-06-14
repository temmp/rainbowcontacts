package ccdr.rainbow.Tool;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.ContentValues;

/**
 * ContactClass is used to store Information of a single Contact
 * 
 * @author Chen Sizhe, Chen xijie
 * @author Yuan Yulai (modify, 2011/09/15)
 * @since 2011/06/01
 * @version 1.0.0
 * 
 */
public class ContactClass {

	private LinkedList<ContentValues> ListFN;
	private LinkedList<ContentValues> ListN;
	private LinkedList<ContentValues> ListTEL;
	private LinkedList<ContentValues> ListEMAIL;
	private LinkedList<ContentValues> ListADR;
	private LinkedList<ContentValues> ListURL;
	private LinkedList<ContentValues> ListORG;
	private LinkedList<ContentValues> ListTITLE;
	private LinkedList<ContentValues> ListNOTE;
	private LinkedList<ContentValues> ListIM;
	private LinkedList<ContentValues> ListNICKNAME;
	private LinkedList<ContentValues> ListEvent;
	private LinkedList<ContentValues> ListPHOTO;

	// for future additional data field
	// added by Yuan Yulai
	private HashMap<String, LinkedList<ContentValues>> AddtionalInfo;
	
	
	

	public ContactClass() {
		this.ListFN = null;
		this.ListN = null;
		this.ListTEL = null;
		this.ListEMAIL = null;
		this.ListADR = null;
		this.ListURL = null;
		this.ListORG = null;
		this.ListTITLE = null;
		this.ListNOTE = null;
		this.ListIM = null;
		this.ListNICKNAME = null;
		this.ListEvent = null;
		this.ListPHOTO = null;
		this.AddtionalInfo = null;
	}

	
	
	
	/**
	 * @author Yuan Yulai
	 * @attention HashMap allows Null to be key and value, return null does not
	 *            mean the key is not exist, maybe the key is null;
	 */
	public LinkedList<ContentValues> getAddtionalInfo(String Name) {
		if (this.AddtionalInfo == null) {
			return null;
		} else {
			return this.AddtionalInfo.get(Name);
		}
	}
	
	public boolean containsAddtionalInfo(String Name) {
		if (this.AddtionalInfo == null)
		{
			return false;
		}
		else
		{
		return this.AddtionalInfo.containsKey(Name);
		}
	}

	public void setAddtionalInfo(String Name, LinkedList<ContentValues> listInfo) {
		if (this.AddtionalInfo == null) {
			this.AddtionalInfo = new HashMap<String, LinkedList<ContentValues>>();
		}
		this.AddtionalInfo.put(Name, listInfo);
	}
	
	

	public LinkedList<ContentValues> getListPHOTO() {
		return ListPHOTO;
	}

	public void setListPHOTO(LinkedList<ContentValues> listPHOTO) {
		ListPHOTO = listPHOTO;
	}

	public LinkedList<ContentValues> getListNICKNAME() {
		return ListNICKNAME;
	}

	public void setListNICKNAME(LinkedList<ContentValues> listNICKNAME) {
		ListNICKNAME = listNICKNAME;
	}

	public LinkedList<ContentValues> getListEvent() {
		return ListEvent;
	}

	public void setListEvent(LinkedList<ContentValues> listEvent) {
		ListEvent = listEvent;
	}

	public LinkedList<ContentValues> getListFN() {
		return ListFN;
	}

	public void setListFN(LinkedList<ContentValues> listFN) {
		ListFN = listFN;
	}

	public LinkedList<ContentValues> getListN() {
		return ListN;
	}

	public void setListN(LinkedList<ContentValues> listN) {
		ListN = listN;
	}

	public LinkedList<ContentValues> getListTEL() {
		return ListTEL;
	}

	public void setListTEL(LinkedList<ContentValues> listTEL) {
		ListTEL = listTEL;
	}

	public LinkedList<ContentValues> getListEMAIL() {
		return ListEMAIL;
	}

	public void setListEMAIL(LinkedList<ContentValues> listEMAIL) {
		ListEMAIL = listEMAIL;
	}

	public LinkedList<ContentValues> getListADR() {
		return ListADR;
	}

	public void setListADR(LinkedList<ContentValues> listADR) {
		ListADR = listADR;
	}

	public LinkedList<ContentValues> getListURL() {
		return ListURL;
	}

	public void setListURL(LinkedList<ContentValues> listURL) {
		ListURL = listURL;
	}

	public LinkedList<ContentValues> getListORG() {
		return ListORG;
	}

	public void setListORG(LinkedList<ContentValues> listORG) {
		ListORG = listORG;
	}

	public LinkedList<ContentValues> getListTITLE() {
		return ListTITLE;
	}

	public void setListTITLE(LinkedList<ContentValues> listTITLE) {
		ListTITLE = listTITLE;
	}

	public LinkedList<ContentValues> getListNOTE() {
		return ListNOTE;
	}

	public void setListNOTE(LinkedList<ContentValues> listNOTE) {
		ListNOTE = listNOTE;
	}

	public LinkedList<ContentValues> getListIM() {
		return ListIM;
	}

	public void setListIM(LinkedList<ContentValues> listIM) {
		ListIM = listIM;
	}

}
