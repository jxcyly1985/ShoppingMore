package cn.lemon.shopping.model;

import java.util.List;

import cn.lemon.network.LemonHttpRequest;
import cn.lemon.network.LemonNetWorkHandler;
import cn.lemon.network.LemonNetWorkRequest;
import cn.lemon.utils.StaticUtils;

public class ModelUtils {

    public static void sendMallRequest(LemonNetWorkHandler handler) {

        LemonNetWorkRequest request = new LemonNetWorkRequest();
        request.mUrl = StaticUtils.MALL_DATA_URL;

        LemonHttpRequest httpRequest = new LemonHttpRequest(request, handler);
        httpRequest.post();

    }

    public static MallTotalInfo jsonToObject(String json) {

        MallTotalInfo mallTotalInfo = null;

        return mallTotalInfo;
    }

}
