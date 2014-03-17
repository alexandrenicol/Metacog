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

public class AdminModuleActivity extends Activity {

	private TextView text1;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_admin_module);
	    
	    text1 = (TextView) findViewById(R.id.textView1);
	    
	    Bundle extras = getIntent().getExtras();
	    text1.setText(extras.getString("textView1"));
	    
	    // TODO Auto-generated method stub
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_module, menu);
		return true;
	}

	

	

}
