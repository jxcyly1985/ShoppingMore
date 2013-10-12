package cn.lemon.shopping;

import java.util.Observable;

import android.os.Bundle;

import cn.lemon.framework.BaseActivity;
import cn.lemon.framework.MessageConstants;
import cn.lemon.framework.MessageManager;
import cn.lemon.utils.DebugUtil;

public class RecommendActivity extends BaseActivity {

    public static final String TAG = "RecommendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.recommend_layout);

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

        
        this.mMesssageManager.addOberver(MessageConstants.MSG_LOAD_DATA_COMPLETE, this);
    }

    @Override
    public void deleteObserver() {

    }

    @Override
    public void update(Observable observable, Object data) {

    }

}
