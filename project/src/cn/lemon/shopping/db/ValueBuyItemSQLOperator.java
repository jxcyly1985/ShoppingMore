package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.model.ValueBuyItemInfo;
import cn.lemon.shopping.model.ValueBuyItemTotalInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-25
 * Time: 下午11:43
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemSQLOperator extends BaseSQLOperator<ValueBuyItemTotalInfo> {

    public ValueBuyItemSQLOperator(Context context) {
        super(context);
    }


    @Override
    public void insert(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {
        try {
            mSQLiteDatabase.beginTransaction();

            List<ValueBuyItemInfo> valueBuyItemInfoList = valueBuyItemTotalInfo.mValueBuyItemInfoList;
            int typeId = valueBuyItemTotalInfo.mTypeId;
            if (valueBuyItemInfoList != null) {
                for (ValueBuyItemInfo item : valueBuyItemInfoList) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ValueBuyItemTable.TYPE_ID, typeId);
                    contentValues.put(ValueBuyItemTable.ITEM_ID, item.mItemId);
                    contentValues.put(ValueBuyItemTable.ITEM_TITLE, item.mTitle);
                    contentValues.put(ValueBuyItemTable.ITEM_IMAGE, item.mImageUrl);
                    contentValues.put(ValueBuyItemTable.ITEM_LINK, item.mItemLink);
                    contentValues.put(ValueBuyItemTable.ITEM_PRICE, item.mPrice);
                    mSQLiteDatabase.insert(ValueBuyItemTable.TABLE_NAME, null, contentValues);
                }
            }

            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
        }

    }

    @Override
    public ValueBuyItemTotalInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = mSQLiteDatabase.query(ValueBuyItemTable.TABLE_NAME, ValueBuyItemTable.COLUMNS,
                selection, selectionArgs, null, null, sortOrder);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ValueBuyItemTotalInfo valueBuyItemTotalInfo = new ValueBuyItemTotalInfo();
                List<ValueBuyItemInfo> valueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();
                do {
                    ValueBuyItemInfo valueBuyItemInfo = new ValueBuyItemInfo();
                    valueBuyItemInfo.mTitle = cursor.getString(ValueBuyItemTable.ITEM_TITLE_INDEX);
                    valueBuyItemInfo.mImageUrl = cursor.getString(ValueBuyItemTable.ITEM_IMAGE_INDEX);
                    valueBuyItemInfo.mItemLink = cursor.getString(ValueBuyItemTable.ITEM_LINK_INDEX);
                    valueBuyItemInfo.mPrice = cursor.getString(ValueBuyItemTable.ITEM_PRICE_INDEX);
                    valueBuyItemInfoList.add(valueBuyItemInfo);
                } while (cursor.moveToNext());

                valueBuyItemTotalInfo.mValueBuyItemInfoList = valueBuyItemInfoList;
                return valueBuyItemTotalInfo;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    @Override
    public ValueBuyItemTotalInfo query() {

        return null;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

    }

    @Override
    public void update(ContentValues values, String whereClause, String[] whereArgs) {

        mSQLiteDatabase.update(ValueBuyItemTable.TABLE_NAME, values, whereClause, whereArgs);

    }
}
