package cn.lemon.framework;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public abstract class BaseActivity extends FragmentActivity implements INotifyInterface{
    
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
