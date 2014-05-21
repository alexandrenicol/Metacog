package com.example.metacog;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class EndExerciceActivity extends Activity {
	
	private Button btDontSave;
	private Button btSave ;
	private Integer id_module;
	private String module_name;
	private String name;
	private String filePath;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_exercice);
		btDontSave = (Button) findViewById(R.id.dontSave);
		btSave = (Button) findViewById(R.id.save);
		
		Bundle extra = getIntent().getExtras();
		module_name = extra.getString("module");
		id_module = extra.getInt("moduleId");
		name = extra.getString("name");
	    
		btDontSave.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			// TODO Auto-generated method stub
    			DeleteResultsFromXml();
    			Intent t=new Intent (EndExerciceActivity.this,SerieActivity.class);
				t.putExtra("moduleId",id_module);
		    	t.putExtra("module",module_name);
		    	t.putExtra("name",name);
				startActivity(t);
				finish();
    		}
    		});
	    
		btSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent t=new Intent (EndExerciceActivity.this,SerieActivity.class);
				t.putExtra("moduleId",id_module);
		    	t.putExtra("module",module_name);
		    	t.putExtra("name",name);
				startActivity(t);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_exercice, menu);
		return true;
	}
	
	private void DeleteResultsFromXml(){
		filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Metacog"+"/"+name.replace(" ", "_")+"_results.xml";
		File f = new File(filePath);
		Document xml = null;
		if(f.exists()){
			try {		
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		
				xml = docBuilder.parse(new File(filePath));	
				NodeList rootList = xml.getElementsByTagName("results");
				Element rootElement = (Element) rootList.item(0);			
				rootElement.removeChild(rootElement.getLastChild());
				
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
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = null;
			try {
				transformer = transformerFactory.newTransformer();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DOMSource source = new DOMSource(xml);
			StreamResult result = new StreamResult(filePath);
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
