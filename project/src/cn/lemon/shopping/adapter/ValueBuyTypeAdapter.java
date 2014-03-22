package cn.lemon.shopping.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        ValueBuyTypeItemView itemView = null;
        if (convertView == null) {
            itemView = new ValueBuyTypeItemView(mContext);
        } else {
            itemView = (ValueBuyTypeItemView) convertView;
        }

        itemView.setTypeName(valueBuyTypeInfo.mTypeName);

        return itemView;
    }
}
