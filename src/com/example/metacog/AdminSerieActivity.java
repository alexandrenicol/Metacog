package com.example.metacog;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class AdminSerieActivity extends Activity {
	private TextView text1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie);

		text1 = (TextView) findViewById(R.id.textView1);
	    
	    Bundle extras = getIntent().getExtras();
	    text1.setText(extras.getString("serieChoisi"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_serie, menu);
		return true;
	}

	



}
