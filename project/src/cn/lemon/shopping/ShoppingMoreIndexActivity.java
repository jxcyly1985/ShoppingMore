package cn.lemon.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cn.lemon.framework.BaseActivityGroup;
import cn.lemon.shopping.adapter.ContentViewPagerAdapter;
import cn.lemon.utils.DebugUtil;
import android.os.Bundle;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class ShoppingMoreIndexActivity extends BaseActivityGroup {

    public static final String TAG = "ShoppingMoreIndexActivity";

    private TabHost mTabHost;
    private TabWidget mTabWidget;

    private ViewPager mViewPager;

    private String mRecommendString;
    private String mCashDeliveryString;
    private String mTabbaoString;

    private List<View> mViews;
    private LocalActivityManager mLocalActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_more_index);
        initView();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        mLocalActivityManager.dispatchDestroy(true);
    }

    private void initView() {

        Resources resources = getResources();

        mLocalActivityManager = getLocalActivityManager();
        buildContentViews();

        int tabWidgetHeight = 0;
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(mLocalActivityManager);
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.id_shopping_more_view_pager);

        mRecommendString = resources.getString(R.string.str_recommend);
        mCashDeliveryString = resources.getString(R.string.str_cash_delivery);
        mTabbaoString = resources.getString(R.string.str_taobao);

        mTabHost.addTab(getTabSpecItem(mRecommendString));
        mTabHost.addTab(getTabSpecItem(mCashDeliveryString));
        mTabHost.addTab(getTabSpecItem(mTabbaoString));
        initViewPager();

        // change tab widget height
        tabWidgetHeight = resources.getDimensionPixelSize(R.dimen.dimen_tab_widget_height);
        mTabWidget.getLayoutParams().height = tabWidgetHeight;
        mTabHost.setCurrentTab(0);
        updateTab(mTabHost);

    }

    @SuppressWarnings("deprecation")
    private void buildContentViews() {

        mViews = new ArrayList<View>();

        Intent recommendIntent = new Intent(this, RecommendActivity.class);
        Window recommendWindow = mLocalActivityManager.startActivity(mRecommendString, recommendIntent);
        mViews.add(recommendWindow.getDecorView());

        Intent cashDeliveryIntent = new Intent(this, ExternalWebActivity.class);
        Window cashDelieryWindow = mLocalActivityManager.startActivity(mRecommendString, cashDeliveryIntent);
        mViews.add(cashDelieryWindow.getDecorView());

        Intent taobaoIntent = new Intent(this, TaobaoTopicActivity.class);
        Window taobaoWindow = mLocalActivityManager.startActivity(mRecommendString, taobaoIntent);
        mViews.add(taobaoWindow.getDecorView());

    }

    private void initViewPager() {

        ContentViewPagerAdapter adapter = new ContentViewPagerAdapter(mViews);
        mViewPager.setAdapter(adapter);

        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                DebugUtil.debug(TAG, "onTabChanged tabId " + tabId);
                int selected = 0;
                if (tabId.equals(mRecommendString)) {
                    selected = 0;
                } else if (tabId.equals(mCashDeliveryString)) {
                    selected = 1;
                } else if (tabId.equals(mTabbaoString)) {
                    selected = 2;
                }
                mViewPager.setCurrentItem(selected);

            }
        });
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                DebugUtil.debug(TAG, "onPageSelected");
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub

                DebugUtil.debug(TAG, "onPageScrolled position " + position + " positionOffset "
                        + positionOffset + " positionOffsetPixels " + positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                DebugUtil.debug(TAG, "onPageScrollStateChanged state " + state);

            }
        });

        mViewPager.setCurrentItem(0);
    }

    private void updateTab(final TabHost tabHost) {
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View view = tabHost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextScaleX(getResources().getDisplayMetrics().density);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            tv.setTypeface(Typeface.SERIF, 2);
            if (tabHost.getCurrentTab() == i) {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.black));
            } else {
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
                tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
            }
        }
    }

    private TabSpec getTabSpecItem(String tag) {

        TabSpec tabspec = mTabHost.newTabSpec(tag).setIndicator(tag)
                .setContent(R.id.id_empty_tabcontent_text_view);
        return tabspec;
    }

    private TabSpec getTabSpecItem(String tag, Intent intent) {

        TabSpec tabspec = mTabHost.newTabSpec(tag).setIndicator(tag).setContent(intent);
        return tabspec;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_more_index, menu);
        return true;
    }

    @Override
    public void addObserver() {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteObserver() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Observable observable, Object data) {
        // TODO Auto-generated method stub

    }

}
