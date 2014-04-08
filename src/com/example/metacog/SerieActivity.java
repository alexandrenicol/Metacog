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
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SerieActivity extends Activity{
	private Button quit = null;
	private ListView serieView = null;
	private NodeList nodeSerie = null;
	private Element elModule = null;
	private String[] list;
	private String module;
	private Integer moduleId;
	private String consigne;
	private TextView consigneView;
	private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        serieView = (ListView) findViewById(R.id.SerieActivity_listView);
        consigneView = (TextView) findViewById(R.id.SerieActivity_consigne);
        quit = (Button) findViewById(R.id.retourSerie);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
          });
        
        Bundle extra = getIntent().getExtras();
        module = extra.getString("module");
        moduleId = extra.getInt("moduleId");
        name=extra.getString("name");

        InputStream is = getResources().openRawResource(R.raw.modules);
        try {
			Document xml = Utils.readXml(is);
			elModule = xml.getElementById("m"+moduleId.toString());
			nodeSerie = elModule.getChildNodes();
			consigne = elModule.getAttribute("consigne");
			
			
			
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
        consigneView.setText("Consigne : "+consigne);
        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i< nodeSerie.getLength();i++){
        	if (nodeSerie.item(i).getNodeType()== Node.ELEMENT_NODE){
				Element elementSerie = (Element) nodeSerie.item(i);
				//String nodeValue = nodeModule.getTextContent();
				
				String nodeValue = elementSerie.getAttribute("name");
				tmp.add(nodeValue);
			}
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
            		
            	if(moduleId == 1){
        /*            TextView text = (TextView) findViewById(R.id.textView1);
                    text.setText(moduleId.toString());*/
            		
            		Intent t = new Intent(SerieActivity.this, QuestionActivity.class);
            		t.putExtra("serie",seriechoisi);
                	t.putExtra("module",module);
                	t.putExtra("id_module",moduleId);
                	t.putExtra("id_serie", position+1);
                	t.putExtra("name", name);
    				startActivity(t);
            	}else if(moduleId == 2){
            		Intent t = new Intent(SerieActivity.this, SauterConcluActivity.class);
            		t.putExtra("serie",seriechoisi);
                	t.putExtra("module",module);
                	t.putExtra("id_module",moduleId);
                	t.putExtra("id_serie", position+1);
                	t.putExtra("name", name);
    				startActivity(t);
            	}
            	
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