package cn.lemon.shopping.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
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

    public MallTotalInfo getMallTotalInfo() {

        MallTotalInfo mallTotalInfo = null;

        List<CategoryEntryInfo> categoryInfos = mLocalSqliteOperator.getMallCategory();

        if (categoryInfos != null) {
            mallTotalInfo = new MallTotalInfo();
            mallTotalInfo.mCategoryList = categoryInfos;
            List<MallEntryInfo> mallInfos = mLocalSqliteOperator.getMallInfo();
            if (mallInfos != null) {
                SparseArray<List<MallEntryInfo>> sparseArry = new SparseArray<List<MallEntryInfo>>();

                for (MallEntryInfo mallEntryInfo : mallInfos) {
                    List<MallEntryInfo> mallInfoArray = sparseArry.get(mallEntryInfo.mCategoryId);
                    if (mallInfoArray == null) {
                        mallInfoArray = new ArrayList<MallEntryInfo>();
                    }
                    mallInfoArray.add(mallEntryInfo);

                    sparseArry.append(mallEntryInfo.mCategoryId, mallInfoArray);
                }

                mallTotalInfo.mCategoryMappingMall = sparseArry;
            }

        } else {

            ModelUtils.sendMallRequest();
        }

        return mallTotalInfo;

    }

    public void insertCategory(List<CategoryEntryInfo> categoryInfoArray) {

        mLocalSqliteOperator.insetCategory(categoryInfoArray);
    }
}
