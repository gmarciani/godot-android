package com.mobile.godot.core.service.message;

import com.mobile.godot.R;
import com.mobile.godot.activity.MainActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class GodotPushNotificator {
	
	public static final int GODOT_PUSH_NOTIFICATION_MOVE_CAR = 24031986;
	
	private static GodotPushNotificator singletonGodotPushNotificator;
	
	private Context context;

	private GodotPushNotificator(Context context) {		
		this.context = context;		
	}
	
	public synchronized static GodotPushNotificator getInstance(Context context) {		
		if (singletonGodotPushNotificator == null) {			
			singletonGodotPushNotificator = new GodotPushNotificator(context) ;			
		}
		
		return singletonGodotPushNotificator;
	}
	
	public void pushNotification(int notification) {		
		switch(notification) {
		
		case GODOT_PUSH_NOTIFICATION_MOVE_CAR:			
			pushMoveCarNotification();
			
		default:
			break;
			
		}
		
	}
	
	private void pushMoveCarNotification() {
		
		Resources mResources = this.context.getResources();
		
		String appName = mResources.getString(R.string.app_name);
		String messageFound = mResources.getString(R.string.dialog_message_found);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(appName)
		.setContentText(messageFound)
		.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		
		Intent resultIntent = new Intent(context, MainActivity.class);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(GODOT_PUSH_NOTIFICATION_MOVE_CAR, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(GODOT_PUSH_NOTIFICATION_MOVE_CAR, mBuilder.build());
		
	}

}
