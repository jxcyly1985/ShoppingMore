package cn.lemon.shopping.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Message;
import android.provider.Settings.Global;
import android.util.SparseArray;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.shopping.MessageConstants;
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

            getDataFromDatabase(categoryInfos);

        } else {

            ModelUtils.sendMallRequest(mMallInfoHandler);
        }

        return mallTotalInfo;

    }

    public void insertCategory(List<CategoryEntryInfo> categoryInfoArray) {

        mLocalSqliteOperator.insetCategory(categoryInfoArray);
    }

    private LemonNetWorkHandler mMallInfoHandler = new LemonNetWorkHandler() {

        @Override
        public void onHandleReceiveError() {

        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            MallTotalInfo mallTotalInfo = ModelUtils.jsonToObject(result);
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_LOAD_DATA_COMPLETE, mallTotalInfo,
                    0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);

        }

    };

    private MallTotalInfo getDataFromDatabase(List<CategoryEntryInfo> categoryInfos) {

        MallTotalInfo mallTotalInfo = new MallTotalInfo();
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

        return mallTotalInfo;
    }
}
