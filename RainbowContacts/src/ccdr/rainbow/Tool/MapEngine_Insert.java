package ccdr.rainbow.Tool;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ccdr.rainbowcontacts.R;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;

public class MapEngine_Insert {

	SQLiteDatabase sqld = null;// �õ�һ�����ݿ����֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	MapEngineHelper db = null;// ����һ�����ݿ�������Ķ���֮��������Ϊȫ�ֱ�����Ϊ�˱���close
	private static final String DataBase = MapEngineHelper.DataBase;
	private static final String TB_NAME = MapEngineHelper.TB_NAME;
	private static final String ID = MapEngineHelper.ID;
	private static final String IM = MapEngineHelper.IM;
	private static final String NICKNAME = MapEngineHelper.NICKNAME;

	// �����һ�����о�Ϊ�䴴����ӳ�����桱���ݿ�

	private Context ct = null;

	public void Insert(Context context) {
		ct = context;
		db = new MapEngineHelper(context, DataBase,
				Context.MODE_WORLD_WRITEABLE);
		sqld = db.getWritableDatabase();// �õ���д�����ݿ����
		InsertData();// ��map.xml�ļ��ж�ȡ����ӳ��������
		close();
	}

	/************ �ر���������������� *************/
	private void close() {
		db.close();
		sqld.close();
	}

	/*************** ��map.xml�ļ��ж�ȡ����ӳ�������� *****************/
	private void InsertData() {

		Resources r = ct.getResources();
		XmlResourceParser xrp = r.getXml(R.xml.map);

		try {
			// ��û�дﵽxml���߼������յ�
			// getEventType�������ض�ȡxml��ǰ���¼�
			while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xrp.getEventType() == XmlPullParser.START_TAG) {
					String name = xrp.getName();
					if (name.equals("customer")) // ���ҷ���������
					{
						ContentValues values = new ContentValues();
						values.put(ID, xrp.getAttributeValue(0));
						values.put(IM, xrp.getAttributeValue(1));
						values.put(NICKNAME, xrp.getAttributeValue(2));
						sqld.insert(TB_NAME, null, values);
					}
				}
				xrp.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
