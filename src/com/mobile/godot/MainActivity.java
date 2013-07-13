package com.mobile.godot;

import java.io.UnsupportedEncodingException;

import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;

import com.mobile.godot.controller.GodotController;
import com.mobile.godot.controller.GodotServiceHandler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private GodotController godotController;
	
	private ZXingLibConfig zxingLibConfig;
	private Handler handler = new Handler();
	
	public AlertDialog alertDialogMessageFound;
	
	private Button btnScan;
	
	private int myId = 1;
    	
  	private GodotServiceHandler mHandler = new GodotServiceHandler() {  		
  		
  		@Override
  		public void handleMessageFound() { 
  			AlertDialog alertDialogMessageFound = buildDialog();
  			alertDialogMessageFound.show();  			 			
  		}
  		
  		@Override
  		public void hendleMessageSent() {
  			Toast.makeText(getApplicationContext(), "Message sent!", Toast.LENGTH_SHORT).show();  			
  		}
  		
  		@Override
  		public void handleDriverUpdate() {
  			Toast.makeText(getApplicationContext(), "Now you are the driver!", Toast.LENGTH_SHORT).show();  			
  		}
  		
  		@Override
  		public void handleListenerError() {
  			Toast.makeText(getApplicationContext(), "Listener Error!", Toast.LENGTH_SHORT).show();  			
  		}
  		
  		@Override
  		public void handleApproacherError() {
  			Toast.makeText(getApplicationContext(), "Approacher Error!", Toast.LENGTH_SHORT).show();  			
  		}
  		
  	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;        		
		
		btnScan = (Button) findViewById(R.id.btnScan);
		
		btnScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				
				IntentIntegrator.initiateScan(MainActivity.this, zxingLibConfig);
				
			}
			
		});
		
		godotController = GodotController.getInstance(mHandler);
		godotController.listen(myId);
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                
                if (scanResult == null) {
                    return;
                }
                
                final String result = scanResult.getContents();
                
                if (result != null) {
                    handler.post(new Runnable() {
                    	
                        @Override
                        public void run() {
                        	Toast.makeText(getApplicationContext(), "Approaching...", Toast.LENGTH_SHORT).show();
                        	approach(Integer.parseInt(result));
                        	
                        }
                        
                    });
                    
                }
                
                break;
            default:
        }
    }
	
	public AlertDialog buildDialog() {
		
		AlertDialog dialogMessageFound;
			
		AlertDialog.Builder builderDialogMessageFound = new AlertDialog.Builder(this);
		builderDialogMessageFound.setTitle("Godot");
		builderDialogMessageFound.setMessage("Move your car!");
			
		builderDialogMessageFound.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
	           public void onClick(DialogInterface dialog, int id) {
	               godotController.pop(myId);
	           }
           
	    });	
			
		dialogMessageFound = builderDialogMessageFound.create();
		
		return dialogMessageFound;
	}
	
	public void approach(int carId) {
		
		godotController.approach(myId, carId);	
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Intent intent = getIntent();
		NdefMessage msgs[];
		
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
				       
				for (int i = 0; i < msgs.length; i++) {
					showGodotMessage(msgs[i]);										
				}
			}
		}
		/*
		for (int i = 0; i < 100; i++) {
			NdefMessage msg = createGodotMessage(String.valueOf(i));
			showGodotMessage(msg);
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public NdefMessage createGodotMessage(String carId) {
		NdefRecord aar = NdefRecord.createApplicationRecord("com.mobile.godot");
		byte data[] = carId.getBytes();
		
		NdefRecord record = NdefRecord.createMime("text/plain", data);
	    
	    NdefMessage message = new NdefMessage(aar, record);
	    
	    return message;
	}
	
	public void showGodotMessage(NdefMessage msg) {
		for (NdefRecord record : msg.getRecords()) {
			byte data[] = record.getPayload();
			String str;
			try {
				str = new String(data, "UTF-8");
				Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG);
				toast.show();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager cm = (ConnectivityManager) 
	      getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	
	
}
