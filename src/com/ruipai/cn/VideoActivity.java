package com.ruipai.cn;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/** 视频管理 */
public class VideoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_video);
	}
}
