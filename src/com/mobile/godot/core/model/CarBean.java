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

package com.mobile.godot.core.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class CarBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String ownerUsername;
	private String driverUsername;
	private boolean message;

	public CarBean() {}

	public String getName() {
		return name;
	}

	public CarBean setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getOwnerUsername() {
		return ownerUsername;
	}

	public CarBean setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
		return this;
	}

	public String getDriverUsername() {
		return driverUsername;
	}

	public CarBean setDriverUsername(String driverUsername) {
		this.driverUsername = driverUsername;
		return this;
	}	

	public boolean getMessage() {
		return message;
	}

	public CarBean setMessage(boolean message) {
		this.message = message;
		return this;
	}	
	
	public JSONObject toJSON() {
		JSONObject jObj = new JSONObject();
		
		try {
			jObj.put("name", this.name);
			jObj.put("ownerUsername", this.ownerUsername);
			jObj.put("driverUsername", this.driverUsername);
			jObj.put("message", this.message);
		} catch (JSONException exc) {
			exc.printStackTrace();
		}		
		
		return jObj;
	}
	
	public CarBean fromJSON(JSONObject jObj) {
		
		try {
			this.setName(jObj.getString("name"));
			this.setOwnerUsername(jObj.getString("ownerUsername"));
			this.setDriverUsername(jObj.getString("driverUsername"));
			this.setMessage(jObj.getBoolean("message"));
		} catch (JSONException exc) {
			exc.printStackTrace();
		}		
		
		return this;
	}
	
	public CarBean fromJSONString(String JSONString) {
		
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
