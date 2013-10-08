package cn.lemon.shopping.db;

import android.provider.BaseColumns;

public class MallInfoTable implements BaseColumns {

	public static final String TABLE_NAME = "mall_infos";
	public static final String MALL_NAME = "name";
	public static final String MALL_ICON_URL = "iconUrl";

	public static String CREATE_SQL = "";
	public static String SP = " ";

	public static String getCreateSQL() {

		CREATE_SQL = "CREATE TABLE" + SP + TABLE_NAME + SP + "("
				+ MallInfoTable._ID + "INTEGER PRIMARY KEY," + MALL_NAME + SP
				+ "TEXT," + SP + MALL_ICON_URL + SP + "TEXT)";
		return CREATE_SQL;
	}
}
