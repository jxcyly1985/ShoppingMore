package cn.lemon.shopping.Ad;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import cn.lemon.bitmap.ImageCache;
import cn.lemon.bitmap.ImageResizer;
import cn.lemon.utils.DebugUtil;

import java.io.*;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 13-12-15
 * Time: 下午1:08
 * To change this template use File | Settings | File Templates.
 */
public class AdImageManager{


    private static final String TAG = "AdImageManager";

    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private ThreadPoolExecutor mImageThreadPoolExecutor;
    private static final String AD_DIR = "AD";
    private boolean mInit = false;
    private Context mContext;
    private File mAdDir;
    private Map<String,SoftReference<BitmapDrawable>> mBitmapDrawableCache;
    private int mImageWidth;
    private int mImageHeight;

    private static class AdImageManagerHolder{
        static AdImageManager sInstance = new  AdImageManager();
    }

    public static AdImageManager getInstance(){

        return AdImageManagerHolder.sInstance;

    }

    public void init(Context context){

        if(!mInit){
            mContext = context;
            mAdDir = ImageCache.getDiskCacheDir(mContext, AD_DIR);
            mInit = true;
        }
    }

    private  AdImageManager(){
        mImageThreadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        mBitmapDrawableCache = new HashMap<String, SoftReference<BitmapDrawable>>();
    }

    public void setAdImageUrls(String[] imageUrls){

        for(String imageUrl : imageUrls){

            mImageThreadPoolExecutor.submit(new ImageDownloadRunnable(imageUrl));
        }
    }

    // Decode File move to thread
    public BitmapDrawable getBitmapDrawable(String url) {

        BitmapDrawable bitmapDrawable = null;
        String key = ImageCache.hashKeyForDisk(url);
        File imageFile = new File(mAdDir, key);
        if (mBitmapDrawableCache.containsKey(url)) {

            SoftReference<BitmapDrawable> reference = mBitmapDrawableCache.get(url);
            bitmapDrawable = reference.get();

            if (bitmapDrawable == null) {
                bitmapDrawable = decodeFile(url, imageFile);
            }

        } else {

            if (imageFile.exists()) {
                bitmapDrawable = decodeFile(url, imageFile);
            }
        }
        return bitmapDrawable;
    }

    public BitmapDrawable decodeFile(String url, File imageFile){
        FileInputStream fileInputStream = null;
        FileDescriptor fileDescriptor = null;
        try {
            fileInputStream = new FileInputStream(imageFile);
            fileDescriptor = fileInputStream.getFD();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        BitmapDrawable bitmapDrawable = null;
        if (fileDescriptor != null) {
            bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth,
                    mImageHeight, null);
            bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
            SoftReference<BitmapDrawable> reference = new SoftReference<BitmapDrawable>(bitmapDrawable);
            mBitmapDrawableCache.put(url, reference);
        }
        return  bitmapDrawable;
    }

    private class ImageDownloadRunnable implements Runnable{

        private String mUrl;
        ImageDownloadRunnable(String url){
            mUrl = url;
        }

        @Override
        public void run() {
            FileOutputStream fileOutputStream = null;
            try {
                String key = ImageCache.hashKeyForDisk(mUrl);
                File imageFile = new File(mAdDir, key);
                fileOutputStream = new FileOutputStream(imageFile);
                boolean isSucceed = downloadUrlToStream(mUrl, fileOutputStream);

                if (isSucceed) {
                    decodeFile(mUrl, imageFile);
                } else {

                    if (imageFile.exists()) {
                        imageFile.delete();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                       e.printStackTrace();
            } finally {

                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
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
            } catch (final IOException e) {}
        }
        return false;
    }



}
