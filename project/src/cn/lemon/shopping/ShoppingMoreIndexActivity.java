package cn.lemon.shopping;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

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
		mWorthBuyingString = resources.getString(R.string.str_worth_buying);
		mSettingString = resources.getString(R.string.str_setting);

		mTabHost.addTab(getTabSpecItem(mRecommendString));
		mTabHost.addTab(getTabSpecItem(mCashDeliveryString));
		mTabHost.addTab(getTabSpecItem(mWorthBuyingString));
		mTabHost.addTab(getTabSpecItem(mSettingString));
		initViewPager();

		// change tab widget height
		tabWidgetHeight = resources
				.getDimensionPixelSize(R.dimen.dimen_tab_widget_height);
		mTabWidget.getLayoutParams().height = tabWidgetHeight;
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

		Intent cashDeliveryIntent = new Intent(this, ExternalWebActivity.class);
		Window cashDelieryWindow = mLocalActivityManager.startActivity(
				mRecommendString, cashDeliveryIntent);
		mViews.add(cashDelieryWindow.getDecorView());

		Intent taobaoIntent = new Intent(this, TaobaoTopicActivity.class);
		Window taobaoWindow = mLocalActivityManager.startActivity(
				mRecommendString, taobaoIntent);
		mViews.add(taobaoWindow.getDecorView());

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
				mViewPager.setCurrentItem(mSelectPos);

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
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub

				DebugUtil.debug(TAG, "onPageScrolled position " + position
						+ " positionOffset " + positionOffset
						+ " positionOffsetPixels " + positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				DebugUtil.debug(TAG, "onPageScrollStateChanged state " + state);

			}
		});

		mViewPager.setCurrentItem(0);
	}

	private void initSettingPopupWindow() {

		List<Pair<Integer, String>> settingPairs = new ArrayList<Pair<Integer, String>>();
		String[] settingArray = getResources().getStringArray(
				R.array.setting_array);
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

		SettingViewAdapter settingAdapter = new SettingViewAdapter(
				getApplicationContext(), R.layout.image_text_item_layout,
				settingPairs);


		View contentView = View.inflate(getApplicationContext(),
				R.layout.setting_layout, null);
		// may need not call setFocusable && setFocusableInTouchMode
		contentView.setFocusable(true);
		// response to menu key click
		contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                
                DebugUtil.debug(TAG, "initSettingPopupWindow keycode " + keyCode
                        + " action " + event.getAction());
                
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
		GridView settingGrid = (GridView) contentView
				.findViewById(R.id.id_setting_grid);
		settingGrid.setAdapter(settingAdapter);
		settingGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				return true;
			}
		});
		contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		// create pop up window
        mSettingWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // let pop up window can get focus
        mSettingWindow.setFocusable(true);
        
		// let dismiss pop up window when click outside  
		mSettingWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.beauty_o));

	}

	@SuppressWarnings("deprecation")
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) mTabWidget.getChildAt(i).findViewById(
					android.R.id.title);
			tv.setTextScaleX(getResources().getDisplayMetrics().density);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			tv.setTypeface(Typeface.SERIF, 2);
			if (tabHost.getCurrentTab() == i) {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.ic_launcher));
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.black));
			} else {
				view.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.ic_launcher));
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.white));
			}
		}
	}

	private void PopupSettingDialog() {

		mSettingWindow.showAtLocation(mTabHost, Gravity.BOTTOM, 0, 0);

	}

	private TabSpec getTabSpecItem(String tag) {

		TabSpec tabspec = mTabHost.newTabSpec(tag).setIndicator(tag)
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
