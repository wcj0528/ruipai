package com.ruipai.cn.base;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ruipai.cn.R;
import com.ruipai.cn.tool.ToastUtil;

/**
 * 首页实现
 */
public class HomePager extends BasePager {

	private Button camerBtn;
	private Button videoBtn;
	private BluetoothAdapter bluetoothAdapter;

	public HomePager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		initView();
		initListener();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	private void initView() {

		View homePagerView = View.inflate(mActivity, R.layout.activity_homepager, null);
		// 向FrameLayout中动态添加布局
		flContent.addView(homePagerView);
		camerBtn = (Button) homePagerView.findViewById(R.id.btn_camera);
		videoBtn = (Button) homePagerView.findViewById(R.id.btn_video);
	}

	private void initListener() {
		camerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					ToastUtil.showShortToast(mActivity, "请先打开蓝牙,和锐拍匹配链接");
					initBluetool();
				//	return;
				}else {
				Intent intent = new Intent();
				// 指定开启系统相机的Action
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				mActivity.startActivityForResult(intent, 1);
				}
			}
		});
		videoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					ToastUtil.showShortToast(mActivity, "请先打开蓝牙,和锐拍匹配链接");
					initBluetool();
				//	return;
				}else {
				Intent intent = new Intent();
				// 指定开启系统E录像的Action
				intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				mActivity.startActivityForResult(intent, 1);
				}
			}
		});
	}

	/** 蓝牙设置界面 */
	private void initBluetool() {
		
		mActivity.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));// 直接进入手机中的蓝牙设置界面
	
	}
	
}
