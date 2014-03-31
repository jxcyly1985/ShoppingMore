package cn.lemon.shopping.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.lemon.shopping.ShoppingConfig;
import cn.lemon.shopping.model.CategoryEntryInfo;
import cn.lemon.shopping.model.MallVersionControlInfo;
import cn.lemon.shopping.model.MallEntryInfo;
import cn.lemon.shopping.model.MallTotalInfo;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-25
 * Time: 下午11:42
 * To change this template use File | Settings | File Templates.
 */
public class MallSQLOperator extends BaseSQLOperator<MallTotalInfo> {


    protected MallVersionControlInfo mDatabaseVersionControlInfo;
    protected MallVersionControlOperator mMallVersionControlOperator;

    public MallSQLOperator(Context context) {
        super(context);
        mMallVersionControlOperator = new MallVersionControlOperator(mContext);
    }

    @Override
    public void insert(MallTotalInfo mallTotalInfo) {

        if (!shouldInsertDatabase(mallTotalInfo)) {
            return;
        }

        try {
            mSQLiteDatabase.beginTransaction();
            insertMallEntryInfo(mallTotalInfo);
            updateMallVersionControl(mallTotalInfo);
            mSQLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSQLiteDatabase.endTransaction();
        }


    }

    private boolean shouldInsertDatabase(MallTotalInfo mallTotalInfo) {
        if (mallTotalInfo == null) {
            return false;
        }
        if (mallTotalInfo.mCurrentPage == ShoppingConfig.VALUE_BUY_ITEM_MAX_PAGE_CACHE) {
            return false;
        }
        return true;
    }

    @Override
    public MallTotalInfo query() {
        return null;
    }

    @Override
    public MallTotalInfo query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Map<String, CategoryEntryInfo> categoryEntryInfoMap = getMallCategory();
        return getMallTotalInfo(categoryEntryInfoMap);
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

