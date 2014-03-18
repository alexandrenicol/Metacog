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
	    //text1.setText(extras.getString("textView1"));
	    
	    String selectedModule = extras.getString("textView1");

	    list = (ListView)findViewById(R.id.listView1);
        
        InputStream is = getResources().openRawResource(R.raw.modules);
        
        List<String> serieList = new ArrayList<String>();

	    try {
			Document xml = Utils.readXml(is);
			
			TextView t = (TextView)findViewById(R.id.textView4);
						
			nodeModuleList = xml.getElementById(selectedModule);
			nodeSerieList = xml.getChildNotes()

			for (int i = 0; i < nodeSerieList.getLength(); ++i)
			{
				Element nodeSerie = (Element) nodeSerieList.item(i);
				//String nodeValue = nodeModule.getTextContent();
				String nodeValue = nodeSerie.getAttribute("id");
				serieList.add(nodeValue);
			}
			
			list2 = new String[serieList.size()];
			serieList.toArray(list2);
			
			// Create The Adapter with passing ArrayList as 3rd parameter
			ArrayAdapter<String> arrayAdapter = 
					new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
			
            // Set The Adapter
            list.setAdapter(arrayAdapter); 
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // TODO Auto-generated method stub

	    list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
        		//Cursor c = (Cursor) arg0.getAdapter().getItem(position); //WRONG
				String serieChoisi = list2[position];
        		Intent third = new Intent(AdminModuleActivity.this,AdminSerieActivity.class);
        		second.putExtra("serieChoisi", serieChoisi);
        		startActivity(third);
        		
        	}    
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin_module, menu);
		return true;
	}

	

	

}
