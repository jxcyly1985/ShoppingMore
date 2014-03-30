package cn.lemon.shopping.db;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-30
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemVersionControlTable implements BaseColumns {

    public static final String TABLE_NAME = "value_buy_item_control";

    public static final String VALUE_BUY_ITEM_TYPE_ID = "value_buy_type_id";
    public static final String VALUE_BUY_ITEM_VERSION = "value_buy_item_version";
    public static final String VALUE_BUY_REQUEST_TIME = "value_buy_request_time";
    public static final String VALUE_BUY_ITEM_PAGE = "value_buy_item_page";
    public static final String VALUE_BUY_ITEM_HAS_NEXT = "value_buy_has_next";

    public static String[] COLUMNS = new String[]{VALUE_BUY_ITEM_TYPE_ID, VALUE_BUY_ITEM_VERSION,
            VALUE_BUY_REQUEST_TIME, VALUE_BUY_ITEM_PAGE, VALUE_BUY_ITEM_HAS_NEXT};

    public static final int ITEM_ID_INDEX = 0;
    public static final int ITEM_VERSION_INDEX = 1;
    public static final int REQUEST_TIME_INDEX = 2;
    public static final int ITEM_PAGE_INDEX = 3;
    public static final int ITEM_HAS_NEXT_INDEX = 5;

    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(_ID)
                .append(" INTEGER PRIMARY KEY,")
                .append(VALUE_BUY_ITEM_TYPE_ID)
                .append(" INTEGER ON CONFLICT REPLACE,")
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

        return "DROP TABLE IF EXISTS value_buy_item_control";
    }
}
