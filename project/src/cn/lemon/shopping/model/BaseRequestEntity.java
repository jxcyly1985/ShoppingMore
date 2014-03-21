package cn.lemon.shopping.model;

import cn.lemon.network.LemonNetWorkHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 上午10:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseRequestEntity<T> {


    public static final String TAG = "ModelUtils";

    protected static final String BASE_URL = "http://gouclient.9955.com.cn/api";
    protected static final String UPGRADE_URL = BASE_URL + "";
    protected static final String MALL_DATA_URL = BASE_URL + "/client_index/channel";
    protected static final String AD_DATA_URL = BASE_URL + "/client_index/ad";
    protected static final String COMMODITY_DATA_URL = BASE_URL + "/client_cod/index";
    protected static final String VALUE_BUY_TYPE_URL = BASE_URL + "/client_zdm/type";
    protected static final String VALUE_BUY_LIST_URL = BASE_URL + "/client_zdm/list";

    protected static final String AD_FILE = "/Ad";
    protected static final String COMMODITY_FILE = "/Commodity";
    protected static final String VALUE_BUY_TYPE_FILE = "/ValueBuyType";

    protected static final String COMMON_USER_INFO_FILE = "common_user_info";

    protected static final String JSON_LAST_MODIFY_KEY = "last_modify";
    protected static final String JSON_KEY_DATA = "data";
    protected static final String JSON_KEY_SUCCESS = "success";
    protected static final String JSON_KEY_MSG = "msg";
    protected static final String JSON_KEY_VERSION = "version";
    protected static final String JSON_KEY_LIST = "list";
    protected static final String JSON_KEY_ITEM = "items";
    protected static final String JSON_KEY_ID = "id";
    protected static final String JSON_KEY_TITLE = "title";
    protected static final String JSON_KEY_NAME = "name";
    protected static final String JSON_KEY_ICON = "icon";
    protected static final String JSON_KEY_BG_COLOR = "bg_color";
    protected static final String JSON_KEY_IMG = "img";
    protected static final String JSON_KEY_LINK = "link";
    protected static final String JSON_KEY_HAS_NEXT_PAGE = "hasnext";
    protected static final String JSON_KEY_CUR_PAGE = "curpage";
    protected static final String JSON_KEY_TYPE_DIR = "type_dir";
    protected static final String JSON_KEY_TYPE_NAME = "type_name";
    protected static final String JSON_KEY_COLOR = "color";
    protected static final String JSON_KEY_TAGS = "tags";

    protected static ThreadPoolExecutor sRequestExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    protected boolean mIsSucceed = false;
    protected String mServerData;


    protected BaseRequestEntity() {

    }

    protected void setServerData(String result) {
        mIsSucceed = true;
        mServerData = result;
    }

    protected void handleReceiveSuccess(String result) {

        setServerData(result);
        deSerialization();
        localize();
        sendMessage();

    }

    public abstract T getRequestEntity();

    protected abstract void sendRequest();

    protected abstract void localize();

    protected abstract T deSerialization();

    protected abstract void sendMessage();

    protected abstract boolean shouldNewRequest();

    protected abstract boolean isExpired();

}
