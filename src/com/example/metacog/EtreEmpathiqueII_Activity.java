package com.example.metacog;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EtreEmpathiqueII_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_etre_empathique_2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.etre_empathique_2_, menu);
		return true;
	}

}
