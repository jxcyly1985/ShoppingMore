package cn.lemon.shopping.db;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-29
 * Time: 下午9:07
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseVersionControlTable implements BaseColumns {

    public static final String TABLE_NAME = "database_version_control";

    public static final String MALL_SERVER_VERSION = "mall_version";
    public static final String MALL_REQUEST_TIME = "mall_request_time";
    public static final String MALL_REQUEST_PAGE = "mall_page";
    public static final String MALL_HAS_NEXT = "mall_has_next";

    public static final String VALUE_BUY_ITEM_VERSION = "value_buy_item_version";
    public static final String VALUE_BUY_REQUEST_TIME = "value_buy_request_time";
    public static final String VALUE_BUY_ITEM_PAGE = "value_buy_item_page";
    public static final String VALUE_BUY_ITEM_HAS_NEXT = "value_buy_has_next";

    public static String[] COLUMNS = new String[]{MALL_SERVER_VERSION, MALL_REQUEST_TIME,
            MALL_REQUEST_PAGE, MALL_HAS_NEXT, VALUE_BUY_ITEM_VERSION, VALUE_BUY_REQUEST_TIME,
            VALUE_BUY_ITEM_PAGE, VALUE_BUY_ITEM_HAS_NEXT};


    public static final int MALL_VERSION_INDEX = 0;
    public static final int MALL_REQUEST_TIME_INDEX = 1;
    public static final int MALL_PAGE_INDEX = 2;
    public static final int MALL_HAS_NEXT_INDEX = 3;
    public static final int VALUE_BUY_VERSION = 4;
    public static final int VALUE_BUY_REQUEST_INDEX = 5;
    public static final int VALUE_BUY_PAGE_INDEX = 6;
    public static final int VALUE_BUY_HAS_NEXT_INDEX = 7;

    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ")
                .append(_ID)
                .append(" INTEGER PRIMARY KEY,")
                .append(TABLE_NAME)
                .append("(")
                .append(MALL_SERVER_VERSION)
                .append(" TEXT,")
                .append(MALL_REQUEST_TIME)
                .append(" NUMERIC,")
                .append(MALL_REQUEST_PAGE)
                .append(" INTEGER,")
                .append(MALL_HAS_NEXT)
                .append(" INTEGER,")
                .append(VALUE_BUY_ITEM_VERSION)
                .append(" TEXT,")
                .append(VALUE_BUY_REQUEST_TIME)
                .append(" NUMERIC,")
                .append(VALUE_BUY_ITEM_PAGE)
                .append(" INTEGER,")
                .append(VALUE_BUY_ITEM_HAS_NEXT)
                .append(" INTEGER,")
                .append(")");

        return stringBuffer.toString();

    }

    public static String getDropSQL() {

        return "DROP TABLE IF EXISTS database_version_control";
    }

    public static String getCreateDefaultRow() {

        return "INSERT INTO database_version_control VALUES ('0', 0, 0, 1, '0', 0, 0, 1) ";
    }


}
