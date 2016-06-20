package com.ruipai.cn;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 代表当前应用程序
 * 
 * @author itcast
 * 
 */
public class BaseApplication extends Application {
	private static BaseApplication application;
	private static int mainTid;
	private static Handler handler;
	public static IWXAPI api;
	@Override
	// 在主线程运行的
	public void onCreate() {
		super.onCreate();
		application = this;
		mainTid = android.os.Process.myTid();
		handler = new Handler();
		
		//注册微信
		//api = WXAPIFactory.createWXAPI(this, "你的应用在微信上申请的app_id", true); 
		//api.registerApp("你的应用在微信上申请的app_id");
	}

	public static Context getApplication() {
		return application;
	}

	public static int getMainTid() {
		return mainTid;
	}

	public static Handler getHandler() {
		return handler;
	}

}
