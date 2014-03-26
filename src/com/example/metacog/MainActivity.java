package com.example.metacog;

import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity 
{ 
	private Button utilisateur;
	private Button admin;
	private Button quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		utilisateur=(Button) findViewById(R.id.utilisateur);
        utilisateur.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent t = new Intent(MainActivity.this, LogActivity.class);
				startActivity(t);
				
			}
		});
        admin=(Button) findViewById(R.id.administrateur);
        admin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent t = new Intent(MainActivity.this, AdminActivity.class);
				startActivity(t);
			}
		});
        
        quit=(Button) findViewById(R.id.quitter);
    	quit.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			finish();
    		}
    		});
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.module_choice, menu);
		return true;
	}
*/
}
