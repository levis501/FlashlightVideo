package com.stevenlevis.flashlightvideo;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

	
	private Camera mCamera;
	private FlashlightVideoView mSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSurfaceView = new FlashlightVideoView(this);
		mSurfaceView.getHolder().addCallback(this);
		setContentView(mSurfaceView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCamera = Camera.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
        if (mCamera != null) {
        	mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters params = mCamera.getParameters();
		params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
		mCamera.setParameters(params);
		try {
			mCamera.setPreviewDisplay(mSurfaceView.getHolder());
		} catch (IOException e) {
			e.printStackTrace();
        	Toast.makeText(this, "Unable to open a camera preview.  Make sure nothing else is using the camera, or contact the application developer.", Toast.LENGTH_LONG).show();
        	finish();
		}
		mCamera.setDisplayOrientation(90);
		mCamera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				e.printStackTrace();
	        	Toast.makeText(this, "Unable to set the camera preview display.  Make sure nothing else is using the camera, or contact the application developer.", Toast.LENGTH_LONG).show();
	        	finish();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	public void autoFocus() {
		if (mCamera != null) {
			mCamera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
				}});
		}
	}
}
