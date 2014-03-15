package cn.lemon.shopping.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.CommodityCategory;
import cn.lemon.shopping.model.CommodityItem;
import cn.lemon.utils.DebugUtil;
import cn.lemon.utils.StaticUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-2-18
 * Time: 下午5:28
 * To change this template use File | Settings | File Templates.
 */
public class CommodityView extends LinearLayout {

    private static final String TAG = "CommodityView";
    private ImageView mCommodityIcon;
    private TextView mCommodityName;
    private CommodityCategoryNameView mCommodityCategoryNameView;
    private CommodityItem mCommodityItem;
    private CommodityNameClickListener mListener;
    private Map<String, String> mCommodityItemLinkMap;

    public CommodityView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setCommodityNameClickListener(CommodityNameClickListener listener) {
        mListener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                DebugUtil.debug(TAG, "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                DebugUtil.debug(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                processMove(x, y);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void creator(CommodityItem item) {

        mCommodityItem = item;
        int pageColor = getResources().getColor(R.color.page_color);
        setBackgroundColor(pageColor);

        DebugUtil.debug(TAG, "creator mCommodityItem " + mCommodityItem.mHasTopSide);

        int vPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_v_padding);

        ListView.LayoutParams listLayoutParams = new ListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(listLayoutParams);

        setPadding(0, 0, 0, vPadding);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (mCommodityItem.mHasTopSide) {
            ImageView topImageView = new ImageView(getContext());
            topImageView.setImageResource(R.drawable.category_item_top_side);
            topImageView.setBackgroundColor(pageColor);
            addViewInLayout(topImageView, getChildCount(), layoutParams, false);
        }

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View title = null;
        if (item.mImagePos == CommodityItem.IMAGE_LEFT_POS) {
            title = layoutInflater.inflate(R.layout.commodity_view_title_left_layout, null);
        } else {
            title = layoutInflater.inflate(R.layout.commodity_view_title_right_layout, null);
        }

        int leftPadding = title.getPaddingLeft();
        int topPadding = title.getPaddingTop();
        int rightPadding = title.getPaddingRight();
        int bottomPadding = title.getPaddingBottom();
        title.setBackgroundResource(R.drawable.category_bg);
        title.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        addViewInLayout(title, getChildCount(), layoutParams, true);

        mCommodityIcon = (ImageView) title.findViewById(R.id.id_commodity_icon);
        mCommodityName = (TextView) title.findViewById(R.id.id_commodity_name);

        int textBgColor = StaticUtils.getInstance().getColor(mCommodityItem.mCommodityNameColor);
        mCommodityName.setBackgroundColor(textBgColor);
        mCommodityName.setText(item.mCommodityName);

        mCommodityCategoryNameView = new CommodityCategoryNameView(getContext());
        mCommodityCategoryNameView.calc();
        mCommodityCategoryNameView.setBackgroundResource(R.drawable.category_bg);
        addViewInLayout(mCommodityCategoryNameView, getChildCount(), layoutParams, true);

        ImageView bottomImageView = new ImageView(getContext());
        bottomImageView.setImageResource(R.drawable.category_item_bottom_side);
        bottomImageView.setBackgroundColor(pageColor);
        addViewInLayout(bottomImageView, getChildCount(), layoutParams, false);

    }

    public void reset(CommodityItem item) {

        mCommodityItem = item;
        mCommodityName.setText(item.mCommodityName);
        mCommodityIcon.setImageDrawable(null);
        mCommodityCategoryNameView.calc();
        requestLayout();

    }


    // QiYun<LeiYong><2014-03-12> modify for CR00000008 begin
    private void processMove(float x, float y) {

        Rect rect = new Rect(mCommodityCategoryNameView.getLeft(), mCommodityCategoryNameView.getTop(),
                mCommodityCategoryNameView.getRight(), mCommodityCategoryNameView.getBottom());

        DebugUtil.debug(TAG, "processMove " + rect.toString());

        if (!rect.contains((int) x, (int) y)) {
            mCommodityCategoryNameView.invalidate();
        }
    }
    // QiYun<LeiYong><2014-03-12> modify for CR00000008 end


