package cn.lemon.shopping.model;

import java.util.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.SparseArray;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.shopping.db.LocalSqliteOperator;
import cn.lemon.utils.DebugUtil;

public class ShoppingMoreDomainDataManager {

    private static final String TAG = "ShoppingMoreDomainDataManager";
    private Context mContext;
    private LocalSqliteOperator mLocalSqliteOperator;

    public static final long AD_REQUEST_TIMER = 24 * 3600 * 1000;
    private static final String COMMON_USER_INFO_FILE = "common_user_info";
    private static final String KEY_MALL_VERSION = "mall_version";

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

        Map<String, CategoryEntryInfo> categoryInfosMap = mLocalSqliteOperator.getMallCategory();
        if (categoryInfosMap != null) {
            MallTotalInfo mallTotalInfo = getDataFromDatabase(categoryInfosMap);
            return mallTotalInfo;
        }
        ModelUtils.sendMallRequest(mMallInfoHandler);
        return null;

    }


    private boolean isAdInfoExpired(AdInfo adInfo) {

        long thisTime = System.currentTimeMillis();
        long pastTime = thisTime - adInfo.mRequestTime;
        return pastTime > AD_REQUEST_TIMER;
    }

    public AdInfo getAdInfo() {

        AdInfo adInfo = ModelUtils.readAdInfo();

        DebugUtil.debug(TAG, "getAdInfo adInfo " + adInfo);

        if (adInfo != null) {

            if (!isAdInfoExpired(adInfo)) {
                return adInfo;
            }
            ModelUtils.sendAdRequest(null);
            return adInfo;
        }
        ModelUtils.sendAdRequest(mAdInfoHandler);
        return null;
    }

    private void localizeMallTotalInfo(MallTotalInfo mallTotalInfo) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(COMMON_USER_INFO_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MALL_VERSION, mallTotalInfo.mVersion);
        editor.commit();

        mLocalSqliteOperator.insertMallTotalInfo(mallTotalInfo);
    }

    private LemonNetWorkHandler mMallInfoHandler = new LemonNetWorkHandler() {

        @Override
        public void onHandleReceiveError() {
            DebugUtil.debug(TAG, "MallInfoHandler onHandleReceiveError");
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_NET_WORK_ERROR, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);

        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "MallInfoHandler result " + result);

            MallTotalInfo mallTotalInfo = ModelUtils.jsonToMallTotalInfoObject(result);
            localizeMallTotalInfo(mallTotalInfo);
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_MALL_DATA_RETURN, mallTotalInfo, 0,
                    0);
            MessageManager.getInstance().sendNotifyMessage(msg);

        }

    };

    private LemonNetWorkHandler mAdInfoHandler = new LemonNetWorkHandler() {

        @Override
        public void onHandleReceiveError() {
            DebugUtil.debug(TAG, "AdInfoHandler onHandleReceiveError");
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_NET_WORK_ERROR, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "AdInfoHandler onHandleReceiveSuccess result " + result);

            AdInfo adInfo = ModelUtils.jsonToAdInfoObject(result);
            ModelUtils.localizeAdInfo(result);
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_AD_DATA_RETURN, adInfo, 0,
                    0);
            MessageManager.getInstance().sendNotifyMessage(msg);

        }

    };


    private String getMallServerVersion() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(COMMON_USER_INFO_FILE, 0);
        return sharedPreferences.getString("mall_version", "0");
    }

    private MallTotalInfo getDataFromDatabase(
            Map<String, CategoryEntryInfo> categoryInfosMap) {

        MallTotalInfo mallTotalInfo = new MallTotalInfo();
        List<MallEntryInfo> mallInfos = mLocalSqliteOperator.getMallInfo();
        if (mallInfos != null) {

            CategoryEntryInfo categoryEntryInfo = null;
            ArrayList<CategoryEntryInfo> categoryEntryInfoArrayList = new ArrayList<CategoryEntryInfo>();
            for (MallEntryInfo mallEntryInfo : mallInfos) {
                categoryEntryInfo = categoryInfosMap.get(mallEntryInfo.mCategoryId);
                categoryEntryInfo.mMallEntryInfoList.add(mallEntryInfo);
            }

            for (Map.Entry<String, CategoryEntryInfo> entry : categoryInfosMap.entrySet()) {
                categoryEntryInfoArrayList.add(entry.getValue());
            }

            Collections.sort(categoryEntryInfoArrayList, new Comparator<CategoryEntryInfo>() {
                @Override
                public int compare(CategoryEntryInfo lhs, CategoryEntryInfo rhs) {
                    return lhs.mServerId.compareTo(rhs.mServerId);
                }
            });

            mallTotalInfo.mCategoryList = categoryEntryInfoArrayList;
            mallTotalInfo.mVersion = getMallServerVersion();
        }

        return mallTotalInfo;
    }

}
