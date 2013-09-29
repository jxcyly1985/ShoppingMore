package cn.lemon.shopping;

import java.util.Observable;

import cn.lemon.framework.BaseActivityGroup;
import cn.lemon.utils.DebugUtil;
import android.os.Bundle;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shopping_more_index);
        initView();
    }

    private void initView() {

        Resources resources = getResources();

        int tabWidgetHeight = 0;
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.id_shopping_more_view_pager);

        mRecommendString = resources.getString(R.string.str_recommend);
        mCashDeliveryString = resources.getString(R.string.str_cash_delivery);
        mTabbaoString = resources.getString(R.string.str_taobao);

        mTabHost.addTab(getTabSpecItem(mRecommendString));
        mTabHost.addTab(getTabSpecItem(mCashDeliveryString));
        mTabHost.addTab(getTabSpecItem(mTabbaoString));

        // change tab widget height
        tabWidgetHeight = resources.getDimensionPixelSize(R.dimen.dimen_tab_widget_height);
        mTabWidget.getLayoutParams().height = tabWidgetHeight;
        mTabHost.setCurrentTab(0);
        updateTab(mTabHost);

        initViewPager();

    }

    private void initViewPager() {

        ImageView imageview1 = new ImageView(this);
        ImageView imageview2 = new ImageView(this);
        ImageView imageview3 = new ImageView(this);
        imageview1.setImageResource(R.drawable.beauty_o);
        imageview2.setImageResource(R.drawable.beauty_r);
        imageview3.setImageResource(R.drawable.beauty_t);
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();

        mViewPager.addView(imageview1, layoutParams);
        mViewPager.addView(imageview2, layoutParams);
        mViewPager.addView(imageview3, layoutParams);

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

                DebugUtil.debug(TAG, "onPageScrolled");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                DebugUtil.debug(TAG, "onPageScrollStateChanged");

            }
        });
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
                .setContent(R.id.id_shopping_more_view_pager);
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
