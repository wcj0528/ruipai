package com.ruipai.cn;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ruipai.cn.tool.Page;

public class MainActivity extends ActivityGroup {
	/**
	 * 主页面
	 */
	private long time;
	private Page page;
	private RadioButton but;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		page = new Page(MainActivity.this, R.id.f);
		
		page.addpage("home", new Intent(MainActivity.this, HomeActivity.class),R.id.rb_home);
		page.addpage("video", new Intent(MainActivity.this, VideoActivity.class),R.id.rb_video);
		page.addpage("person", new Intent(MainActivity.this, PersonActivity.class),R.id.rb_person );
		page.addpage("about", new Intent(MainActivity.this, AboutActivity.class),R.id.rb_about );

		but = (RadioButton) findViewById(R.id.rb_home);
		but.setChecked(true);
	}

	


	/**
	 * @作用：双击back键退出程序，间隔不超过2s
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - time) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			// ToastUtils.showShort("再按一次退出程序");
			time = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}
}
