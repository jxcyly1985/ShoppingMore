package cn.lemon.shopping.adapter;

import java.util.List;

import cn.lemon.shopping.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingViewAdapter extends ArrayAdapter<String> {

	private class ViewHolder{
		ImageView mImageView;
		TextView mTextView;
	}
	
	public SettingViewAdapter(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = View.inflate(getContext(), R.layout.image_text_item_layout, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView)convertView.findViewById(R.id.id_item_image);
			viewHolder.mTextView = (TextView) convertView.findViewById(R.id.id_item_text);
			convertView.setTag(viewHolder);
			viewHolder.mImageView.setImageResource(android.R.drawable.ic_input_add);
			viewHolder.mTextView.setText("测试");
		}
		
		return convertView;
	}
	
}
