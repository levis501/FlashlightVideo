package com.stevenlevis.flashlightvideo;

import java.io.IOException;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class FlashlightVideoView extends SurfaceView implements SurfaceHolder.Callback {
	MainActivity mMainActivity;
	SurfaceHolder mHolder;
	private Camera mCamera;
	
	private boolean mAutoFocusOK = false;
	private boolean mAutoFocusInProgress = false;
	
	public FlashlightVideoView(MainActivity context) {
		super(context);
		mMainActivity = context;

		mHolder = getHolder();
		mHolder.addCallback(this);
	}
	
	void setCamera(Camera camera) {
		mAutoFocusOK = false;
		mCamera = camera;
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(mHolder);
			} catch (IOException e) {
	        	Toast.makeText(mMainActivity, "Unable to set camera preview.  Make sure nothing else is using the camera, or contact the application developer.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			try {
				Camera.Parameters params = mCamera.getParameters();
				params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(params);
			} catch (RuntimeException e) {
				Toast.makeText(mMainActivity, "Your camera doesn't appear to support torch mode.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			requestLayout();
		}
	}
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if ((mCamera != null) && mAutoFocusOK && (!mAutoFocusInProgress)) {
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					mAutoFocusInProgress = false;
				}});
			mAutoFocusInProgress = true;
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		setCamera(mCamera);
		if (mCamera != null) {
			try {
				Camera.Parameters parameters = mCamera.getParameters();
				List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
				int maxPixels = -1;
				Camera.Size maxSize = null;
				for (Camera.Size size : previewSizes) {
					int pixels = size.height * size.height;
					if (pixels > maxPixels) {
						maxPixels = pixels;
						maxSize = size;
					}
				}
				parameters.setPreviewSize(maxSize.width, maxSize.height);
				mCamera.setParameters(parameters);
				mCamera.startPreview();
			} catch (RuntimeException e) {
	        	Toast.makeText(mMainActivity, "Unable to set camera preview.  Make sure nothing else is using the camera, and re-launch the app.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
				mMainActivity.finish();
			}
			mAutoFocusOK = true;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;
		setCamera(mCamera);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
		mAutoFocusOK = false;
	}

}
