package cn.lemon.shopping.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ContentViewPagerAdapter extends PagerAdapter {

    private List<View> mViews;

    public ContentViewPagerAdapter(List<View> views) {

        super();
        mViews = views;
    }

    @Override
    public int getCount() {

        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = mViews.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = mViews.get(position);
        container.removeView(view);

    }
}
