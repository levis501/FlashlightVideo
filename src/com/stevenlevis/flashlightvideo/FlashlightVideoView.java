package com.stevenlevis.flashlightvideo;

import android.view.MotionEvent;
import android.view.SurfaceView;

public class FlashlightVideoView extends SurfaceView {
	MainActivity mMainActivity;
	
	public FlashlightVideoView(MainActivity context) {
		super(context);
		mMainActivity = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mMainActivity.autoFocus();
		return super.onTouchEvent(event);
	}

}
