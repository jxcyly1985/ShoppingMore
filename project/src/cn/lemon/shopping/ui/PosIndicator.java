package cn.lemon.shopping.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.lemon.shopping.R;
import cn.lemon.utils.DebugUtil;

public class PosIndicator extends ViewGroup {

    private static final String TAG = "PosIndicator";

    private int mItemCount = 0;
    private int mSelectedPos = -1;

    public static final int HORIZONTAL_INDICATOR = 0;
    public static final int VERTICAL_INDICATOR = 1;

    private int mIndicatorOrientation = HORIZONTAL_INDICATOR;
    private int mIndicatorDrawableRes;

    private final int TRANSITION_TIME = 50;
    private Drawable mDrawable;
    private int mDotSpacing = 0;
    private int mDrawableWidth = 0;
    private int mDrawableHeight = 0;

    public PosIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.indicator);
        mIndicatorDrawableRes = typedArray.getResourceId(R.styleable.indicator_dot_res, R.drawable.pos_indicator_res);
        mDrawable = getResources().getDrawable(mIndicatorDrawableRes);
        mDrawableWidth = mDrawable.getIntrinsicWidth();
        mDrawableHeight = mDrawable.getIntrinsicHeight();
        mDotSpacing = typedArray.getDimensionPixelOffset(R.styleable.indicator_dot_spacing, R.dimen.dimen_pos_indicator_dot_spacing);
        mIndicatorOrientation = typedArray.getInteger(R.styleable.indicator_dot_orientation, HORIZONTAL_INDICATOR);

        DebugUtil.debug(TAG, "PosIndicator res 0x" + Integer.toHexString(mIndicatorDrawableRes)
                + " spacing " + mDotSpacing + " orientation " + mIndicatorOrientation);

        typedArray.recycle();
        init();
    }

    protected void init() {
        setFocusable(false);
        setWillNotDraw(false);
    }

    public void setItemCount(int count) {

        DebugUtil.debug(TAG, "setItemCount count " + count);

        if (count != mItemCount) {
            mItemCount = count;
            createLayout();
            // do measure and layout
            requestLayout();
        }
    }

    public void setSelectedPos(int pos) {
        if (pos != mSelectedPos) {
            mSelectedPos = pos;
            updatePos();
        }
    }

    public void setIndicatorType(int orientation) {
        mIndicatorOrientation = orientation;
    }

    public void setIndicatorRes(int resId) {

        mIndicatorDrawableRes = resId;
        mDrawable = getResources().getDrawable(mIndicatorDrawableRes);
        DebugUtil.debug(TAG, "setIndicatorRes drawable width " + mDrawable.getIntrinsicWidth()
                + " height " + mDrawable.getIntrinsicHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        DebugUtil.debug(TAG, "onMeasure widthSpec " + widthSpec + " heightSpec " + heightSpec
                + " widthSpecMode " + widthSpecMode + " heightSpecMode " + heightSpecMode);

        DebugUtil.debug(TAG, "onMeasure drawableWidth " + mDrawableWidth + " drawableHeight " + mDrawableHeight);

        int posIndicatorWidth = 0;
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mDrawableWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mDrawableHeight, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            posIndicatorWidth += mDrawableWidth;
        }
        posIndicatorWidth += ((getChildCount() - 1) * mDotSpacing);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(posIndicatorWidth, MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDrawableHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        DebugUtil.debug(TAG, "onLayout changed " + changed + " l " + l + " t " + t + " r " + r + " b " + b);

        if (mIndicatorOrientation == HORIZONTAL_INDICATOR) {
            horizontalLayout();
            return;
        }

        if (mIndicatorOrientation == VERTICAL_INDICATOR) {
            verticalLayout();
            return;
        }
    }

    protected void horizontalLayout() {

        DebugUtil.debug(TAG, "horizontalLayout");
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            DebugUtil.debug(TAG, "child width " + child.getWidth() + " height " + child.getHeight()
                    + " measureWidth " + child.getMeasuredWidth() + " measureHeight " + child.getMeasuredHeight());
            int left = (mDrawableWidth + mDotSpacing) * i;
            int top = 0;
            int right = left + mDrawableWidth;
            int bottom = mDrawableHeight;
            child.layout(left, top, right, bottom);

        }

    }

    protected void verticalLayout() {

        DebugUtil.debug(TAG, "verticalLayout");
        for (int i = 0; i < getChildCount(); ++i) {
            View child = getChildAt(i);
            DebugUtil.debug(TAG, "child width " + child.getWidth() + " height " + child.getHeight()
                    + " measureWidth " + child.getMeasuredWidth() + " measureHeight " + child.getMeasuredHeight());
            int left = 0;
            int top = (mDrawableHeight + mDotSpacing) * i;
            int right = left + mDrawableWidth;
            int bottom = top + mDrawableHeight;
            child.layout(left, top, right, bottom);

        }

    }

    protected void createLayout() {

        DebugUtil.debug(TAG, "createLayout drawable width " + mDrawable.getIntrinsicWidth()
                + " height " + mDrawable.getIntrinsicHeight());

        detachAllViewsFromParent();
        for (int i = 0; i < mItemCount; ++i) {
            ImageView imageView = new ImageView(getContext());
            // QiYun<LeiYong><2014-01-14> modify for CR00000006 begin
            Drawable drawable = getResources().getDrawable(mIndicatorDrawableRes);
            imageView.setImageDrawable(drawable);
            // QiYun<LeiYong><2014-01-14> modify for CR00000006 end
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addViewInLayout(imageView, getChildCount(), p, true);
        }

    }

    protected void updatePos() {

        // DebugUtil.debug(TAG, "updatePos SelectedPos " + mSelectedPos);
        for (int i = 0; i < getChildCount(); ++i) {
            final ImageView img = (ImageView) getChildAt(i);
            TransitionDrawable tmp = (TransitionDrawable) img.getDrawable();
            if (i == mSelectedPos) {
                tmp.startTransition(TRANSITION_TIME);
            } else {
                tmp.resetTransition();
            }
        }
    }


}
