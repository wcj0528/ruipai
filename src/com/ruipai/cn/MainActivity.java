package com.ruipai.cn;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.ruipai.cn.fragment.ContentFragment;

public class MainActivity extends FragmentActivity {
	/**
	 * 主页面
	 * 
	 */
	private long time;
	private static final String FRAGMENT_CONTENT = "fragment_content";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initFragment();

	}

	/**
	 * 初始化fragment, 将fragment数据填充给布局文件
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开启事务

		// 用fragment替换framelayout
		transaction.replace(R.id.fl_content, new ContentFragment(),
				FRAGMENT_CONTENT);

		transaction.commit();// 提交事务
	}

	// 获取主页面fragment
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(FRAGMENT_CONTENT);

		return fragment;
	}

	/*
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
