package com.stevenlevis.flashlightvideo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class MainActivity extends Activity {

	private void launchVideoCamera() {
		String action = MediaStore.ACTION_VIDEO_CAPTURE;
		Intent searchIntent = new Intent(action);
		startActivity(searchIntent);
		finish();
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onResume() {
		launchVideoCamera();
		super.onResume();
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

}
