package com.mobile.godot.core.service.task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotCoreHandler;
import com.mobile.godot.util.GodotURLUtils;

import android.util.SparseIntArray;

public class GodotAction implements Runnable {
	
	private String mServlet;
	private List<BasicNameValuePair> mParams;
	private SparseIntArray mMessageMap;
	private GodotCoreHandler mHandler;	

	public GodotAction(String mServlet, List<BasicNameValuePair> mParams, SparseIntArray mMessageMap, GodotCoreHandler mHandler) {
		this.mServlet = mServlet;
		this.mParams = mParams;
		this.mMessageMap = mMessageMap;
		this.mHandler = mHandler;
	}

	@Override
	public void run() {
		
		System.out.println("inside runnable");
		
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		
		URL url = GodotURLUtils.parseToURL(this.mServlet, this.mParams);	
		
		System.out.println("url: " + url);
		
		HttpURLConnection connection = null;
		InputStream iStream;
		InputStream eStream;
		int responseCode = 0;
		String data = null;		
		
		try {	
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();
			
			if (!url.getHost().equals(connection.getURL().getHost())) {
				this.mHandler.obtainMessage(GodotMessage.Error.REDIRECTION_ERROR).sendToTarget();
				connection.disconnect();
				return;
			}
			
			try {		
				
				iStream = new BufferedInputStream(connection.getInputStream());
				BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
	         	data = bReader.readLine();
				responseCode = connection.getResponseCode();
				
			} catch (IOException exc) {			
				
				eStream = new BufferedInputStream(connection.getErrorStream());
	         	BufferedReader bReader = new BufferedReader(new InputStreamReader(eStream));
	         	data = bReader.readLine();	
	         	responseCode = connection.getResponseCode();

			} finally {
	         	
	         	if (data != null) {
	         		
	         		this.mHandler.obtainMessage(this.mMessageMap.get(responseCode), data).sendToTarget();
	         		
	    		} else {
	    			
	    			this.mHandler.obtainMessage(this.mMessageMap.get(responseCode)).sendToTarget();
	    			
	    		}
			}         	
         	
		} catch (IOException exc) {
			
			this.mHandler.obtainMessage(GodotMessage.Error.SERVER_ERROR).sendToTarget();
			
		} finally {
			
			connection.disconnect();
			
		}
		
	}

}
