package com.mobile.godot.core.service.message;

import android.os.Handler;
import android.os.Message;

public class GodotCoreHandler extends Handler {

	public GodotCoreHandler() {}

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
				
			case GodotMessage.Error.CONFLICT:
				handleConflictError(mMessage);
				break;
				
			case GodotMessage.Error.UNACCEPTABLE:
				handleUnacceptableError(mMessage);
				break;
				
			case GodotMessage.Error.NOT_FOUND:
				handleNotFoundError(mMessage);
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

	public void handleRegistered(Message mMessage) {}

	public void handleLoggedIn(Message mMessage) {}

	public void handleLoggedOut(Message mMessage) {}

	public void handleNotLogged(Message mMessage) {}
	
	public void handleCarCreated(Message mMessage) {}

	public void handleCarRemoved(Message mMessage) {}

	public void handleCoownerAdded(Message mMessage) {}

	public void handleCoownerRemoved(Message mMessage) {}
	
	public void handleMessageFound(Message mMessage) {}

	public void handleMessageNotFound(Message mMessage) {}

	public void handleMessageSent(Message mMessage) {}

	public void handleMessageRead(Message mMessage) {}

	public void handleDriverUpdated(Message mMessage) {}	
	
	public void handleNfcError(Message mMessage) {}

	public void handleQrCodeError(Message mMessage) {}

	public void handleUnauthorizedError(Message mMessage) {}
	
	public void handleUnacceptableError(Message mMessage) {}
	
	public void handleConflictError(Message mMessage) {}
	
	public void handleNotFoundError(Message mMessage) {}

	public void handleRedirectionError(Message mMessage) {}

	public void handleServerError(Message mMessage) {}

}
