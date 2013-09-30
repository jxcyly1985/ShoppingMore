package cn.lemon.shopping.upgrade;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * 升级需求： 1. 可以设置强制和非强制升级 2. 可以断点续传 3. 下载完之后进行安装 定时升级策略： 1. 版本对比规则 ｛force:0,
 * downloadurl:"http://shopping/download", version:"1.0.1.a" size:19033223, ｝
 * 
 */
public class UpgradeManager {

    private boolean mInitDone = false;
    private Context mContext;
    private long mTotalSize;
    private long mCompleteSize;
    private boolean mForceUpgrade = false;
    private String mLastRetVer;

    private String mSharePrefrenceName = "cn.lemom.upgrade.share_prefrence";
    private String mVersionKey = "version";
    private String mForceKey = "force";
    private String mTotalSizeKey = "total_size";
    private String mDoneSizeKey = "complete_pos";

    private DownloadChangeListener mDownloadChangeListener;

    private UpgradeManager() {

    }

    public interface DownloadChangeListener {

        public void onSizeChange(long size);

        public void onDownloadFinish();
    }

    public class DownloadFinishObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            
            mDownloadChangeListener.onDownloadFinish();
        }

    }

    public class DownloadSizeChangerObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            
            long size = (Long) data;
            mDownloadChangeListener.onSizeChange(size);
        }

    }

    public static class UpgradeManagerHolder {
        static UpgradeManager sInstance = new UpgradeManager();
    }

    public static UpgradeManager getInstance() {
        return UpgradeManagerHolder.sInstance;

    }

    public void initialize(Context context) {

        if (mInitDone) {

            throw new RuntimeException("don't call initialize more than once!");
        }
        mContext = context;
        mInitDone = true;
        readSharePrefrence();
    }

    private void readSharePrefrence() {

        SharedPreferences sharePreference = mContext.getSharedPreferences(mSharePrefrenceName,
                Context.MODE_PRIVATE);
        mLastRetVer = sharePreference.getString(mVersionKey, null);
        mTotalSize = sharePreference.getLong(mTotalSizeKey, 0);
        mCompleteSize = sharePreference.getLong(mDoneSizeKey, 0);
        mForceUpgrade = sharePreference.getBoolean(mForceKey, false);
    }

    public void setDownloadFinishListener(DownloadChangeListener listener) {
        mDownloadChangeListener = listener;
    }

}
