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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ModulesActivity extends Activity {
	private Button changeUser;
	private Button quitter ;
	private NodeList nodeSerie = null;
	private String[] list = null;
	private String[] listId = null;
	private ListView moduleView;
	private TextView bonjour;
	private String bonjourName;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_modules);
	    
	    quitter = (Button) findViewById(R.id.quitModule);
	    changeUser = (Button) findViewById(R.id.changeUser);
	    bonjour = (TextView) findViewById(R.id.bonjour);
	    
	    Bundle extra=getIntent().getExtras();
	    bonjourName="Bonjour "+ extra.getString("name");
	    bonjour.setText(bonjourName);
	    
	    quitter.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			Intent t=new Intent (ModulesActivity.this,MainActivity.class);
				startActivity(t);
    		}
    		});
	    
	    changeUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t=new Intent (ModulesActivity.this,LogActivity.class);
				startActivity(t);
				
			}
		});
	
	
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
	listId = new String[nodeSerie.getLength()];
	for (int i = 0; i< nodeSerie.getLength();i++){
		if (nodeSerie.item(i).getNodeType()== Node.ELEMENT_NODE){
			Element aNode = (Element) nodeSerie.item(i);
			//String nodeValue = nodeModule.getTextContent();
			String nodeValue = aNode.getAttribute("name");
			String nodeId = aNode.getAttribute("id");
			//tmp.add(nodeSerie.item(i).getTextContent());
			listId[i]=nodeId;
			tmp.add(nodeValue);
		}
	}

	list = new String[tmp.size()];
	tmp.toArray(list);
	moduleView = (ListView) findViewById(R.id.listModule);
    moduleView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
	moduleView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	    	String moduleIdchoisi = listId[position];
	    	String modulechoisi = list[position];
	    	Intent t = new Intent(ModulesActivity.this, SerieActivity.class);
	    	t.putExtra("moduleId",moduleIdchoisi);
	    	t.putExtra("module",modulechoisi);
			startActivity(t);
	    }
	});
	}

	
}
	    
	  