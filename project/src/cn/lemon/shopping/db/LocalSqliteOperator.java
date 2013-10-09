package cn.lemon.shopping.db;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.shopping.model.MallEntryInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LocalSqliteOperator {

    public static final String TAG = "LocalSqliteOperator";

    private static LocalSqliteOperator sInstance;
    private LocalSqliteOpenHelper mLocalSqliteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public synchronized static LocalSqliteOperator getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalSqliteOperator(context);
        }
        return sInstance;
    }

    private LocalSqliteOperator(Context context) {
        mLocalSqliteOpenHelper = LocalSqliteOpenHelper.getInstance(context);
        mSQLiteDatabase = mLocalSqliteOpenHelper.getWritableDatabase();
    }

    public void close() {

        mSQLiteDatabase.close();
    }

    public void insertCategory(ContentValues contentValues) {

        if (contentValues != null) {
            mSQLiteDatabase.insert(MallCategoryTable.TABLE_NAME, null, contentValues);
        }

    }

    public void insertMallInfo(ContentValues contentValues) {
        if (contentValues != null) {
            mSQLiteDatabase.insert(MallInfoTable.TABLE_NAME, null, contentValues);
        }

    }

    public List<MallEntryInfo> getMallInfoByCategory(String category) {

        String selection = MallInfoTable.MALL_CATEGORY + "=?";
        String[] selectionArgs = new String[] {category};

        Cursor cursor = null;
        List<MallEntryInfo> mallInfos = null;
        try {
            cursor = mSQLiteDatabase.query(MallInfoTable.TABLE_NAME, MallInfoTable.COLUMNS, selection,
                    selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                mallInfos = new ArrayList<MallEntryInfo>();
                cursor.moveToFirst();
                MallEntryInfo mallInfo = null;
                do {
                    mallInfo = new MallEntryInfo();
                    mallInfo.mName = cursor.getString(0);
                    mallInfo.mIconUrl = cursor.getString(1);
                    mallInfo.mCategory = cursor.getString(2);
                    mallInfos.add(mallInfo);
                } while (cursor.moveToNext());

                return mallInfos;
            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return null;

    }
}
