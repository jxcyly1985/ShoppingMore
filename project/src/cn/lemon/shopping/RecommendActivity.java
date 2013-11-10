package cn.lemon.shopping;

import java.util.Observable;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.lemon.framework.BaseActivity;
import cn.lemon.utils.DebugUtil;

public class RecommendActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = "RecommendActivity";
	
	// Add Image Fetcher
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		DebugUtil.debug(TAG, "onCreate");
		setContentView(R.layout.recommend_layout);
		initData();
		initView();

	}

	private void initData() {

	}
	
	private void initView() {
		
	}



	@Override
	public void onClick(View v) {
		
		
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
	public void addObserver() {

		this.mMesssageManager.addOberver(
				MessageConstants.MSG_LOAD_DATA_COMPLETE, this);
	}

	@Override
	public void deleteObserver() {
		this.mMesssageManager.deleteOberver(
				MessageConstants.MSG_LOAD_DATA_COMPLETE, this);
	}

	@Override
	public void update(Observable observable, Object data) {

		Message msg = (Message) data;

		switch (msg.what) {
		case MessageConstants.MSG_LOAD_DATA_COMPLETE:
			// notify adapter data set changed

			break;

		default:
			break;
		}
	}



}
