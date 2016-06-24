package com.ruipai.cn.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.view.View;

import com.ruipai.cn.R;

public class VideoPager extends BasePager {

	public VideoPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		View videoPager = View.inflate(mActivity, R.layout.activity_homepager, null);

		// 向FrameLayout中动态添加布局
		flContent.addView(videoPager);
	}

}
