package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallCategoryTable implements BaseColumns {

	public static final String TABLE_NAME = "mall_category";

	public static final String CATEGORY_NAME = "name";
	public static final String VERSION = "version";

	public static String CREATE_SQL = "";
	public static String SP = " ";

	public static String getCreateSQL() {

		CREATE_SQL = "CREATE TABLE" + SP + TABLE_NAME + SP + "("
				+ MallCategoryTable._ID + "INTEGER PRIMARY KEY,"
				+ CATEGORY_NAME + SP + "TEXT," + SP + VERSION + SP + "TEXT)";
		return CREATE_SQL;
	}

}
