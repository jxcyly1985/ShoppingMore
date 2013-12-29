package cn.lemon.shopping;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import cn.lemon.shopping.Ad.AdImageManager;
import cn.lemon.shopping.adapter.MallEntryAdapter;
import cn.lemon.shopping.model.*;
import cn.lemon.shopping.ui.ViewPagerIndicator;
import cn.lemon.shopping.utils.Utils;
import cn.lemon.utils.DebugUtil;
import cn.lemon.utils.StaticUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class RecommendActivity extends BaseActivity implements OnClickListener {

    public static final String TAG = "RecommendActivity";

    // Data
    private ShoppingMoreDomainDataManager mShoppingMoreDomainDataManager;
    private AdImageManager mAdImageManager;
    private AdInfo mAdInfo;
    private MallTotalInfo mMallTotalInfo;
    private int mCurrentAdPos = 0;
    private boolean mAdMoving = true;
    private Map<String, SoftReference<BitmapDrawable>> mAdBitmapDrawableMap;
    private final int AD_CHANGE_TIMER = 3 * 1000;

    // UI
    private LinearLayout mRecommendContainer;
    private ImageSwitcher mAdImageSwitcher;
    private ViewPagerIndicator mAdIndicator;

    // Tool
    private ImageFetcher mAdImageFetcher;
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

    private void initData() {

        mShoppingMoreDomainDataManager = ShoppingMoreDomainDataManager.getInstance();

        mAdImageManager = AdImageManager.getInstance();
        mAdImageManager.init(this);

        mAdInfo = mShoppingMoreDomainDataManager.getAdInfo();
        mMallTotalInfo = mShoppingMoreDomainDataManager.getMallTotalInfo();

        mAdChangeHandler = new AdChangeHandler();
        mAdBitmapDrawableMap = new HashMap<String, SoftReference<BitmapDrawable>>();

        mAdImageFetcher = ImageFetcherManager.getInstance().getAdImageFetcher(RecommendActivity.this);
    }

    private void initView() {

        mRecommendContainer = (LinearLayout) findViewById(R.id.id_recommend_container);
        mAdImageSwitcher = (ImageSwitcher) findViewById(R.id.id_ad_image_switch);
        mAdImageSwitcher.setFactory(new ImageFoctory());
        mAdIndicator = (ViewPagerIndicator) findViewById(R.id.id_ad_indicator);
        mAdImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.x_enter_anim));
        mAdImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.x_leave_anim));
        mAdImageSwitcher.setOnClickListener(this);
        // init position is zero
        initAdResource();
        initCategory();
    }

    private void initAdResource() {

        if (mAdInfo != null) {
            List<AdInfo.AdData> adDataList = mAdInfo.mDatas;
            int adDataSize = adDataList.size();
            mAdIndicator.setItemCount(adDataSize);
            mAdIndicator.setSelectedPos(mCurrentAdPos);
            String[] adImageUrls = new String[adDataSize];
            for (int i = 0; i < adDataSize; ++i) {
                adImageUrls[i] = adDataList.get(i).mImageURL;
            }
            mAdImageManager.setAdImageUrls(adImageUrls);
            mAdChangeHandler.sendEmptyMessageDelayed(0, AD_CHANGE_TIMER);
            initImageSwitch();

        }

    }


    private void initImageSwitch() {

        ImageView childZero = (ImageView) mAdImageSwitcher.getChildAt(0);
        ImageView childOne = (ImageView) mAdImageSwitcher.getChildAt(1);
        childZero.setImageResource(R.drawable.ad_preload_page_1);
        childOne.setImageResource(R.drawable.ad_preload_page_2);
    }

    private MallEntryAdapter getCategoryAdapter(List<MallEntryInfo> mallEntryInfos) {
        ImageFetcher imageFetcher = ImageFetcherManager.getInstance().getMallImageFetcher(RecommendActivity.this);
        return new MallEntryAdapter(this, mallEntryInfos, imageFetcher);
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
        categoryIcon.setImageResource(R.drawable.icon);

    }

    private void initCategoryTitle(View categoryView, String categoryName) {

        TextView categoryTitle = (TextView) categoryView.findViewById(R.id.id_category_title);
        categoryTitle.setText(categoryName);
    }

    private void initCategoryTileBar(View categoryView, String colorString) {

        View categoryBar = categoryView.findViewById(R.id.id_category_bar);
        int color = StaticUtils.getInstance().getColor(colorString);
        categoryBar.setBackgroundColor(color);
    }

    private void addToPage(View categoryView){
        mRecommendContainer.addView(categoryView);
    }

    private View obtainCategoryView(){
        return getLayoutInflater().inflate(R.layout.category_item_layout, null);
    }

    private void initCategory() {

        if (mMallTotalInfo != null) {

            List<CategoryEntryInfo> categoryEntryInfos = mMallTotalInfo.mCategoryList;

            for (CategoryEntryInfo categoryEntryInfo : categoryEntryInfos) {

                View categoryView = obtainCategoryView();
                initGridView(categoryView, categoryEntryInfo.mMallEntryInfoList);
                initCategoryTileBar(categoryView, categoryEntryInfo.mBackgroundColor);
                initCategoryTitle(categoryView, categoryEntryInfo.mCategoryName);
                initCategoryIcon(categoryView, categoryEntryInfo.mIconUrl);
                addToPage(categoryView);
            }
        }

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

    private class AdChangeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if (mCurrentAdPos == mAdInfo.mDatas.size()) {
                mCurrentAdPos = 0;
            }

            String adImageUrl = getAdImageUrl();
            BitmapDrawable bitmapDrawable = null;
            ImageView child = null;

            if (adImageUrl != null) {
                child = (ImageView) mAdImageSwitcher.getNextView();
                bitmapDrawable = mAdImageManager.getBitmapDrawable(adImageUrl);
                if (bitmapDrawable != null) {
                    child.setImageDrawable(bitmapDrawable);
                }
                mAdImageSwitcher.showNext();
            }

//            if(adImageUrl != null){
//                if (mAdBitmapDrawableMap.containsKey(adImageUrl)) {
//
//                    bitmapDrawable = mAdBitmapDrawableMap.get(adImageUrl).get();
//                    if (bitmapDrawable != null) {
//                        mAdImageSwitcher.setImageDrawable(bitmapDrawable);
//                    } else {
//
//                        mAdImageSwitcher.showNext();
//                        child = (ImageView) mAdImageSwitcher.getNextView();
//                        mAdImageFetcher.loadImage(adImageUrl, child);
//                    }
//                } else {
//                    mAdImageSwitcher.showNext();
//                    child = (ImageView) mAdImageSwitcher.getNextView();
//                    mAdImageFetcher.loadImage(adImageUrl, child);
//                }
//            }

//            mCurrentAdPos++;
//
//            if (mAdMoving) {
//                mAdChangeHandler.sendEmptyMessageDelayed(0, AD_CHANGE_TIMER);
//            }

        }
    }


    private String getAdImageUrl() {

        if (mAdInfo != null) {

            return mAdInfo.mDatas.get(mCurrentAdPos).mImageURL;
        }
        return null;

    }


    private String getAdLinkUrl() {

        if (mAdInfo != null) {

            return mAdInfo.mDatas.get(mCurrentAdPos).mLinkURL;
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.id_ad_image_switch: {
                String url = getAdLinkUrl();
                if (url != null) {
                    ActivityUtils.toAdHostActivity(this, url);
                }
            }
            break;
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        mAdMoving = false;
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
    public void addObserver() {

        this.mMesssageManager.addOberver(
                MessageConstants.MSG_MALL_DATA_RETURN, this);
        this.mMesssageManager.addOberver(MessageConstants.MSG_AD_DATA_RETURN, this);
    }

    @Override
    public void deleteObserver() {
        this.mMesssageManager.deleteOberver(
                MessageConstants.MSG_MALL_DATA_RETURN, this);
        this.mMesssageManager.deleteOberver(MessageConstants.MSG_AD_DATA_RETURN, this);
    }

    @Override
    public void update(Observable observable, Object data) {

        Message msg = (Message) data;

        int what = msg.what;
        DebugUtil.debug(TAG, "update what 0x" + Integer.toHexString(what));

        switch (what) {
            case MessageConstants.MSG_MALL_DATA_RETURN:
                // notify adapter data set changed
                initCategory();
                break;

            case MessageConstants.MSG_AD_DATA_RETURN:
                mAdInfo = (AdInfo) msg.obj;
                initAdResource();

                break;

            default:
                break;
        }
    }


}
