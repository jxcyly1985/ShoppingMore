package cn.lemon.framework;

import android.app.Activity;
import android.os.Bundle;


public abstract class BaseActivity extends Activity implements INotifyInterface{
    
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
