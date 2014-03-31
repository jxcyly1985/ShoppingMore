package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallCategoryTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_category";

    public static final String CATEGORY_ID = "server_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_ICON = "icon";
    public static final String CATEGORY_BG_COLOR = "color";

    public static String[] COLUMNS = new String[]{CATEGORY_ID, CATEGORY_NAME, CATEGORY_ICON, CATEGORY_BG_COLOR};

    public static final int CATEGORY_ID_INDEX = 0;
    public static final int CATEGORY_NAME_INDEX = 1;
    public static final int CATEGORY_ICON_INDEX = 2;
    public static final int CATEGORY_BG_COLOR_INDEX = 3;

    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(_ID)
                .append(" INTEGER PRIMARY KEY,")
                .append(CATEGORY_ID)
                .append(" TEXT,")
                .append(CATEGORY_NAME)
                .append(" TEXT,")
                .append(CATEGORY_ICON)
                .append(" TEXT,")
                .append(CATEGORY_BG_COLOR)
                .append(" TEXT,")
                .append(")");

        return stringBuffer.toString();
    }

    public static String getDropSQL() {

        return "DROP TABLE IF EXISTS mall_category";

    }

}
