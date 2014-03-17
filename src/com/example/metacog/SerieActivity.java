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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SerieActivity extends Activity{
	private Button quit = null;
	private ListView serieView = null;
	private NodeList nodeSerie = null;
	private String[] list;
	private String module;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        serieView = (ListView) findViewById(R.id.listView2);
        quit = (Button) findViewById(R.id.retourSerie);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
          });
        
       // Bundle extra = getIntent().getExtras();
       // module = extra.getString("module");
        module="module 1";
        InputStream is = getResources().openRawResource(R.raw.series);
        try {
			Document xml = Utils.readXml(is);
			nodeSerie = xml.getElementsByTagName("serie");
			
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
        
       // File dir = new File("C:/workspace/meta/res/raw/"+module);
       // final String items[] = dir.list();
        serieView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
        serieView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            	String seriechoisi = list[position];
            	Intent t = new Intent(SerieActivity.this, QuestionActivity.class);
            	//t.putExtra("serie",seriechoisi);
            	//t.putExtra("module",module);
				startActivity(t);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}