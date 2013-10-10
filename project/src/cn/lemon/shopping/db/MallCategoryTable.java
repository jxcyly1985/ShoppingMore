package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallCategoryTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_category";

    public static final String CATEGORY_ID = "server_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String VERSION = "version";

    public static String SP = " ";

    public static String[] COLUMNS = new String[] {CATEGORY_NAME, VERSION};

    public static String getCreateSQL() {

        String CREATE_SQL = "CREATE TABLE" + SP + TABLE_NAME + SP + "(" + MallCategoryTable._ID
                + "INTEGER PRIMARY KEY," + CATEGORY_NAME + SP + "TEXT," + SP + VERSION + SP + "TEXT)";
        return CREATE_SQL;
    }

    public static String getDropSQL() {

        String DROP_SQL = "";
        return DROP_SQL;

    }

}
