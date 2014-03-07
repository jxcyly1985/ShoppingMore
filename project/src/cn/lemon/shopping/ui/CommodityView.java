package cn.lemon.shopping.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.CommodityCategory;
import cn.lemon.shopping.model.CommodityItem;

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
    private CommodityItem mCommodityItem;
    private CommodityNameClickListener mListener;
    private Map<String, String> mCommodityItemLinkMap;

    public CommodityView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.category_bg);
    }

    public void setCommodityNameClickListener(CommodityNameClickListener listener) {
        mListener = listener;
    }

    public void creator(CommodityItem item) {

        mCommodityItem = item;
        int hPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_h_padding);
        int vPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_v_padding);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (mCommodityItem.mHasTopSide) {
            ImageView topImageView = new ImageView(getContext());
            topImageView.setImageResource(R.drawable.category_item_top__side);
            addViewInLayout(topImageView, getChildCount(), layoutParams, false);
            setPadding(hPadding, 0, hPadding, vPadding);
        } else {
            setPadding(hPadding, vPadding, hPadding, vPadding);
        }


        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View title = null;
        if (item.mImagePos == CommodityItem.IMAGE_LEFT_POS) {
            title = layoutInflater.inflate(R.layout.commodity_view_title_left_layout, null);
        } else {
            title = layoutInflater.inflate(R.layout.commodity_view_title_right_layout, null);
        }

        addViewInLayout(title, getChildCount(), layoutParams, true);

        mCommodityIcon = (ImageView) title.findViewById(R.id.id_commodity_icon);
        mCommodityName = (TextView) title.findViewById(R.id.id_commodity_name);

        CommodityCategoryNameView commodityCategoryNameView = new CommodityCategoryNameView(getContext());
        commodityCategoryNameView.calc();
        addViewInLayout(commodityCategoryNameView, getChildCount(), layoutParams, true);

        ImageView bottomImageView = new ImageView(getContext());
        bottomImageView.setImageResource(R.drawable.category_item_bottom_side);
        addViewInLayout(bottomImageView, getChildCount(), layoutParams, false);

    }

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
        private String mDownClickName;
        private ArrayList<List<String>> mCommodityOrderList;
        private ArrayList<Float> mItemPaddingArray;
        private Map<String, RectF> mItemRectMap;

        private Map<String, Float> mItemStringWidthMap;

        public CommodityCategoryNameView(Context context) {
            super(context);
        }

        private void calc() {

            mTextSize = getResources().getDimensionPixelSize(R.dimen.dimen_commodity_item_text_size);
            mItemHeight = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_item_name_height);
            int marginLeft = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_margin_horizontal);
            mItemHSpacing = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_item_h_spacing);
            mItemVSpacing = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_item_v_spacing);
            mItemHorizontalPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_item_h_padding_min);
            mItemVerticalPadding = getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_item_v_padding_min);
            int totalWidth = getResources().getDisplayMetrics().widthPixels - 2 * marginLeft;


            Paint paint = new Paint();
            paint.setTextSize(mTextSize);

            mCommodityOrderList = new ArrayList<List<String>>();
            mItemPaddingArray = new ArrayList<Float>();
            List<String> RowList = new ArrayList<String>();
            mItemStringWidthMap = new HashMap<String, Float>();
            mCommodityItemLinkMap = new HashMap<String, String>();
            float lineWidth = 0;
            List<CommodityCategory> commodityCategoryList = mCommodityItem.mCommodityCategoryList;
            int count = commodityCategoryList.size();
            for (int i = 0; i < count; ++i) {
                CommodityCategory item = commodityCategoryList.get(i);
                String commodityName = item.mCommodityCategoryName;
                float textWidth = paint.measureText(commodityName, 0, commodityName.length());
                float itemWidth = textWidth + i * 2 * mItemHorizontalPadding;
                mItemStringWidthMap.put(commodityName, textWidth);
                mCommodityItemLinkMap.put(commodityName, item.mCommodityCategoryLink);
                lineWidth += itemWidth;
                if (lineWidth <= totalWidth) {
                    RowList.add(commodityName);
                } else {
                    float remainSize = totalWidth - lineWidth - itemWidth;
                    float theNewPadding = remainSize / (i * 2);
                    mItemPaddingArray.add(theNewPadding);
                    mCommodityOrderList.add(RowList);
                    RowList = new ArrayList<String>();
                    lineWidth = itemWidth;
                }

            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    processDown(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    processUp(x, y);
                    break;
            }
            return super.onTouchEvent(event);
        }

        protected void processDown(float x, float y) {

            for (Map.Entry<String, RectF> entry : mItemRectMap.entrySet()) {
                RectF rectF = entry.getValue();
                if (rectF.contains(x, y)) {
                    mDownClickName = entry.getKey();
                    int left = (int) (rectF.left + 0.5f);
                    int top = (int) (rectF.top + 0.5f);
                    int right = (int) (rectF.right + 0.5f);
                    int bottom = (int) (rectF.bottom + 0.5f);
                    postInvalidate(left, top, right, bottom);
                    break;
                }
            }


        }

        protected void processUp(float x, float y) {

            for (Map.Entry<String, RectF> entry : mItemRectMap.entrySet()) {
                RectF rectF = entry.getValue();
                String name = entry.getKey();
                if (rectF.contains(x, y)) {
                    if (name.equals(mDownClickName)) {
                        handleCommodityNameClick(entry.getKey());
                    }
                    break;
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int itemColor = getResources().getColor(R.color.white);
            int textColor = getResources().getColor(R.color.commodity_item_name_color);
            int selectBoundsColor = getResources().getColor(R.color.commodity_item_select_bounds_color);
            Paint paint = new Paint();
            paint.setTextSize(mTextSize);
            paint.setAntiAlias(true);

            mItemRectMap = new HashMap<String, RectF>();

            int lineCount = mCommodityOrderList.size();
            for (int i = 0; i < lineCount; ++i) {
                List<String> itemList = mCommodityOrderList.get(i);
                float left = 0;
                float right = 0;
                float textLeft = 0;
                float textTop = 0;
                float top = i * (mItemHeight + mItemVSpacing);
                float bottom = top + mItemHeight;
                float itemPadding = mItemPaddingArray.get(i);
                int columnCount = itemList.size();
                for (int j = 0; j < columnCount; j++) {
                    String itemName = itemList.get(j);
                    float itemWidth = 2 * itemPadding + mItemStringWidthMap.get(itemName) + mItemHSpacing;
                    left = j * itemWidth;
                    right = left + itemWidth;
                    textLeft = left + mItemHorizontalPadding;
                    textTop = top + mItemVerticalPadding;
                    paint.setColor(itemColor);
                    canvas.drawRect(left, top, right, bottom, paint);
                    if (itemName.equals(mDownClickName)) {
                        paint.setColor(selectBoundsColor);
                        float[] points = new float[]{left, top, right, top, right, bottom, left, bottom};
                        canvas.drawLines(points, paint);
                    }
                    paint.setColor(textColor);
                    canvas.drawText(itemName, textLeft, textTop, paint);
                    RectF rect = new RectF(left, top, right, bottom);
                    mItemRectMap.put(itemName, rect);

                }
            }
        }


    }
}
