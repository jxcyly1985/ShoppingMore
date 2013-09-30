package cn.lemon.shopping.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ContentViewPagerAdapter extends PagerAdapter {

    private List<String> mTagList;

    public ContentViewPagerAdapter(List<String> taglist) {

        super();
        mTagList = taglist;
    }

    @Override
    public int getCount() {

        return mTagList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        return super.instantiateItem(container, position);
    }
}
