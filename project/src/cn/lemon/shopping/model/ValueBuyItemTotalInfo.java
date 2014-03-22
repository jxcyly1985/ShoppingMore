package cn.lemon.shopping.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-22
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemTotalInfo {

    public long mRequestTime;
    public boolean mIsSucceed;
    public boolean mHasNext;
    public String mMsg;
    public String mVersion;
    public int mCurrentPage;
    public List<ValueBuyItemInfo> mValueBuyItemInfoList;
}
