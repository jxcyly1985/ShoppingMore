package cn.lemon.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.ValueBuyItemInfo;
import cn.lemon.utils.DebugUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-22
 * Time: 下午2:18
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyItemAdapter extends BaseAdapter {

    private static final String TAG = "ValueBuyItemAdapter";

    private Context mContext;
    private List<ValueBuyItemInfo> mValueBuyItemInfos;
    private ImageFetcher mImageFetcher;
    private int mItemWidth;
    private int mItemHeight;

    private class ViewHolder {

        private ImageView mItemImage;
        private ImageView mFavorite;
        private TextView mItemPrice;
        private TextView mItemTitle;

    }

    public ValueBuyItemAdapter(Context context, List<ValueBuyItemInfo> list, ImageFetcher imageFetcher) {

        mContext = context;
        mValueBuyItemInfos = list;
        mImageFetcher = imageFetcher;
        mItemWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_value_buy_item_width);
        mItemHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_value_buy_item_height);
    }

    @Override
    public int getCount() {

        DebugUtil.debug(TAG, "getCount " + mValueBuyItemInfos.size());

        return mValueBuyItemInfos.size();
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
        View itemView = null;
        ViewHolder viewHolder = null;
        ValueBuyItemInfo valueBuyItemInfo = mValueBuyItemInfos.get(position);

        DebugUtil.debug(TAG, "getView valueBuyItemInfo " + valueBuyItemInfo);

        if (convertView == null) {

            itemView = LayoutInflater.from(mContext).inflate(R.layout.value_buy_commodity_item_layout, null);
            AbsListView.LayoutParams layoutParams =
                    new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mItemHeight);
            itemView.setLayoutParams(layoutParams);
            viewHolder = new ViewHolder();
            viewHolder.mItemImage = (ImageView) itemView.findViewById(R.id.id_value_buy_item_image);
            viewHolder.mFavorite = (ImageView) itemView.findViewById(R.id.id_value_buy_item_favorite);
            viewHolder.mItemPrice = (TextView) itemView.findViewById(R.id.id_value_buy_item_price);
            viewHolder.mItemTitle = (TextView) itemView.findViewById(R.id.id_value_buy_item_name);
            itemView.setTag(viewHolder);
        } else {
            itemView = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.mItemPrice.setText(valueBuyItemInfo.mPrice);
        viewHolder.mItemTitle.setText(valueBuyItemInfo.mTitle);
        mImageFetcher.loadImage(valueBuyItemInfo.mImageUrl, viewHolder.mItemImage);


        return itemView;
    }
}