    public TextView getCommodityNameCtrl() {
        return mCommodityName;
    }

    public ImageView getCommodityIconCtrl() {
        return mCommodityIcon;
    }


    public interface CommodityNameClickListener {

        public void onClick(String name, String linkUrl);

    }

    private void handleCommodityNameClick(String commodityName) {

        String link = mCommodityItemLinkMap.get(commodityName);
        mListener.onClick(commodityName, link);

    }

    private class CommodityCategoryNameView extends View {

        private int mTextSize;
        private int mItemHeight;
        private int mItemHSpacing;
        private int mItemVSpacing;
        private int mItemHorizontalPadding;
        private int mItemVerticalPadding;
        private int mItemTopMarginV;
        private int mItemBottomMarginV;
        private int mItemMarginH;
        private int mStrokeWidth;
        private int mTotalWidth;
        private int mDrawaHeight;
        private float mTextBaseLineDiff;
        private boolean mShowBounds;
        private String mDownClickName;
        private ArrayList<List<String>> mCommodityVerticalList;
        private ArrayList<Float> mItemPaddingArray;
        private Map<String, RectF> mItemRectMap;
        private Map<String, Float> mItemStringWidthMap;
        private Map<String, Integer> mCommodityItemColorMap;
        private Rect mInvalidateItemRect = new Rect();
        private Paint.FontMetrics mFontMetrics;

        public CommodityCategoryNameView(Context context) {
            super(context);
            setClickable(true);
            init();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpec = MeasureSpec.getSize(heightMeasureSpec);
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            DebugUtil.debug(TAG, "onMeasure widthSpec " + widthSpec + " heightSpec " + heightSpec
                    + " widthSpecMode " + widthSpecMode + " heightSpecMode " + heightSpecMode);

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDrawaHeight, MeasureSpec.EXACTLY);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    DebugUtil.debug(TAG, "ACTION_DOWN");
                    commodityItemProcessDown(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    DebugUtil.debug(TAG, "ACTION_UP");
                    commodityItemProcessUp(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    commodityItemProcessMove(x, y);
                    break;
                // QiYun<LeiYong><2014-03-12> modify for CR00000008 begin
                case MotionEvent.ACTION_CANCEL:
                    DebugUtil.debug(TAG, "ACTION_CANCEL");
                    commodityItemProcessCancel(x, y);
                    break;
                // QiYun<LeiYong><2014-03-12> modify for CR00000008 end
                case MotionEvent.ACTION_OUTSIDE:
                    DebugUtil.debug(TAG, "ACTION_OUTSIDE");
                    break;
            }
            return super.onTouchEvent(event);
        }

        protected void commodityItemProcessDown(float x, float y) {

            for (Map.Entry<String, RectF> entry : mItemRectMap.entrySet()) {
                RectF rectF = entry.getValue();
                if (rectF.contains(x, y)) {

                    DebugUtil.debug(TAG, "commodityItemProcessDown contains X, Y");

                    mDownClickName = entry.getKey();
                    mInvalidateItemRect.left = (int) (rectF.left - mStrokeWidth);
                    mInvalidateItemRect.top = (int) (rectF.top - mStrokeWidth);
                    mInvalidateItemRect.right = (int) (rectF.right + mStrokeWidth);
                    mInvalidateItemRect.bottom = (int) (rectF.bottom + mStrokeWidth);
                    postInvalidate(mInvalidateItemRect.left, mInvalidateItemRect.top,
                            mInvalidateItemRect.right, mInvalidateItemRect.bottom);
                    break;
                }
            }


        }

        protected void commodityItemProcessUp(float x, float y) {

            for (Map.Entry<String, RectF> entry : mItemRectMap.entrySet()) {
                String name = entry.getKey();
                RectF rectF = entry.getValue();
                if (rectF.contains(x, y)) {
                    if (name.equals(mDownClickName)) {
                        handleCommodityNameClick(mDownClickName);
                    }
                    break;
                }
            }
            postInvalidate(mInvalidateItemRect.left - mStrokeWidth, mInvalidateItemRect.top - mStrokeWidth,
                    mInvalidateItemRect.right + mStrokeWidth, mInvalidateItemRect.bottom + mStrokeWidth);
            mInvalidateItemRect.setEmpty();
            mDownClickName = null;

        }

