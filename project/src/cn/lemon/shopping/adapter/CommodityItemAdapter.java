package cn.lemon.shopping.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.shopping.model.CommodityItem;
import cn.lemon.shopping.ui.CommodityView;
import cn.lemon.utils.DebugUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-7
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
public class CommodityItemAdapter extends BaseAdapter {

    private static final String TAG = "CommodityItemAdapter";
    private Context mContext;
    private ImageFetcher mImageFetcher;
    private List<CommodityItem> mCommodityItems;
    private CommodityView.CommodityNameClickListener mCommodityNameClickListener;

    public CommodityItemAdapter(Context context, List<CommodityItem> items, CommodityView.CommodityNameClickListener commodityNameClickListener, ImageFetcher imageFetcher) {
        super();
        mContext = context;
        mCommodityItems = items;
        mCommodityNameClickListener = commodityNameClickListener;
        mImageFetcher = imageFetcher;
    }

    @Override
    public int getCount() {

        DebugUtil.debug(TAG, "mCommodityItems.size " + mCommodityItems.size());

        return mCommodityItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DebugUtil.debug(TAG, "getView position " + position);

        CommodityView commodityView = null;
        CommodityItem item = mCommodityItems.get(position);
        if (convertView == null) {
            commodityView = new CommodityView(mContext);
            commodityView.creator(item);
            commodityView.setCommodityNameClickListener(mCommodityNameClickListener);
        } else {
            commodityView = (CommodityView) convertView;
            commodityView.reset(item);
        }

        DebugUtil.debug(TAG, "CommodityIconUrl " + item.mCommodityIconUrl);
        mImageFetcher.loadImage(item.mCommodityIconUrl, commodityView.getCommodityIconCtrl());

        return commodityView;

    }
}
