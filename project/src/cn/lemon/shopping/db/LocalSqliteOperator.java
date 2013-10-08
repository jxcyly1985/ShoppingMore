package cn.lemon.shopping.db;

import android.content.Context;

public class LocalSqliteOperator {

    private LocalSqliteOpenHelper mLocalSqliteOpenHelper;

    public LocalSqliteOperator(Context context) {
        mLocalSqliteOpenHelper = new LocalSqliteOpenHelper(context);
    }

}
