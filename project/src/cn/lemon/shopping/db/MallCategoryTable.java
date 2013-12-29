package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallCategoryTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_category";

    public static final String CATEGORY_ID = "server_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_ICON = "icon";
    public static final String CATEGROY_BG_COLOR = "color";

    public static String SP = " ";

    public static String[] COLUMNS = new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_ICON, CATEGROY_BG_COLOR};

    public static final int CATEGORY_ID_INDEX = 0;
    public static final int CATEGORY_NAME_INDEX = 1;
    public static final int CATEGORY_ICON_INDEX = 2;
    public static final int CATEGORY_BG_COLOR_INDEX = 3;

    public static String getCreateSQL() {

        String CREATE_SQL = "CREATE TABLE" + SP
                + TABLE_NAME + SP
                + "(" + MallCategoryTable._ID + SP + "INTEGER PRIMARY KEY,"
                + CATEGORY_ID + SP + "TEXT,"
                + CATEGORY_NAME + SP + "TEXT,"
                + CATEGORY_ICON + SP + "TEXT,"
                + CATEGROY_BG_COLOR + SP + "TEXT" + ")";
        return CREATE_SQL;
    }

    public static String getDropSQL() {

        String DROP_SQL = "";
        return DROP_SQL;

    }

}
