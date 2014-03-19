package cn.lemon.shopping.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 下午1:08
 * To change this template use File | Settings | File Templates.
 */
public class RequestEntityDelegator<T> {

    public static final int REQUEST_TYPE_MALL = 0;
    public static final int REQUEST_TYPE_AD = 1;
    public static final int REQUEST_TYPE_COMMODITY = 2;

    public T getRequestEntity(Context context, int type) {

        BaseRequestEntity<?> baseRequestEntity;
        switch (type) {

            case REQUEST_TYPE_MALL:
                baseRequestEntity = new MallCategoryRequestEntity(context);
                break;
            case REQUEST_TYPE_AD:
                baseRequestEntity = new AdRequestEntity(context);
                break;
            case REQUEST_TYPE_COMMODITY:
                baseRequestEntity = new CommodityRequestEntity(context);
                break;

            default:
                throw new IllegalArgumentException("not support type " + type);
        }

        return (T) baseRequestEntity.getRequestEntity();

    }
}
