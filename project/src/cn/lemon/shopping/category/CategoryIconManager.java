package cn.lemon.shopping.category;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import cn.lemon.bitmap.AsyncTask;
import cn.lemon.bitmap.ImageCache;
import cn.lemon.bitmap.ImageResizer;
import cn.lemon.shopping.utils.Utils;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-1-14
 * Time: 下午11:02
 * To change this template use File | Settings | File Templates.
 */
public class CategoryIconManager {

    private static CategoryIconManager sInstance;
    private Map<String, Drawable> mDrawableMap;
    private File mIconDir;
    private static final String CATEGORY_ICON_DIR = "category_icon";
    private Context mContext;

    public synchronized static CategoryIconManager getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryIconManager();
        }
        return sInstance;
    }

    private CategoryIconManager() {
        mDrawableMap = new HashMap<String, Drawable>();
    }

    public void init(Context context) {
        mContext = context;
        mIconDir = ImageCache.getDiskCacheDir(context, CATEGORY_ICON_DIR);
        if (!mIconDir.exists()) {
            mIconDir.mkdirs();
        }
    }


    public void getIcon(String url, ImageView imageView) {

        AsyncDrawableTask task = new AsyncDrawableTask(imageView);
        task.execute(url);
    }

    public File getImageFile(String url) {
        String key = ImageCache.hashKeyForDisk(url);
        return new File(mIconDir, key);
    }

    private class AsyncDrawableTask extends AsyncTask<String, Integer, BitmapDrawable> {

        private final WeakReference<ImageView> mImageViewReference;
        private String mUrl;

        public AsyncDrawableTask(ImageView imageView) {
            mImageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            mUrl = params[0];
            File imageFile = getImageFile(mUrl);
            FileOutputStream fileOutputStream = null;

            try {
                if (imageFile.exists()) {
                    return decodeBitmapFromFile(imageFile);
                }
                fileOutputStream = new FileOutputStream(imageFile);
                boolean isSucceed = Utils.downloadUrlToStream(mUrl, fileOutputStream);
                if (isSucceed) {
                    return decodeBitmapFromFile(imageFile);
                } else {
                    deleteDirtyFile(imageFile);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(BitmapDrawable bitmapDrawable) {
            super.onPostExecute(bitmapDrawable);
            if (bitmapDrawable != null) {
                if (getAttachedImageView() != null) {
                    getAttachedImageView().setImageDrawable(bitmapDrawable);
                }
            }
        }

        private ImageView getAttachedImageView() {
            return mImageViewReference.get();
        }

        private BitmapDrawable decodeBitmapFromFile(File imageFile) throws IOException {
            FileInputStream fileInputStream = new FileInputStream(imageFile);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            if (fileDescriptor != null) {
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                return new BitmapDrawable(mContext.getResources(), bitmap);
            }
            return null;
        }

        private void deleteDirtyFile(File imageFile) {
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }
    }
}
