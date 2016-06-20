package com.ruipai.cn.base;

import com.ruipai.cn.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SettingPager extends BasePager{

	public SettingPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		TextView text = new TextView(mActivity);
		text.setText("............关于我们");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
      
		// 向FrameLayout中动态添加布局
		flContent.addView(text);
	}
}
