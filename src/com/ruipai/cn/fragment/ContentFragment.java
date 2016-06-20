package com.ruipai.cn.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ruipai.cn.R;
import com.ruipai.cn.base.BasePager;
import com.ruipai.cn.base.HomePager;
import com.ruipai.cn.base.PersonPager;
import com.ruipai.cn.base.SettingPager;
import com.ruipai.cn.base.VideoPager;

public class ContentFragment extends BaseFragment {
	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;

	@ViewInject(R.id.vp_content)
	private ViewPager mViewPager;

	private ArrayList<BasePager> mPagerList;

	@Override
	public View initViews() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view); // 注入view和事件
		return view;
	}

	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);// 默认勾选首页

		// 初始化4个子页面
		mPagerList = new ArrayList<BasePager>();
		// for (int i = 0; i < 4; i++) {
		// BasePager pager = new BasePager(mActivity);
		// mPagerList.add(pager);
		// }
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new VideoPager(mActivity));
		mPagerList.add(new PersonPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));

		mViewPager.setAdapter(new ContentAdapter());

		// 监听RadioGroup的选择事件
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:// 首页
					mViewPager.setCurrentItem(0, false);// 去掉切换页面的动画

					break;
				case R.id.rb_news:// 视频管理
					mViewPager.setCurrentItem(1, false);// 去掉切换页面的动画
					break;
				case R.id.rb_smart:// 个人中心
					mViewPager.setCurrentItem(2, false);// 去掉切换页面的动画
					break;
				case R.id.rb_gov:// 关于我们
					mViewPager.setCurrentItem(3, false);// 去掉切换页面的动画
					break;

				default:
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mPagerList.get(0).initData();// 初始化首页数据
	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagerList.get(position);
			container.addView(pager.mRootView);
			// pager.initData();// 初始化数据.... 不要放在此处初始化数据, 否则会预加载下一个页面
			return pager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
