package com.mobile.godot.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.os.AsyncTask;

public class GodotController {
	
	private static final int GODOT_ACTION_LISTEN = 1;
	private static final int GODOT_ACTION_POP = 2;
	private static final int GODOT_ACTION_APPROACH = 3;
	private static final int GODOT_ACTION_TRANSICTION = 4;
	
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

	private GodotController(GodotServiceHandler mHandler) {
		this.godotService = new GodotService(mHandler);
		this.godotService.execute(new String[]{});
	}
	
	public static synchronized GodotController getInstance(GodotServiceHandler mHandler) {
		if (godotController == null) {
			godotController = new GodotController(mHandler);
		}
		
		return godotController;
	}
	
	public synchronized void listen(int myId) {
		this.godotService.setMyId(myId);
		this.godotService.setAction(GODOT_ACTION_LISTEN);
		//this.godotListener.setListenerActiveStatus(true);
		//this.godotListener.execute(new String[] {myId});
		//this.godotListener.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {myId});
	}
	
	public synchronized void pop(int myId) {	
		this.godotService.setMyId(myId);
		this.godotService.setAction(GODOT_ACTION_POP);
		//this.godotPopper.setPopperActiveStatus(true);
		//this.godotPopper.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {myId});
		//this.godotPopper.execute(new String[] {myId});
		//Popper mPopper = new Popper(myId);
		//mPopper.start();
		//this.resumeListener();
	}
	
	public synchronized void approach(int myId, int carId) {
		this.godotService.setMyId(myId);
		this.godotService.setCarId(carId);
		this.godotService.setAction(GODOT_ACTION_APPROACH);
		//this.godotApproacher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {myId, carId});
		//this.godotApproacher.execute(new String[] {myId, carId});
		//Approacher mApproacher = new Approacher(this.mHandler, myId, carId);
		//mApproacher.start();
	}
	
	public synchronized void waitForInput() {
		this.godotService.setAction(GODOT_ACTION_TRANSICTION);
	}
	
	private class GodotService extends AsyncTask<String, Void, Void> {

		private GodotServiceHandler mHandler;
		
		private int action;
		private int myId = -1;
		private int carId = -1;
		
		public GodotService(GodotServiceHandler mHandler) {
			this.mHandler = mHandler;
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
						this.setAction(GODOT_ACTION_TRANSICTION);
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
				case GODOT_ACTION_TRANSICTION:
					while(this.getAction() == GODOT_ACTION_TRANSICTION) {
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
