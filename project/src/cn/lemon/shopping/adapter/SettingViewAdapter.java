package cn.lemon.shopping.adapter;

import java.util.List;

import cn.lemon.shopping.R;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingViewAdapter extends ArrayAdapter<Pair<Integer, String>> {

	private class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}

	public SettingViewAdapter(Context context, int resource,
			List<Pair<Integer, String>> objects) {
		super(context, resource, objects);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			Pair<Integer, String> pair = getItem(position);
			convertView = View.inflate(getContext(),
					R.layout.image_text_item_layout, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.id_item_image);
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.id_item_text);
			convertView.setTag(viewHolder);
			viewHolder.mImageView.setImageResource(pair.first);
			viewHolder.mTextView.setText(pair.second);
		}

		return convertView;
	}

}
