package ccdr.rainbow.Tool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MapEngineHelper extends SQLiteOpenHelper {

	private static final int DBVERSION = 1;
	public static final String TB_NAME = "MapEngine";
	public static final String DataBase = "MapEngine_DB";
	public static final String ID = "id";
	public static final String IM = "im";
	public static final String NICKNAME = "nickname";

	public MapEngineHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MapEngineHelper(Context context, String name) {
		this(context, name, DBVERSION);
	}

	public MapEngineHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// ���õ�SQLiteDatabase����ʱ������MapEngine���ݿ�
		System.out.println("create the MapEngine DB");
		arg0.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" + ID
				+ " varchar primary key," + // SQLite���ݿ���id����:�ֻ��ͺ�
				IM + " varchar," + // ��ʱ����
				NICKNAME + " varchar" + ")");// �ǳ�
	}

	@Override
	public synchronized void close() {

		System.out.println("close the DB");
		super.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		System.out.println("update a DB");
	}

}
