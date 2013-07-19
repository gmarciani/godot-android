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

package com.mobile.godot.core.service.broadcast;

import com.mobile.godot.core.service.intent.GodotIntentResult;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GodotListenerBroadcastReceiver extends BroadcastReceiver {

	public GodotListenerBroadcastReceiver() {}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(GodotIntentResult.Core.MESSAGE_FOUND)) {
			
			handleMessageFound();
			
		}

	}

	public void handleMessageFound() {}

}
