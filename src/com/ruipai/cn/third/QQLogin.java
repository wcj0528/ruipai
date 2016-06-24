package com.ruipai.cn.third;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLogin implements IUiListener {
	private UserInfo mInfo; // 用户信息
	private Context context;
	public static Tencent mTencent;
	public QQAuth mQQAuth;

	public QQLogin(Context context) {
		this.context = context;
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Constant.QQ_APP_ID, context);
		}
		// Tencent类是SDK的主要实现类，通过此访问腾讯开放的OpenAPI。
		if (mQQAuth == null) {
			mQQAuth = QQAuth.createInstance(Constant.QQ_APP_ID, context);
		}
	}

	@Override
	public void onComplete(Object response) {
//		if (null == response) {
//			Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		JSONObject jsonResponse = (JSONObject) response;
//		if (null != jsonResponse && jsonResponse.length() == 0) {
//			Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
//		String openId = null, accessToken = null;
//		try {
//			openId = ((JSONObject) response).getString(Constants.PARAM_OPEN_ID);
//			accessToken = ((JSONObject) response)
//					.getString(Constants.PARAM_ACCESS_TOKEN);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		Log.d("gaolei", "QQLogin,openId:" + openId + ",accessToken:"
//				+ accessToken + ",platform:" + "qq");
//		Toast.makeText(context,
//				"QQLogin,openId:" + openId + ",accessToken:" + accessToken,
//				Toast.LENGTH_SHORT).show();
//		doComplete((JSONObject) response);
		
		System.out.println("返回的东西"+response.toString());
	}

	protected void doComplete(JSONObject values) {

	}

	@Override
	public void onError(UiError e) {
		Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel() {
		Toast.makeText(context, "取消授权", Toast.LENGTH_SHORT).show();
	}

	/** 获取头像 */
	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {

			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {

				}

				@Override
				public void onComplete(final Object response) {
					JSONObject json = (JSONObject) response;
					// 昵称
					Message msg = new Message();
					String nickname = null;
					try {
						nickname = ((JSONObject) response)
								.getString("nickname");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					msg.getData().putString("nickname", nickname);
					msg.what = 0;
					mHandler.sendMessage(msg);
					// 头像
					String path;
					try {
						path = json.getString("figureurl_qq_2");
						MyImgThread imgThread = new MyImgThread(path);
						Thread thread = new Thread(imgThread);
						thread.start();
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}

				@Override
				public void onCancel() {

				}
			};
			mInfo = new UserInfo(context, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				// mUserInfo.setVisibility(android.view.View.VISIBLE);
				// mUserInfo.setText(msg.getData().getString("nickname"));
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				// mUserLogo.setImageBitmap(bitmap);
				// mUserLogo.setVisibility(android.view.View.VISIBLE);
			}
		}
	};

	/**
	 * 开启线程 获取头像
	 */
	class MyImgThread implements Runnable {
		private String imgPath;
		private Bitmap bitmap;

		public MyImgThread(String imgpath) {
			this.imgPath = imgpath;
		}

		@Override
		public void run() {
			bitmap = getImgBitmap(imgPath);
			Message msg = new Message();
			msg.obj = bitmap;
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 根据头像的url 获取bitmap
	 */
	public Bitmap getImgBitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			URL myFileUrl = new URL(imageUri);
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();

			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				conn.disconnect();
				is.close();
				is.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}