package cn.lemon.shopping.model;

import android.content.Context;

public class ShoppingMoreDomainDataManager {

    private static final String TAG = "ShoppingMoreDomainDataManager";

    private Context mContext;

    public static final int TYPE_AD = 0;
    public static final int TYPE_MALL = 1;
    public static final int TYPE_COMMODITY = 2;
    public static final int TYPE_VALUE_BUY_TYPE = 3;

    private ShoppingMoreDomainDataManager() {

    }

    private static class ShoppingMoreDomainDataManagerHolder {

        static ShoppingMoreDomainDataManager sInstance = new ShoppingMoreDomainDataManager();
    }

    public static ShoppingMoreDomainDataManager getInstance() {
        return ShoppingMoreDomainDataManagerHolder.sInstance;
    }

    public void initialize(Context context) {
        mContext = context;
    }

    public BaseRequestEntity getRequestEntityDelegator(int type) {

        switch (type) {
            case TYPE_AD:
                return new AdRequestEntity(mContext);
            case TYPE_MALL:
                return new MallCategoryRequestEntity(mContext);
            case TYPE_COMMODITY:
                return new CommodityRequestEntity(mContext);
            case TYPE_VALUE_BUY_TYPE:
                return new ValueBuyTypeRequestEntity(mContext);
            default:
                throw new IllegalArgumentException("ShoppingMoreDomainDataManager not support type " + type);
        }


    }

}
