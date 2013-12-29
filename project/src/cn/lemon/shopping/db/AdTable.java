package cn.lemon.shopping.db;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-3
 * Time: 下午10:25
 * To change this template use File | Settings | File Templates.
 */
public class AdTable implements BaseColumns {

    public static final String AD_TITLE = "title";
    public static final String AD_IMAGEURL = "image_url";
    public static final String AD_LINK = "ad_link";

    public static final String[] COLUMNS = new String[]{AD_TITLE, AD_IMAGEURL, AD_LINK};

    public static String getCreateSQL(){

        String sql = "";
        return sql;
    }
}
