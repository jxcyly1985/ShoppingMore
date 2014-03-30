package cn.lemon.shopping.model;

import java.util.List;
import android.util.SparseArray;

public class MallTotalInfo {

    public long mRequestTime;
    public String mVersion;
    public String mMsg;
    public int mCurrentPage;
    public boolean mIsSucceed;
    public boolean mHasNext;
    public List<CategoryEntryInfo> mCategoryList;

}
