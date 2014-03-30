package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.model.MallVersionControlInfo;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-29
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class MallVersionControlOperator extends BaseSQLOperator<MallVersionControlInfo> {

    public MallVersionControlOperator(Context context) {
        super(context);
    }

    @Override
    public void insert(MallVersionControlInfo databaseVersionControlInfo) {

    }

    @Override
    public MallVersionControlInfo query() {
        return null;
    }

    @Override
    public MallVersionControlInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // QiYun<LeiYong><2014-03-19> modify for CR00000019 begin
        Cursor cursor = null;
        // QiYun<LeiYong><2014-03-19> modify for CR00000019 end
        MallVersionControlInfo mallVersionControlInfo = null;
        try {
            cursor = mSQLiteDatabase.query(MallVersionControlTable.TABLE_NAME, MallVersionControlTable.COLUMNS, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                mallVersionControlInfo = new MallVersionControlInfo();
                int mallHasNext = cursor.getInt(MallVersionControlTable.MALL_HAS_NEXT_INDEX);
                mallVersionControlInfo.mMallVersion = cursor.getString(MallVersionControlTable.MALL_VERSION_INDEX);
                mallVersionControlInfo.mMallRequestTime = cursor.getLong(MallVersionControlTable.MALL_REQUEST_TIME_INDEX);
                mallVersionControlInfo.mMallPage = cursor.getInt(MallVersionControlTable.MALL_PAGE_INDEX);
                mallVersionControlInfo.mMallHasNext = mallHasNext == 1 ? true : false;
            }

        } catch (Exception e) {
            // QiYun<LeiYong><2014-03-19> modify for CR00000019 begin
            e.printStackTrace();
            // QiYun<LeiYong><2014-03-19> modify for CR00000019 end
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return mallVersionControlInfo;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

    }

    @Override
    public void update(ContentValues values, String whereClause, String[] whereArgs) {

        // rely on caller MallSQLOperator transaction
        mSQLiteDatabase.update(MallVersionControlTable.TABLE_NAME, values, null, null);
    }
}
