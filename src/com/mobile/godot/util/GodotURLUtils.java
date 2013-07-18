package com.mobile.godot.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class GodotURLUtils {

	public GodotURLUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static URL parseToURL(String servlet, List<BasicNameValuePair> params) {
		String parsedParams = "?";
		
		for (int count = 0; count < params.size(); count ++) {
			String paramName = params.get(count).getName();
			String paramValue = params.get(count).getValue();
			
			parsedParams += paramName + "=" + paramValue;
			parsedParams += (count + 1 < params.size()) ? "&" : "";
		}
		
		String parsedURL = "http://www.godot.mobi/" + servlet + parsedParams;	
		
		try {
			URL url = new URL(parsedURL);
			return url;
		} catch (MalformedURLException exc) {
			exc.printStackTrace();
			return null;
		}				
	}
	
	public static String parseToString(String servlet, List<BasicNameValuePair> params) {
		String parsedParams = "?";
		
		for (int count = 0; count < params.size(); count ++) {
			String paramName = params.get(count).getName();
			String paramValue = params.get(count).getValue();
			
			parsedParams += paramName + "=" + paramValue;
			parsedParams += (count + 1 < params.size()) ? "&" : "";
		}
		
		String parsedURL = "http://www.godot.mobi/" + servlet + parsedParams;	
		
		return parsedURL;				
	}

}
