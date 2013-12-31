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
import cn.lemon.utils.DebugUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private static final int IO_BUFFER_SIZE = 8 * 1024;
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

        if (!mInit) {
            mContext = context;
            mAdDir = ImageCache.getDiskCacheDir(mContext, AD_DIR);
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

        BitmapDrawable bitmapDrawable = mMemoryCache.get(url);
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

    private void removeCleaner(String url){

        mImageUrlCleaner.remove(url);
        if (imageAllReady()) {
            Message msg = FramewokUtils.makeMessage(MessageConstants.MSG_AD_IMAGE_READY, null, 0, 0);
            MessageManager.getInstance().sendNotifyMessage(msg);
        }
    }

    private void putMemoryCache(String url, BitmapDrawable bitmapDrawable) {
        mMemoryCache.put(url, bitmapDrawable);
    }

    public File getImageFile(String url) {
        String key = ImageCache.hashKeyForDisk(url);
        return new File(mAdDir, key);
    }


    private BitmapDrawable decodeFile(File imageFile) {
        FileInputStream fileInputStream = null;
        FileDescriptor fileDescriptor = null;
        BitmapDrawable bitmapDrawable = null;
        try {
            fileInputStream = new FileInputStream(imageFile);
            fileDescriptor = fileInputStream.getFD();
            if (fileDescriptor != null) {
                Bitmap bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth,
                        mImageHeight, null);
                bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
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

    private class DecodeDiskTask extends AsyncTask<String, Integer, BitmapDrawable>{

        private ImageView mImageView;
        private String mUrl;

        public DecodeDiskTask(ImageView imageView){
            mImageView = imageView;
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
                if (mImageView.getTag().equals(mUrl)) {
                    mImageView.setImageDrawable(bitmapDrawable);
                }
            }
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
                BitmapDrawable bitmapDrawable = decodeDiskCacheFile(imageFile);
                if (bitmapDrawable != null) {
                    putMemoryCache(mUrl, bitmapDrawable);
                    removeCleaner(mUrl);
                    return;
                }

                fileOutputStream = new FileOutputStream(imageFile);
                boolean isSucceed = downloadUrlToStream(mUrl, fileOutputStream);

                if (isSucceed) {
                    bitmapDrawable = decodeDiskCacheFile(imageFile);
                    if(bitmapDrawable != null){
                        putMemoryCache(mUrl, bitmapDrawable);
                        removeCleaner(mUrl);
                    }
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

        public void deleteDirtyFile(File dirtyFile) {
            if (dirtyFile.exists()) {
                dirtyFile.delete();
            }
        }

        public BitmapDrawable decodeDiskCacheFile(File imageFile) {

            if (!imageFile.exists()) {
                return null;
            }
            return decodeFile(imageFile);
        }
    }


    public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {

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
