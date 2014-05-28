package com.example.metacog;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class AdminSerieActivity extends Activity {
	private TextView text1;
	
	private Button add;
	private Button delete;
	private Button retour;
	
	private Integer selectedModuleId;
	private String selectedModuleName;
	private Integer selectedSerieId;
	private String selectedSerieName;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_serie);

		text1 = (TextView) findViewById(R.id.textView1);
	    
	    Bundle extras = getIntent().getExtras();
	    selectedModuleId = extras.getInt("selectedModuleId");
	    selectedModuleName = extras.getString("selectedModuleName");
	    selectedSerieId = extras.getInt("selectedSerieId");
	    selectedSerieName = extras.getString("selectedSerieName");
	    
	    text1.setText(selectedModuleName+" - "+selectedSerieName);
	    
	    add=(Button) findViewById(R.id.activity_admin_serie_add);
	    retour=(Button) findViewById(R.id.activity_admin_serie_retour);
	    
	    add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent t;
				if (selectedModuleId==1) {
					t = new Intent(AdminSerieActivity.this, AdminSerieAddM1Activity.class);
				}else if (selectedModuleId==2){
					t = new Intent(AdminSerieActivity.this, AdminSerieAddEtreEmpaIIActivity.class);
				}else if (selectedModuleId==3){
					t = new Intent(AdminSerieActivity.this, AdminSerieAddEtreEmpaIIIActivity.class);
				}else if (selectedModuleId==4){
					t = new Intent(AdminSerieActivity.this, AdminSerieAddSauterConcluActivity.class);	
				}else{
					t = new Intent(AdminSerieActivity.this, AdminSerieAddM1Activity.class);
			    }
				t.putExtra("selectedModuleId", selectedModuleId);
        		t.putExtra("selectedModuleName", selectedModuleName);
        		t.putExtra("selectedSerieId", selectedSerieId);
        		t.putExtra("selectedSerieName", selectedSerieName);
				startActivity(t);
			}
		});
	    
	    retour.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent t=new Intent (AdminSerieActivity.this,AdminModuleActivity.class);
					t.putExtra("id_module", selectedModuleId);
					t.putExtra("name_module", selectedModuleName);
					startActivity(t);
					finish();
					
				}
		});
        
        
        delete=(Button) findViewById(R.id.activity_admin_serie_delete);
        delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent t = new Intent(AdminSerieActivity.this, AdminDelNodeActivity.class);
				t.putExtra("selectedModuleId", selectedModuleId);
        		t.putExtra("selectedModuleName", selectedModuleName);
        		t.putExtra("selectedSerieId", selectedSerieId);
        		t.putExtra("selectedSerieName", selectedSerieName);
        		t.putExtra("type", "Question");
				startActivity(t);
			}
		});
	    
	    
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_serie, menu);
		return true;
	}

	



}
