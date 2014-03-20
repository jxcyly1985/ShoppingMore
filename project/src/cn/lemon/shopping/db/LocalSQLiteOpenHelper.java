package cn.lemon.shopping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "shopping_more.db";

    private static LocalSQLiteOpenHelper sInstance;

    public synchronized static LocalSQLiteOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalSQLiteOpenHelper(context);
        }

        return sInstance;
    }

    private LocalSQLiteOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MallCategoryTable.getCreateSQL());
        db.execSQL(MallInfoTable.getCreateSQL());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
