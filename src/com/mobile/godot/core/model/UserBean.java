package com.mobile.godot.core.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	private String firstname;
	private String lastname;
	private String mail;
	private String username;
	private String password;

	public UserBean(){}

	public String getFirstname() {
		return firstname;
	}

	public UserBean setFirstname(String nome) {
		this.firstname = nome;
		return this;
	}

	public String getLastname() {
		return this.lastname;
	}

	public UserBean setLastname(String cognome) {
		this.lastname = cognome;
		return this;
	}

	public String getMail() {
		return this.mail;
	}

	public UserBean setMail(String mail) {
		this.mail = mail;
		return this;
	}

	public String getUsername() {
		return this.username;
	}

	public UserBean setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return this.password;
	}

	public UserBean setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public JSONObject toJSON() {
		JSONObject jObj = new JSONObject();
		
		try {
			jObj.put("firstname", this.firstname);
			jObj.put("lastname", this.lastname);
			jObj.put("mail", this.mail);
			jObj.put("username", this.username);
			jObj.put("password", this.password);
		} catch (JSONException exc) {
			exc.printStackTrace();
		}	
		
		return jObj;
	}
	
	public UserBean fromJSON(JSONObject jObj) {
		
		try {
			this.setFirstname(jObj.getString("firstname"));
			this.setLastname(jObj.getString("lastname"));
			this.setMail(jObj.getString("mail"));
			this.setUsername(jObj.getString("username"));
			this.setPassword(jObj.getString("password"));
		} catch (JSONException exc) {
			exc.printStackTrace();
		}		
		
		return this;
	}
	
	public UserBean fromJSONString(String JSONString) {
		
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
