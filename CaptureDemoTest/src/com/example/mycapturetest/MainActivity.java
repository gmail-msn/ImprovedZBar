package com.example.mycapturetest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.zxing.client.android.ScannerRelativeLayout;
import com.google.zxing.client.android.camera.CameraSettings;
import com.google.zxing.client.android.InterfaceBarCode;

public class MainActivity extends Activity implements InterfaceBarCode {

	private ScannerRelativeLayout scanner;
	private FrameLayout mFrameLayout;
	Handler handler = new Handler();
	Handler messageHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		CameraSettings.setCAMERA_FACING(CameraSettings.FACING_FRONT);
		CameraSettings.setAUTO_FOCUS(false);
		CameraSettings.setBEEP(true);
		// true： can distinguish continuously ，false：distinguish one time,then
		// you should call startScan
		CameraSettings.setBULKMODE(true);
		mFrameLayout = (FrameLayout) findViewById(R.id.scanner);

		// BEGIN: startScan
		// END: stopScan

		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				scanner = new ScannerRelativeLayout(MainActivity.this);
				scanner.setBarCodeCallBack(MainActivity.this);
				mFrameLayout.addView(scanner, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				scanner.startScan();

			}
		});

		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scanner.stopScan();
				mFrameLayout.removeAllViews();
			}
		});

	}

	@Override
	protected void onResume() {
		if (scanner != null)
			scanner.startScan();
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (scanner != null) {
			scanner.stopScan();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (scanner != null) {
			scanner.stopScan();
		}
		super.onDestroy();
	}

	@Override
	public void getData(String data) {
		Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
	}

}
