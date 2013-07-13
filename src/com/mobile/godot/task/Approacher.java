package com.mobile.godot.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.mobile.godot.controller.GodotController;
import com.mobile.godot.controller.GodotServiceHandler;

public class Approacher extends Thread{
	
	private GodotServiceHandler mHandler;
	private String myId = new String();
	private String carId = new String();

	public Approacher(GodotServiceHandler mHandler, String myId, String carId) {
		this.mHandler = mHandler;
		this.myId = myId;
		this.carId = carId;
	}
	
	@Override
	public void run() {
		
		URL url = null;
		
		try {
			url = new URL("http://www.godot.mobi/Approach?myId=" + this.myId + "&carId=" + this.carId);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		int result = GodotController.APPROACHER_STATUS_UNDEFINED;
		
		try {				
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			result = connection.getResponseCode();
		} catch (ProtocolException exc) {
			result = exc.hashCode();
		} catch (IOException exc) {
			result = exc.hashCode();
		}
		
		switch(result) {
		case 202:
			this.mHandler.obtainMessage(GodotController.GODOT_DRIVER_UPDATE).sendToTarget();
			break;
		case 201:
			this.mHandler.obtainMessage(GodotController.GODOT_MESSAGE_SENT).sendToTarget();
			break;
		default:
			this.mHandler.obtainMessage(GodotController.GODOT_APPROACHER_ERROR, result, -1).sendToTarget();
			break;
		}	
		
	}

}
