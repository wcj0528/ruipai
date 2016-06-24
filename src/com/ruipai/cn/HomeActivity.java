package com.ruipai.cn;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.ruipai.cn.tool.ToastUtil;

/** 首页 */
public class HomeActivity extends Activity {
	
	private Button camerBtn;
	private Button videoBtn;
	private BluetoothAdapter bluetoothAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_homepager);
		initView();
		initListener();
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	private void initView() {

		camerBtn = (Button) findViewById(R.id.btn_camera);
		videoBtn = (Button)findViewById(R.id.btn_video);
	}

	private void initListener() {
		camerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					ToastUtil.showShortToast(HomeActivity.this, "请先打开蓝牙,和锐拍匹配链接");
					initBluetool();
				//	return;
				}else {
				Intent intent = new Intent();
				// 指定开启系统相机的Action
				intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				startActivityForResult(intent, 1);
				}
			}
		});
		videoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {// 如果蓝牙还没开启
					ToastUtil.showShortToast(HomeActivity.this, "请先打开蓝牙,和锐拍匹配链接");
					initBluetool();
				//	return;
				}else {
				Intent intent = new Intent();
				// 指定开启系统E录像的Action
				intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				startActivityForResult(intent, 1);
				}
			}
		});
	}

	/** 蓝牙设置界面 */
	private void initBluetool() {
		
   startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));// 直接进入手机中的蓝牙设置界面
	
	}
}
