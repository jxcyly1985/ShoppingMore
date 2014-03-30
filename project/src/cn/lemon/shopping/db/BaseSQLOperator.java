package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-25
 * Time: 下午11:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSQLOperator<T> {


    protected Context mContext;
    protected LocalSQLiteOpenHelper mLocalSQLiteOpenHelper;
    protected SQLiteDatabase mSQLiteDatabase;


    public BaseSQLOperator(Context context) {
        mContext = context;
        mLocalSQLiteOpenHelper = LocalSQLiteOpenHelper.getInstance(context);
        mSQLiteDatabase = mLocalSQLiteOpenHelper.getWritableDatabase();


    }

    public abstract void insert(T t);

    public abstract T query();

    public abstract T query(String[] projection, String selection, String[] selectionArgs, String sortOrder);

    public abstract void delete(String whereClause, String[] whereArgs);

    public abstract void update(ContentValues values, String whereClause, String[] whereArgs);

}
