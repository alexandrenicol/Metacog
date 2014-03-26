package com.example.metacog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class AdminModuleActivity extends Activity {

	private TextView text1;
	private String[] list2;
	private Integer selectedModuleId;
	private String selectedModuleName;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_admin_module);
	    
	    text1 = (TextView) findViewById(R.id.textView1);
	    
	    Bundle extras = getIntent().getExtras();
	    //text1.setText(extras.getString("textView1"));
	    
	    selectedModuleId = extras.getInt("id_module");
	    selectedModuleName = extras.getString("name_module");
	    
	    String selectedModule = "m"+selectedModuleId;
	    text1.setText(selectedModuleName);

	    ListView list = (ListView)findViewById(R.id.listView1);
        
        InputStream is = getResources().openRawResource(R.raw.modules);
        
        List<String> serieList = new ArrayList<String>();

	    
		try {
			Document xml = Utils.readXml(is);
			
			TextView t = (TextView)findViewById(R.id.textView4);
						
			Element nodeModule = xml.getElementById(selectedModule);
			NodeList nodeSerieList = nodeModule.getChildNodes();

			for (int i = 0; i < nodeSerieList.getLength(); ++i)
			{
				if (nodeSerieList.item(i).getNodeType()== Node.ELEMENT_NODE){
					Element nodeSerie = (Element) nodeSerieList.item(i);
					
					String nodeValue = nodeSerie.getAttribute("name");
					serieList.add(nodeValue);
				}
				
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
        		third.putExtra("selectedModuleId", selectedModuleId);
        		third.putExtra("selectedModuleName", selectedModuleName);
        		third.putExtra("selectedSerieId", position+1);
        		third.putExtra("selectedSerieName", serieChoisi);
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
