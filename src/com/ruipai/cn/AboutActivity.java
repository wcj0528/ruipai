package com.ruipai.cn;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/** 关于我们 */
public class AboutActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
	}
}
