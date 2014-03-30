package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.model.ValueBuyItemVersionControlInfo;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-30
 * Time: 下午10:28
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemVersionControlOperator extends BaseSQLOperator<ValueBuyItemVersionControlInfo> {

    public ValueBuyItemVersionControlOperator(Context context) {
        super(context);
    }

    @Override
    public void insert(ValueBuyItemVersionControlInfo valueBuyItemVersionControlInfo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ValueBuyItemVersionControlTable.VALUE_BUY_ITEM_TYPE_ID,
                valueBuyItemVersionControlInfo.mTypeId);
        mSQLiteDatabase.insert(ValueBuyItemVersionControlTable.TABLE_NAME, null, contentValues);

    }

    @Override
    public ValueBuyItemVersionControlInfo query() {
        return null;
    }

    @Override
    public ValueBuyItemVersionControlInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        ValueBuyItemVersionControlInfo valueBuyItemVersionControlInfo = null;
        try {
            cursor = mSQLiteDatabase.query(ValueBuyItemVersionControlTable.TABLE_NAME,
                    ValueBuyItemVersionControlTable.COLUMNS, selection, selectionArgs, null, null, sortOrder);
            if (cursor != null && cursor.getCount() > 0) {
                valueBuyItemVersionControlInfo = new ValueBuyItemVersionControlInfo();
                valueBuyItemVersionControlInfo.mTypeId = cursor.getInt(ValueBuyItemVersionControlTable.ITEM_ID_INDEX);
                valueBuyItemVersionControlInfo.mValueBuyItemVersion = cursor.getString(ValueBuyItemVersionControlTable.ITEM_VERSION_INDEX);
                valueBuyItemVersionControlInfo.mValueBuyRequestTime = cursor.getLong(ValueBuyItemVersionControlTable.REQUEST_TIME_INDEX);
                valueBuyItemVersionControlInfo.mValueBuyPage = cursor.getInt(ValueBuyItemVersionControlTable.ITEM_PAGE_INDEX);
                int hasNext = cursor.getInt(ValueBuyItemVersionControlTable.ITEM_HAS_NEXT_INDEX);
                valueBuyItemVersionControlInfo.mHasNext = hasNext == 1 ? true : false;
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
        return valueBuyItemVersionControlInfo;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

        // TODO delete value buy item Version control

    }

    @Override
    public void update(ContentValues values, String whereClause, String[] whereArgs) {

        mSQLiteDatabase.update(ValueBuyItemVersionControlTable.TABLE_NAME, values, whereClause, whereArgs);

    }
}
