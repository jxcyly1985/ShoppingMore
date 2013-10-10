package cn.lemon.shopping.model;

import java.util.List;

import android.content.Context;
import cn.lemon.shopping.db.LocalSqliteOperator;

public class ShoppingMoreDomainDataManager {

    private Context mContext;
    private LocalSqliteOperator mLocalSqliteOperator;

    private ShoppingMoreDomainDataManager() {

    }

    private static class ShoppingMoreDomainDataManagerHolder {

        static ShoppingMoreDomainDataManager sInstance = new ShoppingMoreDomainDataManager();
    }

    public static ShoppingMoreDomainDataManager getInstance() {
        return ShoppingMoreDomainDataManagerHolder.sInstance;
    }

    public void initialize(Context context) {

        mContext = context;
        mLocalSqliteOperator = LocalSqliteOperator.getInstance(mContext);
    }

    public void insertCategory(List<CategoryEntryInfo> categoryInfoArray) {

        mLocalSqliteOperator.insetCategory(categoryInfoArray);
    }
}
