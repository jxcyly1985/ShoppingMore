package cn.lemon.shopping.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-8
 * Time: 下午2:55
 * To change this template use File | Settings | File Templates.
 */
public class CommodityItems {

    public long mRequestTime;
    public boolean mIsSucceed;
    public boolean mHasNext;
    public int mPageIndex;
    public String mMsg;
    public String mVersionCode;

    public List<CommodityItem> mCommodityItemList;

}
