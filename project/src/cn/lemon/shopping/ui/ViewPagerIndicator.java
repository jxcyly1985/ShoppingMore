package cn.lemon.shopping.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ViewPagerIndicator extends LinearLayout {

    private int mItemCount = 0;
    private int mSelectedPos = 0;

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setItemCount(int count){
        mItemCount = count;
    }

    public void setSelectedPos(int pos){
        mSelectedPos = pos;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
    }




}
