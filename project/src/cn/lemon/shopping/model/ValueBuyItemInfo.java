package cn.lemon.shopping.model;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-22
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemInfo {

    public int mTypeId;
    public int mItemId;
    public String mTitle;
    public String mImageUrl;
    public String mItemLink;
    public String mPrice;

    public String toString() {
        return "TypeId " + mTypeId + " ItemId " + mItemId + " Title " + mTitle + " ImageUrl "
                + mImageUrl + " ItemLink " + mItemLink + " Price " + mPrice;
    }


}
