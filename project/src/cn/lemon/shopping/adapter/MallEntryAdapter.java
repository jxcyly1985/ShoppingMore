package cn.lemon.shopping.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.MallEntryInfo;

import java.util.List;

public class MallEntryAdapter extends BaseAdapter {

    private Context mContext;
    private List<MallEntryInfo> mMallEntryInfos;
    private ImageFetcher mImageFetcher;
    private int mImageWidth;
    private int mImageHeight;

    private class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }

    public MallEntryAdapter(Context context, List<MallEntryInfo> mallEntryInfos, ImageFetcher imageFetcher) {

        mContext = context;
        mMallEntryInfos = mallEntryInfos;
        mImageFetcher = imageFetcher;
    }

    @Override
    public int getCount() {

        return mMallEntryInfos.size();
    }

    @Override
    public Object getItem(int position) {

        return mMallEntryInfos.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MallEntryInfo mallEntryInfo = mMallEntryInfos.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_text_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.id_item_image);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.id_item_text);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.mTextView.setText(mallEntryInfo.mName);
        mImageFetcher.loadImage(mallEntryInfo.mImageUrl, viewHolder.mImageView);

        return convertView;
    }

}
