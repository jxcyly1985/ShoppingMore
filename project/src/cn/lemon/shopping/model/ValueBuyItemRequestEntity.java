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
 * Date: 14-3-22
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemRequestEntity extends BaseRequestEntity<ValueBuyItemTotalInfo> {

    private static final String TAG = "ValueBuyItemRequestEntity";
    private final long VALUE_BUY_ITEM_REQUEST_TIMER = 6 * 3600 * 1000;


    private Context mContext;
    private ValueBuyItemTotalInfo mValueBuyTotalInfo;


    protected ValueBuyItemRequestEntity(Context context) {
        mContext = context;
    }

    @Override
    protected ValueBuyItemTotalInfo getRequestEntity() {
        ValueBuyItemTotalInfo valueBuyItemTotalInfo = getValueBuyTotalTypes();
        if (valueBuyItemTotalInfo != null) {
            if (isExpired()) {
                sendRequest();
            }
            return valueBuyItemTotalInfo;
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
                request.mUrl = VALUE_BUY_LIST_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, mValueBuyItemHandler);
                httpRequest.get();

            }
        });

    }

    @Override
    protected void localize() {

        File typeFile = getValueBuyItemFile();
        try {
            if (!typeFile.exists()) {
                typeFile.createNewFile();
            }
            JSONObject jsonObject = new JSONObject(mServerData);
            long requestTime = System.currentTimeMillis();
            jsonObject.put(JSON_LAST_MODIFY_KEY, requestTime);
            FileOutputStream fileOutputStream = new FileOutputStream(typeFile);
            fileOutputStream.write(jsonObject.toString().getBytes());
            fileOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected ValueBuyItemTotalInfo deSerialization() {

        try {

            JSONObject jsonObject = new JSONObject(mServerData);
            mValueBuyTotalInfo = new ValueBuyItemTotalInfo();
            if (!jsonObject.isNull(JSON_LAST_MODIFY_KEY)) {
                mValueBuyTotalInfo.mRequestTime = jsonObject.getLong(JSON_LAST_MODIFY_KEY);
            }
            boolean isSucceed = jsonObject.getBoolean(JSON_KEY_SUCCESS);
            mValueBuyTotalInfo.mIsSucceed = isSucceed;
            if (isSucceed) {
                JSONObject data = jsonObject.getJSONObject(JSON_KEY_DATA);
                JSONArray list = data.getJSONArray(JSON_KEY_LIST);
                List<ValueBuyItemInfo> valueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();
                for (int i = 0; i < list.length(); ++i) {
                    ValueBuyItemInfo valueBuyItemInfo = new ValueBuyItemInfo();
                    JSONObject item = list.getJSONObject(i);
                    valueBuyItemInfo.mTitle = item.getString(JSON_KEY_TITLE);
                    valueBuyItemInfo.mImageUrl = item.getString(JSON_KEY_IMG);
                    valueBuyItemInfo.mItemLink = item.getString(JSON_KEY_LINK);
                    valueBuyItemInfo.mPrice = item.getString(JSON_KEY_PRICE);
                    valueBuyItemInfoList.add(valueBuyItemInfo);

                }
                mValueBuyTotalInfo.mValueBuyItemInfoList = valueBuyItemInfoList;
                mValueBuyTotalInfo.mHasNext = data.getBoolean(JSON_KEY_HAS_NEXT_PAGE);
                mValueBuyTotalInfo.mCurrentPage = data.getInt(JSON_KEY_CUR_PAGE);
                //TODO Server should Add Version
                //mValueBuyTotalInfo.mVersion = jsonObject.getString(JSON_KEY_VERSION);

            } else {
                mValueBuyTotalInfo.mMsg = jsonObject.getString(JSON_KEY_MSG);
            }

            return mValueBuyTotalInfo;

        } catch (JSONException e) {
            DebugUtil.debug(TAG, "JSONException");
            //TODO handle receive error json example CMCC Login page
            //QiYun<LeiYong><2014-03-22> modify for CR0000014 begin
            mIsSucceed = false;
            e.printStackTrace();
            //QiYun<LeiYong><2014-03-22> modify for CR0000014 begin
        }

        return null;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_VALUE_BUY_LIST, mValueBuyTotalInfo, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        } else {
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_NET_WORK_ERROR, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }


    }

    @Override
    protected boolean shouldNewRequest() {
        long thisTime = System.currentTimeMillis();
        long pastTime = thisTime - mValueBuyTotalInfo.mRequestTime;
        return pastTime > VALUE_BUY_ITEM_REQUEST_TIMER;
    }

    @Override
    protected boolean isExpired() {
        return false;
    }

    private LemonNetWorkHandler mValueBuyItemHandler = new LemonNetWorkHandler() {
        @Override
        public void onHandleReceiveError() {
            DebugUtil.debug(TAG, "onHandleReceiveError");
            sendMessage();
        }

        @Override
        public void onHandleReceiveSuccess(String result) {
            DebugUtil.debug(TAG, "onHandleReceiveSuccess result " + result);
            handleReceiveSuccess(result);
        }
    };

    private File getValueBuyItemFile() {

        File appDirFile = mContext.getFilesDir();
        String valueBuyItemFilePath = appDirFile.getAbsolutePath() + VALUE_BUY_LIST_FILE;
        return new File(valueBuyItemFilePath);
    }

    private ValueBuyItemTotalInfo getValueBuyTotalTypes() {
        File valueBuyItemFile = getValueBuyItemFile();
        String jsonString = StaticUtils.getFileString(valueBuyItemFile);
        if (jsonString != null) {
            setServerData(jsonString);
            return deSerialization();
        }
        return null;
    }
}
