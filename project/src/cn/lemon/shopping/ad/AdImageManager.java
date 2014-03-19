package cn.lemon.shopping.ad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import cn.lemon.bitmap.AsyncTask;
import cn.lemon.bitmap.ImageCache;
import cn.lemon.bitmap.ImageResizer;
import cn.lemon.framework.FramewokUtils;
import cn.lemon.framework.MessageManager;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.shopping.R;
import cn.lemon.shopping.utils.Utils;
import cn.lemon.utils.DebugUtil;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-15
 * Time: 下午1:08
 * To change this template use File | Settings | File Templates.
 */
public class AdImageManager {


    private static final String TAG = "AdImageManager";

    private ThreadPoolExecutor mImageThreadPoolExecutor;
    private static final String AD_DIR = "AD";
    private boolean mInit = false;
    private Context mContext;
    private File mAdDir;
    private int mImageWidth;
    private int mImageHeight;

    public boolean mAdCanChange = true;
    public LruCache<String, BitmapDrawable> mMemoryCache;

    public static int AD_MAX_CACHE_SIZE = 5 * 1024 * 1024;

    private String[] mAdImageUrls;

    private Set<String> mImageUrlCleaner;


    private static class AdImageManagerHolder {
        static AdImageManager sInstance = new AdImageManager();
    }

    public static AdImageManager getInstance() {

        return AdImageManagerHolder.sInstance;

    }

    public void init(Context context) {

        DebugUtil.debug(TAG, "init");

        if (!mInit) {
            mContext = context;
            mImageWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_width);
            mImageHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.dimen_mall_item_image_height);
            mAdDir = ImageCache.getDiskCacheDir(mContext, AD_DIR);
            // QiYun<LeiYong><2014-01-11> modify for CR000000002 begin
            if (!mAdDir.exists()) {
                mAdDir.mkdirs();
            }
            // QiYun<LeiYong><2014-01-11> modify for CR000000002 end
            mInit = true;
        }
    }

    public AdImageInfo getAdImageInfo() {

        AdImageInfo adImageInfo = new AdImageInfo();

        return adImageInfo;
    }

    public void onPause() {

        setAdCanChange(false);
    }

    public void onResume() {
        setAdCanChange(true);
    }

    public void onDestroy() {
        setAdCanChange(false);
        clearMemoryCache();
    }

    private AdImageManager() {

        mImageThreadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        mMemoryCache = new LruCache<String, BitmapDrawable>(AD_MAX_CACHE_SIZE);
    }


    public void submit(String[] imageUrls) {

        DebugUtil.debug(TAG, "submit");
        setAdImageUrls(imageUrls);
        getAdImages();
    }

    private void setAdImageUrls(String[] imageUrls) {

        mAdImageUrls = imageUrls;
        mImageUrlCleaner = new HashSet<String>();
        for (String imageUrl : imageUrls) {
            mImageUrlCleaner.add(imageUrl);
        }
    }

    private void getAdImages() {

        for (String url : mAdImageUrls) {
            mImageThreadPoolExecutor.execute(new AsyncImageRunnable(url));
        }
    }

    private void setAdCanChange(boolean canChange) {

        mAdCanChange = canChange;

    }

    private void clearMemoryCache() {

        mMemoryCache.evictAll();
    }

    public void getDrawable(ImageView imageView, String url) {

        BitmapDrawable bitmapDrawable = getMemoryCache(url);
        if (bitmapDrawable != null) {
            imageView.setImageDrawable(bitmapDrawable);
            return;
        }

        DecodeDiskTask task = new DecodeDiskTask(imageView);
        task.execute(url);
    }

    private boolean imageAllReady() {

        DebugUtil.debug(TAG, "imageAllReady size " + mImageUrlCleaner.size());
        return mImageUrlCleaner.size() == 0;
    }

    private void removeCleaner(String url) {

        DebugUtil.debug(TAG, "removeCleaner url " + url);

        mImageUrlCleaner.remove(url);
        if (imageAllReady()) {
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_AD_IMAGE_READY, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }
    }

    private BitmapDrawable getMemoryCache(String url) {

        return mMemoryCache.get(url);
    }


    private void putMemoryCache(String url, BitmapDrawable bitmapDrawable) {

        DebugUtil.debug(TAG, "putMemoryCache url " + url);

        mMemoryCache.put(url, bitmapDrawable);
    }

    public File getImageFile(String url) {
        String key = ImageCache.hashKeyForDisk(url);
        return new File(mAdDir, key);
    }


    private BitmapDrawable decodeFile(File imageFile) {

        DebugUtil.debug(TAG, "decodeFile");

        FileInputStream fileInputStream = null;
        BitmapDrawable bitmapDrawable = null;
        try {
            fileInputStream = new FileInputStream(imageFile);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            if (fileDescriptor != null) {
                Bitmap bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth,
                        mImageHeight, null);

                // QiYun<LeiYong><2014-03-18> modify for CR00000011 begin
                if(bitmap != null){
                    bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
                }
                // QiYun<LeiYong><2014-03-18> modify for CR00000011 end
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmapDrawable;
    }

    private class DecodeDiskTask extends AsyncTask<String, Integer, BitmapDrawable> {

        private final WeakReference<ImageView> mImageViewReference;
        private String mUrl;

        public DecodeDiskTask(ImageView imageView) {
            mImageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            mUrl = params[0];
            File imageFile = getImageFile(mUrl);
            return decodeFile(imageFile);
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);

            if (bitmapDrawable != null) {
                putMemoryCache(mUrl, bitmapDrawable);
                if (getAttachedImageView() != null) {

                    DebugUtil.debug(TAG, "mUrl " + mUrl + " Tag " + getAttachedImageView().getTag());

                    if (getAttachedImageView().getTag().equals(mUrl)) {
                        getAttachedImageView().setImageDrawable(bitmapDrawable);
                    }
                }

            }
        }

        private ImageView getAttachedImageView() {
            return mImageViewReference.get();
        }
    }


    protected class AsyncImageRunnable implements Runnable {

        private String mUrl;

        public AsyncImageRunnable(String url) {
            mUrl = url;
        }

        @Override
        public void run() {

            FileOutputStream fileOutputStream = null;
            try {

                File imageFile = getImageFile(mUrl);
                if (processDiskCacheFile(imageFile)) {
                    DebugUtil.debug(TAG, "run processDiskCacheFile succeed");
                    return;
                }

                fileOutputStream = new FileOutputStream(imageFile);
                boolean isSucceed = Utils.downloadUrlToStream(mUrl, fileOutputStream);

                if (isSucceed) {
                    DebugUtil.debug(TAG, "run downloadUrlToStream succeed");
                    processDiskCacheFile(imageFile);
                } else {
                    deleteDirtyFile(imageFile);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private boolean processDiskCacheFile(File imageFile) {

            DebugUtil.debug(TAG, "processDiskCacheFile");

            BitmapDrawable bitmapDrawable = decodeDiskCacheFile(imageFile);
            if (bitmapDrawable != null) {
                putMemoryCache(mUrl, bitmapDrawable);
                removeCleaner(mUrl);
                return true;
            }
            return false;
        }

        public void deleteDirtyFile(File dirtyFile) {

            DebugUtil.debug(TAG, "deleteDirtyFile");

            if (dirtyFile.exists()) {
                dirtyFile.delete();
            }
        }

        public BitmapDrawable decodeDiskCacheFile(File imageFile) {

            DebugUtil.debug(TAG, "decodeDiskCacheFile imageFile " + imageFile.getPath());

            if (!imageFile.exists()) {
                return null;
            }
            return decodeFile(imageFile);
        }
    }
}
