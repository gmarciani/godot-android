package com.mobile.godot.core.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.mobile.godot.R;
import com.mobile.godot.activity.MainActivity;
import com.mobile.godot.core.service.intent.GodotIntent;
import com.mobile.godot.core.service.intent.GodotIntentExtra;
import com.mobile.godot.core.service.intent.GodotIntentResult;
import com.mobile.godot.core.service.message.GodotNotification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

public class GodotListenerService extends IntentService {
		
	NotificationManager notificationManager;

	public GodotListenerService(String name) {
		super(name);
	}
	
	public GodotListenerService() {
		super("GodotListenerService");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		if (intent.getAction().equals(GodotIntent.Core.LISTEN)) {
			
			String username = intent.getStringExtra(GodotIntentExtra.EXTRA_USERNAME);
			
			if (username == null) return;
			
			Thread mThread = new Thread(new ListenRunnable(username));
			mThread.start();
			
		}
		
	}
	
	public class ListenRunnable implements Runnable {
		
		private static final String URL = "http://www.godot.mobi/Listen?username=";
		
		private String username;
		
		public ListenRunnable(String username) {
			this.username = username;
		}

		@Override
		public void run() {
			
			while(true) {
				
				URL mURL = null;
				
				try {
					mURL = new URL(URL + username);
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
					
					pushMoveCarNotification();
					
					Intent intent = new Intent();
					intent.setAction(GodotIntentResult.Core.MESSAGE_FOUND);
					sendBroadcast(intent);	
					return;
					
				}
			}
			
		}	
		
		
		private void pushMoveCarNotification() {
			
			Context mContext = getApplicationContext();
			
			Resources mResources = mContext.getResources();
			
			String appName = mResources.getString(R.string.app_name);
			String messageFound = mResources.getString(R.string.dialog_message_found);
			
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.setAction(GodotIntentResult.Core.MESSAGE_READ);
			PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
						
			Notification mNotification = new NotificationCompat.Builder(mContext)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(appName)
			.setContentText(messageFound)
			.setContentIntent(pIntent)
			.setWhen(System.currentTimeMillis())
			.setDefaults(Notification.DEFAULT_SOUND)
			.setAutoCancel(true)
			.build();

			notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			
			notificationManager.notify(GodotNotification.MOVE_YOUR_CAR, mNotification);
			
		}
		
	}
	
}

