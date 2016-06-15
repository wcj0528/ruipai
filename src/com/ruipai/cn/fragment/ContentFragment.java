package com.ruipai.cn.fragment;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruipai.cn.R;

public class ContentFragment extends BaseFragment {
	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view); // 注入view和事件
		return view;
	}

	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);// 默认勾选首页
		// 监听RadioGroup的选择事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:// 首页
					
					break;
				case R.id.rb_news:// 视频管理

					break;
				case R.id.rb_smart:// 个人中心

					break;
				case R.id.rb_gov:// 关于我们

					break;

				default:
					break;
				}
			}
		});
	}
}
