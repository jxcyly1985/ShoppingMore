package cn.lemon.shopping;

import android.app.Application;
import cn.lemon.shopping.db.DatabaseVersionControlOperator;
import cn.lemon.shopping.model.DatabaseVersionControlInfo;
import cn.lemon.shopping.model.ShoppingMoreDomainDataManager;
import cn.lemon.utils.StaticUtils;

public class ShoppingMoreApplication extends Application {

    public static final String TAG = "ShoppingMoreApplication";
    private DatabaseVersionControlInfo mDatabaseVersionControlInfo;


    @Override
    public void onCreate() {

        super.onCreate();

        StaticUtils.getInstance().init(this);
        ShoppingMoreDomainDataManager.getInstance().initialize(this);
        ImageFetcherManager.getInstance().init(this);
        DatabaseVersionControlOperator databaseVersionControlOperator = new DatabaseVersionControlOperator(this);
        mDatabaseVersionControlInfo = databaseVersionControlOperator.query(null, null, null, null);
    }

}
