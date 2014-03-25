package cn.lemon.shopping;

import android.os.Bundle;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.framework.BaseActivity;
import cn.lemon.shopping.adapter.CommodityItemAdapter;
import cn.lemon.shopping.model.*;
import cn.lemon.shopping.ui.CommodityView;
import cn.lemon.utils.DebugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CashDeliveryActivity extends PageScrollActivity {

    public static final String TAG = "CashDeliveryActivity";

    private ShoppingMoreDomainDataManager mShoppingMoreDomainDataManager;
    private CommodityItems mCommodityItems;
    private ListView mCommodityListView;
    private BaseAdapter mCommodityAdapter;
    private List<CommodityItem> mCommodityItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.cash_delivery_layout);
        initData();
        initView();
    }

    private void initData() {
        mCommodityItemList = new ArrayList<CommodityItem>();
        mShoppingMoreDomainDataManager = ShoppingMoreDomainDataManager.getInstance();
        BaseRequestEntity<CommodityItems> requestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_COMMODITY);
        RequestEntityDelegator<CommodityItems> requestEntityDelegator = new RequestEntityDelegator<CommodityItems>();
        mCommodityItems = requestEntityDelegator.getRequestEntity(requestEntity);
        if (mCommodityItems != null) {
            mCommodityItemList.addAll(mCommodityItems.mCommodityItemList);
        }
    }

    private void initView() {

        mCommodityListView = (ListView) findViewById(R.id.id_commodity_list_view);
        mCommodityAdapter = getCommodityAdapter();
        mCommodityListView.setAdapter(mCommodityAdapter);
    }

    @Override
    public void onPageScroll() {

    }

    @Override
    public void onPageSelected() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        DebugUtil.debug(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugUtil.debug(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugUtil.debug(TAG, "onDestroy");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DebugUtil.debug(TAG, "onSaveInstanceState");
    }

    private List<CommodityItem> getCommodityData() {

        return mCommodityItemList;
    }

    private ImageFetcher getImageFetcher() {

        return ImageFetcherManager.getInstance().getCommodityIconImageFetcher(this);
    }

    private BaseAdapter getCommodityAdapter() {

        return new CommodityItemAdapter(this, getCommodityData(),
                mCommodityNameClickListener, getImageFetcher());
    }

    @Override
    public void addObserver() {
        mMesssageManager.addObserver(MessageConstants.MSG_COMMODITY_DATA_RETURN, this);

    }

    @Override
    public void deleteObserver() {

        mMesssageManager.deleteObserver(MessageConstants.MSG_COMMODITY_DATA_RETURN, this);
    }

    @Override
    public void update(Observable observable, Object data) {

        Message message = (Message) data;
        int what = message.what;
        switch (what) {
            case MessageConstants.MSG_COMMODITY_DATA_RETURN:
                DebugUtil.debug(TAG, "MSG_COMMODITY_DATA_RETURN");
                mCommodityItems = (CommodityItems) message.obj;
                handleCommodityDataReturn();

                break;
        }

    }

    private void handleCommodityDataReturn() {

        // QiYun<LeiYong><2014-03-20> modify for CR00000012 begin
        if (mCommodityItems.mIsSucceed) {
            mCommodityItemList.clear();
            mCommodityItemList.addAll(mCommodityItems.mCommodityItemList);
            mCommodityAdapter.notifyDataSetChanged();
        } else {
            // TODO
        }
        // QiYun<LeiYong><2014-03-20> modify for CR00000012 end
    }

    private CommodityView.CommodityNameClickListener mCommodityNameClickListener
            = new CommodityView.CommodityNameClickListener() {
        @Override
        public void onClick(String name, String linkUrl) {

            DebugUtil.debug(TAG, "CommodityNameClickListener name " + name + " linkUrl " + linkUrl);
            ActivityUtils.toCommodityCategoryActivity(CashDeliveryActivity.this, name, linkUrl);

        }
    };

}
