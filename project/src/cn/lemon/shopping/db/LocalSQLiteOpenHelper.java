package cn.lemon.shopping.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.lemon.shopping.ShoppingConfig;
import cn.lemon.utils.DebugUtil;

public class LocalSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "LocalSQLiteOpenHelper";

    private static final int DB_VERSION = ShoppingConfig.DB_VERSION;
    private static final String DB_NAME = "shopping_more.db";

    private static LocalSQLiteOpenHelper sInstance;

    public synchronized static LocalSQLiteOpenHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new LocalSQLiteOpenHelper(context);
        }

        return sInstance;
    }

    private LocalSQLiteOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        DebugUtil.debug(TAG, "onCreate");

        db.execSQL(MallCategoryTable.getCreateSQL());
        db.execSQL(MallInfoTable.getCreateSQL());
        db.execSQL(ValueBuyItemTable.getCreateSQL());
        db.execSQL(MallVersionControlTable.getCreateSQL());
        db.execSQL(MallVersionControlTable.getCreateDefaultRow());
        db.execSQL(ValueBuyItemVersionControlTable.getCreateSQL());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        DebugUtil.debug(TAG, "onUpgrade oldVersion " + oldVersion + " newVersion " + newVersion);

        db.execSQL(MallCategoryTable.getDropSQL());
        db.execSQL(MallInfoTable.getDropSQL());
        db.execSQL(ValueBuyItemTable.getDropSQL());
        db.execSQL(MallVersionControlTable.getDropSQL());
        db.execSQL(ValueBuyItemVersionControlTable.getDropSQL());
        onCreate(db);

    }

}
