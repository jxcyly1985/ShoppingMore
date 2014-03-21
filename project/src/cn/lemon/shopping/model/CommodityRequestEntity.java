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
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class CommodityRequestEntity extends BaseRequestEntity<CommodityItems> {

    private static final String TAG = "CommodityRequestEntity";
    private final int COMMODITY_REQUEST_TIMER = 12 * 3600 * 1000;
    private Context mContext;
    private CommodityItems mCommodityItems;

    protected CommodityRequestEntity(Context context) {
        mContext = context;
    }

    @Override
    public CommodityItems getRequestEntity() {
        CommodityItems commodityItems = readCommodityInfo();
        DebugUtil.debug(TAG, "getCommodityItems commodityItems " + commodityItems);

        if (commodityItems != null) {
            DebugUtil.debug(TAG, "getCommodityItems size " + commodityItems.mCommodityItemList.size());
            return commodityItems;
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
                request.mUrl = COMMODITY_DATA_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, mCommodityHandler);
                httpRequest.get();

            }
        });


    }

    @Override
    protected void localize() {

        File commodityFile = getCommodityFile();
        try {

            try {
                if (!commodityFile.exists()) {
                    commodityFile.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(commodityFile);
                JSONObject jsonObject = new JSONObject(mServerData);
                long lastModify = System.currentTimeMillis();
                jsonObject.put(JSON_LAST_MODIFY_KEY, lastModify);
                fileOutputStream.write(jsonObject.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected CommodityItems deSerialization() {

        try {
            JSONObject jsonObject = new JSONObject(mServerData);
            boolean isSucceed = jsonObject.getBoolean(JSON_KEY_SUCCESS);
            mCommodityItems = new CommodityItems();
            if (!jsonObject.isNull(JSON_LAST_MODIFY_KEY)) {
                mCommodityItems.mRequestTime = jsonObject.getLong(JSON_LAST_MODIFY_KEY);
            }
            mCommodityItems.mIsSucceed = isSucceed;
            if (isSucceed) {
                List<CommodityItem> commodityItemList = new ArrayList<CommodityItem>();
                JSONObject data = (JSONObject) jsonObject.get(JSON_KEY_DATA);
                JSONArray list = data.getJSONArray(JSON_KEY_LIST);
                for (int i = 0; i < list.length(); ++i) {
                    CommodityItem commodityItem = new CommodityItem();
                    JSONObject item = (JSONObject) list.get(i);
                    commodityItem.mHasTopSide = (i == 0 ? false : true);
                    commodityItem.mImagePos = item.getInt(JSON_KEY_TYPE_DIR);
                    commodityItem.mCommodityLink = item.getString(JSON_KEY_LINK);
                    commodityItem.mCommodityIconUrl = item.getString(JSON_KEY_IMG);
                    commodityItem.mCommodityName = item.getString(JSON_KEY_TYPE_NAME);
                    commodityItem.mCommodityNameColor = item.getString(JSON_KEY_COLOR);
                    JSONArray tagArray = item.getJSONArray(JSON_KEY_TAGS);
                    List<CommodityCategory> commodityCategoryList = new ArrayList<CommodityCategory>();
                    for (int j = 0; j < tagArray.length(); ++j) {
                        CommodityCategory category = new CommodityCategory();
                        JSONObject tag = (JSONObject) tagArray.get(j);
                        category.mCommodityCategoryLink = tag.getString(JSON_KEY_LINK);
                        category.mCommodityCategoryName = tag.getString(JSON_KEY_NAME);
                        category.mCommodityColor = (String) tag.opt(JSON_KEY_COLOR);
                        commodityCategoryList.add(category);
                    }
                    commodityItem.mCommodityCategoryList = commodityCategoryList;
                    commodityItemList.add(commodityItem);
                }
                mCommodityItems.mHasNext = data.getBoolean(JSON_KEY_HAS_NEXT_PAGE);
                mCommodityItems.mPageIndex = data.getInt(JSON_KEY_CUR_PAGE);
                mCommodityItems.mVersionCode = data.getString(JSON_KEY_VERSION);
                mCommodityItems.mCommodityItemList = commodityItemList;
                return mCommodityItems;

            } else {
                mCommodityItems.mMsg = jsonObject.getString(JSON_KEY_MSG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void sendMessage() {

        if (mIsSucceed) {
            Message msg = FramewokUtils.makeMessage(
                    MessageConstants.MSG_COMMODITY_DATA_RETURN, mCommodityItems, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        } else {
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_NET_WORK_ERROR, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }

    }

    @Override
    protected boolean shouldNewRequest() {

        long thisTime = System.currentTimeMillis();
        long pastTime = thisTime - mCommodityItems.mRequestTime;
        return pastTime > COMMODITY_REQUEST_TIMER;
    }

    @Override
    protected boolean isExpired() {
        return false;
    }

    private LemonNetWorkHandler mCommodityHandler = new LemonNetWorkHandler() {
        @Override
        public void onHandleReceiveError() {
            sendMessage();
        }

        @Override
        public void onHandleReceiveSuccess(String result) {

            DebugUtil.debug(TAG, "CommodityHandler onHandleReceiveSuccess result " + result);
            handleReceiveSuccess(result);
        }
    };

    private File getCommodityFile() {

        File appFileDir = mContext.getFilesDir();
        String commodityFilePath = appFileDir.getAbsolutePath() + COMMODITY_FILE;
        return new File(commodityFilePath);

    }

    private CommodityItems readCommodityInfo() {

        File commodityFile = getCommodityFile();
        if (commodityFile.exists()) {
            String jsonString = StaticUtils.getFileString(commodityFile);

            DebugUtil.debug(TAG, "readCommodityInfo jsonString " + jsonString);

            if (jsonString != null) {
                setServerData(jsonString);
                return deSerialization();
            }
        }

        return null;
    }


}
