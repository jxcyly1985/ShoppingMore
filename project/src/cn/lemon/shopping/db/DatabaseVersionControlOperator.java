package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.model.DatabaseVersionControlInfo;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-29
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseVersionControlOperator extends BaseSQLOperator<DatabaseVersionControlInfo> {

    private DatabaseVersionControlInfo mDatabaseVersionControlInfo;


    public DatabaseVersionControlOperator(Context context) {
        super(context);
    }

    @Override
    public void insert(DatabaseVersionControlInfo databaseVersionControlInfo) {

    }

    @Override
    public DatabaseVersionControlInfo query() {
        return null;
    }

    @Override
    public DatabaseVersionControlInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = mSQLiteDatabase.query(DatabaseVersionControlTable.TABLE_NAME, DatabaseVersionControlTable.COLUMNS, null, null, null, null, null);
        DatabaseVersionControlInfo databaseVersionControlInfo = null;
        try {

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                databaseVersionControlInfo = new DatabaseVersionControlInfo();
                int mallHasNext = cursor.getInt(DatabaseVersionControlTable.MALL_HAS_NEXT_INDEX);
                int valueBuyHasNext = cursor.getInt(DatabaseVersionControlTable.VALUE_BUY_HAS_NEXT_INDEX);
                databaseVersionControlInfo.mMallVersion = cursor.getString(DatabaseVersionControlTable.MALL_VERSION_INDEX);
                databaseVersionControlInfo.mMallRequestTime = cursor.getLong(DatabaseVersionControlTable.MALL_REQUEST_TIME_INDEX);
                databaseVersionControlInfo.mMallPage = cursor.getInt(DatabaseVersionControlTable.MALL_PAGE_INDEX);
                databaseVersionControlInfo.mMallHasNext = mallHasNext == 1 ? true : false;
                databaseVersionControlInfo.mValueBuyItemVersion = cursor.getString(DatabaseVersionControlTable.VALUE_BUY_VERSION);
                databaseVersionControlInfo.mValueBuyRequestTime = cursor.getLong(DatabaseVersionControlTable.VALUE_BUY_REQUEST_INDEX);
                databaseVersionControlInfo.mValueBuyPage = cursor.getInt(DatabaseVersionControlTable.VALUE_BUY_PAGE_INDEX);
                databaseVersionControlInfo.mValueBuyHasNext = valueBuyHasNext == 1 ? true : false;

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return databaseVersionControlInfo;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

    }

    @Override
    public void update(ContentValues values, String whereClause, String[] whereArgs) {

        mSQLiteDatabase.update(DatabaseVersionControlTable.TABLE_NAME, values, null, null);
    }
}
