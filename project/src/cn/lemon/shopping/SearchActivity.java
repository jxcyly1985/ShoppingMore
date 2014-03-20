package cn.lemon.shopping;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import cn.lemon.framework.BaseActivity;

import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-19
 * Time: 下午8:40
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";

    private ImageView mBackImage;
    private ImageView mSearchImage;
    private ImageView mSearchCancel;
    private ListView mSearchListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page_layout);
        initView();
        initData();
    }

    private void initView() {

        mBackImage = (ImageView) findViewById(R.id.id_back_arrow);
        mSearchImage = (ImageView) findViewById(R.id.id_search_start);
        mSearchCancel = (ImageView) findViewById(R.id.id_search_cancel);
        mSearchListView = (ListView) findViewById(R.id.id_search_list_view);

        mBackImage.setOnClickListener(this);
        mSearchImage.setOnClickListener(this);
        mSearchCancel.setOnClickListener(this);


    }

    private void initData() {

        mSearchListView.setAdapter(null);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.id_back_arrow:
                break;
            case R.id.id_search_start:
                break;
            case R.id.id_search_cancel:
                break;
        }

    }
}
