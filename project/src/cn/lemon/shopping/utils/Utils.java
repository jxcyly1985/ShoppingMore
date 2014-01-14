package cn.lemon.shopping.utils;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.lemon.shopping.R;
import cn.lemon.utils.DebugUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-24
 * Time: 下午9:28
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    private static final String TAG = "Utils";

    private static final int IO_BUFFER_SIZE = 8 * 1024;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        final Resources resources = gridView.getContext().getResources();
        final int numColumns = resources.getInteger(R.integer.integer_category_item_columns);
        final int itemHeight = resources.getDimensionPixelOffset(R.dimen.dimen_mall_grid_item_height);
        final int verticalSpacing = gridView.getVerticalSpacing();
        final int itemCount = listAdapter.getCount();
        int totalHeight = 0;
        for (int i = 0; i < itemCount; i += numColumns) {
            totalHeight += itemHeight;
            if (i + numColumns < itemCount) {
                totalHeight += verticalSpacing;
            }
        }
        final ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public static  boolean downloadUrlToStream(String urlString, OutputStream outputStream) {

        DebugUtil.debug(TAG, "downloadUrlToStream");

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            DebugUtil.debug(TAG, "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
            }
        }
        return false;
    }
}