        private void commodityItemProcessMove(float x, float y) {

            if (mInvalidateItemRect.contains((int) x, (int) y)) {
                DebugUtil.debug(TAG, "commodityItemProcessMove mShowBounds TRUE");
                mShowBounds = true;
            } else {
                DebugUtil.debug(TAG, "commodityItemProcessMove mShowBounds FALSE");
                mShowBounds = false;
            }

            postInvalidate(mInvalidateItemRect.left - mStrokeWidth, mInvalidateItemRect.top - mStrokeWidth,
                    mInvalidateItemRect.right + mStrokeWidth, mInvalidateItemRect.bottom + mStrokeWidth);

        }

        private void commodityItemProcessCancel(float x, float y) {

            mInvalidateItemRect.setEmpty();
            mDownClickName = null;
            mShowBounds = false;
            postInvalidate(mInvalidateItemRect.left - mStrokeWidth, mInvalidateItemRect.top - mStrokeWidth,
                    mInvalidateItemRect.right + mStrokeWidth, mInvalidateItemRect.bottom + mStrokeWidth);
        }

        private void clear() {
            mDrawaHeight = 0;
        }

        private void init() {

            Resources resources = getResources();
            mItemTopMarginV = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_category_name_margin_top_v);
            mItemBottomMarginV = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_category_name_margin_bottom_v);
            mTextSize = resources.getDimensionPixelSize(R.dimen.dimen_commodity_item_text_size);
            mItemHeight = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_item_name_height);
            mItemMarginH = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_category_name_margin_h);
            mItemHSpacing = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_item_h_spacing);
            mItemVSpacing = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_item_v_spacing);
            mItemHorizontalPadding = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_item_h_padding_min);
            mItemVerticalPadding = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_item_v_padding_min);
            mStrokeWidth = resources.getDimensionPixelOffset(R.dimen.dimen_commodity_stroke_width);
            mTotalWidth = resources.getDisplayMetrics().widthPixels - 2 * mItemMarginH;

            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);

            mFontMetrics = paint.getFontMetrics();
            DebugUtil.debug(TAG, "top : " + mFontMetrics.top + " bottom : " + mFontMetrics.bottom
                    + " ascent:" + mFontMetrics.ascent + " decent : " + mFontMetrics.descent);

            mTextBaseLineDiff = mItemHeight / 2 - mFontMetrics.descent +
                    (mFontMetrics.bottom - mFontMetrics.top) / 2;


        }

        private void calc() {

            clear();
            mDrawaHeight += mItemTopMarginV;
            DebugUtil.debug(TAG, "calc totalWidth " + mTotalWidth);

            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);

            mCommodityVerticalList = new ArrayList<List<String>>();
            mItemPaddingArray = new ArrayList<Float>();
            List<String> RowList = new ArrayList<String>();
            mItemStringWidthMap = new HashMap<String, Float>();
            mCommodityItemLinkMap = new HashMap<String, String>();
            mCommodityItemColorMap = new HashMap<String, Integer>();
            float lineWidth = 0;
            List<CommodityCategory> commodityCategoryList = mCommodityItem.mCommodityCategoryList;
            int count = commodityCategoryList.size();
            int rowCount = 1;
            for (int i = 0; i < count; ++i) {
                CommodityCategory item = commodityCategoryList.get(i);
                String commodityName = item.mCommodityCategoryName;
                float textWidth = paint.measureText(commodityName, 0, commodityName.length());
                float itemWidth = textWidth + 2 * mItemHorizontalPadding;

                DebugUtil.debug(TAG, "calc textWidth " + textWidth);
                DebugUtil.debug(TAG, "calc itemWidth " + itemWidth);

                mItemStringWidthMap.put(commodityName, textWidth);
                mCommodityItemLinkMap.put(commodityName, item.mCommodityCategoryLink);
                if (item.mCommodityColor != null) {
                    mCommodityItemColorMap.put(commodityName,
                            StaticUtils.getInstance().getColor(item.mCommodityColor));
                }

                lineWidth += itemWidth;
                lineWidth = lineWidth + (i == 0 ? 0 : mItemHSpacing);
                if (lineWidth <= mTotalWidth) {
                    RowList.add(commodityName);
                } else {
                    float remainSize = mTotalWidth - lineWidth - itemWidth;
                    float theNewPadding = remainSize / (i * 2);
                    mItemPaddingArray.add(theNewPadding);
                    mCommodityVerticalList.add(RowList);
                    RowList = new ArrayList<String>();
                    lineWidth = itemWidth;
                    rowCount++;
                }

                if (i == (count - 1)) {
                    mCommodityVerticalList.add(RowList);
                    mItemPaddingArray.add((float) mItemHorizontalPadding);
                    int itemTotalHeight = rowCount * mItemHeight + (rowCount - 1) * mItemVSpacing;
                    mDrawaHeight += itemTotalHeight;

                }
            }

            DebugUtil.debug(TAG, "RowList " + RowList.size());

            mDrawaHeight += mItemBottomMarginV;

            DebugUtil.debug(TAG, "mCommodityVerticalList " + mCommodityVerticalList.size());
        }


        @Override
        protected void onDraw(Canvas canvas) {

            DebugUtil.debug(TAG, "onDraw");

            super.onDraw(canvas);

            int itemColor = getResources().getColor(R.color.white);
            int textColor = getResources().getColor(R.color.commodity_item_name_color);
            int selectBoundsColor = getResources().getColor(R.color.commodity_item_select_bounds_color);
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);

            mItemRectMap = new HashMap<String, RectF>();

            int lineCount = mCommodityVerticalList.size();
            for (int i = 0; i < lineCount; ++i) {
                List<String> itemList = mCommodityVerticalList.get(i);
                float left = 0;
                float right = 0;
                float textLeft = 0;
                float top = i * (mItemHeight + mItemVSpacing) + mItemTopMarginV;
                float bottom = top + mItemHeight;

                float textTop = top + mTextBaseLineDiff;

                float itemPadding = mItemPaddingArray.get(i);
                int columnCount = itemList.size();
                int itemBgColor = 0;
                float itemWidth = 0;
                for (int j = 0; j < columnCount; j++) {
                    left = mItemMarginH + itemWidth;
                    left = left + (j == 0 ? 0 : mItemHSpacing);
                    String itemName = itemList.get(j);
                    itemWidth = 2 * itemPadding + mItemStringWidthMap.get(itemName);
                    right = left + itemWidth;
                    textLeft = left + itemPadding;
                    Integer color = mCommodityItemColorMap.get(itemName);
                    itemBgColor = color == null ? itemColor : color;
                    paint.setColor(itemBgColor);
                    paint.setStrokeWidth(0);

                    DebugUtil.debug(TAG, "onDraw itemName " + itemName + " itemWidth " + itemWidth
                            + " left " + left + " top " + top + " right " + right + " bottom " + bottom);

                    canvas.drawRect(left, top, right, bottom, paint);
                    paint.setColor(textColor);
                    canvas.drawText(itemName, textLeft, textTop, paint);
                    if (itemName.equals(mDownClickName) && mShowBounds) {
                        paint.setColor(selectBoundsColor);
                        paint.setStrokeWidth(mStrokeWidth);
                        float[] points = new float[]{
                                mInvalidateItemRect.left, mInvalidateItemRect.top,
                                mInvalidateItemRect.right, mInvalidateItemRect.top,
                                mInvalidateItemRect.right, mInvalidateItemRect.top,
                                mInvalidateItemRect.right, mInvalidateItemRect.bottom,
                                mInvalidateItemRect.right, mInvalidateItemRect.bottom,
                                mInvalidateItemRect.left, mInvalidateItemRect.bottom,
                                mInvalidateItemRect.left, mInvalidateItemRect.bottom,
                                mInvalidateItemRect.left, mInvalidateItemRect.top};
                        canvas.drawLines(points, paint);
                    }
                    RectF rect = new RectF(left, top, right, bottom);
                    mItemRectMap.put(itemName, rect);

                }
            }
        }


    }
}
