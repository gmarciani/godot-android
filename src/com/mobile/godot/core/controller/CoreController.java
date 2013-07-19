package com.mobile.godot.core.controller;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.mobile.godot.core.model.CarBean;
import com.mobile.godot.core.model.LoginBean;
import com.mobile.godot.core.model.UserBean;
import com.mobile.godot.core.service.message.GodotMessage;
import com.mobile.godot.core.service.message.GodotCoreHandler;
import com.mobile.godot.core.service.task.GodotAction;

import android.util.SparseIntArray;

public class CoreController {
	
	private static CoreController godotController;	
	
	private static GodotCoreHandler mHandler;
	
	private CoreController() {}
	
	public static synchronized CoreController getInstance(GodotCoreHandler handler) {
		if (godotController == null) {
			godotController = new CoreController();
		}
		
		mHandler = handler;	
		
		return godotController;
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
	
	public synchronized void addCar(CarBean car) {
		
		String servlet = "AddCar";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("carName", car.getName()));
		params.add(new BasicNameValuePair("username", car.getOwnerUsername()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.CAR_CREATED);
		mMessageMap.append(HttpURLConnection.HTTP_CONFLICT, GodotMessage.Error.CONFLICT);
		mMessageMap.append(HttpURLConnection.HTTP_NOT_ACCEPTABLE, GodotMessage.Error.UNACCEPTABLE);		
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void removeCar(String carName, LoginBean login) {
		
		String servlet = "RemoveCar";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("carName", carName));
		params.add(new BasicNameValuePair("username", login.getUsername()));
		params.add(new BasicNameValuePair("password", login.getPassword()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.CAR_REMOVED);
		mMessageMap.append(HttpURLConnection.HTTP_UNAUTHORIZED, GodotMessage.Error.UNAUTHORIZED);		
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void addCoOwner(String carName, String coOwnerUsername, LoginBean login) {
		
		String servlet = "AddCoOwner";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("carName", carName));
		params.add(new BasicNameValuePair("coOwnerUsername", coOwnerUsername));
		params.add(new BasicNameValuePair("username", login.getUsername()));
		params.add(new BasicNameValuePair("password", login.getPassword()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.COOWNER_ADDED);
		mMessageMap.append(HttpURLConnection.HTTP_UNAUTHORIZED, GodotMessage.Error.UNAUTHORIZED);	
		mMessageMap.append(HttpURLConnection.HTTP_NOT_FOUND, GodotMessage.Error.NOT_FOUND);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void removeCoOwner(String carName, String coOwnerUsername, LoginBean login) {
		
		String servlet = "RemoveCoOwner";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("carName", carName));
		params.add(new BasicNameValuePair("coOwnerUsername", coOwnerUsername));
		params.add(new BasicNameValuePair("username", login.getUsername()));
		params.add(new BasicNameValuePair("password", login.getPassword()));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.COOWNER_REMOVED);
		mMessageMap.append(HttpURLConnection.HTTP_UNAUTHORIZED, GodotMessage.Error.UNAUTHORIZED);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void findCarByDriver(String username) {
		
		String servlet = "FindCarByDriver";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.CAR_BY_DRIVER_FOUND);	
		mMessageMap.append(HttpURLConnection.HTTP_NOT_FOUND, GodotMessage.Error.NOT_FOUND);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	
	public synchronized void findCarsByOwner(String username) {
		
		String servlet = "FindCarsByOwner";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		SparseIntArray mMessageMap = new SparseIntArray();
		mMessageMap.append(HttpURLConnection.HTTP_OK, GodotMessage.Entity.CARS_BY_OWNER_FOUND);	
		mMessageMap.append(HttpURLConnection.HTTP_NOT_FOUND, GodotMessage.Error.NOT_FOUND);
		
		GodotAction action = new GodotAction(servlet, params, mMessageMap, mHandler);
		Thread tAction = new Thread(action);
		tAction.start();
		
	}
	

}
