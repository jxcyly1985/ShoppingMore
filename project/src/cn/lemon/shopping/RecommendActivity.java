package cn.lemon.shopping;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.*;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.framework.BaseActivity;
import cn.lemon.shopping.ad.AdImageManager;
import cn.lemon.shopping.adapter.MallEntryAdapter;
import cn.lemon.shopping.category.CategoryIconManager;
import cn.lemon.shopping.model.*;
import cn.lemon.shopping.ui.PosIndicator;
import cn.lemon.shopping.utils.Utils;
import cn.lemon.utils.DebugUtil;
import cn.lemon.utils.StaticUtils;

import java.util.List;
import java.util.Observable;

public class RecommendActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = "RecommendActivity";

    private final int AD_WAIT_TIME = 1000;
    private final int AD_CHANGE_TIMER = 3 * 1000;

    // Data
    private ShoppingMoreDomainDataManager mShoppingMoreDomainDataManager;
    private AdImageManager mAdImageManager;
    private CategoryIconManager mCategoryIconManager;
    private AdInfo mAdInfo;
    private MallTotalInfo mMallTotalInfo;

    private int mCurrentAdPos = 0;

    private boolean mAdCanMove = true;
    private boolean mAdReady = false;

    // UI
    private LinearLayout mRecommendContainer;
    private ImageSwitcher mAdImageSwitcher;
    private PosIndicator mAdIndicator;

    // Tool
    private ImageFetcher mMallImageFetcher;
    private AdChangeHandler mAdChangeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.recommend_layout);
        initData();
        initView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DebugUtil.debug(TAG, "onSaveInstanceState");
    }


    private void initData() {


        mAdImageManager = AdImageManager.getInstance();
        mAdImageManager.init(this);
        mCategoryIconManager = CategoryIconManager.getInstance();
        mCategoryIconManager.init(this);
        mMallImageFetcher = ImageFetcherManager.getInstance().getMallImageFetcher(RecommendActivity.this);

        mShoppingMoreDomainDataManager = ShoppingMoreDomainDataManager.getInstance();
        BaseRequestEntity<AdInfo> adInfoBaseRequestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_AD);
        BaseRequestEntity<MallTotalInfo> mallTotalInfoBaseRequestEntity =
                mShoppingMoreDomainDataManager.getRequestEntityDelegator(ShoppingMoreDomainDataManager.TYPE_MALL);

        RequestEntityDelegator<AdInfo> adInfoRequestEntityDelegator = new RequestEntityDelegator<AdInfo>();
        RequestEntityDelegator<MallTotalInfo> mallTotalInfoRequestEntityDelegator = new RequestEntityDelegator<MallTotalInfo>();

        mAdInfo = adInfoRequestEntityDelegator.getRequestEntity(adInfoBaseRequestEntity);
        mMallTotalInfo = mallTotalInfoRequestEntityDelegator.getRequestEntity(mallTotalInfoBaseRequestEntity);

        mAdChangeHandler = new AdChangeHandler();

    }

    private void initView() {

        mRecommendContainer = (LinearLayout) findViewById(R.id.id_recommend_container);
        mAdImageSwitcher = (ImageSwitcher) findViewById(R.id.id_ad_image_switch);
        mAdImageSwitcher.setFactory(new ImageFoctory());
        mAdIndicator = (PosIndicator) findViewById(R.id.id_ad_indicator);
        mAdImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.x_enter_anim));
        mAdImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.x_leave_anim));
        mAdImageSwitcher.setOnClickListener(this);
        // init position is zero
        initAdResource();
        initCategory();
    }

    private void submitAdTask(AdInfo adInfo) {

        List<AdInfo.AdData> adDataList = adInfo.mDatas;
        int adDataSize = adDataList.size();
        String[] adImageUrls = new String[adDataSize];
        for (int i = 0; i < adDataSize; ++i) {
            adImageUrls[i] = adDataList.get(i).mImageURL;
        }
        mAdImageManager.submit(adImageUrls);

    }

    private void initAdIndicator(AdInfo adInfo) {

        List<AdInfo.AdData> adDataList = adInfo.mDatas;
        int adDataSize = adDataList.size();

        if (adDataSize > 0) {
            DebugUtil.debug(TAG, "initAdIndicator size " + adDataSize);
            mAdIndicator.setItemCount(adDataSize);
            mAdIndicator.setSelectedPos(0);
        }

    }

    private void initAdResource() {

        // QiYun<LeiYong><2014-03-20> modify for CR00000012 begin
        if (mAdInfo != null && mAdInfo.mIsSuccess) {
            submitAdTask(mAdInfo);
            initImageSwitch();
        }
        // QiYun<LeiYong><2014-03-20> modify for CR00000012 end
    }

    private void initImageSwitch() {

        ImageView child = (ImageView) mAdImageSwitcher.getChildAt(0);
        child.setImageResource(R.drawable.ad_preload_page);
    }

    private MallEntryAdapter getCategoryAdapter(List<MallEntryInfo> mallEntryInfos) {

        DebugUtil.debug(TAG, "getCategoryAdapter mallEntryInfos size " + mallEntryInfos.size());
        //ImageFetcher mMallImageFetcher = ImageFetcherManager.getInstance().getMallImageFetcher(RecommendActivity.this);
        return new MallEntryAdapter(this, mallEntryInfos, mMallImageFetcher);
    }


    private void initGridView(View categoryView, List<MallEntryInfo> infos) {
        GridView categoryGridView = (GridView) categoryView.findViewById(R.id.id_category_grid);
        MallEntryAdapter adapter = getCategoryAdapter(infos);
        categoryGridView.setAdapter(adapter);
        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MallEntryInfo mallEntryInfo = (MallEntryInfo) parent.getAdapter().getItem(position);
                ActivityUtils.toMallHostActivity(RecommendActivity.this, mallEntryInfo.mLinkedUrl);
            }
        });

        Utils.setGridViewHeightBasedOnChildren(categoryGridView);

    }

    private void initCategoryIcon(View categoryView, String iconUrl) {

        ImageView categoryIcon = (ImageView) categoryView.findViewById(R.id.id_category_icon);

        //todo 需要替换默认的图标
        categoryIcon.setImageResource(R.drawable.icon);
        mCategoryIconManager.getIcon(iconUrl, categoryIcon);
    }

    private void initCategoryTitle(View categoryView, String categoryName) {

        TextView categoryTitle = (TextView) categoryView.findViewById(R.id.id_category_title);
        categoryTitle.setText(categoryName);
    }

    private void initCategoryTileBar(View categoryView, String colorString) {

        View categoryBar = categoryView.findViewById(R.id.id_category_bar);
        int color = StaticUtils.getInstance().getColor(colorString);
        categoryBar.setBackgroundColor(color);

        DebugUtil.debug(TAG, "initCategoryTileBar color " + color);
    }

    private void addToPage(View categoryView, boolean useMarginTop) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (useMarginTop) {
            layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_category_vertical_spacing);
        }
        mRecommendContainer.addView(categoryView, layoutParams);
    }

    private View obtainCategoryView() {
        return getLayoutInflater().inflate(R.layout.category_item_layout, null);
    }

    private void initTopSide(View categoryView) {

        View topSide = categoryView.findViewById(R.id.id_category_item_top_side);
        topSide.setVisibility(View.GONE);

    }

    private View initCategoryView(CategoryEntryInfo categoryEntryInfo) {
        View categoryView = obtainCategoryView();
        initGridView(categoryView, categoryEntryInfo.mMallEntryInfoList);
        initCategoryTileBar(categoryView, categoryEntryInfo.mBackgroundColor);
        initCategoryTitle(categoryView, categoryEntryInfo.mCategoryName);
        initCategoryIcon(categoryView, categoryEntryInfo.mIconUrl);
        return categoryView;
    }

    private void initCategoryViewNoTopSide(CategoryEntryInfo categoryEntryInfo) {

        DebugUtil.debug(TAG, "initCategoryViewNoTopSide");
        View categoryView = initCategoryView(categoryEntryInfo);
        initTopSide(categoryView);
        addToPage(categoryView, false);
    }

    private void initCategoryViewWithTopSide(CategoryEntryInfo categoryEntryInfo) {

        DebugUtil.debug(TAG, "initCategoryViewWithTopSide");
        View categoryView = initCategoryView(categoryEntryInfo);
        addToPage(categoryView, true);
    }


    private void initCategory() {

        // QiYun<LeiYong><2014-03-20> modify for CR00000012 begin
        if (mMallTotalInfo != null && mMallTotalInfo.mCategoryList.size() > 0) {
            int pos = 0;
            CategoryEntryInfo categoryEntryInfo = mMallTotalInfo.mCategoryList.get(pos);
            initCategoryViewNoTopSide(categoryEntryInfo);
            pos++;

            while (pos < mMallTotalInfo.mCategoryList.size()) {
                categoryEntryInfo = mMallTotalInfo.mCategoryList.get(pos);
                initCategoryViewWithTopSide(categoryEntryInfo);
                pos++;
            }
        }
        // QiYun<LeiYong><2014-03-20> modify for CR00000012 end

    }

    private class ImageFoctory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {

            DebugUtil.debug(TAG, "RecommendActivity ImageFoctory makeView");

            ImageView imageView = new ImageView(RecommendActivity.this);
            int imageWidth = getResources().getDimensionPixelOffset(R.dimen.dimen_ad_height);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, imageWidth));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
    }

    private int getAdPos() {

        if (mCurrentAdPos == mAdInfo.mDatas.size()) {
            mCurrentAdPos = 0;
        }

        return mCurrentAdPos;
    }

    private void IncAdPos() {

        mCurrentAdPos++;
    }

    private void switchAdView(String imageUrl, int pos) {

        if (imageUrl != null) {
            ImageView imageView = (ImageView) mAdImageSwitcher.getNextView();
            // QiYun<LeiYong><2014-03-18> modify for CR00000011 begin
            imageView.setTag(imageUrl);
            // QiYun<LeiYong><2014-03-18> modify for CR00000011 end
            mAdImageManager.getDrawable(imageView, imageUrl);
            mAdImageSwitcher.showNext();
            mAdIndicator.setSelectedPos(pos);
        }
    }

    private void invokeNextAdChange() {

        if (isAdCanMove()) {

            mAdChangeHandler.sendEmptyMessageDelayed(0, AD_CHANGE_TIMER);
        }
    }


    private class AdChangeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            DebugUtil.debug(TAG, "AdChangeHandler handleMessage");

            int pos = getAdPos();
            String adImageUrl = getAdImageUrl(pos);
            switchAdView(adImageUrl, pos);
            IncAdPos();
            invokeNextAdChange();
        }
    }


    private String getAdImageUrl(int pos) {

        if (mAdInfo != null) {
            return mAdInfo.mDatas.get(pos).mImageURL;
        }
        return null;
    }


    private String getAdLinkUrl() {

        if (mAdInfo != null) {
            return mAdInfo.mDatas.get(getAdPos()).mLinkURL;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.id_ad_image_switch:
                ActivityUtils.toAdHostActivity(this, getAdLinkUrl());
                break;
        }
    }

    private boolean isAdCanMove() {
        return mAdCanMove;
    }

    private void setAdCanMove(boolean canMove) {
        mAdCanMove = canMove;
    }

    private void resumeAd() {
        if (mAdReady) {
            setAdCanMove(true);
            mAdImageManager.onResume();
            mAdChangeHandler.sendEmptyMessageDelayed(0, AD_CHANGE_TIMER);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        mAdImageManager.onPause();
        setAdCanMove(false);
        DebugUtil.debug(TAG, "onPause");

    }

    @Override
    protected void onResume() {

        super.onResume();
        resumeAd();
        DebugUtil.debug(TAG, "onResume");
    }

    private void handleAdDestroy() {
        mAdImageManager.onDestroy();
        setAdCanMove(false);
        mAdChangeHandler.removeMessages(0);

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        handleAdDestroy();
        DebugUtil.debug(TAG, "onDestroy");
    }

    @Override
    public void addObserver() {
        mMesssageManager.addObserver(MessageConstants.MSG_NET_WORK_ERROR, this);
        mMesssageManager.addObserver(MessageConstants.MSG_MALL_DATA_RETURN, this);
        mMesssageManager.addObserver(MessageConstants.MSG_AD_DATA_RETURN, this);
        mMesssageManager.addObserver(MessageConstants.MSG_AD_IMAGE_READY, this);

    }

    @Override
    public void deleteObserver() {
        mMesssageManager.deleteObserver(MessageConstants.MSG_NET_WORK_ERROR, this);
        mMesssageManager.deleteObserver(MessageConstants.MSG_MALL_DATA_RETURN, this);
        mMesssageManager.deleteObserver(MessageConstants.MSG_AD_DATA_RETURN, this);
        mMesssageManager.deleteObserver(MessageConstants.MSG_AD_IMAGE_READY, this);
    }

    @Override
    public void update(Observable observable, Object data) {

        Message msg = (Message) data;

        int what = msg.what;
        DebugUtil.debug(TAG, "update what 0x" + Integer.toHexString(what));

        switch (what) {
            case MessageConstants.MSG_NET_WORK_ERROR:
                DebugUtil.debug(TAG, "MSG_NET_WORK_ERROR");
                Utils.showToast(this, R.string.str_net_error);
                break;
            case MessageConstants.MSG_MALL_DATA_RETURN:
                DebugUtil.debug(TAG, "MSG_MALL_DATA_RETURN");
                // QiYun<LeiYong><2014-01-11> modify for CR00000004 begin
                mMallTotalInfo = (MallTotalInfo) msg.obj;
                // QiYun<LeiYong><2014-01-11> modify for CR00000004 end
                initCategory();
                break;

            case MessageConstants.MSG_AD_DATA_RETURN:
                DebugUtil.debug(TAG, "MSG_AD_DATA_RETURN");
                mAdInfo = (AdInfo) msg.obj;
                initAdResource();
                break;
            case MessageConstants.MSG_AD_IMAGE_READY:
                DebugUtil.debug(TAG, "MSG_AD_IMAGE_READY");
                mAdChangeHandler.sendEmptyMessageDelayed(0, AD_WAIT_TIME);
                initAdIndicator(mAdInfo);
                mAdReady = true;
                break;
            default:
                break;
        }
    }


}
