package com.mobile.godot;

import java.io.UnsupportedEncodingException;
import com.mobile.godot.controller.GodotController;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private GodotController godotController;
	private String myId = "1";
	
	private EditText etCarId;
	private Button btnListen;
	private Button btnApproach;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		godotController = GodotController.getInstance(myId);
		
		etCarId = (EditText) findViewById(R.id.etCarId);
		btnListen = (Button) findViewById(R.id.btnListen);
		btnApproach = (Button) findViewById(R.id.btnApproach);
		
		btnListen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	
				
				int status = godotController.listen();
				Toast.makeText(getApplicationContext(), String.valueOf(status), Toast.LENGTH_SHORT).show();
								
			}
			
		});
		
		btnApproach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {			
				String carId = etCarId.getText().toString();
				
				godotController = GodotController.getInstance(myId);
				
				int status = godotController.approach(carId);
				Toast.makeText(getApplicationContext(), String.valueOf(status), Toast.LENGTH_SHORT).show();
								
			}
			
		});
		
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
	
	
}
