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
 * Date: 14-3-21
 * Time: 下午8:21
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyTypeRequestEntity extends BaseRequestEntity<ValueBuyTotalTypes> {

    private static final String TAG = "ValueBuyTypeRequestEntity";
    private final long TYPE_REQUEST_TIMER = 7 * 24 * 3600 * 1000;

    private Context mContext;
    private ValueBuyTotalTypes mValueBuyTotalTypes;

    protected ValueBuyTypeRequestEntity(Context context) {
        mContext = context;
    }

    @Override
    public ValueBuyTotalTypes getRequestEntity() {

        ValueBuyTotalTypes valueBuyTotalTypes = getValueBuyTotalTypes();
        if (valueBuyTotalTypes != null) {
            if (shouldNewRequest()) {
                sendRequest();
            }
            return valueBuyTotalTypes;
        }

        sendRequest();
        return null;

    }

    @Override
    protected void sendRequest() {

        sRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {

                LemonNetWorkRequest lemonNetWorkRequest = new LemonNetWorkRequest();
                lemonNetWorkRequest.mUrl = VALUE_BUY_TYPE_URL;

                LemonHttpRequest request = new LemonHttpRequest(lemonNetWorkRequest, mTypeHandler);
                request.get();

            }
        });

    }

    @Override
    protected void localize() {

        File typeFile = getValueBuyTypeFile();
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
    protected ValueBuyTotalTypes deSerialization() {

        try {
            JSONObject jsonObject = new JSONObject(mServerData);
            mValueBuyTotalTypes = new ValueBuyTotalTypes();
            if (!jsonObject.isNull(JSON_LAST_MODIFY_KEY)) {
                mValueBuyTotalTypes.mRequestTime = jsonObject.getLong(JSON_LAST_MODIFY_KEY);
            }
            boolean isSucceed = jsonObject.getBoolean(JSON_KEY_SUCCESS);
            mValueBuyTotalTypes.mIsSucceed = jsonObject.getBoolean(JSON_KEY_SUCCESS);
            if (isSucceed) {
                List<ValueBuyTypeInfo> valueBuyTypeInfoList = new ArrayList<ValueBuyTypeInfo>();
                JSONArray data = jsonObject.getJSONArray(JSON_KEY_DATA);

                for (int i = 0; i < data.length(); ++i) {
                    JSONObject item = data.getJSONObject(i);
                    ValueBuyTypeInfo valueBuyTypeInfo = new ValueBuyTypeInfo();
                    valueBuyTypeInfo.mTypeId = item.getString(JSON_KEY_ID);
                    valueBuyTypeInfo.mTypeName = item.getString(JSON_KEY_NAME);
                    valueBuyTypeInfoList.add(valueBuyTypeInfo);
                }

                mValueBuyTotalTypes.mVersion = jsonObject.getString(JSON_KEY_VERSION);
                mValueBuyTotalTypes.mValueBuyTypeInfoList = valueBuyTypeInfoList;

            } else {
                mValueBuyTotalTypes.mMsg = jsonObject.getString(JSON_KEY_MSG);
            }

            return mValueBuyTotalTypes;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_VALUE_BUY_TYPE, mValueBuyTotalTypes, 0,
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
        long pastTime = thisTime - mValueBuyTotalTypes.mRequestTime;
        return pastTime > TYPE_REQUEST_TIMER;
    }

    @Override
    protected boolean isExpired() {
        return false;
    }


    private LemonNetWorkHandler mTypeHandler = new LemonNetWorkHandler() {
        @Override
        public void onHandleReceiveError() {
            sendMessage();
        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "onHandleReceiveSuccess result " + result);
            handleReceiveSuccess(result);

        }
    };

    private File getValueBuyTypeFile() {
        File appFileDir = mContext.getFilesDir();
        String typeFilePath = appFileDir.getAbsolutePath() + VALUE_BUY_TYPE_FILE;
        return new File(typeFilePath);

    }

    private ValueBuyTotalTypes getValueBuyTotalTypes() {
        File typeFile = getValueBuyTypeFile();
        String jsonString = StaticUtils.getFileString(typeFile);
        if (jsonString != null) {
            setServerData(jsonString);
            return deSerialization();
        }
        return null;

    }
}
