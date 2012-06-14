package ccdr.rainbow.Tool;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;

public class Calls  {
	private ContentResolver m_contentResolver;
	
	public ContentResolver getMyContentResolver() {
		return m_contentResolver;
	}
	public  Set getCalls(){
	Cursor cursor = getMyContentResolver().query(CallLog.Calls.CONTENT_URI,
			null, "name is not null", null, CallLog.Calls.DEFAULT_SORT_ORDER);
	Set calls = new HashSet<String>();
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
		} while (cursor.moveToNext());
	}
	return calls;
	}
}
