package cn.lemon.shopping.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class CommodityRequestEntity extends BaseRequestEntity<CommodityItems> {

    private static final String TAG = "CommodityRequestEntity";
    private Context mContext;

    public CommodityRequestEntity(Context context) {
        mContext = context;
    }

    @Override
    public CommodityItems getRequestEntity() {
        return null;
    }

    @Override
    protected void sendRequest() {

    }

    @Override
    protected void localize() {

    }

    @Override
    protected CommodityItems deSerialization() {
        return null;
    }

    @Override
    protected void sendMessage() {

    }
}
