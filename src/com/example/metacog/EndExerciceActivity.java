package com.example.metacog;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndExerciceActivity extends Activity {
	
	private Button menu;
	private Button next_serie ;
	private Integer id_module;
	private String module_name;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_exercice);
		menu = (Button) findViewById(R.id.end_exercice_menu);
		next_serie = (Button) findViewById(R.id.end_exercice_next_serie);
		
		Bundle extra = getIntent().getExtras();
		module_name = extra.getString("module");
		id_module = extra.getInt("moduleId");
	    
		menu.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent t=new Intent (EndExerciceActivity.this,MainActivity.class);
				startActivity(t);
				finish();
    		}
    		});
	    
		next_serie.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t=new Intent (EndExerciceActivity.this,SerieActivity.class);
				t.putExtra("moduleId",id_module);
		    	t.putExtra("module",module_name);
				startActivity(t);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_exercice, menu);
		return true;
	}

}
