package cn.lemon.shopping;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import cn.lemon.bitmap.ImageCache;
import cn.lemon.bitmap.ImageFetcher;
import cn.lemon.bitmap.ImageWorker;
import android.support.v4.app.FragmentActivity;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-8
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class ImageFetcherManager {

    private Context mContext;

    private final String MALL_DIR_NAME = "MALL";
    private final String AD_DIR_NAME = "AD";

    private static class  ImageFetcherManagerHolder {
        static ImageFetcherManager sInstance = new ImageFetcherManager();
    }

    public static ImageFetcherManager getInstance(){

        return ImageFetcherManagerHolder.sInstance;
    }

    public void init(Context context){
        mContext = context;

    }

    private ImageFetcherManager(){

    }


    public ImageFetcher getAdImageFetcher(FragmentActivity fragmentActivity){
        int widthPixel = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = widthPixel * 9 / 16;

        ImageFetcher imageFetcher = new ImageFetcher(mContext,widthPixel, height);
        ImageCache.ImageCacheParams imageCacheParams = new ImageCache.ImageCacheParams(mContext, AD_DIR_NAME);
        //imageCacheParams.setMemCacheSizePercent(0.1f);
        imageFetcher.addImageCache(fragmentActivity.getSupportFragmentManager(), imageCacheParams);

        return imageFetcher;

    }

    public ImageFetcher getMallImageFetcher(FragmentActivity fragmentActivity){

        int width = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_width);
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_height);
        ImageFetcher imageFetcher = new ImageFetcher(mContext,width, height);
        ImageCache.ImageCacheParams imageCacheParams = new ImageCache.ImageCacheParams(mContext, MALL_DIR_NAME);
        //imageCacheParams.setMemCacheSizePercent(0.2f);
        imageFetcher.addImageCache(fragmentActivity.getSupportFragmentManager(), imageCacheParams);

        return imageFetcher;
    }


}
