package cn.lemon.shopping;

import java.util.Observable;

import android.os.Bundle;

import cn.lemon.framework.BaseActivity;
import cn.lemon.utils.DebugUtil;

public class ValueBuyActivity extends BaseActivity {

    public static final String TAG = "ValueBuyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        DebugUtil.debug(TAG, "onCreate");
        setContentView(R.layout.value_buy_layout);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        DebugUtil.debug(TAG, "onSaveInstanceState");
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

}
