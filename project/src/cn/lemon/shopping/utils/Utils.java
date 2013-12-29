package cn.lemon.shopping.utils;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.lemon.shopping.R;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-24
 * Time: 下午9:28
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView)
    {
        ListAdapter listAdapter = gridView.getAdapter();

        if (listAdapter == null)
        {
            // pre-condition
            return;
        }
        Resources resources = gridView.getContext().getResources();
        int numColumns = 4;
        int itemHeight =  resources.getDimensionPixelOffset(R.dimen.dimen_mall_grid_item_height);
        int verticalSpacing = resources.getDimensionPixelOffset(R.dimen.dimen_mall_grid_v_spacing);
        Rect rect = new Rect();
        gridView.getSelector().getPadding(rect);
        int selectorHeight = rect.height();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i+=numColumns)
        {
            totalHeight += (itemHeight + verticalSpacing + 15);
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}
