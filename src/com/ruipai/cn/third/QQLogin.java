package com.ruipai.cn.third;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class QQLogin implements IUiListener {

	private Context context;
	public static Tencent mTencent;

	public QQLogin(Context context) {
		this.context = context;
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Constant.QQ_APP_ID, context);
		}
	}

	@Override
	public void onComplete(Object response) {
		if (null == response) {
			Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
			return;
		}
		JSONObject jsonResponse = (JSONObject) response;
		if (null != jsonResponse && jsonResponse.length() == 0) {
			Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
		String openId = null, 
		accessToken = null;
		try {
			openId = ((JSONObject) response).getString(Constants.PARAM_OPEN_ID);
			accessToken = ((JSONObject) response)
					.getString(Constants.PARAM_ACCESS_TOKEN);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("gaolei", "QQLogin,openId:" + openId + ",accessToken:"
				+ accessToken + ",platform:" + "qq");
		Toast.makeText(context,
				"QQLogin,openId:" + openId + ",accessToken:" + accessToken,
				Toast.LENGTH_SHORT).show();
		doComplete((JSONObject) response);
	}

	protected void doComplete(JSONObject values) {

	}

	@Override
	public void onError(UiError e) {
		Toast.makeText(context, "授权成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCancel() {
		Toast.makeText(context, "取消授权", Toast.LENGTH_SHORT).show();
	}

}