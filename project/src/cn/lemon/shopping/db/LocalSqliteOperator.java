package cn.lemon.shopping.db;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.shopping.model.CategoryEntryInfo;
import cn.lemon.shopping.model.MallEntryInfo;
import cn.lemon.shopping.model.MallTotalInfo;

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

    public void insertMallTotalInfo(MallTotalInfo mallTotalInfo) {
        if (mallTotalInfo != null) {
            List<CategoryEntryInfo> categoryEntryInfos = mallTotalInfo.mCategoryList;
            insertCategory(categoryEntryInfos);

            int mallListSize = mallTotalInfo.mCategoryMappingMall.size();

            for (int i = 0; i < mallListSize; ++i) {
                List<MallEntryInfo> mallEntryInfos = mallTotalInfo.mCategoryMappingMall.valueAt(i);
                insertMallInfo(mallEntryInfos);
            }

        }

    }

    public void insertCategory(CategoryEntryInfo categoryInfo) {

        if (categoryInfo != null) {
            ContentValues contentValues = new ContentValues();
            mSQLiteDatabase.insert(MallCategoryTable.TABLE_NAME, null, contentValues);
        }

    }

    public void insertCategory(List<CategoryEntryInfo> categoryInfoArray) {

        if (categoryInfoArray != null) {
            mSQLiteDatabase.beginTransaction();
            try {
                for (CategoryEntryInfo categoryInfo : categoryInfoArray) {
                    insertCategory(categoryInfo);
                }
                mSQLiteDatabase.setTransactionSuccessful();
            } finally {

                mSQLiteDatabase.endTransaction();
            }
        }

    }

    public void insertMallInfo(MallEntryInfo mallInfo) {
        if (mallInfo != null) {
            ContentValues contentValues = new ContentValues();
            mSQLiteDatabase.insert(MallInfoTable.TABLE_NAME, null, contentValues);
        }

    }

    public void insertMallInfo(List<MallEntryInfo> mallInfoArray) {

        if (mallInfoArray != null) {

            mSQLiteDatabase.beginTransaction();
            try {
                for (MallEntryInfo mallInfo : mallInfoArray) {
                    insertMallInfo(mallInfo);
                }
                mSQLiteDatabase.setTransactionSuccessful();
            } finally {

                mSQLiteDatabase.endTransaction();
            }

        }

    }

    public List<CategoryEntryInfo> getMallCategory() {

        List<CategoryEntryInfo> categoryInfos = null;
        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(MallCategoryTable.TABLE_NAME, MallCategoryTable.COLUMNS, null,
                    null, null, null, null);

            CategoryEntryInfo categoryEntryInfo = null;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                categoryInfos = new ArrayList<CategoryEntryInfo>();
                do {
                    categoryEntryInfo = new CategoryEntryInfo();
                    categoryEntryInfo.mServerId = cursor.getInt(MallCategoryTable.CATEGORY_ID_INDEX);
                    categoryEntryInfo.mCategoryName = cursor.getString(MallCategoryTable.CATEGORY_NAME_INDEX);
                    categoryInfos.add(categoryEntryInfo);
                } while (cursor.moveToNext());

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryInfos;

    }

    public List<MallEntryInfo> getMallInfo() {

        Cursor cursor = null;
        List<MallEntryInfo> mallInfos = null;
        try {
            cursor = mSQLiteDatabase.query(MallInfoTable.TABLE_NAME, MallInfoTable.COLUMNS, null, null, null,
                    null, null);

            if (cursor.getCount() > 0) {
                mallInfos = new ArrayList<MallEntryInfo>();
                cursor.moveToFirst();
                MallEntryInfo mallInfo = null;
                do {
                    mallInfo = new MallEntryInfo();
                    mallInfo.mName = cursor.getString(MallInfoTable.MALL_NAME_INDEX);
                    mallInfo.mIconUrl = cursor.getString(MallInfoTable.MALL_ICON_URL_INDEX);
                    mallInfo.mLinkedUrl = cursor.getString(MallInfoTable.MALL_URL_INDEX);
                    mallInfo.mCategoryId = cursor.getInt(MallInfoTable.MALL_CATEGORY_ID_INDEX);
                    mallInfo.mWeight = cursor.getInt(MallInfoTable.MALL_WEIGHT_INDEX);
                    mallInfos.add(mallInfo);
                } while (cursor.moveToNext());

            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return mallInfos;
    }

    public List<MallEntryInfo> getMallInfoByCategory(int categoryId) {

        String selection = MallInfoTable.MALL_CATEGORY_ID + "=?";
        String categoryIdString = String.valueOf(categoryId);
        String[] selectionArgs = new String[] {categoryIdString};

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
                    mallInfo.mName = cursor.getString(MallInfoTable.MALL_NAME_INDEX);
                    mallInfo.mIconUrl = cursor.getString(MallInfoTable.MALL_ICON_URL_INDEX);
                    mallInfo.mLinkedUrl = cursor.getString(MallInfoTable.MALL_URL_INDEX);
                    mallInfo.mCategoryId = cursor.getInt(MallInfoTable.MALL_CATEGORY_ID_INDEX);
                    mallInfo.mWeight = cursor.getInt(MallInfoTable.MALL_WEIGHT_INDEX);
                    mallInfos.add(mallInfo);
                } while (cursor.moveToNext());

            }

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return mallInfos;

    }
}
