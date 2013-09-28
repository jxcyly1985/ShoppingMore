package cn.lemon.framework;

import android.app.ActivityGroup;
import android.os.Bundle;

public abstract class BaseActivityGroup extends ActivityGroup implements
		INotifyInterface {
	protected MessageManager mMesssageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMesssageManager = MessageManager.getInstance();
		addObserver();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		deleteObserver();
	}

}
