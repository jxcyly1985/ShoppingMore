package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.lemon.shopping.model.CategoryEntryInfo;
import cn.lemon.shopping.model.MallEntryInfo;
import cn.lemon.shopping.model.MallTotalInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            try{
                mSQLiteDatabase.beginTransaction();
                for(CategoryEntryInfo categoryEntryInfo : categoryEntryInfos){
                    insertCategory(categoryEntryInfo);
                    for(MallEntryInfo mallEntryInfo : categoryEntryInfo.mMallEntryInfoList)  {
                        insertMallInfo(mallEntryInfo);
                    }
                }
                mSQLiteDatabase.setTransactionSuccessful();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mSQLiteDatabase.endTransaction();
            }

        }

    }

    public void insertCategory(CategoryEntryInfo categoryInfo) {

        if (categoryInfo != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MallCategoryTable.CATEGORY_ID, categoryInfo.mServerId);
            contentValues.put(MallCategoryTable.CATEGORY_NAME, categoryInfo.mCategoryName);
            contentValues.put(MallCategoryTable.CATEGORY_ICON, categoryInfo.mIconUrl);
            contentValues.put(MallCategoryTable.CATEGROY_BG_COLOR, categoryInfo.mBackgroundColor);
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
            contentValues.put(MallInfoTable.MALL_NAME, mallInfo.mName);
            contentValues.put(MallInfoTable.MALL_ICON_URL, mallInfo.mImageUrl);
            contentValues.put(MallInfoTable.MALL_URL, mallInfo.mLinkedUrl);
            contentValues.put(MallInfoTable.MALL_CATEGORY_ID, mallInfo.mCategoryId);
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

    public Map<String,CategoryEntryInfo> getMallCategory() {

        Map<String,CategoryEntryInfo> categoryInfosMap = null;
        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(MallCategoryTable.TABLE_NAME, MallCategoryTable.COLUMNS, null,
                    null, null, null, null);

            CategoryEntryInfo categoryEntryInfo = null;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                categoryInfosMap = new HashMap<String,CategoryEntryInfo>();
                do {
                    categoryEntryInfo = new CategoryEntryInfo();
                    categoryEntryInfo.mServerId = cursor.getString(MallCategoryTable.CATEGORY_ID_INDEX);
                    categoryEntryInfo.mCategoryName = cursor.getString(MallCategoryTable.CATEGORY_NAME_INDEX);
                    categoryEntryInfo.mIconUrl = cursor.getString(MallCategoryTable.CATEGORY_ICON_INDEX);
                    categoryEntryInfo.mBackgroundColor = cursor.getString(MallCategoryTable.CATEGORY_BG_COLOR_INDEX);
                    categoryEntryInfo.mMallEntryInfoList = new ArrayList<MallEntryInfo>();
                    categoryInfosMap.put(categoryEntryInfo.mServerId, categoryEntryInfo);
                } while (cursor.moveToNext());

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryInfosMap;

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
                    mallInfo.mImageUrl = cursor.getString(MallInfoTable.MALL_ICON_URL_INDEX);
                    mallInfo.mLinkedUrl = cursor.getString(MallInfoTable.MALL_URL_INDEX);
                    mallInfo.mCategoryId = cursor.getString(MallInfoTable.MALL_CATEGORY_ID_INDEX);
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
                    mallInfo.mImageUrl = cursor.getString(MallInfoTable.MALL_ICON_URL_INDEX);
                    mallInfo.mLinkedUrl = cursor.getString(MallInfoTable.MALL_URL_INDEX);
                    mallInfo.mCategoryId = cursor.getString(MallInfoTable.MALL_CATEGORY_ID_INDEX);
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
