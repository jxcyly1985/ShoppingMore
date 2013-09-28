package cn.lemon.shopping;

import java.util.Observable;

import cn.lemon.framework.BaseActivity;
import cn.lemon.framework.BaseActivityGroup;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;

public class ShoppingMoreIndexActivity extends BaseActivityGroup {

	private TabHost mTabHost;
	private TabWidget mTabWidget;

	private Intent mRecommendIntent;
	private Intent mCashDeliveryIntent;
	private Intent mTaobaoIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_shopping_more_index);
		initView();
	}

	private void initView() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(getLocalActivityManager());
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);

		Resources resource = getResources();

		String firstTag = resource.getString(R.string.first_tag_name);
		String secondTag = resource.getString(R.string.second_tag_name);
		String thirdTag = resource.getString(R.string.third_tag_name);

		mRecommendIntent = new Intent(this, RecommendActivity.class);
		mCashDeliveryIntent = new Intent(this, ExternalWebActivity.class);
		mTaobaoIntent = new Intent(this, TaobaoTopicActivity.class);

		mTabHost.addTab(getTabSpecItem(firstTag, android.R.drawable.ic_delete, mRecommendIntent));
		mTabHost.addTab(getTabSpecItem(secondTag, android.R.drawable.ic_delete, mCashDeliveryIntent));
		mTabHost.addTab(getTabSpecItem(thirdTag, android.R.drawable.ic_delete, mTaobaoIntent));
	}

	private TabSpec getTabSpecItem(String tag, int resId, Intent intent) {

		TabSpec tabspec =  mTabHost.newTabSpec(tag).setIndicator(tag, getResources().getDrawable(resId))
				.setContent(intent);
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
