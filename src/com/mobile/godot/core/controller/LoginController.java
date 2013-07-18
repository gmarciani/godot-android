package com.mobile.godot.core.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotServiceHandler;
import com.mobile.godot.core.service.task.GodotAction;

import android.content.Context;
import android.util.SparseIntArray;

public class LoginController {
	
	private static LoginController godotController;	
	
	private static GodotServiceHandler mHandler;

	private LoginController(Context mContext, GodotServiceHandler mHandler) {}
	
	private LoginController() {}
	
	public static synchronized LoginController getInstance(Context context, GodotServiceHandler handler) {
		if (godotController == null) {
			godotController = new LoginController();
		}
		mHandler = handler;
		
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

}
