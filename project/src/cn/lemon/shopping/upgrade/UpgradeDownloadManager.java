package cn.lemon.shopping.upgrade;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import cn.lemon.shopping.upgrade.UpgradeManager.IDownloadChangeListener;
import cn.lemon.shopping.upgrade.UpgradeManager.DownloadStatusE;
import cn.lemon.shopping.upgrade.UpgradeManager.UpgradRequest;

final class UpgradeDownloadManager {

	private UpgradeDownloadRunnable mUpgradeDownloadRunnable;

	public static final int BUFFER_SIZE = 4096;
	public static final int CONNECT_TIMEOUT = 15 * 1000;
	public static final int SO_TIMEOUT = 15 * 1000;
	public static final String CHARSET_NAME = "utf-8";
	private UpgradRequest mUpgradRequest;
	private IDownloadChangeListener mDownloadChangeListener;

	private UpgradeDownloadManager() {
		mUpgradeDownloadRunnable = new UpgradeDownloadRunnable();
	}

	protected static class UpgradeDownloadManagerHolder {
		static UpgradeDownloadManager sInstance = new UpgradeDownloadManager();
	}

	protected static UpgradeDownloadManager getInstance() {
		return UpgradeDownloadManagerHolder.sInstance;
	}

	protected boolean start() {
		new Thread() {
			@Override
			public void run() {

				super.run();
				mUpgradeDownloadRunnable.run();
			}
		};
		return true;
	}

	protected boolean pause() {

		if (mUpgradeDownloadRunnable.mDownloadFinish
				|| mUpgradeDownloadRunnable.mCancel
				|| mUpgradeDownloadRunnable.mDownloadError) {
			return false;
		} else {
			mUpgradeDownloadRunnable.pause();
			return true;
		}

	}

	protected boolean cancel() {
		if (mUpgradeDownloadRunnable.mDownloadFinish
				|| mUpgradeDownloadRunnable.mCancel
				|| mUpgradeDownloadRunnable.mDownloadError) {
			return false;
		} else {
			mUpgradeDownloadRunnable.cancel();
			return true;
		}
	}

	protected void setDownloadRequest(UpgradRequest request) {
		mUpgradRequest = request;
	}

	protected void setDownloadListener(IDownloadChangeListener listener) {
		mDownloadChangeListener = listener;
	}

	private class UpgradeDownloadRunnable implements Runnable {

		private boolean mPause = false;
		private boolean mCancel = false;
		private boolean mDownloadFinish = false;
		private boolean mDownloadError = false;
		private long mDoneSize = 0;

		@Override
		public void run() {

			if (!mCancel) {
				DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
				BasicHttpParams httpParams = new BasicHttpParams();
				HttpGet httpGet = new HttpGet(mUpgradRequest.mUrl);

				if (mUpgradRequest.mRequestHeaders != null) {
					for (Entry<String, String> entry : mUpgradRequest.mRequestHeaders
							.entrySet()) {
						httpGet.addHeader(entry.getKey(), entry.getValue());
					}

				}

				HttpConnectionParams.setConnectionTimeout(httpParams,
						CONNECT_TIMEOUT);
				HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

				httpGet.setParams(httpParams);

				HttpResponse response = null;
				InputStream inputStream = null;
				OutputStream outputStream = null;
				BufferedInputStream bufferedInputStream = null;
				BufferedOutputStream bufferedOutputStream = null;
				int statusCode = 0;

				try {
					response = defaultHttpClient.execute(httpGet);
					statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == HttpStatus.SC_OK
							|| statusCode == HttpStatus.SC_PARTIAL_CONTENT) {

						if (mUpgradRequest.mUnDoneSize != 0
								&& response.getEntity().getContentLength() > 0) {
							if (response.getEntity().getContentLength() != mUpgradRequest.mUnDoneSize) {

								mDownloadError = true;
								mDownloadChangeListener
										.onStatusChange(DownloadStatusE.DOWNLOAD_ERROR);
								return;
							}
						}

						HttpEntity entity = response.getEntity();
						if (entity != null) {
							int readLen = -1;
							byte[] buffer = new byte[BUFFER_SIZE];
							inputStream = entity.getContent();
							outputStream = new FileOutputStream(
									mUpgradRequest.mSaveFilePath);
							bufferedInputStream = new BufferedInputStream(
									inputStream);
							bufferedOutputStream = new BufferedOutputStream(
									outputStream, BUFFER_SIZE);

							mDownloadChangeListener
									.onStatusChange(DownloadStatusE.DOWNLOAD_ING);
							while ((readLen = bufferedInputStream.read(buffer)) != -1) {
								mDoneSize += readLen;
								bufferedOutputStream.write(buffer, 0, readLen);
								mDownloadChangeListener.onSizeChange(mDoneSize);
							}

							mDownloadFinish = true;
							mDownloadChangeListener
									.onStatusChange(DownloadStatusE.DOWNLOAD_FINISH);

						}

					}

				} catch (ClientProtocolException e) {
					mDownloadError = true;
					mDownloadChangeListener
							.onStatusChange(DownloadStatusE.DOWNLOAD_ERROR);
					e.printStackTrace();
				} catch (IOException e) {
					mDownloadError = true;
					mDownloadChangeListener
							.onStatusChange(DownloadStatusE.DOWNLOAD_ERROR);
					e.printStackTrace();
				} finally {
					if (bufferedInputStream != null) {
						try {
							bufferedInputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (bufferedOutputStream != null) {
						try {
							bufferedOutputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

		}

		public void pause() {
			mDownloadChangeListener
					.onStatusChange(DownloadStatusE.DOWNLOAD_PAUSE);
			mPause = true;

		}

		public void cancel() {
			mDownloadChangeListener
					.onStatusChange(DownloadStatusE.DOWNLOAD_CANCEL);
			mCancel = true;
		}

	}

}
