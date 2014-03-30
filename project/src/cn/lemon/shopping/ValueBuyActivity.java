package cn.lemon.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.os.Bundle;

import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.framework.BaseActivity;
import cn.lemon.shopping.adapter.ValueBuyItemAdapter;
import cn.lemon.shopping.adapter.ValueBuyTypeAdapter;
import cn.lemon.shopping.model.*;
import cn.lemon.shopping.ui.HorizontalListView;
import cn.lemon.utils.DebugUtil;

public class ValueBuyActivity extends PageScrollActivity {

    public static final String TAG = "ValueBuyActivity";

    private ShoppingMoreDomainDataManager mShoppingMoreDomainDataManager;
    private HorizontalListView mHorizontalListView;
    private GridView mGridView;
    private BaseAdapter mTypeAdapter;
    private BaseAdapter mItemAdapter;
    private List<ValueBuyTypeInfo> mValueBuyTypeInfoList = new ArrayList<ValueBuyTypeInfo>();
    private List<ValueBuyItemInfo> mValueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();

    private SparseArray<List<ValueBuyItemInfo>> mTypeItemSparseArray = new SparseArray<List<ValueBuyItemInfo>>();


    private ValueBuyTotalTypes mValueBuyTotalTypes;
    private ValueBuyItemTotalInfo mValueBuyItemTotalInfo;

