package com.mobile.godot.activity;

import java.io.UnsupportedEncodingException;

import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;

import com.mobile.godot.R;
import com.mobile.godot.core.GodotPreference;
import com.mobile.godot.core.controller.CoreController;
import com.mobile.godot.core.model.CarBean;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.model.UserBean;
import com.mobile.godot.core.service.GodotListenerService;
import com.mobile.godot.core.service.broadcast.GodotListenerBroadcastReceiver;
import com.mobile.godot.core.service.intent.GodotIntent;
import com.mobile.godot.core.service.intent.GodotIntentExtra;
import com.mobile.godot.core.service.intent.GodotIntentResult;
import com.mobile.godot.core.service.message.GodotCoreHandler;
import com.mobile.godot.util.ui.SystemUiHider;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	//Controller
	private CoreController controller;
	
	//Preferences
	private SharedPreferences mPref;

	//Fullscreen UI Management Constants
	private static final boolean AUTO_HIDE = true;
	private static final int SHORT_AUTO_HIDE_DELAY_MILLIS = 100;
	private static final int AUTO_HIDE_DELAY_MILLIS = 5000;
	private static final int LONG_AUTO_HIDE_DELAY_MILLIS = 10000;
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
	
	//QrCodes
	private ZXingLibConfig zxingLibConfig;
	private Handler handler = new Handler();
	
	//Push Notifications
	NotificationManager notificationManager;
	
	//Dialogs
	private AlertDialog dialogMessageFound;
	private AlertDialog dialogConfirmApproach;
	private AlertDialog dialogAddNewCar;
	
	//Views
	private Button buttonScan;
	private Button buttonManage;
	private Button buttonLogout;
	
	//Click Listeners
	View.OnClickListener mScanClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			scan();			
		}
	};
	
	View.OnClickListener mManageClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			manage();			
		}
	};
	
	View.OnClickListener mLogoutClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			logout();			
		}
	};
	
	//Alert Dialog Click Listener	
	private class MessageFoundClickListener implements DialogInterface.OnClickListener {
		
		private String mUsername;
		
		public MessageFoundClickListener(String username) {
			this.mUsername = username;
		}
				
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
   			controller.pop(this.mUsername);
   			
		}
		
	}
	
	private class ConfirmApproachClickListener implements DialogInterface.OnClickListener {
		
		private String username;
		private String carName;

		public ConfirmApproachClickListener(String username, String carName) {
			this.username = username;
			this.carName = carName;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
   			Toast.makeText(getApplicationContext(), R.string.toast_approaching, Toast.LENGTH_LONG).show();
   			controller.approach(this.username, this.carName);			
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
	
	//Editor Listener
	TextView.OnEditorActionListener mDelayHideEditorActionListener = new TextView.OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (AUTO_HIDE) {
				delayedHide(LONG_AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
		
	};
	
	//Handler
	private GodotCoreHandler mHandler = new GodotCoreHandler() {  		
  		
  		@Override
  		public void handleMessageFound(Message mMessage) {  	
  			dialogMessageFound.show();  			
  		}
  		
  		@Override
  		public void handleMessageSent(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_message_sent, Toast.LENGTH_SHORT).show();  			
  		}
  		
  		@Override
  		public void handleDriverUpdated(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_driver_update, Toast.LENGTH_SHORT).show();  			
  		}
  		
  		@Override
  		public void handleCarCreated(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_car_created, Toast.LENGTH_SHORT).show();
  			
  		}

  		@Override
  		public void handleCarRemoved(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_car_removed, Toast.LENGTH_SHORT).show();
  			
  		}

  		@Override
  		public void handleCoownerAdded(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_co_owner_added, Toast.LENGTH_SHORT).show();
  			
  		}

  		@Override
  		public void handleCoownerRemoved(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_co_owner_removed, Toast.LENGTH_SHORT).show();
  			
  		}
  		
  		@Override
  		public void handleConflictError(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_conflict_error, Toast.LENGTH_SHORT).show();
  			
  		}
  		
  		@Override
  		public void handleNotFoundError(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_not_found_error, Toast.LENGTH_SHORT).show();
  			
  		}
  		
  		@Override
  		public void handleUnacceptableError(Message mMessage) {
  			Toast.makeText(getApplicationContext(), R.string.toast_unacceptable_error, Toast.LENGTH_SHORT).show();
  			
  		}
  		
  	};
  	
  	private class ListenerBroadcast extends GodotListenerBroadcastReceiver {
  		
  		public ListenerBroadcast() {
  			super();
  		}
  		
  		@Override
		public void handleMessageFound() {
			dialogMessageFound.show(); 
		}
  		
  	}
  	
  	ListenerBroadcast mGodotListenerReceiver = new ListenerBroadcast();

  	//User Variables
	private UserBean userBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);		
		
		this.initializeFullScreenMode();
		
		this.setUser(this.getLoggedUser());
				
		this.refreshActionBar(this.getUser());
		
		this.getPreference();
		
		this.initializeZxing();	
		
		this.handleNfcAvailability();
		
		buttonScan = (Button) findViewById(R.id.button_scan);
		buttonManage = (Button) findViewById(R.id.button_manage);
		buttonLogout = (Button) findViewById(R.id.button_logout);
		
		buttonScan.setOnClickListener(mScanClickListener);
		buttonManage.setOnClickListener(mManageClickListener);
		buttonLogout.setOnClickListener(mLogoutClickListener);
		
		buttonScan.setOnTouchListener(mDelayHideTouchListener);
		buttonManage.setOnTouchListener(mDelayHideTouchListener);
		buttonLogout.setOnTouchListener(mDelayHideTouchListener);			
						
		Intent service = new Intent(this, GodotListenerService.class);
		service.setAction(GodotIntent.Core.LISTEN);
		service.putExtra(GodotIntentExtra.EXTRA_USERNAME, this.getUser().getUsername());
		this.startService(service);
		
		dialogMessageFound = buildDialogMessageFound();
		dialogAddNewCar = buildDialogAddNewCar();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		this.controller = CoreController.getInstance(this.mHandler);
		
		IntentFilter filterListener = new IntentFilter();
		filterListener.addAction(GodotIntentResult.Core.MESSAGE_FOUND);		
		
		this.registerReceiver(mGodotListenerReceiver, filterListener);
		
		if (handleNetworkAvailability()) {
			
			Intent intent = getIntent();
			NdefMessage msgs[];

			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
				Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				if (rawMsgs != null) {
					msgs = new NdefMessage[rawMsgs.length];
					for (int i = 0; i < rawMsgs.length; i++) {
						msgs[i] = (NdefMessage) rawMsgs[i];
					}
					
					if (msgs[0] != null) {
						String carName = this.getGodotTag(msgs[0]);
						
						if (carName != null) {
							this.controller.approach(this.getUser().getUsername(), carName);
						}
						
					}
					
				}
			}
			
		}
		
	}
	
	private String getGodotTag(NdefMessage msg) {
		
		NdefRecord[] records = msg.getRecords();
		String str = null;
		
		if (records[1] != null) {
			
			byte data[] = records[1].getPayload();
			
			try {
				str = new String(data, "UTF-8");
			} catch (UnsupportedEncodingException exc) {
				exc.printStackTrace();
			}				
		}
		
		return str;
	}
	
	/*
	//To show how Godot Tag has been created
	
	public NdefMessage createGodotMessage(String carId) {
		NdefRecord aar = NdefRecord.createApplicationRecord("com.mobile.godot");
		byte data[] = carId.getBytes();

		NdefRecord record = NdefRecord.createMime("text/plain", data);

	    NdefMessage message = new NdefMessage(aar, record);

	    return message;
	}*/	
	
	@Override
	protected void onPause() {
		super.onPause();
		
		this.unregisterReceiver(mGodotListenerReceiver);
	}	
	
	private UserBean getLoggedUser() {
		UserBean loggedUser = null;
		
		Intent intentLoggedUser = this.getIntent();
		if (intentLoggedUser.getAction().equals(GodotIntent.Session.LOGIN)) {
			loggedUser = (UserBean) intentLoggedUser.getSerializableExtra(GodotIntentExtra.EXTRA_USER);
		}			
		
		if (loggedUser == null) {
			this.logout();
			//loggedUser = this.getCachedUser();
		}
				
		return loggedUser;
	}

	private UserBean getUser() {
		return this.userBean;
	}
	
	private void setUser(UserBean userBean) {
		if (userBean == null) {
			this.logout();
		}
		this.userBean = userBean;
	}
	
	private void getPreference() {
		this.mPref = getSharedPreferences(GodotPreference.PREF, MODE_PRIVATE);
		
		//other preferences
	}
	
	private void savePreference() {
		mPref = getSharedPreferences(GodotPreference.PREF, MODE_PRIVATE);
		
	    SharedPreferences.Editor editor = this.mPref.edit();
	    
	    //Preferences
	    
	    editor.commit();		
	}	
	
	public void initializeZxing() {
		zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;
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
                        	approach(result);                        	
                        }
                        
                    });
                    
                }
                
                break;
                
            default:
            	break;
        }
    }
	
	protected void onStop() {
		super.onStop();
		
		savePreference();		
	}
		
	private void scan() {			
		IntentIntegrator.initiateScan(MainActivity.this, "QR_CODE", null, zxingLibConfig);	
	}
	
	private void approach(String carName) {		
		dialogConfirmApproach = buildDialogConfirmApproach(carName);
		dialogConfirmApproach.show();
	}
	
	private void manage() {		
		dialogAddNewCar.show();	
	}
	
	private void addNewCarWithCoOwners(String carName, String coOwners) {
		if (carName == null || carName.isEmpty() || coOwners == null || coOwners.isEmpty()) {
			Toast.makeText(getApplicationContext(), R.string.toast_invalid_input, Toast.LENGTH_SHORT).show();
			return;
		}
		
		CarBean car = new CarBean()
		.setName(carName)
		.setOwnerUsername(getUser().getUsername())
		.setDriverUsername(getUser().getUsername());
		
		this.controller.addCar(car);
		
		Handler sleepH = new Handler();
		
		sleepH.postDelayed(new Runnable() {
			
			@Override
			public void run() {}
			
		}, 1500);
		
		String[] coOwnersList = coOwners.split(",");
		
		LoginBean login = new LoginBean()
		.setUsername(this.getUser().getUsername())
		.setPassword(this.getUser().getPassword());
		
		for (int count = 0 ; count < coOwnersList.length ; count ++ ) {
			this.controller.addCoOwner(carName, coOwnersList[count], login);
		}
	}
	
	private void logout() {		
		
		Toast.makeText(getApplicationContext(), R.string.toast_logging_out, Toast.LENGTH_SHORT).show();
		
		Intent intentStopService = new Intent(this, GodotListenerService.class);
		this.stopService(intentStopService);
		
		Intent intentLogout = new Intent(this, LauncherActivity.class);
		intentLogout.putExtra(GodotIntentExtra.EXTRA_LOGIN_MODE, false);
		this.startActivity(intentLogout);
		
	}	
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    
	    return false;
	}
	
	private boolean handleNetworkAvailability() {
		if (!isNetworkAvailable()) {
			Toast.makeText(getApplicationContext(), R.string.toast_no_network, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
		
	private boolean isNfcAvailable() {
		NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null) {
		    return true;
		}
		
		return false;
	}
	
	private boolean handleNfcAvailability() {
		if (!isNfcAvailable()) {
			Toast.makeText(getApplicationContext(), R.string.toast_no_nfc, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	
	public void initializeFullScreenMode() {
		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);
		
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					controlsView
							.animate()
							.translationY(visible ? 0 : mControlsHeight)
							.setDuration(mShortAnimTime);
				} else {
					controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
				}

				if (visible && AUTO_HIDE) {
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

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
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		delayedHide(SHORT_AUTO_HIDE_DELAY_MILLIS);
	}
	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}	
	
	private AlertDialog buildDialogMessageFound() {
		
		AlertDialog dialogMessageFound;
			
		AlertDialog.Builder builderDialogMessageFound = new AlertDialog.Builder(this)
		.setTitle(R.string.app_name)
		.setIcon(R.drawable.ic_launcher)
		.setMessage(R.string.dialog_content_message_found);
			
		builderDialogMessageFound.setPositiveButton(R.string.dialog_button_ok, new MessageFoundClickListener(this.getUser().getUsername()));
		
		builderDialogMessageFound.setCancelable(true);
			
		dialogMessageFound = builderDialogMessageFound.create();
		
		return dialogMessageFound;
	}
	
	private AlertDialog buildDialogConfirmApproach(String carName) {
		
		AlertDialog dialogMessageFound;
		
		String message = getResources().getString(R.string.dialog_content_approaching);
		message += " " + carName + "?";
			
		AlertDialog.Builder builderDialogMessageFound = new AlertDialog.Builder(this)
		.setTitle(R.string.app_name)
		.setIcon(R.drawable.ic_launcher)
		.setMessage(message);
			
		builderDialogMessageFound.setPositiveButton(R.string.dialog_button_ok, new ConfirmApproachClickListener(this.getUser().getUsername(), carName));	
		
		builderDialogMessageFound.setCancelable(true);
			
		dialogMessageFound = builderDialogMessageFound.create();
		
		return dialogMessageFound;
	}
	
	private AlertDialog buildDialogAddNewCar() {
		
		AlertDialog dialogAddNewCar;
		
		AlertDialog.Builder builderDialogAddNewCar = new AlertDialog.Builder(this);
		builderDialogAddNewCar
		.setTitle(R.string.dialog_title_add_new_car)
		.setIcon(R.drawable.ic_launcher);
		
		LayoutInflater inflaterDialogAddNewCar = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View contentDialogAddNewCar = inflaterDialogAddNewCar.inflate(R.layout.dialog_car_registration, (ViewGroup) findViewById(R.id.dialog_add_car));			
		builderDialogAddNewCar.setView(contentDialogAddNewCar);
		
		final EditText etCarName = (EditText) contentDialogAddNewCar.findViewById(R.id.et_car_name);	
		final EditText etCoOwners = (EditText) contentDialogAddNewCar.findViewById(R.id.et_co_owners);		
		
		builderDialogAddNewCar.setPositiveButton(R.string.dialog_button_add_car, new DialogInterface.OnClickListener() {
			
			
           public void onClick(DialogInterface dialog, int id) {
        	   
        	   String carName = etCarName.getText().toString();
        	   String coOwners = etCoOwners.getText().toString();
        	   
        	   addNewCarWithCoOwners(carName, coOwners);
               
           }
	           
       });
		
		builderDialogAddNewCar.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
	           
			public void onClick(DialogInterface dialog, int id) {

				
	        }
			
	    });	
		
		dialogAddNewCar = builderDialogAddNewCar.create();
		
		return dialogAddNewCar;
	}

	private final void refreshActionBar(UserBean user) {
    	
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(user.getFirstname() + " " + user.getLastname());
    }
	
}
