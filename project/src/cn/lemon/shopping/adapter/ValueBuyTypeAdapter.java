package cn.lemon.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lemon.shopping.R;
import cn.lemon.shopping.model.ValueBuyTypeInfo;
import cn.lemon.shopping.ui.ValueBuyTypeItemView;
import cn.lemon.utils.DebugUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-22
 * Time: 下午1:54
 * To change this template use File | Settings | File Templates.
 */
public class ValueBuyTypeAdapter extends BaseAdapter {

    private static final String TAG = "ValueBuyTypeAdapter";

    private Context mContext;
    private List<ValueBuyTypeInfo> mValueBuyTypeInfoList;

    private class ViewHolder {
        TextView mTypeName;
        ImageView mTypeDivider;
    }

    public ValueBuyTypeAdapter(Context context, List<ValueBuyTypeInfo> list) {
        mContext = context;
        mValueBuyTypeInfoList = list;
    }

    @Override
    public int getCount() {

        DebugUtil.debug(TAG, "getCount " + mValueBuyTypeInfoList.size());
        return mValueBuyTypeInfoList.size();
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

        ValueBuyTypeInfo valueBuyTypeInfo = mValueBuyTypeInfoList.get(position);
        View typeView = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            typeView = LayoutInflater.from(mContext).inflate(R.layout.value_buy_type_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.mTypeName = (TextView) typeView.findViewById(R.id.id_value_buy_type_name);
            viewHolder.mTypeDivider = (ImageView) typeView.findViewById(R.id.id_value_buy_type_divider);
            typeView.setTag(viewHolder);
        } else {
            typeView = convertView;
            viewHolder = (ViewHolder) typeView.getTag();
        }

        viewHolder.mTypeName.setText(valueBuyTypeInfo.mTypeName);

        return typeView;
    }
}
