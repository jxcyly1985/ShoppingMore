package cn.lemon.shopping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSqliteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public LocalSqliteOpenHelper(Context context) {

        super(context, null, null, VERSION);
    }

    public LocalSqliteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

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
