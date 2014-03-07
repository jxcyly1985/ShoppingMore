package cn.lemon.shopping;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.framework.BaseActivity;
import cn.lemon.shopping.adapter.CommodityItemAdapter;
import cn.lemon.shopping.model.CommodityItem;
import cn.lemon.shopping.ui.CommodityView;
import cn.lemon.utils.DebugUtil;

import java.util.List;
import java.util.Observable;

public class CashDeliveryActivity extends BaseActivity {

    public static final String TAG = "CashDeliveryActivity";

    private ListView mCommodityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.cash_delivery_layout);
        initView();
    }

    private void initView() {

        mCommodityListView = (ListView) findViewById(R.id.id_commodity_list_view);
        mCommodityListView.setAdapter(getCommodityAdapter());
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

    private List<CommodityItem> getCommodityData() {

        return null;
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

    }

    @Override
    public void deleteObserver() {

    }

    @Override
    public void update(Observable observable, Object data) {

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
