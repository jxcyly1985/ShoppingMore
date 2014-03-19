package cn.lemon.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.app.Activity;
import android.widget.*;
import cn.lemon.framework.BaseActivityGroup;
import cn.lemon.shopping.adapter.ContentViewPagerAdapter;
import cn.lemon.shopping.adapter.SettingViewAdapter;
import cn.lemon.utils.DebugUtil;
import android.os.Bundle;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class ShoppingMoreIndexActivity extends BaseActivityGroup implements
        OnClickListener {

    public static final String TAG = "ShoppingMoreIndexActivity";

    private TabHost mTabHost;
    private TabWidget mTabWidget;

    private ViewPager mViewPager;

    private String mRecommendString;
    private String mCashDeliveryString;
    private String mWorthBuyingString;
    private String mSettingString;

    private List<View> mViews;
    private LocalActivityManager mLocalActivityManager;

    private ImageView mSearchImageView;
    private int mSelectPos = 0;

    private PopupWindow mSettingWindow;

    private boolean mSettingNoIcon = true;

    private int[] mTabIconRes;
    private int[] mTabTextRes;

    private ImageFetcherManager mImageFetcherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_more_index);

        initManager(savedInstanceState);
        initRes();
        initView();

        DebugUtil.debug(TAG, "onCreate");
    }


    private void initManager(Bundle savedInstanceState) {
        mLocalActivityManager = new LocalActivityManager(this, false);
        Bundle states = savedInstanceState != null
                ? (Bundle) savedInstanceState.getBundle("android:states") : null;
        mLocalActivityManager.dispatchCreate(states);

        mImageFetcherManager = ImageFetcherManager.getInstance();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalActivityManager.dispatchDestroy(true);
        // QiYun<LeiYong><2014-03-19> modify for CR00000012 begin
        mImageFetcherManager.clear();
        // QiYun<LeiYong><2014-03-19> modify for CR00000012 end
        DebugUtil.debug(TAG, "onDestroy");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(false);
        DebugUtil.debug(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocalActivityManager.dispatchStop();
        mImageFetcherManager.flush();
        DebugUtil.debug(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
        DebugUtil.debug(TAG, "onResume");
    }

    private void initRes() {

        Resources resources = getResources();

        mTabIconRes = new int[]{R.drawable.tab_recommend_icon_selector, R.drawable.tab_cash_icon_selector,
                R.drawable.tab_value_buy_selector};
        mTabTextRes = new int[]{R.string.str_recommend, R.string.str_cash_delivery, R.string.str_worth_buying};

        mRecommendString = resources.getString(R.string.str_recommend);
        mCashDeliveryString = resources.getString(R.string.str_cash_delivery);
        mWorthBuyingString = resources.getString(R.string.str_worth_buying);
        mSettingString = resources.getString(R.string.str_setting);

    }

    private void initView() {

        buildContentViews();

        int tabWidgetHeight = 0;
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(mLocalActivityManager);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.id_shopping_more_view_pager);

        mTabHost.addTab(getTabSpecItem(mRecommendString));
        mTabHost.addTab(getTabSpecItem(mCashDeliveryString));
        mTabHost.addTab(getTabSpecItem(mWorthBuyingString));
        //mTabHost.addTab(getTabSpecItem(mSettingString));
        initViewPager();

        // remove bottom indicator
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            mTabWidget.setStripEnabled(false);
        }
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mTabWidget.setShowDividers(TabWidget.SHOW_DIVIDER_MIDDLE);
        }

        mTabWidget.setDividerDrawable(R.drawable.tab_widget_divider);
        int dividerPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_tab_widget_divider_padding);
        mTabWidget.setDividerPadding(dividerPadding);
        mTabHost.setCurrentTab(0);
        updateTab(mTabHost);

        mSearchImageView = (ImageView) findViewById(R.id.id_search_image_view);
        mSearchImageView.setOnClickListener(this);

        // initialize setting pop up window
        initSettingPopupWindow();

    }

    @SuppressWarnings("deprecation")
    private void buildContentViews() {

        mViews = new ArrayList<View>();

        Intent recommendIntent = new Intent(this, RecommendActivity.class);
        Window recommendWindow = mLocalActivityManager.startActivity(
                mRecommendString, recommendIntent);
        mViews.add(recommendWindow.getDecorView());

        Intent cashDeliveryIntent = new Intent(this, CashDeliveryActivity.class);
        Window cashDeliveryWindow = mLocalActivityManager.startActivity(
                mCashDeliveryString, cashDeliveryIntent);
        mViews.add(cashDeliveryWindow.getDecorView());

        Intent valueBuyIntent = new Intent(this, ValueBuyActivity.class);
        Window valueBuyWindow = mLocalActivityManager.startActivity(
                mWorthBuyingString, valueBuyIntent);
        mViews.add(valueBuyWindow.getDecorView());

    }


    private void onActivityPageChanged() {

        // send resume, pause and stop state
        // QiYun<LeiYong><2014-03-15> modify for CR00000011 begin
//        mLocalActivityManager.dispatchPause(false);
//        mLocalActivityManager.dispatchStop();

        Activity currentActivity = mLocalActivityManager.getCurrentActivity();

        // QiYun<LeiYong><2014-03-15> modify for CR00000011 end
    }

    private void initViewPager() {

        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(mViews);
        mViewPager.setAdapter(adapter);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                DebugUtil.debug(TAG, "onTabChanged tabId " + tabId);
                if (tabId.equals(mRecommendString)) {
                    mSelectPos = 0;
                } else if (tabId.equals(mCashDeliveryString)) {
                    mSelectPos = 1;
                } else if (tabId.equals(mWorthBuyingString)) {
                    mSelectPos = 2;
                } else if (tabId.equals(mSettingString)) {
                    PopupSettingDialog();
                }
                onActivityPageChanged();
                mViewPager.setCurrentItem(mSelectPos);

                // updateTab(mTabHost);

            }
        });
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                DebugUtil.debug(TAG, "onPageSelected");
                onActivityPageChanged();
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

                DebugUtil.debug(TAG, "onPageScrolled position " + position
                        + " positionOffset " + positionOffset
                        + " positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                DebugUtil.debug(TAG, "onPageScrollStateChanged state " + state);

            }
        });

        mViewPager.setCurrentItem(0);
    }

    private void PopupSettingDialog() {

        mSettingWindow.setAnimationStyle(R.style.popup_window_anim);

        mSettingWindow.showAtLocation(mTabHost, Gravity.BOTTOM, 0, 0);

    }

    private void initSettingPopupWindow() {

        List<Pair<Integer, String>> settingPairs = new ArrayList<Pair<Integer, String>>();
        String[] settingArray = getResources().getStringArray(
                R.array.setting_array);

        BaseAdapter settingAdapter = null;

        if (mSettingNoIcon) {
            settingAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.text_item_layout, R.id.id_text_item, settingArray);

        } else {
            Pair<Integer, String> refreshItem = Pair.create(
                    android.R.drawable.ic_delete, settingArray[0]);
            Pair<Integer, String> checkVersionItem = Pair.create(
                    android.R.drawable.ic_delete, settingArray[1]);
            Pair<Integer, String> helpItem = Pair.create(
                    android.R.drawable.ic_delete, settingArray[2]);
            Pair<Integer, String> exitItem = Pair.create(
                    android.R.drawable.ic_delete, settingArray[3]);
            settingPairs.add(refreshItem);
            settingPairs.add(checkVersionItem);
            settingPairs.add(helpItem);
            settingPairs.add(exitItem);
            settingAdapter = new SettingViewAdapter(getApplicationContext(),
                    R.layout.image_text_item_layout, settingPairs);
        }

        View contentView = View.inflate(getApplicationContext(),
                R.layout.setting_layout, null);
        // may need not call setFocusable && setFocusableInTouchMode
        contentView.setFocusable(true);
        // response to menu key click
        contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                DebugUtil.debug(TAG, "initSettingPopupWindow keycode "
                        + keyCode + " action " + event.getAction());

                // compare to ACTION_DOWN if ACTION_UP cause problem
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mSettingWindow.isShowing()) {
                            mSettingWindow.dismiss();
                            return true;
                        }

                    }
                    if (keyCode == KeyEvent.KEYCODE_MENU) {
                        if (mSettingWindow.isShowing()) {
                            mSettingWindow.dismiss();
                        } else {
                            PopupSettingDialog();
                        }

                        return true;
                    }
                }
                return false;

            }
        });
        ListView settingView = (ListView) contentView
                .findViewById(R.id.id_setting_adapter_view);
        settingView.setAdapter(settingAdapter);
        settingView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                return true;
            }
        });
        contentView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // create pop up window
        mSettingWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // let pop up window can get focus
        mSettingWindow.setFocusable(true);

        // let dismiss pop up window when click outside
        mSettingWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.popup_window_bg));

    }

    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View indicatorView = tabHost.getTabWidget().getChildAt(i);
            ImageView icon = (ImageView) indicatorView.findViewById(R.id.id_tab_item_icon);
            TextView textView = (TextView) indicatorView.findViewById(R.id.id_tab_item_text);
            // set selector
            icon.setImageResource(mTabIconRes[i]);
            textView.setText(mTabTextRes[i]);

        }
    }


    @SuppressWarnings("deprecation")
    private void updateTab2(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
                    android.R.id.title);
            ImageView icon = (ImageView) mTabWidget.getChildAt(i).findViewById(android.R.id.icon);

            // Set Tab view Bottom padding
            int verticalPadding = getResources().getInteger(
                    R.integer.tab_vertical);
            view.setPadding(0, verticalPadding, 0, verticalPadding);

            // set tab text size in dip unit
            int textSize = getResources().getInteger(R.integer.tab_text_size);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);

            // set text view align bottom
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                    RelativeLayout.TRUE);

            // http://blog.csdn.net/thinkscape/article/details/1967339
            tv.setTypeface(Typeface.SANS_SERIF);
            // set selector
            view.setBackgroundResource(R.drawable.tab_widget_bg);
            tv.setTextColor(R.color.tab_text_selected_color);
            icon.setImageResource(mTabIconRes[i]);

        }
    }


//    private TabSpec getTabSpecItem(String tag) {
//
//        @SuppressWarnings("deprecation")
//        Drawable iconDrawable = new StateListDrawable();
//        TabSpec tabspec = mTabHost.newTabSpec(tag)
//                .setIndicator(tag, iconDrawable)
//                .setContent(R.id.id_empty_tabcontent_text_view);
//        return tabspec;
//    }


    private TabSpec getTabSpecItem(String tag) {


        View indicatorView = getLayoutInflater().inflate(R.layout.tab_widget_item_layout, null);
        TabSpec tabspec = mTabHost.newTabSpec(tag)
                .setIndicator(indicatorView)
                .setContent(R.id.id_empty_tabcontent_text_view);
        return tabspec;
    }

    private TabSpec getTabSpecItem(String tag, Intent intent) {

        TabSpec tabspec = mTabHost.newTabSpec(tag).setIndicator(tag)
                .setContent(intent);
        return tabspec;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        DebugUtil.debug(TAG, "activity onKeyDown");

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mSettingWindow.isShowing()) {
                mSettingWindow.dismiss();
            } else {
                PopupSettingDialog();
                // change to setting tab
                mTabHost.setCurrentTab(3);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
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

    @Override
    public void onClick(View v) {

    }

}
