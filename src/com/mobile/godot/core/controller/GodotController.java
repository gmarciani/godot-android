package com.mobile.godot.core.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.service.GodotListener;
import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotServiceHandler;
import com.mobile.godot.core.service.task.GodotAction;

import android.content.Context;
import android.os.AsyncTask;
import android.util.SparseIntArray;

public class GodotController {
	
	private static GodotController godotController;	
	
	private static GodotListener godotListener;
	
	private static Context mContext;
	private static GodotServiceHandler mHandler;
	
	private GodotController() {}
	
	public static synchronized GodotController getInstance(Context context, GodotServiceHandler handler) {
		if (godotController == null) {
			godotController = new GodotController();
		}
		
		mContext = context;
		mHandler = handler;	
		godotListener = new GodotListener(mContext, mHandler);
		
		return godotController;
	}
	
	public synchronized void login(LoginBean login) {
		
		String servlet = "Login";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", login.getUsername()));
		params.add(new BasicNameValuePair("password", login.getPassword()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Session.LOGGED_IN);
		mMessageMap.append(HttpURLConnection.HTTP_UNAUTHORIZED, GodotMessage.Session.NOT_LOGGED);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void logout(LoginBean login) {
		this.stopListener();
	}
	
	public synchronized void startListener(String username) {
		godotListener.setUsername(username);		
		
		if (godotListener.getState() == GodotListener.STATE_UNDEFINED) {				
			
			godotListener.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{});
			
		}		
		
		godotListener.setState(GodotListener.STATE_LISTENING);
	}
	
	public synchronized void pauseListener() {
		godotListener.setState(GodotListener.STATE_SYNC_STATE);
	}
	
	public synchronized void stopListener() {
		godotListener.cancel(true);
	}
	
	public synchronized void pop(String username) {	
		
		String servlet = "PopMessage";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Core.MESSAGE_READ);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void approach(String username, String carName) {
		
		String servlet = "Approach";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("carName", carName));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_ACCEPTED, GodotMessage.Core.DRIVER_UPDATED);
		mMessageMap.append(HttpURLConnection.HTTP_CREATED, GodotMessage.Core.MESSAGE_SENT);
		mMessageMap.append(HttpURLConnection.HTTP_NOT_FOUND, GodotMessage.Core.CAR_NOT_FOUND);
		mMessageMap.append(HttpURLConnection.HTTP_UNAUTHORIZED, GodotMessage.Error.UNAUTHORIZED);		
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}

}
