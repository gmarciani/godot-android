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

package com.mobile.godot.core.service.intent;

public final class GodotIntentResult {

	private GodotIntentResult() {}
	
	public static final class Session {
		
		private Session() {}
		
		public static final String REGISTERED = "com.mobile.godot.intent.result.session.REGISTERED";
		
		public static final String LOGGED_IN = "com.mobile.godot.intent.result.session.LOGGED_IN";
		
		public static final String NOT_LOGGED = "com.mobile.godot.intent.result.session.NOT_LOGGED";
		
		public static final String LOGGED_OUT = "com.mobile.godot.intent.result.session.LOGGED_OUT";
		
	}
	
	public static final class Entity {
		
		private Entity() {}
		
		public static final String CAR_CREATED = "com.mobile.godot.intent.result.entity.CAR_CREATED";
		
		public static final String COOWNER_ADDED = "com.mobile.godot.intent.result.entity.COOWNER_ADDED";
		
		public static final String CAR_REMOVED = "com.mobile.godot.intent.result.entity.CAR_REMOVED";
		
		public static final String COOWNER_REMOVED = "com.mobile.godot.intent.result.entity.COOWNER_REMOVED";
		
	}
	
	public static final class Core {
		
		private Core() {}
		
		public static final String MESSAGE_FOUND = "com.mobile.godot.intent.result.core.MESSAGE_FOUND";
		
		public static final String MESSAGE_NOT_FOUND = "com.mobile.godot.intent.result.core.MESSAGE_NOT_FOUND";
		
		public static final String DRIVER_UPDATED = "com.mobile.godot.intent.core.DRIVER_UPDATED";
		
		public static final String MESSAGE_SENT = "com.mobile.godot.intent.core.MESSAGE_SENT";
		
		public static final String MESSAGE_READ = "com.mobile.godot.intent.core.MESSAGE_READ";
		
	}	
	
	public static final class Error {
		
		private Error() {}
		
		public static final String UNAUTHORIZED = "com.mobile.godot.intent.result.error.UNAUTHORIZED";
		
		public static final String SERVER_ERROR = "com.mobile.godot.intent.result.error.SERVER";
		
		public static final String REDIRECTION_ERROR = "com.mobile.godot.intent.result.error.REDIRECTION";
		
		public static final String QRCODE_ERROR = "com.mobile.godot.intent.result.error.QRCODE";
		
		public static final String NFC_ERROR = "com.mobile.godot.intent.result.error.NFC";
		
	}

}
