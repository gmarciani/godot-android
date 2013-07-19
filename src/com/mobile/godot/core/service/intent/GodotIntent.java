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

public final class GodotIntent {

	private GodotIntent() {}		
	
	public static final String SESSION = "com.mobile.godot.intent.category.SESSION";
	
	public static final String ENTITY = "com.mobile.godot.intent.category.ENTITY";
	
	public static final String CORE = "com.mobile.godot.intent.category.CORE";
	
	public static final class Session {
		
		private Session() {}
		
		public static final String REGISTER = "com.mobile.godot.intent.action.REGISTER";
		
		public static final String LOGIN = "com.mobile.godot.intent.action.LOGIN";
		
		public static final String LOGOUT = "com.mobile.godot.intent.action.LOGOUT";
		
	}
	
	public static final class Entity {
		
		private Entity() {}
		
		public static final String ADD_CAR = "com.mobile.godot.intent.action.ADD_CAR";
		
		public static final String REMOVE_CAR = "com.mobile.godot.intent.action.REMOVE_CAR";
		
		public static final String ADD_COOWNER = "com.mobile.godot.intent.action.ADD_COOWNER";		
		
		public static final String REMOVE_COOWNER = "com.mobile.godot.intent.action.REMOVE_COOWNER";
		
	}
	
	public static final class Core {
		
		private Core() {}
		
		public static final String LISTEN = "com.mobile.godot.intent.action.LISTEN";
		
		public static final String APPROACH = "com.mobile.godot.intent.action.APPROACH";
		
		public static final String POP = "com.mobile.godot.intent.action.POP";
		
	}	

}
