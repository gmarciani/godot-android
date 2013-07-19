package com.mobile.godot.core.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;

	public LoginBean() {}

	public String getUsername() {
		return this.username;
	}

	public LoginBean setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return this.password;
	}

	public LoginBean setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public JSONObject toJSON() {		
		JSONObject jObj = new JSONObject();
		
		try {
			jObj.put("username", this.username);
			jObj.put("password", this.password);
		} catch (JSONException exc) {
			exc.printStackTrace();
		}		
		
		return jObj;
	}
	
	public LoginBean fromJSON(JSONObject jObj) {
		
		try {
			this.setUsername(jObj.getString("username"));
			this.setPassword(jObj.getString("password"));
		} catch (JSONException exc) {
			exc.printStackTrace();
		}		
		
		return this;
	}	
	
	public LoginBean fromJSONString(String JSONString) {
		
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(JSONString);
		} catch (JSONException exc) {
			exc.printStackTrace();
		}
		
		return this.fromJSON(jObj);
	}
	
	public String toJSONString() {		
		return this.toJSON().toString();
	}

}
