package com.ruipai.cn.base;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

public class VideoPager extends BasePager{

	public VideoPager(Activity activity) {
		super(activity);
	}
	@Override
	public void initData() {
		TextView text = new TextView(mActivity);
		text.setText("............video");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		// 向FrameLayout中动态添加布局
		flContent.addView(text);
	}
}
