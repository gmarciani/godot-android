package com.mobile.godot.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.mobile.godot.entity.GodotPushNotificator;
import com.mobile.godot.entity.GodotServiceHandler;

import android.content.Context;
import android.os.AsyncTask;

public class GodotController {
	
	private static final int GODOT_ACTION_LISTEN = 1;
	private static final int GODOT_ACTION_POP = 2;
	private static final int GODOT_ACTION_APPROACH = 3;
	private static final int GODOT_ACTION_SYNC_STATE = 4;
	
	private static GodotController godotController;	
	
	private GodotService godotService;
    
    public static final int GODOT_DRIVER_UPDATE = 1;
	public static final int GODOT_MESSAGE_SENT = 2;
	public static final int GODOT_MESSAGE_FOUND = 3;
	public static final int GODOT_MESSAGE_NOT_FOUND = 4;
	
	public static final int LISTENER_STATUS_UNDEFINED = -1;
	public static final int GODOT_LISTENER_ERROR = -2;		
	public static final int APPROACHER_STATUS_UNDEFINED = -3;
	public static final int GODOT_APPROACHER_ERROR = -4;

	private GodotController(Context context, GodotServiceHandler mHandler) {
		this.godotService = new GodotService(context, mHandler);
		this.godotService.execute(new String[]{});
	}
	
	public static synchronized GodotController getInstance(Context context, GodotServiceHandler mHandler) {
		if (godotController == null) {
			godotController = new GodotController(context, mHandler);
		}
		
		return godotController;
	}
	
	public synchronized void listen(int myId) {
		this.godotService.setMyId(myId);
		this.godotService.setAction(GODOT_ACTION_LISTEN);
	}
	
	public synchronized void pop(int myId) {	
		this.godotService.setMyId(myId);
		this.godotService.setAction(GODOT_ACTION_POP);
	}
	
	public synchronized void approach(int myId, int carId) {
		this.godotService.setMyId(myId);
		this.godotService.setCarId(carId);
		this.godotService.setAction(GODOT_ACTION_APPROACH);
	}
	
	public synchronized void waitForInput() {
		this.godotService.setAction(GODOT_ACTION_SYNC_STATE);
	}
	
	private class GodotService extends AsyncTask<String, Void, Void> {

		private GodotServiceHandler mHandler;
		private GodotPushNotificator mNotificator;
		
		private int action;
		private int myId = -1;
		private int carId = -1;
		
		public GodotService(Context context, GodotServiceHandler mHandler) {
			this.mHandler = mHandler;
			this.mNotificator = GodotPushNotificator.getInstance(context);
		}
		
		@Override
		protected Void doInBackground(String... args) {
			
			URL url = null;
			
			while(true) {
				
				switch(action) {
				case GODOT_ACTION_LISTEN:
					
					if (this.myId == -1) {
						break;
					}
					
					try {
						url = new URL("http://www.godot.mobi/Listen?myId=" + this.myId);
					} catch (MalformedURLException exc) {
						exc.printStackTrace();
					}
					
					int result;
					
					try {				
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.connect();
						result = connection.getResponseCode();
						connection.disconnect();
					} catch (ProtocolException exc) {
						result = exc.hashCode();
					} catch (IOException exc) {
						result = exc.hashCode();
					}
					
					switch(result) {
					case 302:
						if (this.mHandler.hasMessages(GODOT_MESSAGE_FOUND)) {
							break;
						}						
						this.mHandler.obtainMessage(GODOT_MESSAGE_FOUND).sendToTarget();
						this.mNotificator.pushNotification(GodotPushNotificator.GODOT_PUSH_NOTIFICATION_MOVE_CAR);
						this.setAction(GODOT_ACTION_SYNC_STATE);
						break;
					case 200:
						this.mHandler.obtainMessage(GODOT_MESSAGE_NOT_FOUND).sendToTarget();
						break;
					default:
						this.mHandler.obtainMessage(GODOT_LISTENER_ERROR, result, -1).sendToTarget();
						break;
					}			
					
					break;
				case GODOT_ACTION_POP:
					
					if (this.myId == -1) {
						break;
					}
					
					try {
						url = new URL("http://www.godot.mobi/PopMessage?myId=" + this.myId);
					} catch (MalformedURLException exc) {
						exc.printStackTrace();
					}
					
					try {				
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.connect();
						result = connection.getResponseCode();	
						connection.disconnect();
					} catch (ProtocolException exc) {
						exc.printStackTrace();
					} catch (IOException exc) {
						exc.printStackTrace();
					}
					
					this.setAction(GODOT_ACTION_LISTEN);
					
					break;
				case GODOT_ACTION_APPROACH:
					
					if (this.myId == -1 || this.carId == -1) {
						break;
					}
					
					try {
						url = new URL("http://www.godot.mobi/Approach?myId=" + this.myId + "&carId=" + this.carId);
					} catch (MalformedURLException exc) {
						exc.printStackTrace();
					}
					
					try {				
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("GET");
						connection.connect();
						result = connection.getResponseCode();
						connection.disconnect();
					} catch (ProtocolException exc) {
						result = exc.hashCode();
					} catch (IOException exc) {
						result = exc.hashCode();
					}
					
					switch(result) {
					case 202:
						this.mHandler.obtainMessage(GODOT_DRIVER_UPDATE).sendToTarget();
						break;
					case 201:
						this.mHandler.obtainMessage(GODOT_MESSAGE_SENT).sendToTarget();
						break;
					default:
						mHandler.obtainMessage(GODOT_APPROACHER_ERROR, result, -1).sendToTarget();
						break;
					}	
					
					this.setAction(GODOT_ACTION_LISTEN);
					
					break;
				case GODOT_ACTION_SYNC_STATE:
					while(this.getAction() == GODOT_ACTION_SYNC_STATE) {
						//
					}
					
					break;
				default:
					break;
				}
			}
		}
		
		public synchronized void setAction(int action) {
			this.action = action;
		}
		
		public synchronized int getAction() {
			return this.action;
		}
		
		public synchronized void setMyId(int myId) {
			this.myId = myId;
		}
		
		public synchronized void setCarId(int carId) {
			this.carId = carId;
		}
		
	}

}
