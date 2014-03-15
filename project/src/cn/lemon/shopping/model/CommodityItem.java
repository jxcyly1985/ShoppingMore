package cn.lemon.shopping.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-2-18
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class CommodityItem {

    public static final int IMAGE_LEFT_POS = 0;
    public static final int IMAGE_RIGHT_POS = 1;

    public int mImagePos;
    public String mCommodityName;
    public String mCommodityLink;
    public String mCommodityIconUrl;
    public String mCommodityNameColor;
    public boolean mHasTopSide = true;
    public List<CommodityCategory> mCommodityCategoryList;

}
