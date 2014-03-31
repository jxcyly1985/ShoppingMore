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
    private List<ValueBuyItemInfo> mAdapterValueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();

    private SparseArray<List<ValueBuyItemInfo>> mItemListSparseArray = new SparseArray<List<ValueBuyItemInfo>>();


    private ValueBuyTotalTypes mValueBuyTotalTypes;
    private ValueBuyItemTotalInfo mValueBuyItemTotalInfo;
    private RequestEntityDelegator<ValueBuyItemTotalInfo> mItemTotalInfoRequestEntityDelegator;
    private int mCurrentTypeId;


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
        BaseRequestEntity valueBuyItemRequestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_ITEM);

        mValueBuyTotalTypes = typeRequestDelegator.getRequestEntity(typeRquestEntity);
        mValueBuyItemTotalInfo = mItemTotalInfoRequestEntityDelegator.getRequestEntity(valueBuyItemRequestEntity);

        DebugUtil.debug(TAG, "initData mValueBuyTotalTypes " + mValueBuyTotalTypes);
        DebugUtil.debug(TAG, "initData mValueBuyItemTotalInfo " + mValueBuyItemTotalInfo);

        if (mValueBuyTotalTypes != null && mValueBuyTotalTypes.mValueBuyTypeInfoList != null) {
            mValueBuyTypeInfoList.addAll(mValueBuyTotalTypes.mValueBuyTypeInfoList);
        }
        if (mValueBuyItemTotalInfo != null && mValueBuyItemTotalInfo.mValueBuyItemInfoList != null) {
            mAdapterValueBuyItemInfoList.addAll(mValueBuyItemTotalInfo.mValueBuyItemInfoList);
        }
    }

    private void initView() {

        ImageFetcher imageFetcher = ImageFetcherManager.getInstance().getValueBuyImageFetcher(this);
        mHorizontalListView = (HorizontalListView) findViewById(R.id.id_value_buy_type_list_view);
        mGridView = (GridView) findViewById(R.id.id_value_buy_concrete_commodity_grid);
        mTypeAdapter = new ValueBuyTypeAdapter(this, mValueBuyTypeInfoList);
        mItemAdapter = new ValueBuyItemAdapter(this, mAdapterValueBuyItemInfoList, imageFetcher);
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
                handleValueBuyTypeMsg(message);
                break;
            case MessageConstants.MSG_VALUE_BUY_LIST:
                handleValueBuyItemMsg(message);
                break;
        }
    }

    private List<ValueBuyItemInfo> getCacheItemInfoList(int typeId) {

        if (mItemListSparseArray.indexOfKey(typeId) < 0) {
            return null;
        } else {
            return mItemListSparseArray.get(mCurrentTypeId);
        }
    }

    private void putCacheItemInfoList(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {

        int typeId = valueBuyItemTotalInfo.mTypeId;
        List<ValueBuyItemInfo> valueBuyItemInfoList = null;
        if (mItemListSparseArray.indexOfKey(typeId) < 0) {
            valueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();
            mItemListSparseArray.put(valueBuyItemTotalInfo.mTypeId, valueBuyItemInfoList);
        } else {
            valueBuyItemInfoList = mItemListSparseArray.get(typeId);
            valueBuyItemInfoList.addAll(valueBuyItemTotalInfo.mValueBuyItemInfoList);
        }
    }

    private void notifyAdapterDataChange(List<ValueBuyItemInfo> valueBuyItemInfoList) {

        mAdapterValueBuyItemInfoList.clear();
        mAdapterValueBuyItemInfoList.addAll(valueBuyItemInfoList);
        mItemAdapter.notifyDataSetChanged();

    }

    private void handleValueBuyTypeMsg(Message message) {

        mValueBuyTotalTypes = (ValueBuyTotalTypes) message.obj;

        if (mValueBuyTotalTypes.mIsSucceed) {
            mValueBuyTypeInfoList.clear();
            mValueBuyTypeInfoList.addAll(mValueBuyTotalTypes.mValueBuyTypeInfoList);
            mCurrentTypeId = mValueBuyTypeInfoList.get(0).mTypeId;
            mTypeAdapter.notifyDataSetChanged();
        } else {
            //TODO handle server error
        }

    }

    private void handleValueBuyItemMsg(Message message) {

        ValueBuyItemTotalInfo valueBuyItemTotalInfo = (ValueBuyItemTotalInfo) message.obj;

        if (valueBuyItemTotalInfo.mIsSucceed) {

            putCacheItemInfoList(valueBuyItemTotalInfo);

            if (shouldNotifyAdapterDataChange(valueBuyItemTotalInfo)) {

                List<ValueBuyItemInfo> valueBuyItemInfoList = getCacheItemInfoList(mCurrentTypeId);
                notifyAdapterDataChange(valueBuyItemInfoList);
            }

        } else {
            //TODO handle server error
        }
    }

    private boolean shouldNotifyAdapterDataChange(ValueBuyItemTotalInfo valueBuyItemTotalInfo) {

        return valueBuyItemTotalInfo.mTypeId == mCurrentTypeId;
    }

    private void handleValueBuyTypeClick(int position) {

        DebugUtil.debug(TAG, "handleValueBuyTypeClick position " + position);

        ValueBuyTypeInfo valueBuyTypeInfo = mValueBuyTypeInfoList.get(position);
        if (valueBuyTypeInfo.mTypeId != mCurrentTypeId) {

            mCurrentTypeId = valueBuyTypeInfo.mTypeId;
            List<ValueBuyItemInfo> valueBuyItemInfoList = getCacheItemInfoList(mCurrentTypeId);
            if (valueBuyItemInfoList != null) {
                notifyAdapterDataChange(valueBuyItemInfoList);
                return;
            }

            // QiYun<LeiYong><2014-03-31> modify for CR00000020 begin
            BaseRequestEntity valueBuyItemRequestEntity = mShoppingMoreDomainDataManager
                    .getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_ITEM);
            // QiYun<LeiYong><2014-03-31> modify for CR00000020 end

            Bundle bundle = new Bundle();
            bundle.putInt(BaseRequestEntity.PARAMS_CID, mCurrentTypeId);
            ValueBuyItemTotalInfo valueBuyItemTotalInfo = mItemTotalInfoRequestEntityDelegator
                    .getRequestEntity(valueBuyItemRequestEntity, bundle);

            if (valueBuyItemTotalInfo != null) {

                putCacheItemInfoList(valueBuyItemTotalInfo);
                notifyAdapterDataChange(valueBuyItemTotalInfo.mValueBuyItemInfoList);
            }
        }
    }

    private void handleValueBuyItemClick(int position) {

        DebugUtil.debug(TAG, "handleValueBuyItemClick position " + position);

    }

}
