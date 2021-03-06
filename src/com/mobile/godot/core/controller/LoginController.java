/*  Godot: Android App. 
    
   Copyright 2013 Giacomo Marciani <giacomo.marciani@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.   
 */

package com.mobile.godot.core.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.model.UserBean;
import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotCoreHandler;
import com.mobile.godot.core.service.task.GodotAction;

import android.content.Context;
import android.util.SparseIntArray;

public class LoginController {
	
	private static LoginController godotController;	
	
	private static GodotCoreHandler mHandler;

	private LoginController(Context mContext, GodotCoreHandler mHandler) {}
	
	private LoginController() {}
	
	public static synchronized LoginController getInstance(Context context, GodotCoreHandler handler) {
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
	
	public synchronized void register(UserBean user) {
		
		String servlet = "AddUser";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("firstname", user.getFirstname()));
		params.add(new BasicNameValuePair("lastname", user.getLastname()));
		params.add(new BasicNameValuePair("mail", user.getMail()));
		params.add(new BasicNameValuePair("username", user.getUsername()));
		params.add(new BasicNameValuePair("password", user.getPassword()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Session.REGISTERED);
		mMessageMap.append(HttpURLConnection.HTTP_CONFLICT, GodotMessage.Error.CONFLICT);
		mMessageMap.append(HttpURLConnection.HTTP_NOT_ACCEPTABLE, GodotMessage.Error.UNACCEPTABLE);		
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}

}
