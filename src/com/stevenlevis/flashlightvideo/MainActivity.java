package com.stevenlevis.flashlightvideo;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Camera mCamera;
	private FlashlightVideoView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
		mView = new FlashlightVideoView(this);
		setContentView(mView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			mCamera = Camera.open();
		} catch (RuntimeException e) {
        	Toast.makeText(this, "Unable to set camera preview.  Make sure nothing else is using the camera, or contact the application developer.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		if (mCamera != null) {
			mCamera.setDisplayOrientation(90);
		}

		mView = new FlashlightVideoView(this);
		setContentView(mView);
		mView.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		super.onPause();
        releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mView.setCamera(null);
        	mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
	}
	
	@Override
	public void onBackPressed() {
        releaseCamera();
		finish();
	}

}
