package cn.lemon.shopping.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-3
 * Time: 下午9:37
 * To change this template use File | Settings | File Templates.
 */
public class AdInfo {

    public long mRequestTime;
    public boolean mIsSucceed;
    public String mMessage;
    public List<AdData> mDatas;
    public String mVersion;

    public static class AdData{
        public String mTitle;
        public String mImageURL;
        public String mLinkURL;
    }

    public String toString(){

        return "IsSuccess " + mIsSucceed + " Message " + mMessage + " Datas " + mDatas;
    }

}
