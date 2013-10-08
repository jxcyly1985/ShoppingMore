package cn.lemon.shopping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSqliteOpenHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "shopping_more.db";

	private static LocalSqliteOpenHelper sInstance;

	public synchronized static LocalSqliteOpenHelper getInstance(Context context) {

		if (sInstance == null) {
			sInstance = new LocalSqliteOpenHelper(context);
		}

		return sInstance;
	}

	private LocalSqliteOpenHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String createSql = "";
		db.execSQL(createSql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
