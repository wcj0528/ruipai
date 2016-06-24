package com.ruipai.cn.wxapi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.ruipai.cn.R;
import com.ruipai.cn.RegisterActivity;
import com.ruipai.cn.third.Constant;
import com.ruipai.cn.third.QQLogin;
import com.ruipai.cn.third.WeiBoLogin;
import com.ruipai.cn.tool.PrefUtils;
import com.ruipai.cn.tool.ToastUtil;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private UserInfo mInfo; // 用户信息qq
	private Tencent mTencent;
	public QQAuth mQQAuth;
	public Bitmap bitmapLogin;

	private WeiBoLogin weiboLogin;
	private QQLogin qqLogin;

	private IWXAPI api;
	public BaseResp resp;

	// 短信
	private String APPkey = "13fba474fa1a6";
	private String APPsecret = "d22e9be2eff44d288a8079b948970f7f";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		// Tencent类是SDK的主要实现类，通过此访问腾讯开放的OpenAPI。
		mQQAuth = QQAuth.createInstance(Constant.QQ_APP_ID,
				this.getApplicationContext());
		// 实例化
		mTencent = Tencent.createInstance(Constant.QQ_APP_ID, this);

		SMSSDK.initSDK(getApplicationContext(), APPkey, APPsecret);

		qqLogin = new QQLogin(this);
		weiboLogin = new WeiBoLogin(this);

		// api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, false);
		// api.handleIntent(getIntent(), this);
		api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, false);
		api.handleIntent(getIntent(), this);
	}

	public void myClick(View view) {
		switch (view.getId()) {
		case R.id.im_qq:// qq登录
			// 前一个版本
			// if (!QQLogin.mTencent.isSessionValid()) {
			// QQLogin.mTencent.login(this, "all", qqLogin);
			// }
			ToastUtil.showShortToast(getApplicationContext(), "请稍等...");
			onClickLogin();
			break;
		case R.id.im_wx:// 微信登录

			api.registerApp(Constant.WEIXIN_APP_ID);

			// api.registerApp(Constant.WEIXIN_APP_ID);
			// SendAuth.Req req = new SendAuth.Req();
			// req.scope = "snsapi_userinfo";
			// req.state = "wechat_sdk_demo";
			// api.sendReq(req);
			break;
		case R.id.im_xl:// 新浪微博登录

			WeiBoLogin.mSsoHandler.authorizeWeb(weiboLogin);
			break;

		case R.id.login_image_back:// 返回
			finish();
			break;

		default:
			break;
		}
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		finish();

	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		if (resp != null) {
			resp = resp;
		}
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			result = "发送返回";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
		finish();
	}

	/** 短信注册界面 */
	public void register(View view) {

		RegisterPage page = new RegisterPage();
		page.setRegisterCallback(new EventHandler() {
			// �¼���ɺ����
			@Override
			public void afterEvent(int event, int result, Object date) {
				// �жϽ���Ƿ��Ѿ����
				if (result == SMSSDK.RESULT_COMPLETE) {
					// ��ȡ����date
					HashMap<String, Object> map = (HashMap<String, Object>) date;
					// ��ȡ������Ϣ
					String country = (String) map.get("country");
					// ��ȡ�绰����
					String phone = (String) map.get("phone");
					submitUserInfo(country, phone);

					Toast.makeText(WXEntryActivity.this, "��֤�ɹ�",
							Toast.LENGTH_SHORT).show();
					// 跳转注册
					Intent intent = new Intent(WXEntryActivity.this,
							RegisterActivity.class);
					intent.putExtra("phone", phone);
					startActivity(intent);
					finish();
				}
			}
		});
		// ��ʾע�����
		page.show(WXEntryActivity.this);

	}

	public void submitUserInfo(String country, String phone) {
		Random random = new Random();
		String uid = Math.abs(random.nextInt()) + "";
		String nickName = "af150714";
		SMSSDK.submitUserInfo(uid, nickName, null, country, phone);
	}

	/** qq登录 */
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				// mUserInfo.setVisibility(android.view.View.VISIBLE);
				// mUserInfo.setText(msg.getData().getString("nickname"));

				String value = msg.getData().getString("nickname");

			} else if (msg.what == 1) {
				bitmapLogin = (Bitmap) msg.obj;
				// mUserLogo.setImageBitmap(bitmap);
				// mUserLogo.setVisibility(android.view.View.VISIBLE);
			}
		}
	};

	// 3
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
						PrefUtils.setString(getApplicationContext(),
								"nickname", nickname);
						
						System.out.println(" 昵称" +nickname);
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
						System.out.println("头像路径 : " + path);

						PrefUtils.setString(getApplicationContext(), "logo",
								path);

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
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		}
	}

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

	// 2.
	public void onClickLogin() {
		// 登录
		if (!mQQAuth.isSessionValid()) {
			// 实例化回调接口
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					// 3
					updateUserInfo();
					if (mQQAuth != null) {
						// mNewLoginButton.setTextColor(Color.BLUE);
						// mNewLoginButton.setText("登录");
					}
				}
			};
			// "all": 所有权限，listener: 回调的实例 mQQAuth.login(this, "all", listener);
			mTencent.loginWithOEM(this, "all", listener, "10000144",
					"10000144", "xxxx");
		} else {
			// 注销登录
			mQQAuth.logout(this);
			updateUserInfo();

			// updateLoginButton();
			// //mNewLoginButton.setTextColor(Color.RED);
			// mNewLoginButton.setText("退出帐号");
		}
	}

	/**
	 * 调用SDK封装好的借口，需要传入回调的实例 会返回服务器的消息
	 */
	private class BaseUiListener implements IUiListener {
		/**
		 * 成功
		 */
		@Override
		public void onComplete(Object response) {
			System.out.println("返回的qq : " + response.toString());

			ToastUtil.showShortToast(getApplicationContext(), "授权成功 ");
			doComplete((JSONObject) response);
		}

		/** 处理返回的消息 比如把json转换为对象什么的 */
		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			Toast.makeText(WXEntryActivity.this, e.toString(), 1000).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(WXEntryActivity.this, "授权取消", 1000).show();
		}
	}
}
