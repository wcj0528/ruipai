package com.ruipai.cn;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract.Constants;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.ruipai.cn.tool.ToastUtil;
import com.ruipai.cn.tool.Util;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class LoginActivity extends Activity {
	/* qq登录 */
	private static final String mAppid = "1105401839";
	public static QQAuth mQQAuth;
	private Tencent mTencent;
	private UserInfo mInfo;

	/* 微博登录 */
	private static final String APP_KEY = "2080386167";
	private static final String REDIRECT_URL = "http://billy.itheima.com";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;
	private AuthInfo mAuthInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mQQAuth = QQAuth.createInstance(mAppid, this);
		mTencent = Tencent.createInstance(mAppid, LoginActivity.this);
		setContentView(R.layout.login);

		// 微博
		mAuthInfo = new AuthInfo(this, APP_KEY, REDIRECT_URL, SCOPE);
		mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
	}

	public void myClick(View view) {
		switch (view.getId()) {
		case R.id.im_qq:// qq登录
			ToastUtil.showLongToast(LoginActivity.this, "请稍等..努力加载中");
			onClickLogin();

			break;
		case R.id.im_wx:// 微信登录

			ToastUtil.showLongToast(LoginActivity.this, "请稍等..努力加载中");
			
			break;
		case R.id.im_xl:// 新浪微博登录

			ToastUtil.showLongToast(LoginActivity.this, "请稍等..努力加载中");

			mSsoHandler.authorize(new AuthListener());
			break;

		case R.id.login_image_back:// 返回
			finish();
			break;

		default:
			break;
		}
	}

	/** 1.点击qq登录 授权 */
	private void onClickLogin() {

		if (!mQQAuth.isSessionValid()) {
			// 2.接受授权结果-->拿到accessToken
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError arg0) {// 授权失败

				}

				@Override
				public void onComplete(Object obj) {// 授权完成
					// JSONObject jsonObject = (JSONObject) obj;
					// // 2.处理授权结果.拿到accessToken
					// String access_token =
					// jsonObject.optString("access_token");
					// Toast.makeText(getApplicationContext(), access_token, 0)
					// .show();
					// System.out.println("access_token:" + access_token);

					updateUserInfo();
				}

				@Override
				public void onCancel() {// 授权取消

				}
			};
			mTencent.login(this, "all", listener);

		} else {
			mQQAuth.logout(this);
			updateUserInfo();
		}

	}

	/** 2. 获取个人信息 */
	private void updateUserInfo() {
		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {
				}

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									bitmap = Util.getbitmap(json
											.getString("figureurl_qq_2"));
								} catch (JSONException e) {

								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
			// mUserInfo.setText("");
			// mUserInfo.setVisibility(android.view.View.GONE);
			// mUserLogo.setVisibility(android.view.View.GONE);
		}
	}

	/** 设置名称 头像 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						System.out.println("昵称"
								+ response.getString("nickname"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
				// mUserLogo.setImageBitmap(bitmap);
				// mUserLogo.setVisibility(android.view.View.VISIBLE);
			}
		}
	};

	/**
	 * 微博登录 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {// 授权完成
			// 从 Bundle 中解析 Token
			Oauth2AccessToken mAccessToken = Oauth2AccessToken
					.parseAccessToken(values);

			// 获取用户信息接口
			// mAccessToken可以是你之前授权获取的那个，也可以
			// 获取当前已保存过的 Token
			UsersAPI mUsersAPI = new UsersAPI(LoginActivity.this, "2080386167",
					mAccessToken);
			long uid = Long.parseLong(mAccessToken.getUid());
			mUsersAPI.show(uid, mListener);

			if (mAccessToken.isSessionValid()) {
				//获取token
				String accessToken = mAccessToken.getToken();
			//	Toast.makeText(getApplicationContext(), accessToken, 0).show();
				
			} else {
			}
		}

		@Override
		public void onCancel() {// 授权取消
			Toast.makeText(getApplicationContext(), "授权取消", 0).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {// 授权异常
			Toast.makeText(getApplicationContext(), "授权异常", 0).show();
		}
	}

	// /***/
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == Constants.ACTIVITY_OK){
	// // SSO 授权回调
	// // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
	// if (mSsoHandler != null) {
	// Log.i("新浪微博登陆返回","返回");
	// //不能少
	// mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	// }
	// }
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	/**
	 * 微博 OpenAPI 回调接口。获取用户信息
	 */
	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				// 调用 User#parse 将JSON串解析成User对象
				User user = User.parse(response);
				if (user != null) {
					Toast.makeText(LoginActivity.this,
							"获取User信息成功，用户昵称：" + user.screen_name,
							Toast.LENGTH_LONG).show();
					
				String hdString= 	user.avatar_hd;
				String hdString2 = user.profile_image_url;
				
				System.out.println("头像--" +hdString);
				} else {
					Toast.makeText(LoginActivity.this, response,
							Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ErrorInfo info = ErrorInfo.parse(e.getMessage());
			Toast.makeText(LoginActivity.this, info.toString(),
					Toast.LENGTH_LONG).show();
		}
	};
}
