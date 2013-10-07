package cn.lemon.shopping.upgrade;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

public class UpgradeManager {

	private boolean mInitDone = false;
	private Context mContext;
	private String mDownloadUrl;
	private long mTotalSize;
	private long mCompleteSize;
	private String mLastRetVer;

	public String mSharePrefrenceName = "cn.lemom.upgrade.share_prefrence";
	public String mVersionKey = "version";
	public String mForceKey = "force";
	public String mTotalSizeKey = "total_size";
	public String mDoneSizeKey = "complete_pos";

	public final String RANGE = "Range";
	public final String RANGE_BYTES = "bytes=";

	private UpgradeDownloadManager mUpgradeDownloadManager;
	private DownloadChangeListener mDownloadChangeListener;
	private SharedPreferences mSharePreference;
	private UpgradeDomainData mUpgradeDomainData;
	private long mDoneSize = 0;
	private NotificationManager mNotificationManager;

	private int mIconId;
	private int mNotificationId;
	private String mNotificationTicker = "";
	private String mNotificationTitle = "";
	private Bitmap mNotificationIcon;

	private UpgradeManager() {

		mUpgradeDownloadManager = UpgradeDownloadManager.getInstance();
		mDownloadChangeListener = new DownloadChangeListener();
		mUpgradeDownloadManager.setDownloadListener(mDownloadChangeListener);
	}

	public interface IDownloadChangeListener {

		public void onSizeChange(long size);

		public void onStatusChange(DownloadStatusE status);
	}

	private class DownloadChangeListener implements IDownloadChangeListener {

		@Override
		public void onSizeChange(long size) {
			mDoneSize = size;

			showDownloadNotification(0, false, true);
			mUpgradeDomainData.onSizeChange(mDoneSize);
		}

		@Override
		public void onStatusChange(DownloadStatusE status) {
			mUpgradeDomainData.onStatusChange(status);
			SharedPreferences.Editor editor = mSharePreference.edit();
			if (status == DownloadStatusE.DOWNLOAD_FINISH) {
				editor.clear();
				mNotificationManager.cancel(mNotificationId);
			} else if (status == DownloadStatusE.DOWNLOAD_ERROR
					|| status == DownloadStatusE.DOWNLOAD_PAUSE) {
				showDownloadNotification(-1, true, false);
				editor.putLong(mDoneSizeKey, mDoneSize);
				mNotificationManager.cancel(mNotificationId);
			} else if (status == DownloadStatusE.DOWNLOAD_ING) {
				showDownloadNotification(0, false, true);
			}

			editor.commit();

		}
	}

	public enum DownloadStatusE {
		DOWNLOAD_WAIT, DOWNLOAD_ING, DOWNLOAD_PAUSE, DOWNLOAD_CANCEL, DOWNLOAD_ERROR, DOWNLOAD_FINISH
	}

	public class UpgradRequest {

		String mUrl;
		String mVersion;
		Map<String, String> mRequestHeaders;
		long mTotalSize;
		long mUnDoneSize;
		String mSaveFilePath;
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

		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		readSharePrefrence();
		mInitDone = true;
	}

	public void reset() {

	}

	public void start() {
		mUpgradeDownloadManager.start();
	}

	public void pause() {
		mUpgradeDownloadManager.pause();
	}

	public void cancel() {
		mUpgradeDownloadManager.cancel();
	}

	public void setNotificationInfo(int notificationId, int iconId,
			String ticker, String title) {
		mNotificationId = notificationId;
		mIconId = iconId;
		mNotificationTicker = ticker;
		mNotificationTitle = title;
		mNotificationIcon = BitmapFactory.decodeResource(
				mContext.getResources(), mIconId);
	}

	public void setUpgradeTask(UpgradRequest request) {

		initRequestHeaders(request);
		request.mUnDoneSize = request.mTotalSize - mCompleteSize;
		mUpgradeDownloadManager.setDownloadRequest(request);
		SharedPreferences.Editor editor = mSharePreference.edit();
		editor.putString(mVersionKey, request.mVersion);
		editor.putLong(mTotalSizeKey, request.mTotalSize);
		editor.commit();
	}

	public void addSizeChangeObserver(Observer observer) {
		mUpgradeDomainData.addSizeChangeObserver(observer);
	}

	public void addStatusChangeObserver(Observer observer) {
		mUpgradeDomainData.addStatusChangeObserver(observer);
	}

	public void deleteSizeChangeObserver(Observer observer) {
		mUpgradeDomainData.deleteSizeChangeObserver(observer);
	}

	public void deleteStatusChangeObserver(Observer observer) {
		mUpgradeDomainData.deleteStatusChangeObserver(observer);
	}

	private void readSharePrefrence() {

		mSharePreference = mContext.getSharedPreferences(mSharePrefrenceName,
				Context.MODE_PRIVATE);
		mLastRetVer = mSharePreference.getString(mVersionKey, null);
		mTotalSize = mSharePreference.getLong(mTotalSizeKey, 0);
		mCompleteSize = mSharePreference.getLong(mDoneSizeKey, 0);
	}

	private void initRequestHeaders(UpgradRequest request) {

		if (request.mRequestHeaders == null) {
			request.mRequestHeaders = new HashMap<String, String>();
		}
		request.mRequestHeaders.put(RANGE,
				RANGE_BYTES + Long.toHexString(mCompleteSize) + "-");

	}

	private void showDownloadNotification(int progress, boolean autoCancel,
			boolean onGoing) {

		Notification.Builder builder = new Notification.Builder(mContext);
		builder.setLargeIcon(mNotificationIcon);
		builder.setSmallIcon(mIconId);
		builder.setAutoCancel(autoCancel);
		builder.setOngoing(onGoing);
		builder.setProgress(100, progress, false);
		builder.setSmallIcon(android.R.drawable.stat_sys_download);

		builder.setTicker(mNotificationTicker)
				.setContentTitle(mNotificationTitle)
				.setContentIntent(getContentIntent());

		mNotificationManager.notify(mNotificationId, builder.build());

	}

	private PendingIntent getContentIntent() {

		// 通过发送广播启动到指定的广播接收器启动activity
		// 通过指定的intent的action来启动，可能存在多个接收该action的activity
		// 外部指定pendingIntent

		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		mContext.getPackageManager();
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		return pendingIntent;
	}

}
