/*  Godot: Android App. 
    
   Copyright 2013 Giacomo Marciani <giacomo.marciani@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.   
 */

package com.mobile.godot.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobile.godot.R;
import com.mobile.godot.core.GodotPreference;
import com.mobile.godot.core.controller.LoginController;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.model.UserBean;
import com.mobile.godot.core.service.intent.GodotIntent;
import com.mobile.godot.core.service.intent.GodotIntentExtra;
import com.mobile.godot.core.service.message.GodotCoreHandler;
import com.mobile.godot.util.ui.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LauncherActivity extends Activity {
	
	//Controller
	private LoginController loginController;
	private static boolean AUTO_LOGIN = true;
	
	//Preferences
	private SharedPreferences mPref;
		
	//Fullscreen UI Management Constants
	private static final boolean AUTO_HIDE = true;
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
	
	//Views
	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	private Button btnRegister;
	
	//Dialogs
	private AlertDialog dialogUserRegistration;
	
	//Click Listeners
	View.OnClickListener mLoginClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			
			LoginBean loginBean = new LoginBean()
			.setUsername(username)
			.setPassword(password);
			
			login(loginBean);			
		}
		
	};	
	
	View.OnClickListener mRegisterClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {			
			dialogUserRegistration.show();			
		}
		
	};
	
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
	
	//Text Watcher
	TextWatcher mDelayHideTextWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (AUTO_HIDE) {
				delayedHide(LONG_AUTO_HIDE_DELAY_MILLIS);
			}
			
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
		public void handleRegistered(Message mMessage) {
			Toast.makeText(getApplicationContext(), R.string.toast_registration_success, Toast.LENGTH_SHORT).show();			
		}
		
		@Override
		public void handleConflictError(Message mMessage) {
			Toast.makeText(getApplicationContext(), R.string.toast_username_already_in_use, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void handleLoggedIn(Message mMessage) {
			
			String data = (String) mMessage.obj;
			
			if (data != null) {
				JSONObject jObj = null;
				
				try {
					jObj = new JSONObject(data);
				} catch (JSONException exc) {
					exc.printStackTrace();
				}
				
				UserBean loggedUserBean = new UserBean().fromJSON(jObj);
				
				LoginBean loginBean = new LoginBean()
				.setUsername(loggedUserBean.getUsername())
				.setPassword(loggedUserBean.getPassword());				
				setCachedLogin(loginBean);
				
				Toast.makeText(getApplicationContext(), R.string.toast_loading_personal_data, Toast.LENGTH_SHORT).show();		
				
				goToMain(loggedUserBean);
			}							
		}
		
		@Override
		public void handleNotLogged(Message mMessage) {
			Toast.makeText(getApplicationContext(), R.string.toast_access_denied, Toast.LENGTH_SHORT).show();			
		}
		
	};	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_launcher);
		
		this.initializeFullScreenMode();
		
		etUsername = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);		
		btnLogin = (Button) findViewById(R.id.button_login);
		btnRegister = (Button) findViewById(R.id.button_register);
		
		btnLogin.setOnClickListener(mLoginClickListener);
		btnRegister.setOnClickListener(mRegisterClickListener);
		
		etUsername.addTextChangedListener(mDelayHideTextWatcher);
		etPassword.addTextChangedListener(mDelayHideTextWatcher);
		btnLogin.setOnTouchListener(mDelayHideTouchListener);
		
		dialogUserRegistration = buildDialogUserRegistration();
		
		this.loginController = LoginController.getInstance(this.getApplicationContext(), this.mHandler);
		
		Intent intentBackToLogin = this.getIntent();
		boolean loginMode = intentBackToLogin.getBooleanExtra(GodotIntentExtra.EXTRA_LOGIN_MODE, true);
		this.setAutoLogin(loginMode);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if (AUTO_LOGIN) {
			LoginBean cachedLoginBean = this.getCachedLogin();
			
			if (cachedLoginBean != null) {
				etUsername.setText(cachedLoginBean.getUsername());
				etPassword.setText(cachedLoginBean.getPassword());
				this.login(cachedLoginBean);
			} else {
				mSystemUiHider.show();
			}
		}		
	}
	
	public void setAutoLogin(boolean bool) {
		AUTO_LOGIN = bool;
	}
	
	private LoginBean getCachedLogin() {
		LoginBean login = null;
		
		this.mPref = getSharedPreferences(GodotPreference.PREF, MODE_PRIVATE);
		
		String JSONString = this.mPref.getString(GodotPreference.LOGIN, null);
		
		if (JSONString != null) {
			login = new LoginBean().fromJSONString(JSONString);
		}
		
		return login;
	}
	
	private void setCachedLogin(LoginBean loginBean) {	
		
		String JSONString = loginBean.toJSONString();
		
		this.mPref = getSharedPreferences(GodotPreference.PREF, MODE_PRIVATE);
		
		SharedPreferences.Editor editor = this.mPref.edit();					
	    
		editor.putString(GodotPreference.LOGIN, JSONString);
	    
	    editor.commit();
	}
	
	private void login(LoginBean login) {
		String username = login.getUsername();
		String password = login.getPassword();
		
		if (username.isEmpty() || username == null || password.isEmpty() || password == null ) {
			Toast.makeText(getApplicationContext(), R.string.toast_login_wrong_syntax, Toast.LENGTH_SHORT).show();
		} else {
			this.loginController.login(login);
		}
	}	
	
	private void goToMain(UserBean user) {
		Intent intentLoggedUser = new Intent(this, MainActivity.class);
		intentLoggedUser.setAction(GodotIntent.Session.LOGIN);
		intentLoggedUser.putExtra(GodotIntentExtra.EXTRA_USER, user);
		this.startActivity(intentLoggedUser);
		
	}
	
	private void registerNewUser(UserBean user) {
		this.loginController.register(user);
	}
	
	private AlertDialog buildDialogUserRegistration() {
		
		AlertDialog dialogUserRegistration;
		
		AlertDialog.Builder builderDialogUserRegistration = new AlertDialog.Builder(this);
		builderDialogUserRegistration
		.setTitle(R.string.dilog_title_user_registration)
		.setIcon(R.drawable.ic_launcher);
		
		LayoutInflater inflaterDialogUserRegistration = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View contentDialogUserRegistration = inflaterDialogUserRegistration.inflate(R.layout.dialog_user_registration, (ViewGroup) findViewById(R.id.dialog_user_registration_root));			
		builderDialogUserRegistration.setView(contentDialogUserRegistration);
		
		final EditText etFirstname = (EditText) contentDialogUserRegistration.findViewById(R.id.etFirstname);	
		final EditText etLastname = (EditText) contentDialogUserRegistration.findViewById(R.id.etLastname);	
		final EditText etMail = (EditText) contentDialogUserRegistration.findViewById(R.id.etMail);	
		final EditText etUsername = (EditText) contentDialogUserRegistration.findViewById(R.id.etUsername);	
		final EditText etPassword = (EditText) contentDialogUserRegistration.findViewById(R.id.etPassword);		
		
		builderDialogUserRegistration.setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
			
			
           public void onClick(DialogInterface dialog, int id) {
        	   
        	   String firstname = etFirstname.getText().toString();
        	   String lastname = etLastname.getText().toString();
        	   String mail = etMail.getText().toString();
        	   String username = etUsername.getText().toString();
        	   String password = etPassword.getText().toString();
        	   
        	   UserBean user = new UserBean()
        	   .setFirstname(firstname)
        	   .setLastname(lastname)
        	   .setMail(mail)
        	   .setUsername(username)
        	   .setPassword(password);
        	   
        	   registerNewUser(user);
               
           }
	           
       });
		
		builderDialogUserRegistration.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
	           
			public void onClick(DialogInterface dialog, int id) {

				Toast.makeText(getApplicationContext(), R.string.toast_we_would_be_glad, Toast.LENGTH_SHORT).show();
				
	        }
			
	    });	
		
		dialogUserRegistration = builderDialogUserRegistration.create();
		
		return dialogUserRegistration;
	}
	
	private void initializeFullScreenMode() {		
		final View contentView = findViewById(R.id.fullscreen_content_logo);
		final View loginView = findViewById(R.id.fullscreen_content_login);
		
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
						mControlsHeight = loginView.getHeight();
					}
					
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
					}
					
					loginView
							.animate()
							.translationY(visible ? 0 : mControlsHeight)
							.setDuration(mShortAnimTime);
				} else {
					loginView.setVisibility(visible ? View.VISIBLE : View.GONE);
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
	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
	
}
