package com.mobile.godot.core.service.message;

import android.os.Handler;
import android.os.Message;

public class GodotServiceHandler extends Handler {

	public GodotServiceHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
    public void handleMessage(Message mMessage) {	
			
			switch(mMessage.what) {
			
			case GodotMessage.Session.REGISTERED:
				handleRegistered(mMessage);
				break;
				
			case GodotMessage.Session.LOGGED_IN:
				handleLoggedIn(mMessage);
				break;
				
			case GodotMessage.Session.LOGGED_OUT:
				handleLoggedOut(mMessage);
				break;
				
			case GodotMessage.Session.NOT_LOGGED:
				handleNotLogged(mMessage);
				break;
				
			case GodotMessage.Entity.CAR_CREATED:
				handleCarCreated(mMessage);
				break;
				
			case GodotMessage.Entity.CAR_REMOVED:
				handleCarRemoved(mMessage);
				break;
				
			case GodotMessage.Entity.COOWNER_ADDED:
				handleCoownerAdded(mMessage);
				break;
				
			case GodotMessage.Entity.COOWNER_REMOVED:
				handleCoownerRemoved(mMessage);
				break;
			
			case GodotMessage.Core.MESSAGE_FOUND:
				handleMessageFound(mMessage);
				break;
				
			case GodotMessage.Core.MESSAGE_NOT_FOUND:
				handleMessageNotFound(mMessage);
				break;
				
			case GodotMessage.Core.MESSAGE_SENT:
				handleMessageSent(mMessage);
				break;
				
			case GodotMessage.Core.MESSAGE_READ:
				handleMessageRead(mMessage);
				break;
				
			case GodotMessage.Core.DRIVER_UPDATED:
				handleDriverUpdated(mMessage);
				break;
				
			case GodotMessage.Error.NFC_ERROR:
				handleNfcError(mMessage);
				break;
				
			case GodotMessage.Error.QRCODE_ERROR:
				handleQrCodeError(mMessage);
				break;
				
			case GodotMessage.Error.UNAUTHORIZED:
				handleUnauthorizedError(mMessage);
				break;
				
			case GodotMessage.Error.REDIRECTION_ERROR:
				handleRedirectionError(mMessage);
				break;
				
			case GodotMessage.Error.SERVER_ERROR:
				handleServerError(mMessage);
				break;
				
			default:
				break;
			}		
    }	

	public void handleRegistered(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleLoggedIn(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleLoggedOut(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleNotLogged(Message mMessage) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleCarCreated(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleCarRemoved(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleCoownerAdded(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleCoownerRemoved(Message mMessage) {
		// TODO Auto-generated method stub
		
	}
	
	public void handleMessageFound(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleMessageNotFound(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleMessageSent(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleMessageRead(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleDriverUpdated(Message mMessage) {
		// TODO Auto-generated method stub
		
	}	
	
	public void handleNfcError(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleQrCodeError(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleUnauthorizedError(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleRedirectionError(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

	public void handleServerError(Message mMessage) {
		// TODO Auto-generated method stub
		
	}

}
