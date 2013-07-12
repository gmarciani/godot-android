package com.mobile.godot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;

public class GodotController {
	
	private static GodotController godotController;
	private String myId = new String();
	private Listener listener;
	private Approacher approacher;
	
	private int listenerStatus;
	private int approacherStatus;

	private GodotController(String myId) {
		this.myId = myId;
		this.listener = new Listener();
		this.listener.execute(new String[] {this.myId});
	}
	
	public static GodotController getInstance(String myId) {
		if (godotController == null) {
			godotController = new GodotController(myId);
		}
		
		return godotController;
	}
	
	public synchronized int listen() {	    
	    return this.listenerStatus;
	}
	
	public synchronized int approach(String carId) {
		this.approacher = new Approacher(this.myId, carId);
		this.approacher.start();
	    return this.approacherStatus;
	}
	
	private class Listener extends AsyncTask<String, Void, Integer> {

		public Listener()  {}

		@Override
		protected Integer doInBackground(String... args) {
			
			String myId = args[0];
			int result;
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpGet httpget = new HttpGet("http://www.godot.mobi/Listen?myId=" + myId);		    
		    
		    //List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		    //params.add(new BasicNameValuePair("myId", myId));		    
		    
		    try {
		    	//httpget.setEntity(new UrlEncodedFormEntity(params));
		    	HttpResponse response = httpclient.execute(httpget);
				result = response.getStatusLine().getStatusCode();
				return result;
			} catch (IOException e) {
				return -3;
			}      
			
		}
		
		@Override
	    protected void onPostExecute(Integer result) {
	      listenerStatus = result;
	    }
	}
	
	private class Approacher extends Thread {
		
		private String myId;
		private String carId;
		
		public Approacher(String myId, String carId)  {
			super();
			this.myId = myId;
			this.carId = carId;			
		}
		
		@Override
		public void run() {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://www.godot.mobi/Approach?myId=" + this.myId + "&carId=" + this.carId);		    
		    
		    //List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		    //params.add(new BasicNameValuePair("myId", myId));
		    //params.add(new BasicNameValuePair("carId", carId));
		    
		    try {
				//httppost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse response = httpclient.execute(httpget);
				approacherStatus = response.getStatusLine().getStatusCode();
				return;
			} catch (IOException e) {
				approacherStatus = -2;
			}  		
			
		}
		
	}

}