        // TODO can drop table more efficient
        mSQLiteDatabase.delete(MallVersionControlTable.TABLE_NAME, null, null);
    }

    @Override
    public void update(ContentValues values, String whereClause, String[] whereArgs) {

    }

    private void insertMallEntryInfo(MallTotalInfo mallTotalInfo) {

        List<CategoryEntryInfo> categoryEntryInfoList = mallTotalInfo.mCategoryList;
        for (CategoryEntryInfo categoryEntryInfo : categoryEntryInfoList) {
            insertCategory(categoryEntryInfo);
            for (MallEntryInfo mallEntryInfo : categoryEntryInfo.mMallEntryInfoList) {
                insertMallEntryInfo(mallEntryInfo);
            }
        }
    }

    private void updateMallVersionControl(MallTotalInfo mallTotalInfo) {

        long requestTime = System.currentTimeMillis();
        int hasNext = mallTotalInfo.mHasNext ? 1 : 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MallVersionControlTable.MALL_SERVER_VERSION, mallTotalInfo.mVersion);
        contentValues.put(MallVersionControlTable.MALL_REQUEST_TIME, requestTime);
        contentValues.put(MallVersionControlTable.MALL_REQUEST_PAGE, mallTotalInfo.mCurrentPage);
        contentValues.put(MallVersionControlTable.MALL_HAS_NEXT, hasNext);
        mMallVersionControlOperator.update(contentValues, null, null);

    }


    private void insertCategory(CategoryEntryInfo categoryInfo) {

        if (categoryInfo != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MallCategoryTable.CATEGORY_ID, categoryInfo.mServerId);
            contentValues.put(MallCategoryTable.CATEGORY_NAME, categoryInfo.mCategoryName);
            contentValues.put(MallCategoryTable.CATEGORY_ICON, categoryInfo.mIconUrl);
            contentValues.put(MallCategoryTable.CATEGORY_BG_COLOR, categoryInfo.mBackgroundColor);
            mSQLiteDatabase.insert(MallCategoryTable.TABLE_NAME, null, contentValues);
        }

    }

    private void insertMallEntryInfo(MallEntryInfo mallInfo) {
        if (mallInfo != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MallInfoTable.MALL_NAME, mallInfo.mName);
            contentValues.put(MallInfoTable.MALL_ICON_URL, mallInfo.mImageUrl);
            contentValues.put(MallInfoTable.MALL_URL, mallInfo.mLinkedUrl);
            contentValues.put(MallInfoTable.MALL_CATEGORY_ID, mallInfo.mCategoryId);
            mSQLiteDatabase.insert(MallInfoTable.TABLE_NAME, null, contentValues);
        }

    }

    private MallTotalInfo getMallTotalInfo(Map<String, CategoryEntryInfo> categoryInfoMap) {

        MallTotalInfo mallTotalInfo = new MallTotalInfo();

        boolean result = addMallEntryInfo(categoryInfoMap);
        if (!result) {
            return mallTotalInfo;
        }

        List<CategoryEntryInfo> categoryEntryInfoList = addCategoryEntryInfo(categoryInfoMap);
        sortCategoryEntryInfo(categoryEntryInfoList);
        setCategoryEntryInfoList(mallTotalInfo, categoryEntryInfoList);
        setDatabaseVersionControlInfo(mallTotalInfo);

        return mallTotalInfo;
    }

    private void setCategoryEntryInfoList(MallTotalInfo mallTotalInfo, List<CategoryEntryInfo> categoryEntryInfoList) {
        mallTotalInfo.mCategoryList = categoryEntryInfoList;
    }


    private void setDatabaseVersionControlInfo(MallTotalInfo mallTotalInfo) {

        mDatabaseVersionControlInfo = getDatabaseVersionControlInfo();
        mallTotalInfo.mVersion = mDatabaseVersionControlInfo.mMallVersion;
        mallTotalInfo.mRequestTime = mDatabaseVersionControlInfo.mMallRequestTime;
        mallTotalInfo.mCurrentPage = mDatabaseVersionControlInfo.mMallPage;
    }

    private MallVersionControlInfo getDatabaseVersionControlInfo() {

        return mDatabaseVersionControlInfo = mMallVersionControlOperator.query(null, null, null, null);
    }

    private Map<String, CategoryEntryInfo> getMallCategory() {

        Map<String, CategoryEntryInfo> categoryEntryInfoMap = null;
        Cursor cursor = null;
        try {
            cursor = mSQLiteDatabase.query(MallCategoryTable.TABLE_NAME, MallCategoryTable.COLUMNS, null,
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                categoryEntryInfoMap = new HashMap<String, CategoryEntryInfo>();
                do {
                    CategoryEntryInfo categoryEntryInfo = new CategoryEntryInfo();
                    categoryEntryInfo.mServerId = cursor.getString(MallCategoryTable.CATEGORY_ID_INDEX);
                    categoryEntryInfo.mCategoryName = cursor.getString(MallCategoryTable.CATEGORY_NAME_INDEX);
                    categoryEntryInfo.mIconUrl = cursor.getString(MallCategoryTable.CATEGORY_ICON_INDEX);
                    categoryEntryInfo.mBackgroundColor = cursor.getString(MallCategoryTable.CATEGORY_BG_COLOR_INDEX);
                    categoryEntryInfo.mMallEntryInfoList = new ArrayList<MallEntryInfo>();
                    categoryEntryInfoMap.put(categoryEntryInfo.mServerId, categoryEntryInfo);
                } while (cursor.moveToNext());

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryEntryInfoMap;

    }

    private List<MallEntryInfo> getFullMallInfoList() {

        Cursor cursor = null;
        List<MallEntryInfo> mallEntryInfoList = null;
        try {
            cursor = mSQLiteDatabase.query(MallInfoTable.TABLE_NAME, MallInfoTable.COLUMNS, null,
                    null, null, null, null);

            if (cursor.getCount() > 0) {
                mallEntryInfoList = new ArrayList<MallEntryInfo>();
                cursor.moveToFirst();
                do {
                    MallEntryInfo mallEntryInfo = new MallEntryInfo();
                    mallEntryInfo.mName = cursor.getString(MallInfoTable.MALL_NAME_INDEX);
                    mallEntryInfo.mImageUrl = cursor.getString(MallInfoTable.MALL_ICON_URL_INDEX);
                    mallEntryInfo.mLinkedUrl = cursor.getString(MallInfoTable.MALL_URL_INDEX);
                    mallEntryInfo.mCategoryId = cursor.getString(MallInfoTable.MALL_CATEGORY_ID_INDEX);
                    mallEntryInfoList.add(mallEntryInfo);
                } while (cursor.moveToNext());

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mallEntryInfoList;

    }

    private boolean addMallEntryInfo(Map<String, CategoryEntryInfo> categoryInfoMap) {
        List<MallEntryInfo> mallEntryInfoList = getFullMallInfoList();
        if (mallEntryInfoList == null) {
            return false;
        }

        for (MallEntryInfo mallEntryInfo : mallEntryInfoList) {
            String categoryId = mallEntryInfo.mCategoryId;
            CategoryEntryInfo categoryEntryInfo = categoryInfoMap.get(categoryId);
            categoryEntryInfo.mMallEntryInfoList.add(mallEntryInfo);
        }

        return true;

    }

    private List<CategoryEntryInfo> addCategoryEntryInfo(Map<String, CategoryEntryInfo> categoryInfoMap) {

        List<CategoryEntryInfo> categoryEntryInfoList = new ArrayList<CategoryEntryInfo>();

        for (Map.Entry<String, CategoryEntryInfo> entry : categoryInfoMap.entrySet()) {
            CategoryEntryInfo categoryEntryInfo = entry.getValue();
            categoryEntryInfoList.add(categoryEntryInfo);
        }
        return categoryEntryInfoList;
    }

    private void sortCategoryEntryInfo(List<CategoryEntryInfo> categoryEntryInfoList) {

        Collections.sort(categoryEntryInfoList, new Comparator<CategoryEntryInfo>() {
            @Override
            public int compare(CategoryEntryInfo lhs, CategoryEntryInfo rhs) {
                return lhs.mServerId.compareTo(rhs.mServerId);
            }
        });
    }


}
