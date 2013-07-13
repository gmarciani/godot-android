package com.mobile.godot.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Popper extends Thread{
	
	private String myId;

	public Popper(String myId) {
		this.myId = myId;
	}
	
	@Override
	public void run() {
		
		URL url = null;
		
		try {
			url = new URL("http://www.godot.mobi/PopMessage?myId=" + this.myId);
		} catch (MalformedURLException exc) {
			exc.printStackTrace();
		}
		
		try {				
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
		} catch (ProtocolException exc) {
			exc.printStackTrace();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		
	}

}
