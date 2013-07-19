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
