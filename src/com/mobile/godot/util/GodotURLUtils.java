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

package com.mobile.godot.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

public class GodotURLUtils {

	private GodotURLUtils() {}
	
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
