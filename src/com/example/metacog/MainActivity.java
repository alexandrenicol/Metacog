package com.example.metacog;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
		
		File theDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog");  // Defining Directory/Folder Name  
		try{   
		    if (!theDir.exists()){  // Checks that Directory/Folder Doesn't Exists!  
		    	boolean result = theDir.mkdir();    
		    	if(result){  
		    	//JOptionPane.showMessageDialog(null, "New Folder created!");
		    	}
		    }
		}catch(Exception e){  
		    //JOptionPane.showMessageDialog(null, e);  
		}
		
		utilisateur=(Button) findViewById(R.id.activity_main_utilisateur);
        utilisateur.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent t = new Intent(MainActivity.this, LogActivity.class);
				startActivity(t);
				
			}
		});
        admin=(Button) findViewById(R.id.activity_main_administrateur);
        admin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent t = new Intent(MainActivity.this, AdminActivity.class);
				startActivity(t);
			}
		});
        
        quit=(Button) findViewById(R.id.activity_main_quitter);
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
