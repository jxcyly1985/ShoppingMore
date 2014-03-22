package cn.lemon.shopping;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import cn.lemon.bitmap.ImageCache;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.bitmap.ImageWorker;
import android.support.v4.app.FragmentActivity;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-8
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class ImageFetcherManager {

    private Context mContext;

    private final String COMMON_HTTP_DIR = "http";
    // QiYun<LeiYong><2014-03-19> modify for CR00000010 begin
    private final String MALL_HTTP_DIR = "mall_http";
    private final String COMMODITY_HTTP_DIR = "commodity_http";
    private final String VALUE_BUY_HTTP_DIR = "value_buy_http";
    // QiYun<LeiYong><2014-03-19> modify for CR00000010 end
    private final String MALL_DIR_NAME = "MALL";
    private final String COMMODITY_DIR_NAME = "COMMODITY";
    private final String VALUE_BUY_DIR_NAME = "VALUE_BUY";
    private final int MALL_DEFAULT_MEM_CACHE = 10 * 1024 * 1024;
    private final int COMMODITY_ICON_MEM_CACHE = 5 * 1024 * 1024;
    private final int VALUE_BUY_MEM_CACHE = 10 * 1024 * 1024;

    private Map<String, ImageFetcher> mImageFetcherMap;

    private static class ImageFetcherManagerHolder {
        static ImageFetcherManager sInstance = new ImageFetcherManager();
    }

    public static ImageFetcherManager getInstance() {

        return ImageFetcherManagerHolder.sInstance;
    }

    public void init(Context context) {
        mContext = context;

    }

    private ImageFetcherManager() {

        mImageFetcherMap = new HashMap<String, ImageFetcher>();
    }

    public ImageFetcher getMallImageFetcher(FragmentActivity fragmentActivity) {

        int width = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_width);
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_height);
        ImageFetcher imageFetcher = new ImageFetcher(mContext, width, height, MALL_HTTP_DIR);
        imageFetcher.setLoadingImage(R.drawable.default_mall_icon);
        ImageCache.ImageCacheParams imageCacheParams = new ImageCache.ImageCacheParams(mContext, MALL_DIR_NAME);
        imageCacheParams.memCacheSize = MALL_DEFAULT_MEM_CACHE;
        // QiYun<LeiYong><2014-01-11> modify for CR00000003 begin
        // imageCacheParams.setMemCacheSizePercent(0.2f);
        // QiYun<LeiYong><2014-01-11> modify for CR00000003 end
        imageFetcher.addImageCache(fragmentActivity.getSupportFragmentManager(), imageCacheParams);
        mImageFetcherMap.put(MALL_DIR_NAME, imageFetcher);
        return imageFetcher;
    }

    public ImageFetcher getCommodityIconImageFetcher(FragmentActivity fragmentActivity) {

        int width = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_icon_width);
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_commodity_icon_height);
        ImageFetcher imageFetcher = new ImageFetcher(mContext, width, height, COMMODITY_HTTP_DIR);
        //TODO set commodity default icon
        imageFetcher.setLoadingImage(R.drawable.default_mall_icon);
        ImageCache.ImageCacheParams imageCacheParams = new ImageCache.ImageCacheParams(mContext, COMMODITY_DIR_NAME);
        imageCacheParams.memCacheSize = COMMODITY_ICON_MEM_CACHE;
        imageFetcher.addImageCache(fragmentActivity.getSupportFragmentManager(), imageCacheParams);
        mImageFetcherMap.put(COMMODITY_DIR_NAME, imageFetcher);
        return imageFetcher;
    }

    public ImageFetcher getValueBuyImageFetcher(FragmentActivity fragmentActivity) {

        int width = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_value_buy_item_image_width);
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_value_buy_item_image_height);
        ImageFetcher imageFetcher = new ImageFetcher(mContext, width, height, VALUE_BUY_HTTP_DIR);
        //TODO set value buy item default icon
        imageFetcher.setLoadingImage(R.drawable.default_mall_icon);
        ImageCache.ImageCacheParams imageCacheParams = new ImageCache.ImageCacheParams(mContext, VALUE_BUY_DIR_NAME);
        imageCacheParams.memCacheSize = VALUE_BUY_MEM_CACHE;
        imageFetcher.addImageCache(fragmentActivity.getSupportFragmentManager(), imageCacheParams);
        mImageFetcherMap.put(VALUE_BUY_DIR_NAME, imageFetcher);
        return imageFetcher;

    }

    // QiYun<LeiYong><2014-03-19> modify for CR00000009 begin
    public void flush() {

        for (Map.Entry<String, ImageFetcher> entry : mImageFetcherMap.entrySet()) {
            entry.getValue().flushCache();
        }
    }
    // QiYun<LeiYong><2014-03-19> modify for CR00000009 begin


    public void clear() {

        for (Map.Entry<String, ImageFetcher> entry : mImageFetcherMap.entrySet()) {

            entry.getValue().closeCache();
        }
        mImageFetcherMap.clear();
    }


}
