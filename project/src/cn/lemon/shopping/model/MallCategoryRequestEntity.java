package cn.lemon.shopping.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.network.LemonNetWorkRequest;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.shopping.db.LocalSQLiteOperator;
import cn.lemon.utils.DebugUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class MallCategoryRequestEntity extends BaseRequestEntity<MallTotalInfo> {

    private static final String TAG = "MallCategoryRequestEntity";

    private final int MALL_REQUEST_TIMER = 7 * 24 * 3600 * 1000;
    private final String DEFAULT_MALL_VERSION = "0";

    public static final String MALL_VERSION = "mall_version";
    private final String KEY_MALL_VERSION = "mall_version";
    private final String KEY_LAST_REQUEST_TIME = "request_time";

    private Context mContext;
    private LocalSQLiteOperator mLocalSQLiteOperator;
    private MallTotalInfo mMallTotalInfo;

    public MallCategoryRequestEntity(Context context) {
        mContext = context;
        mLocalSQLiteOperator = LocalSQLiteOperator.getInstance(mContext);
    }

    @Override
    public MallTotalInfo getRequestEntity() {

        Map<String, CategoryEntryInfo> categoryInfoMap = mLocalSQLiteOperator.getMallCategory();
        if (categoryInfoMap != null) {
            MallTotalInfo mallTotalInfo = getDataFromDatabase(categoryInfoMap);
            if (!shouldNewRequest()) {
                return mallTotalInfo;
            }
            sendRequest();
            return mallTotalInfo;

        }
        sendRequest();
        return null;
    }

    @Override
    protected void sendRequest() {
        sRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {

                LemonNetWorkRequest request = new LemonNetWorkRequest();
                request.mUrl = MALL_DATA_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, mMallInfoHandler);
                httpRequest.get();
            }
        });
    }

    @Override
    protected void localize() {

        // SP Version Code
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(COMMON_USER_INFO_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_MALL_VERSION, mMallTotalInfo.mVersion);
        editor.putLong(KEY_LAST_REQUEST_TIME, System.currentTimeMillis());
        editor.commit();
        // SQL
        if (isExpired()) {
            mLocalSQLiteOperator.deleteExpiredMallTotalInfo();
        }
        mLocalSQLiteOperator.insertMallTotalInfo(mMallTotalInfo);
    }

    @Override
    protected MallTotalInfo deSerialization() {
        mMallTotalInfo = new MallTotalInfo();

        try {
            JSONObject root = new JSONObject(mServerData);
            boolean isSucceed = root.getBoolean(JSON_KEY_SUCCESS);
            if (isSucceed) {
                JSONObject data = root.getJSONObject(JSON_KEY_DATA);
                JSONArray list = data.getJSONArray(JSON_KEY_LIST);
                CategoryEntryInfo categoryEntryInfo = null;
                MallEntryInfo mallEntryInfo = null;
                JSONObject categoryJsonObject = null;
                JSONObject mallJsonObject = null;
                JSONArray mallItemsArray = null;
                List<CategoryEntryInfo> categoryList = new ArrayList<CategoryEntryInfo>();
                List<MallEntryInfo> mallList = null;
                for (int i = 0; i < list.length(); ++i) {
                    categoryJsonObject = (JSONObject) list.opt(i);
                    categoryEntryInfo = new CategoryEntryInfo();
                    categoryEntryInfo.mServerId = categoryJsonObject.getString(JSON_KEY_ID);
                    categoryEntryInfo.mCategoryName = categoryJsonObject.getString(JSON_KEY_TITLE);
                    categoryEntryInfo.mIconUrl = categoryJsonObject.getString(JSON_KEY_ICON);
                    categoryEntryInfo.mBackgroundColor = categoryJsonObject.getString(JSON_KEY_BG_COLOR);

                    mallItemsArray = categoryJsonObject.getJSONArray(JSON_KEY_ITEM);
                    mallList = new ArrayList<MallEntryInfo>();
                    for (int j = 0; j < mallItemsArray.length(); ++j) {

                        mallEntryInfo = new MallEntryInfo();
                        mallJsonObject = (JSONObject) mallItemsArray.opt(j);
                        mallEntryInfo.mCategoryId = categoryEntryInfo.mServerId;
                        mallEntryInfo.mName = mallJsonObject.getString(JSON_KEY_NAME);
                        mallEntryInfo.mImageUrl = mallJsonObject.getString(JSON_KEY_IMG);
                        mallEntryInfo.mLinkedUrl = mallJsonObject.getString(JSON_KEY_LINK);
                        mallList.add(mallEntryInfo);
                    }

                    categoryEntryInfo.mMallEntryInfoList = mallList;
                    categoryList.add(categoryEntryInfo);
                }

                mMallTotalInfo.mCategoryList = categoryList;
                mMallTotalInfo.mVersion = root.getString(JSON_KEY_VERSION);

            } else {

                mMallTotalInfo.mMsg = root.getString(JSON_KEY_MSG);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mMallTotalInfo;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_MALL_DATA_RETURN, mMallTotalInfo, 0,
                    0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        } else {
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_NET_WORK_ERROR, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }


    }

    @Override
    protected boolean shouldNewRequest() {

        long thisTime = System.currentTimeMillis();
        long pastTime = thisTime - mMallTotalInfo.mRequestTime;
        return pastTime > MALL_REQUEST_TIMER;
    }

    @Override
    protected boolean isExpired() {

        return false;
    }

    private LemonNetWorkHandler mMallInfoHandler = new LemonNetWorkHandler() {

        @Override
        public void onHandleReceiveError() {
            DebugUtil.debug(TAG, "MallInfoHandler onHandleReceiveError");
            sendMessage();
        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "MallInfoHandler result " + result);
            setServerData(result);
            deSerialization();
            localize();
            sendMessage();

        }
    };

    private MallTotalInfo getDataFromDatabase(Map<String, CategoryEntryInfo> categoryInfoMap) {

        mMallTotalInfo = new MallTotalInfo();
        List<MallEntryInfo> mallInfoList = mLocalSQLiteOperator.getMallInfo();
        if (mallInfoList != null) {

            CategoryEntryInfo categoryEntryInfo = null;
            ArrayList<CategoryEntryInfo> categoryEntryInfoArrayList = new ArrayList<CategoryEntryInfo>();
            for (MallEntryInfo mallEntryInfo : mallInfoList) {
                categoryEntryInfo = categoryInfoMap.get(mallEntryInfo.mCategoryId);
                categoryEntryInfo.mMallEntryInfoList.add(mallEntryInfo);
            }

            for (Map.Entry<String, CategoryEntryInfo> entry : categoryInfoMap.entrySet()) {
                categoryEntryInfoArrayList.add(entry.getValue());
            }

            Collections.sort(categoryEntryInfoArrayList, new Comparator<CategoryEntryInfo>() {
                @Override
                public int compare(CategoryEntryInfo lhs, CategoryEntryInfo rhs) {
                    return lhs.mServerId.compareTo(rhs.mServerId);
                }
            });

            mMallTotalInfo.mCategoryList = categoryEntryInfoArrayList;
            mMallTotalInfo.mVersion = getMallServerVersion();
            mMallTotalInfo.mRequestTime = getLastRequestTime();
        }

        return mMallTotalInfo;
    }

    private String getMallServerVersion() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(COMMON_USER_INFO_FILE, 0);
        return sharedPreferences.getString(MALL_VERSION, DEFAULT_MALL_VERSION);
    }

    private long getLastRequestTime() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(COMMON_USER_INFO_FILE, 0);
        return sharedPreferences.getLong(KEY_LAST_REQUEST_TIME, 0);
    }


}
