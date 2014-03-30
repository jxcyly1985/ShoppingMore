package cn.lemon.shopping.model;

import android.content.Context;
import android.os.Message;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.network.LemonNetWorkRequest;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.shopping.db.MallSQLOperator;
import cn.lemon.utils.DebugUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class MallRequestEntity extends BaseRequestEntity<MallTotalInfo> {

    private static final String TAG = "MallRequestEntity";

    private final int MALL_REQUEST_TIMER = 7 * 24 * 3600 * 1000;
    private final String DEFAULT_MALL_VERSION = "0";

    private Context mContext;
    private MallSQLOperator mMallSQLOperator;
    private MallTotalInfo mMallTotalInfo;

    protected MallRequestEntity(Context context) {
        mContext = context;
        mMallSQLOperator = new MallSQLOperator(mContext);
    }

    @Override
    protected MallTotalInfo getRequestEntity() {

        mMallTotalInfo = mMallSQLOperator.query();
        if (mMallTotalInfo != null) {
            if (shouldNewRequest()) {
                sendRequest();
            }
            return mMallTotalInfo;
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
                request.mUrl = getRequestUrl();

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, mMallInfoHandler);
                httpRequest.get();
            }
        });
    }

    @Override
    protected void localize() {

        // SQL
        if (isExpired()) {
            mMallSQLOperator.delete(null, null);
        }
        mMallSQLOperator.insert(mMallTotalInfo);
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

                mMallTotalInfo.mHasNext = data.getBoolean(JSON_KEY_HAS_NEXT_PAGE);
                mMallTotalInfo.mCurrentPage = data.getInt(JSON_KEY_CUR_PAGE);

                mMallTotalInfo.mCategoryList = categoryList;
                mMallTotalInfo.mVersion = root.getString(JSON_KEY_VERSION);

            } else {

                mMallTotalInfo.mMsg = root.getString(JSON_KEY_MSG);
            }

        } catch (JSONException e) {
            mIsSucceed = false;
            e.printStackTrace();
        }

        return mMallTotalInfo;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_MALL_DATA_RETURN, mMallTotalInfo, 0, 0);
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
            handleReceiveSuccess(result);

        }
    };

    private String getRequestUrl() {

        StringBuffer stringBuffer = new StringBuffer();

        if (mMallTotalInfo == null) {
            stringBuffer.append(URL_PARAMS_VERSION)
                    .append(DEFAULT_MALL_VERSION)
                    .append("?page=1");
        }

        return MALL_DATA_URL + stringBuffer.toString();


    }

}
