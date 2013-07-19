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

package com.mobile.godot.core.service.message;

public final class GodotMessage {

	private GodotMessage() {}
	
	public static final class Category {
		
		private Category() {}
		
		public static final int SESSION = 100;
		
		public static final int ENTITY = 200;
		
		public static final int CORE = 300;
		
		public static final int ERROR = 400;
		
	}
	
	public static final class Session {
		
		private Session() {}
		
		public static final int REGISTERED = 101;
		
		public static final int LOGGED_IN = 102;
		
		public static final int NOT_LOGGED = 103;
		
		public static final int LOGGED_OUT = 104;
		
	}
	
	public static final class Entity {
		
		private Entity() {}
		
		public static final int CAR_CREATED = 201;
		
		public static final int COOWNER_ADDED = 202;
		
		public static final int CAR_REMOVED = 203;
		
		public static final int COOWNER_REMOVED = 204;
		
		public static final int CAR_BY_DRIVER_FOUND = 205;
		
		public static final int CARS_BY_OWNER_FOUND = 206;
		
	}
	
	public static final class Core {
		
		private Core() {}
		
		public static final int MESSAGE_FOUND = 301;
		
		public static final int MESSAGE_NOT_FOUND = 302;
		
		public static final int DRIVER_UPDATED = 303;
		
		public static final int MESSAGE_SENT = 304;
		
		public static final int MESSAGE_READ = 305;
		
		public static final int CAR_NOT_FOUND = 306;
		
	}	
	
	public static final class Error {
		
		private Error() {}
		
		public static final int UNAUTHORIZED = 401;	
		
		public static final int UNACCEPTABLE = 402;	
		
		public static final int CONFLICT = 403;	
		
		public static final int NOT_FOUND = 404;	
		
		public static final int SERVER_ERROR = 405;
		
		public static final int REDIRECTION_ERROR = 406;
		
		public static final int QRCODE_ERROR = 407;
		
		public static final int NFC_ERROR = 408;	
		
	}

}
