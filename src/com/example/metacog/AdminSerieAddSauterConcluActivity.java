package com.example.metacog;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AdminSerieAddSauterConcluActivity extends Activity {

	private Button add_image1;
	private Button add_image_field;
	
	private String externalStorage;
	private String structureFilename;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	private Integer cursor = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie_add_sauter_conclu);
		// Show the Up button in the action bar.
		setupActionBar();
		
		externalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        structureFilename = externalStorage+"/structure_modules.xml";
		
		Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    
		
		add_image1=(Button) findViewById(R.id.add_image1);
		add_image1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				FileChooserDialog dialog = new FileChooserDialog(AdminSerieAddSauterConcluActivity.this);
			    dialog.loadFolder(Environment.getExternalStorageDirectory().getAbsolutePath());
			     
			    // Assign listener for the select event.
			    dialog.addListener(AdminSerieAddSauterConcluActivity.this.onFileSelectedListener);
		    		
		    	// Define the filter for select images.
		    	dialog.setFilter(".*jpg|.*png|.*gif|.*JPG|.*PNG|.*GIF");
		    	dialog.setShowOnlySelectable(false);
			     
			    dialog.show();
			}
		});
		
		add_image_field=(Button) findViewById(R.id.add_image_field);
		add_image_field.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				LinearLayout linearLayoutButton = (LinearLayout)findViewById(R.id.linearLayoutButton);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				
				cursor++;
				final Integer id_ = cursor;
				Button btn1 = new Button(AdminSerieAddSauterConcluActivity.this);
				btn1.setText("button");
				btn1.setId(id_);
				linearLayoutButton.addView(btn1, lp);
				
				btn1 = ((Button) findViewById(id_));
				
			    btn1.setOnClickListener(new View.OnClickListener() {
			        public void onClick(View view) {
			            Toast.makeText(view.getContext(),
			                    "Button clicked index = " + id_, Toast.LENGTH_SHORT)
			                    .show();
			        }
			    });
			}
		});
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_serie_add_sauter_conclu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// ---- Methods for display the results ----- //
	
		private FileChooserDialog.OnFileSelectedListener onFileSelectedListener = new FileChooserDialog.OnFileSelectedListener() {
			public void onFileSelected(Dialog source, File file) {
				source.hide();
				Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this, "File selected: " + file.getAbsolutePath(), Toast.LENGTH_LONG);
				toast.show();
				
				
				
			}
			public void onFileSelected(Dialog source, File folder, String name) {
				source.hide();
				Toast toast = Toast.makeText(AdminSerieAddSauterConcluActivity.this, "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
				toast.show();
			}
		};

}
