package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallInfoTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_infos";
    public static final String MALL_NAME = "mall_name";
    public static final String MALL_ICON_URL = "mall_iconUrl";
    public static final String MALL_URL = "mall_url";
    public static final String MALL_CATEGORY = "mall_category";
    public static final String MALL_WEIGHT = "mall_weight";

    public static String SP = " ";

    public static String[] COLUMNS = new String[] {MALL_NAME, MALL_ICON_URL, MALL_URL, MALL_CATEGORY,
            MALL_WEIGHT};

    public static String getCreateSQL() {

        String CREATE_SQL = "CREATE TABLE" + SP + TABLE_NAME + SP + "(" + MallInfoTable._ID
                + "INTEGER PRIMARY KEY," + MALL_NAME + SP + "TEXT," + SP + MALL_ICON_URL + SP + "TEXT,"
                + MALL_URL + SP + "TEXT," + MALL_WEIGHT + SP + "INTEGER" + ")";
        return CREATE_SQL;
    }

    public static String getDropSQL() {
        String DROP_SQL = "";
        return DROP_SQL;

    }
}
