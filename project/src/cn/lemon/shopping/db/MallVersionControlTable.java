package cn.lemon.shopping.db;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-29
 * Time: 下午9:07
 * To change this template use File | Settings | File Templates.
 */
public class MallVersionControlTable implements BaseColumns {

    public static final String TABLE_NAME = "mall_version_control";

    public static final String MALL_SERVER_VERSION = "mall_version";
    public static final String MALL_REQUEST_TIME = "mall_request_time";
    public static final String MALL_REQUEST_PAGE = "mall_page";
    public static final String MALL_HAS_NEXT = "mall_has_next";

    public static String[] COLUMNS = new String[]{MALL_SERVER_VERSION, MALL_REQUEST_TIME,
            MALL_REQUEST_PAGE, MALL_HAS_NEXT};


    public static final int MALL_VERSION_INDEX = 0;
    public static final int MALL_REQUEST_TIME_INDEX = 1;
    public static final int MALL_PAGE_INDEX = 2;
    public static final int MALL_HAS_NEXT_INDEX = 3;

    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(_ID)
                .append(" INTEGER PRIMARY KEY,")
                .append(MALL_SERVER_VERSION)
                .append(" TEXT,")
                .append(MALL_REQUEST_TIME)
                .append(" NUMERIC,")
                .append(MALL_REQUEST_PAGE)
                .append(" INTEGER,")
                .append(MALL_HAS_NEXT)
                .append(" INTEGER,")
                .append(")");

        return stringBuffer.toString();

    }

    public static String getDropSQL() {

        return "DROP TABLE IF EXISTS database_version_control";
    }

    public static String getCreateDefaultRow() {

        return "INSERT INTO database_version_control VALUES ('0', 0, 0, 1) ";
    }


}
