package com.ruipai.cn.wxapi;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import com.ruipai.cn.LoginActivity;
import com.ruipai.cn.R;
import com.ruipai.cn.RegisterActivity;
import com.ruipai.cn.third.Constant;
import com.ruipai.cn.third.QQLogin;
import com.ruipai.cn.third.WeiBoLogin;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

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
			// ToastUtil.showLongToast(WXEntryActivity.this, "请稍等..努力加载中");
			// onClickLogin();
			if (!QQLogin.mTencent.isSessionValid()) {
				QQLogin.mTencent.login(this, "all", qqLogin);
			}
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
					//跳转注册
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
	public void submitUserInfo(String country,String phone){
		Random random = new Random();
		String uid = Math.abs(random.nextInt())+"";
		String nickName = "af150714";
		SMSSDK.submitUserInfo(uid, nickName, null, country, phone);
	}
}
