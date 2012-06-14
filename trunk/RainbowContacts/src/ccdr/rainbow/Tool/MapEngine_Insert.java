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

	SQLiteDatabase sqld = null;// 得到一个数据库对象，之所以声明为全局变量是为了便于close
	MapEngineHelper db = null;// 声明一个数据库助手类的对象，之所以声明为全局变量是为了便于close
	private static final String DataBase = MapEngineHelper.DataBase;
	private static final String TB_NAME = MapEngineHelper.TB_NAME;
	private static final String ID = MapEngineHelper.ID;
	private static final String IM = MapEngineHelper.IM;
	private static final String NICKNAME = MapEngineHelper.NICKNAME;

	// 软件第一次运行就为其创建“映射引擎”数据库

	private Context ct = null;

	public void Insert(Context context) {
		ct = context;
		db = new MapEngineHelper(context, DataBase,
				Context.MODE_WORLD_WRITEABLE);
		sqld = db.getWritableDatabase();// 得到可写的数据库对象
		InsertData();// 从map.xml文件中读取插入映射表的数据
		close();
	}

	/************ 关闭数据与助手类对象 *************/
	private void close() {
		db.close();
		sqld.close();
	}

	/*************** 从map.xml文件中读取插入映射表的数据 *****************/
	private void InsertData() {

		Resources r = ct.getResources();
		XmlResourceParser xrp = r.getXml(R.xml.map);

		try {
			// 当没有达到xml的逻辑结束终点
			// getEventType方法返回读取xml当前的事件
			while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
				if (xrp.getEventType() == XmlPullParser.START_TAG) {
					String name = xrp.getName();
					if (name.equals("customer")) // 查找符合条件的
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
