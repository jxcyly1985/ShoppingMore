package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallInfoTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_infos";
    public static final String MALL_NAME = "mall_name";
    public static final String MALL_ICON_URL = "mall_imageUrl";
    public static final String MALL_URL = "mall_linkUrl";
    public static final String MALL_CATEGORY_ID = "category_id";
    public static final String MALL_WEIGHT = "mall_weight";

    public static final int MALL_NAME_INDEX = 0;
    public static final int MALL_ICON_URL_INDEX = 1;
    public static final int MALL_URL_INDEX = 2;
    public static final int MALL_CATEGORY_ID_INDEX = 3;
    public static final int MALL_WEIGHT_INDEX = 4;

    public static String[] COLUMNS = new String[]{MALL_NAME, MALL_ICON_URL, MALL_URL, MALL_CATEGORY_ID,
            MALL_WEIGHT};

    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(_ID)
                .append(" INTEGER PRIMARY KEY,")
                .append(MALL_NAME)
                .append(" TEXT,")
                .append(MALL_ICON_URL)
                .append(" TEXT,")
                .append(MALL_URL)
                .append(" TEXT,")
                .append(MALL_CATEGORY_ID)
                .append(" TEXT,")
                .append(MALL_WEIGHT)
                .append(" INTEGER,")
                .append(")");

        return stringBuffer.toString();
    }

    public static String getDropSQL() {
        return "DROP TABLE IF EXISTS mall_infos";

    }
}
