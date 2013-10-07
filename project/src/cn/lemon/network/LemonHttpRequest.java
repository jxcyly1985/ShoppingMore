package cn.lemon.network;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

public class LemonHttpRequest {

	private LemonNetWorkRequest mLemonNetWorkRequest;
	private LemonNetWorkHandler mLemonNetWorkHandler;

	public static final int CONNECT_TIMEOUT = 15 * 1000;
	public static final int SO_TIMEOUT = 15 * 1000;
	public static final String CHARSET_NAME = "utf-8";

	public LemonHttpRequest(LemonNetWorkRequest request,
			LemonNetWorkHandler handler) {
		mLemonNetWorkRequest = request;
		mLemonNetWorkHandler = handler;
	}

	public void get() {

		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpGet httpGet = new HttpGet(mLemonNetWorkRequest.mUrl);

		if (mLemonNetWorkRequest.mHeader != null) {
			for (Entry<String, String> entry : mLemonNetWorkRequest.mHeader
					.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}

		}

		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

		httpGet.setParams(httpParams);

		HttpResponse response = null;
		String result = null;

		try {
			response = defaultHttpClient.execute(httpGet);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				if (mLemonNetWorkRequest.mContentLenght != 0
						&& response.getEntity().getContentLength() > 0) {
					if (response.getEntity().getContentLength() != mLemonNetWorkRequest.mContentLenght) {
						mLemonNetWorkHandler.onHandleReceiveError();
						return;
					}
				}

				result = EntityUtils.toString(response.getEntity(),
						CHARSET_NAME);
				mLemonNetWorkHandler.onHandleReceiveSuccess(result);
			}

		} catch (ClientProtocolException e) {
			mLemonNetWorkHandler.onHandleReceiveError();
			e.printStackTrace();
		} catch (IOException e) {
			mLemonNetWorkHandler.onHandleReceiveError();
			e.printStackTrace();
		}

	}

	public void post() {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpPost httpPost = new HttpPost(mLemonNetWorkRequest.mUrl);

		if (mLemonNetWorkRequest.mHeader != null) {
			for (Entry<String, String> entry : mLemonNetWorkRequest.mHeader
					.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}

		}

		HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

		httpPost.setParams(httpParams);

		HttpResponse response = null;
		String result = null;

		try {
			StringEntity stringEntity = new StringEntity(
					mLemonNetWorkRequest.mPostString, CHARSET_NAME);
			httpPost.setEntity(stringEntity);
			response = defaultHttpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				if (mLemonNetWorkRequest.mContentLenght != 0
						&& response.getEntity().getContentLength() > 0) {
					if (response.getEntity().getContentLength() != mLemonNetWorkRequest.mContentLenght) {
						mLemonNetWorkHandler.onHandleReceiveError();
						return;
					}
				}
				result = EntityUtils.toString(response.getEntity(),
						CHARSET_NAME);
				mLemonNetWorkHandler.onHandleReceiveSuccess(result);
			}

		} catch (ClientProtocolException e) {
			mLemonNetWorkHandler.onHandleReceiveError();
			e.printStackTrace();
		} catch (IOException e) {
			mLemonNetWorkHandler.onHandleReceiveError();
			e.printStackTrace();
		}
	}
}
