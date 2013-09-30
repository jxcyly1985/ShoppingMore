package cn.lemon.shopping;

import java.util.Observable;

import android.os.Bundle;

import cn.lemon.framework.BaseActivity;
import cn.lemon.utils.DebugUtil;

public class TaobaoTopicActivity extends BaseActivity {

    public static final String TAG = "TaobaoTopicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.taobao_layout);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        DebugUtil.debug(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        DebugUtil.debug(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        DebugUtil.debug(TAG, "onDestroy");
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
