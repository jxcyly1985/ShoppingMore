package cn.lemon.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.os.Bundle;

import android.os.Message;
import android.view.View;
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

public class ValueBuyActivity extends BaseActivity {

    public static final String TAG = "ValueBuyActivity";

    private ShoppingMoreDomainDataManager mShoppingMoreDomainDataManager;
    private HorizontalListView mHorizontalListView;
    private GridView mGridView;
    private BaseAdapter mTypeAdapter;
    private BaseAdapter mItemAdapter;
    private List<ValueBuyTypeInfo> mValueBuyTypeInfoList = new ArrayList<ValueBuyTypeInfo>();
    private List<ValueBuyItemInfo> mValueBuyItemInfoList = new ArrayList<ValueBuyItemInfo>();

    private ValueBuyTotalTypes mValueBuyTotalTypes;
    private ValueBuyItemTotalInfo mValueBuyItemTotalInfo;

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
        RequestEntityDelegator<ValueBuyItemTotalInfo> itemRequestDelegator = new RequestEntityDelegator<ValueBuyItemTotalInfo>();

        BaseRequestEntity typeRquestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_TYPE);
        BaseRequestEntity itemRequestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_VALUE_BUY_ITEM);

        mValueBuyTotalTypes = typeRequestDelegator.getRequestEntity(typeRquestEntity);
        mValueBuyItemTotalInfo = itemRequestDelegator.getRequestEntity(itemRequestEntity);

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

        ValueBuyTotalTypes valueBuyTotalTypes = (ValueBuyTotalTypes) message.obj;

        if (valueBuyTotalTypes.mIsSucceed) {
            mValueBuyTypeInfoList.clear();
            mValueBuyTypeInfoList.addAll(valueBuyTotalTypes.mValueBuyTypeInfoList);
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
    }

    private void handleValueBuyItemClick(int position) {

        DebugUtil.debug(TAG, "handleValueBuyItemClick position " + position);
    }

}
