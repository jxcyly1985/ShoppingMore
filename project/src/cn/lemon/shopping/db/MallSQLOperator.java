package cn.lemon.shopping.db;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-25
 * Time: 下午11:42
 * To change this template use File | Settings | File Templates.
 */
public class MallSQLOperator extends BaseSQLOperator {

    public MallSQLOperator(Context context) {
        super(context);
    }


    @Override
    public void insert(Object o) {

    }

    @Override
    public Object query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public Object query() {
        return null;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {

    }

    @Override
    public void update() {

    }
}
