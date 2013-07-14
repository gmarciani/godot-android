package com.mobile.godot.entity;

import com.mobile.godot.controller.GodotController;

import android.os.Handler;
import android.os.Message;

public class GodotServiceHandler extends Handler {

	public GodotServiceHandler() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void handleMessage(Message mMessage) {		
		
        switch (mMessage.what) {
        case GodotController.GODOT_DRIVER_UPDATE:
            handleDriverUpdate();
            break;
        case GodotController.GODOT_MESSAGE_SENT:
        	hendleMessageSent();
        	break;
        case GodotController.GODOT_MESSAGE_FOUND:
        	handleMessageFound();
        	break;   
        case GodotController.GODOT_LISTENER_ERROR:
        	handleListenerError();
        	break;
        case GodotController.GODOT_APPROACHER_ERROR:
        	handleApproacherError();
        	break;
        default:
        	break;
        }
    }	

	public void handleMessageFound() {
		// TODO Auto-generated method stub
		
	}

	public void hendleMessageSent() {
		// TODO Auto-generated method stub
		
	}

	public void handleDriverUpdate() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleListenerError() {
		// TODO Auto-generated method stub
		
	}
	
	public void handleApproacherError() {
		// TODO Auto-generated method stub
		
	}

}
