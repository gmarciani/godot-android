package com.mobile.godot.activity;

import java.io.UnsupportedEncodingException;

import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;

import com.mobile.godot.R;
import com.mobile.godot.controller.GodotController;
import com.mobile.godot.entity.GodotPushNotificator;
import com.mobile.godot.entity.GodotServiceHandler;
import com.mobile.godot.util.ui.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LaunchActivity extends Activity {

	//Fullscreen UI Management Constants
	private static final boolean AUTO_HIDE = true;
	private static final int AUTO_HIDE_DELAY_MILLIS = 5000;
	private static final boolean TOGGLE_ON_CLICK = true;
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	
	//UI Management Handlers
	private SystemUiHider mSystemUiHider;
  	Handler mHideHandler = new Handler();
  	
  	//UI Management Runnable
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
	
	//Controller
	private GodotController godotController;
	
	//Push Notifications
	NotificationManager notificationManager;
	
	//QrCodes
	private ZXingLibConfig zxingLibConfig;
	private Handler handler = new Handler();
	
	//Dialogs
	public AlertDialog alertDialogMessageFound;
	
	//Views
	private Button buttonScan;
	private Button buttonOption;
	private Button buttonQuit;
	
	//Click Listeners
  	private class ScanListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {	
			scan();
		}
		
	}
	
	private class OptionListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			option();						
		}
		
	}
	
	private class QuitListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			quit();
					
		}
		
	}
  	
	//Touch Listener
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};
		
	//Service Message Handler
	private GodotServiceHandler mHandler = new GodotServiceHandler() {  		
  		
  		@Override
  		public void handleMessageFound() { 
  			AlertDialog alertDialogMessageFound = buildDialogMessageFound();
  			alertDialogMessageFound.show();  			 			
  		}
  		
  		@Override
  		public void hendleMessageSent() {
  			Toast.makeText(getApplicationContext(), R.string.toast_message_sent, Toast.LENGTH_LONG).show();  			
  		}
  		
  		@Override
  		public void handleDriverUpdate() {
  			Toast.makeText(getApplicationContext(), R.string.toast_driver_update, Toast.LENGTH_LONG).show();  			
  		}
  		
  		@Override
  		public void handleListenerError() {
  			Toast.makeText(getApplicationContext(), R.string.toast_listener_error, Toast.LENGTH_LONG).show();  			
  		}
  		
  		@Override
  		public void handleApproacherError() {
  			Toast.makeText(getApplicationContext(), R.string.toast_approacher_error, Toast.LENGTH_LONG).show();  			
  		}
  		
  	};

  	//User Variables
	private int myId = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_launch);
		
		initializeFullScreenMode();
		
		initializeZxing();	
		
		handleNfcAvailability();
		
		buttonScan = (Button) findViewById(R.id.button_scan);
		buttonOption = (Button) findViewById(R.id.button_option);
		buttonQuit = (Button) findViewById(R.id.button_quit);
		
		buttonScan.setOnClickListener(new ScanListener());
		buttonOption.setOnClickListener(new OptionListener());
		buttonQuit.setOnClickListener(new QuitListener());
		
		buttonScan.setOnTouchListener(mDelayHideTouchListener);
		buttonOption.setOnTouchListener(mDelayHideTouchListener);
		buttonQuit.setOnTouchListener(mDelayHideTouchListener);
		
		godotController = GodotController.getInstance(getApplicationContext(), mHandler);
		
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
	}	
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		delayedHide(100);
	}
	
	public void initializeFullScreenMode() {
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView
							.animate()
							.translationY(visible ? 0 : mControlsHeight)
							.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE
							: View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});
	}
	
	public void initializeZxing() {
		zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		handlePushNotification();
		
		if (handleNetworkAvailability()) {
			godotController.listen(myId);
		}
		
		if (isNfcAvailable()) {
			Intent intent = getIntent();
			NdefMessage msgs[];
			
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
				Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				if (rawMsgs != null) {
					msgs = new NdefMessage[rawMsgs.length];
					for (int i = 0; i < rawMsgs.length; i++) {
						msgs[i] = (NdefMessage) rawMsgs[i];
					}
					       
					for (int i = 0; i < msgs.length; i++) {
						showGodotMessage(msgs[i]);										
					}
				}
			}			
		}
	}
	
	public void onStart() {
		super.onStart();
		handlePushNotification();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                
                if (scanResult == null) {
                    return;
                }
                
                final String result = scanResult.getContents();
                
                if (result != null) {
                    handler.post(new Runnable() {
                    	
                        @Override
                        public void run() {
                        	approach(Integer.parseInt(result));                        	
                        }
                        
                    });
                    
                }
                
                break;
            default:
        }
    }
		
	public void scan() {		
		IntentIntegrator.initiateScan(LaunchActivity.this, zxingLibConfig);		
	}
	
	public void approach(int carId) {		
		Toast.makeText(getApplicationContext(), R.string.toast_approaching, Toast.LENGTH_LONG).show();
		godotController.approach(myId, carId);		
	}
	
	public void option() {		
		Toast.makeText(getApplicationContext(), "Option", Toast.LENGTH_SHORT).show();		
	}
	
	public void quit() {		
		Toast.makeText(getApplicationContext(), "Quit", Toast.LENGTH_SHORT).show();			
	}
	
	public AlertDialog buildDialogMessageFound() {
		
		AlertDialog dialogMessageFound;
			
		AlertDialog.Builder builderDialogMessageFound = new AlertDialog.Builder(this);
		builderDialogMessageFound.setTitle(R.string.app_name);
		builderDialogMessageFound.setMessage(R.string.dialog_message_found);
			
		builderDialogMessageFound.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
			
	           public void onClick(DialogInterface dialog, int id) {
	               godotController.pop(myId);
	           }
           
	    });	
			
		dialogMessageFound = builderDialogMessageFound.create();
		
		return dialogMessageFound;
	}
	
	public void showGodotMessage(NdefMessage msg) {
		for (NdefRecord record : msg.getRecords()) {
			byte data[] = record.getPayload();
			String str;
			try {
				str = new String(data, "UTF-8");
				Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
				toast.show();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public boolean handleNetworkAvailability() {
		if (!isNetworkAvailable()) {
			Toast.makeText(getApplicationContext(), R.string.toast_no_network, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	public boolean handleNfcAvailability() {
		if (!isNfcAvailable()) {
			Toast.makeText(getApplicationContext(), R.string.toast_no_nfc, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	public void handlePushNotification() {		
		notificationManager.cancel(GodotPushNotificator.GODOT_PUSH_NOTIFICATION_MOVE_CAR);
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    
	    return false;
	}
	
	public boolean isNfcAvailable() {
		NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null) {
		    return true;
		}
		
		return false;
	}
	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}	
	
}
