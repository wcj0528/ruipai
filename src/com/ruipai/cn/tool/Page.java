package com.ruipai.cn.tool;

import java.util.HashMap;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;

public class Page {
	
	private Context context;
	private FrameLayout framelayout;
	private HashMap<Button, View> hm;
	private RadioButton but;
	
	
	public Page(Context context,int framelayoutid) {
		this.context=context;
		this.framelayout=(FrameLayout) ((ActivityGroup)context).findViewById(framelayoutid);
		hm=new HashMap<Button, View>();
		
	}
	
	public void addpage(String pagename,Intent intent,int buttonid) {
		
		View view=((ActivityGroup)context).getLocalActivityManager().startActivity(pagename, intent).getDecorView();
		
		framelayout.addView(view);
		
		but=(RadioButton) ((ActivityGroup)context).findViewById(buttonid);
		
		hm.put(but, view);
		
		but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				framelayout.bringChildToFront(hm.get(v));
				framelayout.invalidate();
			}
		});
		
	}

}
