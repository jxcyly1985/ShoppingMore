package cn.lemon.framework;

import android.app.Fragment;
import android.os.Bundle;

public abstract class BaseFragement extends Fragment implements INotifyInterface {

    protected MessageManager mMesssageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mMesssageManager = MessageManager.getInstance();
        addObserver();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        deleteObserver();
    }
}
