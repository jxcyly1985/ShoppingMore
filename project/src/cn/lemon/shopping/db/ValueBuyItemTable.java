package cn.lemon.shopping.db;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-24
 * Time: 下午8:05
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemTable implements BaseColumns {

    public static final String TABLE_NAME = "value_buy_item";

    public static final String TYPE_ID = "type_id";
    public static final String ITEM_TITLE = "title";
    public static final String ITEM_IMAGE = "image";
    public static final String ITEM_LINK = "link";
    public static final String ITEM_PRICE = "price";

    public static final String[] COLUMNS = new String[]{TYPE_ID, ITEM_TITLE, ITEM_IMAGE, ITEM_LINK, ITEM_PRICE};


    public static String getCreateSQL() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE ")
                    .append(TABLE_NAME)
                    .append("(")
                    .append(_ID)
                    .append(" INTEGER PRIMARY KEY,")
                    .append(TYPE_ID)
                    .append(" INTEGER,")
                    .append(ITEM_TITLE)
                    .append(" TEXT,")
                    .append(ITEM_IMAGE)
                    .append(" TEXT,")
                    .append(ITEM_LINK)
                    .append(" TEXT,")
                    .append(ITEM_PRICE)
                    .append(" TEXT)");
        return stringBuffer.toString();
    }

    public static String getDropSQL() {

        return "DROP TABLE IF EXISTS value_buy_item";
    }


}
