package ccdr.rainbow.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MapEngineExchange {// 利用映射引擎对Vcard文件字段进行替换的类

	private static final String TB_NAME = MapEngineHelper.TB_NAME;
	private static final String DataBase = MapEngineHelper.DataBase;
	private static final String ID = MapEngineHelper.ID;
	private static final String IM = MapEngineHelper.IM;
	private static final String NICKNAME = MapEngineHelper.NICKNAME;

	SQLiteDatabase sqld = null;// 得到一个数据库对象，之所以声明为全局变量是为了便于close
	MapEngineHelper db = null;// 声明一个数据库助手类的对象，之所以声明为全局变量是为了便于close
	Cursor cursor_otherMobile = null;
	Cursor cursor_selfMobile = null;

	/**************** 利用映射引擎对Vcard文件进行转换 ******************/
	public void Exchange(Context context, File vcardFile, String otherMobile,
			String selfMobile) throws IOException {

		BufferedReader bf = new BufferedReader(new FileReader(vcardFile));
		String content;

		db = new MapEngineHelper(context, DataBase);
		sqld = db.getReadableDatabase();

		cursor_otherMobile = sqld.query(TB_NAME, new String[] { ID + " as _id",
				IM, NICKNAME }, "_id = ?", new String[] { otherMobile }, null,
				null, null);
		cursor_otherMobile.moveToNext();

		cursor_selfMobile = sqld.query(TB_NAME, new String[] { ID + " as _id",
				IM, NICKNAME }, "_id = ?", new String[] { selfMobile }, null,
				null, null);
		cursor_selfMobile.moveToNext();

		String im_otherMobile = cursor_otherMobile.getString(cursor_otherMobile
				.getColumnIndex(IM));
		String nickname_otherMobile = cursor_otherMobile
				.getString(cursor_otherMobile.getColumnIndex(NICKNAME));
		String im_selfMobile = cursor_selfMobile.getString(cursor_selfMobile
				.getColumnIndex(IM));
		String nickname_selfMobile = cursor_selfMobile
				.getString(cursor_selfMobile.getColumnIndex(NICKNAME));

		while ((content = bf.readLine()) != null) {

			if (content.contains(im_otherMobile)) {
				content.replaceAll(im_otherMobile, im_selfMobile);
				continue;
			} else if (content.contains(nickname_otherMobile)) {
				content.replaceAll(nickname_otherMobile, nickname_selfMobile);
				continue;
			}
		}
		close();
	}

	/************ 关闭数据与助手类对象 *************/
	private void close() {
		db.close();
		sqld.close();
		cursor_otherMobile.close();
		cursor_selfMobile.close();
	}

}
