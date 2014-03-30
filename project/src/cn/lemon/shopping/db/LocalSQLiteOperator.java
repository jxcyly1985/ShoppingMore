package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.lemon.shopping.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalSQLiteOperator {

    public static final String TAG = "LocalSQLiteOperator";

    private static LocalSQLiteOperator sInstance;
    private LocalSQLiteOpenHelper mLocalSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public synchronized static LocalSQLiteOperator getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalSQLiteOperator(context);
        }
        return sInstance;
    }

    private LocalSQLiteOperator(Context context) {
        mLocalSQLiteOpenHelper = LocalSQLiteOpenHelper.getInstance(context);
        mSQLiteDatabase = mLocalSQLiteOpenHelper.getWritableDatabase();
    }

    public void close() {

        mSQLiteDatabase.close();
    }

}

