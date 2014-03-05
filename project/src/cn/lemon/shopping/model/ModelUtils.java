package cn.lemon.shopping.model;

import android.content.Context;
import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.network.LemonNetWorkRequest;
import cn.lemon.utils.DebugUtil;
import cn.lemon.utils.StaticUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ModelUtils {

    private static final String TAG = "ModelUtils";
    public static final String BASE_URL = "http://gouclient.9955.com.cn/api";
    public static final String UPGRADE_URL = BASE_URL + "";
    public static final String MALL_DATA_URL = BASE_URL + "/client_index/channel";
    public static final String AD_DATA_URL = BASE_URL + "/client_index/ad";
    public static final String AdFile = "/Ad";
    public static final String JSON_LAST_MODIFY_KEY = "last_modify";

    private static final String JSON_KEY_DATA = "data";
    private static final String JSON_KEY_SUCCESS = "success";
    private static final String JSON_KEY_MSG = "msg";
    private static final String JSON_KEY_VERSION = "version";
    private static final String JSON_KEY_LIST = "list";
    private static final String JSON_KEY_ITEM = "items";
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_TITLE = "title";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_ICON = "icon";
    private static final String JSON_KEY_BG_COLOR = "bg_color";
    private static final String JSON_KEY_IMG = "img";
    private static final String JSON_KEY_LINK = "link";
    private static final String JSON_KEY_HAS_NEXT_PAGE = "hasnext";
    private static final String JSON_KEY_CUR_PAGE = "curpage";

    private static ThreadPoolExecutor sRequestExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);


    public static void sendMallRequest(final LemonNetWorkHandler handler) {

        sRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {

                LemonNetWorkRequest request = new LemonNetWorkRequest();
                request.mUrl = MALL_DATA_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, handler);
                httpRequest.get();
            }
        });


    }

    public static void sendAdRequest(final LemonNetWorkHandler handler){

        sRequestExecutor.submit(new Runnable() {
            @Override
            public void run() {
                LemonNetWorkRequest request = new LemonNetWorkRequest();
                request.mUrl = AD_DATA_URL;

                LemonHttpRequest httpRequest = new LemonHttpRequest(request, handler);
                httpRequest.get();
            }
        });

    }

    public static void localizeAdInfo(String jsonString){
        File adFile = StaticUtils.getInstance().getAdFile();
        try {
            try {
                if (!adFile.exists()) {
                    adFile.createNewFile();
                }
                JSONObject jsonObject = new JSONObject(jsonString);
                long lastModify = System.currentTimeMillis();
                jsonObject.put(JSON_LAST_MODIFY_KEY, lastModify);
                FileOutputStream fileOutputStream = new FileOutputStream(adFile);
                fileOutputStream.write(jsonString.getBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static AdInfo readAdInfo(){

        File adFile = StaticUtils.getInstance().getAdFile();
        AdInfo adInfo = null;

        try {
            if (adFile.exists()) {
                FileReader fileReader = new FileReader(adFile);
                int i = 0;
                char[] buffer = new char[1024];
                StringBuffer stringBuffer = new StringBuffer();
                while((i = fileReader.read(buffer)) != -1){
                    stringBuffer.append(buffer, 0, i);
                }

                try {
                    String jsonString = stringBuffer.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    adInfo = jsonToAdInfoObject(jsonString);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  adInfo;

    }

    public static MallTotalInfo jsonToMallTotalInfoObject(String json) {

        MallTotalInfo mallTotalInfo = null;

        try {
            JSONObject root = new JSONObject(json);
            boolean isSucceed = root.getBoolean(JSON_KEY_SUCCESS);
            mallTotalInfo = new MallTotalInfo();
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
                    for(int j=0; j < mallItemsArray.length(); ++j){

                        mallEntryInfo = new MallEntryInfo();
                        mallJsonObject = (JSONObject)mallItemsArray.opt(j);
                        mallEntryInfo.mCategoryId = categoryEntryInfo.mServerId;
                        mallEntryInfo.mName = mallJsonObject.getString(JSON_KEY_NAME);
                        mallEntryInfo.mImageUrl = mallJsonObject.getString(JSON_KEY_IMG);
                        mallEntryInfo.mLinkedUrl = mallJsonObject.getString(JSON_KEY_LINK);
                        mallList.add(mallEntryInfo);
                    }

                    categoryEntryInfo.mMallEntryInfoList = mallList;
                    categoryList.add(categoryEntryInfo);
                }

                mallTotalInfo.mCategoryList = categoryList;
                mallTotalInfo.mVersion = root.getString(JSON_KEY_VERSION);

            } else {

                mallTotalInfo.mMsg = root.getString(JSON_KEY_MSG);
            }

            

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mallTotalInfo;
    }


    public static AdInfo jsonToAdInfoObject(String json){
        AdInfo adInfo = null;

        try {
            JSONObject root = new JSONObject(json);
            adInfo = new AdInfo();
            if(!root.isNull(JSON_LAST_MODIFY_KEY)) {
                adInfo.mRequestTime = root.getLong(JSON_LAST_MODIFY_KEY);
            }
            boolean isSuccess = root.getBoolean(JSON_KEY_SUCCESS);
            adInfo.mIsSuccess = isSuccess;
            if(isSuccess) {
                List<AdInfo.AdData> adDatas = new ArrayList<AdInfo.AdData>();
                JSONObject data = root.getJSONObject(JSON_KEY_DATA);
                JSONArray list = data.getJSONArray(JSON_KEY_LIST);
                AdInfo.AdData adData;
                for(int i=0; i < list.length(); ++i){
                    adData = new AdInfo.AdData();
                    JSONObject info = (JSONObject)list.get(i);
                    adData.mTitle = info.getString(JSON_KEY_TITLE);
                    adData.mImageURL = info.getString(JSON_KEY_IMG);
                    adData.mLinkURL = info.getString(JSON_KEY_LINK);
                    adDatas.add(adData);
                }
                String version = data.getString(JSON_KEY_VERSION);

                adInfo.mDatas = adDatas;
                adInfo.mVersion = version;

            }else {
                adInfo.mMessage = root.getString(JSON_KEY_MSG);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        DebugUtil.debug(TAG, "jsonToAdInfoObject " + adInfo);

        return adInfo;
    }
}
