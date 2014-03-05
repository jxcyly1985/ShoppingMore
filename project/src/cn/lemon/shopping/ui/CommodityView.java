package cn.lemon.shopping.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.CommodityItem;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-2-18
 * Time: 下午5:28
 * To change this template use File | Settings | File Templates.
 */
public class CommodityView extends LinearLayout {

    private static final String TAG = "CommodityView";
    public ImageView mCommodityIcon;
    public TextView mCommodityName;

    public CommodityView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        mCommodityIcon = new ImageView(context);
        mCommodityName = new TextView(context);

    }

    public void creator(CommodityItem item) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (item.mImagePos == CommodityItem.IMAGE_LEFT_POS) {
            inflater.inflate(R.layout.commodity_view_title_left_layout, this, true);
        } else {
            inflater.inflate(R.layout.commodity_view_title_right_layout, this, true);
        }
    }

    private class CommodityCategoryNameView extends LinearLayout {
        public CommodityCategoryNameView(Context context) {
            super(context);
        }

    }
}
