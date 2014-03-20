package cn.lemon.shopping.model;

import android.content.Context;
import android.os.Message;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.network.LemonNetWorkRequest;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.utils.DebugUtil;
import cn.lemon.utils.StaticUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 上午10:54
 * To change this template use File | Settings | File Templates.
 */
public class AdRequestEntity extends BaseRequestEntity<AdInfo> {

    private static final String TAG = "AdRequestEntity";
    public static final long AD_REQUEST_TIMER = 24 * 3600 * 1000;

    private Context mContext;
    private AdInfo mAdInfo;
    private long mLastRequestTime;

    public AdRequestEntity(Context context) {
        mContext = context;
    }

    @Override
    public AdInfo getRequestEntity() {

        AdInfo adInfo = readAdInfo();

        DebugUtil.debug(TAG, "getAdInfo adInfo " + adInfo);

        if (adInfo != null) {

            if (!shouldNewRequest()) {
                return adInfo;
            }
            sendRequest();
            return adInfo;
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
                request.mUrl = AD_DATA_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, mAdInfoHandler);
                httpRequest.get();
            }
        });


    }

    @Override
    protected void localize() {

        File adFile = StaticUtils.getInstance().getAdFile();
        try {
            try {
                if (!adFile.exists()) {
                    adFile.createNewFile();
                }
                JSONObject jsonObject = new JSONObject(mServerData);
                long lastModify = System.currentTimeMillis();
                jsonObject.put(JSON_LAST_MODIFY_KEY, lastModify);
                FileOutputStream fileOutputStream = new FileOutputStream(adFile);
                fileOutputStream.write(jsonObject.toString().getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected AdInfo deSerialization() {

        try {
            JSONObject root = new JSONObject(mServerData);
            mAdInfo = new AdInfo();
            if (!root.isNull(JSON_LAST_MODIFY_KEY)) {
                mAdInfo.mRequestTime = root.getLong(JSON_LAST_MODIFY_KEY);
            }
            boolean isSuccess = root.getBoolean(JSON_KEY_SUCCESS);
            mAdInfo.mIsSuccess = isSuccess;
            if (isSuccess) {
                List<AdInfo.AdData> datas = new ArrayList<AdInfo.AdData>();
                JSONObject data = root.getJSONObject(JSON_KEY_DATA);
                JSONArray list = data.getJSONArray(JSON_KEY_LIST);
                AdInfo.AdData adData;
                for (int i = 0; i < list.length(); ++i) {
                    adData = new AdInfo.AdData();
                    JSONObject info = (JSONObject) list.get(i);
                    adData.mTitle = info.getString(JSON_KEY_TITLE);
                    adData.mImageURL = info.getString(JSON_KEY_IMG);
                    adData.mLinkURL = info.getString(JSON_KEY_LINK);
                    datas.add(adData);
                }
                String version = data.getString(JSON_KEY_VERSION);

                mAdInfo.mDatas = datas;
                mAdInfo.mVersion = version;

            } else {
                mAdInfo.mMessage = root.getString(JSON_KEY_MSG);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        DebugUtil.debug(TAG, "jsonToAdInfoObject " + mAdInfo);

        return mAdInfo;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {

            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_AD_DATA_RETURN, mAdInfo, 0,
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
        long pastTime = thisTime - mAdInfo.mRequestTime;
        return pastTime > AD_REQUEST_TIMER;
    }

    @Override
    protected boolean isExpired() {
        return false;
    }

    private LemonNetWorkHandler mAdInfoHandler = new LemonNetWorkHandler() {

        @Override
        public void onHandleReceiveError() {
            DebugUtil.debug(TAG, "AdInfoHandler onHandleReceiveError");
            sendMessage();
        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "AdInfoHandler onHandleReceiveSuccess result " + result);
            setServerData(result);
            deSerialization();
            localize();
        }

    };

    private AdInfo readAdInfo() {

        File adFile = StaticUtils.getInstance().getAdFile();
        String jsonString = StaticUtils.getFileString(adFile);
        if (jsonString != null) {
            setServerData(jsonString);
            return deSerialization();
        }

        return null;

    }

}
