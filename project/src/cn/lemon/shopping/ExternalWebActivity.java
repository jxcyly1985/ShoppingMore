package cn.lemon.shopping;

import java.util.Observable;

import android.os.Bundle;

import cn.lemon.framework.BaseActivity;

public class ExternalWebActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.external_web_layout);
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
