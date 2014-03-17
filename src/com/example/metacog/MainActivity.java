package com.example.metacog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

	private NodeList nodeModuleList = null;
	public ListView list = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        list = (ListView)findViewById(R.id.listView1);
        
        InputStream is = getResources().openRawResource(R.raw.modulelist);
        
        List<String> moduleList = new ArrayList<String>();
               
        try {
			Document xml = Utils.readXml(is);
			
			TextView t = (TextView)findViewById(R.id.textView4);
						
			nodeModuleList = xml.getElementsByTagName("modules");
						
			for (int i = 0; i < nodeModuleList.getLength(); ++i)
			{
				Element nodeModule = (Element) nodeModuleList.item(i);
				String nodeValue = nodeModule.getTextContent();
				moduleList.add(nodeValue);
			}
			
			
			// Create The Adapter with passing ArrayList as 3rd parameter
			ArrayAdapter<String> arrayAdapter = 
					new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,moduleList);
			
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
        
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){
        		Cursor c = (Cursor) arg0.getAdapter().getItem(position);
        		Intent second = new Intent(MainActivity.this,SecondActivity.class);
        		second.putExtra("textView1", c.getString(0));
        		startActivity(second);
        		
        	}    
        });
        
        
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
    
    
    
    
}
	