package com.ruipai.cn.base;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.ruipai.cn.LoginActivity;
import com.ruipai.cn.R;
import com.ruipai.cn.wxapi.WXEntryActivity;

/**
 * 
 * 个人中心
 */
public class PersonPager extends BasePager {
	private ImageButton btn_login;

	public PersonPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {

		initView();
		initListener();

	}

	public void initView() {
		View view = View.inflate(mActivity, R.layout.personpager, null);
		// 向FrameLayout中动态添加布局
		flContent.addView(view);
		btn_login = (ImageButton) view.findViewById(R.id.exitlogin_image_ibt);

	}

	private void initListener() {
		// 登录注册
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, WXEntryActivity.class);
				mActivity.startActivity(intent);
			}
		});
	}

	// 切换登录按钮状态
	private void updateLoginButton() {
		if (LoginActivity.mQQAuth != null
				&& LoginActivity.mQQAuth.isSessionValid()) {
			// btn_login.setImageDrawable()
			// / mNewLoginButton.setText("退出帐号");
		} else {
			// mNewLoginButton.setTextColor(Color.BLUE);
			// mNewLoginButton.setText("登录");
		}
	}
}
