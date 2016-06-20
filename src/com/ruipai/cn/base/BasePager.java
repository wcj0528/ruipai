package com.ruipai.cn.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ruipai.cn.R;

/**
 * 主页下4个子页面的基类
 * 
 * 
 */
public class BasePager {

	public Activity mActivity;
	public View mRootView;// 布局对象

	public TextView tvTitle;// 标题对象

	public FrameLayout flContent;// 内容

	public ImageButton btnMenu;// 菜单按钮
	public ImageButton btnPhoto;// 组图切换按钮

	public BasePager(Activity activity) {
		mActivity = activity;
		initViews();
		//initData();
	}

	/**
	 * 初始化布局
	 */
	public void initViews() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);

		// tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootView.findViewById(R.id.f_content);
		// btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);

	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}
}
