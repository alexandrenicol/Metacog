package com.example.metacog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

public class MainActivity extends Activity 
{ 
	private Button quit;
	private Button admin;
	private NodeList nodeSerie = null;
	private String[] list;
	private ListView moduleView;
/** Called when the activity is first created. */ 
@Override 
public void onCreate(Bundle savedInstanceState) 
{ 
super.onCreate(savedInstanceState); 
setContentView(R.layout.activity_main);
	moduleView = (ListView) findViewById(R.id.listView1);
	quit = (Button) findViewById(R.id.Quit);
	admin = (Button) findViewById(R.id.Admin);
	quit.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
	});
	
	admin.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent t = new Intent(MainActivity.this, AdminActivity.class);
			startActivity(t);
		}
		});
	
TabHost tabs=(TabHost)findViewById(R.id.tabhost); 
tabs.setup();

TabHost.TabSpec spec=tabs.newTabSpec("tag1"); 
spec.setContent(R.id.bouton1);
spec.setIndicator("Metacog");
 

tabs.addTab(spec); 
spec=tabs.newTabSpec("tag2"); 
spec.setContent(R.id.listView1);
spec.setIndicator("Modules"); 

tabs.addTab(spec); 
tabs.setCurrentTab(0); 

InputStream is = getResources().openRawResource(R.raw.modules);
try {
	Document xml = Utils.readXml(is);
	nodeSerie = xml.getElementsByTagName("module");
	
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

List<String> tmp = new ArrayList<String>();
for (int i = 0; i< nodeSerie.getLength();i++){
	tmp.add(nodeSerie.item(i).getTextContent());
}

list = new String[tmp.size()];
tmp.toArray(list);
moduleView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
moduleView.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	String modulechoisi = list[position];
    	Intent t = new Intent(MainActivity.this, SerieActivity.class);
    	//t.putExtra("module",modulechoisi);
		startActivity(t);
    }
});
}

    }


