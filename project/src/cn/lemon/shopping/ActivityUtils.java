package cn.lemon.shopping;

import android.content.Context;
import cn.lemon.utils.DebugUtil;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-12
 * Time: 下午10:16
 * To change this template use File | Settings | File Templates.
 */
public class ActivityUtils {

    private static final String TAG = "ActivityUtils";


    public static void toAdHostActivity(Context context, String url) {

        DebugUtil.debug(TAG, "toAdHostActivity url " + url);

    }

    public static void toMallHostActivity(Context context, String url) {

        DebugUtil.debug(TAG, "toMallHostActivity url " + url);
    }

    public static void toCommodityCategoryActivity(Context context, String name, String uri) {

        DebugUtil.debug(TAG, "toCommodityCategoryActivity");

    }


}
