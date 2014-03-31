package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.model.ValueBuyItemInfo;
import cn.lemon.shopping.model.ValueBuyItemTotalInfo;
import cn.lemon.shopping.model.ValueBuyItemVersionControlInfo;

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

    private ValueBuyItemVersionControlOperator mItemVersionControlOperator;

    public ValueBuyItemSQLOperator(Context context) {
        super(context);
        mItemVersionControlOperator = new ValueBuyItemVersionControlOperator(context);
    }


    @Override
    public void insert(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {
        try {
            mSQLiteDatabase.beginTransaction();

            insertValueBuyItem(valueBuyItemTotalInfo);
            updateValueBuyItemVersionInfo(valueBuyItemTotalInfo);

            mSQLiteDatabase.setTransactionSuccessful();
        } finally {
            mSQLiteDatabase.endTransaction();
        }

    }

    @Override
    public ValueBuyItemTotalInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (selection.startsWith(ValueBuyItemTable.TYPE_ID)) {
            return queryByTypeId(selection, selectionArgs, sortOrder);
        }

        return queryWithNoFilter(selection, selectionArgs, sortOrder);
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


    private ValueBuyItemTotalInfo queryWithNoFilter(String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(ValueBuyItemTable.TABLE_NAME, ValueBuyItemTable.COLUMNS,
                    selection, selectionArgs, null, null, sortOrder);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ValueBuyItemTotalInfo valueBuyItemTotalInfo = new ValueBuyItemTotalInfo();
                List<ValueBuyItemInfo> valueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();
                do {
                    ValueBuyItemInfo valueBuyItemInfo = new ValueBuyItemInfo();
                    valueBuyItemInfo.mTypeId = cursor.getInt(ValueBuyItemTable.TYPE_ID_INDEX);
                    valueBuyItemInfo.mItemId = cursor.getInt(ValueBuyItemTable.ITEM_ID_INDEX);
                    valueBuyItemInfo.mTitle = cursor.getString(ValueBuyItemTable.ITEM_TITLE_INDEX);
                    valueBuyItemInfo.mImageUrl = cursor.getString(ValueBuyItemTable.ITEM_IMAGE_INDEX);
                    valueBuyItemInfo.mItemLink = cursor.getString(ValueBuyItemTable.ITEM_LINK_INDEX);
                    valueBuyItemInfo.mPrice = cursor.getString(ValueBuyItemTable.ITEM_PRICE_INDEX);
                    valueBuyItemInfoList.add(valueBuyItemInfo);
                } while (cursor.moveToNext());

                int typeId = valueBuyItemInfoList.get(0).mTypeId;
                valueBuyItemTotalInfo.mTypeId = typeId;
                valueBuyItemTotalInfo.mValueBuyItemInfoList = valueBuyItemInfoList;
                return valueBuyItemTotalInfo;
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

        return null;
    }

    private ValueBuyItemTotalInfo setVersionControlInfo(String typeId, ValueBuyItemTotalInfo valueBuyItemTotalInfo) {
        String selection = ValueBuyItemVersionControlTable.VALUE_BUY_ITEM_TYPE_ID;
        String[] selectionArgs = new String[]{typeId};
        ValueBuyItemVersionControlInfo valueBuyItemVersionControlInfo = mItemVersionControlOperator
                .query(null, selection, selectionArgs, null);
        valueBuyItemTotalInfo.mTypeId = valueBuyItemVersionControlInfo.mTypeId;
        valueBuyItemTotalInfo.mVersion = valueBuyItemVersionControlInfo.mValueBuyItemVersion;
        valueBuyItemTotalInfo.mRequestTime = valueBuyItemVersionControlInfo.mValueBuyRequestTime;
        valueBuyItemTotalInfo.mCurrentPage = valueBuyItemVersionControlInfo.mValueBuyPage;
        valueBuyItemTotalInfo.mHasNext = valueBuyItemVersionControlInfo.mHasNext;
        return valueBuyItemTotalInfo;
    }

    private ValueBuyItemTotalInfo queryByTypeId(String selection, String[] selectionArgs, String sortOrder) {

        String typeId = selectionArgs[0];
        ValueBuyItemTotalInfo valueBuyItemTotalInfo = null;

        valueBuyItemTotalInfo = queryWithNoFilter(selection, selectionArgs, sortOrder);
        if (valueBuyItemTotalInfo != null) {
            valueBuyItemTotalInfo = setVersionControlInfo(typeId, valueBuyItemTotalInfo);
        }

        return valueBuyItemTotalInfo;
    }

    private void insertValueBuyItem(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {

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
    }

    private void updateValueBuyItemVersionInfo(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {

        long requestTime = System.currentTimeMillis();
        ValueBuyItemVersionControlInfo valueBuyItemVersionControlInfo = new ValueBuyItemVersionControlInfo();
        valueBuyItemVersionControlInfo.mTypeId = valueBuyItemTotalInfo.mTypeId;
        valueBuyItemVersionControlInfo.mValueBuyItemVersion = valueBuyItemTotalInfo.mVersion;
        valueBuyItemVersionControlInfo.mValueBuyRequestTime = requestTime;
        valueBuyItemVersionControlInfo.mValueBuyPage = valueBuyItemTotalInfo.mCurrentPage;
        valueBuyItemVersionControlInfo.mHasNext = valueBuyItemTotalInfo.mHasNext;
        mItemVersionControlOperator.insert(valueBuyItemVersionControlInfo);

    }
}
