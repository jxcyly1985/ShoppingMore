package cn.lemon.shopping;

import android.os.Bundle;
import cn.lemon.framework.BaseActivity;
import cn.lemon.network.NetworkUtils;

import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-1-21
 * Time: 下午5:46
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingMoreLoadingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkNetConnection()) {

        }
    }

    private boolean checkNetConnection() {

        return NetworkUtils.isNetAvailable(this, true);
    }

    private void showNetSettingAlertDialog() {



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
