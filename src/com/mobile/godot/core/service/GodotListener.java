package com.mobile.godot.core.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotPushNotificator;
import com.mobile.godot.core.service.message.GodotServiceHandler;


import android.content.Context;
import android.os.AsyncTask;

public class GodotListener extends AsyncTask<String, Void, Void> {
	
	public static final int STATE_LISTENING = 1;
	
	public static final int STATE_SYNC_STATE = 0;
	
	public static final int STATE_UNDEFINED = -1;
	
	private int state;
	
	private static final String URL = "http://www.godot.mobi/Listen?username=";
	
	private String username;

	private GodotServiceHandler mHandler;
	private GodotPushNotificator mNotificator;
	
	public GodotListener(Context context, GodotServiceHandler mHandler) {
		this.mHandler = mHandler;
		this.mNotificator = GodotPushNotificator.getInstance(context);
		this.setState(STATE_UNDEFINED);
	}
	
	@Override
	protected Void doInBackground(String... args) {
		
		this.setState(STATE_LISTENING);
		
		while(true) {
			
			switch(this.getState()) {
			
			case STATE_LISTENING:
				this.doListening(this.getUsername());
				break;
				
			case STATE_SYNC_STATE:
				
				while (this.getState() == STATE_SYNC_STATE) {
					//
				}
				
			default:
				break;
				
			}
			
		}
	}	

	@Override
	protected void onCancelled() {
		this.mHandler.removeCallbacksAndMessages(null);
	}
	
	private void doListening(String username) {
		
		if (username.isEmpty() || username == null) {
			return;
		}			
		
		URL mURL = null;
			
		try {
			mURL = new URL(URL + this.getUsername());
		} catch (MalformedURLException exc) {
			exc.printStackTrace();
		}
		
		int mResult;
		
		try {				
			
			HttpURLConnection mConnection = (HttpURLConnection) mURL.openConnection();
			mConnection.setRequestMethod("GET");
			mConnection.connect();
			mResult = mConnection.getResponseCode();
			mConnection.disconnect();
			
		} catch (ProtocolException exc) {
			
			mResult = exc.hashCode();
			
		} catch (IOException exc) {
			
			mResult = exc.hashCode();
		}
		
		if (mResult == 302) {
			
			this.setState(STATE_SYNC_STATE);
			this.mHandler.obtainMessage(GodotMessage.Core.MESSAGE_FOUND).sendToTarget();
			this.mNotificator.pushNotification(GodotPushNotificator.GODOT_PUSH_NOTIFICATION_MOVE_CAR);	
			
			
		}
		
	}
	
	private String getUsername() {
		return this.username;
	}
	
	public synchronized void setUsername(String username) {
		this.username = username;
	}

	public int getState() {
		return this.state;
	}

	public synchronized void setState(int state) {
		this.state = state;
	}		
	
}