    private int mCurrentTypeId;
    private int mLastTypeId;
    private BaseRequestEntity mValueBuyItemRequestEntity;
    private RequestEntityDelegator<ValueBuyItemTotalInfo> mItemTotalInfoRequestEntityDelegator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.value_buy_layout);
        initData();
        initView();
    }

    private void initData() {

        mShoppingMoreDomainDataManager = ShoppingMoreDomainDataManager.getInstance();
        RequestEntityDelegator<ValueBuyTotalTypes> typeRequestDelegator = new RequestEntityDelegator<ValueBuyTotalTypes>();
        mItemTotalInfoRequestEntityDelegator = new RequestEntityDelegator<ValueBuyItemTotalInfo>();

        BaseRequestEntity typeRquestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_TYPE);
        mValueBuyItemRequestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_ITEM);

        mValueBuyTotalTypes = typeRequestDelegator.getRequestEntity(typeRquestEntity);
        mValueBuyItemTotalInfo = mItemTotalInfoRequestEntityDelegator.getRequestEntity(mValueBuyItemRequestEntity);

        DebugUtil.debug(TAG, "initData mValueBuyTotalTypes " + mValueBuyTotalTypes);
        DebugUtil.debug(TAG, "initData mValueBuyItemTotalInfo " + mValueBuyItemTotalInfo);

        if (mValueBuyTotalTypes != null && mValueBuyTotalTypes.mValueBuyTypeInfoList != null) {
            mValueBuyTypeInfoList.addAll(mValueBuyTotalTypes.mValueBuyTypeInfoList);
        }
        if (mValueBuyItemTotalInfo != null && mValueBuyItemTotalInfo.mValueBuyItemInfoList != null) {
            mValueBuyItemInfoList.addAll(mValueBuyItemTotalInfo.mValueBuyItemInfoList);
        }
    }

    private void initView() {

        ImageFetcher imageFetcher = ImageFetcherManager.getInstance().getValueBuyImageFetcher(this);
        mHorizontalListView = (HorizontalListView) findViewById(R.id.id_value_buy_type_list_view);
        mGridView = (GridView) findViewById(R.id.id_value_buy_concrete_commodity_grid);
        mTypeAdapter = new ValueBuyTypeAdapter(this, mValueBuyTypeInfoList);
        mItemAdapter = new ValueBuyItemAdapter(this, mValueBuyItemInfoList, imageFetcher);
        mHorizontalListView.setAdapter(mTypeAdapter);
        mGridView.setAdapter(mItemAdapter);

        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.setSelection(position);
                handleValueBuyTypeClick(position);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleValueBuyItemClick(position);
            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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

    @Override
    public void addObserver() {

        mMesssageManager.addObserver(MessageConstants.MSG_VALUE_BUY_TYPE, this);
        mMesssageManager.addObserver(MessageConstants.MSG_VALUE_BUY_LIST, this);

    }

    @Override
    public void deleteObserver() {

        mMesssageManager.deleteObserver(MessageConstants.MSG_VALUE_BUY_TYPE, this);
        mMesssageManager.deleteObserver(MessageConstants.MSG_VALUE_BUY_LIST, this);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DebugUtil.debug(TAG, "" + mHorizontalListView.getWidth() + " " + mHorizontalListView.getHeight()
                + " " + mGridView.getWidth() + " " + mGridView.getHeight());

    }

    @Override
    public void update(Observable observable, Object data) {

        Message message = (Message) data;
        int what = message.what;

        switch (what) {
            case MessageConstants.MSG_VALUE_BUY_TYPE:
                handleValueBuyType(message);
                break;
            case MessageConstants.MSG_VALUE_BUY_LIST:
                handleValueBuyItem(message);
                break;
        }
    }

    private void handleValueBuyType(Message message) {

        mValueBuyTotalTypes = (ValueBuyTotalTypes) message.obj;

        if (mValueBuyTotalTypes.mIsSucceed) {
            mValueBuyTypeInfoList.clear();
            mValueBuyTypeInfoList.addAll(mValueBuyTotalTypes.mValueBuyTypeInfoList);
            mCurrentTypeId = mValueBuyTypeInfoList.get(0).mTypeId;
            mLastTypeId = mCurrentTypeId;
            mTypeAdapter.notifyDataSetChanged();
        } else {
            //TODO handle server error
        }

    }

    private void handleValueBuyItem(Message message) {

        ValueBuyItemTotalInfo valueBuyItemTotalInfo = (ValueBuyItemTotalInfo) message.obj;
        if (valueBuyItemTotalInfo.mIsSucceed) {
            mValueBuyItemInfoList.clear();
            mValueBuyItemInfoList.addAll(valueBuyItemTotalInfo.mValueBuyItemInfoList);
            mItemAdapter.notifyDataSetChanged();
        } else {
            //TODO handle server error
        }
    }

    private void handleValueBuyTypeClick(int position) {

        DebugUtil.debug(TAG, "handleValueBuyTypeClick position " + position);
        mCurrentTypeId = mValueBuyTypeInfoList.get(position).mTypeId;
        Bundle bundle = new Bundle();
        bundle.putString(BaseRequestEntity.PARAMS_VERSION, "");
        bundle.putString(BaseRequestEntity.PARAMS_CID, "");
        bundle.putString(BaseRequestEntity.PARAMS_PAGE, "");
        ValueBuyItemTotalInfo valueBuyItemTotalInfo = mItemTotalInfoRequestEntityDelegator
                .getRequestEntity(mValueBuyItemRequestEntity, bundle);

        List<ValueBuyItemInfo> memValueBuyItemInfoList = null;
        if (mTypeItemSparseArray.indexOfKey(mCurrentTypeId) >= 0) {
            memValueBuyItemInfoList = mTypeItemSparseArray.get(mCurrentTypeId);
        }

        if (valueBuyItemTotalInfo != null) {
            List<ValueBuyItemInfo> valueBuyItemInfoList;
            if (mTypeItemSparseArray.indexOfKey(mCurrentTypeId) < 0) {
                valueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();
            } else {
                valueBuyItemInfoList = mTypeItemSparseArray.get(mCurrentTypeId);
            }
            valueBuyItemInfoList.addAll(valueBuyItemTotalInfo.mValueBuyItemInfoList);

            if (mLastTypeId != mCurrentTypeId) {
                mValueBuyItemInfoList.clear();
                mValueBuyItemInfoList.addAll(valueBuyItemInfoList);
            } else {
                mValueBuyItemInfoList.addAll(valueBuyItemInfoList);
            }

            mItemAdapter.notifyDataSetChanged();
        }

    }

    private void handleValueBuyItemClick(int position) {

        DebugUtil.debug(TAG, "handleValueBuyItemClick position " + position);

    }

}
