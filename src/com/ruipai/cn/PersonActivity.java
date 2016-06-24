package com.ruipai.cn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ruipai.cn.tool.PrefUtils;
import com.ruipai.cn.wxapi.WXEntryActivity;

/** 个人中心 */
public class PersonActivity extends Activity {

	private ImageButton mImLoginbtn;
	private TextView mTvName;
	private BitmapUtils mBitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_personpager);
		mBitmapUtils = new BitmapUtils(this);
		initView();
		initListener();
	}

	private void initView() {
		mImLoginbtn = (ImageButton) findViewById(R.id.exitlogin_image_ibt);
		mTvName = (TextView) findViewById(R.id.tv_nickname);
		
	}

	private void initListener() {
		// 登录注册
		mImLoginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonActivity.this,	WXEntryActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onPause() {
		
		String nickName = PrefUtils.getString(PersonActivity.this, "nickname","");
		System.out.println("---取出来的--"+nickName);
		if (nickName!=null) {
			mTvName.setText(nickName);
			mTvName.setVisibility(View.VISIBLE);
		}
		String uri = PrefUtils.getString(PersonActivity.this, "path","");
		System.out.println("uri 头像 --"+uri);
		if (uri!=null) {
			
			mBitmapUtils.display(mImLoginbtn, uri);
		}
	}
}
